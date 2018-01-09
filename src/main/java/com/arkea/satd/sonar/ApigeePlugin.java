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

import org.sonar.api.Plugin;

import com.arkea.satd.sonar.xml.ApigeeXmlRulesDefinition;
import com.arkea.satd.sonar.xml.ApigeeXmlSensor;
import com.arkea.satd.sonar.xml.ApigeeXmlSonarWayProfile;


/**
 * Main class for the plugin
 * @author Nicolas Tisserand
 *
 */
public class ApigeePlugin implements Plugin {

	@Override
	public void define(Context context) {

		context.addExtensions(
			//Xml.class, // Already imported by sonar-xml-plugin
			ApigeeXmlRulesDefinition.class,
			ApigeeXmlSensor.class,
			ApigeeXmlSonarWayProfile.class
			
		);
	}

}