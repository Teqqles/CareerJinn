package com.careerjinn.search

import com.google.appengine.api.datastore.Entity
import com.google.appengine.api.search.Document
/**
 * @author Dave Long
 * Date: 03/02/13
 * Time: 17:24
 */
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

    public JobDocument setTitle( String title ) {
        this.title = Field.newBuilder().setName("title").setText( title ).build();
        skillBuffer += " " + title;
        return this;
    }

    public JobDocument setSalary( Number salaryMax, Number salaryMin, String salaryDescription ) {
        Number salaryAverage = ( salaryMax + salaryMin ) / 2;
        this.salaryMinimum = Field.newBuilder().setName("salaryMinimum").setNumber( salaryMin.doubleValue() ).build();
        this.salaryMaximum = Field.newBuilder().setName("salaryMaximum").setNumber( salaryMax.doubleValue() ).build();
        this.salaryAverage = Field.newBuilder().setName("salaryAverage").setNumber( salaryAverage.doubleValue() ).build();
        this.salaryDescription = Field.newBuilder().setName("salaryDescription").setText( salaryDescription ).build();
        return this;
    }

    public JobDocument setLocation( String location ) {
        this.location = Field.newBuilder().setName("location").setText( location ).build();
        return this;
    }

    public JobDocument setContent( String content ) {
        this.content = Field.newBuilder().setName("content").setHTML( content ).build();
        this.displayContent = Field.newBuilder().setName("displayContent").setText( extractText( content ) ).build();
        skillBuffer += " " + content;
        return this;
    }

    public JobDocument setVendor( String vendor ) {
        this.vendor = Field.newBuilder().setName("vendor").setAtom( vendor ).build();
        return this;
    }

    public JobDocument setAdded( Date added ) {
        this.added = Field.newBuilder().setName("added").setDate( added ).build();
        return this;
    }

    public JobDocument setExpires( Date expires ) {
        this.expires = Field.newBuilder().setName("expires").setDate( expires ).build();
        return this;
    }

    public JobDocument setLink( String link ) {
        this.link = Field.newBuilder().setName("link").setAtom( link ).build();
        return this;
    }

    private Field generateContentTitle( Field title, Field content ) {
        return Field.newBuilder().setName("title_content").setHTML( title.getText() + " " + content.getHTML() ).build();
    }

    public String getSkillBuffer() {
        return skillBuffer;
    }

    public Field addSkill( Entity skill ) {
        Field skillField = Field.newBuilder().setName("skill").setAtom( (String)skill.getProperty( "name" ) ).build();
        skills.add( skillField );
        return skillField;
    }

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

    private String extractText( String html ) {
        //if we indeed have HTML lets extract only the body text
        String body = html.find( "<\\s*body[^>]*>.+<\\s*/\\s*body\\s*>" );
        if ( !body ) {
            body = html; // we could not extract the body so we assume we have a plain text advert
        }
        return body.replaceAll( "<[^>]*>", "" ); //remove unwanted HTML formatting
    }


}

import com.google.appengine.api.search.Field
