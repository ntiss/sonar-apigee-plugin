package com.arkea.devops.sonar.xml;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.List;

import org.junit.Test;
import org.sonar.plugins.xml.checks.BundleRecorder;
import org.sonar.plugins.xml.checks.ThreatProtectionCheck;
import org.sonar.plugins.xml.checks.XmlIssue;
import org.sonar.plugins.xml.checks.XmlSourceCode;

public class ThreatProtectionCheckTest extends AbstractCheckTester {

	private List<XmlIssue> getIssues(String content) throws IOException {
		XmlSourceCode sourceCode = parseAndCheck(createTempFile(content), new ThreatProtectionCheck());
		return sourceCode.getXmlIssues();
	}
	
	@Test
	public void test_json_ok1_condition_in_step() throws Exception {
		
		// Fake ProxyEndpoint file
		XmlSourceCode proxyEndpointXML = parse(createTempFile("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<ProxyEndpoint name=\"default\">\r\n" + 
				"    <Description/>\r\n" + 
				"    <PreFlow name=\"PreFlow\">\r\n" + 
				"        <Request>\r\n" + 
				"            <Step>\r\n" + 
				"                <Name>JSON-Threat-Protection-1</Name>\r\n" + 
				"                <Condition>(request.header.Content-Type Matches \"application/json*\") and (request.header.Content-Length != 0)</Condition>\r\n" + 
				"            </Step>\r\n" + 
				"        </Request>\r\n" + 
				"        <Response/>\r\n" + 
				"    </PreFlow>\r\n" +  
				"</ProxyEndpoint>" 
				));
		BundleRecorder.clear();
		BundleRecorder.storeFile(proxyEndpointXML);

		List<XmlIssue> issues = getIssues(
			"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
			"<JSONThreatProtection async=\"false\" continueOnError=\"false\" enabled=\"true\" name=\"JSON-Threat-Protection-1\">\r\n" + 
			"    <DisplayName>JSON-Threat-Protection-1</DisplayName>\r\n" + 
			"</JSONThreatProtection>"
		);
		
		assertEquals(0, issues.size());
		assertEquals(0, proxyEndpointXML.getXmlIssues().size());
	}
	
	@Test
	public void test_xml_ok1_condition_in_step() throws Exception {
		
		// Fake ProxyEndpoint file
		XmlSourceCode proxyEndpointXML = parse(createTempFile("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<ProxyEndpoint name=\"default\">\r\n" + 
				"    <Description/>\r\n" + 
				"    <PreFlow name=\"PreFlow\">\r\n" + 
				"        <Request>\r\n" + 
				"            <Step>\r\n" + 
				"                <Name>XML-Threat-Protection-1</Name>\r\n" + 
				"                <Condition>(request.header.Content-Type Matches \"application/xml*\") and (request.header.Content-Length != 0)</Condition>\r\n" + 
				"            </Step>\r\n" + 
				"        </Request>\r\n" + 
				"        <Response/>\r\n" + 
				"    </PreFlow>\r\n" +  
				"</ProxyEndpoint>" 
				));
		BundleRecorder.clear();
		BundleRecorder.storeFile(proxyEndpointXML);

		List<XmlIssue> issues = getIssues(
			"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
			"<XMLThreatProtection async=\"false\" continueOnError=\"false\" enabled=\"true\" name=\"XML-Threat-Protection-1\">\r\n" + 
			"    <DisplayName>XML-Threat-Protection-1</DisplayName>\r\n" + 
			"</XMLThreatProtection>"
		);
		
		assertEquals(0, issues.size());
		assertEquals(0, proxyEndpointXML.getXmlIssues().size());
	}

