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
package com.arkea.satd.sonar.checks;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Collection;

import org.junit.rules.TemporaryFolder;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.fs.internal.DefaultInputFile;
import org.sonar.api.batch.fs.internal.TestInputFileBuilder;
import org.sonar.api.batch.rule.internal.ActiveRulesBuilder;
import org.sonar.api.batch.sensor.internal.SensorContextTester;
import org.sonar.api.batch.sensor.issue.Issue;
import org.sonar.api.rule.RuleKey;
import org.sonar.plugins.xml.Xml;
import org.sonarsource.analyzer.commons.xml.XmlFile;
import org.sonarsource.analyzer.commons.xml.checks.SonarXmlCheck;

import com.arkea.satd.sonar.AbstractXmlPluginTester;
import com.arkea.satd.sonar.xml.ApigeeXmlSensor;

public abstract class AbstractCheckTester extends AbstractXmlPluginTester {

  @org.junit.Rule
  public TemporaryFolder temporaryFolder = new TemporaryFolder();

	protected Collection<Issue> getIssues(SonarXmlCheck check, String content) throws IOException {
		
		SensorContextTester context = SensorContextTester
				.create(Paths.get(""))
				.setActiveRules((new ActiveRulesBuilder())
				.create(RuleKey.of(Xml.KEY, "ruleKey"))
				.activate()
				.build());		
		ApigeeXmlSensor.setContext(context);
	    XmlFile xmlFile = createTempFile("defaultTempFilename", content);
	    if(xmlFile!=null) {
	    	check.scanFile(context, RuleKey.of(Xml.KEY, "ruleKey"), xmlFile);
	    }
		return context.allIssues();	    
	}
  
	protected Collection<Issue> getIssues(SonarXmlCheck check, XmlFile xmlFile) throws IOException {
		
		SensorContextTester context = SensorContextTester
				.create(Paths.get(""))
				.setActiveRules((new ActiveRulesBuilder())
				.create(RuleKey.of(Xml.KEY, "ruleKey"))
				.activate()
				.build());		
		ApigeeXmlSensor.setContext(context);
	    if(xmlFile!=null) {
	    	check.scanFile(context, RuleKey.of(Xml.KEY, "ruleKey"), xmlFile);
	    }
		return context.allIssues();	    
	}	
	
	protected XmlFile createTempFile(String filename, String content) throws IOException {

	    DefaultInputFile defaultInputFile = TestInputFileBuilder.create("key", filename)
	  	      .setType(InputFile.Type.MAIN)
	  	      .setContents(content)
	  	      .setLanguage(Xml.KEY)
	  	      .setCharset(StandardCharsets.UTF_8)
	  	      .build();
		
	    XmlFile xmlFile = null;
	    try {
	      xmlFile = XmlFile.create(defaultInputFile);
	    } catch (Exception e) {
	      //throw new IllegalStateException("Unable to scan xml file", e);
	    }
	    
	    return xmlFile;
		
	}
}
