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

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.sonar.api.batch.fs.InputFile.Type;
import org.sonar.api.batch.fs.internal.DefaultFileSystem;
import org.sonar.api.batch.fs.internal.DefaultInputFile;
import org.sonar.api.batch.fs.internal.FileMetadata;
import org.sonar.api.batch.fs.internal.TestInputFileBuilder;
import org.sonar.api.batch.rule.CheckFactory;
import org.sonar.api.batch.rule.internal.ActiveRulesBuilder;
import org.sonar.api.batch.sensor.internal.SensorContextTester;
import org.sonar.api.rule.RuleKey;
import org.sonar.plugins.xml.Xml;

import com.arkea.satd.sonar.xml.ApigeeXmlSensor;
import com.arkea.satd.sonar.xml.CheckRepository;

public class ApigeeXmlSensorTest extends AbstractXmlPluginTester {

	@Rule
	public TemporaryFolder temporaryFolder = new TemporaryFolder();

	private DefaultFileSystem fs;
	private ApigeeXmlSensor sensor;
	private SensorContextTester context;

	/**
	 * Expect issue for rule: DescriptionCheck
	 */
	@Test
	public void testSensor() throws Exception {

		init();
		
		// Test on one file
		DefaultInputFile inputFile = createInputFile("MyAwfulProxy/apiproxy/MyAwfulProxy.xml");
		fs.add(inputFile);

		sensor.execute(context);
		assertThat(context.allIssues())
				.extracting("ruleKey")
				.containsExactlyInAnyOrder(RuleKey.of(CheckRepository.REPOSITORY_KEY, "TooMuchProxyEndpointsCheck"),
						RuleKey.of(CheckRepository.REPOSITORY_KEY, "TooMuchTargetEndpointsCheck"));

		// Important : clean the context !!!!
		ApigeeXmlSensor.setContext(null);
	}

	@Test
	public void testSensorOnMyAwfulProxy() throws Exception {

		init();
		
		// Scan the awful bundle recursively and add all file in the context
		String basePath = "src/test/resources/MyAwfulProxy/apiproxy";
		Collection<File> allFiles = FileUtils.listFiles(new File(basePath), TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
		for(Iterator<File> it = allFiles.iterator(); it.hasNext();) {
			File f = it.next();
			String absolutePath = f.getAbsolutePath();
			DefaultInputFile inputFile = createInputFile(absolutePath);
			fs.add(inputFile);
		}

		sensor.execute(context);
		assertThat(context.allIssues()).hasSize(46);
		
		// Important : clean the context !!!!
		ApigeeXmlSensor.setContext(null);
	}	
	

	@Test
	public void testSensorOnMyAwfulSharedFlow() throws Exception {

		init();

		// Scan the awful bundle recursively and add all file in the context
		String basePath = "src/test/resources/MyAwfulSharedFlow/sharedflowbundle";
		Collection<File> allFiles = FileUtils.listFiles(new File(basePath), TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
		for(Iterator<File> it = allFiles.iterator(); it.hasNext();) {
			File f = it.next();
			String absolutePath = f.getAbsolutePath();
			DefaultInputFile inputFile = createInputFile(absolutePath);
			fs.add(inputFile);
		}

		sensor.execute(context);
		assertThat(context.allIssues()).hasSize(14);

		// Important : clean the context !!!!
		ApigeeXmlSensor.setContext(null);
	}	
	
	
	@SuppressWarnings("rawtypes")
	private void init() throws Exception {

		File moduleBaseDir = new File("src/test/resources");
		context = SensorContextTester.create(moduleBaseDir);

		fs = new DefaultFileSystem(moduleBaseDir);
		fs.setWorkDir(temporaryFolder.newFolder("temp").toPath());
	
		// Activate all rules of the CheckRepository
		ActiveRulesBuilder activeRuleBuilder = new ActiveRulesBuilder();
		for(Class check : CheckRepository.getCheckClasses()) {
			activeRuleBuilder
				.create(RuleKey.of(CheckRepository.REPOSITORY_KEY, check.getSimpleName()))
				.activate();	
		}
		CheckFactory checkFactory = new CheckFactory(activeRuleBuilder.build());

		sensor = new ApigeeXmlSensor(fs, checkFactory);
	}

	private DefaultInputFile createInputFile(String name) throws FileNotFoundException {
		DefaultInputFile inputFile = TestInputFileBuilder.create("modulekey", name)
				.setModuleBaseDir(Paths.get("src/test/resources"))
				.setType(Type.MAIN)
				.setLanguage(Xml.KEY)
				.setCharset(StandardCharsets.UTF_8).build();

		inputFile.setMetadata(new FileMetadata().readMetadata(new FileInputStream(inputFile.file()),
				StandardCharsets.UTF_8, inputFile.absolutePath()));
		return inputFile;
	}

}
