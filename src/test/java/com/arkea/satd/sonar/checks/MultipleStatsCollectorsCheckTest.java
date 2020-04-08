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
import org.sonarsource.analyzer.commons.xml.XmlFile;
import org.sonarsource.analyzer.commons.xml.checks.SonarXmlCheck;

import com.arkea.satd.sonar.xml.BundleRecorder;
import com.arkea.satd.sonar.xml.checks.MultipleStatsCollectorsCheck;

public class MultipleStatsCollectorsCheckTest extends AbstractCheckTester {

	private SonarXmlCheck check = new MultipleStatsCollectorsCheck();

	@Test
	public void test_duplicated_ok() throws Exception {
		
		// Fake StatisticsCollector file
		XmlFile policyXml1 = createTempFile("policy1.xml", "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<StatisticsCollector name=\"__collect-statistics-request__\">\r\n" + 
				"	<Statistics>\r\n" + 
				"		<Statistic name=\"something\" ref=\"anyVariable\" type=\"String\"/>\r\n" +
				"	</Statistics>\r\n" +
				"</StatisticsCollector>"
				);
		BundleRecorder.clear();
		BundleRecorder.storeFile(policyXml1);

		XmlFile policyXml2 = createTempFile("policy2.xml", "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<StatisticsCollector name=\"__collect-statistics-request2__\">\r\n" + 
				"	<Statistics>\r\n" + 
				"		<Statistic name=\"somethingElse\" ref=\"otherVariable\" type=\"String\"/>\r\n" +
				"	</Statistics>\r\n" +
				"</StatisticsCollector>"
				);
		BundleRecorder.storeFile(policyXml2);
		
		Collection<Issue> issues = getIssues(check, policyXml2);
		assertEquals(0, issues.size());
	}

	@Test
	public void test_duplicated_ko() throws Exception {
		
		// Fake StatisticsCollector file
		XmlFile policyXml1 = createTempFile("policy1.xml", "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<StatisticsCollector name=\"__collect-statistics-request__\">\r\n" + 
				"	<Statistics>\r\n" + 
				"		<Statistic name=\"something\" ref=\"anyVariable\" type=\"String\"/>\r\n" +
				"	</Statistics>\r\n" +
				"</StatisticsCollector>"
				);
		BundleRecorder.clear();
		BundleRecorder.storeFile(policyXml1);

		XmlFile policyXml2 = createTempFile("policy2.xml", "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<StatisticsCollector name=\"__collect-statistics-request2__\">\r\n" + 
				"	<Statistics>\r\n" + 
				"		<Statistic name=\"something\" ref=\"anyVariable\" type=\"String\"/>\r\n" +
				"	</Statistics>\r\n" +
				"</StatisticsCollector>"
				);
		BundleRecorder.storeFile(policyXml2);

		Collection<Issue> issues = getIssues(check, policyXml2);
		assertEquals(1, issues.size());
	}


