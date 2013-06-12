package com.careerjinn.search

import com.google.appengine.api.datastore.DatastoreService
import com.google.appengine.api.datastore.DatastoreServiceFactory
import com.google.appengine.api.datastore.Entity

/**
 * @author David Long
 * Date: 25/05/13
 * Time: 16:11
  */
class InstanceEntity {

    private String name;
    private long count;
    private Entity internalEntity;
    private boolean changed = false;

    public InstanceEntity( Entity skill ) {
        this.name = skill.getProperty( "name" );
        this.count = (long)skill.getProperty( "count" );
        internalEntity = skill;
    }

    public String getName() {
        return this.name;
    }

    public int getCount() {
        return count;
    }

    public void incrementCount() {
        count++;
        changed = true;
    }

    public void updateEntity() {
        if ( changed ) {
            DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
            internalEntity.setProperty( "count", count );
            datastore.put( internalEntity );
        }
    }
}
