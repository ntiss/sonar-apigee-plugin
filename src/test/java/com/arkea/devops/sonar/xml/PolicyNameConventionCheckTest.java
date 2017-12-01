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
import org.sonar.plugins.xml.checks.PolicyNameConventionCheck;
import org.sonar.plugins.xml.checks.XmlIssue;
import org.sonar.plugins.xml.checks.XmlSourceCode;

public class PolicyNameConventionCheckTest extends AbstractCheckTester {

	private List<XmlIssue> getIssues(String content) throws IOException {
		XmlSourceCode sourceCode = parseAndCheck(createTempFile(content), new PolicyNameConventionCheck());
		return sourceCode.getXmlIssues();
	}

	@Test
	public void test_ok1() throws Exception {
		List<XmlIssue> issues = getIssues("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n"
				+ "<RaiseFault async=\"false\" continueOnError=\"false\" enabled=\"true\" name=\"RF-Raise-Fault-1\">\r\n"
				+ "</RaiseFault>");
		assertEquals(0, issues.size());
	}

	@Test
	public void test_ok2() throws Exception {
		List<XmlIssue> issues = getIssues("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n"
				+ "<SpikeArrest async=\"false\" continueOnError=\"false\" enabled=\"true\" name=\"spike_arrest-1\">\r\n"
				+ "</SpikeArrest>");
		assertEquals(0, issues.size());
	}

	@Test
	public void test_ok3() throws Exception {
		List<XmlIssue> issues = getIssues("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n"
				+ "<XMLToJSON async=\"false\" continueOnError=\"false\" enabled=\"true\" name=\"xTOj-transform_54654\">\r\n"
				+ "</XMLToJSON>");
		assertEquals(0, issues.size());
	}

	@Test
	public void test_ok4() throws Exception {
		List<XmlIssue> issues = getIssues("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n"
				+ "<XMLToJSON async=\"false\" continueOnError=\"false\" enabled=\"true\" name=\"xTOjtransform_54654\">\r\n"
				+ "</XMLToJSON>");
		assertEquals(0, issues.size());
	}
	
	@Test
	public void test_bad_type1() throws Exception {
		List<XmlIssue> issues = getIssues("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n"
				+ "<RaiseFault async=\"false\" continueOnError=\"false\" enabled=\"true\" name=\"Raise-Fault-1\">\r\n"
				+ "</RaiseFault>");
		assertEquals(1, issues.size());
	}

	@Test
	public void test_no_type() throws Exception {
		List<XmlIssue> issues = getIssues("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n"
				+ "<RaiseFault async=\"false\" continueOnError=\"false\" enabled=\"true\" name=\"error-1\">\r\n"
				+ "</RaiseFault>");
		assertEquals(1, issues.size());
	}

	@Test
	public void test_bad_type2() throws Exception {
		List<XmlIssue> issues = getIssues("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n"
				+ "<XMLToJSON async=\"false\" continueOnError=\"false\" enabled=\"true\" name=\"xml2json-transform_54654\">\r\n"
				+ "</XMLToJSON>");
		assertEquals(1, issues.size());
	}
	
	
}