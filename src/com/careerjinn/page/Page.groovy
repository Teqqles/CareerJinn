package com.careerjinn.page

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Interface to define the Page contract
 *
 * @author Dave Long
 * Date: 27/01/13
 * Time: 17:54
 */
public interface Page {

    /**
     * setHttpRequest
     *
     * Sets the HTTP request object ready for use in page rendering
     *
     * @param servletRequest
     * @return Page
     */
    public Page setHttpRequest( HttpServletRequest servletRequest );

    /**
     * setHttpResponse
     *
     * Sets the HTTP response object ready for use in page rendering
     *
     * @param servletResponse
     * @return Page
     */
    public Page setHttpResponse( HttpServletResponse servletResponse );

    /**
     * renderPage
     *
     * Allows custom page configuration and is the place page rendering should occur
     */
    public void renderPage();
}