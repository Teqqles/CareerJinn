package com.careerjinn.search

import com.careerjinn.skill.SkillList
import com.google.appengine.api.datastore.Entity
import com.google.appengine.api.memcache.Expiration
import com.google.appengine.api.memcache.MemcacheService
import com.google.appengine.api.memcache.MemcacheServiceFactory
import com.google.appengine.api.search.Document
import com.google.appengine.api.search.Index
import com.google.appengine.api.search.PutException

import java.security.DigestInputStream
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.regex.Pattern

/**
 * Utility class for adding job documents to an index
 *
 * @author David Long
 * Date: 17/04/13
 * Time: 18:17
 */
class JobIndexImporter {

    private Index index;
    private List<InstanceEntity> listCache =  new ArrayList<InstanceEntity>();
    private List<Document> documents = new ArrayList<Document>();
    private int documentCount = 1;
    private MemcacheService memcache = MemcacheServiceFactory.getMemcacheService();

    /**
     * Gets a reference to the SearchIndex and stores locally
     */
    public JobIndexImporter() {
        //cache the index for loading multiple documents
        this.index = SearchIndex.getIndex();

        SkillList list = new SkillList();
        for( Entity skill: list.getList() ) {
            listCache.add( new InstanceEntity( skill ) );
        }
    }

    public void updateSkills() {
        for( InstanceEntity skill: listCache ) {
            skill.updateEntity();
        }
    }

    public static String calculateHash( JobDocument job ) throws Exception{
        String hashable = job.getSkillBuffer();
        MessageDigest algorithm;
        try {
            algorithm = MessageDigest.getInstance("SHA1");
        } catch ( NoSuchAlgorithmException e ) {
            algorithm = MessageDigest.getInstance("MD5");
        }

        algorithm.update( hashable.getBytes() );
        // get the hash value as byte array
        byte[] hash = algorithm.digest();
        return byteArray2Hex(hash);
    }

    private static String byteArray2Hex(byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        return formatter.toString();
    }

    /**
     *
     * Adds a job document to the search index
     *
     * @param job
     * @throws PutException
     */
    public void addToIndex( JobDocument job, Date expires ) throws PutException {
        String hash = calculateHash( job );
        if ( memcache.contains( hash ) ) {
            return;
        }
        job = addSkills( job );
        documentCount++;
        documents.add( job.build() )
        if ( documentCount >= 100 ) {
            this.index.put( documents );
            documentCount = 1;
            documents.clear();
        }
        memcache.put( hash,
                      1,
                      Expiration.onDate( expires ) );
    }

    private JobDocument addSkills( JobDocument jobDocument ) {

        for( InstanceEntity skill: listCache ) {
            if( documentMatchesSkill( jobDocument, skill ) ) {
                jobDocument.addSkill( skill );
                skill.incrementCount();
            }
        }
        return jobDocument;
    }

    private boolean documentMatchesSkill(JobDocument jobDocument, InstanceEntity skill) {
        Pattern pattern = Pattern.compile( '\\b' + Pattern.quote((String) skill.getName() ) + '\\b', Pattern.CASE_INSENSITIVE );
        return jobDocument.getSkillBuffer().find( pattern.pattern() );
    }
}
