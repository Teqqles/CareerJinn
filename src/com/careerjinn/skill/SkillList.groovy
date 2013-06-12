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

    /**
     * Sets up the skill list in the data store for loading
     */
    public SkillList() {
        prepareList();
    }

    /**
     * Filters the skill list by the entity normalized name
     *
     * @param entityName
     * @return List<Entity>
     */
    public List<Entity> filter( String entityName ) {
        skillQuery.setFilter( new Query.FilterPredicate( 'normal_name', Query.FilterOperator.EQUAL, entityName ) );
        prepareList();
        return getList();
    }

    private void prepareList() {
        skillList = datastore.prepare( skillQuery )
                .asList( FetchOptions.Builder.withDefaults() );
    }

    /**
     * Retrieves the previously prepared or filtered list
     *
     * @return List<Entity>
     */
    public List<Entity> getList() {
        return skillList;
    }

}
