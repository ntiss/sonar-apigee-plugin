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
import org.sonar.plugins.xml.checks.ExtractVariablesCheck;
import org.sonarsource.analyzer.commons.xml.XmlFile;
import org.sonarsource.analyzer.commons.xml.checks.SonarXmlCheck;

public class ExtractVariablesCheckTest extends AbstractCheckTester {


	private SonarXmlCheck check = new ExtractVariablesCheck();

	
	@Test
	public void test_json_ok1() throws Exception {
		
		// Fake ProxyEndpoint file
		XmlFile proxyEndpointXML = createTempFile("", "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<ProxyEndpoint name=\"default\">\r\n" + 
				"    <Description/>\r\n" + 
				"    <PreFlow name=\"PreFlow\">\r\n" + 
				"        <Request>\r\n" + 
				"            <Step>\r\n" + 
				"                <Name>EV-Extract-Things</Name>\r\n" + 
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
			"<ExtractVariables async=\"false\" continueOnError=\"false\" enabled=\"true\" name=\"EV-Extract-Things\">\r\n" + 
			"    <JSONPayload>\r\n" + 
			"        <Variable name=\"aVariable\" type=\"string\">\r\n" + 
			"            <JSONPath>$.here</JSONPath>\r\n" + 
			"        </Variable>\r\n" + 
			"    </JSONPayload>\r\n" + 
			"</ExtractVariables>"
		);
		
		assertEquals(0, issues.size());		
	}
	
	@Test
	public void test_xml_ok1() throws Exception {
		
		// Fake ProxyEndpoint file
		XmlFile proxyEndpointXML = createTempFile("", "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<ProxyEndpoint name=\"default\">\r\n" + 
				"    <Description/>\r\n" + 
				"    <PreFlow name=\"PreFlow\">\r\n" + 
				"        <Request>\r\n" + 
				"            <Step>\r\n" + 
				"                <Name>EV-Extract-Things</Name>\r\n" + 
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
					"<ExtractVariables async=\"false\" continueOnError=\"false\" enabled=\"true\" name=\"EV-Extract-Things\">\r\n" + 
					"    <XMLPayload>\r\n" + 
					"        <Variable name=\"aVariable\" type=\"string\">\r\n" + 
					"            <XPath>/test/example</XPath>\r\n" + 
					"        </Variable>\r\n" + 
					"    </XMLPayload>\r\n" + 
					"</ExtractVariables>"
				);
	
		assertEquals(0, issues.size());
	}

	@Test
	public void test_form_ok1() throws Exception {
		
		// Fake ProxyEndpoint file
		XmlFile proxyEndpointXML = createTempFile("", "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<ProxyEndpoint name=\"default\">\r\n" + 
				"    <Description/>\r\n" + 
				"    <PreFlow name=\"PreFlow\">\r\n" + 
				"        <Request>\r\n" + 
				"            <Step>\r\n" + 
				"                <Name>EV-Extract-Things</Name>\r\n" + 
				"                <Condition>(request.header.Content-Type Matches \"application/x-www-form-urlencoded\") and (request.header.Content-Length != 0)</Condition>\r\n" + 
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
					"    <FormParam name=\"var\">\r\n" + 
					"        <Pattern>hello {user}</Pattern>\r\n" + 
					"    </FormParam>\r\n" + 
					"</ExtractVariables>"
				);
	
		assertEquals(0, issues.size());
	}
	
	@Test
	public void test_json_ok2() throws Exception {
		
		// Fake ProxyEndpoint file
		XmlFile proxyEndpointXML = createTempFile("", "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
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
				"                    <Name>EV-Extract-Things</Name>\r\n" + 
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
					"<ExtractVariables async=\"false\" continueOnError=\"false\" enabled=\"true\" name=\"EV-Extract-Things\">\r\n" + 
					"    <JSONPayload>\r\n" + 
					"        <Variable name=\"aVariable\" type=\"string\">\r\n" + 
					"            <JSONPath>$.here</JSONPath>\r\n" + 
					"        </Variable>\r\n" + 
					"    </JSONPayload>\r\n" + 
					"</ExtractVariables>"
				);
		assertEquals(0, issues.size());
	}
	
	@Test
	public void test_json_ok3() throws Exception {
		
		// Fake ProxyEndpoint file
		XmlFile proxyEndpointXML = createTempFile("", "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
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
				"                    <Name>EV-Extract-Things</Name>\r\n" + 
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
						"<ExtractVariables async=\"false\" continueOnError=\"false\" enabled=\"true\" name=\"EV-Extract-Things\">\r\n" + 
						"    <JSONPayload>\r\n" + 
						"        <Variable name=\"aVariable\" type=\"string\">\r\n" + 
						"            <JSONPath>$.here</JSONPath>\r\n" + 
						"        </Variable>\r\n" + 
						"    </JSONPayload>\r\n" + 
						"</ExtractVariables>"
					);
		assertEquals(0, issues.size());
	}

