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

import com.arkea.satd.sonar.xml.checks.UnknownResourceFlowCheck;

public class UnknownResourceFlowCheckTest extends AbstractCheckTester {

	private SonarXmlCheck check = new UnknownResourceFlowCheck();
	
	@Test
	public void test_ok() throws Exception {
		Collection<Issue> issues = getIssues(check, "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<ProxyEndpoint name=\"default\">\r\n" + 
				"    <PreFlow name=\"PreFlow\">\r\n" + 
				"        <Request/>\r\n" + 
				"        <Response/>\r\n" + 
				"    </PreFlow>\r\n" + 
				"    <Flows>\r\n" + 
				"        <Flow name=\"GET_check\">\r\n" + 
				"            <Condition>(proxy.pathsuffix MatchesPath \"/check\") and (request.verb = \"GET\")</Condition>\r\n" + 
				"            <Description>Check</Description>\r\n" + 
				"            <Request/>\r\n" + 
				"            <Response></Response>\r\n" + 
				"        </Flow>\r\n" + 
				"        <Flow name=\"unknownResource\">\r\n" + 
				"            <Description>Flow used when no other has been used</Description>\r\n" + 
				"            <Request/>\r\n" + 
				"            <Response/>\r\n" + 
				"            <Condition>true</Condition>\r\n" + 
				"        </Flow>\r\n" + 
				"    </Flows>\r\n" + 
				"</ProxyEndpoint>"

		);
		assertEquals(0, issues.size());
	}
	
	@Test
	public void test_ok_other_file() throws Exception {
		Collection<Issue> issues = getIssues(check, "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<SpikeArrest async=\"false\" continueOnError=\"false\" enabled=\"true\" name=\"Spike-Arrest-1\">\r\n" + 
				"    <DisplayName>Spike Arrest</DisplayName>\r\n" + 
				"    <Properties/>\r\n" + 
				"    <Identifier ref=\"request.header.some-header-name\"/>\r\n" + 
				"    <MessageWeight ref=\"request.header.weight\"/>\r\n" + 
				"    <Rate>30pm</Rate>\r\n" + 
				"</SpikeArrest>"

		);
		assertEquals(0, issues.size());
	}	
	
	@Test
	public void test_missing_cond() throws Exception {
		Collection<Issue> issues = getIssues(check, "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<ProxyEndpoint name=\"default\">\r\n" + 
				"    <PreFlow name=\"PreFlow\">\r\n" + 
				"        <Request/>\r\n" + 
				"        <Response/>\r\n" + 
				"    </PreFlow>\r\n" + 
				"    <Flows>\r\n" + 
				"        <Flow name=\"GET_check\">\r\n" + 
				"            <Condition>(proxy.pathsuffix MatchesPath \"/check\") and (request.verb = \"GET\")</Condition>\r\n" + 
				"            <Description>Check</Description>\r\n" + 
				"            <Request/>\r\n" + 
				"            <Response></Response>\r\n" + 
				"        </Flow>\r\n" + 
				"        <Flow name=\"unknownResource\">\r\n" + 
				"            <Description>Flow used when no other has been used</Description>\r\n" + 
				"            <Request/>\r\n" + 
				"            <Response/>\r\n" + 
				"        </Flow>\r\n" + 
				"    </Flows>\r\n" + 
				"</ProxyEndpoint>"
		);
		assertEquals(0, issues.size());
	}	
	
	
	
	@Test
	public void test_bad_cond() throws Exception {
		Collection<Issue> issues = getIssues(check, "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<ProxyEndpoint name=\"default\">\r\n" + 
				"    <PreFlow name=\"PreFlow\">\r\n" + 
				"        <Request/>\r\n" + 
				"        <Response/>\r\n" + 
				"    </PreFlow>\r\n" + 
				"    <Flows>\r\n" + 
				"        <Flow name=\"GET_check\">\r\n" + 
				"            <Condition>(proxy.pathsuffix MatchesPath \"/check\") and (request.verb = \"GET\")</Condition>\r\n" + 
				"            <Description>Check</Description>\r\n" + 
				"            <Request/>\r\n" + 
				"            <Response></Response>\r\n" + 
				"        </Flow>\r\n" + 
				"        <Flow name=\"POST_check\">\r\n" + 
				"            <Description>Flow used when no other has been used</Description>\r\n" + 
				"            <Request/>\r\n" + 
				"            <Response/>\r\n" + 
				"            <Condition>(proxy.pathsuffix MatchesPath \"/check\") and (request.verb = \"POST\")</Condition>\r\n" + 
				"        </Flow>\r\n" + 
				"    </Flows>\r\n" + 
				"</ProxyEndpoint>"
		);
		assertEquals(1, issues.size());
	}		

}