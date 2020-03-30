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

import com.arkea.satd.sonar.xml.checks.TooMuchPoliciesCheck;
import com.arkea.satd.sonar.xml.checks.TooMuchProxyEndpointsCheck;

public class TooMuchPoliciesCheckTest extends AbstractCheckTester {

	private TooMuchPoliciesCheck check = new TooMuchPoliciesCheck();
	
	@Test
	public void test_default_for_proxy_ok() throws Exception {
		Collection<Issue> issues = getIssues(check, "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<APIProxy revision=\"1\" name=\"myAPI\">\r\n" + 
				"    <Policies>\r\n" + 
				"        <Policy>pol_1</Policy>\r\n" + 
				"        <Policy>pol_2</Policy>\r\n" + 
				"        <Policy>pol_3</Policy>\r\n" + 
				"        <Policy>pol_4</Policy>\r\n" + 
				"        <Policy>pol_5</Policy>\r\n" + 
				"        <Policy>po1_6</Policy>\r\n" + 
				"        <Policy>pol_7</Policy>\r\n" + 
				"        <Policy>pol_8</Policy>\r\n" + 
				"        <Policy>pol_9</Policy>\r\n" + 
				"        <Policy>pol_10</Policy>\r\n" + 
				"        <Policy>pol_11</Policy>\r\n" + 
				"        <Policy>pol_12</Policy>\r\n" + 
				"        <Policy>pol_13</Policy>\r\n" + 
				"        <Policy>pol_14</Policy>\r\n" + 
				"        <Policy>pol_15</Policy>\r\n" + 
				"        <Policy>po1_16</Policy>\r\n" + 
				"        <Policy>pol_17</Policy>\r\n" + 
				"        <Policy>pol_18</Policy>\r\n" + 
				"        <Policy>pol_19</Policy>\r\n" + 
				"        <Policy>pol_20</Policy>\r\n" + 
				"    </Policies>\r\n" + 
				"</APIProxy>"

		);
		assertEquals(0, issues.size());
	}
	
	
	@Test
	public void test_default_for_proxy_ko() throws Exception {
		Collection<Issue> issues = getIssues(check, "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<APIProxy revision=\"1\" name=\"myAPI\">\r\n" + 
				"    <Policies>\r\n" + 
				"        <Policy>pol_1</Policy>\r\n" + 
				"        <Policy>pol_2</Policy>\r\n" + 
				"        <Policy>pol_3</Policy>\r\n" + 
				"        <Policy>pol_4</Policy>\r\n" + 
				"        <Policy>pol_5</Policy>\r\n" + 
				"        <Policy>po1_6</Policy>\r\n" + 
				"        <Policy>pol_7</Policy>\r\n" + 
				"        <Policy>pol_8</Policy>\r\n" + 
				"        <Policy>pol_9</Policy>\r\n" + 
				"        <Policy>pol_10</Policy>\r\n" + 
				"        <Policy>pol_11</Policy>\r\n" + 
				"        <Policy>pol_12</Policy>\r\n" + 
				"        <Policy>pol_13</Policy>\r\n" + 
				"        <Policy>pol_14</Policy>\r\n" + 
				"        <Policy>pol_15</Policy>\r\n" + 
				"        <Policy>po1_16</Policy>\r\n" + 
				"        <Policy>pol_17</Policy>\r\n" + 
				"        <Policy>pol_18</Policy>\r\n" + 
				"        <Policy>pol_19</Policy>\r\n" + 
				"        <Policy>pol_20</Policy>\r\n" + 
				"        <Policy>pol_21</Policy>\r\n" + 
				"    </Policies>\r\n" + 
				"</APIProxy>"

		);
		assertEquals(1, issues.size());
	}	
	
	@Test
	public void test_configured_for_proxy_ok() throws Exception {
		check.setMaxAllowedPolicies(5);
		Collection<Issue> issues = getIssues(check, "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<APIProxy revision=\"1\" name=\"myAPI\">\r\n" + 
				"    <Policies>\r\n" + 
				"        <Policy>pol_1</Policy>\r\n" + 
				"        <Policy>pol_2</Policy>\r\n" + 
				"        <Policy>pol_3</Policy>\r\n" + 
				"        <Policy>pol_4</Policy>\r\n" + 
				"        <Policy>pol_5</Policy>\r\n" + 
				"    </Policies>\r\n" + 
				"</APIProxy>"

		);
		assertEquals(0, issues.size());
	}	
		
	@Test
	public void test_configured_for_proxy_ko() throws Exception {
		check.setMaxAllowedPolicies(4);
		Collection<Issue> issues = getIssues(check, "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<APIProxy revision=\"1\" name=\"myAPI\">\r\n" + 
				"    <Policies>\r\n" + 
				"        <Policy>pol_1</Policy>\r\n" + 
				"        <Policy>pol_2</Policy>\r\n" + 
				"        <Policy>pol_3</Policy>\r\n" + 
				"        <Policy>pol_4</Policy>\r\n" + 
				"        <Policy>pol_5</Policy>\r\n" + 
				"    </Policies>\r\n" + 
				"</APIProxy>"
		);
		assertEquals(1, issues.size());
	}	
		
