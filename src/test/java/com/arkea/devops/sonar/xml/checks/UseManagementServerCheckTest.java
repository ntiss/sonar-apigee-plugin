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
package com.arkea.devops.sonar.xml.checks;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.List;

import org.junit.Test;
import org.sonar.plugins.xml.checks.UseManagementServerCheck;
import org.sonar.plugins.xml.checks.XmlIssue;
import org.sonar.plugins.xml.checks.XmlSourceCode;

public class UseManagementServerCheckTest extends AbstractCheckTester {

	private List<XmlIssue> getIssues(String content) throws IOException {
		XmlSourceCode sourceCode = parseAndCheck(createTempFile(content), new UseManagementServerCheck());
		return sourceCode.getXmlIssues();
	}

	@Test
	public void test_ok() throws Exception {
		List<XmlIssue> issues = getIssues("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<ServiceCallout async=\"false\" continueOnError=\"false\" enabled=\"true\" name=\"SC-Callout\">\r\n" + 
				"    <DisplayName>Callout</DisplayName>\r\n" + 
				"    <HTTPTargetConnection>\r\n" + 
				"        <URL>http://localhost:8080/v1/something</URL>\r\n" + 
				"    </HTTPTargetConnection>\r\n" + 
				"</ServiceCallout>"
		);
		assertEquals(0, issues.size());
	}
	
	@Test
	public void test_ko_in_targetEndpoint() throws Exception {
		List<XmlIssue> issues = getIssues("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<TargetEndpoint name=\"default\">\r\n" + 
				"    <HTTPTargetConnection>\r\n" + 
				"        <URL>http://enterprise.apigee.com/v1/dadadadada</URL>\r\n" + 
				"    </HTTPTargetConnection>\r\n" + 
				"</TargetEndpoint>"
		);
		assertEquals(1, issues.size());
	}
	
	@Test
	public void test_ko_in_serviceCallout() throws Exception {
		List<XmlIssue> issues = getIssues("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<ServiceCallout async=\"false\" continueOnError=\"false\" enabled=\"true\" name=\"SC-Callout\">\r\n" + 
				"    <DisplayName>Callout</DisplayName>\r\n" + 
				"    <HTTPTargetConnection>\r\n" + 
				"        <URL>http://{serverHost}:{serverPort}/v1/organizations</URL>\r\n" + 
				"    </HTTPTargetConnection>\r\n" + 
				"</ServiceCallout>"
		);
		assertEquals(1, issues.size());
	}

}