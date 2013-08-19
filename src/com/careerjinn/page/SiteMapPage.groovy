package com.careerjinn.page

import com.careerjinn.http.ThemeWriter
import com.careerjinn.search.FindDocuments
import com.careerjinn.search.SaveSearch
import com.careerjinn.search.SearchParameters
import com.careerjinn.search.SearchQuery
import com.careerjinn.skill.SkillList
import com.google.appengine.api.datastore.Entity
import com.google.appengine.api.search.Document
import com.google.appengine.api.search.Field
import com.google.appengine.api.search.Results
import com.google.appengine.api.search.ScoredDocument

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * @author David Long
 * Date: 07/08/13
 * Time: 10:28
 */
class SiteMapPage implements Page {

    private HttpServletRequest httpRequest;
    private HttpServletResponse httpResponse;
    private def properties;

    /**
     * SearchPage
     *
     * Sets some default properties for page generation
     *
     */
    public SiteMapPage() {

        //set some default properties so Groovy does not fail if none are set.
        properties = [
        ];

    }

    /**
     * setHttpRequest
     *
     * Sets the HTTP request object ready for use in page rendering
     *
     * @param servletRequest
     * @return Page
     */
    @Override
    public Page setHttpRequest(HttpServletRequest servletRequest) {
        httpRequest = servletRequest;
        return this;
    }

    /**
     * setHttpResponse
     *
     * Sets the HTTP response object ready for use in page rendering
     *
     * @param servletResponse
     * @return Page
     */
    @Override
    public Page setHttpResponse(HttpServletResponse servletResponse) {
        httpResponse = servletResponse;
        return this;
    }

    /**
     * renderPage
     *
     * Builds the SearchParameters and query objects, gathers any results and adds them to the page display
     */
    @Override
    public void renderPage() {
        ThemeWriter themeWriter = new ThemeWriter(httpResponse);
        themeWriter.generateResponseHeader( "text/plain" );
        themeWriter.displayTemplate("sitemap.txt", [skills: getSkills()]);
    }

    private LinkedHashMap getSkills() {
        LinkedHashMap skills = [];
        SkillList list = new SkillList();
        for (Entity skill : list.getList()) {
            skills.put(URLEncoder.encode(skill.getProperty("normal_name").toString(), "UTF-8"), skill.getProperty("name"));
        }
        return skills;
    }

}
