package com.careerjinn.page

import com.careerjinn.http.ThemeWriter
import com.careerjinn.search.SaveSearch

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * @author David Long
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
        String hiddenSearchSection = "hide";
        String savedSearches = SaveSearch.renderSavedSearches( httpRequest );
        if (savedSearches != "" ) {
            hiddenSearchSection = "";
        }
        ThemeWriter themeWriter = new ThemeWriter( httpResponse );
        themeWriter.generateResponseHeader();
        themeWriter.displayTemplate( "home.html", [ savedSearches: savedSearches,
                                                    hiddenSearchSection: hiddenSearchSection ] );
    }
}
