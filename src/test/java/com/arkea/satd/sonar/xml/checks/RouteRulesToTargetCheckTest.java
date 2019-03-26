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
import org.sonar.plugins.xml.checks.BundleRecorder;
import org.sonar.plugins.xml.checks.RouteRulesToTargetCheck;
import org.sonarsource.analyzer.commons.xml.XmlFile;
import org.sonarsource.analyzer.commons.xml.checks.SonarXmlCheck;

public class RouteRulesToTargetCheckTest extends AbstractCheckTester {

	private SonarXmlCheck check = new RouteRulesToTargetCheck();

	@Test
	public void test_ok() throws Exception {
		
		// Fake TargetEndpoint file
		XmlFile targetEndpointXML = createTempFile("targetEndpoint.xml", "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<TargetEndpoint name=\"existingTarget\">\r\n" + 
				"</TargetEndpoint>"
				);
		BundleRecorder.clear();
		BundleRecorder.storeFile(targetEndpointXML);

		Collection<Issue> issues = getIssues(check, "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<ProxyEndpoint name=\"default\">\r\n" + 
				"    <Description/>\r\n" + 
				"    <RouteRule name=\"myTarget\">\r\n" + 
				"        <TargetEndpoint>existingTarget</TargetEndpoint>\r\n" + 
				"    </RouteRule>\r\n" + 
				"</ProxyEndpoint>");
		assertEquals(0, issues.size());
	}

	
	@Test
	public void test_ko1() throws Exception {
		
		Collection<Issue> issues = getIssues(check, "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<ProxyEndpoint name=\"default\">\r\n" + 
				"    <Description/>\r\n" + 
				"    <RouteRule name=\"myTarget\">\r\n" + 
				"        <TargetEndpoint>inexistantTarget</TargetEndpoint>\r\n" + 
				"    </RouteRule>\r\n" + 
				"</ProxyEndpoint>");
		assertEquals(1, issues.size());
	}

	@Test
	public void test_ko2() throws Exception {
		
		// Fake TargetEndpoint file
		XmlFile targetEndpointXML = createTempFile("targetEndpoint.xml", "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<TargetEndpoint name=\"anotherExistingTarget\">\r\n" + 
				"</TargetEndpoint>"
				);
		BundleRecorder.clear();
		BundleRecorder.storeFile(targetEndpointXML);

		Collection<Issue> issues = getIssues(check, "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<ProxyEndpoint name=\"default\">\r\n" + 
				"    <Description/>\r\n" + 
				"    <RouteRule name=\"myTarget\">\r\n" + 
				"        <TargetEndpoint>aTarget</TargetEndpoint>\r\n" + 
				"    </RouteRule>\r\n" + 
				"</ProxyEndpoint>");
		assertEquals(1, issues.size());
	}	
	
}