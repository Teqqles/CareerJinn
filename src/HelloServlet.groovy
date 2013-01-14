import javax.servlet.http.*

class HelloServlet extends HttpServlet {
    void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.contentType = "text/plain"
        resp.writer.println "Hello Google App Engine Groovy!"
    }
}