	@Test
	public void test_with_conditions_ok() throws Exception {

		BundleRecorder.clear();

		// Fake ProxyEndpoint file
		XmlFile proxyEndpointXML = createTempFile("proxyEndpoint.xml", "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<ProxyEndpoint name=\"default\">\r\n" + 
				"    <Description/>\r\n" + 
				"    <PreFlow name=\"PreFlow\">\r\n" + 
				"        <Request>\r\n" + 
				"            <Step>\r\n" + 
				"                <Name>__collect-statistics-request__</Name>\r\n" + 
				"                <Condition>anyCondition</Condition>\r\n" + 
				"            </Step>\r\n" + 
				"            <Step>\r\n" + 
				"                <Name>__collect-statistics-request2__</Name>\r\n" + 
				"                <Condition>anyOtherCondition</Condition>\r\n" + 
				"            </Step>\r\n" + 
				"        </Request>\r\n" + 
				"        <Response/>\r\n" + 
				"    </PreFlow>\r\n" +  
				"</ProxyEndpoint>" 
				);
		BundleRecorder.storeFile(proxyEndpointXML);

		
		// Fake StatisticsCollector file
		XmlFile policyXml1 = createTempFile("policy1.xml", "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<StatisticsCollector name=\"__collect-statistics-request__\">\r\n" + 
				"	<Statistics>\r\n" + 
				"		<Statistic name=\"something\" ref=\"anyVariable\" type=\"String\"/>\r\n" +
				"	</Statistics>\r\n" +
				"</StatisticsCollector>"
				);
		BundleRecorder.storeFile(policyXml1);

		XmlFile policyXml2 = createTempFile("policy2.xml", "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<StatisticsCollector name=\"__collect-statistics-request2__\">\r\n" + 
				"	<Statistics>\r\n" + 
				"		<Statistic name=\"somethingElse\" ref=\"anyVariable\" type=\"String\"/>\r\n" +
				"	</Statistics>\r\n" +
				"</StatisticsCollector>"
				);
		BundleRecorder.storeFile(policyXml2);

		Collection<Issue> issues = getIssues(check, policyXml2);
		assertEquals(0, issues.size());
	}

	
	@Test
	public void test_with_conditions_ko1() throws Exception {

		BundleRecorder.clear();

		// Fake ProxyEndpoint file
		XmlFile proxyEndpointXML = createTempFile("proxyEndpoint.xml", "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<ProxyEndpoint name=\"default\">\r\n" + 
				"    <Description/>\r\n" + 
				"    <PreFlow name=\"PreFlow\">\r\n" + 
				"        <Request>\r\n" + 
				"            <Step>\r\n" + 
				"                <Name>__collect-statistics-request__</Name>\r\n" + 
				"                <Condition>anyCondition</Condition>\r\n" + 
				"            </Step>\r\n" + 
				"            <Step>\r\n" + 
				"                <Name>__collect-statistics-request2__</Name>\r\n" + 
				"            </Step>\r\n" + 
				"        </Request>\r\n" + 
				"        <Response/>\r\n" + 
				"    </PreFlow>\r\n" +  
				"</ProxyEndpoint>" 
				);
		BundleRecorder.storeFile(proxyEndpointXML);

		
		// Fake StatisticsCollector file
		XmlFile policyXml1 = createTempFile("policy1.xml", "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<StatisticsCollector name=\"__collect-statistics-request__\">\r\n" + 
				"	<Statistics>\r\n" + 
				"		<Statistic name=\"something\" ref=\"anyVariable\" type=\"String\"/>\r\n" +
				"	</Statistics>\r\n" +
				"</StatisticsCollector>"
				);
		BundleRecorder.storeFile(policyXml1);

		XmlFile policyXml2 = createTempFile("policy2.xml", "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<StatisticsCollector name=\"__collect-statistics-request2__\">\r\n" + 
				"	<Statistics>\r\n" + 
				"		<Statistic name=\"somethingElse\" ref=\"anyVariable\" type=\"String\"/>\r\n" +
				"	</Statistics>\r\n" +
				"</StatisticsCollector>"
				);
		BundleRecorder.storeFile(policyXml2);

		Collection<Issue> issues = getIssues(check, policyXml2);
		assertEquals(3, issues.size());
	}
	
	@Test
	public void test_with_conditions_ko2() throws Exception {

		BundleRecorder.clear();

		// Fake ProxyEndpoint file
		XmlFile proxyEndpointXML = createTempFile("proxyEndpoint.xml", "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<ProxyEndpoint name=\"default\">\r\n" + 
				"    <Description/>\r\n" + 
				"    <PreFlow name=\"PreFlow\">\r\n" + 
				"        <Request>\r\n" + 
				"            <Step>\r\n" + 
				"                <Name>__collect-statistics-request__</Name>\r\n" + 
				"            </Step>\r\n" + 
				"            <Step>\r\n" + 
				"                <Name>__collect-statistics-request__</Name>\r\n" + 
				"                <Condition>anyCondition</Condition>\r\n" + 
				"            </Step>\r\n" + 
				"        </Request>\r\n" + 
				"        <Response/>\r\n" + 
				"    </PreFlow>\r\n" +  
				"</ProxyEndpoint>" 
				);
		BundleRecorder.storeFile(proxyEndpointXML);

		
		// Fake StatisticsCollector file
		XmlFile policyXml1 = createTempFile("policy1.xml", "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<StatisticsCollector name=\"__collect-statistics-request__\">\r\n" + 
				"	<Statistics>\r\n" + 
				"		<Statistic name=\"something\" ref=\"anyVariable\" type=\"String\"/>\r\n" +
				"	</Statistics>\r\n" +
				"</StatisticsCollector>"
				);
		BundleRecorder.storeFile(policyXml1);

		Collection<Issue> issues = getIssues(check, policyXml1);
		assertEquals(3, issues.size());
	}
	

