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

import com.arkea.satd.sonar.xml.checks.TooMuchResourcesCheck;

public class TooMuchResourcesCheckTest extends AbstractCheckTester {

	private TooMuchResourcesCheck check = new TooMuchResourcesCheck();
	
	@Test
	public void test_default_for_proxy_ok() throws Exception {
		Collection<Issue> issues = getIssues(check, "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<APIProxy revision=\"1\" name=\"myAPI\">\r\n" + 
				"    <Resources>\r\n" + 
				"        <Resource>res_1</Resource>\r\n" + 
				"        <Resource>res_2</Resource>\r\n" + 
				"        <Resource>res_3</Resource>\r\n" + 
				"        <Resource>res_4</Resource>\r\n" + 
				"        <Resource>res_5</Resource>\r\n" + 
				"        <Resource>po1_6</Resource>\r\n" + 
				"        <Resource>res_7</Resource>\r\n" + 
				"        <Resource>res_8</Resource>\r\n" + 
				"        <Resource>res_9</Resource>\r\n" + 
				"        <Resource>res_10</Resource>\r\n" + 
				"        <Resource>res_11</Resource>\r\n" + 
				"        <Resource>res_12</Resource>\r\n" + 
				"        <Resource>res_13</Resource>\r\n" + 
				"        <Resource>res_14</Resource>\r\n" + 
				"        <Resource>res_15</Resource>\r\n" + 
				"        <Resource>po1_16</Resource>\r\n" + 
				"        <Resource>res_17</Resource>\r\n" + 
				"        <Resource>res_18</Resource>\r\n" + 
				"        <Resource>res_19</Resource>\r\n" + 
				"        <Resource>res_20</Resource>\r\n" + 
				"    </Resources>\r\n" + 
				"</APIProxy>"

		);
		assertEquals(0, issues.size());
	}
	
	
	@Test
	public void test_default_for_proxy_ko() throws Exception {
		Collection<Issue> issues = getIssues(check, "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<APIProxy revision=\"1\" name=\"myAPI\">\r\n" + 
				"    <Resources>\r\n" + 
				"        <Resource>res_1</Resource>\r\n" + 
				"        <Resource>res_2</Resource>\r\n" + 
				"        <Resource>res_3</Resource>\r\n" + 
				"        <Resource>res_4</Resource>\r\n" + 
				"        <Resource>res_5</Resource>\r\n" + 
				"        <Resource>po1_6</Resource>\r\n" + 
				"        <Resource>res_7</Resource>\r\n" + 
				"        <Resource>res_8</Resource>\r\n" + 
				"        <Resource>res_9</Resource>\r\n" + 
				"        <Resource>res_10</Resource>\r\n" + 
				"        <Resource>res_11</Resource>\r\n" + 
				"        <Resource>res_12</Resource>\r\n" + 
				"        <Resource>res_13</Resource>\r\n" + 
				"        <Resource>res_14</Resource>\r\n" + 
				"        <Resource>res_15</Resource>\r\n" + 
				"        <Resource>po1_16</Resource>\r\n" + 
				"        <Resource>res_17</Resource>\r\n" + 
				"        <Resource>res_18</Resource>\r\n" + 
				"        <Resource>res_19</Resource>\r\n" + 
				"        <Resource>res_20</Resource>\r\n" + 
				"        <Resource>res_21</Resource>\r\n" + 
				"    </Resources>\r\n" + 
				"</APIProxy>"

		);
		assertEquals(1, issues.size());
	}	
	
	@Test
	public void test_configured_for_proxy_ok() throws Exception {
		check.setMaxAllowedResources(5);
		Collection<Issue> issues = getIssues(check, "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<APIProxy revision=\"1\" name=\"myAPI\">\r\n" + 
				"    <Resources>\r\n" + 
				"        <Resource>res_1</Resource>\r\n" + 
				"        <Resource>res_2</Resource>\r\n" + 
				"        <Resource>res_3</Resource>\r\n" + 
				"        <Resource>res_4</Resource>\r\n" + 
				"        <Resource>res_5</Resource>\r\n" + 
				"    </Resources>\r\n" + 
				"</APIProxy>"

		);
		assertEquals(0, issues.size());
	}	
		
	@Test
	public void test_configured_for_proxy_ko() throws Exception {
		check.setMaxAllowedResources(4);
		Collection<Issue> issues = getIssues(check, "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<APIProxy revision=\"1\" name=\"myAPI\">\r\n" + 
				"    <Resources>\r\n" + 
				"        <Resource>res_1</Resource>\r\n" + 
				"        <Resource>res_2</Resource>\r\n" + 
				"        <Resource>res_3</Resource>\r\n" + 
				"        <Resource>res_4</Resource>\r\n" + 
				"        <Resource>res_5</Resource>\r\n" + 
				"    </Resources>\r\n" + 
				"</APIProxy>"
		);
		assertEquals(1, issues.size());
	}	
		
