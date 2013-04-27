package com.careerjinn.page

import com.careerjinn.skill.InitializeSkillData
import configuration.ConfigurationFactory
import configuration.PageConfiguration

/**
 * @author David Long
 * Date: 27/01/13
 * Time: 16:53
 */
class InitialLoadingPage extends HomePage {

    public InitialLoadingPage() {
        PageConfiguration pageConfig = ConfigurationFactory.createPageConfiguration();
        InitializeSkillData skillData = new InitializeSkillData();
        pageConfig.configurePageData();
        skillData.createSkillEntries();
    }
}
