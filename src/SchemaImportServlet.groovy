import com.careerjinn.search.JobIndexImporter
import groovy.util.slurpersupport.GPathResult

import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse;

import com.careerjinn.search.JobDocument
import com.google.appengine.api.search.PutException

/**
 * @author Dave Long
 * Date: 29/01/13
 * Time: 19:55
 */
class SchemaImportServlet extends HttpServlet {
    void doGet( HttpServletRequest httpRequest, HttpServletResponse httpResponse ) throws IOException, MalformedURLException {

        processCvLibrary();
        processItjb();
    }

    private void processItjb() {
        //the it job board
        for ( int j = 1; j<= 2; j++ ) { //perm & contract jobs
            URL url = new URL("http://www.theitjobboard.co.uk/rss/all-jobs/all-locations/en/jobs-feed.xml?xc=247&WT.mc_id=R104&JobTypeFilter=" + j );

            GPathResult jobs = new XmlSlurper().parse( new InputStreamReader( url.openStream(), "UTF-8" ) );

            for( int i = 0; i < jobs.channel.item.size() && i < 100; i++ ) {
                //noinspection GroovyAssignabilityCheck
                GPathResult xmlJob = jobs.channel.item.getAt( i );

                JobIndexImporter importer = new JobIndexImporter();
                JobDocument job = new JobDocument();
                String description = xmlJob.description.text()
                def descriptionParser = /(?msi)Job Description\s*:\s*(.*)Advertiser\s*:\s*([^<]*).*Location\s*:\s*([^<]*).*Salary\s*:\s*([^<]*).*/
                def descriptionMatcher = ( description =~ descriptionParser );
                String vendor = descriptionMatcher[0][2];
                if ( vendor == "" ) {
                    vendor = "The IT Job Board";
                }
                job.setTitle( xmlJob.title.text() )
                        .setContent( descriptionMatcher[0][1] )
                        .setSalary( 0.00, 0.00, "0" )//not used by The IT Job Board feed
                        .setLocation( descriptionMatcher[0][3] )
                        .setVendor( vendor )
                        .setAdded( new Date() )
                        .setExpires( new Date() )
                        .setLink( xmlJob.link.text() );
                try {
                    // Put the document.
                    importer.addToIndex( job );
                } catch ( PutException e ) {
                    // log import errors
                    e.getOperationResult().getCode();
                }
            }
        }
    }

    private void processCvLibrary() {
        URL url = new URL("http://www.cv-library.co.uk/cgi-bin/jobs.rss?fn=CV-library.co.uk%20-%20UK%20Jobs%20%28IT%29&industry=IT&area=UK&search=1&order=sm&page=IT+Jobs&q=IT&add_ind_no_q=IT" );

        GPathResult jobs = new XmlSlurper().parse( new InputStreamReader( url.openStream(), "UTF-8" ) );

        for( int i = 0; i < jobs.channel.item.size() && i < 100; i++ ) {
            //noinspection GroovyAssignabilityCheck
            GPathResult xmlJob = jobs.channel.item.getAt( i );

            JobIndexImporter importer = new JobIndexImporter();
            JobDocument job = new JobDocument();
            String description = xmlJob.description.text();
            String title = xmlJob.title.text();
            def titleParser = /(?msi)(.*),([^,]+)$/
            def titleMatcher = ( title =~ titleParser );
            String vendor = "CV Library";
            job.setTitle( titleMatcher[0][1] )
                    .setContent( description )
                    .setSalary( 0.00, 0.00, "0" )
                    .setLocation( titleMatcher[0][2] )
                    .setVendor( vendor )
                    .setAdded( new Date() )
                    .setExpires( new Date() )
                    .setLink( xmlJob.link.text() );
            try {
                // Put the document.
                importer.addToIndex( job );
            } catch ( PutException e ) {
                // log import errors
                e.getOperationResult().getCode();
            }
        }
    }
}