	@Test
	public void test_in_different_flows_without_conditions_ok() throws Exception {

		BundleRecorder.clear();

		// Fake ProxyEndpoint file
		XmlFile proxyEndpointXML = createTempFile("proxyEndpoint.xml", "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<ProxyEndpoint name=\"default\">\r\n" + 
				"    <Description/>\r\n" + 
				"    <Flows>\r\n" + 
				"	    <Flow name=\"Flow_1\">\r\n" + 
				"	        <Condition>flow_1_Condition</Condition>\r\n" + 
				"	        <Request>\r\n" + 
				"	            <Step>\r\n" + 
				"	                <Name>__collect-statistics-request__</Name>\r\n" + 
				"	            </Step>\r\n" + 
				"	        </Request>\r\n" + 
				"	        <Response/>\r\n" + 
				"	    </Flow>\r\n" +  
				"	    <Flow name=\"Flow_2\">\r\n" + 
				"	        <Condition>flow_2_Condition</Condition>\r\n" + 
				"	        <Request>\r\n" + 
				"	            <Step>\r\n" + 
				"	                <Name>__collect-statistics-request__</Name>\r\n" + 
				"	            </Step>\r\n" + 
				"	        </Request>\r\n" + 
				"	        <Response/>\r\n" + 
				"	    </Flow>\r\n" +  
				"    </Flows>\r\n" +  
				"</ProxyEndpoint>" 
				);
		BundleRecorder.storeFile(proxyEndpointXML);

		
		// Fake StatisticsCollector file
		XmlFile policyXml1 = createTempFile("policy1.xml", "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<StatisticsCollector name=\"__collect-statistics-request__\">\r\n" + 
				"	<Statistics>\r\n" + 
				"		<Statistic name=\"something\" ref=\"anyVariable\" type=\"String\"/>\r\n" +
				"	</Statistics>\r\n" +
				"</StatisticsCollector>"
				);
		BundleRecorder.storeFile(policyXml1);

		Collection<Issue> issues = getIssues(check, policyXml1);
		assertEquals(0, issues.size());
	}
	
	@Test
	public void test_in_different_flows_without_conditions_ko1() throws Exception {

		BundleRecorder.clear();

		// Fake ProxyEndpoint file
		XmlFile proxyEndpointXML = createTempFile("proxyEndpoint.xml", "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<ProxyEndpoint name=\"default\">\r\n" + 
				"    <Description/>\r\n" + 
				"    <PreFlow name=\"preFlow\">\r\n" + 
				"        <Request>\r\n" + 
				"            <Step>\r\n" + 
				"                <Name>__collect-statistics-request__</Name>\r\n" + 
				"            </Step>\r\n" + 
				"        </Request>\r\n" + 
				"        <Response/>\r\n" + 
				"    </PreFlow>\r\n" +  
				"    <Flows>\r\n" +  
				"	    <Flow name=\"Flow_2\">\r\n" + 
				"	        <Condition>flow_2_Condition</Condition>\r\n" + 
				"	        <Request>\r\n" + 
				"	            <Step>\r\n" + 
				"	                <Name>__collect-statistics-request__</Name>\r\n" + 
				"	            </Step>\r\n" + 
				"	        </Request>\r\n" + 
				"	        <Response/>\r\n" + 
				"	    </Flow>\r\n" +  
				"    </Flows>\r\n" +  
				"</ProxyEndpoint>" 
				);
		BundleRecorder.storeFile(proxyEndpointXML);

		// Fake StatisticsCollector file
		XmlFile policyXml1 = createTempFile("policy1.xml", "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<StatisticsCollector name=\"__collect-statistics-request__\">\r\n" + 
				"	<Statistics>\r\n" + 
				"		<Statistic name=\"something\" ref=\"anyVariable\" type=\"String\"/>\r\n" +
				"	</Statistics>\r\n" +
				"</StatisticsCollector>"
				);
		BundleRecorder.storeFile(policyXml1);

		Collection<Issue> issues = getIssues(check, policyXml1);
		assertEquals(3, issues.size());
	}

