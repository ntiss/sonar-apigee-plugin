package com.arkea.devops.sonar.xml;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.List;

import org.junit.Test;
import org.sonar.plugins.xml.checks.AvoidPythonCheck;
import org.sonar.plugins.xml.checks.XmlIssue;
import org.sonar.plugins.xml.checks.XmlSourceCode;

public class AvoidPythonCheckTest extends AbstractCheckTester {

	private List<XmlIssue> getIssues(String content) throws IOException {
		XmlSourceCode sourceCode = parseAndCheck(createTempFile(content), new AvoidPythonCheck());
		return sourceCode.getXmlIssues();
	}

	
	@Test
	public void test_ok() throws Exception {
		List<XmlIssue> issues = getIssues(
			"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
			"<Script async=\"false\" continueOnError=\"false\" enabled=\"true\" name=\"theName\">\r\n" + 
			"    <DisplayName>theDisplayName</DisplayName>\r\n" + 
			"    <Properties/>\r\n" + 
			"    <ResourceURL>jsc://script.js</ResourceURL>\r\n" + 
			"</Script>"
		);
		assertEquals(0, issues.size());
	}
	
	@Test
	public void test_ko1() throws Exception {
		List<XmlIssue> issues = getIssues(
				"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
						"<Script async=\"false\" continueOnError=\"false\" enabled=\"true\" name=\"theName\">\r\n" + 
						"    <DisplayName>theDisplayName</DisplayName>\r\n" + 
						"    <Properties/>\r\n" + 
						"    <ResourceURL>py://script.py</ResourceURL>\r\n" + 
						"</Script>"
					);
		assertEquals(1, issues.size());
	}

	@Test
	public void test_ko2() throws Exception {
		List<XmlIssue> issues = getIssues(
				"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
						"<Script async=\"false\" continueOnError=\"false\" enabled=\"true\" name=\"theName\">\r\n" + 
						"    <DisplayName>theDisplayName</DisplayName>\r\n" + 
						"    <Properties/>\r\n" + 
						"    <ResourceURL>py://script.py</ResourceURL>\r\n" + 
						"    <ResourceURL>py://script.py</ResourceURL>\r\n" +  // Blocked by Apigee, should never happen 
						"</Script>"
					);
		assertEquals(1, issues.size());
	}		
}