package com.careerjinn.page

import com.careerjinn.search.SaveSearch
import com.careerjinn.search.SearchParameters

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * @author David Long
 * Date: 27/01/13
 * Time: 17:57
 */
class SaveSearchPage implements Page {

    private HttpServletRequest httpRequest;
    private HttpServletResponse httpResponse;
    private SearchParameters params;

    /**
     * setHttpRequest
     *
     * Sets the HTTP request object ready for use in page rendering
     *
     * @param servletRequest
     * @return Page
     */
    @Override
    Page setHttpRequest( HttpServletRequest servletRequest ) {
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
    Page setHttpResponse( HttpServletResponse servletResponse ) {
        httpResponse = servletResponse;
        return this;
    }

    /**
     * renderPage
     *
     * Adds the save search to a cookie on the users machine and then redirects back to the search results page
     */
    @Override
    void renderPage() {

        params = new SearchParameters();
        params
                .setKeywords( httpRequest.getParameter( "q" ) )  // query/keywords
                .setLocation( httpRequest.getParameter( "l" ) ); // location

        SaveSearch saveSearch = new SaveSearch();
        saveSearch.addParameters( params );
        saveSearch.save( httpRequest, httpResponse );

        //redirect back to search page
        httpResponse.sendRedirect( "?Page=Search&" + params.cookieCode() );
    }

}

