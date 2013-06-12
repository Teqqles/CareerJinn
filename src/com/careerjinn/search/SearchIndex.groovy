package com.careerjinn.search

import com.google.appengine.api.search.Index
import com.google.appengine.api.search.IndexSpec
import com.google.appengine.api.search.SearchServiceFactory

/**
 * Utility class for retrieving the job search index from Google
 *
 * @author David Long
 * Date: 18/04/13
 * Time: 21:01
 */
class SearchIndex {
    public static Index getIndex() {
        IndexSpec indexSpec = IndexSpec.newBuilder().setName("jobSearch").build();
        return SearchServiceFactory.getSearchService().getIndex( indexSpec );
    }
}
