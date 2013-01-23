import javax.servlet.http.*
import com.careerjinn.http.*

class ControllerServlet extends HttpServlet {
    void doGet( HttpServletRequest httpRequest, HttpServletResponse httpResponse ) throws IOException {

        ThemeWriter themeWriter = new ThemeWriter( httpResponse );
        themeWriter.generateResponseHeader();
        themeWriter.displayTemplate();
    }
}