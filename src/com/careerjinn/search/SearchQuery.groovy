package com.careerjinn.search

import java.text.SimpleDateFormat

/**
 * @author David Long
 * Date: 18/04/13
 * Time: 22:13
 */
class SearchQuery {

    String keywords = "";
    String location = "";
    String query = "";

    /**
     * Constructs the search query string from a SearchParameters object
     *
     * @param params
     * @return String
     */
    public String buildSearchQuery( SearchParameters params ) {
        String today = new SimpleDateFormat("yyyy-MM-dd").format( new Date() );
        if( params.getKeywords() != "" ) {
            keywords = sanitizeTerms( params.getKeywords() );
            if ( keywords != "" ) {
                query    += "(title_content:(" + keywords + "))"; //a merged field containing title and content to simplify the search query
            }
        }
        if( params.getLocation() != "" ) {
            location = sanitizeTerms( params.getLocation() );
            if ( location != "" ) {
                if ( query != "" ) {
                    query += " AND ";
                }
                query += "location:(" + location + ")";
            }
        }
        if ( query != "" ) {
            query += " AND ";
        }
        query += "expires >= " + today;
        System.out.println( query );
        return query;
    }

    /**
     * Remove characters or character grouping which will break the Search API
     *
     * @param terms
     * @return String terms
     */
    public String sanitizeTerms( String terms ) {
        terms = terms.replace( "<", "" );
        terms = terms.replace( ">", "" );
        terms = removeUnclosedCharacter( terms, '"' as char );
        terms = removeUnclosedCharacter( terms, "'" as char );
        terms = removeUnclosedCharacter( terms, "(" as char, ")" as char );  // remove unmatched parenthesis
        terms = terms.replaceAll( '"\\s*"', '' ); // remove empty quoted strings
        terms = terms.replaceAll( '~([^"]|$)', '' ); // remove pluralisation if it does not occur before quotations
        terms = terms.replaceAll( "\\([^\\w]*\\)", '' ); // remove empty parenthesis
        //uppercase boolean log as per Google App Engine API rules
        terms = terms.replaceAll( "(?i)(\\b)(or)(\\b)", '$1OR$3' ); // remove empty parenthesis
        terms = terms.replaceAll( "(?i)(\\b)(and)(\\b)", '$1AND$3' ); // remove empty parenthesis
        terms = terms.replaceAll( "(?i)(\\b)(not)(\\b)", '$1NOT$3' ); // remove empty parenthesis
        terms = terms.replaceAll( "\\([^\\w]*\\)", '' ); // remove empty parenthesis
        return terms;
    }

    private String removeUnclosedCharacter( String terms, char character ) {
        StringBuilder removedTerms = new StringBuilder( terms );
        Boolean open = false;
        int lastOccurrence = 0;
        for( int i = 0; i < removedTerms.length(); i++ ) {
            if ( removedTerms.charAt( i ) == character ) {
                open = !open;
                lastOccurrence = i;
            }
        }
        if ( open ) {
            removedTerms.deleteCharAt( lastOccurrence );
        }
        return removedTerms;
    }

    private String removeUnclosedCharacter( String terms, char character, char closeCharacter ) {
        StringBuilder removedTerms = new StringBuilder( terms );
        int openedCharGroups = 0;
        int i = 0;
        while( i < removedTerms.length() ) {
            if ( closeCharacter == removedTerms.charAt( i ) ) {
                if ( openedCharGroups == 0 ) {
                    removedTerms.deleteCharAt( i );
                    continue;
                }  else {
                    openedCharGroups--;
                }
            } else if( character == removedTerms.charAt( i ) ){
                openedCharGroups++;
            }
            i++;
        }
        for( i = 0; i < openedCharGroups; i++ ) {
            removedTerms.append( ")" );
        }
        return removedTerms;
    }
}
