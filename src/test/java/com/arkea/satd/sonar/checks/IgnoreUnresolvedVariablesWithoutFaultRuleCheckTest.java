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
import com.arkea.satd.sonar.xml.checks.IgnoreUnresolvedVariablesWithoutFaultRuleCheck;

public class IgnoreUnresolvedVariablesWithoutFaultRuleCheckTest extends AbstractCheckTester {

	private SonarXmlCheck check = new IgnoreUnresolvedVariablesWithoutFaultRuleCheck();
		
	@Test
	public void test_ok1() throws Exception {
		Collection<Issue> issues = getIssues(check, 
				"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<ExtractVariables async=\"false\" continueOnError=\"false\" enabled=\"true\" name=\"EV-Extract-Things\">\r\n" + 
				"    <JSONPayload>\r\n" + 
				"        <Variable name=\"aVariable\" type=\"string\">\r\n" + 
				"            <JSONPath>$.here</JSONPath>\r\n" + 
				"        </Variable>\r\n" + 
				"    </JSONPayload>\r\n" + 
				"    <IgnoreUnresolvedVariables>false</IgnoreUnresolvedVariables>\r\n" + 				
				"</ExtractVariables>"
			);
		
		assertEquals(0, issues.size());
	}
	
	@Test
	public void test_ok2() throws Exception {
		// Fake ProxyEndpoint file
		XmlFile proxyEndpointXML = createTempFile("", "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<ProxyEndpoint name=\"default\">\r\n" + 
				"    <Description/>\r\n" + 
				"    <PreFlow name=\"PreFlow\">\r\n" + 
				"        <Request>\r\n" + 
				"            <Step>\r\n" + 
				"                <Name>EV-Extract-Things</Name>\r\n" + 
				"            </Step>\r\n" + 
				"        </Request>\r\n" + 
				"        <Response/>\r\n" + 
				"    </PreFlow>\r\n" +  
				"    <DefaultFaultRule>\r\n" + 
				"       <Step>\r\n" + 
				"          <Name>RF-Raise403</Name>\r\n" + 
				"        </Step>\r\n" + 
				"    </DefaultFaultRule>\r\n" + 
				"</ProxyEndpoint>" 
				);
		BundleRecorder.clear();
		BundleRecorder.storeFile(proxyEndpointXML);

		Collection<Issue> issues = getIssues(check, 
			"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
			"<ExtractVariables async=\"false\" continueOnError=\"false\" enabled=\"true\" name=\"EV-Extract-Things\">\r\n" + 
			"    <JSONPayload>\r\n" + 
			"        <Variable name=\"aVariable\" type=\"string\">\r\n" + 
			"            <JSONPath>$.here</JSONPath>\r\n" + 
			"        </Variable>\r\n" + 
			"    </JSONPayload>\r\n" + 
			"    <IgnoreUnresolvedVariables>true</IgnoreUnresolvedVariables>\r\n" + 				
			"</ExtractVariables>"
		);
		
		assertEquals(0, issues.size());		
	}

	
	@Test
	public void test_ok3() throws Exception {
		// Fake ProxyEndpoint file
		XmlFile proxyEndpointXML = createTempFile("", "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<ProxyEndpoint name=\"default\">\r\n" + 
				"    <Description/>\r\n" + 
				"    <PreFlow name=\"PreFlow\">\r\n" + 
				"        <Request>\r\n" + 
				"            <Step>\r\n" + 
				"                <Name>EV-Extract-Things</Name>\r\n" + 
				"            </Step>\r\n" + 
				"        </Request>\r\n" + 
				"        <Response/>\r\n" + 
				"    </PreFlow>\r\n" +  
				"    <DefaultFaultRule>\r\n" + 
				"       <Step>\r\n" + 
				"          <Name>RF-Raise403</Name>\r\n" + 
				"        </Step>\r\n" + 
				"    </DefaultFaultRule>\r\n" + 
				"</ProxyEndpoint>" 
				);
		BundleRecorder.clear();
		BundleRecorder.storeFile(proxyEndpointXML);

		Collection<Issue> issues = getIssues(check, 
			"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
			"<ExtractVariables async=\"false\" continueOnError=\"false\" enabled=\"true\" name=\"EV-Extract-Things\">\r\n" + 
			"    <JSONPayload>\r\n" + 
			"        <Variable name=\"aVariable\" type=\"string\">\r\n" + 
			"            <JSONPath>$.here</JSONPath>\r\n" + 
			"        </Variable>\r\n" + 
			"    </JSONPayload>\r\n" + 
			"    <IgnoreUnresolvedVariables>false</IgnoreUnresolvedVariables>\r\n" + 				
			"</ExtractVariables>"
		);
		
		assertEquals(0, issues.size());		
	}

