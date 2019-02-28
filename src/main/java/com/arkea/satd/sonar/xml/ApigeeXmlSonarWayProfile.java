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

import java.io.StringReader;

import org.sonar.api.profiles.ProfileDefinition;
import org.sonar.api.profiles.RulesProfile;
import org.sonar.api.profiles.XMLProfileParser;
import org.sonar.api.utils.AnnotationUtils;
import org.sonar.api.utils.ValidationMessages;
import org.sonar.plugins.xml.Xml;

import com.arkea.satd.sonar.xml.checks.CheckRepository;

/**
 * Default XML Apige profile.
 * 
 * @author Nicolas Tisserand
 */
public final class ApigeeXmlSonarWayProfile extends ProfileDefinition {

	public static final String PROFILE_NAME = "Sonar way Apigee";

	private final XMLProfileParser xmlParser;

	public ApigeeXmlSonarWayProfile(XMLProfileParser xmlParser) {
		this.xmlParser = xmlParser;
	}

	@Override
	public RulesProfile createProfile(ValidationMessages validation) {
		return xmlParser.parse(new StringReader(prepareProfileXML()), validation);
	}	
	
	private String prepareProfileXML() {
		StringBuilder xml = new StringBuilder(
			"<profile>\n" + 
			"	<language>"+Xml.KEY+"</language>\n" + 
			"	<name>"+PROFILE_NAME+"</name>\n" + 
			"	<rules>\n");
		
		// Add a <rule> block for each rule of the repository
		for (Object ruleClass : CheckRepository.getChecks()) {
			org.sonar.check.Rule ruleAnnotation = AnnotationUtils.getAnnotation(ruleClass, org.sonar.check.Rule.class);
			if (ruleAnnotation != null) {
				xml.append(
					"		<rule>\n" + 
					"			<repositoryKey>"+CheckRepository.REPOSITORY_KEY+"</repositoryKey>\n" + 
					"			<key>"+ruleAnnotation.key()+"</key>\n" + 
					"		</rule>\n");
			}
		}

		xml.append(
			"	</rules>\n" + 
			"</profile>");
		
		return xml.toString();
	}
}
