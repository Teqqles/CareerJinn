package com.careerjinn.page

/**
 * @author David Long
 * Date: 20/04/13
 * Time: 15:17
 */
class HtmlEncode {

    public static String encode( String unsafeString ) {
        String safeString = unsafeString.replace( "<", "&lt;" )
                                        .replace( '"', "&quot;" )
                                        .replace( "\u00A3", "&pound;" );
        return safeString;
    }

    public static String decode( String unsafeString ) {
        String safeString = unsafeString.replace( "&lt;", "<" )
                                        .replace( "&quot;", '"' )
                                        .replace( "&pound;", "\u00A3" );
        return safeString;
    }
}
