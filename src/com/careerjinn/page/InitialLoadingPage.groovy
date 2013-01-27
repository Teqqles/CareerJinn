package com.careerjinn.page

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory
import configuration.ConfigurationFactory
import configuration.PageConfiguration

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * @author Dave Long
 * Date: 27/01/13
 * Time: 16:53
 */
class InitialLoadingPage extends HomePage {

    public InitialLoadingPage() {
        PageConfiguration pageConfig = ConfigurationFactory.createPageConfiguration();
        pageConfig.configurePageData();
    }
}
