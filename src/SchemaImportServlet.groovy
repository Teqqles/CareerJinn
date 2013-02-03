import com.google.appengine.api.search.Index
import com.google.appengine.api.search.IndexSpec
import com.google.appengine.api.search.SearchServiceFactory

import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse;

import com.careerjinn.search.JobDocument
import com.google.appengine.api.search.Document
import com.google.appengine.api.search.PutException

/**
 * @author Dave Long
 * Date: 29/01/13
 * Time: 19:55
 */
class SchemaImportServlet extends HttpServlet {
    void doGet( HttpServletRequest httpRequest, HttpServletResponse httpResponse ) throws IOException {
        JobDocument job = new JobDocument();
        job.setTitle( "Job Title" )
           .setContent( "<html><head><title>Advert</title></head><body>content</body></html>" )
           .setSalary( 10000.00, 10000.00, "£10,000" )
           .setLocation( "London" )
           .setVendor( "The IT Job Board" )
           .setAdded( new Date() )
           .setExpires( new Date() );
        Document doc = job.build();
        try {
            // Put the document.
            getIndex().put( doc );
        } catch ( PutException e ) {
            // log import errors
            e.getOperationResult().getCode();
        }
    }

    @SuppressWarnings("GrMethodMayBeStatic")
    private Index getIndex() {
        IndexSpec indexSpec = IndexSpec.newBuilder().setName("jobSearch").build();
        return SearchServiceFactory.getSearchService().getIndex( indexSpec );
    }
}