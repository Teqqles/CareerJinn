package com.careerjinn.skill

import com.google.appengine.api.datastore.Entity
import com.google.appengine.api.datastore.Key

/**
 * @author David Long
 * Date: 22/04/13
 * Time: 21:31
 */
class SkillLoader {

    /**
     * Loads each skill from a text file, assigning a name, a normalized lowercase name, a count and any related skills
     * returning the complete entity list at the end
     *
     * @param entityParent
     * @return List<Entity>
     */
    public List<Entity> loadSkillList( Key entityParent ) {
            BufferedReader fileBuffer;
            FileReader reader = null;
            List<Entity> list = new ArrayList<Entity>();

            try {
                reader = new FileReader( "skillList.txt" );
                fileBuffer = new BufferedReader( reader );
                String rawSkill;
                while ( ( rawSkill = fileBuffer.readLine() ) != null ) {
                    Entity entity = new Entity( "Skill" );
                    String[] skill = rawSkill.split('\\|');
                    if ( skill.length > 0 ) {
                        rawSkill = skill[0];
                        if ( skill.length > 1 ) {
                            entity.setProperty( "related", skill[1] ); //comma separated list of related skills.
                        }
                    }
                    entity.setProperty( "name", rawSkill );
                    entity.setProperty( "normal_name", rawSkill.toLowerCase() );
                    entity.setProperty( "count", 0 );
                    list.add( entity );
                }
            } catch ( Exception e ) {
                //log failures
            } finally {
                fileBuffer.close();
                reader.close();
            }

            return list;
        }
}
