/*
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
import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.plugins.xml.Xml;
import org.sonarsource.analyzer.commons.RuleMetadataLoader;

/**
 * Repository for XML rules.
 * 
 * @author Nicolas Tisserand
 */
public final class ApigeeXmlRulesDefinition implements RulesDefinition {

	private final SonarRuntime sonarRuntime;

	public ApigeeXmlRulesDefinition(SonarRuntime sonarRuntime) {
	    this.sonarRuntime = sonarRuntime;
	}
	
	@Override
	public void define(Context context) {
		NewRepository repository = context.createRepository(CheckRepository.REPOSITORY_KEY, Xml.KEY).setName(CheckRepository.REPOSITORY_NAME);

		RuleMetadataLoader ruleMetadataLoader = new RuleMetadataLoader(Xml.XML_RESOURCE_PATH, Xml.SONAR_WAY_PATH, sonarRuntime);

		// add the new checks
		ruleMetadataLoader.addRulesByAnnotatedClass(repository, CheckRepository.getCheckClasses());

		repository.done();
	}

}
