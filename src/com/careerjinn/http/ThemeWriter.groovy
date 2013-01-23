package com.careerjinn.http

import javax.servlet.http.*
import groovy.text.SimpleTemplateEngine;
import groovy.text.Template;

/**
 * @author: David Long
 * Date: 21/01/13
 * Time: 13:45
 */
class ThemeWriter {

    private HttpServletResponse httpResponse;

    ThemeWriter( HttpServletResponse httpResponse ) {
        setHttpResponse( httpResponse );
    }

    private void setHttpResponse( HttpServletResponse httpResponse ) {
        this.httpResponse = httpResponse;
    }

    public void generateResponseHeader() {
        httpResponse.contentType = "text/html"
    }

    public void displayTemplate() {
        def binding = [favlang: "Groovy"]
        //def templateFile = new FileReader( "home.html" );
        //SimpleTemplateEngine engine = new SimpleTemplateEngine( )
        //def template = engine.createTemplate( templateFile ).make(binding)
        httpResponse.writer.print "test"//template.toString();
    }

    public HttpServletResponse getHttpResponse() {
        return httpResponse;
    }
}
