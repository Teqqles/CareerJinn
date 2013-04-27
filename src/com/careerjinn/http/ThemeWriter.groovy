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

    public void displayTemplate( String template = "home.html", def pageBinds = [] ) {
        PageConfiguration pageConfig = ConfigurationFactory.createPageConfiguration();
        Entity pageEntity = pageConfig.retrievePageData();
        def binding = [ Site: pageEntity.getProperty( "Site" )
                      , PageTitle: "Find your new job and the skills you need"
                      , PageKeywords: pageEntity.getProperty( "DefaultKeywords" )
                      , PageDescription: pageEntity.getProperty( "DefaultDescription" ) ];
        binding += pageBinds;
        def templateFile = new FileReader( template );
        SimpleTemplateEngine engine = new SimpleTemplateEngine( )
        Template templateToDisplay = engine.createTemplate( templateFile )
        httpResponse.writer.print templateToDisplay.make( binding ).toString();
    }

    public HttpServletResponse getHttpResponse() {
        return httpResponse;
    }
}
