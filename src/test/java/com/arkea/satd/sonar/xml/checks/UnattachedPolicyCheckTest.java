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
package com.arkea.satd.sonar.xml.checks;

import static org.junit.Assert.assertEquals;

import java.util.Collection;

import org.junit.Test;
import org.sonar.api.batch.fs.internal.DefaultInputFile;
import org.sonar.api.batch.sensor.issue.Issue;
import org.sonar.plugins.xml.checks.BundleRecorder;
import org.sonar.plugins.xml.checks.UnattachedPolicyCheck;
import org.sonarsource.analyzer.commons.xml.XmlFile;
import org.sonarsource.analyzer.commons.xml.checks.SonarXmlCheck;

public class UnattachedPolicyCheckTest extends AbstractCheckTester {

	private SonarXmlCheck check = new UnattachedPolicyCheck();
	
	@Test
	public void test_ok() throws Exception {
		
		// Fake ProxyEndpoint file
		XmlFile proxyEndpointXML = createTempFile("proxyEndpoint.xml", "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<ProxyEndpoint name=\"default\">\r\n" + 
				"    <Description/>\r\n" + 
				"    <PreFlow name=\"PreFlow\">\r\n" + 
				"        <Request>\r\n" + 
				"            <Step>\r\n" + 
				"                <Name>Verify-API-Key-1</Name>\r\n" + 
				"            </Step>\r\n" + 
				"        </Request>\r\n" + 
				"        <Response/>\r\n" + 
				"    </PreFlow>\r\n" +  
				"</ProxyEndpoint>" 
				);
		BundleRecorder.clear();
		BundleRecorder.storeFile(proxyEndpointXML);

		String thePolicy = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<VerifyAPIKey async=\"false\" continueOnError=\"false\" enabled=\"true\" name=\"Verify-API-Key-1\">\r\n" + 
				"    <DisplayName>Verify API Key-1</DisplayName>\r\n" + 
				"    <Properties/>\r\n" + 
				"    <APIKey ref=\"request.header.apikey\"/>\r\n" + 
				"</VerifyAPIKey>";
//		BundleRecorder.storeFile(createTempFile("proxyEndpoint.xml", thePolicy));
		
		Collection<Issue> issues = getIssues(check, thePolicy);
		assertEquals(0, issues.size());
	}

	@Test
	public void test_ok_inFaultRule() throws Exception {
		
		// Fake ProxyEndpoint file
		XmlFile proxyEndpointXML = createTempFile("proxyEndpoint.xml", "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<ProxyEndpoint name=\"default\">\r\n" + 
				"    <Description/>\r\n" + 
				"    <FaultRules>\r\n" + 
				"        <FaultRule name=\"error403\">\r\n" + 
				"            <Condition>message.status.code=403</Condition>\r\n" + 
				"            <Step>\r\n" + 
				"                <Name>Assign-Message-UsedInFaultRule</Name>\r\n" + 
				"            </Step>\r\n" + 
				"            <Step>\r\n" + 
				"                <Name>Raise-Fault-1</Name>\r\n" + 
				"            </Step>\r\n" + 
				"        </FaultRule>\r\n" + 
				"    </FaultRules>" +
				"</ProxyEndpoint>" 
				);
		BundleRecorder.clear();
		BundleRecorder.storeFile(proxyEndpointXML);

		String thePolicy = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<AssignMessage async=\"false\" continueOnError=\"false\" enabled=\"true\" name=\"Assign-Message-UsedInFaultRule\">\r\n" + 
				"    <DisplayName>Assign Message-UsedInFaultRule</DisplayName>\r\n" + 
				"    <Properties/>\r\n" + 
				"</AssignMessage>";
//		BundleRecorder.storeFile(createTempFile("proxyEndpoint.xml", thePolicy));
		
		Collection<Issue> issues = getIssues(check, thePolicy);
		assertEquals(0, issues.size());
	}
		
	
	@Test
	public void test_ko() throws Exception {
		
		// Fake ProxyEndpoint file
		XmlFile proxyEndpointXML = createTempFile("proxyEndpoint.xml", "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<ProxyEndpoint name=\"default\">\r\n" + 
				"    <Description/>\r\n" + 
				"    <PreFlow name=\"PreFlow\">\r\n" + 
				"        <Request>\r\n" + 
				"            <Step>\r\n" + 
				"                <Name>Verify-API-Key-2</Name>\r\n" + 
				"            </Step>\r\n" + 
				"        </Request>\r\n" + 
				"        <Response/>\r\n" + 
				"    </PreFlow>\r\n" +  
				"</ProxyEndpoint>" 
				);
		BundleRecorder.clear();
		BundleRecorder.storeFile(proxyEndpointXML);

		String thePolicyFilename = "thePolicy.xml";
		XmlFile thePolicy = createTempFile(thePolicyFilename, "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<VerifyAPIKey async=\"false\" continueOnError=\"false\" enabled=\"true\" name=\"Verify-API-Key-1\">\r\n" + 
				"    <DisplayName>Verify API Key-1</DisplayName>\r\n" + 
				"    <Properties/>\r\n" + 
				"    <APIKey ref=\"request.header.apikey\"/>\r\n" + 
				"</VerifyAPIKey>");
		BundleRecorder.storeFile(thePolicy);
		
		Collection<Issue> issues = getIssues(check, thePolicy);
		assertEquals(1, issues.size());
		
		// assert also the location of the issue
		Issue iss = issues.toArray(new Issue[] {})[0];
		DefaultInputFile dif = (DefaultInputFile)iss.primaryLocation().inputComponent();
		assertEquals(thePolicyFilename, dif.filename());
		
	}
	
