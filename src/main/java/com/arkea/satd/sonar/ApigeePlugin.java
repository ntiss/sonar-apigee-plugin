/*
 * SonarQube Apigee Plugin
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
package com.arkea.satd.sonar;

import org.sonar.api.Plugin;

import com.arkea.satd.sonar.python.ApigeePythonRulesDefinition;
import com.arkea.satd.sonar.python.ApigeePythonSensor;
import com.arkea.satd.sonar.python.PythonSonarWayProfile;
import com.arkea.satd.sonar.xml.ApigeeXmlRulesDefinition;
import com.arkea.satd.sonar.xml.ApigeeXmlSensor;
import com.arkea.satd.sonar.xml.XmlSonarWayProfile;


/**
 * Main class for the plugin
 * @author Nicolas Tisserand
 *
 */
public class ApigeePlugin implements Plugin {

	  public static final String SONAR_WAY_PROFILE_NAME = "Sonar way";
	
	@Override
	public void define(Context context) {

		context.addExtensions(
			//Xml.class, // Already imported by sonar-xml-plugin
			ApigeeXmlRulesDefinition.class,
			ApigeeXmlSensor.class,
			XmlSonarWayProfile.class,

			//Python.class, // Already imported by sonar-python-plugin
			ApigeePythonSensor.class,
			ApigeePythonRulesDefinition.class,
			PythonSonarWayProfile.class
		);
	}

}