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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.sonar.api.profiles.RulesProfile;
import org.sonar.api.profiles.XMLProfileParser;
import org.sonar.api.rules.Rule;
import org.sonar.api.rules.RuleFinder;
import org.sonar.api.utils.ValidationMessages;
import org.sonar.plugins.xml.language.Xml;

import com.arkea.satd.sonar.xml.ApigeeXmlSonarWayProfile;
import com.arkea.satd.sonar.xml.checks.CheckRepository;

public class ApigeeXmlSonarWayProfileTest {

	@Test
	public void should_create_sonar_way_apigee_profile() {

		XMLProfileParser xmlParser = mock(XMLProfileParser.class);
		xmlParser = new XMLProfileParser(ruleFinder());
		ValidationMessages validation = ValidationMessages.create();
		RulesProfile profile = new ApigeeXmlSonarWayProfile(xmlParser).createProfile(validation);
		//verify(xmlParser).parse(Mockito.any(Reader.class), Mockito.same(validation));
		
		assertThat(profile.getLanguage()).isEqualTo(Xml.KEY);
		assertThat(profile.getName()).isEqualTo(ApigeeXmlSonarWayProfile.PROFILE_NAME);
		assertThat(profile.getActiveRulesByRepository(CheckRepository.REPOSITORY_KEY).size() >= 24).isTrue();  // At least 24 rules
		assertThat(validation.hasErrors()).isFalse();
	}

	static RuleFinder ruleFinder() {
		return when(mock(RuleFinder.class).findByKey(Mockito.anyString(), Mockito.anyString())).thenAnswer(new Answer<Rule>() {
			public Rule answer(InvocationOnMock invocation) {
				Object[] arguments = invocation.getArguments();
				return Rule.create((String) arguments[0], (String) arguments[1], (String) arguments[1]);
			}
		}).getMock();
	}

}
