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

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.sonar.api.SonarQubeSide;
import org.sonar.api.SonarRuntime;
import org.sonar.api.internal.SonarRuntimeImpl;
import org.sonar.api.server.profile.BuiltInQualityProfilesDefinition;
import org.sonar.api.utils.ValidationMessages;
import org.sonar.api.utils.Version;
import org.sonar.plugins.xml.Xml;
import org.sonar.plugins.xml.checks.CheckList;

public class ApigeeXmlSonarWayProfileTest {

	@Test
	public void should_create_sonar_way_apigee_profile() {

	    SonarRuntime sonarRuntime = SonarRuntimeImpl.forSonarQube(Version.create(7, 3), SonarQubeSide.SERVER);
	    ValidationMessages validation = ValidationMessages.create();

	    BuiltInQualityProfilesDefinition.Context context = new BuiltInQualityProfilesDefinition.Context();

	    ApigeeXmlSonarWayProfile definition = new ApigeeXmlSonarWayProfile(sonarRuntime);
	    definition.define(context);

	    BuiltInQualityProfilesDefinition.BuiltInQualityProfile profile = context.profile(Xml.KEY, ApigeeXmlSonarWayProfile.PROFILE_NAME);

	    assertThat(profile.language()).isEqualTo(Xml.KEY);
	    assertThat(profile.name()).isEqualTo(ApigeeXmlSonarWayProfile.PROFILE_NAME);
	    assertThat(profile.rules().size()).isGreaterThanOrEqualTo(28); // At least 28 rules
	    assertThat(profile.rules().size()).isGreaterThanOrEqualTo(CheckList.getCheckClasses().size());
	    assertThat(validation.hasErrors()).isFalse();				
	}


}