	@Test
	public void test_ok4() throws Exception {
		// Fake ProxyEndpoint file
		XmlFile proxyEndpointXML = createTempFile("", "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<ProxyEndpoint name=\"default\">\r\n" + 
				"    <Description/>\r\n" + 
				"    <PreFlow name=\"PreFlow\">\r\n" + 
				"        <Request>\r\n" + 
				"            <Step>\r\n" + 
				"                <Name>EV-Extract-Things</Name>\r\n" + 
				"            </Step>\r\n" + 
				"        </Request>\r\n" + 
				"        <Response/>\r\n" + 
				"    </PreFlow>\r\n" +  
				"    <FaultRules>\r\n" + 
				"        <FaultRule name=\"baderror\">\r\n" + 
				"          <Step>\r\n" + 
				"            <Name>RF-Raise403</Name>\r\n" + 
				"          </Step>\r\n" + 
				"        </FaultRule>\r\n" + 
				"    </FaultRules>\r\n" + 
				"</ProxyEndpoint>" 
				);
		BundleRecorder.clear();
		BundleRecorder.storeFile(proxyEndpointXML);

		Collection<Issue> issues = getIssues(check, 
			"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
			"<ExtractVariables async=\"false\" continueOnError=\"false\" enabled=\"true\" name=\"EV-Extract-Things\">\r\n" + 
			"    <JSONPayload>\r\n" + 
			"        <Variable name=\"aVariable\" type=\"string\">\r\n" + 
			"            <JSONPath>$.here</JSONPath>\r\n" + 
			"        </Variable>\r\n" + 
			"    </JSONPayload>\r\n" + 
			"    <IgnoreUnresolvedVariables>false</IgnoreUnresolvedVariables>\r\n" + 				
			"</ExtractVariables>"
		);
		
		assertEquals(0, issues.size());		
	}
	
	@Test
	public void test_ko1() throws Exception {
		// Fake ProxyEndpoint file
		XmlFile proxyEndpointXML = createTempFile("", "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<ProxyEndpoint name=\"default\">\r\n" + 
				"    <Description/>\r\n" + 
				"    <PreFlow name=\"PreFlow\">\r\n" + 
				"        <Request>\r\n" + 
				"            <Step>\r\n" + 
				"                <Name>EV-Extract-Things</Name>\r\n" + 
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
			"<ExtractVariables async=\"false\" continueOnError=\"false\" enabled=\"true\" name=\"EV-Extract-Things\">\r\n" + 
			"    <JSONPayload>\r\n" + 
			"        <Variable name=\"aVariable\" type=\"string\">\r\n" + 
			"            <JSONPath>$.here</JSONPath>\r\n" + 
			"        </Variable>\r\n" + 
			"    </JSONPayload>\r\n" + 
			"    <IgnoreUnresolvedVariables>true</IgnoreUnresolvedVariables>\r\n" + 				
			"</ExtractVariables>"
		);
		
		assertEquals(1, issues.size());		
	}
	
	@Test
	public void test_ko2() throws Exception {
		// Fake ProxyEndpoint file
		XmlFile proxyEndpointXML = createTempFile("", "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<ProxyEndpoint name=\"default\">\r\n" + 
				"    <Description/>\r\n" + 
				"    <PreFlow name=\"PreFlow\">\r\n" + 
				"        <Request>\r\n" + 
				"            <Step>\r\n" + 
				"                <Name>AM-Assign-Things</Name>\r\n" + 
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
			"<AssignMessage async=\"false\" continueOnError=\"false\" enabled=\"true\" name=\"AM-Assign-Things\">\r\n" + 
			"    <AssignTo createNew=\"false\" transport=\"http\" type=\"request\"/>\r\n" + 
			"</AssignMessage>"
		);
		
		assertEquals(1, issues.size());		
	}

}