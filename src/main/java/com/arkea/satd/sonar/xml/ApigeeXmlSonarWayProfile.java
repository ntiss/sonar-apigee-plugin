/*
 * Copyright 2017 Credit Mutuel Arkea
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */

package com.arkea.satd.sonar.xml;

import org.sonar.api.profiles.ProfileDefinition;
import org.sonar.api.profiles.RulesProfile;
import org.sonar.api.rules.Rule;
import org.sonar.api.rules.RuleFinder;
import org.sonar.api.utils.AnnotationUtils;
import org.sonar.api.utils.ValidationMessages;
import org.sonar.plugins.xml.language.Xml;

import com.arkea.satd.sonar.ApigeePlugin;
import com.arkea.satd.sonar.xml.checks.CheckRepository;

/**
 * Default XML profile.
 * 
 * @author Nicolas Tisserand
 */
public final class XmlSonarWayProfile extends ProfileDefinition {

  private final RuleFinder ruleFinder;

  public XmlSonarWayProfile(RuleFinder ruleFinder) {
    this.ruleFinder = ruleFinder;
  }

  @Override
  public RulesProfile createProfile(ValidationMessages validation) {
    RulesProfile profile = RulesProfile.create(ApigeePlugin.SONAR_WAY_PROFILE_NAME, Xml.KEY);
    
    for(Object ruleClass : CheckRepository.getChecks()) {
    	org.sonar.check.Rule ruleAnnotation = AnnotationUtils.getAnnotation(ruleClass, org.sonar.check.Rule.class);
	    if (ruleAnnotation != null) {
	      String ruleKey = ruleAnnotation.key();
	      Rule rule = ruleFinder.findByKey(CheckRepository.REPOSITORY_KEY, ruleKey);
	      
	      profile.activateRule(rule, null);
	      
	    }
    }
    return profile;
  }



}