	@Test
	public void test_default_for_sharedFlow_ok() throws Exception {
		Collection<Issue> issues = getIssues(check, "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<SharedFlowBundle revision=\"1\" name=\"myAPI\">\r\n" + 
				"    <Policies>\r\n" + 
				"        <Policy>pol_1</Policy>\r\n" + 
				"        <Policy>pol_2</Policy>\r\n" + 
				"        <Policy>pol_3</Policy>\r\n" + 
				"        <Policy>pol_4</Policy>\r\n" + 
				"        <Policy>pol_5</Policy>\r\n" + 
				"        <Policy>po1_6</Policy>\r\n" + 
				"        <Policy>pol_7</Policy>\r\n" + 
				"        <Policy>pol_8</Policy>\r\n" + 
				"        <Policy>pol_9</Policy>\r\n" + 
				"        <Policy>pol_10</Policy>\r\n" + 
				"        <Policy>pol_11</Policy>\r\n" + 
				"        <Policy>pol_12</Policy>\r\n" + 
				"        <Policy>pol_13</Policy>\r\n" + 
				"        <Policy>pol_14</Policy>\r\n" + 
				"        <Policy>pol_15</Policy>\r\n" + 
				"        <Policy>po1_16</Policy>\r\n" + 
				"        <Policy>pol_17</Policy>\r\n" + 
				"        <Policy>pol_18</Policy>\r\n" + 
				"        <Policy>pol_19</Policy>\r\n" + 
				"        <Policy>pol_20</Policy>\r\n" + 
				"    </Policies>\r\n" + 
				"</SharedFlowBundle>"

		);
		assertEquals(0, issues.size());
	}
	
	
	@Test
	public void test_default_for_sharedFlow_ko() throws Exception {
		Collection<Issue> issues = getIssues(check, "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<SharedFlowBundle revision=\"1\" name=\"myAPI\">\r\n" + 
				"    <Policies>\r\n" + 
				"        <Policy>pol_1</Policy>\r\n" + 
				"        <Policy>pol_2</Policy>\r\n" + 
				"        <Policy>pol_3</Policy>\r\n" + 
				"        <Policy>pol_4</Policy>\r\n" + 
				"        <Policy>pol_5</Policy>\r\n" + 
				"        <Policy>po1_6</Policy>\r\n" + 
				"        <Policy>pol_7</Policy>\r\n" + 
				"        <Policy>pol_8</Policy>\r\n" + 
				"        <Policy>pol_9</Policy>\r\n" + 
				"        <Policy>pol_10</Policy>\r\n" + 
				"        <Policy>pol_11</Policy>\r\n" + 
				"        <Policy>pol_12</Policy>\r\n" + 
				"        <Policy>pol_13</Policy>\r\n" + 
				"        <Policy>pol_14</Policy>\r\n" + 
				"        <Policy>pol_15</Policy>\r\n" + 
				"        <Policy>po1_16</Policy>\r\n" + 
				"        <Policy>pol_17</Policy>\r\n" + 
				"        <Policy>pol_18</Policy>\r\n" + 
				"        <Policy>pol_19</Policy>\r\n" + 
				"        <Policy>pol_20</Policy>\r\n" + 
				"        <Policy>pol_21</Policy>\r\n" + 
				"    </Policies>\r\n" + 
				"</SharedFlowBundle>"

		);
		assertEquals(1, issues.size());
	}	
	
	@Test
	public void test_configured_for_sharedFlow_ok() throws Exception {
		check.setMaxAllowedPolicies(5);
		Collection<Issue> issues = getIssues(check, "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<SharedFlowBundle revision=\"1\" name=\"myAPI\">\r\n" + 
				"    <Policies>\r\n" + 
				"        <Policy>pol_1</Policy>\r\n" + 
				"        <Policy>pol_2</Policy>\r\n" + 
				"        <Policy>pol_3</Policy>\r\n" + 
				"        <Policy>pol_4</Policy>\r\n" + 
				"        <Policy>pol_5</Policy>\r\n" + 
				"    </Policies>\r\n" + 
				"</SharedFlowBundle>"

		);
		assertEquals(0, issues.size());
	}	
		
	@Test
	public void test_configured_for_sharedFlow_ko() throws Exception {
		check.setMaxAllowedPolicies(4);
		Collection<Issue> issues = getIssues(check, "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<SharedFlowBundle revision=\"1\" name=\"myAPI\">\r\n" + 
				"    <Policies>\r\n" + 
				"        <Policy>pol_1</Policy>\r\n" + 
				"        <Policy>pol_2</Policy>\r\n" + 
				"        <Policy>pol_3</Policy>\r\n" + 
				"        <Policy>pol_4</Policy>\r\n" + 
				"        <Policy>pol_5</Policy>\r\n" + 
				"    </Policies>\r\n" + 
				"</SharedFlowBundle>"
		);
		assertEquals(1, issues.size());
	}	

}