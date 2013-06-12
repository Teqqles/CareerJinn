package com.careerjinn.search

import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * @author David Long
 * Date: 17/04/13
 * Time: 21:06
  */
class SaveSearch {

    private SearchParameters parameters;
    private final static String SAVED_SEARCH_COOKIE = "SS";

    /**
     * Add a search parameters object
     *
     * @param params
     * @return SaveSearch
     */
    public SaveSearch addParameters( SearchParameters params ) {
        parameters = params;
        return this;
    }

    /**
     * Save a search to the users cookie
     *
     * @param request
     * @param response
     *
     * @return Boolean
     */
    public Boolean save( HttpServletRequest request, HttpServletResponse response ) {
        String existingSearches = getCookieContent(request.getCookies(), SAVED_SEARCH_COOKIE );
        if ( existingSearches != "" ) {
            //add the search divider if we are adding multiple searches
            existingSearches += ":";
        }
        //do not insert duplicates
        if ( existingSearches.contains( parameters.cookieCode() ) ) {
            return false;
        }
        response.addCookie( new Cookie( SAVED_SEARCH_COOKIE, existingSearches + parameters.cookieCode() ) );
        return true;
    }

    /**
     * Load all cookie saved searches into search parameter objects and return the list
     *
     * @param request
     * @return List<SearchParameters>
     */
    public List<SearchParameters> load( HttpServletRequest request ) {
        String existingSearches = getCookieContent(request.getCookies(), SAVED_SEARCH_COOKIE );
        String[] rawSearches = existingSearches.split( ":" );
        List<SearchParameters> searchParameterList = new ArrayList<SearchParameters>();
        //do we have cookie entries?
        if ( existingSearches == "" ) {
            return searchParameterList;
        }
        for( int i = 0; i < rawSearches.length; i++ ) {
            SearchParameters params = new SearchParameters();
            searchParameterList.add( params.convertFromString( rawSearches[i] ) );
        }
        return searchParameterList;
    }

    /**
     * Extract a named cookie from the list provided and return the value as a string
     *
     * @param cookies
     * @param cookieName
     * @return String
     */
    public static String getCookieContent( Cookie[] cookies, String cookieName ) {
        if (!cookies ) {
            return ""; //no cookie, so return an empty string
        }
        for( int i=0; i < cookies.length; i++ ) {
            Cookie cookie = cookies[i];
            if ( cookieName == cookie.getName() ) {
                return cookie.getValue();
            }
        }
        return "";
    }

    /**
     * Render the saved searches into a HTML string
     *
     * @param request
     * @return String
     */
    public static String renderSavedSearches( HttpServletRequest request ) {
        String savedSearchRender = "";
        SaveSearch savedSearch = new SaveSearch();
        List<SearchParameters> searchList = savedSearch.load( request );
        searchList = searchList.reverse();//place the newest saved search on top
        for ( SearchParameters params: searchList ) {
            savedSearchRender += '<li><a href="?Page=Search&amp;' + params.cookieCode().replace( "&", "&amp;" ) +  '">' + params + '</a></li>';
        }
        return savedSearchRender;
    }

}
