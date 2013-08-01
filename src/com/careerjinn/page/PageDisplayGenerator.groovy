package com.careerjinn.page

/**
 * @author David Long
 * Date: 31/07/13
 * Time: 18:21
 */
class PageDisplayGenerator {

    private static final int PAGES_DISPLAYED = 5;
    private static final int OFFSET_LIMIT = Math.ceil(PAGES_DISPLAYED / 2);
    private static final int OFFSET = Math.floor(PAGES_DISPLAYED / 2);
    private static final int MIN_PAGE = 1;

    private int pageCount = 0;
    private int currentPage = 0;

    public PageDisplayGenerator setPageCount(long resultCount, int resultsPerPage) {
        this.pageCount = Math.ceil(resultCount / resultsPerPage);
        return this;
    }

    public PageDisplayGenerator setCurrentPage(int page) {
        currentPage = page;
        return this;
    }

    private int pageOffset() {
        if ((currentPage - OFFSET) < MIN_PAGE || pageCount < PAGES_DISPLAYED ) {
            return MIN_PAGE;
        }
        if ((currentPage + OFFSET) >= pageCount) {
            return pageCount - PAGES_DISPLAYED + MIN_PAGE;
        }
        return currentPage - OFFSET;
    }

    public String pageDisplay( String params ) {
        String pageList = '<ul>';
        int offset = pageOffset();
        if ( currentPage > MIN_PAGE ) {
            pageList += '<li><a href="?Page=Search&p='+ ( currentPage - 1 ) + '&' + params + '">&lt;&lt;</a></li>';
        }
        for (int i = offset; i < offset + PAGES_DISPLAYED && i <= pageCount; i++) {
            if ( i != currentPage ) {
                pageList += '<li><a href="?Page=Search&p='+ i + '&' + params + '">' + i + '</a></li>';
            } else {
                pageList += '<li>' + i + '</li>';
            }
        }
        if ( pageCount > currentPage ) {
            pageList += '<li><a href="?Page=Search&p='+ ( currentPage + 1 ) + '&' + params + '">&gt;&gt;</a></li>';
        }
        pageList += '</ul>';
        return pageList;
    }


}
