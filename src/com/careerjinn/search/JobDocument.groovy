package com.careerjinn.search

import com.google.appengine.api.search.Document
import com.google.appengine.api.search.Field

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
    private Field vendor;
    private Field added;
    private Field expires;

    public JobDocument setTitle( String title ) {
        this.title = Field.newBuilder().setName("title").setText( title ).build();
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
        return this;
    }

    public JobDocument setVendor( String vendor ) {
        this.vendor = Field.newBuilder().setName("vendor").setText( vendor ).build();
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

    public Document build() {
        return Document.newBuilder()
                .addField( this.title )
                .addField( this.salaryMinimum )
                .addField( this.salaryMaximum )
                .addField( this.salaryAverage )
                .addField( this.salaryDescription )
                .addField( this.location )
                .addField( this.content )
                .addField( this.vendor )
                .addField( this.added )
                .addField( this.expires )
                .build();
    }
}
