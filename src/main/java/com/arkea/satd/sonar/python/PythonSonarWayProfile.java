/*
 * SonarQube Apigee Python Plugin
 * @author Nicolas Tisserand
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package com.arkea.satd.sonar.python;

import org.sonar.api.profiles.ProfileDefinition;
import org.sonar.api.profiles.RulesProfile;
import org.sonar.api.rules.Rule;
import org.sonar.api.rules.RuleFinder;
import org.sonar.api.utils.AnnotationUtils;
import org.sonar.api.utils.ValidationMessages;
import org.sonar.plugins.python.Python;

import com.arkea.satd.sonar.ApigeePlugin;
import com.arkea.satd.sonar.python.checks.CheckRepository;

/**
 * Default XML profile.
 * 
 * @author Matthijs Galesloot
 */
public final class PythonSonarWayProfile extends ProfileDefinition {

  private final RuleFinder ruleFinder;

  public PythonSonarWayProfile(RuleFinder ruleFinder) {
    this.ruleFinder = ruleFinder;
  }

  @Override
  public RulesProfile createProfile(ValidationMessages validation) {
    RulesProfile profile = RulesProfile.create(ApigeePlugin.SONAR_WAY_PROFILE_NAME, Python.KEY);
    
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
