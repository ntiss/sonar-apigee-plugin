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
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import org.junit.Test;
import org.sonar.plugins.xml.checks.BundleRecorder;
import org.sonar.plugins.xml.checks.CacheCoherenceCheck;
import org.sonar.plugins.xml.checks.XmlIssue;
import org.sonar.plugins.xml.checks.XmlSourceCode;

public class CacheCoherenceCheckTest extends AbstractCheckTester {

	private List<XmlIssue> getIssues(String content) throws IOException {
		XmlSourceCode sourceCode = parseAndCheck(createTempFile(content), new CacheCoherenceCheck());
		return sourceCode.getXmlIssues();
	}
	
	@Test
	public void test_ok1() throws Exception {
		
		// Fake LookupCache file
		XmlSourceCode lookupCacheXML = parse(createTempFile(
			"<LookupCache async=\"false\" continueOnError=\"false\" enabled=\"true\" name=\"Lookup-Cache-1\">\r\n" + 
			"    <CacheKey>\r\n" + 
			"        <Prefix>thePrefix</Prefix>\r\n" + 
			"        <KeyFragment>fragment</KeyFragment>\r\n" + 
			"    </CacheKey>\r\n" + 
			"</LookupCache>"));
		BundleRecorder.clear();
		BundleRecorder.storeFile(lookupCacheXML);

		List<XmlIssue> issues = getIssues(
			"<PopulateCache async=\"false\" continueOnError=\"false\" enabled=\"true\" name=\"Populate-Cache-1\">\r\n" + 
			"    <CacheKey>\r\n" + 
			"        <Prefix>thePrefix</Prefix>\r\n" + 
			"        <KeyFragment>fragment</KeyFragment>\r\n" + 
			"    </CacheKey>\r\n" + 
			"</PopulateCache>");
		
		assertEquals(0, issues.size());
	}
	
	@Test
	public void test_ko1_no_corresponding() throws Exception {
		List<XmlIssue> issues = getIssues(
			"<PopulateCache async=\"false\" continueOnError=\"false\" enabled=\"true\" name=\"Populate-Cache-1\">\r\n" + 
			"    <CacheKey>\r\n" + 
			"        <Prefix/>\r\n" + 
			"        <KeyFragment ref=\"\"/>\r\n" + 
			"    </CacheKey>\r\n" + 
			"</PopulateCache>");
		
		assertEquals(1, issues.size());
		assertTrue(issues.get(0).getMessage().startsWith("PopulateCache"));
	}
	
	@Test
	public void test_ko2_no_corresponding() throws Exception {
		List<XmlIssue> issues = getIssues(
			"<LookupCache async=\"false\" continueOnError=\"false\" enabled=\"true\" name=\"Lookup-Cache-1\">\r\n" + 
			"    <CacheKey>\r\n" + 
			"        <Prefix>thePrefix</Prefix>\r\n" + 
			"        <KeyFragment>fragment</KeyFragment>\r\n" + 
			"    </CacheKey>\r\n" + 
			"</LookupCache>");
		
		assertEquals(1, issues.size());
		assertTrue(issues.get(0).getMessage().startsWith("LookupCache"));
	}

	
	@Test
	public void test_ko3_bad_corresponding() throws Exception {
		
		// Fake LookupCache file
		XmlSourceCode lookupCacheXML = parse(createTempFile(
			"<LookupCache async=\"false\" continueOnError=\"false\" enabled=\"true\" name=\"Lookup-Cache-1\">\r\n" + 
			"    <CacheKey>\r\n" + 
			"        <Prefix>thePrefix</Prefix>\r\n" + 
			"        <KeyFragment>fragment</KeyFragment>\r\n" + 
			"    </CacheKey>\r\n" + 
			"</LookupCache>"));
		BundleRecorder.clear();
		BundleRecorder.storeFile(lookupCacheXML);

		List<XmlIssue> issues = getIssues(
			"<PopulateCache async=\"false\" continueOnError=\"false\" enabled=\"true\" name=\"Populate-Cache-1\">\r\n" + 
			"    <CacheKey>\r\n" + 
			"        <Prefix>thePrefix</Prefix>\r\n" + 
			"        <KeyFragment ref=\"theRef\"/>\r\n" + 
			"    </CacheKey>\r\n" + 
			"</PopulateCache>"
		);
		
		assertEquals(1, issues.size());
	}

	@Test
	public void test_ko4_bad_corresponding() throws Exception {
		BundleRecorder.clear();
		
		// Fake LookupCache file
		BundleRecorder.storeFile(parse(createTempFile(
			"<LookupCache async=\"false\" continueOnError=\"false\" enabled=\"true\" name=\"Lookup-Cache-1\">\r\n" + 
			"    <CacheKey>\r\n" + 
			"        <Prefix>thePrefix1</Prefix>\r\n" + 
			"        <KeyFragment>fragment</KeyFragment>\r\n" + 
			"    </CacheKey>\r\n" + 
			"</LookupCache>")));
		
		BundleRecorder.storeFile(parse(createTempFile(
			"<PopulateCache async=\"false\" continueOnError=\"false\" enabled=\"true\" name=\"Populate-Cache-1\">\r\n" + 
			"    <CacheKey>\r\n" + 
			"        <Prefix>thePrefix</Prefix>\r\n" + 
			"        <KeyFragment ref=\"theRef\"/>\r\n" + 
			"    </CacheKey>\r\n" + 
			"</PopulateCache>")));		

		List<XmlIssue> issues = getIssues(
			"<LookupCache async=\"false\" continueOnError=\"false\" enabled=\"true\" name=\"Lookup-Cache-2\">\r\n" + 
			"    <CacheKey>\r\n" + 
			"        <Prefix>thePrefix2</Prefix>\r\n" + 
			"        <KeyFragment>fragment</KeyFragment>\r\n" + 
			"    </CacheKey>\r\n" + 
			"</LookupCache>");
		
		assertEquals(1, issues.size());
	}
	
	
}