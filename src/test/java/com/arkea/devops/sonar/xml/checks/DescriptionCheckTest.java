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
import org.sonar.plugins.xml.checks.DescriptionCheck;
import org.sonar.plugins.xml.checks.XmlIssue;
import org.sonar.plugins.xml.checks.XmlSourceCode;

public class DescriptionCheckTest extends AbstractCheckTester {

	private List<XmlIssue> getIssues(String content) throws IOException {
		XmlSourceCode sourceCode = parseAndCheck(createTempFile(content), new DescriptionCheck());
		return sourceCode.getXmlIssues();
	}

	@Test
	public void test_ok() throws Exception {
		List<XmlIssue> issues = getIssues(
			"<APIProxy revision=\"1\" name=\"XXX\">\r\n" + 
			"    <Description>Text of description</Description>\r\n" + 
			"</APIProxy>"
		);
		assertEquals(0, issues.size());
	}
	
	@Test
	public void test_too_short() throws Exception {
		List<XmlIssue> issues = getIssues(
			"<APIProxy revision=\"1\" name=\"XXX\">\r\n" + 
			"    <Description>short</Description>\r\n" + 
			"</APIProxy>"
		);
		assertEquals(1, issues.size());
	}	

}