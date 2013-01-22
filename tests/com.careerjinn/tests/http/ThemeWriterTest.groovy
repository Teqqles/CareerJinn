package com.careerjinn.tests.http

import com.careerjinn.http.ThemeWriter
import groovy.mock.interceptor.MockFor

import javax.servlet.http.HttpServletResponse

/**
 * @author: David Long
 * Date: 21/01/13
 * Time: 13:57
 */
class ThemeWriterTest extends GroovyTestCase {

    private MockFor mock

    void setUp( ) {
        mock         = new MockFor( HttpServletResponse.class );
    }

    public void testConstructorSetsHttpResponse() {
        def mockHttpServletResponse = castToHttpServletResponse();
        ThemeWriter themeWriter = new ThemeWriter( mockHttpServletResponse );
        assert mockHttpServletResponse == themeWriter.httpResponse;
    }

    public void testSetHttpResponse() {
        def mockHttpServletResponse = castToHttpServletResponse();
        ThemeWriter themeWriter  = new ThemeWriter( null );
        themeWriter.httpResponse = mockHttpServletResponse;
        assert mockHttpServletResponse == themeWriter.httpResponse;
    }

    public void testGenerateResponse() {
        def expected = "text/html";
        mock.demand.with {
            setContentType{ String got -> assert expected == got }
        }
        def mockHttpServletResponse = mock.proxyInstance();
        def themeWriter = new ThemeWriter( mockHttpServletResponse );
        themeWriter.generateResponse();
        mock.verify( mockHttpServletResponse );
    }

    private HttpServletResponse castToHttpServletResponse() {
        return mock.proxyInstance();
    }

}
