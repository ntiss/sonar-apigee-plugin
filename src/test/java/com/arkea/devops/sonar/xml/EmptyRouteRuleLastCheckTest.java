package com.arkea.devops.sonar.xml;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.List;

import org.junit.Test;
import org.sonar.plugins.xml.checks.XmlIssue;
import org.sonar.plugins.xml.checks.XmlSourceCode;

import com.arkea.satd.sonar.xml.checks.EmptyRouteRuleLastCheck;

public class EmptyRouteRuleLastCheckTest extends AbstractCheckTester {

	private List<XmlIssue> getIssues(String content) throws IOException {
		XmlSourceCode sourceCode = parseAndCheck(createTempFile(content), new EmptyRouteRuleLastCheck());
		return sourceCode.getXmlIssues();
	}
	
	@Test
	public void test_ok() throws Exception {
		List<XmlIssue> issues = getIssues("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<ProxyEndpoint name=\"default\">\r\n" + 
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
	public void test_empty_route() throws Exception {
		List<XmlIssue> issues = getIssues("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<ProxyEndpoint name=\"default\">\r\n" + 
				"    <RouteRule name=\"NoRoute\">\r\n" + 
				"    </RouteRule>\r\n" + 
				"</ProxyEndpoint>"

		);
		assertEquals(0, issues.size());
	}	
	
	@Test
	public void test_no_cond() throws Exception {
		List<XmlIssue> issues = getIssues("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<ProxyEndpoint name=\"default\">\r\n" + 
				"    <RouteRule name=\"firstRoute\">\r\n" + 
				"        <Condition>request.header.route = \"firstRoute\"</Condition>\r\n" + 
				"		<TargetEndpoint>firstRoute</TargetEndpoint>" +
				"    </RouteRule>\r\n" + 
				"    <RouteRule name=\"firstRoute\">\r\n" + 
				"		<TargetEndpoint>firstRoute</TargetEndpoint>" +
				"    </RouteRule>\r\n" + 
				"    <RouteRule name=\"default\">\r\n" + 
				"		<TargetEndpoint>default</TargetEndpoint>" +
				"    </RouteRule>\r\n" + 
				"</ProxyEndpoint>"

		);
		assertEquals(1, issues.size());
	}
	
	@Test
	public void test_multiple() throws Exception {
		List<XmlIssue> issues = getIssues("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<ProxyEndpoint name=\"default\">\r\n" + 
				"    <RouteRule name=\"route1\">\r\n" + 
				"        <Condition/>\r\n" + 
				"		<TargetEndpoint>route1</TargetEndpoint>" +
				"    </RouteRule>\r\n" + 
				"    <RouteRule name=\"route2\">\r\n" + 
				"        <Condition>request.header.route = \"firstRoute\"</Condition>\r\n" + 
				"		<TargetEndpoint>route2</TargetEndpoint>" +
				"    </RouteRule>\r\n" + 
				"    <RouteRule name=\"route3\">\r\n" + 
				"        <Condition>true</Condition>\r\n" + 
				"		<TargetEndpoint>route3</TargetEndpoint>" +
				"    </RouteRule>\r\n" + 
				"    <RouteRule name=\"route4\">\r\n" + 
				"        <Condition>request.header.route = \"firstRoute\"</Condition>\r\n" + 
				"		<TargetEndpoint>route4</TargetEndpoint>" +
				"    </RouteRule>\r\n" + 
				"    <RouteRule name=\"route5\">\r\n" + 
				"		<TargetEndpoint>route5</TargetEndpoint>" +
				"    </RouteRule>\r\n" + 
				"    <RouteRule name=\"default\">\r\n" + 
				"		<TargetEndpoint>default</TargetEndpoint>" +
				"    </RouteRule>\r\n" + 
				"</ProxyEndpoint>"

		);
		assertEquals(3, issues.size());
	}	
		
}