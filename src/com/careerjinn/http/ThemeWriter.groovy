package com.careerjinn.http

import javax.servlet.http.*
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

    public ThemeWriter setHttpResponse( HttpServletResponse httpResponse ) {
        this.httpResponse = httpResponse;
    }

    public void generateResponse() {

    }


}
