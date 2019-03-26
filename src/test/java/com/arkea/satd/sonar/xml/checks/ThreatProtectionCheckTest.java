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
import java.util.Iterator;

import org.junit.Test;
import org.sonar.api.batch.fs.internal.DefaultInputFile;
import org.sonar.api.batch.sensor.issue.Issue;
import org.sonar.plugins.xml.checks.BundleRecorder;
import org.sonar.plugins.xml.checks.ThreatProtectionCheck;
import org.sonarsource.analyzer.commons.xml.XmlFile;
import org.sonarsource.analyzer.commons.xml.checks.SonarXmlCheck;

public class ThreatProtectionCheckTest extends AbstractCheckTester {

	private SonarXmlCheck check = new ThreatProtectionCheck();
	
	
	@Test
	public void test_json_ok1_condition_in_step() throws Exception {
		
		// Fake ProxyEndpoint file
		XmlFile proxyEndpointXML = createTempFile("proxyEndpoint.xml", "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
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
				);
		BundleRecorder.clear();
		BundleRecorder.storeFile(proxyEndpointXML);

		Collection<Issue> issues = getIssues(check, 
			"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
			"<JSONThreatProtection async=\"false\" continueOnError=\"false\" enabled=\"true\" name=\"JSON-Threat-Protection-1\">\r\n" + 
			"    <DisplayName>JSON-Threat-Protection-1</DisplayName>\r\n" + 
			"</JSONThreatProtection>"
		);
		
		assertEquals(0, issues.size());
	}
	
	@Test
	public void test_xml_ok1_condition_in_step() throws Exception {
		
		// Fake ProxyEndpoint file
		XmlFile proxyEndpointXML = createTempFile("proxyEndpoint.xml", "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
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
				);
		BundleRecorder.clear();
		BundleRecorder.storeFile(proxyEndpointXML);

		Collection<Issue> issues = getIssues(check, 
			"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
			"<XMLThreatProtection async=\"false\" continueOnError=\"false\" enabled=\"true\" name=\"XML-Threat-Protection-1\">\r\n" + 
			"    <DisplayName>XML-Threat-Protection-1</DisplayName>\r\n" + 
			"</XMLThreatProtection>"
		);
		
		assertEquals(0, issues.size());
	}

	@Test
	public void test_json_ok2_condition_in_step() throws Exception {
		
		// Fake ProxyEndpoint file
		XmlFile proxyEndpointXML = createTempFile("proxyEndpoint.xml", "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
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
				);
		BundleRecorder.clear();
		BundleRecorder.storeFile(proxyEndpointXML);
		
		Collection<Issue> issues = getIssues(check, 
			"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
			"<JSONThreatProtection async=\"false\" continueOnError=\"false\" enabled=\"true\" name=\"JSON-Threat-Protection-1\">\r\n" + 
			"    <DisplayName>JSON-Threat-Protection-1</DisplayName>\r\n" + 
			"</JSONThreatProtection>"
		);
		assertEquals(0, issues.size());
	}
	
	@Test
	public void test_json_ok3_condition_in_proxyEndpoint() throws Exception {
		
		// Fake ProxyEndpoint file
		XmlFile proxyEndpointXML = createTempFile("proxyEndpoint.xml", "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
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
				);
		BundleRecorder.clear();
		BundleRecorder.storeFile(proxyEndpointXML);

		Collection<Issue> issues = getIssues(check, 
			"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
			"<JSONThreatProtection async=\"false\" continueOnError=\"false\" enabled=\"true\" name=\"JSON-Threat-Protection-1\">\r\n" + 
			"    <DisplayName>JSON-Threat-Protection-1</DisplayName>\r\n" + 
			"</JSONThreatProtection>"
		);
		assertEquals(0, issues.size());
	}

	@Test
	public void test_json_ok4_condition_in_targetEnpoint() throws Exception {
		
		// Fake ProxyEndpoint file
		XmlFile proxyEndpointXML = createTempFile("proxyEndpoint.xml", "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
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
				);
		BundleRecorder.clear();
		BundleRecorder.storeFile(proxyEndpointXML);

		Collection<Issue> issues = getIssues(check, 
			"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
			"<JSONThreatProtection async=\"false\" continueOnError=\"false\" enabled=\"true\" name=\"JSON-Threat-Protection-1\">\r\n" + 
			"    <DisplayName>JSON-Threat-Protection-1</DisplayName>\r\n" + 
			"</JSONThreatProtection>"
		);
		assertEquals(0, issues.size());
	}
	
	@Test
	public void test_json_ko1() throws Exception {
		
		// Fake ProxyEndpoint file
		XmlFile proxyEndpointXML = createTempFile("proxyEndpoint.xml", "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
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
				);
		BundleRecorder.clear();
		BundleRecorder.storeFile(proxyEndpointXML);

		Collection<Issue> issues = getIssues(check, 
			"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
			"<JSONThreatProtection async=\"false\" continueOnError=\"false\" enabled=\"true\" name=\"JSON-Threat-Protection-1\">\r\n" + 
			"    <DisplayName>JSON-Threat-Protection-1</DisplayName>\r\n" + 
			"</JSONThreatProtection>"
		);
		
		assertEquals(1, issues.size());
		
		// assert also the location of the issue
		Issue iss = issues.toArray(new Issue[] {})[0];
		DefaultInputFile dif = (DefaultInputFile)iss.primaryLocation().inputComponent();
		assertEquals("proxyEndpoint.xml", dif.filename());
	}

	@Test
	public void test_xml_ko1() throws Exception {
		
		// Fake ProxyEndpoint file
		XmlFile proxyEndpointXML = createTempFile("proxyEndpoint.xml", "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
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
				);
		BundleRecorder.clear();
		BundleRecorder.storeFile(proxyEndpointXML);

		Collection<Issue> issues = getIssues(check, 
			"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
			"<XMLThreatProtection async=\"false\" continueOnError=\"false\" enabled=\"true\" name=\"XML-Threat-Protection-1\">\r\n" + 
			"    <DisplayName>JSON-Threat-Protection-1</DisplayName>\r\n" + 
			"</XMLThreatProtection>"
		);

		assertEquals(3, issues.size());
		
		// assert also the location of all the issues
		for(Iterator<Issue> it = issues.iterator(); it.hasNext();) {
			Issue iss = it.next();
			DefaultInputFile dif = (DefaultInputFile)iss.primaryLocation().inputComponent();
			assertEquals("proxyEndpoint.xml", dif.filename());		
		}		
	
	}		
}