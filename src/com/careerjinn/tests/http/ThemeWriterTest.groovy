package com.careerjinn.tests.http

import com.careerjinn.http.ThemeWriter

import javax.servlet.http.HttpServletResponse

/**
 * @author: David Long
 * Date: 21/01/13
 * Time: 13:57
 */
class ThemeWriterTest extends GroovyTestCase {

    void setUp( ) {

    }

    public void testSetHttpResponse() {

        def mockHttpResponse = { } as HttpServletResponse

        ThemeWriter themeWriter = new ThemeWriter( mockHttpResponse );

    }

}
