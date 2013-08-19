package com.careerjinn.search

import com.careerjinn.page.HtmlEncode

import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * @author David Long
 * Date: 17/04/13
 * Time: 19:53
 */
class SearchParameters {

    private String keywords = "";
    private String location = "";

    /**
     * setKeywords
     *
     * Sets the keywords string for the search query
     *
     * @param keywords
     * @return SearchParameters
     */
    public SearchParameters setKeywords( String keywords ) {
        this.keywords = HtmlEncode.decode( removeKeywordsDefaults( keywords ) );
        return this;
    }

    /**
     * getKeywords
     *
     * Returns a HTML decoded version of the search keywords string
     *
     * @return String
     */
    public String getKeywords() {
        return keywords;
    }

    /**
     * setLocation
     *
     * Sets the location which the user is searching
     *
     * @param location
     * @return SearchParameters
     */
    public SearchParameters setLocation( String location ) {
        this.location = HtmlEncode.decode( removeLocationDefaults( location ) );
        return this;
    }

    /**
     * getLocation
     *
     * Retrieves a HTML decoded version of the search location
     *
     * @return String
     */
    public String getLocation() {
        return location;
    }

    /**
     *
     * @return
     */
    public LinkedHashMap getProperties() {
        return [ keywords: HtmlEncode.encode( this.keywords ),
                 location: HtmlEncode.encode( this.location ),
                 saveSearchUrl: cookieCode().replace( "&", "&amp;" )
        ];
    }

    private static String removeKeywordsDefaults( String keywords ) {
        if ( keywords == "Job Title / Skills / Company Name" ) {
            keywords = "";
        }
        return keywords;
    }

    private static String removeLocationDefaults( String location ) {
        if( location == "City / Country / Postcode" ) {
            location = "";
        }
        return location;
    }

    /**
     * cookieCode
     *
     * Generates the string to be used in cookie storage of search parameters
     *
     * @return String
     */
    public String cookieCode() {
        return "q=" + URLEncoder.encode( keywords, "UTF-8" ) + "&l=" + URLEncoder.encode( location, "UTF-8" );
    }

    /**
     * convertFromString
     *
     * Extracts the keyword and location from a string.  Useful for building SearchParameters
     * from cookie storage
     *
     * @param rawSearchParameters
     * @return
     */
    public SearchParameters convertFromString( String rawSearchParameters ) {
        setKeywords(
                URLDecoder.decode(
                        extractParameterFromString( 'q=([^&]*)&', rawSearchParameters ),
                        "UTF-8" ) );
        setLocation(
                URLDecoder.decode(
                        extractParameterFromString( 'l=(.*)', rawSearchParameters ),
                        "UTF-8" ) );
        return this;
    }

    private static String extractParameterFromString( String rule, String rawParameter ) {
        Pattern pattern = Pattern.compile( rule );
        Matcher matcher = pattern.matcher( rawParameter );
        if ( !matcher.find() ) {
            return "";
        }
        return matcher.group( 1 );
    }

    /**
     * toString
     *
     * Overwritten to generate friendly display and simplify pages using the display mechanic
     *
     * @return String
     */
    @Override
    public String toString() {
        String params = "";
        if ( keywords != "" ) {
            params += keywords;
            params += " Jobs ";
        } else {
            params += "All Jobs ";
        }
        if ( params != "" && location != "" ) {
            params += "in ";
        }
        if ( location != "" ) {
            params += location;
        }
        return params;
    }
}
