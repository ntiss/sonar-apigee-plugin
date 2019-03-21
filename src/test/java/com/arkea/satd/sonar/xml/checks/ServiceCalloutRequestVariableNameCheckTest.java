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
import org.sonar.plugins.xml.checks.ServiceCalloutRequestVariableNameCheck;
import org.sonarsource.analyzer.commons.xml.checks.SonarXmlCheck;

public class ServiceCalloutRequestVariableNameCheckTest extends AbstractCheckTester {

	private SonarXmlCheck check = new ServiceCalloutRequestVariableNameCheck();

	@Test
	public void test_ok() throws Exception {
		Collection<Issue> issues = getIssues(check, "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<ServiceCallout async=\"false\" continueOnError=\"true\" enabled=\"true\" name=\"SC-theCallout\">\r\n" + 
				"    <DisplayName>SC-theCallout</DisplayName>\r\n" + 
				"    <Request clearPayload=\"false\" variable=\"customVariableName\">\r\n" + 
				"    </Request>\r\n" + 
				"</ServiceCallout>"
		);
		assertEquals(0, issues.size());
	}
	
	@Test
	public void test_ko() throws Exception {
		Collection<Issue> issues = getIssues(check, "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<ServiceCallout async=\"false\" continueOnError=\"true\" enabled=\"true\" name=\"SC-theCallout\">\r\n" + 
				"    <DisplayName>SC-theCallout</DisplayName>\r\n" + 
				"    <Request clearPayload=\"false\" variable=\"request\">\r\n" + 
				"    </Request>\r\n" + 
				"</ServiceCallout>"
		);
		assertEquals(1, issues.size());
	}

}