package com.careerjinn.search

import com.google.appengine.api.search.Cursor;
import com.google.appengine.api.search.Query;
import com.google.appengine.api.search.QueryOptions;
import com.google.appengine.api.search.Results;
import com.google.appengine.api.search.SearchException;
import com.google.appengine.api.search.ScoredDocument;

/**
 * @author David Long
 * Date: 18/04/13
 * Time: 20:52
 */
class FindDocuments {

    public static Results<ScoredDocument> findDocuments( String queryString, int limit, Cursor cursor ) {
        try {
            QueryOptions options = QueryOptions.newBuilder()
                    .setLimit(limit)
                    .setFieldsToReturn( "title", "vendor", "location", "displayContent", "link", "added" )
                    .setCursor(cursor)
                    .build();
            Query query = Query.newBuilder().setOptions(options).build( queryString );
            return SearchIndex.getIndex().search(query);
        } catch (SearchException e) {
            return null;
        }
    }
}