	@Test
	public void test_in_different_flows_without_conditions_ko2() throws Exception {

		BundleRecorder.clear();

		// Fake ProxyEndpoint file
		XmlFile proxyEndpointXML = createTempFile("proxyEndpoint.xml", "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<ProxyEndpoint name=\"default\">\r\n" + 
				"    <Description/>\r\n" + 
				"    <Flows>\r\n" +  
				"	    <Flow name=\"Flow_2\">\r\n" + 
				"	        <Condition>flow_2_Condition</Condition>\r\n" + 
				"	        <Request>\r\n" + 
				"	            <Step>\r\n" + 
				"	                <Name>__collect-statistics-request__</Name>\r\n" + 
				"	            </Step>\r\n" + 
				"	        </Request>\r\n" + 
				"	        <Response/>\r\n" + 
				"	    </Flow>\r\n" +  
				"    </Flows>\r\n" +  
				"    <PostFlow>\r\n" + 
				"        <Request>\r\n" + 
				"            <Step>\r\n" + 
				"                <Name>__collect-statistics-request__</Name>\r\n" + 
				"            </Step>\r\n" + 
				"        </Request>\r\n" + 
				"        <Response/>\r\n" + 
				"    </PostFlow>\r\n" +  
				"</ProxyEndpoint>" 
				);
		BundleRecorder.storeFile(proxyEndpointXML);

		// Fake StatisticsCollector file
		XmlFile policyXml1 = createTempFile("policy1.xml", "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<StatisticsCollector name=\"__collect-statistics-request__\">\r\n" + 
				"	<Statistics>\r\n" + 
				"		<Statistic name=\"something\" ref=\"anyVariable\" type=\"String\"/>\r\n" +
				"	</Statistics>\r\n" +
				"</StatisticsCollector>"
				);
		BundleRecorder.storeFile(policyXml1);

		Collection<Issue> issues = getIssues(check, policyXml1);
		assertEquals(3, issues.size());
	}

	@Test
	public void test_duplicated_with_conditions_ko1() throws Exception {

		BundleRecorder.clear();

		// Fake ProxyEndpoint file
		XmlFile proxyEndpointXML = createTempFile("proxyEndpoint.xml", "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<ProxyEndpoint name=\"default\">\r\n" + 
				"    <Description/>\r\n" + 
				"    <PreFlow name=\"PreFlow\">\r\n" + 
				"        <Request>\r\n" + 
				"            <Step>\r\n" + 
				"                <Name>__collect-statistics-request__</Name>\r\n" + 
				"                <Condition>anyCondition</Condition>\r\n" + 
				"            </Step>\r\n" + 
				"            <Step>\r\n" + 
				"                <Name>__collect-statistics-request2__</Name>\r\n" + 
				"                <Condition>anyOtherCondition</Condition>\r\n" + 
				"            </Step>\r\n" + 
				"        </Request>\r\n" + 
				"        <Response/>\r\n" + 
				"    </PreFlow>\r\n" +  
				"</ProxyEndpoint>" 
				);
		BundleRecorder.storeFile(proxyEndpointXML);

		
		// Fake StatisticsCollector file
		XmlFile policyXml1 = createTempFile("policy1.xml", "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<StatisticsCollector name=\"__collect-statistics-request__\">\r\n" + 
				"	<Statistics>\r\n" + 
				"		<Statistic name=\"something\" ref=\"anyVariable\" type=\"String\"/>\r\n" +
				"	</Statistics>\r\n" +
				"</StatisticsCollector>"
				);
		BundleRecorder.storeFile(policyXml1);

		XmlFile policyXml2 = createTempFile("policy2.xml", "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<StatisticsCollector name=\"__collect-statistics-request2__\">\r\n" + 
				"	<Statistics>\r\n" + 
				"		<Statistic name=\"something\" ref=\"anyVariable\" type=\"String\"/>\r\n" +
				"	</Statistics>\r\n" +
				"</StatisticsCollector>"
				);
		BundleRecorder.storeFile(policyXml2);

		Collection<Issue> issues = getIssues(check, policyXml2);
		assertEquals(1, issues.size());
	}
	
