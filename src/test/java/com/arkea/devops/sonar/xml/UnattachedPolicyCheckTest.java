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
package com.arkea.devops.sonar.xml;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.List;

import org.junit.Test;
import org.sonar.plugins.xml.checks.BundleRecorder;
import org.sonar.plugins.xml.checks.UnattachedPolicyCheck;
import org.sonar.plugins.xml.checks.XmlIssue;
import org.sonar.plugins.xml.checks.XmlSourceCode;

public class UnattachedPolicyCheckTest extends AbstractCheckTester {

	private List<XmlIssue> getIssues(String content) throws IOException {
		XmlSourceCode sourceCode = parseAndCheck(createTempFile(content), new UnattachedPolicyCheck());
		return sourceCode.getXmlIssues();
	}

	@Test
	public void test_ok() throws Exception {
		
		// Fake ProxyEndpoint file
		XmlSourceCode proxyEndpointXML = parse(createTempFile("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
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
				));
		BundleRecorder.clear();
		BundleRecorder.storeFile(proxyEndpointXML);

		String thePolicy = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<VerifyAPIKey async=\"false\" continueOnError=\"false\" enabled=\"true\" name=\"Verify-API-Key-1\">\r\n" + 
				"    <DisplayName>Verify API Key-1</DisplayName>\r\n" + 
				"    <Properties/>\r\n" + 
				"    <APIKey ref=\"request.header.apikey\"/>\r\n" + 
				"</VerifyAPIKey>";
		BundleRecorder.storeFile(parse(createTempFile(thePolicy)));
		
		List<XmlIssue> issues = getIssues(thePolicy);
		assertEquals(0, issues.size());
	}
	
	@Test
	public void test_ko() throws Exception {
		
		// Fake ProxyEndpoint file
		XmlSourceCode proxyEndpointXML = parse(createTempFile("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
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
				));
		BundleRecorder.clear();
		BundleRecorder.storeFile(proxyEndpointXML);

		String thePolicy = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<VerifyAPIKey async=\"false\" continueOnError=\"false\" enabled=\"true\" name=\"Verify-API-Key-1\">\r\n" + 
				"    <DisplayName>Verify API Key-1</DisplayName>\r\n" + 
				"    <Properties/>\r\n" + 
				"    <APIKey ref=\"request.header.apikey\"/>\r\n" + 
				"</VerifyAPIKey>";
		BundleRecorder.storeFile(parse(createTempFile(thePolicy)));
		
		List<XmlIssue> issues = getIssues(thePolicy);
		assertEquals(1, issues.size());
	}
	
}