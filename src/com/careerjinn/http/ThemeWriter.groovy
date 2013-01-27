package com.careerjinn.http

import com.google.appengine.api.datastore.Entity
import configuration.ConfigurationFactory
import configuration.PageConfiguration

import javax.servlet.http.*
import groovy.text.SimpleTemplateEngine;
import groovy.text.Template;

/**
 * @author: David Long
 * Date: 21/01/13
 * Time: 13:45
 */
class ThemeWriter {

    private def templateBinds;

    private HttpServletResponse httpResponse;

    ThemeWriter( HttpServletResponse httpResponse ) {
        setHttpResponse( httpResponse );
    }

    private void setHttpResponse( HttpServletResponse httpResponse ) {
        this.httpResponse = httpResponse;
    }

    public void generateResponseHeader() {
        httpResponse.contentType = "text/html"
    }

    public void displayTemplate() {
        PageConfiguration pageConfig = ConfigurationFactory.createPageConfiguration();
        Entity pageEntity = pageConfig.retrievePageData();
        def binding = [ Site: pageEntity.getProperty( "Site" )
                      , PageTitle: "Find your new job and the skills you need"
                      , PageKeywords: "test"
                      , PageDescription: "Test" ]
        def templateFile = new FileReader( "home.html" );
        SimpleTemplateEngine engine = new SimpleTemplateEngine( )
        Template templateToDisplay = engine.createTemplate( templateFile )
        httpResponse.writer.print templateToDisplay.make( binding ).toString();
    }

    public HttpServletResponse getHttpResponse() {
        return httpResponse;
    }

    public ThemeWriter setTemplateBinds( GroovyObject templateBinds ) {

        return this;
    }

    public GroovyObject getTemplateBinds() {

    }
}