	@Test
	public void test_duplicated_with_conditions_ko2() throws Exception {

		BundleRecorder.clear();

		// Fake ProxyEndpoint file
		XmlFile proxyEndpointXML = createTempFile("proxyEndpoint.xml", "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<ProxyEndpoint name=\"default\">\r\n" + 
				"    <Description/>\r\n" + 
				"    <PreFlow name=\"PreFlow\">\r\n" + 
				"        <Request>\r\n" + 
				"            <Step>\r\n" + 
				"                <Name>__collect-statistics-request__</Name>\r\n" + 
				"                <Condition>anyCondition</Condition>\r\n" + 
				"            </Step>\r\n" + 
				"            <Step>\r\n" + 
				"                <Name>__collect-statistics-request2__</Name>\r\n" + 
				"            </Step>\r\n" + 
				"        </Request>\r\n" + 
				"        <Response/>\r\n" + 
				"    </PreFlow>\r\n" +  
				"</ProxyEndpoint>" 
				);
		BundleRecorder.storeFile(proxyEndpointXML);

		
		// Fake StatisticsCollector file
		XmlFile policyXml1 = createTempFile("policy1.xml", "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<StatisticsCollector name=\"__collect-statistics-request__\">\r\n" + 
				"	<Statistics>\r\n" + 
				"		<Statistic name=\"something\" ref=\"anyVariable\" type=\"String\"/>\r\n" +
				"	</Statistics>\r\n" +
				"</StatisticsCollector>"
				);
		BundleRecorder.storeFile(policyXml1);

		XmlFile policyXml2 = createTempFile("policy2.xml", "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<StatisticsCollector name=\"__collect-statistics-request2__\">\r\n" + 
				"	<Statistics>\r\n" + 
				"		<Statistic name=\"something\" ref=\"anyVariable\" type=\"String\"/>\r\n" +
				"	</Statistics>\r\n" +
				"</StatisticsCollector>"
				);
		BundleRecorder.storeFile(policyXml2);

		Collection<Issue> issues = getIssues(check, policyXml2);
		assertEquals(4, issues.size());
	}

	@Test
	public void test_faultrule_with_conditions_ok() throws Exception {

		BundleRecorder.clear();

		// Fake ProxyEndpoint file
		XmlFile proxyEndpointXML = createTempFile("proxyEndpoint.xml", "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<ProxyEndpoint name=\"default\">\r\n" + 
				"    <Description/>\r\n" + 
				"    <Flows>\r\n" +  
				"	    <Flow name=\"PreFlow\">\r\n" + 
				"	        <Request>\r\n" + 
				" 	           <Step>\r\n" + 
				"                <Name>__collect-statistics-request__</Name>\r\n" + 
				"                <Condition>anyCondition</Condition>\r\n" + 
				"	            </Step>\r\n" + 
				"	        </Request>\r\n" + 
				"        <Response/>\r\n" + 
				"	    </Flow>\r\n" +  
				"    </Flows>\r\n" +  
				"    <FaultRules>\r\n" +  
				"	    <FaultRule name=\"baderror\">\r\n" +  
				"		    <Condition>anyCondition</Condition>\r\n" +  
				" 	        <Step>\r\n" + 
				"              <Name>__collect-statistics-request__</Name>\r\n" + 
				"	        </Step>\r\n" + 
				"	    </FaultRule>\r\n" +  
				"    </FaultRules>\r\n" +  
				"</ProxyEndpoint>" 
				);
		BundleRecorder.storeFile(proxyEndpointXML);

		
		// Fake StatisticsCollector file
		XmlFile policyXml1 = createTempFile("policy1.xml", "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<StatisticsCollector name=\"__collect-statistics-request__\">\r\n" + 
				"	<Statistics>\r\n" + 
				"		<Statistic name=\"something\" ref=\"anyVariable\" type=\"String\"/>\r\n" +
				"	</Statistics>\r\n" +
				"</StatisticsCollector>"
				);
		BundleRecorder.storeFile(policyXml1);

		Collection<Issue> issues = getIssues(check, policyXml1);
		assertEquals(0, issues.size());
	}

