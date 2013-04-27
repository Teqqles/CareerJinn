package com.careerjinn.search

import com.google.appengine.api.datastore.Entity
import com.google.appengine.api.datastore.Query

/**
 * @author David Long
 * Date: 24/04/13
 * Time: 20:40
 */
class FindSkills {

    public List<Entity> SearchSkills( String search ) {
        Query skillQuery = new Query( "Skill" )
                .addFilter( "normal_name",
                Query.FilterOperator.EQUAL,
                search );
    }
}
