package com.careerjinn.page

import com.careerjinn.http.ThemeWriter

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * @author Dave Long
 * Date: 27/01/13
 * Time: 17:57
 */
class HomePage implements Page {

    private HttpServletRequest httpRequest;
    private HttpServletResponse httpResponse;

    @Override
    Page setHttpRequest(HttpServletRequest servletRequest) {
        httpRequest = servletRequest;
        return this;
    }

    @Override
    Page setHttpResponse(HttpServletResponse servletResponse) {
        httpResponse = servletResponse;
        return this;
    }

    @Override
    void renderPage() {
        ThemeWriter themeWriter = new ThemeWriter( httpResponse );
        themeWriter.generateResponseHeader();
        themeWriter.displayTemplate();
    }
}
