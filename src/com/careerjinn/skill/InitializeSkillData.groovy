package com.careerjinn.skill

import com.google.appengine.api.datastore.DatastoreService
import com.google.appengine.api.datastore.DatastoreServiceFactory
import com.google.appengine.api.datastore.Entity
import com.google.appengine.api.datastore.Key

/**
 * @author David Long
 * Date: 22/04/13
 * Time: 21:06
 */
class InitializeSkillData {

    Entity parentEntity;

    /**
     * Adds a parent entity ready for loading the skills dictionary
     */
    public InitializeSkillData() {
        parentEntity = new Entity( "Skill" ); // global parent entity for all skills
    }

    @SuppressWarnings("GrMethodMayBeStatic")
    private Key generateSkillDataKey() {
        return parentEntity.getKey();
    }

    /**
     * Loads the skills list and adds each entry into the datastore
     */
    public void createSkillEntries() {
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        SkillLoader loader = new SkillLoader();
        List<Entity> list  = loader.loadSkillList( generateSkillDataKey() );
        if( list.size() > 0 ) {
            datastore.put( list );
        }
    }

}
