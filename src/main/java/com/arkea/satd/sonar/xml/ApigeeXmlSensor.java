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

import static org.sonar.plugins.xml.compat.CompatibilityHelper.wrap;

import org.sonar.api.batch.fs.FilePredicate;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.rule.CheckFactory;
import org.sonar.api.batch.rule.Checks;
import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.api.batch.sensor.issue.NewIssue;
import org.sonar.api.batch.sensor.issue.NewIssueLocation;
import org.sonar.plugins.xml.checks.AbstractXmlCheck;
import org.sonar.plugins.xml.checks.BundleRecorder;
import org.sonar.plugins.xml.checks.XmlFile;
import org.sonar.plugins.xml.checks.XmlIssue;
import org.sonar.plugins.xml.checks.XmlSourceCode;
import org.sonar.plugins.xml.compat.CompatibleInputFile;
import org.sonar.plugins.xml.language.Xml;
import org.sonar.plugins.xml.parsers.ParseException;

import com.arkea.satd.sonar.xml.checks.CheckRepository;

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
		this.checks = checkFactory.create(CheckRepository.REPOSITORY_KEY).addAnnotatedChecks((Iterable<?>) CheckRepository.getChecks());
		this.fileSystem = fileSystem;
		this.mainFilesPredicate = fileSystem.predicates().and(fileSystem.predicates().hasType(InputFile.Type.MAIN), fileSystem.predicates().hasLanguage(Xml.KEY));
	}

	public void analyse(SensorContext sensorContext) {
		execute(sensorContext);
	}

	private void runChecks(SensorContext context, XmlFile xmlFile) {
		XmlSourceCode sourceCode = new XmlSourceCode(xmlFile);

		// Do not execute any XML rule when an XML file is corrupted (SONARXML-13)
		try {
			sourceCode.parseSource();
			for (Object check : checks.all()) {
				((AbstractXmlCheck) check).setRuleKey(checks.ruleKey(check));
				((AbstractXmlCheck) check).validate(sourceCode);
			}
			saveIssue(context, sourceCode);
		} catch(ParseException e) {
			// Do nothing
		}

	}

	public static void saveIssue(SensorContext context, XmlSourceCode sourceCode) {
		if(context!=null) {
			for (XmlIssue xmlIssue : sourceCode.getXmlIssues()) {
				NewIssue newIssue = context.newIssue().forRule(xmlIssue.getRuleKey());
				NewIssueLocation location = newIssue.newLocation().on(sourceCode.getInputFile().wrapped())
						.message(xmlIssue.getMessage());
				if (xmlIssue.getLine() != null) {
					location.at(sourceCode.getInputFile().selectLine(xmlIssue.getLine()));
				}
				newIssue.at(location).save();
			}
		}
	}

	@Override
	public String toString() {
		return getClass().getSimpleName();
	}

	@Override
	public void describe(SensorDescriptor descriptor) {
		descriptor.onlyOnLanguage(Xml.KEY).name("Apigee XML Sensor");
	}

	@Override
	public void execute(SensorContext context) {
		// Catch the context
		ApigeeXmlSensor.setContext(context);
		
		// First loop to store ALL files.
		for (CompatibleInputFile inputFile : wrap(fileSystem.inputFiles(mainFilesPredicate), context)) {
			XmlFile xmlFile = new XmlFile(inputFile, fileSystem);
			XmlSourceCode xmlSourceCode = new XmlSourceCode(xmlFile);
			BundleRecorder.storeFile(xmlSourceCode);
		}
		
		// Second loop to checks files one by one.
		for (CompatibleInputFile inputFile : wrap(fileSystem.inputFiles(mainFilesPredicate), context)) {
			XmlFile xmlFile = new XmlFile(inputFile, fileSystem);
			runChecks(context, xmlFile);
		}
	}

	public static SensorContext getContext() {
		return staticContext;
	}
}