	@Test
	public void test_json_ok2_condition_in_step() throws Exception {
		
		// Fake ProxyEndpoint file
		XmlSourceCode proxyEndpointXML = parse(createTempFile("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<ProxyEndpoint name=\"default\">\r\n" + 
				"    <Description/>\r\n" + 
				"    <PreFlow name=\"PreFlow\">\r\n" + 
				"        <Request/>\r\n" + 
				"        <Response/>\r\n" + 
				"    </PreFlow>\r\n" + 
				"    <Flows>\r\n" + 
				"        <Flow name=\"create resources\">\r\n" + 
				"            <Condition>(proxy.pathsuffix MatchesPath \"/resources\") and (request.verb = \"POST\")</Condition>\r\n" + 
				"            <Description>CREATE the resource</Description>\r\n" + 
				"            <Request>\r\n" + 
				"                <Step>\r\n" + 
				"                    <Name>JSON-Threat-Protection-1</Name>\r\n" + 
				"                    <Condition>(request.header.Content-Type Matches \"application/json*\") and (request.header.Content-Length != 0)</Condition>\r\n" + 
				"                </Step>\r\n" + 
				"            </Request>\r\n" + 
				"            <Response/>\r\n" + 
				"        </Flow>\r\n" + 
				"    </Flows>\r\n" + 
				"</ProxyEndpoint>" 
				));
		BundleRecorder.clear();
		BundleRecorder.storeFile(proxyEndpointXML);
		
		List<XmlIssue> issues = getIssues(
			"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
			"<JSONThreatProtection async=\"false\" continueOnError=\"false\" enabled=\"true\" name=\"JSON-Threat-Protection-1\">\r\n" + 
			"    <DisplayName>JSON-Threat-Protection-1</DisplayName>\r\n" + 
			"</JSONThreatProtection>"
		);
		assertEquals(0, issues.size());
		assertEquals(0, proxyEndpointXML.getXmlIssues().size());
	}
	
	@Test
	public void test_json_ok3_condition_in_proxyEndpoint() throws Exception {
		
		// Fake ProxyEndpoint file
		XmlSourceCode proxyEndpointXML = parse(createTempFile("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<ProxyEndpoint name=\"default\">\r\n" + 
				"    <Description/>\r\n" + 
				"    <PreFlow name=\"PreFlow\">\r\n" + 
				"        <Request/>\r\n" + 
				"        <Response/>\r\n" + 
				"    </PreFlow>\r\n" + 
				"    <Flows>\r\n" + 
				"        <Flow name=\"list resources\">\r\n" + 
				"            <Condition>(proxy.pathsuffix MatchesPath \"/resources\") and (request.verb = \"POST\") and (request.header.Content-Length != 0)</Condition>\r\n" + 
				"            <Description>CREATE the resource</Description>\r\n" + 
				"            <Request>\r\n" + 
				"                <Step>\r\n" + 
				"                    <Name>JSON-Threat-Protection-1</Name>\r\n" + 
				"                </Step>\r\n" + 
				"            </Request>\r\n" + 
				"            <Response/>\r\n" + 
				"        </Flow>\r\n" + 
				"    </Flows>\r\n" + 
				"</ProxyEndpoint>" 
				));
		BundleRecorder.clear();
		BundleRecorder.storeFile(proxyEndpointXML);

		List<XmlIssue> issues = getIssues(
			"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
			"<JSONThreatProtection async=\"false\" continueOnError=\"false\" enabled=\"true\" name=\"JSON-Threat-Protection-1\">\r\n" + 
			"    <DisplayName>JSON-Threat-Protection-1</DisplayName>\r\n" + 
			"</JSONThreatProtection>"
		);
		assertEquals(0, issues.size());
		assertEquals(0, proxyEndpointXML.getXmlIssues().size());
	}

	@Test
	public void test_json_ok4_condition_in_targetEnpoint() throws Exception {
		
		// Fake ProxyEndpoint file
		XmlSourceCode proxyEndpointXML = parse(createTempFile("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<TargetEndpoint name=\"default\">\r\n" + 
				"    <Description/>\r\n" + 
				"    <PreFlow/>\r\n" + 
				"    <Flows>\r\n" + 
				"        <Flow name=\"list resources\">\r\n" + 
				"            <Condition>(proxy.pathsuffix MatchesPath \"/resources\") and (request.verb = \"POST\") and (request.header.Content-Length != 0)</Condition>\r\n" + 
				"            <Description>CREATE the resource</Description>\r\n" + 
				"            <Request>\r\n" + 
				"                <Step>\r\n" + 
				"                    <Name>JSON-Threat-Protection-1</Name>\r\n" + 
				"                </Step>\r\n" + 
				"            </Request>\r\n" + 
				"            <Response/>\r\n" + 
				"        </Flow>\r\n" + 
				"    </Flows>\r\n" + 
				"</TargetEndpoint>" 
				));
		BundleRecorder.clear();
		BundleRecorder.storeFile(proxyEndpointXML);

		List<XmlIssue> issues = getIssues(
			"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
			"<JSONThreatProtection async=\"false\" continueOnError=\"false\" enabled=\"true\" name=\"JSON-Threat-Protection-1\">\r\n" + 
			"    <DisplayName>JSON-Threat-Protection-1</DisplayName>\r\n" + 
			"</JSONThreatProtection>"
		);
		assertEquals(0, issues.size());
		assertEquals(0, proxyEndpointXML.getXmlIssues().size());
	}
	
	
	
