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
 * Date: 27/01/13
 * Time: 17:57
 */
class SearchPage implements Page {

    private HttpServletRequest httpRequest;
    private HttpServletResponse httpResponse;
    private SearchParameters params;
    private def properties;

    /**
     * SearchPage
     *
     * Sets some default properties for page generation
     *
     */
    public SearchPage() {

        //set some default properties so Groovy does not fail if none are set.
        properties = [
                keywords: "",
                joiner: "",
                location: "",
                pageNumber: 0,
                pageSize: 0,
                pageCount: 0
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

        params = new SearchParameters();
        SearchQuery search = new SearchQuery();
        params
                .setKeywords(httpRequest.getParameter("q"))  // query/keywords
                .setLocation(httpRequest.getParameter("l")); // location

        properties += params.getProperties();
        properties += addTermJoiner(params);
        properties += [savedSearches: SaveSearch.renderSavedSearches(httpRequest)];

        Results<ScoredDocument> results;
        LinkedHashMap viewableResults = [];
        int resultCounter = 0;
        long resultCount = 0;
        int pageNumber = 1;
        int jobsPerPage = 10;
        if (httpRequest.getParameter("p")) {
            pageNumber = httpRequest.getParameter("p").toInteger();
        }
        long resultReturned = 0;
        try {
            results = FindDocuments.findDocuments(search.buildSearchQuery(params), jobsPerPage, pageNumber);
            resultCount = results.numberFound;
            resultReturned = results.numberReturned;
            for (ScoredDocument document : results) {
                viewableResults.put(resultCounter++, resultDocumentConverter(document));
            }
        } catch (Exception e) {
            //log search errors
        }

        if ( resultReturned == 0 ) {
            System.out.println( "No Result Query" );
        }

        int adjustedPageNumber = ((pageNumber - 1) * jobsPerPage) + 1;
        properties += [results: viewableResults];
        properties += [related: getRelatedSkills(params.getKeywords())]
        properties += [pageCount: resultCount ];
        properties += [pageList: pageListGenerator(resultCount, jobsPerPage, pageNumber)];
        properties += [pageSize: (resultReturned + (jobsPerPage * (pageNumber - 1)))];
        properties += [pageNumber: adjustedPageNumber ];
        properties += [PageTitle: params.toString()];

        ThemeWriter themeWriter = new ThemeWriter(httpResponse);

        themeWriter.generateResponseHeader();
        themeWriter.displayTemplate("search.html", properties);
    }

    private String pageListGenerator(long resultCount, int jobsPerPage, int currentPage) {
        PageDisplayGenerator generator = new PageDisplayGenerator();
        generator
                .setPageCount(resultCount, jobsPerPage)
                .setCurrentPage(currentPage);
        return generator.pageDisplay( params.cookieCode() );
    }

    private LinkedHashMap getRelatedSkills(String keywords) {
        keywords = keywords.toLowerCase();
        LinkedHashMap relatedSkills = [];
        SkillList list = new SkillList();
        List<Entity> filteredList = list.filter(keywords);
        for (Entity skill : filteredList) {
            String skillsField = (String) skill.getProperty("related");
            if (skillsField) {
                for (String individualSkill : skillsField.split(',')) {
                    relatedSkills.put(URLEncoder.encode(individualSkill, "UTF-8"), individualSkill);
                }
            }
        }
        return relatedSkills;
    }

    private static LinkedHashMap addTermJoiner(SearchParameters searchParameters) {
        if (searchParameters.getKeywords() != "" && searchParameters.getLocation() != "") {
            return [joiner: "in"];
        }
        return [];
    }

    private LinkedHashMap resultDocumentConverter(Document doc) {
        return [name: extractDocumentField(doc, "title"),
                added: extractDocumentField(doc, "added"),
                content: trimString(extractDocumentField(doc, "displayContent"), 200, true),
                location: extractDocumentField(doc, "location"),
                vendor: extractDocumentField(doc, "vendor"),
                link: extractDocumentField(doc, "link").replace("&", "&amp;")];
    }

    private String extractDocumentField(Document doc, String fieldName) {
        if (doc.getFieldCount(fieldName) == 1) {
            switch (doc.getOnlyField(fieldName).getType()) {
                case Field.FieldType.TEXT:
                    return HtmlEncode.encode(doc.getOnlyField(fieldName).getText());
                case Field.FieldType.HTML:
                    return HtmlEncode.encode(doc.getOnlyField(fieldName).getHTML());
                case Field.FieldType.ATOM:
                    return HtmlEncode.encode(doc.getOnlyField(fieldName).getAtom());
                case Field.FieldType.DATE:
                    int day = Integer.parseInt(doc.getOnlyField(fieldName).getDate().format("dd"));
                    return doc.getOnlyField(fieldName).getDate().format("MMM dd") + makeSuffix(day);
            }
        }

        return "";
    }

    public static String trimString(String string, int length, boolean soft) {
        if (string == null || string.trim().isEmpty()) {
            return string;
        }

        StringBuffer sb = new StringBuffer(string);
        int actualLength = length - 3;
        if (sb.length() > actualLength) {
            // -3 because we add 3 dots at the end. Returned string length has to be length including the dots.
            if (!soft)
                return sb.insert(actualLength, "...").substring(0, actualLength + 3);
            else {
                int endIndex = sb.indexOf(" ", actualLength);
                return sb.insert(endIndex, "...").substring(0, endIndex + 3);
            }
        }
        return string;
    }

    public String makeSuffix(int dayOfMonth) {
        String suffix = "";
        switch (dayOfMonth) {
            case 1:
            case 21:
            case 31:
                suffix = "st";
                break;
            case 2:
            case 22:
                suffix = "nd";
                break;
            case 3:
            case 23:
                suffix = "rd";
                break;
            default:
                suffix = "th";
        }
        return suffix;
    }

}
