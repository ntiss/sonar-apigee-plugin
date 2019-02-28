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
package com.arkea.satd.sonar.xml.checks;

import static org.junit.Assert.assertEquals;

import java.util.Collection;

import org.junit.Test;
import org.sonar.api.batch.sensor.issue.Issue;
import org.sonar.api.rule.RuleKey;
import org.sonar.plugins.xml.Xml;
import org.sonar.plugins.xml.checks.AvoidPythonCheck;
import org.sonarsource.analyzer.commons.xml.checks.SonarXmlCheck;


public class AvoidPythonCheckTest extends AbstractCheckTester {

	private SonarXmlCheck check = new AvoidPythonCheck();
	
	@Test
	public void test_ok() throws Exception {
		
		Collection<Issue> issues = getIssues(check, 
				"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<Script async=\"false\" continueOnError=\"false\" enabled=\"true\" name=\"theName\">\r\n" + 
				"    <DisplayName>theDisplayName</DisplayName>\r\n" + 
				"    <Properties/>\r\n" + 
				"    <ResourceURL>jsc://script.js</ResourceURL>\r\n" + 
				"</Script>");
		
		assertEquals(0, issues.size());
	}
	
	@Test
	public void test_ko1() throws Exception {
	
		Collection<Issue> issues = getIssues(check, 
				"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<Script async=\"false\" continueOnError=\"false\" enabled=\"true\" name=\"theName\">\r\n" + 
				"    <DisplayName>theDisplayName</DisplayName>\r\n" + 
				"    <Properties/>\r\n" + 
				"    <ResourceURL>py://script.py</ResourceURL>\r\n" + 
				"</Script>");

		assertEquals(1, issues.size());
	}

	@Test
	public void test_ko2() throws Exception {
		
		Collection<Issue> issues = getIssues(check, 
				"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<Script async=\"false\" continueOnError=\"false\" enabled=\"true\" name=\"theName\">\r\n" + 
				"    <DisplayName>theDisplayName</DisplayName>\r\n" + 
				"    <Properties/>\r\n" + 
				"    <ResourceURL>py://script.py</ResourceURL>\r\n" + 
				"    <ResourceURL>py://script.py</ResourceURL>\r\n" +  // Blocked by Apigee, should never happen 
				"</Script>");
	
		assertEquals(1, issues.size());
	}	
	
}