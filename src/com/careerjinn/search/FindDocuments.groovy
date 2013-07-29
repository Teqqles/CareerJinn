package com.careerjinn.search

import com.google.appengine.api.search.Cursor;
import com.google.appengine.api.search.Query;
import com.google.appengine.api.search.QueryOptions;
import com.google.appengine.api.search.Results;
import com.google.appengine.api.search.SearchException;
import com.google.appengine.api.search.ScoredDocument;

/**
 *
 * Wrapper for the Google Search API, takes the user query and returns search results where available
 *
 * @author David Long
 * Date: 18/04/13
 * Time: 20:52
 */
class FindDocuments {

    /**
     * Takes the user query, the number of results to return and a cursor.  Calls the Search API
     * with title, vendor, location, displayContent, link and added date and returns the result
     *
     * @param queryString
     * @param limit
     * @param cursor
     * @return Results<ScoredDocument>
     */
    public static Results<ScoredDocument> findDocuments( String queryString, int limit, int page ) {
        try {
            QueryOptions options = QueryOptions.newBuilder()
                    .setLimit(limit)
                    .setOffset( limit * (page - 1))
                    .setFieldsToReturn( "title", "vendor", "location", "displayContent", "link", "added" )
                    .build();
            Query query = Query.newBuilder().setOptions(options).build( queryString );
            return SearchIndex.getIndex().search(query);
        } catch (SearchException e) {
            return null;
        }
    }
}
