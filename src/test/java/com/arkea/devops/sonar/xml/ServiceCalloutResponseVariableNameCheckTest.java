package com.arkea.devops.sonar.xml;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.List;

import org.junit.Test;
import org.sonar.plugins.xml.checks.ServiceCalloutResponseVariableNameCheck;
import org.sonar.plugins.xml.checks.XmlIssue;
import org.sonar.plugins.xml.checks.XmlSourceCode;

public class ServiceCalloutResponseVariableNameCheckTest extends AbstractCheckTester {

	private List<XmlIssue> getIssues(String content) throws IOException {
		XmlSourceCode sourceCode = parseAndCheck(createTempFile(content), new ServiceCalloutResponseVariableNameCheck());
		return sourceCode.getXmlIssues();
	}
	
	@Test
	public void test_ok() throws Exception {
		List<XmlIssue> issues = getIssues("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<ServiceCallout async=\"false\" continueOnError=\"true\" enabled=\"true\" name=\"SC-theCallout\">\r\n" + 
				"    <DisplayName>SC-theCallout</DisplayName>\r\n" + 
				"    <Response>customVariableName</Response>\r\n" + 
				"</ServiceCallout>"
		);
		assertEquals(0, issues.size());
	}
	
	@Test
	public void test_ko() throws Exception {
		List<XmlIssue> issues = getIssues("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<ServiceCallout async=\"false\" continueOnError=\"true\" enabled=\"true\" name=\"SC-theCallout\">\r\n" + 
				"    <DisplayName>SC-theCallout</DisplayName>\r\n" + 
				"    <Response>response</Response>\r\n" + 
				"</ServiceCallout>"
		);
		assertEquals(1, issues.size());
	}
}