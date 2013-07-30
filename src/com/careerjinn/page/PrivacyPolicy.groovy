package com.careerjinn.page

import com.careerjinn.http.ThemeWriter
import com.careerjinn.search.SaveSearch

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * @author David Long
 * Date: 30/07/13
 * Time: 17:57
 */
class PrivacyPolicy implements Page {

    private HttpServletRequest httpRequest;
    private HttpServletResponse httpResponse;

    /**
     * setHttpRequest
     *
     * Sets the HTTP request object ready for use in page rendering
     *
     * @param servletRequest
     * @return Page
     */
    @Override
    Page setHttpRequest(HttpServletRequest servletRequest) {
        httpRequest = servletRequest;
        return this;
    }

    /**
     * setHttpResponse
     *
     * Sets the HTTP response object ready for use in page rendering
     *
     * @param servletResponse
     * @return Page
     */
    @Override
    Page setHttpResponse(HttpServletResponse servletResponse) {
        httpResponse = servletResponse;
        return this;
    }

    /**
     * renderPage
     *
     * Displays the homepage template complete with any saved searches from the previous visits
     */
    @Override
    void renderPage() {
        String hiddenSearchSection = "hide";
        String savedSearches = SaveSearch.renderSavedSearches( httpRequest );
        if (savedSearches != "" ) {
            hiddenSearchSection = "";
        }
        ThemeWriter themeWriter = new ThemeWriter( httpResponse );
        themeWriter.generateResponseHeader();
        themeWriter.displayTemplate( "policy.html", [ savedSearches: savedSearches,
                hiddenSearchSection: hiddenSearchSection ] );
    }
}
