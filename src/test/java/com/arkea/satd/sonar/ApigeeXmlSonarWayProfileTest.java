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
package com.arkea.satd.sonar;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.sonar.api.server.profile.BuiltInQualityProfilesDefinition;
import org.sonar.api.utils.ValidationMessages;
import org.sonar.plugins.xml.Xml;
import org.sonar.plugins.xml.checks.CheckList;

import com.arkea.satd.sonar.xml.ApigeeXmlSonarWayProfile;

public class ApigeeXmlSonarWayProfileTest {

	@Test
	public void should_create_sonar_way_apigee_profile() {

	    ValidationMessages validation = ValidationMessages.create();

	    BuiltInQualityProfilesDefinition.Context context = new BuiltInQualityProfilesDefinition.Context();

	    ApigeeXmlSonarWayProfile definition = new ApigeeXmlSonarWayProfile();
	    definition.define(context);

	    BuiltInQualityProfilesDefinition.BuiltInQualityProfile profile = context.profile(Xml.KEY, ApigeeXmlSonarWayProfile.PROFILE_NAME);

	    assertThat(profile.language()).isEqualTo(Xml.KEY);
	    assertThat(profile.name()).isEqualTo(ApigeeXmlSonarWayProfile.PROFILE_NAME);
	    assertThat(profile.rules()).hasSizeGreaterThanOrEqualTo(30); // At least 30 rules
	    assertThat(profile.rules()).hasSizeGreaterThanOrEqualTo(CheckList.getCheckClasses().size());
	    assertThat(validation.hasErrors()).isFalse();				
	}


}
