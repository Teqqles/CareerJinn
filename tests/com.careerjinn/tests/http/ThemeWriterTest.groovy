package com.careerjinn.tests.http

import com.careerjinn.http.ThemeWriter
import groovy.mock.interceptor.MockFor
import groovy.mock.interceptor.StubFor

import javax.servlet.http.HttpServletResponse

/**
 * @author: David Long
 * Date: 21/01/13
 * Time: 13:57
 */
class ThemeWriterTest extends GroovyTestCase {

    private MockFor mockHttpServletResponse;
    private MockFor mockFile;

    void setUp( ) {
        mockHttpServletResponse = new MockFor( HttpServletResponse.class );
        mockFile                = new MockFor( FileReader.class );
    }

    public void testConstructorSetsHttpResponse() {
        def mockHttpServletResponse = castToHttpServletResponse();
        ThemeWriter themeWriter = new ThemeWriter( mockHttpServletResponse );
        assert mockHttpServletResponse == themeWriter.httpResponse;
    }

    public void testGenerateResponseHeader() {
        String expected = "text/html";
        mockHttpServletResponse.demand.with {
            setContentType{ String got -> assert expected == got }
        }
        def mockHttpInstance = castToHttpServletResponse();
        def themeWriter = new ThemeWriter( mockHttpInstance );
        themeWriter.generateResponseHeader();
        mockHttpServletResponse.verify( mockHttpInstance as GroovyObject );
    }

    public void testDisplayTemplate() {
        String expected = "test";
        StringWriter stringWriter = new StringWriter();
        mockHttpServletResponse.demand.with {
            getWriter{ new PrintWriter( stringWriter ) }
        }

        def mockHttpInstance = castToHttpServletResponse();
        def themeWriter = new ThemeWriter( mockHttpInstance );
        themeWriter.displayTemplate();
        assert stringWriter.toString() == expected
        mockHttpServletResponse.verify( mockHttpInstance as GroovyObject );
    }

    private HttpServletResponse castToHttpServletResponse() {
        return mockHttpServletResponse.proxyDelegateInstance() as HttpServletResponse;
    }

}
