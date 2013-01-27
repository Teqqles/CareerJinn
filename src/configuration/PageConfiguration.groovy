package configuration

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory

/**
 * @author Dave Long
 * Date: 27/01/13
 * Time: 18:11
 */
class PageConfiguration {

    public static final String PAGE_DATA_NAME = "CommonData";
    public static final String PAGE_DATA_KIND = "CommonPageData";

    private Entity entity;

    private Key generateConfigurationKey() {
        return KeyFactory.createKey( PAGE_DATA_KIND, PAGE_DATA_NAME );
    }

    public Entity retrievePageData() {
        DatastoreService dataStore = DatastoreServiceFactory.getDatastoreService();
        entity = dataStore.get( generateConfigurationKey() );
        return entity;
    }

    public void configurePageData() {

        Entity commonPageData = new Entity( generateConfigurationKey() );
        commonPageData.setProperty( "Site", "Career Jinn" );

        DatastoreService dataStore = DatastoreServiceFactory.getDatastoreService();
        dataStore.put( commonPageData );
    }

}
