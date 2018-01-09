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

import java.io.IOException;
import java.util.List;

import org.junit.Test;
import org.sonar.plugins.xml.checks.BundleRecorder;
import org.sonar.plugins.xml.checks.RouteRulesToTargetCheck;
import org.sonar.plugins.xml.checks.XmlIssue;
import org.sonar.plugins.xml.checks.XmlSourceCode;

public class RouteRulesToTargetCheckTest extends AbstractCheckTester {

	private List<XmlIssue> getIssues(String content) throws IOException {
		XmlSourceCode sourceCode = parseAndCheck(createTempFile(content), new RouteRulesToTargetCheck());
		return sourceCode.getXmlIssues();
	}

	@Test
	public void test_ok() throws Exception {
		
		// Fake TargetEndpoint file
		XmlSourceCode targetEndpointXML = parse(createTempFile("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<TargetEndpoint name=\"existingTarget\">\r\n" + 
				"</TargetEndpoint>"
				));
		BundleRecorder.clear();
		BundleRecorder.storeFile(targetEndpointXML);

		String theEndpoint = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<ProxyEndpoint name=\"default\">\r\n" + 
				"    <Description/>\r\n" + 
				"    <RouteRule name=\"myTarget\">\r\n" + 
				"        <TargetEndpoint>existingTarget</TargetEndpoint>\r\n" + 
				"    </RouteRule>\r\n" + 
				"</ProxyEndpoint>";
		BundleRecorder.storeFile(parse(createTempFile(theEndpoint)));
		
		List<XmlIssue> issues = getIssues(theEndpoint);
		assertEquals(0, issues.size());
	}

	
	@Test
	public void test_ko() throws Exception {
		
		BundleRecorder.clear();
		String theEndpoint = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<ProxyEndpoint name=\"default\">\r\n" + 
				"    <Description/>\r\n" + 
				"    <RouteRule name=\"myTarget\">\r\n" + 
				"        <TargetEndpoint>inexistantTarget</TargetEndpoint>\r\n" + 
				"    </RouteRule>\r\n" + 
				"</ProxyEndpoint>";
		BundleRecorder.storeFile(parse(createTempFile(theEndpoint)));
		
		List<XmlIssue> issues = getIssues(theEndpoint);
		assertEquals(1, issues.size());
	}
	
}