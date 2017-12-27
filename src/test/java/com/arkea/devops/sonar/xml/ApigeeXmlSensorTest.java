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
package com.arkea.devops.sonar.xml;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.fs.internal.DefaultFileSystem;
import org.sonar.api.batch.fs.internal.DefaultInputFile;
import org.sonar.api.batch.fs.internal.FileMetadata;
import org.sonar.api.batch.rule.ActiveRules;
import org.sonar.api.batch.rule.CheckFactory;
import org.sonar.api.batch.rule.internal.ActiveRulesBuilder;
import org.sonar.api.batch.sensor.internal.SensorContextTester;
import org.sonar.api.measures.FileLinesContext;
import org.sonar.api.measures.FileLinesContextFactory;
import org.sonar.api.rule.RuleKey;
import org.sonar.plugins.xml.language.Xml;

import com.arkea.satd.sonar.xml.ApigeeXmlSensor;
import com.arkea.satd.sonar.xml.checks.CheckRepository;

public class ApigeeXmlSensorTest extends AbstractXmlPluginTester {

  @Rule
  public TemporaryFolder temporaryFolder = new TemporaryFolder();

  private DefaultFileSystem fs;
  private ApigeeXmlSensor sensor;
  private SensorContextTester context;
  private final RuleKey ruleKey = RuleKey.of(CheckRepository.REPOSITORY_KEY, "DescriptionCheck");


  /**
   * Expect issue for rule: DescriptionCheck
   */
  @Test
  public void testSensor() throws Exception {
	  
//Thread.sleep(5000);
	  
    init();
    fs.add(createInputFile("apiproxy/MyAwfulProxy.xml"));

    sensor.analyse(context);
    assertThat(context.allIssues()).extracting("ruleKey").containsOnly(ruleKey);
    
    // Important : clean the context !!!!
    ApigeeXmlSensor.setContext(null);
  }



  private void init() throws Exception {
    File moduleBaseDir = new File("src/test/resources");
    context = SensorContextTester.create(moduleBaseDir);

    fs = new DefaultFileSystem(moduleBaseDir);
    fs.setWorkDir(temporaryFolder.newFolder("temp"));

    ActiveRules activeRules = new ActiveRulesBuilder()
        .create(ruleKey)
        .activate()
        .build();
    
    CheckFactory checkFactory = new CheckFactory(activeRules);

    FileLinesContextFactory fileLinesContextFactory = mock(FileLinesContextFactory.class);
    when(fileLinesContextFactory.createFor(any(InputFile.class))).thenReturn(mock(FileLinesContext.class));

    sensor = new ApigeeXmlSensor(fs, checkFactory);
  }

  private DefaultInputFile createInputFile(String name) {
    DefaultInputFile defaultInputFile = new DefaultInputFile("modulekey", name)
      .setModuleBaseDir(Paths.get("src/test/resources"))
      .setType(InputFile.Type.MAIN)
      .setLanguage(Xml.KEY)
      .setCharset(StandardCharsets.UTF_8);
    defaultInputFile.initMetadata(new FileMetadata().readMetadata(defaultInputFile.file(), StandardCharsets.UTF_8));
    return defaultInputFile;
  }


}
