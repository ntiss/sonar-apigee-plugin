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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.sonar.api.batch.fs.FilePredicate;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.rule.CheckFactory;
import org.sonar.api.batch.rule.Checks;
import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.api.rule.RuleKey;
import org.sonar.plugins.xml.Xml;
import org.sonarsource.analyzer.commons.ProgressReport;
import org.sonarsource.analyzer.commons.xml.XmlFile;
import org.sonarsource.analyzer.commons.xml.checks.SonarXmlCheck;

/**
 * ApigeeXmlSensor provides analysis of xml files.
 * 
 * @author Matthijs Galesloot
 * @author Nicolas Tisserand
 */
public class ApigeeXmlSensor implements Sensor {

	private final Checks<Object> checks;
	private final FileSystem fileSystem;
	private final FilePredicate mainFilesPredicate;

	private static SensorContext staticContext;

	public static void setContext(SensorContext ctx) {
		staticContext = ctx;
	}
	
	public ApigeeXmlSensor(FileSystem fileSystem, CheckFactory checkFactory) {		
		this.checks = checkFactory.create(CheckRepository.REPOSITORY_KEY).addAnnotatedChecks(CheckRepository.getCheckClasses());
		this.fileSystem = fileSystem;
		this.mainFilesPredicate = fileSystem.predicates().and(
		fileSystem.predicates().hasType(InputFile.Type.MAIN),
		fileSystem.predicates().hasLanguage(Xml.KEY));
	}

	private void runChecks(SensorContext context, XmlFile newXmlFile) {
	    checks.all().stream()
	      .map(SonarXmlCheck.class::cast)
	      // checks.ruleKey(check) is never null because "check" is part of "checks.all()"
	      .forEach(check -> runCheck(context, check, checks.ruleKey(check), newXmlFile));
	}
	
	// Visible for testing
	  void runCheck(SensorContext context, SonarXmlCheck check, RuleKey ruleKey, XmlFile newXmlFile) {
	    try {
	      check.scanFile(context, ruleKey, newXmlFile);
	    } catch (Exception e) {
	    	// Do nothing
	    }
	}	
		
	@Override
	public String toString() {
		return getClass().getSimpleName();
	}

	@Override
	public void describe(SensorDescriptor descriptor) {
		descriptor
			.onlyOnLanguage(Xml.KEY)
			.name("Apigee XML Sensor");
	}

	@Override
	public void execute(SensorContext context) {
		
		// Catch the context
		ApigeeXmlSensor.setContext(context);

		// Clear the BundleRecorder from eventual previous execution
		BundleRecorder.clear();

	    List<InputFile> inputFiles = new ArrayList<>();
	    fileSystem.inputFiles(mainFilesPredicate).forEach(inputFiles::add);

	    if (inputFiles.isEmpty()) {
	      return;
	    }

	    ProgressReport progressReport = new ProgressReport("Report about progress of Apigee XML analyzer", TimeUnit.SECONDS.toMillis(10));
	    progressReport.start(inputFiles.stream().map(InputFile::toString).collect(Collectors.toList()));

	    boolean cancelled = false;
	    try {
	    	
			// First loop to store ALL files.
			for (InputFile inputFile : inputFiles) {
				try {
					XmlFile xmlFile = XmlFile.create(inputFile);
					BundleRecorder.storeFile(xmlFile);
				} catch(Exception e) {
					// Case of parse exception
			    }
			}
	    	
			// Second loop to checks files one by one.
	      for (InputFile inputFile : inputFiles) {
	        if (context.isCancelled()) {
	          cancelled = true;
	          break;
	        }
			
	        try {
				XmlFile xmlFile = XmlFile.create(inputFile);
				runChecks(context, xmlFile);
			} catch(Exception e) {
				// Case of parse exception
		    }	        
	        progressReport.nextFile();
	      }
	    } finally {
	      if (!cancelled) {
	        progressReport.stop();
	      } else {
	        progressReport.cancel();
	      }
	    }		
	}

	public static SensorContext getContext() {
		return staticContext;
	}
}
