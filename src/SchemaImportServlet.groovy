import com.careerjinn.search.JobIndexImporter
import com.careerjinn.search.SearchIndex
import com.google.appengine.api.search.Document
import com.google.appengine.api.search.Query
import com.google.appengine.api.search.QueryOptions
import com.google.appengine.api.search.Results
import com.google.appengine.api.search.ScoredDocument
import groovy.util.slurpersupport.GPathResult

import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse;

import com.careerjinn.search.JobDocument
import com.google.appengine.api.search.PutException

import java.text.SimpleDateFormat

/**
 * Job loader, takes RSS feeds from The IT Job Board and CV Library and adds them to the search index for Job Search 2.0
 *
 * @author David Long
 * Date: 29/01/13
 * Time: 19:55
 */
class SchemaImportServlet extends HttpServlet {

    private JobIndexImporter importer = new JobIndexImporter();

    public void doGet(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws IOException, MalformedURLException {
        switch (httpRequest.getParameter("Site")) {
            case 'clear':
                clearIndex()
                break;
            case 'itjblite':
            case 'itjb':
                processItjb();
                break;
            case 'cvlibrary':
                processCvLibrary();
                break;
        }

        importer.updateSkills();
    }

    private void clearIndex() {
        String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        try {
            while (true) {

                // Return a set of document IDs.
                QueryOptions options = QueryOptions.newBuilder()
                        .setReturningIdsOnly(true)
                        .setLimit(1000)
                        .setNumberFoundAccuracy(10000).build();
                Query query = Query.newBuilder().setOptions(options).build("expires < " + today);
                Results<ScoredDocument> response = SearchIndex.getIndex().search(query);
                if (response.getResults().isEmpty()) {
                    System.out.println("no results");
                    break;
                }
                int count = 1;
                List<String> docIds = new ArrayList<String>();
                for (Document doc : response) {
                    if (count >= 200) {
                        SearchIndex.getIndex().delete(docIds);
                        docIds.clear();
                        count = 1;
                    }
                    docIds.add(doc.getId());
                    count++;
                }
                if (!docIds.isEmpty()) {
                    SearchIndex.getIndex().delete(docIds);
                }
            }
        } catch (RuntimeException e) {
        }
    }

    private void processItjb() {
        //the it job board
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.add(Calendar.DAY_OF_MONTH, 7);
        Date expires = calendar.getTime();

        URL url = new URL("http://www.theitjobboard.co.uk/monitor/ads.php");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(60000);
        conn.setReadTimeout(60000);

        GPathResult adverts = new XmlSlurper().parse(new InputStreamReader(conn.getInputStream(), "UTF-8"));

        for (int i = 0; i < adverts.advert.size(); i++) {
            //noinspection GroovyAssignabilityCheck
            GPathResult xmlJob = adverts.advert.getAt(i);
            String vendor = xmlJob."contact_office".text();
            if (vendor.isEmpty()) {
                vendor = "The IT Job Board";
            }
            JobDocument job = new JobDocument();
            job.setTitle(xmlJob.title.text())
                    .setContent(xmlJob."advert_body".text())
                    .setSalary(0.00, 0.00, xmlJob.salary.text())
                    .setLocation(xmlJob."free_location".text())
                    .setVendor(vendor)
                    .setAdded(new Date())
                    .setExpires(expires)
                    .setLink(xmlJob."url".text());
            try {
                // Put the document.
                importer.addToIndex(job, expires);
            } catch (PutException e) {
                // log import errors
                e.getOperationResult().getCode();
            }
        }
    }

    private void processCvLibrary() {
        URL url = new URL("http://www.cv-library.co.uk/cgi-bin/jobs.rss?fn=CV-library.co.uk%20-%20UK%20Jobs%20%28IT%29&industry=IT&area=UK&posted=1&search=1&order=sm&page=IT+Jobs&q=IT&add_ind_no_q=IT");

        GPathResult jobs = new XmlSlurper().parse(new InputStreamReader(url.openStream(), "UTF-8"));

        GregorianCalendar calendar = new GregorianCalendar();
        calendar.add(Calendar.DAY_OF_MONTH, 7);
        Date expires = calendar.getTime();

        for (int i = 0; i < jobs.channel.item.size(); i++) {
            //noinspection GroovyAssignabilityCheck
            GPathResult xmlJob = jobs.channel.item.getAt(i);

            JobDocument job = new JobDocument();
            String description = xmlJob.description.text();
            String title = xmlJob.title.text();
            def titleParser = /(?msi)(.*),([^,]+)$/
            def titleMatcher = (title =~ titleParser);
            String vendor = "CV Library";
            job.setTitle(titleMatcher[0][1])
                    .setContent(description)
                    .setSalary(0.00, 0.00, "0")
                    .setLocation(titleMatcher[0][2])
                    .setVendor(vendor)
                    .setAdded(new Date())
                    .setExpires(expires)
                    .setLink(xmlJob.link.text());
            try {
                // Put the document.
                importer.addToIndex(job, expires);
            } catch (PutException e) {
                // log import errors
                e.getOperationResult().getCode();
            }
        }
    }
}