	@Test
	public void test_json_ko1() throws Exception {
		
		// Fake ProxyEndpoint file
		XmlSourceCode proxyEndpointXML = parse(createTempFile("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<ProxyEndpoint name=\"default\">\r\n" + 
				"    <Description/>\r\n" + 
				"    <PreFlow name=\"PreFlow\">\r\n" + 
				"        <Request>\r\n" + 
				"            <Step>\r\n" + 
				"                <Name>JSON-Threat-Protection-1</Name>\r\n" + 
				"            </Step>\r\n" + 
				"        </Request>\r\n" + 
				"        <Response/>\r\n" + 
				"    </PreFlow>\r\n" +  
				"</ProxyEndpoint>" 
				));
		BundleRecorder.clear();
		BundleRecorder.storeFile(proxyEndpointXML);

		List<XmlIssue> issues = getIssues(
			"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
			"<JSONThreatProtection async=\"false\" continueOnError=\"false\" enabled=\"true\" name=\"JSON-Threat-Protection-1\">\r\n" + 
			"    <DisplayName>JSON-Threat-Protection-1</DisplayName>\r\n" + 
			"</JSONThreatProtection>"
		);
		
		assertEquals(0, issues.size());
		assertEquals(1, proxyEndpointXML.getXmlIssues().size());
	}

	

	@Test
	public void test_xml_ko1() throws Exception {
		
		// Fake ProxyEndpoint file
		XmlSourceCode proxyEndpointXML = parse(createTempFile("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<ProxyEndpoint name=\"default\">\r\n" + 
				"    <Description/>\r\n" + 
				"    <PreFlow name=\"PreFlow\">\r\n" + 
				"        <Request>\r\n" + 
				"            <Step>\r\n" + 
				"                <Name>XML-Threat-Protection-1</Name>\r\n" + 
				"            </Step>\r\n" + 
				"        </Request>\r\n" + 
				"        <Response/>\r\n" + 
				"    </PreFlow>\r\n" + 
				"    <Flows>\r\n" + 
				"        <Flow name=\"create resources\">\r\n" + 
				"            <Condition>true</Condition>\r\n" + 
				"            <Description>CREATE the resource</Description>\r\n" + 
				"            <Request>\r\n" + 
				"                <Step>\r\n" + 
				"                    <Name>XML-Threat-Protection-1</Name>\r\n" + 
				"                </Step>\r\n" + 
				"            </Request>\r\n" + 
				"            <Response/>\r\n" + 
				"        </Flow>\r\n" + 
				"        <Flow name=\"create resources 2\">\r\n" + 
				"            <Description>CREATE the resource</Description>\r\n" + 
				"            <Request>\r\n" + 
				"                <Step>\r\n" + 
				"                    <Name>XML-Threat-Protection-1</Name>\r\n" + 
				"                    <Condition>something and somethingelse</Condition>\r\n" + 
				"                </Step>\r\n" + 
				"            </Request>\r\n" + 
				"            <Response/>\r\n" + 
				"        </Flow>\r\n" + 
				"    </Flows>\r\n" + 
				"</ProxyEndpoint>" 
				));
		BundleRecorder.clear();
		BundleRecorder.storeFile(proxyEndpointXML);

		List<XmlIssue> issues = getIssues(
			"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
			"<XMLThreatProtection async=\"false\" continueOnError=\"false\" enabled=\"true\" name=\"XML-Threat-Protection-1\">\r\n" + 
			"    <DisplayName>JSON-Threat-Protection-1</DisplayName>\r\n" + 
			"</XMLThreatProtection>"
		);
		
		assertEquals(0, issues.size());
		assertEquals(3, proxyEndpointXML.getXmlIssues().size());
	}		
}