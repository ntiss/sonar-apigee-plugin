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

import com.arkea.satd.sonar.xml.checks.RegexLookAroundCheck;

public class RegexLookAroundCheckTest extends AbstractCheckTester {
	
	private SonarXmlCheck check = new RegexLookAroundCheck();

	@Test
	public void test_ok() throws Exception {
		Collection<Issue> issues = getIssues(check, 
			"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
			"<RegularExpressionProtection async=\"false\" continueOnError=\"false\" enabled=\"true\" name=\"Regular-Expression-Protection-1\">\r\n" + 
			"     <URIPath>\r\n" + 
			"         <Pattern>REGEX PATTERN1</Pattern>\r\n" + 
			"     </URIPath>\r\n" + 
			"     <QueryParam name=\"a-query-param\">\r\n" + 
			"         <Pattern>REGEX PATTERN2</Pattern>\r\n" + 
			"     </QueryParam>\r\n" + 
			"     <XMLPayload>\r\n" + 
			"         <Namespaces>\r\n" + 
			"             <Namespace prefix=\"apigee\">http://www.apigee.com</Namespace>\r\n" + 
			"         </Namespaces>\r\n" + 
			"         <XPath>\r\n" + 
			"             <Expression>/apigee:Greeting/apigee:User</Expression>\r\n" + 
			"             <Type>string</Type>\r\n" + 
			"             <Pattern>REGEX PATTERN3</Pattern>\r\n" + 
			"             <Pattern>REGEX PATTERN4</Pattern>\r\n" + 
			"         </XPath>\r\n" + 
			"     </XMLPayload>\r\n" + 
			"     <JSONPayload>\r\n" + 
			"         <JSONPath>\r\n" + 
			"             <Expression>$.store.book[*].author</Expression>\r\n" + 
			"             <Pattern>REGEX PATTERN5</Pattern>\r\n" + 
			"             <Pattern>REGEX PATTERN6</Pattern>\r\n" + 
			"         </JSONPath>\r\n" + 
			"	</JSONPayload>\r\n" + 
			"</RegularExpressionProtection>"
		);
		assertEquals(0, issues.size());
	}
	
	@Test
	public void test_ko() throws Exception {
		Collection<Issue> issues = getIssues(check, 
			"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
			"<RegularExpressionProtection async=\"false\" continueOnError=\"false\" enabled=\"true\" name=\"Regular-Expression-Protection-1\">\r\n" + 
			"     <URIPath>\r\n" + 
			"         <Pattern>(?REGEX PATTERN1)</Pattern>\r\n" + 
			"     </URIPath>\r\n" + 
			"     <QueryParam name=\"a-query-param\">\r\n" + 
			"         <Pattern>(?REGEX PATTERN2)</Pattern>\r\n" + 
			"     </QueryParam>\r\n" + 
			"     <XMLPayload>\r\n" + 
			"         <Namespaces>\r\n" + 
			"             <Namespace prefix=\"apigee\">http://www.apigee.com</Namespace>\r\n" + 
			"         </Namespaces>\r\n" + 
			"         <XPath>\r\n" + 
			"             <Expression>/apigee:Greeting/apigee:User</Expression>\r\n" + 
			"             <Type>string</Type>\r\n" + 
			"             <Pattern>(?REGEX PATTERN3)</Pattern>\r\n" + 
			"             <Pattern>(?REGEX PATTERN4)</Pattern>\r\n" + 
			"         </XPath>\r\n" + 
			"     </XMLPayload>\r\n" + 
			"     <JSONPayload>\r\n" + 
			"         <JSONPath>\r\n" + 
			"             <Expression>$.store.book[*].author</Expression>\r\n" + 
			"             <Pattern>(?REGEX PATTERN5)</Pattern>\r\n" + 
			"             <Pattern>(?REGEX PATTERN6)</Pattern>\r\n" + 
			"         </JSONPath>\r\n" + 
			"	</JSONPayload>\r\n" + 
			"</RegularExpressionProtection>"
		);
		assertEquals(6, issues.size());
		
		
	}	

}