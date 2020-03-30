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
import org.sonarsource.analyzer.commons.xml.checks.SonarXmlCheck;

import com.arkea.satd.sonar.xml.checks.EmptyStepCheck;

public class EmptyStepCheckTest extends AbstractCheckTester {

	private SonarXmlCheck check = new EmptyStepCheck();

	@Test
	public void test_for_proxy_ok() throws Exception {
		Collection<Issue> issues = getIssues(check,
				"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<ProxyEndpoint name=\"default\">\r\n" + 
				"    <PreFlow name=\"PreFlow\">\r\n" + 
				"        <Request>\r\n" + 
				"            <Step>\r\n" + 
				"                <Name>Verify-API-Key-1</Name>\r\n" + 
				"                <Condition>request.verb != \"OPTIONS\"</Condition>\r\n" + 
				"            </Step>\r\n" + 
				"        </Request>\r\n" + 
				"        <Response/>\r\n" + 
				"    </PreFlow>\r\n" + 
				"    <Flows>\r\n" + 
				"        <Flow name=\"GET_check\">\r\n" + 
				"            <Condition>(proxy.pathsuffix MatchesPath \"/check\") and (request.verb = \"GET\")</Condition>\r\n" + 
				"            <Description>Check</Description>\r\n" + 
				"   	     <Request>\r\n" + 
				"           	 <Step>\r\n" + 
				"     	           <Name>Verify-API-Key-1</Name>\r\n" + 
				"      	          <Condition>request.verb != \"OPTIONS\"</Condition>\r\n" + 
				"      	      </Step>\r\n" + 
				"       	 </Request>\r\n" + 
				"            <Response></Response>\r\n" + 
				"        </Flow>\r\n" + 
				"    </Flows>\r\n" + 
				"</ProxyEndpoint>"

		);
		assertEquals(0, issues.size());
	}
	
	@Test
	public void test_for_sharedflow_ok() throws Exception {
		Collection<Issue> issues = getIssues(check, 
				"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" + 
				"<SharedFlow name=\"default\">" + 
				"    <Step>" + 
				"        <Name>OA-verifyAccessToken</Name>" + 
				"        <Condition>request.verb != \"OPTIONS\"</Condition>" + 
				"    </Step>" + 
				"    <Step>" + 
				"        <Name>RF-raise401</Name>" + 
				"        <Condition>request.verb != \"OPTIONS\" and oauthV2.OA-verifyAccessToken.failed == \"true\"</Condition>" + 
				"    </Step>" + 
				"</SharedFlow>"
		);
		assertEquals(0, issues.size());
	}

	@Test
	public void test_for_proxy_ko1() throws Exception {
		Collection<Issue> issues = getIssues(check,
				"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<ProxyEndpoint name=\"default\">\r\n" + 
				"    <PreFlow name=\"PreFlow\">\r\n" + 
				"        <Request>\r\n" + 
				"            <Step>\r\n" + 
				"                <Name>Verify-API-Key-1</Name>\r\n" + 
				"                <Condition>request.verb != \"OPTIONS\"</Condition>\r\n" + 
				"            </Step>\r\n" + 
				"            <Step/>\r\n" + 
				"        </Request>\r\n" + 
				"        <Response/>\r\n" + 
				"    </PreFlow>\r\n" + 
				"    <Flows>\r\n" + 
				"        <Flow name=\"GET_check\">\r\n" + 
				"            <Condition>(proxy.pathsuffix MatchesPath \"/check\") and (request.verb = \"GET\")</Condition>\r\n" + 
				"            <Description>Check</Description>\r\n" + 
				"   	     <Request>\r\n" + 
				"           	 <Step>\r\n" + 
				"     	           <Name>Verify-API-Key-1</Name>\r\n" + 
				"      	          <Condition>request.verb != \"OPTIONS\"</Condition>\r\n" + 
				"      	      </Step>\r\n" + 
				"       	 </Request>\r\n" + 
				"            <Response></Response>\r\n" + 
				"        </Flow>\r\n" + 
				"    </Flows>\r\n" + 
				"</ProxyEndpoint>"

		);
		assertEquals(1, issues.size());
	}	
	
	
	
	@Test
	public void test_for_proxy_ko3() throws Exception {
		Collection<Issue> issues = getIssues(check,
				"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<ProxyEndpoint name=\"default\">\r\n" + 
				"    <PreFlow name=\"PreFlow\">\r\n" + 
				"        <Request>\r\n" + 
				"            <Step attr=\"1\">\r\n" + 
				"                <Name>Verify-API-Key-1</Name>\r\n" + 
				"                <Condition>request.verb != \"OPTIONS\"</Condition>\r\n" + 
				"            </Step>\r\n" + 
				"            <Step/>\r\n" + 
				"        </Request>\r\n" + 
				"        <Response/>\r\n" + 
				"    </PreFlow>\r\n" + 
				"    <Flows>\r\n" + 
				"        <Flow name=\"GET_check\">\r\n" + 
				"            <Condition>(proxy.pathsuffix MatchesPath \"/check\") and (request.verb = \"GET\")</Condition>\r\n" + 
				"            <Description>Check</Description>\r\n" + 
				"   	     <Request>\r\n" + 
				"           	 <Step>\r\n" + 
				"     	           <Name>Verify-API-Key-1</Name>\r\n" + 
				"      	          <Condition>request.verb != \"OPTIONS\"</Condition>\r\n" + 
				"      	      	</Step>\r\n" + 
				"            	<Step attr=\"2\">\r\n" + 
				"            	</Step>\r\n" + 
				"            	<Step attr=\"3\">\r\n" + 
				"            	<!-- comment -->\r\n" + 
				"            	</Step>\r\n" + 
				"       	 </Request>\r\n" + 
				"            <Response></Response>\r\n" + 
				"        </Flow>\r\n" + 
				"    </Flows>\r\n" + 
				"</ProxyEndpoint>"

		);
		assertEquals(3, issues.size());
	}		
	
	@Test
	public void test_for_sharedflow_ko1() throws Exception {
		Collection<Issue> issues = getIssues(check, 
				"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" + 
				"<SharedFlow name=\"default\">" + 
				"    <Step>" + 
				"        <Name>OA-verifyAccessToken</Name>" + 
				"        <Condition>request.verb != \"OPTIONS\"</Condition>" + 
				"    </Step>" + 
				"    <Step>" + 
				"    </Step>" + 
				"</SharedFlow>"
		);
		assertEquals(1, issues.size());
	}
	
	@Test
	public void test_for_sharedflow_ko3() throws Exception {
		Collection<Issue> issues = getIssues(check, 
				"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" + 
				"<SharedFlow name=\"default\">" + 
				"    <Step>" + 
				"        <Name>OA-verifyAccessToken</Name>" + 
				"        <Condition>request.verb != \"OPTIONS\"</Condition>" + 
				"    </Step>" + 
				"    <Step/>" + 
				"    <Step attr=\"2\">\r\n" + 
				"    </Step>\r\n" + 
				"    <Step attr=\"3\">\r\n" + 
				"    <!-- comment -->\r\n" + 
				"    </Step>\r\n" + 
				"</SharedFlow>"
		);
		assertEquals(3, issues.size());
	}
	
	

}