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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;
import org.sonar.api.batch.fs.internal.DefaultInputFile;
import org.sonar.api.batch.sensor.issue.Issue;
import org.sonar.plugins.xml.checks.BundleRecorder;
import org.sonar.plugins.xml.checks.CacheCoherenceCheck;
import org.sonarsource.analyzer.commons.xml.XmlFile;
import org.sonarsource.analyzer.commons.xml.checks.SonarXmlCheck;

public class CacheCoherenceCheckTest extends AbstractCheckTester {

	private SonarXmlCheck check = new CacheCoherenceCheck();
	
	@Test
	public void test_ok1() throws Exception {
		
		// Fake LookupCache file
		XmlFile lookupCacheXML = createTempFile("", 
			"<LookupCache async=\"false\" continueOnError=\"false\" enabled=\"true\" name=\"Lookup-Cache-1\">\r\n" + 
			"    <CacheKey>\r\n" + 
			"        <Prefix>thePrefix</Prefix>\r\n" + 
			"        <KeyFragment>fragment</KeyFragment>\r\n" + 
			"    </CacheKey>\r\n" + 
			"</LookupCache>");
		BundleRecorder.clear();
		BundleRecorder.storeFile(lookupCacheXML);

		Collection<Issue> issues = getIssues(check, 
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
		Collection<Issue> issues = getIssues(check, 
			"<PopulateCache async=\"false\" continueOnError=\"false\" enabled=\"true\" name=\"Populate-Cache-1\">\r\n" + 
			"    <CacheKey>\r\n" + 
			"        <Prefix/>\r\n" + 
			"        <KeyFragment ref=\"\"/>\r\n" + 
			"    </CacheKey>\r\n" + 
			"</PopulateCache>");
		
		assertEquals(1, issues.size());
		Issue issue = (new ArrayList<>(issues)).get(0);
		assertTrue(issue.primaryLocation().message().startsWith("PopulateCache"));
	}
	
	@Test
	public void test_ko2_no_corresponding() throws Exception {
		Collection<Issue> issues = getIssues(check, 
			"<LookupCache async=\"false\" continueOnError=\"false\" enabled=\"true\" name=\"Lookup-Cache-1\">\r\n" + 
			"    <CacheKey>\r\n" + 
			"        <Prefix>thePrefix</Prefix>\r\n" + 
			"        <KeyFragment>fragment</KeyFragment>\r\n" + 
			"    </CacheKey>\r\n" + 
			"</LookupCache>");
		
		assertEquals(1, issues.size());
		Issue issue = (new ArrayList<>(issues)).get(0);
		assertTrue(issue.primaryLocation().message().startsWith("LookupCache"));
	}

	
	@Test
	public void test_ko3_bad_corresponding() throws Exception {
		
		// Fake LookupCache file
		String tempFileName = "lookupCache.xml";
		XmlFile lookupCacheXML = createTempFile(tempFileName, 
			"<LookupCache async=\"false\" continueOnError=\"false\" enabled=\"true\" name=\"Lookup-Cache-1\">\r\n" + 
			"    <CacheKey>\r\n" + 
			"        <Prefix>thePrefix</Prefix>\r\n" + 
			"        <KeyFragment>fragment</KeyFragment>\r\n" + 
			"    </CacheKey>\r\n" + 
			"</LookupCache>");
		BundleRecorder.clear();
		BundleRecorder.storeFile(lookupCacheXML);

		Collection<Issue> issues = getIssues(check, 
			"<PopulateCache async=\"false\" continueOnError=\"false\" enabled=\"true\" name=\"Populate-Cache-1\">\r\n" + 
			"    <CacheKey>\r\n" + 
			"        <Prefix>thePrefix</Prefix>\r\n" + 
			"        <KeyFragment ref=\"theRef\"/>\r\n" + 
			"    </CacheKey>\r\n" + 
			"</PopulateCache>"
		);
		
		assertEquals(1, issues.size());
		
		// assert also the location of the issue
		Issue iss = issues.toArray(new Issue[] {})[0];
		DefaultInputFile dif = (DefaultInputFile)iss.primaryLocation().inputComponent();
		assertFalse(tempFileName.equals(dif.filename()));
	}

	@Test
	public void test_ko4_bad_corresponding() throws Exception {
		BundleRecorder.clear();
		
		// Fake LookupCache file
		String tempFileName1 = "lookupCache.xml";
		BundleRecorder.storeFile(createTempFile(tempFileName1, 
			"<LookupCache async=\"false\" continueOnError=\"false\" enabled=\"true\" name=\"Lookup-Cache-1\">\r\n" + 
			"    <CacheKey>\r\n" + 
			"        <Prefix>thePrefix1</Prefix>\r\n" + 
			"        <KeyFragment>fragment</KeyFragment>\r\n" + 
			"    </CacheKey>\r\n" + 
			"</LookupCache>"));
		
		String tempFileName2 = "populateCache.xml";
		BundleRecorder.storeFile(createTempFile(tempFileName2, 
			"<PopulateCache async=\"false\" continueOnError=\"false\" enabled=\"true\" name=\"Populate-Cache-1\">\r\n" + 
			"    <CacheKey>\r\n" + 
			"        <Prefix>thePrefix</Prefix>\r\n" + 
			"        <KeyFragment ref=\"theRef\"/>\r\n" + 
			"    </CacheKey>\r\n" + 
			"</PopulateCache>"));		

		Collection<Issue> issues = getIssues(check, 
			"<LookupCache async=\"false\" continueOnError=\"false\" enabled=\"true\" name=\"Lookup-Cache-2\">\r\n" + 
			"    <CacheKey>\r\n" + 
			"        <Prefix>thePrefix2</Prefix>\r\n" + 
			"        <KeyFragment>fragment</KeyFragment>\r\n" + 
			"    </CacheKey>\r\n" + 
			"</LookupCache>");
		
		assertEquals(1, issues.size());
		
		// assert also the location of the issue
		Issue iss = issues.toArray(new Issue[] {})[0];
		DefaultInputFile dif = (DefaultInputFile)iss.primaryLocation().inputComponent();
		assertFalse(tempFileName1.equals(dif.filename()));
		assertFalse(tempFileName2.equals(dif.filename()));
		
	}
	
	
}