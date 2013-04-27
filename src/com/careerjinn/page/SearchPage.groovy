package com.careerjinn.page

import com.careerjinn.http.ThemeWriter
import com.careerjinn.search.FindDocuments
import com.careerjinn.search.SaveSearch
import com.careerjinn.search.SearchParameters
import com.careerjinn.search.SearchQuery
import com.careerjinn.skill.SkillList
import com.google.appengine.api.datastore.Entity
import com.google.appengine.api.search.Cursor
import com.google.appengine.api.search.Document
import com.google.appengine.api.search.Field
import com.google.appengine.api.search.Results
import com.google.appengine.api.search.ScoredDocument

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * @author David Long
 * Date: 27/01/13
 * Time: 17:57
 */
class SearchPage implements Page {

    private HttpServletRequest httpRequest;
    private HttpServletResponse httpResponse;
    private SearchParameters params;
    private def properties;

    public SearchPage() {

        //set some default properties so Groovy does not fail if none are set.
        properties = [
                keywords: "",
                joiner: "",
                location: "",
                pageNumber: 0,
                pageSize: 0,
                pageCount: 0
        ];

    }

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

        params             = new SearchParameters();
        SearchQuery search = new SearchQuery();
        params
                .setKeywords( httpRequest.getParameter( "q" ) )  // query/keywords
                .setLocation( httpRequest.getParameter( "l" ) ); // location

        properties += params.getProperties();
        properties += addTermJoiner( params );
        properties += [ savedSearches: SaveSearch.renderSavedSearches( httpRequest ) ];

        Results<ScoredDocument> results;
        LinkedHashMap viewableResults = [];
        int resultCounter = 0;
        long resultCount = 0;
        long pageNumber = 0;
        long resultReturned = 0;
        try {
            results = FindDocuments.findDocuments( search.buildSearchQuery( params ), 20, Cursor.newBuilder().build() );
            resultCount = results.numberFound;
            resultReturned = results.numberReturned;
            pageNumber = 1;
            for (ScoredDocument document : results) {
                viewableResults.put( resultCounter++, resultDocumentConverter( document ) );
            }
        } catch ( Exception e ) {
            //log search errors
        }
        properties += [ results: viewableResults ];
        properties += [ related: getRelatedSkills( params.getKeywords() ) ]
        properties += [ pageCount: resultCount ];
        properties += [ pageSize: resultReturned ];
        properties += [ pageNumber: pageNumber ];

        ThemeWriter themeWriter = new ThemeWriter( httpResponse );

        themeWriter.generateResponseHeader();
        themeWriter.displayTemplate( "search.html", properties );
    }

    private LinkedHashMap getRelatedSkills( String keywords ) {
        keywords = keywords.toLowerCase();
        LinkedHashMap relatedSkills = [];
        SkillList list = new SkillList();
        List<Entity> filteredList = list.filter( keywords );
        for( Entity skill: filteredList ){
            String skillsField = (String)skill.getProperty( "related" );
            if ( skillsField ) {
                for( String individualSkill : skillsField.split( ',' ) ) {
                    relatedSkills.put( URLEncoder.encode( individualSkill, "UTF-8" ), individualSkill );
                }
            }
        }
        return relatedSkills;
    }

    private static LinkedHashMap addTermJoiner( SearchParameters searchParameters ) {
        if ( searchParameters.getKeywords() != "" && searchParameters.getLocation() != "" ) {
            return [ joiner: "in" ];
        }
        return [];
    }

    private LinkedHashMap resultDocumentConverter( Document doc ) {
        return [ name: extractDocumentField( doc, "title" ),
                 added: extractDocumentField( doc, "added" ),
                 content: extractDocumentField( doc, "displayContent" ),
                 location: extractDocumentField( doc, "location" ),
                 vendor: extractDocumentField( doc, "vendor" ),
                 link: extractDocumentField( doc, "link" ) ];
    }

    private String extractDocumentField( Document doc, String fieldName ) {
        if ( doc.getFieldCount(fieldName) == 1) {
            switch ( doc.getOnlyField(fieldName).getType() ) {
                case Field.FieldType.TEXT:
                    return HtmlEncode.encode( doc.getOnlyField(fieldName).getText() );
                case Field.FieldType.HTML:
                    return HtmlEncode.encode( doc.getOnlyField(fieldName).getHTML() );
                case Field.FieldType.ATOM:
                    return HtmlEncode.encode( doc.getOnlyField(fieldName).getAtom() );
                case Field.FieldType.DATE:
                    return doc.getOnlyField(fieldName).getDate();;
            }
        }

        return "";
    }

}
