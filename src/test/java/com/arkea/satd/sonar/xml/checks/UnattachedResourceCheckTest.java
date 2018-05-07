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

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.Test;
import org.sonar.plugins.xml.checks.BundleRecorder;
import org.sonar.plugins.xml.checks.UnattachedResourceCheck;
import org.sonar.plugins.xml.checks.XmlIssue;
import org.sonar.plugins.xml.checks.XmlSourceCode;

public class UnattachedResourceCheckTest extends AbstractCheckTester {

	private List<XmlIssue> getIssues(File theFile) throws IOException {
		XmlSourceCode sourceCode = parseAndCheck(theFile, new UnattachedResourceCheck());
		return sourceCode.getXmlIssues();
	}
	
	@Test
	public void test_ok_deal_with_xslt() throws Exception {

		// Fake XSL Script
		String theScript = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\r\n" + 
				"<xsl:stylesheet version=\"1.0\" xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\">\r\n" + 
				"</xsl:stylesheet>";
		File tempScript = createTempFile(theScript, "XSL-Transform-1.xsl", "xsl");

		BundleRecorder.clear();
		BundleRecorder.storeFile(parse(tempScript));
		
		// Fake XMLPolicy file
		XmlSourceCode proxyEndpointXML = parse(createTempFile("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<XSL async=\"false\" continueOnError=\"false\" enabled=\"true\" name=\"XSL-Transform-1\">\r\n" + 
				"    <DisplayName>XSL Transform-1</DisplayName>\r\n" + 
				"    <Properties/>\r\n" + 
				"    <Source>request</Source>\r\n" + 
				"    <ResourceURL>xsl://"+ tempScript.getName() +"</ResourceURL>\r\n" + 
				"    <Parameters ignoreUnresolvedVariables=\"true\" />\r\n" + 
				"    <OutputVariable></OutputVariable>\r\n" + 
				"</XSL>" 
				));
		BundleRecorder.storeFile(proxyEndpointXML);
		
		List<XmlIssue> issues = getIssues(tempScript);
		assertEquals(0, issues.size());
	}	
	
	@Test
	public void test_ko_deal_with_xslt() throws Exception {

		// Fake XSL Script
		String theScript = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\r\n" + 
				"<xsl:stylesheet version=\"1.0\" xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\">\r\n" + 
				"</xsl:stylesheet>";
		File tempScript = createTempFile(theScript, "XSL-Transform-1.xsl", "xsl");

		BundleRecorder.clear();
		BundleRecorder.storeFile(parse(tempScript));
	
		List<XmlIssue> issues = getIssues(tempScript);
		assertEquals(1, issues.size());
	}
	
	@Test
	public void test_ok_deal_with_wsdl() throws Exception {

		// Fake WSDL Script
		String theScript = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\r\n" + 
				"<wsdl:definitions xmlns:wsdl=\"http://schemas.xmlsoap.org/wsdl/\">\r\n" + 
				"    <wsdl:types>\r\n" + 
				"        <xs:schema xmlns:xs=\"http://www.w3.org/2001/XMLSchema\">\r\n" + 
				"            <xs:element name=\"request\" type=\"xs:string\"/>\r\n" + 
				"        </xs:schema>\r\n" + 
				"    </wsdl:types>\r\n" + 
				"</wsdl:definitions>";
		File tempScript = createTempFile(theScript, "Script-1.wsdl", "xsl");

		BundleRecorder.clear();
		BundleRecorder.storeFile(parse(tempScript));
		
		// Fake XMLPolicy file
		XmlSourceCode proxyEndpointXML = parse(createTempFile("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<MessageValidation async=\"false\" continueOnError=\"false\" enabled=\"true\" name=\"SOAP-Message-Validation-1\">\r\n" + 
				"    <DisplayName>SOAP Message Validation-1</DisplayName>\r\n" + 
				"    <Properties/>\r\n" + 
				"    <Element namespace=\"http://sample.com\"> sampleObject</Element>\r\n" + 
				"    <SOAPMessage/>\r\n" + 
				"    <Source>request</Source>\r\n" + 
				"    <ResourceURL>wsdl://"+tempScript.getName()+"</ResourceURL>\r\n" + 
				"</MessageValidation>" 
				));
		BundleRecorder.storeFile(proxyEndpointXML);
		
		List<XmlIssue> issues = getIssues(tempScript);
		assertEquals(0, issues.size());
	}	
	
	@Test
	public void test_ko_deal_with_wsdl() throws Exception {

		// Fake WSDL Script
		String theScript = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\r\n" + 
				"<wsdl:definitions xmlns:wsdl=\"http://schemas.xmlsoap.org/wsdl/\">\r\n" + 
				"    <wsdl:types>\r\n" + 
				"        <xs:schema xmlns:xs=\"http://www.w3.org/2001/XMLSchema\">\r\n" + 
				"            <xs:element name=\"request\" type=\"xs:string\"/>\r\n" + 
				"        </xs:schema>\r\n" + 
				"    </wsdl:types>\r\n" + 
				"</wsdl:definitions>";
		File tempScript = createTempFile(theScript, "XSL-Transform-1.xsl", "xsl");

		BundleRecorder.clear();
		BundleRecorder.storeFile(parse(tempScript));
	
		List<XmlIssue> issues = getIssues(tempScript);
		assertEquals(1, issues.size());
	}	

	
	@Test
	public void test_ok_deal_with_xsd() throws Exception {

		// Fake XSD Script
		String theScript = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\r\n" + 
				"<xs:schema xmlns:xs=\"http://www.w3.org/2001/XMLSchema\">\r\n" + 
				"    <xs:element name=\"request\" type=\"xs:string\"/>\r\n" + 
				"</xs:schema>";
		File tempScript = createTempFile(theScript, "Script-1.wsdl", "xsl");

		BundleRecorder.clear();
		BundleRecorder.storeFile(parse(tempScript));
		
		// Fake XMLPolicy file
		XmlSourceCode proxyEndpointXML = parse(createTempFile("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<MessageValidation async=\"false\" continueOnError=\"false\" enabled=\"true\" name=\"SOAP-Message-Validation-1\">\r\n" + 
				"    <DisplayName>XSD Message Validation-1</DisplayName>\r\n" + 
				"    <Properties/>\r\n" + 
				"    <Element namespace=\"http://sample.com\"> sampleObject</Element>\r\n" + 
				"    <SOAPMessage/>\r\n" + 
				"    <Source>request</Source>\r\n" + 
				"    <ResourceURL>xsd://"+tempScript.getName()+"</ResourceURL>\r\n" + 
				"</MessageValidation>" 
				));
		BundleRecorder.storeFile(proxyEndpointXML);
		
		List<XmlIssue> issues = getIssues(tempScript);
		assertEquals(0, issues.size());
	}	
	
	@Test
	public void test_ko_deal_with_xsd() throws Exception {

		// Fake XSD Script
		String theScript = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\r\n" + 
				"<xs:schema xmlns:xs=\"http://www.w3.org/2001/XMLSchema\">\r\n" + 
				"    <xs:element name=\"request\" type=\"xs:string\"/>\r\n" + 
				"</xs:schema>";
		File tempScript = createTempFile(theScript, "XSL-Transform-1.xsl", "xsl");

		BundleRecorder.clear();
		BundleRecorder.storeFile(parse(tempScript));
	
		List<XmlIssue> issues = getIssues(tempScript);
		assertEquals(1, issues.size());
	}	
	
	
	
}