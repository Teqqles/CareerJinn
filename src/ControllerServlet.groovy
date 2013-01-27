import com.careerjinn.page.Page
import com.careerjinn.page.PageFactory

import javax.servlet.http.*

class ControllerServlet extends HttpServlet {
    void doGet( HttpServletRequest httpRequest, HttpServletResponse httpResponse ) throws IOException {

        Page page = PageFactory.createPage( httpRequest.getParameter( "Page" ) );
        page.setHttpRequest( httpRequest );
        page.setHttpResponse( httpResponse );
        page.renderPage();

    }
}