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

    public SearchParameters setKeywords( String keywords ) {
        this.keywords = HtmlEncode.decode( removeKeywordsDefaults( keywords ) );
        return this;
    }

    public String getKeywords() {
        return keywords;
    }

    public SearchParameters setLocation( String location ) {
        this.location = HtmlEncode.decode( removeLocationDefaults( location ) );
        return this;
    }

    public String getLocation() {
        return location;
    }

    public LinkedHashMap getProperties() {
        System.out.println( this.keywords );
        return [ keywords: HtmlEncode.encode( this.keywords ),
                 location: HtmlEncode.encode( this.location ),
                 saveSearchUrl: cookieCode()
        ];
    }

    private static String removeKeywordsDefaults( String keywords ) {
        if ( keywords == "Job Title / Skills / Company Name" ) {
            keywords = "";
        }
        return keywords;
    }

    private static String removeLocationDefaults( String location ) {
        if( location == "City / Country / Post Code" ) {
            location = "";
        }
        return location;
    }

    public String cookieCode() {
        return "q=" + URLEncoder.encode( keywords, "UTF-8" ) + "&l=" + URLEncoder.encode( location, "UTF-8" );
    }

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
     * Overwritten to generate friendly display
     *
     * @return String
     */
    @Override
    public String toString() {
        String params = "";
        if ( keywords != "" ) {
            params += keywords;
            params += " ";
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
