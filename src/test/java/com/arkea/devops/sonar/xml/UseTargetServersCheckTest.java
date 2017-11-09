package com.arkea.devops.sonar.xml;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.List;

import org.junit.Test;
import org.sonar.plugins.xml.checks.XmlIssue;
import org.sonar.plugins.xml.checks.XmlSourceCode;

import com.arkea.satd.sonar.xml.checks.UseTargetServersCheck;

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
	public void test_one_target() throws Exception {
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
		assertEquals(1, issues.size());
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