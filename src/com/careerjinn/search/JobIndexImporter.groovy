package com.careerjinn.search

import com.careerjinn.skill.SkillList
import com.google.appengine.api.datastore.DatastoreService
import com.google.appengine.api.datastore.DatastoreServiceFactory
import com.google.appengine.api.datastore.Entity
import com.google.appengine.api.search.Index
import com.google.appengine.api.search.PutException

import java.util.regex.Pattern

/**
 * @author Dave Long
 * Date: 17/04/13
 * Time: 18:17
 */
class JobIndexImporter {

    private Index index;

    public JobIndexImporter() {
        //cache the index for loading multiple documents
        this.index = SearchIndex.getIndex();
    }

    public void addToIndex( JobDocument job ) throws PutException {
        //find skills and add to the job document
        job = addSkills( job );
        this.index.put( job.build() );
    }

    private JobDocument addSkills( JobDocument jobDocument ) {
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        SkillList list = new SkillList();
        for( Entity skill: list.getList() ) {
            if(documentMatchesSkill( jobDocument, skill ) ) {
                jobDocument.addSkill( skill );
                long count = (long)skill.getProperty( "count" );
                count++;
                skill.setProperty( "count", count );
                datastore.put( skill );
                System.out.println( "adding skill" );
            }
        }
        return jobDocument;
    }

    private boolean documentMatchesSkill(JobDocument jobDocument, Entity skill) {
        Pattern pattern = Pattern.compile( '\\b' + Pattern.quote((String) skill.getProperty( "name" ) ) + '\\b', Pattern.CASE_INSENSITIVE );
        return jobDocument.getSkillBuffer().find( pattern.pattern() );
    }
}
