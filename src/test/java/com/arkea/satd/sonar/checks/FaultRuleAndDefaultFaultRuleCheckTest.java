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

import com.arkea.satd.sonar.xml.checks.FaultRuleAndDefaultFaultRuleCheck;

public class FaultRuleAndDefaultFaultRuleCheckTest extends AbstractCheckTester {

	private SonarXmlCheck check = new FaultRuleAndDefaultFaultRuleCheck();
		
	@Test
	public void test_ok1() throws Exception {
		Collection<Issue> issues = getIssues(check,
			"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
			"<ProxyEndpoint name=\"default\">\r\n" + 
			"    <FaultRules>\r\n" + 
			"        <FaultRule name=\"baderror\">\r\n" + 
			"          <Condition>message.status.code=403</Condition>\r\n" + 
			"          <Step>\r\n" + 
			"            <Name>RF-Raise403</Name>\r\n" + 
			"          </Step>\r\n" + 
			"        </FaultRule>\r\n" + 
			"    </FaultRules>\r\n" + 
			"</ProxyEndpoint>"
		);
		assertEquals(0, issues.size());
	}
	
	@Test
	public void test_ok2() throws Exception {
		Collection<Issue> issues = getIssues(check,
			"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
			"<ProxyEndpoint name=\"default\">\r\n" + 
			"    <FaultRules>\r\n" + 
			"        <FaultRule name=\"baderror\">\r\n" + 
			"          <Step>\r\n" + 
			"            <Name>RF-Raise403</Name>\r\n" + 
			"          </Step>\r\n" + 
			"        </FaultRule>\r\n" + 
			"    </FaultRules>\r\n" + 
			"</ProxyEndpoint>"
		);
		assertEquals(0, issues.size());
	}

	@Test
	public void test_ok3() throws Exception {
		Collection<Issue> issues = getIssues(check,
			"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
			"<ProxyEndpoint name=\"default\">\r\n" + 
			"    <DefaultFaultRule>\r\n" + 
			"       <Step>\r\n" + 
			"          <Name>RF-Raise403</Name>\r\n" + 
			"        </Step>\r\n" + 
			"    </DefaultFaultRule>\r\n" + 
			"</ProxyEndpoint>"
		);
		assertEquals(0, issues.size());
	}
	
	@Test
	public void test_ok4() throws Exception {
		Collection<Issue> issues = getIssues(check,
			"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
			"<ProxyEndpoint name=\"default\">\r\n" + 
			"    <FaultRules>\r\n" + 
			"        <FaultRule name=\"baderror\">\r\n" + 
			"          <Condition>message.status.code=403</Condition>\r\n" + 
			"          <Step>\r\n" + 
			"            <Name>RF-Raise403</Name>\r\n" + 
			"          </Step>\r\n" + 
			"        </FaultRule>\r\n" + 
			"    </FaultRules>\r\n" + 
			"    <DefaultFaultRule>\r\n" + 
			"       <Step>\r\n" + 
			"          <Name>RF-Raise403</Name>\r\n" + 
			"        </Step>\r\n" + 
			"    </DefaultFaultRule>\r\n" + 
			"</ProxyEndpoint>"
		);
		assertEquals(0, issues.size());
	}
		
	@Test
	public void test_ok5() throws Exception {
		Collection<Issue> issues = getIssues(check,
				"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<ProxyEndpoint name=\"default\">\r\n" + 
				"    <FaultRules>\r\n" + 
				"        <FaultRule name=\"baderror\">\r\n" + 
				"          <Step>\r\n" + 
				"            <Name>RF-Raise403</Name>\r\n" + 
				"          </Step>\r\n" + 
				"        </FaultRule>\r\n" + 
				"    </FaultRules>\r\n" + 
				"    <DefaultFaultRule>\r\n" + 
				"       <AlwaysEnforce>true</AlwaysEnforce>" +  // No issue is triggered because of <AlwaysEnforce>  (see https://github.com/CreditMutuelArkea/sonar-apigee-plugin/issues/9 ) 
				"		<Step>\r\n" + 
				"          <Name>RF-Raise403</Name>\r\n" + 
				"        </Step>\r\n" + 
				"    </DefaultFaultRule>\r\n" + 
				"</ProxyEndpoint>"
			);
		assertEquals(0, issues.size());
	}

	@Test
	public void test_ko1() throws Exception {
		Collection<Issue> issues = getIssues(check,
				"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<ProxyEndpoint name=\"default\">\r\n" + 
				"    <FaultRules>\r\n" + 
				"        <FaultRule name=\"baderror\">\r\n" + 
				"          <Step>\r\n" + 
				"            <Name>RF-Raise403</Name>\r\n" + 
				"          </Step>\r\n" + 
				"        </FaultRule>\r\n" + 
				"    </FaultRules>\r\n" + 
				"    <DefaultFaultRule>\r\n" + 
				"       <Step>\r\n" + 
				"          <Name>RF-Raise403</Name>\r\n" + 
				"        </Step>\r\n" + 
				"    </DefaultFaultRule>\r\n" + 
				"</ProxyEndpoint>"
			);
		assertEquals(2, issues.size());
	}

	@Test
	public void test_ko2() throws Exception {
		Collection<Issue> issues = getIssues(check,
				"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<ProxyEndpoint name=\"default\">\r\n" + 
				"    <FaultRules>\r\n" + 
				"        <FaultRule name=\"baderror\">\r\n" + 
				"          <Step>\r\n" + 
				"            <Name>RF-Raise403</Name>\r\n" + 
				"          </Step>\r\n" + 
				"        </FaultRule>\r\n" + 
				"    </FaultRules>\r\n" + 
				"    <DefaultFaultRule>\r\n" + 
				"       <AlwaysEnforce>false</AlwaysEnforce>" + 
				"		<Step>\r\n" + 
				"          <Name>RF-Raise403</Name>\r\n" + 
				"        </Step>\r\n" + 
				"    </DefaultFaultRule>\r\n" + 
				"</ProxyEndpoint>"
			);
		assertEquals(0, issues.size());
	}

}