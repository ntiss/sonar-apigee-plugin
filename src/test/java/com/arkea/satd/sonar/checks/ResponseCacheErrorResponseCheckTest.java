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
package com.arkea.satd.sonar.checks;

import static org.junit.Assert.assertEquals;

import java.util.Collection;

import org.junit.Test;
import org.sonar.api.batch.sensor.issue.Issue;
import org.sonarsource.analyzer.commons.xml.XmlFile;
import org.sonarsource.analyzer.commons.xml.checks.SonarXmlCheck;

import com.arkea.satd.sonar.xml.BundleRecorder;
import com.arkea.satd.sonar.xml.checks.ResponseCacheErrorResponseCheck;


public class ResponseCacheErrorResponseCheckTest extends AbstractCheckTester {

	private SonarXmlCheck check = new ResponseCacheErrorResponseCheck();
	
	@Test
	public void test_ok1() throws Exception {
		
		Collection<Issue> issues = getIssues(check, 
				"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<ResponseCache name=\"ResponseCache\">" + 
				"    <CacheKey>" + 
				"        <KeyFragment ref=\"request.queryparam.w\" />" + 
				"    </CacheKey>" + 
				"	 <ExcludeErrorResponse>true</ExcludeErrorResponse>" + 
				"    <ExpirySettings>" + 
				"        <TimeoutInSeconds>600</TimeoutInSeconds>" + 
				"    </ExpirySettings>" + 
				"</ResponseCache>");
		
		assertEquals(0, issues.size());
	}
	
	@Test
	public void test_ok2() throws Exception {
	
		Collection<Issue> issues = getIssues(check, 
				"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<ResponseCache name=\"ResponseCache\">" + 
				"    <CacheKey>" + 
				"        <KeyFragment ref=\"request.queryparam.w\" />" + 
				"    </CacheKey>" + 
				"    <ExpirySettings>" + 
				"        <TimeoutInSeconds>600</TimeoutInSeconds>" + 
				"    </ExpirySettings>" + 
				"</ResponseCache>");

		// No issue : because the policy is not attached. Tricky !
		assertEquals(0, issues.size());
	}

	@Test
	public void test_ok3() throws Exception {
		BundleRecorder.clear();

		// Fake ProxyEndpoint file
		XmlFile proxyEndpointXML = createTempFile("proxyEndpoint.xml", "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<ProxyEndpoint name=\"default\">\r\n" + 
				"    <Description/>\r\n" + 
				"    <PostFlow name=\"PostFlow\">\r\n" + 
				"        <Response>\r\n" + 
				"            <Step>\r\n" + 
				"                <Name>theResponseCache</Name>\r\n" + 
				"                <Condition>response.status == 200</Condition>\r\n" + 
				"            </Step>\r\n" + 
				"        </Response>\r\n" + 
				"    </PostFlow>\r\n" +  
				"</ProxyEndpoint>" 
				);
		BundleRecorder.storeFile(proxyEndpointXML);
		
		// Fake ResponseCache file
		XmlFile policyXml1 = createTempFile("policy1.xml", "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<ResponseCache name=\"theResponseCache\">" + 
				"    <CacheKey>" + 
				"        <KeyFragment ref=\"request.queryparam.w\" />" + 
				"    </CacheKey>" + 
				"	 <ExcludeErrorResponse>false</ExcludeErrorResponse>" + 
				"    <ExpirySettings>" + 
				"        <TimeoutInSeconds>600</TimeoutInSeconds>" + 
				"    </ExpirySettings>" + 
				"</ResponseCache>"
				);
		BundleRecorder.storeFile(policyXml1);

		Collection<Issue> issues = getIssues(check, policyXml1);
	
		assertEquals(0, issues.size());
	}

	@Test
	public void test_ok4() throws Exception {
		BundleRecorder.clear();

		// Fake ProxyEndpoint file
		XmlFile proxyEndpointXML = createTempFile("proxyEndpoint.xml", "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<ProxyEndpoint name=\"default\">\r\n" + 
				"    <Description/>\r\n" + 
				"    <PostFlow name=\"PostFlow\">\r\n" + 
				"        <Response>\r\n" + 
				"            <Step>\r\n" + 
				"                <Name>theResponseCache</Name>\r\n" + 
				"            </Step>\r\n" + 
				"        </Response>\r\n" + 
				"    </PostFlow>\r\n" +  
				"</ProxyEndpoint>" 
				);
		BundleRecorder.storeFile(proxyEndpointXML);
		
		// Fake ResponseCache file
		XmlFile policyXml1 = createTempFile("policy1.xml", "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<ResponseCache name=\"theResponseCache\">" + 
				"    <CacheKey>" + 
				"        <KeyFragment ref=\"request.queryparam.w\" />" + 
				"    </CacheKey>" + 
				"	 <ExcludeErrorResponse>true</ExcludeErrorResponse>" + 
				"    <ExpirySettings>" + 
				"        <TimeoutInSeconds>600</TimeoutInSeconds>" + 
				"    </ExpirySettings>" + 
				"</ResponseCache>"
				);
		BundleRecorder.storeFile(policyXml1);

		Collection<Issue> issues = getIssues(check, policyXml1);
	
		assertEquals(0, issues.size());
	}

	
	@Test
	public void test_ko1() throws Exception {
		BundleRecorder.clear();

		// Fake ProxyEndpoint file
		XmlFile proxyEndpointXML = createTempFile("proxyEndpoint.xml", "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<ProxyEndpoint name=\"default\">\r\n" + 
				"    <Description/>\r\n" + 
				"    <PostFlow name=\"PostFlow\">\r\n" + 
				"        <Response>\r\n" + 
				"            <Step>\r\n" + 
				"                <Name>theResponseCache</Name>\r\n" + 
				"            </Step>\r\n" + 
				"        </Response>\r\n" + 
				"    </PostFlow>\r\n" +  
				"</ProxyEndpoint>" 
				);
		BundleRecorder.storeFile(proxyEndpointXML);
		
		// Fake ResponseCache file
		XmlFile policyXml1 = createTempFile("policy1.xml", "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<ResponseCache name=\"theResponseCache\">" + 
				"    <CacheKey>" + 
				"        <KeyFragment ref=\"request.queryparam.w\" />" + 
				"    </CacheKey>" + 
				"	 <ExcludeErrorResponse>false</ExcludeErrorResponse>" + 
				"    <ExpirySettings>" + 
				"        <TimeoutInSeconds>600</TimeoutInSeconds>" + 
				"    </ExpirySettings>" + 
				"</ResponseCache>"
				);
		BundleRecorder.storeFile(policyXml1);

		Collection<Issue> issues = getIssues(check, policyXml1);
	
		assertEquals(2, issues.size());
	}

}