	@Test
	public void test_json_ok4() throws Exception {
		
		// Fake ProxyEndpoint file
		XmlFile proxyEndpointXML = createTempFile("", "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<ProxyEndpoint name=\"default\">\r\n" + 
				"    <Description/>\r\n" + 
				"    <PreFlow name=\"PreFlow\">\r\n" + 
				"        <Request/>\r\n" + 
				"        <Response/>\r\n" + 
				"    </PreFlow>\r\n" + 
				"    <Flows>\r\n" + 
				"        <Flow name=\"list resources\">\r\n" + 
				"            <Description>CREATE the resource</Description>\r\n" + 
				"            <Request>\r\n" + 
				"                <Step>\r\n" + 
				"                    <Name>EV-Extract-Things</Name>\r\n" + 
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
					"<ExtractVariables async=\"false\" continueOnError=\"false\" enabled=\"true\" name=\"EV-Extract-Things\">\r\n" + 
					"    <JSONPayload>\r\n" + 
					"    </JSONPayload>\r\n" + 
					"</ExtractVariables>"
					);
		assertEquals(0, issues.size());
	}
	
	@Test
	public void test_queryparam_ok() throws Exception {
		
		// Fake ProxyEndpoint file
		XmlFile proxyEndpointXML = createTempFile("", "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<ProxyEndpoint name=\"default\">\r\n" + 
				"    <Description/>\r\n" + 
				"    <PreFlow name=\"PreFlow\">\r\n" + 
				"        <Request/>\r\n" + 
				"        <Response/>\r\n" + 
				"    </PreFlow>\r\n" + 
				"    <Flows>\r\n" + 
				"        <Flow name=\"list resources\">\r\n" + 
				"            <Description>CREATE the resource</Description>\r\n" + 
				"            <Request>\r\n" + 
				"                <Step>\r\n" + 
				"                    <Name>EV-Extract-Things</Name>\r\n" + 
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
					"<ExtractVariables async=\"false\" continueOnError=\"false\" enabled=\"true\" name=\"EV-Extract-Things\">\r\n" +
					"	<QueryParam name=\"code\">\r\n" + 
					"      <Pattern ignoreCase=\"true\">DBN{dbncode}</Pattern>\r\n" + 
					"   </QueryParam>" + 
					"</ExtractVariables>"
					);
		assertEquals(0, issues.size());
	}
		
	@Test
	public void test_json_ko1() throws Exception {
		
		// Fake ProxyEndpoint file
		String proxyEndpointFilename = "proxyEndpoint.xml";
		XmlFile proxyEndpointXML = createTempFile(proxyEndpointFilename, "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
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
					"</ExtractVariables>"
				);

		
		assertEquals(1, issues.size());

		// assert also the location of the issue
		Issue iss = issues.toArray(new Issue[] {})[0];
		DefaultInputFile dif = (DefaultInputFile)iss.primaryLocation().inputComponent();
		assertEquals(proxyEndpointFilename, dif.filename());		
	}

	@Test
	public void test_xml_ko1() throws Exception {
		
		// Fake ProxyEndpoint file
		String proxyEndpointFilename = "proxyEndpoint.xml";
		XmlFile proxyEndpointXML = createTempFile(proxyEndpointFilename, "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
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
				"    <Flows>\r\n" + 
				"        <Flow name=\"create resources\">\r\n" + 
				"            <Condition>true</Condition>\r\n" + 
				"            <Description>CREATE the resource</Description>\r\n" + 
				"            <Request>\r\n" + 
				"                <Step>\r\n" + 
				"                    <Name>EV-Extract-Things</Name>\r\n" + 
				"                </Step>\r\n" + 
				"            </Request>\r\n" + 
				"            <Response/>\r\n" + 
				"        </Flow>\r\n" + 
				"        <Flow name=\"create resources 2\">\r\n" + 
				"            <Description>CREATE the resource</Description>\r\n" + 
				"            <Request>\r\n" + 
				"                <Step>\r\n" + 
				"                    <Name>EV-Extract-Things</Name>\r\n" + 
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
					"<ExtractVariables async=\"false\" continueOnError=\"false\" enabled=\"true\" name=\"EV-Extract-Things\">\r\n" + 
					"    <XMLPayload>\r\n" + 
					"        <Variable name=\"aVariable\" type=\"string\">\r\n" + 
					"            <XPath>/test/example</XPath>\r\n" + 
					"        </Variable>\r\n" + 
					"    </XMLPayload>\r\n" + 
					"</ExtractVariables>"
				);
		
		assertEquals(3, issues.size());

		// assert also the location of all the issues
		for(Iterator<Issue> it = issues.iterator(); it.hasNext();) {
			Issue iss = it.next();
			DefaultInputFile dif = (DefaultInputFile)iss.primaryLocation().inputComponent();
			assertEquals(proxyEndpointFilename, dif.filename());		
		}
	}
}