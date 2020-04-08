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

import com.arkea.satd.sonar.xml.checks.TooMuchProxyEndpointsCheck;

public class TooMuchProxyEndpointsCheckTest extends AbstractCheckTester {

	private TooMuchProxyEndpointsCheck check = new TooMuchProxyEndpointsCheck();
	
	@Test
	public void test_default_ok() throws Exception {
		Collection<Issue> issues = getIssues(check, "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<APIProxy revision=\"1\" name=\"myAPI\">\r\n" + 
				"    <ProxyEndpoints>\r\n" + 
				"        <ProxyEndpoint>default</ProxyEndpoint>\r\n" + 
				"        <ProxyEndpoint>public</ProxyEndpoint>\r\n" + 
				"    </ProxyEndpoints>\r\n" + 
				"</APIProxy>"

		);
		assertEquals(0, issues.size());
	}
	
	
	@Test
	public void test_default_ko() throws Exception {
		Collection<Issue> issues = getIssues(check, "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<APIProxy revision=\"1\" name=\"myAPI\">\r\n" + 
				"    <ProxyEndpoints>\r\n" + 
				"        <ProxyEndpoint>default</ProxyEndpoint>\r\n" + 
				"        <ProxyEndpoint>public-1</ProxyEndpoint>\r\n" + 
				"        <ProxyEndpoint>public-2</ProxyEndpoint>\r\n" + 
				"    </ProxyEndpoints>\r\n" + 
				"</APIProxy>"

		);
		assertEquals(1, issues.size());
	}	
	
	@Test
	public void test_configured_ok() throws Exception {
		check.setMaxAllowedEndpoints(3);
		Collection<Issue> issues = getIssues(check, "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<APIProxy revision=\"1\" name=\"myAPI\">\r\n" + 
				"    <ProxyEndpoints>\r\n" + 
				"        <ProxyEndpoint>default</ProxyEndpoint>\r\n" + 
				"        <ProxyEndpoint>public-1</ProxyEndpoint>\r\n" + 
				"        <ProxyEndpoint>public-2</ProxyEndpoint>\r\n" + 
				"    </ProxyEndpoints>\r\n" + 
				"</APIProxy>"

		);
		assertEquals(0, issues.size());
	}	
		
	@Test
	public void test_configured_ko() throws Exception {
		check.setMaxAllowedEndpoints(3);
		Collection<Issue> issues = getIssues(check, "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<APIProxy revision=\"1\" name=\"myAPI\">\r\n" + 
				"    <ProxyEndpoints>\r\n" + 
				"        <ProxyEndpoint>default</ProxyEndpoint>\r\n" + 
				"        <ProxyEndpoint>public-1</ProxyEndpoint>\r\n" + 
				"        <ProxyEndpoint>public-2</ProxyEndpoint>\r\n" + 
				"        <ProxyEndpoint>public-3</ProxyEndpoint>\r\n" + 
				"    </ProxyEndpoints>\r\n" + 
				"</APIProxy>"

		);
		assertEquals(1, issues.size());
	}	
		

}