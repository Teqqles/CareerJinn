package com.careerjinn.page

import com.careerjinn.http.ThemeWriter
import com.careerjinn.search.SaveSearch
import com.careerjinn.search.SearchParameters
import com.careerjinn.skill.SkillList
import com.google.appengine.api.datastore.Entity

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * @author David Long
 * Date: 27/04/13
 * Time: 15:39
  */
class TreePage implements Page {
    private HttpServletRequest httpRequest;
    private HttpServletResponse httpResponse;
    private SearchParameters params;
    private def properties;

    public TreePage() {

        //set some default properties so Groovy does not fail if none are set.
        properties = [
                keywords: "",
                joiner: "",
                location: ""
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
        params
                .setKeywords( httpRequest.getParameter( "q" ) )  // query/keywords
                .setLocation( httpRequest.getParameter( "l" ) ); // location

        properties += params.getProperties();
        properties += addTermJoiner( params );
        properties += [ savedSearches: SaveSearch.renderSavedSearches( httpRequest ) ];
        properties += [ related: getRelatedSkills( params.getKeywords() ) ]

        ThemeWriter themeWriter = new ThemeWriter( httpResponse );

        themeWriter.generateResponseHeader();
        themeWriter.displayTemplate( "tree.html", properties );
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
                    relatedSkills.put( URLEncoder.encode( individualSkill, "UTF-8" ), [ name: individualSkill,
                                                                                        count: getSkillJobCount( individualSkill ) ] );
                }
            }
        }
        relatedSkills.sort {  a, b -> a.value.count <=> b.value.count }
        return relatedSkills;
    }

    private int getSkillJobCount( String skillText ) {
        skillText = skillText.toLowerCase();
        SkillList list = new SkillList();
        List<Entity> filteredList = list.filter( skillText );
        for( Entity skill: filteredList ){
            return (int)skill.getProperty( "count" );
        }
        return 0;
    }

    private static LinkedHashMap addTermJoiner( SearchParameters searchParameters ) {
        if ( searchParameters.getKeywords() != "" && searchParameters.getLocation() != "" ) {
            return [ joiner: "in" ];
        }
        return [];
    }

}
