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

import org.sonar.api.SonarRuntime;
import org.sonar.api.server.profile.BuiltInQualityProfilesDefinition;
import org.sonar.plugins.xml.Xml;
import org.sonarsource.analyzer.commons.BuiltInQualityProfileJsonLoader;

import com.arkea.satd.sonar.xml.checks.CheckRepository;

/**
 * Default XML Apige profile.
 * 
 * @author Nicolas Tisserand
 */
public final class ApigeeXmlSonarWayProfile implements BuiltInQualityProfilesDefinition {

	private final org.sonar.api.SonarRuntime sonarRuntime;

	public static final String PROFILE_NAME = "Sonar way Apigee";

	private static final String SONAR_WAY_PATH = "org/sonar/l10n/xml/rules/xml/Sonar_way_apigee_profile.json";

	public ApigeeXmlSonarWayProfile(SonarRuntime sonarRuntime) {
		this.sonarRuntime = sonarRuntime;
	}

	@Override
	public void define(Context context) {
		NewBuiltInQualityProfile sonarWay = context.createBuiltInQualityProfile(ApigeeXmlSonarWayProfile.PROFILE_NAME, Xml.KEY);
		BuiltInQualityProfileJsonLoader.load(sonarWay, CheckRepository.REPOSITORY_KEY, ApigeeXmlSonarWayProfile.SONAR_WAY_PATH, Xml.XML_RESOURCE_PATH, sonarRuntime);
		sonarWay.done();
	}

}