	@Test
	public void test_ok_exclude_manifest() throws Exception {

		// Fake Manifest file
		String manifestXML = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<Manifest name=\"manifest\">\r\n" + 
				"    <Policies/>\r\n" + 
				"    <ProxyEndpoints/>\r\n" + 
				"    <Resources/>\r\n" + 
				"    <SharedFlows/>\r\n" + 
				"    <TargetEndpoints/>\r\n" + 
				"</Manifest>";

		BundleRecorder.clear();
		BundleRecorder.storeFile(createTempFile("proxyEndpoint.xml", manifestXML));
		
		Collection<Issue> issues = getIssues(check, manifestXML);
		assertEquals(0, issues.size());
	}	
	
	@Test
	public void test_ok_deal_with_xslt() throws Exception {

		// Fake XSL Script
		//File tempScript = createTempFile("XSL-Transform-1.xsl", theScript, "XSL-Transform-1.xsl", "xsl");
		
		String tempFilename = "XSL-Transform-1.xsl";
		XmlFile tempScript = createTempFile(tempFilename, "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\r\n" + 
				"<xsl:stylesheet version=\"1.0\" xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\">\r\n" + 
				"</xsl:stylesheet>");

		BundleRecorder.clear();
		BundleRecorder.storeFile(tempScript);

		Collection<Issue> issues = getIssues(check, "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<XSL async=\"false\" continueOnError=\"false\" enabled=\"true\" name=\"XSL-Transform-1\">\r\n" + 
				"    <DisplayName>XSL Transform-1</DisplayName>\r\n" + 
				"    <Properties/>\r\n" + 
				"    <Source>request</Source>\r\n" + 
				"    <ResourceURL>xsl://"+ tempFilename +"</ResourceURL>\r\n" + 
				"    <Parameters ignoreUnresolvedVariables=\"true\" />\r\n" + 
				"    <OutputVariable></OutputVariable>\r\n" + 
				"</XSL>");
		assertEquals(0, issues.size());
	}	
	
	@Test
	public void test_ok2_deal_with_xslt() throws Exception {

		Collection<Issue> issues = getIssues(check, "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\r\n" + 
				"<xsl:stylesheet version=\"1.0\" xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\">\r\n" + 
				"</xsl:stylesheet>");
		
		// No issue : because it's not ckecked in UnattachedPolicyCheck, but in UnattachedResourceCheck
		assertEquals(0, issues.size());
	}	
}