	@Test
	public void test_default_for_sharedFlow_ok() throws Exception {
		Collection<Issue> issues = getIssues(check, "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<SharedFlowBundle revision=\"1\" name=\"myAPI\">\r\n" + 
				"    <Resources>\r\n" + 
				"        <Resource>res_1</Resource>\r\n" + 
				"        <Resource>res_2</Resource>\r\n" + 
				"        <Resource>res_3</Resource>\r\n" + 
				"        <Resource>res_4</Resource>\r\n" + 
				"        <Resource>res_5</Resource>\r\n" + 
				"        <Resource>po1_6</Resource>\r\n" + 
				"        <Resource>res_7</Resource>\r\n" + 
				"        <Resource>res_8</Resource>\r\n" + 
				"        <Resource>res_9</Resource>\r\n" + 
				"        <Resource>res_10</Resource>\r\n" + 
				"        <Resource>res_11</Resource>\r\n" + 
				"        <Resource>res_12</Resource>\r\n" + 
				"        <Resource>res_13</Resource>\r\n" + 
				"        <Resource>res_14</Resource>\r\n" + 
				"        <Resource>res_15</Resource>\r\n" + 
				"        <Resource>po1_16</Resource>\r\n" + 
				"        <Resource>res_17</Resource>\r\n" + 
				"        <Resource>res_18</Resource>\r\n" + 
				"        <Resource>res_19</Resource>\r\n" + 
				"        <Resource>res_20</Resource>\r\n" + 
				"    </Resources>\r\n" + 
				"</SharedFlowBundle>"

		);
		assertEquals(0, issues.size());
	}
	
	
	@Test
	public void test_default_for_sharedFlow_ko() throws Exception {
		Collection<Issue> issues = getIssues(check, "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<SharedFlowBundle revision=\"1\" name=\"myAPI\">\r\n" + 
				"    <Resources>\r\n" + 
				"        <Resource>res_1</Resource>\r\n" + 
				"        <Resource>res_2</Resource>\r\n" + 
				"        <Resource>res_3</Resource>\r\n" + 
				"        <Resource>res_4</Resource>\r\n" + 
				"        <Resource>res_5</Resource>\r\n" + 
				"        <Resource>po1_6</Resource>\r\n" + 
				"        <Resource>res_7</Resource>\r\n" + 
				"        <Resource>res_8</Resource>\r\n" + 
				"        <Resource>res_9</Resource>\r\n" + 
				"        <Resource>res_10</Resource>\r\n" + 
				"        <Resource>res_11</Resource>\r\n" + 
				"        <Resource>res_12</Resource>\r\n" + 
				"        <Resource>res_13</Resource>\r\n" + 
				"        <Resource>res_14</Resource>\r\n" + 
				"        <Resource>res_15</Resource>\r\n" + 
				"        <Resource>po1_16</Resource>\r\n" + 
				"        <Resource>res_17</Resource>\r\n" + 
				"        <Resource>res_18</Resource>\r\n" + 
				"        <Resource>res_19</Resource>\r\n" + 
				"        <Resource>res_20</Resource>\r\n" + 
				"        <Resource>res_21</Resource>\r\n" + 
				"    </Resources>\r\n" + 
				"</SharedFlowBundle>"

		);
		assertEquals(1, issues.size());
	}	
	
	@Test
	public void test_configured_for_sharedFlow_ok() throws Exception {
		check.setMaxAllowedResources(5);
		Collection<Issue> issues = getIssues(check, "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<SharedFlowBundle revision=\"1\" name=\"myAPI\">\r\n" + 
				"    <Resources>\r\n" + 
				"        <Resource>res_1</Resource>\r\n" + 
				"        <Resource>res_2</Resource>\r\n" + 
				"        <Resource>res_3</Resource>\r\n" + 
				"        <Resource>res_4</Resource>\r\n" + 
				"        <Resource>res_5</Resource>\r\n" + 
				"    </Resources>\r\n" + 
				"</SharedFlowBundle>"

		);
		assertEquals(0, issues.size());
	}	
		
	@Test
	public void test_configured_for_sharedFlow_ko() throws Exception {
		check.setMaxAllowedResources(4);
		Collection<Issue> issues = getIssues(check, "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<SharedFlowBundle revision=\"1\" name=\"myAPI\">\r\n" + 
				"    <Resources>\r\n" + 
				"        <Resource>res_1</Resource>\r\n" + 
				"        <Resource>res_2</Resource>\r\n" + 
				"        <Resource>res_3</Resource>\r\n" + 
				"        <Resource>res_4</Resource>\r\n" + 
				"        <Resource>res_5</Resource>\r\n" + 
				"    </Resources>\r\n" + 
				"</SharedFlowBundle>"
		);
		assertEquals(1, issues.size());
	}	

}