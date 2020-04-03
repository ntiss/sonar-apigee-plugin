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

import com.arkea.satd.sonar.xml.checks.NondistributedQuotaCheck;


public class NondistributedQuotaCheckTest extends AbstractCheckTester {

	private SonarXmlCheck check = new NondistributedQuotaCheck();
	
	@Test
	public void test_ok() throws Exception {
		
		Collection<Issue> issues = getIssues(check, 
				"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<Quota name=\"QuotaPolicy\" type=\"calendar\">" + 
				"	<StartTime>2017-02-18 10:30:00</StartTime>" + 
				"	<Interval>5</Interval>" + 
				"	<TimeUnit>hour</TimeUnit>" + 
				"	<Allow count=\"99\"/>" + 
				"	<Distributed>true</Distributed>" + 
				"</Quota>");
		
		assertEquals(0, issues.size());
	}
	
	@Test
	public void test_ko1() throws Exception {
	
		Collection<Issue> issues = getIssues(check, 
				"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<Quota name=\"QuotaPolicy\" type=\"calendar\">" + 
				"	<StartTime>2017-02-18 10:30:00</StartTime>" + 
				"	<Interval>5</Interval>" + 
				"	<TimeUnit>hour</TimeUnit>" + 
				"	<Allow count=\"99\"/>" + 
				"</Quota>");

		assertEquals(1, issues.size());
	}

	@Test
	public void test_ko2() throws Exception {
		
		Collection<Issue> issues = getIssues(check, 
				"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<Quota name=\"QuotaPolicy\" type=\"calendar\">" + 
				"	<StartTime>2017-02-18 10:30:00</StartTime>" + 
				"	<Interval>5</Interval>" + 
				"	<TimeUnit>hour</TimeUnit>" + 
				"	<Allow count=\"99\"/>" + 
				"	<Distributed></Distributed>" + 
				"</Quota>");
	
		assertEquals(1, issues.size());
	}	
	
	@Test
	public void test_ko3() throws Exception {
		
		Collection<Issue> issues = getIssues(check, 
				"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<Quota name=\"QuotaPolicy\" type=\"calendar\">" + 
				"	<StartTime>2017-02-18 10:30:00</StartTime>" + 
				"	<Interval>5</Interval>" + 
				"	<TimeUnit>hour</TimeUnit>" + 
				"	<Allow count=\"99\"/>" + 
				"	<Distributed>false</Distributed>" + 
				"</Quota>");
	
		assertEquals(1, issues.size());
	}	

}