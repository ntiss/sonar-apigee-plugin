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

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.List;

import org.junit.Test;
import org.sonar.plugins.xml.checks.UnreachableRouteRuleCheck;
import org.sonar.plugins.xml.checks.XmlIssue;
import org.sonar.plugins.xml.checks.XmlSourceCode;

public class UnreachableRouteRuleCheckTest extends AbstractCheckTester {

	private List<XmlIssue> getIssues(String content) throws IOException {
		XmlSourceCode sourceCode = parseAndCheck(createTempFile(content), new UnreachableRouteRuleCheck());
		return sourceCode.getXmlIssues();
	}
	
	@Test
	public void test_ok() throws Exception {
		List<XmlIssue> issues = getIssues("<ProxyEndpoint name=\"default\">\r\n" + 
			"    <RouteRule name=\"default1\">\r\n" + 
			"        <TargetEndpoint>default1</TargetEndpoint>\r\n" + 
			"        <Condition>aCondition1</Condition>\r\n" + 
			"    </RouteRule>\r\n" + 
			"    <RouteRule name=\"default2\">\r\n" + 
			"        <TargetEndpoint>default2</TargetEndpoint>\r\n" + 
			"        <Condition>aCondition2</Condition>\r\n" + 
			"    </RouteRule>\r\n" + 
			"</ProxyEndpoint>");
		
		assertEquals(0, issues.size());
	}
	
	@Test
	public void test_ko1_two_no_cond() throws Exception {
		List<XmlIssue> issues = getIssues("<ProxyEndpoint name=\"default\">\r\n" + 
				"    <RouteRule name=\"default1\">\r\n" + 
				"        <TargetEndpoint>default1</TargetEndpoint>\r\n" + 
				"    </RouteRule>\r\n" + 
				"    <RouteRule name=\"default2\">\r\n" + 
				"        <TargetEndpoint>default2</TargetEndpoint>\r\n" + 
				"    </RouteRule>\r\n" + 
				"</ProxyEndpoint>");
		assertEquals(2, issues.size());
	}	
	
	@Test
	public void test_ko2_two_no_cond() throws Exception {
		List<XmlIssue> issues = getIssues("<ProxyEndpoint name=\"default\">\r\n" + 
				"    <RouteRule name=\"default1\">\r\n" + 
				"        <TargetEndpoint>default1</TargetEndpoint>\r\n" + 
				"    </RouteRule>\r\n" + 
				"    <RouteRule name=\"default2\">\r\n" + 
				"        <TargetEndpoint>default2</TargetEndpoint>\r\n" + 
				"        <Condition>true</Condition>\r\n" + 
				"    </RouteRule>\r\n" + 
				"</ProxyEndpoint>");
		assertEquals(2, issues.size());
	}	
		
	@Test
	public void test_ko3_two_no_cond() throws Exception {
		List<XmlIssue> issues = getIssues("<ProxyEndpoint name=\"default\">\r\n" + 
				"    <RouteRule name=\"default1\">\r\n" + 
				"        <TargetEndpoint>default1</TargetEndpoint>\r\n" + 
				"        <Condition></Condition>\r\n" + 
				"    </RouteRule>\r\n" + 
				"    <RouteRule name=\"default2\">\r\n" + 
				"        <TargetEndpoint>default2</TargetEndpoint>\r\n" + 
				"        <Condition>true</Condition>\r\n" + 
				"    </RouteRule>\r\n" + 
				"</ProxyEndpoint>");
		assertEquals(2, issues.size());
	}	
		
}