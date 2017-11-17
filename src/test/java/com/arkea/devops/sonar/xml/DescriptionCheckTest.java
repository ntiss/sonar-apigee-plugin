package com.arkea.devops.sonar.xml;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.List;

import org.junit.Test;
import org.sonar.plugins.xml.checks.DescriptionCheck;
import org.sonar.plugins.xml.checks.XmlIssue;
import org.sonar.plugins.xml.checks.XmlSourceCode;

public class DescriptionCheckTest extends AbstractCheckTester {

	@Test
	public void test_ok() throws Exception {
		List<XmlIssue> issues = getIssues(
			"<APIProxy revision=\"1\" name=\"XXX\">\r\n" + 
			"    <Description>Text of description</Description>\r\n" + 
			"</APIProxy>"
		);
		assertEquals(0, issues.size());
	}
	
	@Test
	public void test_too_short() throws Exception {
		List<XmlIssue> issues = getIssues(
			"<APIProxy revision=\"1\" name=\"XXX\">\r\n" + 
			"    <Description>short</Description>\r\n" + 
			"</APIProxy>"
		);
		assertEquals(1, issues.size());
	}	
	
	private List<XmlIssue> getIssues(String content) throws IOException {
		XmlSourceCode sourceCode = parseAndCheck(createTempFile(content), new DescriptionCheck());
		return sourceCode.getXmlIssues();
	}

}