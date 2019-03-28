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

import static org.junit.Assert.assertEquals;

import java.util.Collection;

import org.junit.Test;
import org.sonar.api.batch.sensor.issue.Issue;
import org.sonarsource.analyzer.commons.xml.checks.SonarXmlCheck;

import com.arkea.satd.sonar.xml.checks.PolicyDisplayNameCheck;

public class PolicyDisplayNameCheckTest extends AbstractCheckTester {

	private SonarXmlCheck check = new PolicyDisplayNameCheck();
	
	@Test
	public void test_ok1() throws Exception {
		Collection<Issue> issues = getIssues(check,"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
			"<RaiseFault async=\"false\" continueOnError=\"false\" enabled=\"true\" name=\"Raise-Fault-1\">\r\n" + 
			"    <DisplayName>Raise-Fault-1</DisplayName>\r\n" + 
			"</RaiseFault>");
		assertEquals(0, issues.size());
	}

	@Test
	public void test_ok2() throws Exception {
		Collection<Issue> issues = getIssues(check, "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<RaiseFault async=\"false\" continueOnError=\"false\" enabled=\"true\" name=\"Raise-Fault-1\">\r\n" + 
				"</RaiseFault>");
		assertEquals(0, issues.size());
	}

	
	@Test
	public void test_bad_displayName1() throws Exception {
		Collection<Issue> issues = getIssues(check, "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<RaiseFault async=\"false\" continueOnError=\"false\" enabled=\"true\" name=\"Raise-Fault-1\">\r\n" + 
				"    <DisplayName>Raise Fault 1</DisplayName>\r\n" + 
				"</RaiseFault>");
		assertEquals(1, issues.size());
	}
	
	@Test
	public void test_bad_displayName2() throws Exception {
		Collection<Issue> issues = getIssues(check, "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<RaiseFault async=\"false\" continueOnError=\"false\" enabled=\"true\" name=\"Raise-Fault-1\">\r\n" + 
				"    <DisplayName>Raise:Fault:1</DisplayName>\r\n" + 
				"</RaiseFault>");
		assertEquals(1, issues.size());
	}
	
	@Test
	public void test_bad_displayName3() throws Exception {
		Collection<Issue> issues = getIssues(check, "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<RaiseFault async=\"false\" continueOnError=\"false\" enabled=\"true\" name=\"Raise:Fault:1\">\r\n" + 
				"    <DisplayName>Raise-Fault-1</DisplayName>\r\n" + 
				"</RaiseFault>");
		assertEquals(1, issues.size());
	}
		
}