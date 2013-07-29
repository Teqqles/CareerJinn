package com.careerjinn.search

import com.google.appengine.api.datastore.Entity
import com.google.appengine.api.search.Document
import com.google.appengine.api.search.Field

/**
 * @author David Long
 * Date: 03/02/13
 * Time: 17:24
**/
class JobDocument {

    private Field title;
    private Field salaryMinimum;
    private Field salaryMaximum;
    private Field salaryAverage;
    private Field salaryDescription;
    private Field location;
    private Field content;
    private Field displayContent;
    private Field vendor;
    private Field added;
    private Field expires;
    private Field link;
    private List<Field> skills = new ArrayList<Field>();

    private String skillBuffer = "";

    /**
     * Job title
     *
     * @param title
     * @return JobDocument
     */
    public JobDocument setTitle( String title ) {
        this.title = Field.newBuilder().setName("title").setText( title ).build();
        skillBuffer += " " + title;
        return this;
    }

    /**
     * Salary expectation for the role, from the minimum to the maximum in the range with additional description
     *
     * @param salaryMin
     * @param salaryMin
     * @param salaryDescription
     *
     * @return JobDocument
     */
    public JobDocument setSalary( Number salaryMax, Number salaryMin, String salaryDescription ) {
        Number salaryAverage = ( salaryMax + salaryMin ) / 2;
        this.salaryMinimum = Field.newBuilder().setName("salaryMinimum").setNumber( salaryMin.doubleValue() ).build();
        this.salaryMaximum = Field.newBuilder().setName("salaryMaximum").setNumber( salaryMax.doubleValue() ).build();
        this.salaryAverage = Field.newBuilder().setName("salaryAverage").setNumber( salaryAverage.doubleValue() ).build();
        this.salaryDescription = Field.newBuilder().setName("salaryDescription").setText( salaryDescription ).build();
        return this;
    }

    /**
     * Adds the job location
     *
     * @param location
     * @return JobDocument
     */
    public JobDocument setLocation( String location ) {
        this.location = Field.newBuilder().setName("location").setText( location ).build();
        return this;
    }

    /**
     * Job Description
     *
     * @param content
     * @return JobDocument
     */
    public JobDocument setContent( String content ) {
        this.content = Field.newBuilder().setName("content").setHTML( content ).build();
        this.displayContent = Field.newBuilder().setName("displayContent").setText( extractText( content ) ).build();
        skillBuffer += " " + trimString( content, 750, true );
        return this;
    }

    /**
     * Job Advertiser
     *
     * @param vendor
     * @return JobDocument
     */
    public JobDocument setVendor( String vendor ) {
        this.vendor = Field.newBuilder().setName("vendor").setAtom( vendor ).build();
        return this;
    }

    /**
     * Date the job was added to Job Search 2.0
     *
     * @param added
     * @return JobDocument
     */
    public JobDocument setAdded( Date added ) {
        this.added = Field.newBuilder().setName("added").setDate( added ).build();
        return this;
    }

    /**
     * Date the job needs removing from Job Search 2.0
     *
     * @param expires
     * @return JobDocument
     */
    public JobDocument setExpires( Date expires ) {
        this.expires = Field.newBuilder().setName("expires").setDate( expires ).build();
        return this;
    }

    /**
     * View hyperlink for the job
     *
     * @param link
     * @return JobDocument
     */
    public JobDocument setLink( String link ) {
        this.link = Field.newBuilder().setName("link").setAtom( link ).build();
        return this;
    }

    private Field generateContentTitle( Field title, Field content ) {
        return Field.newBuilder().setName("title_content").setHTML( title.getText() + " " + content.getHTML() ).build();
    }

    /**
     * Retrieve all text in the job advert which can contain skills
     *
     * @return String
     */
    public String getSkillBuffer() {
        return skillBuffer;
    }

    /**
     * Add skill entity to a field
     *
     * @param skill
     * @return Field
     */
    public Field addSkill( InstanceEntity skill ) {
        Field skillField = Field.newBuilder().setName("skill").setAtom( (String)skill.getName() ).build();
        skills.add( skillField );
        return skillField;
    }

    /**
     * Compile all job fields into a document and return for adding to an index
     *
     * @return Document
     */
    public Document build() {
        Document.Builder docBuilder = Document.newBuilder();
        docBuilder.addField( this.title )
                  .addField( this.salaryMinimum )
                  .addField( this.salaryMaximum )
                  .addField( this.salaryAverage )
                  .addField( this.salaryDescription )
                  .addField( this.location )
                  .addField( this.content )
                  .addField( this.displayContent )
                  .addField( this.vendor )
                  .addField( this.added )
                  .addField( this.expires )
                  .addField( this.link )
                  .addField( this.generateContentTitle( title, content ) );
        for ( Field skill: skills ) {
            docBuilder.addField( skill );
        }
        return docBuilder.build();
    }

    /**
     * Extract text from HTML content
     *
     * @param html
     * @return String
     */
    private String extractText( String html ) {
        //if we indeed have HTML lets extract only the body text
        String body = html.find( "<\\s*body[^>]*>.+<\\s*/\\s*body\\s*>" );
        if ( !body ) {
            body = html; // we could not extract the body so we assume we have a plain text advert
        }
        return body.replaceAll( "<[^>]*>", " " ); //remove unwanted HTML formatting
    }

    public static String trimString(String string, int length, boolean soft) {
        if(string == null || string.trim().isEmpty()){
            return string;
        }

        StringBuffer sb = new StringBuffer(string);
        int actualLength = length - 3;
        if(sb.length() > actualLength){
            // -3 because we add 3 dots at the end. Returned string length has to be length including the dots.
            if(!soft)
                return sb.insert(actualLength, "...").substring(0, actualLength+3);
            else {
                int endIndex = sb.indexOf(" ",actualLength);
                if ( endIndex < 0 ) {
                    endIndex = 0;
                }
                return sb.insert(endIndex,"...").substring(0, endIndex+3);
            }
        }
        return string;
    }


}
