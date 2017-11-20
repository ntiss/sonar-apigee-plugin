package com.arkea.devops.sonar.xml;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.List;

import org.junit.Test;
import org.sonar.plugins.xml.checks.FaultRuleConditionCheck;
import org.sonar.plugins.xml.checks.XmlIssue;
import org.sonar.plugins.xml.checks.XmlSourceCode;

public class FaultRuleConditionCheckTest extends AbstractCheckTester {

	private List<XmlIssue> getIssues(String content) throws IOException {
		XmlSourceCode sourceCode = parseAndCheck(createTempFile(content), new FaultRuleConditionCheck());
		return sourceCode.getXmlIssues();
	}
	
	@Test
	public void test_ok() throws Exception {
		List<XmlIssue> issues = getIssues("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
			"<ProxyEndpoint name=\"default\">\r\n" + 
			"    <FaultRules>\r\n" + 
			"        <FaultRule name=\"baderror\">\r\n" + 
			"          <Condition>message.status.code=403</Condition>\r\n" + 
			"          <Step>\r\n" + 
			"            <Name>RF-Raise403</Name>\r\n" + 
			"          </Step>\r\n" + 
			"        </FaultRule>\r\n" + 
			"    </FaultRules>\r\n" + 
			"</ProxyEndpoint>"
		);
		assertEquals(0, issues.size());
	}
	
	@Test
	public void test_ko1() throws Exception {
		List<XmlIssue> issues = getIssues("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<ProxyEndpoint name=\"default\">\r\n" + 
				"    <FaultRules>\r\n" + 
				"        <FaultRule name=\"baderror\">\r\n" + 
				"          <Step>\r\n" + 
				"            <Name>RF-Raise403</Name>\r\n" + 
				"            <Condition>message.status.code=403</Condition>\r\n" + 
				"          </Step>\r\n" + 
				"        </FaultRule>\r\n" + 
				"    </FaultRules>\r\n" + 
				"</ProxyEndpoint>"
					);
		assertEquals(1, issues.size());
	}

	@Test
	public void test_ko2() throws Exception {
		List<XmlIssue> issues = getIssues("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<ProxyEndpoint name=\"default\">\r\n" + 
				"    <FaultRules>\r\n" + 
				"        <FaultRule name=\"baderror1\">\r\n" + 
				"          <Condition></Condition>\r\n" + 
				"          <Step>\r\n" + 
				"            <Name>RF-Raise403</Name>\r\n" + 
				"          </Step>\r\n" + 
				"        </FaultRule>\r\n" + 
				"        <FaultRule name=\"baderror2\">\r\n" + 
				"          <Condition/>\r\n" + 
				"          <Step>\r\n" + 
				"            <Name>RF-Raise403</Name>\r\n" + 
				"          </Step>\r\n" + 
				"        </FaultRule>\r\n" + 
				"        <FaultRule name=\"baderror3\">\r\n" + 
				"          <Condition>true</Condition>\r\n" + 
				"          <Step>\r\n" + 
				"            <Name>RF-Raise403</Name>\r\n" + 
				"          </Step>\r\n" + 
				"        </FaultRule>\r\n" + 
				"    </FaultRules>\r\n" + 
				"</ProxyEndpoint>"
			);
		assertEquals(3, issues.size());
	}
	
}