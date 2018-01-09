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

import java.io.IOException;
import java.util.List;

import org.junit.Test;
import org.sonar.plugins.xml.checks.BundleRecorder;
import org.sonar.plugins.xml.checks.ExtractVariablesCheck;
import org.sonar.plugins.xml.checks.XmlIssue;
import org.sonar.plugins.xml.checks.XmlSourceCode;

public class ExtractVariablesCheckTest extends AbstractCheckTester {

	private List<XmlIssue> getIssues(String content) throws IOException {
		XmlSourceCode sourceCode = parseAndCheck(createTempFile(content), new ExtractVariablesCheck());
		return sourceCode.getXmlIssues();
	}
	
	@Test
	public void test_json_ok1() throws Exception {
		
		// Fake ProxyEndpoint file
		XmlSourceCode proxyEndpointXML = parse(createTempFile("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
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
				));
		BundleRecorder.clear();
		BundleRecorder.storeFile(proxyEndpointXML);

		List<XmlIssue> issues = getIssues(
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
		assertEquals(0, proxyEndpointXML.getXmlIssues().size());
	}
	
	@Test
	public void test_xml_ok1() throws Exception {
		
		// Fake ProxyEndpoint file
		XmlSourceCode proxyEndpointXML = parse(createTempFile("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
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
				));
		BundleRecorder.clear();
		BundleRecorder.storeFile(proxyEndpointXML);

		List<XmlIssue> issues = getIssues(
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
		assertEquals(0, proxyEndpointXML.getXmlIssues().size());
	}

	@Test
	public void test_form_ok1() throws Exception {
		
		// Fake ProxyEndpoint file
		XmlSourceCode proxyEndpointXML = parse(createTempFile("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
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
				));
		BundleRecorder.clear();
		BundleRecorder.storeFile(proxyEndpointXML);

		List<XmlIssue> issues = getIssues(
			"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
					"<ExtractVariables async=\"false\" continueOnError=\"false\" enabled=\"true\" name=\"EV-Extract-Things\">\r\n" + 
					"    <FormParam name=\"var\">\r\n" + 
					"        <Pattern>hello {user}</Pattern>\r\n" + 
					"    </FormParam>\r\n" + 
					"</ExtractVariables>"
				);
	
		assertEquals(0, issues.size());
		assertEquals(0, proxyEndpointXML.getXmlIssues().size());
	}
	
	@Test
	public void test_json_ok2() throws Exception {
		
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
				"                    <Name>EV-Extract-Things</Name>\r\n" + 
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
					"<ExtractVariables async=\"false\" continueOnError=\"false\" enabled=\"true\" name=\"EV-Extract-Things\">\r\n" + 
					"    <JSONPayload>\r\n" + 
					"        <Variable name=\"aVariable\" type=\"string\">\r\n" + 
					"            <JSONPath>$.here</JSONPath>\r\n" + 
					"        </Variable>\r\n" + 
					"    </JSONPayload>\r\n" + 
					"</ExtractVariables>"
				);
		assertEquals(0, issues.size());
		assertEquals(0, proxyEndpointXML.getXmlIssues().size());
	}
	
	@Test
	public void test_json_ok3() throws Exception {
		
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
				"                    <Name>EV-Extract-Things</Name>\r\n" + 
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
						"<ExtractVariables async=\"false\" continueOnError=\"false\" enabled=\"true\" name=\"EV-Extract-Things\">\r\n" + 
						"    <JSONPayload>\r\n" + 
						"        <Variable name=\"aVariable\" type=\"string\">\r\n" + 
						"            <JSONPath>$.here</JSONPath>\r\n" + 
						"        </Variable>\r\n" + 
						"    </JSONPayload>\r\n" + 
						"</ExtractVariables>"
					);
		assertEquals(0, issues.size());
		assertEquals(0, proxyEndpointXML.getXmlIssues().size());
	}

	@Test
	public void test_json_ok4() throws Exception {
		
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
				));
		BundleRecorder.clear();
		BundleRecorder.storeFile(proxyEndpointXML);

		List<XmlIssue> issues = getIssues(
			"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
					"<ExtractVariables async=\"false\" continueOnError=\"false\" enabled=\"true\" name=\"EV-Extract-Things\">\r\n" + 
					"    <JSONPayload>\r\n" + 
					"    </JSONPayload>\r\n" + 
					"</ExtractVariables>"
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
				"                <Name>EV-Extract-Things</Name>\r\n" + 
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
					"<ExtractVariables async=\"false\" continueOnError=\"false\" enabled=\"true\" name=\"EV-Extract-Things\">\r\n" + 
					"    <JSONPayload>\r\n" + 
					"        <Variable name=\"aVariable\" type=\"string\">\r\n" + 
					"            <JSONPath>$.here</JSONPath>\r\n" + 
					"        </Variable>\r\n" + 
					"    </JSONPayload>\r\n" + 
					"</ExtractVariables>"
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
				));
		BundleRecorder.clear();
		BundleRecorder.storeFile(proxyEndpointXML);

		List<XmlIssue> issues = getIssues(
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
		assertEquals(3, proxyEndpointXML.getXmlIssues().size());
	}		
}