	@Test
	public void test_faultrule_with_conditions_ko() throws Exception {

		BundleRecorder.clear();

		// Fake ProxyEndpoint file
		XmlFile proxyEndpointXML = createTempFile("proxyEndpoint.xml", "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<ProxyEndpoint name=\"default\">\r\n" + 
				"    <Description/>\r\n" + 
				"    <FaultRules>\r\n" +  
				"	    <FaultRule name=\"baderror\">\r\n" +  
				"		    <Condition>anyCondition</Condition>\r\n" +  
				" 	        <Step>\r\n" + 
				"              <Name>__collect-statistics-request__</Name>\r\n" + 
				"	        </Step>\r\n" + 
				" 	        <Step>\r\n" + 
				"             <Name>__collect-statistics-request__</Name>\r\n" + 
				"             <Condition>anyCondition</Condition>\r\n" + 
				"	        </Step>\r\n" + 
				"	    </FaultRule>\r\n" +  
				"    </FaultRules>\r\n" +  
				"</ProxyEndpoint>" 
				);
		BundleRecorder.storeFile(proxyEndpointXML);

		
		// Fake StatisticsCollector file
		XmlFile policyXml1 = createTempFile("policy1.xml", "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<StatisticsCollector name=\"__collect-statistics-request__\">\r\n" + 
				"	<Statistics>\r\n" + 
				"		<Statistic name=\"something\" ref=\"anyVariable\" type=\"String\"/>\r\n" +
				"	</Statistics>\r\n" +
				"</StatisticsCollector>"
				);
		BundleRecorder.storeFile(policyXml1);

		Collection<Issue> issues = getIssues(check, policyXml1);
		assertEquals(3, issues.size());
	}

	@Test
	public void test_defautfaultrule_with_conditions_ok() throws Exception {

		BundleRecorder.clear();

		// Fake ProxyEndpoint file
		XmlFile proxyEndpointXML = createTempFile("proxyEndpoint.xml", "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<ProxyEndpoint name=\"default\">\r\n" + 
				"    <Description/>\r\n" + 
				"    <FaultRules>\r\n" +  
				"	    <FaultRule name=\"baderror\">\r\n" +  
				"		    <Condition>anyCondition</Condition>\r\n" +  
				" 	        <Step>\r\n" + 
				"              <Name>__collect-statistics-request__</Name>\r\n" + 
				"	        </Step>\r\n" + 
				"	    </FaultRule>\r\n" +  
				"    </FaultRules>\r\n" +
				"    <DefaultFaultRule>\r\n" + 
				"       <Step>\r\n" + 
				"          <Name>__collect-statistics-request__</Name>\r\n" + 
				"        </Step>\r\n" + 
				"    </DefaultFaultRule>\r\n" + 
				"</ProxyEndpoint>" 
				);
		BundleRecorder.storeFile(proxyEndpointXML);

		
		// Fake StatisticsCollector file
		XmlFile policyXml1 = createTempFile("policy1.xml", "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<StatisticsCollector name=\"__collect-statistics-request__\">\r\n" + 
				"	<Statistics>\r\n" + 
				"		<Statistic name=\"something\" ref=\"anyVariable\" type=\"String\"/>\r\n" +
				"	</Statistics>\r\n" +
				"</StatisticsCollector>"
				);
		BundleRecorder.storeFile(policyXml1);

		Collection<Issue> issues = getIssues(check, policyXml1);
		assertEquals(0, issues.size());
	}
	

	@Test
	public void test_defautfaultrule_with_conditions_ko() throws Exception {

		BundleRecorder.clear();

		// Fake ProxyEndpoint file
		XmlFile proxyEndpointXML = createTempFile("proxyEndpoint.xml", "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<ProxyEndpoint name=\"default\">\r\n" + 
				"    <Description/>\r\n" + 
				"    <DefaultFaultRule>\r\n" + 
				"       <Step>\r\n" + 
				"          <Name>__collect-statistics-request__</Name>\r\n" + 
				"        </Step>\r\n" + 
				"        <Step>\r\n" + 
				"           <Name>__collect-statistics-request__</Name>\r\n" + 
				"			<Condition>anyCondition</Condition>\r\n" +  
				"        </Step>\r\n" + 
				"    </DefaultFaultRule>\r\n" + 
				"</ProxyEndpoint>" 
				);
		BundleRecorder.storeFile(proxyEndpointXML);

		
		// Fake StatisticsCollector file
		XmlFile policyXml1 = createTempFile("policy1.xml", "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<StatisticsCollector name=\"__collect-statistics-request__\">\r\n" + 
				"	<Statistics>\r\n" + 
				"		<Statistic name=\"something\" ref=\"anyVariable\" type=\"String\"/>\r\n" +
				"	</Statistics>\r\n" +
				"</StatisticsCollector>"
				);
		BundleRecorder.storeFile(policyXml1);

		Collection<Issue> issues = getIssues(check, policyXml1);
		assertEquals(3, issues.size());
	}

}