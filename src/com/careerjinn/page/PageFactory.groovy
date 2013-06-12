package com.careerjinn.page

/**
 * A static factory for loading the user requested page
 *
 * @author David Long
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
            case "Search":
                return new SearchPage();
            case "SkillTree":
                return new TreePage();
            case "SaveSearch":
                return new SaveSearchPage();
            default:
                return new HomePage();
        }
    }
}
