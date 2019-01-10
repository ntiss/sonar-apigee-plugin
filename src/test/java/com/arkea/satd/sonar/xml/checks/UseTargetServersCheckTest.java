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
import org.sonar.plugins.xml.checks.UseTargetServersCheck;
import org.sonar.plugins.xml.checks.XmlIssue;
import org.sonar.plugins.xml.checks.XmlSourceCode;

public class UseTargetServersCheckTest extends AbstractCheckTester {

	private List<XmlIssue> getIssues(String content) throws IOException {
		XmlSourceCode sourceCode = parseAndCheck(createTempFile(content), new UseTargetServersCheck());
		return sourceCode.getXmlIssues();
	}
	
	@Test
	public void test_ok() throws Exception {
		List<XmlIssue> issues = getIssues("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<ProxyEndpoint name=\"default\">\r\n" + 
				"    <RouteRule name=\"NoRoute\">\r\n" + 
				"        <Condition>request.verb == \"OPTIONS\"</Condition>\r\n" + 
				"    </RouteRule>\r\n" + 
				"    <RouteRule name=\"firstRoute\">\r\n" + 
				"        <Condition>request.header.route = \"firstRoute\"</Condition>\r\n" + 
				"		<TargetEndpoint>firstRoute</TargetEndpoint>" +
				"    </RouteRule>\r\n" + 
				"    <RouteRule name=\"default\">\r\n" + 
				"		<TargetEndpoint>default</TargetEndpoint>" +
				"    </RouteRule>\r\n" + 
				"</ProxyEndpoint>"

		);
		assertEquals(0, issues.size());
	}
	
	@Test
	public void test_no_route() throws Exception {
		List<XmlIssue> issues = getIssues("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<ProxyEndpoint name=\"default\">\r\n" + 
				"    <RouteRule name=\"NoRoute\">\r\n" + 
				"        <Condition>request.verb == \"OPTIONS\"</Condition>\r\n" + 
				"    </RouteRule>\r\n" + 
				"</ProxyEndpoint>"

		);
		assertEquals(0, issues.size());
	}	
	
	@Test
	public void test_one_target_without_no_route() throws Exception {
		List<XmlIssue> issues = getIssues("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<ProxyEndpoint name=\"default\">\r\n" + 
				"    <RouteRule name=\"default\">\r\n" + 
				"		<TargetEndpoint>default</TargetEndpoint>" +
				"    </RouteRule>\r\n" + 
				"</ProxyEndpoint>"

		);
		assertEquals(1, issues.size());
	}
	
	@Test
	public void test_one_target_with_no_route() throws Exception {
		List<XmlIssue> issues = getIssues("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<ProxyEndpoint name=\"default\">\r\n" + 
				"    <RouteRule name=\"NoRoute\">\r\n" + 
				"        <Condition>request.verb == \"OPTIONS\"</Condition>\r\n" + 
				"    </RouteRule>\r\n" + 
				"    <RouteRule name=\"default\">\r\n" + 
				"		<TargetEndpoint>default</TargetEndpoint>" +
				"    </RouteRule>\r\n" + 
				"</ProxyEndpoint>"

		);
		assertEquals(0, issues.size());
	}
	
	
	@Test
	public void test_same_target() throws Exception {
		List<XmlIssue> issues = getIssues("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<ProxyEndpoint name=\"default\">\r\n" + 
				"    <RouteRule name=\"NoRoute\">\r\n" + 
				"        <Condition>request.verb == \"OPTIONS\"</Condition>\r\n" + 
				"    </RouteRule>\r\n" + 
				"    <RouteRule name=\"firstRoute\">\r\n" + 
				"        <Condition>request.header.route = \"firstRoute\"</Condition>\r\n" + 
				"		<TargetEndpoint>firstRoute</TargetEndpoint>" +
				"    </RouteRule>\r\n" + 
				"    <RouteRule name=\"default\">\r\n" + 
				"		<TargetEndpoint>firstRoute</TargetEndpoint>" +
				"    </RouteRule>\r\n" + 
				"</ProxyEndpoint>"

		);
		assertEquals(1, issues.size());
	}	
		
}