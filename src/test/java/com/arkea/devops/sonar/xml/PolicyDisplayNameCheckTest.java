package com.arkea.devops.sonar.xml;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.List;

import org.junit.Test;
import org.sonar.plugins.xml.checks.PolicyDisplayNameCheck;
import org.sonar.plugins.xml.checks.XmlIssue;
import org.sonar.plugins.xml.checks.XmlSourceCode;

public class PolicyDisplayNameCheckTest extends AbstractCheckTester {

	private List<XmlIssue> getIssues(String content) throws IOException {
		XmlSourceCode sourceCode = parseAndCheck(createTempFile(content), new PolicyDisplayNameCheck());
		return sourceCode.getXmlIssues();
	}

	@Test
	public void test_ok1() throws Exception {
		List<XmlIssue> issues = getIssues("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<RaiseFault async=\"false\" continueOnError=\"false\" enabled=\"true\" name=\"Raise-Fault-1\">\r\n" + 
				"    <DisplayName>Raise-Fault-1</DisplayName>\r\n" + 
				"</RaiseFault>");
		assertEquals(0, issues.size());
	}

	@Test
	public void test_ok2() throws Exception {
		List<XmlIssue> issues = getIssues("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<RaiseFault async=\"false\" continueOnError=\"false\" enabled=\"true\" name=\"Raise-Fault-1\">\r\n" + 
				"</RaiseFault>");
		assertEquals(0, issues.size());
	}

	
	@Test
	public void test_bad_displayName() throws Exception {
		List<XmlIssue> issues = getIssues("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<RaiseFault async=\"false\" continueOnError=\"false\" enabled=\"true\" name=\"Raise-Fault-1\">\r\n" + 
				"    <DisplayName>Raise Fault 1</DisplayName>\r\n" + 
				"</RaiseFault>");
		assertEquals(1, issues.size());
	}
	
	
}