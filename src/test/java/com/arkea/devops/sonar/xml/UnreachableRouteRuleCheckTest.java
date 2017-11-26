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