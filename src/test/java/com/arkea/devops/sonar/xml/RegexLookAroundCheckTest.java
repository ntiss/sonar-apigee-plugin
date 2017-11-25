package com.arkea.devops.sonar.xml;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.List;

import org.junit.Test;
import org.sonar.plugins.xml.checks.RegexLookAroundCheck;
import org.sonar.plugins.xml.checks.XmlIssue;
import org.sonar.plugins.xml.checks.XmlSourceCode;

public class RegexLookAroundCheckTest extends AbstractCheckTester {
	
	private List<XmlIssue> getIssues(String content) throws IOException {
		XmlSourceCode sourceCode = parseAndCheck(createTempFile(content), new RegexLookAroundCheck());
		return sourceCode.getXmlIssues();
	}

	@Test
	public void test_ok() throws Exception {
		List<XmlIssue> issues = getIssues(
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
		List<XmlIssue> issues = getIssues(
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