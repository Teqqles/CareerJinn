package com.careerjinn.page

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * @author Dave Long
 * Date: 27/01/13
 * Time: 17:54
 */
public interface Page {
    public Page setHttpRequest( HttpServletRequest servletRequest );
    public Page setHttpResponse( HttpServletResponse servletResponse );
    public void renderPage();
}