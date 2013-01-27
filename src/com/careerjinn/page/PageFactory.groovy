package com.careerjinn.page

/**
 * @author Dave Long
 * Date: 27/01/13
 * Time: 17:52
 */
class PageFactory {

    //This factory cannot be instantiated
    private PageFactory() {

    }

    public static Page createPage( String destinationPage ) {
        switch( destinationPage ) {
            case "InitialLoad":
                return new InitialLoadingPage();
            default:
                return new HomePage();
        }
    }
}
