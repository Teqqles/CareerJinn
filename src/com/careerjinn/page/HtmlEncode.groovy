package com.careerjinn.page

/**
 * A utility class for the encoding and decoding of special characters for display within HTML
 *
 * @author David Long
 * Date: 20/04/13
 * Time: 15:17
 */
class HtmlEncode {

    /**
     * encodes a number of characters which will break a HTML form
     *
     * @param unsafeString
     * @return String
     */
    public static String encode( String unsafeString ) {
        String safeString = unsafeString.replace( "<", "&lt;" )
                                        .replace( '"', "&quot;" )
                                        .replace( "\u00A3", "&pound;" );
        return safeString;
    }

    /**
     * decodes HTML entities to ensure that it can be parsed easily
     *
     * @param unsafeString
     * @return String
     */
    public static String decode( String unsafeString ) {
        String safeString = unsafeString.replace( "&lt;", "<" )
                                        .replace( "&quot;", '"' )
                                        .replace( "&pound;", "\u00A3" );
        return safeString;
    }
}
