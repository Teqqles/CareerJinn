package com.careerjinn.skill

import com.google.appengine.api.datastore.DatastoreService
import com.google.appengine.api.datastore.DatastoreServiceFactory
import com.google.appengine.api.datastore.Entity
import com.google.appengine.api.datastore.FetchOptions
import com.google.appengine.api.datastore.Query

/**
 * @author David Long
 * Date: 22/04/13
 * Time: 21:45
 */
class SkillList {

    private List<Entity> skillList;
    private DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    private Query skillQuery = new Query( "Skill" );

    public SkillList() {
        prepareList();
    }

    public List<Entity> filter( String entityName ) {
        skillQuery.setFilter( new Query.FilterPredicate( 'normal_name', Query.FilterOperator.EQUAL, entityName ) );
        prepareList();
        return getList();
    }

    private void prepareList() {
        skillList = datastore.prepare( skillQuery )
                .asList( FetchOptions.Builder.withDefaults() );
    }

    public List<Entity> getList() {
        return skillList;
    }

}
