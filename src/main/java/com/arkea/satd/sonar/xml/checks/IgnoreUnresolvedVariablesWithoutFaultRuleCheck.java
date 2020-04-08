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

import java.util.Arrays;
import java.util.List;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.sonar.check.Rule;
import org.sonarsource.analyzer.commons.xml.XmlFile;
import org.sonarsource.analyzer.commons.xml.checks.SonarXmlCheck;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.arkea.satd.sonar.xml.BundleRecorder;

/**
 * Use of IgnoreUnresolvedVariables without the use of FaultRules may lead to unexpected errors.
 * Warning : the policies "AssignMessage", "BasicAuthentication", "RaiseFault" assume that the default value is true if the tag is not present.
 * Code : BN008
 * @author Nicolas Tisserand
 */
@Rule(key = "IgnoreUnresolvedVariablesWithoutFaultRuleCheck")
public class IgnoreUnresolvedVariablesWithoutFaultRuleCheck extends SonarXmlCheck {

	@Override
	public void scanFile(XmlFile xmlFile) {
		
	    Document document = xmlFile.getDocument();
	    if (document.getDocumentElement() != null) {
	    	String rootNodeName = document.getDocumentElement().getNodeName();
		    XPathFactory xPathfactory = XPathFactory.newInstance();
		    XPath xpath = xPathfactory.newXPath();
		    Node errorLocation = document.getDocumentElement();
		    
		    try {
				Node ignoreUnresolvedVariablesNode = (Node)xpath.evaluate("//IgnoreUnresolvedVariables[text()='true']", document, XPathConstants.NODE);
				errorLocation = ignoreUnresolvedVariablesNode!=null ? ignoreUnresolvedVariablesNode : document;
				
				boolean isIgnoreUnresolvedVariablesEnabled = ignoreUnresolvedVariablesNode!=null;
				if(!isIgnoreUnresolvedVariablesEnabled) {
					// Be careful, the default value of <IgnoreUnresolvedVariables> depends on the policy type !!
					// So if the tag is absent, it could be either "true" or "false"
					// These policies assume that no tag means "true". Ugly?
					List<String> defaultAtTrue = Arrays.asList("AssignMessage", "BasicAuthentication", "RaiseFault");
					isIgnoreUnresolvedVariablesEnabled = defaultAtTrue.contains(rootNodeName);
				}
				
		    	if(isIgnoreUnresolvedVariablesEnabled) {
		    		
		    		String policyName = (String)xpath.evaluate("//@name", document, XPathConstants.STRING);
		    		
			    	// Search for a faultRule or a defaultFaultRule in the endpoint where this policy is attached to
		    		List<XmlFile> endpointsList = BundleRecorder.searchByStepName(policyName);
		    		
		    		int sumOfFaultRules = 0;
		    		for(XmlFile currentXmlFile : endpointsList) {
		    			NodeList faultRuleNodeList = (NodeList)xpath.evaluate("//*[(name()='FaultRule' or name()='DefaultFaultRule')]", currentXmlFile.getDocument(), XPathConstants.NODESET);
		    			sumOfFaultRules += faultRuleNodeList.getLength();
		    		}
		    		
	    			// Report an issue if there is no FaultRule nor DefaultFaultRule
	    			if(sumOfFaultRules==0) {
	    				reportIssue(errorLocation, "Use of IgnoreUnresolvedVariables without the use of FaultRules may lead to unexpected errors.");
	    			}
		    	}

			} catch (XPathExpressionException e) {
				// Nothing to do
			}
	    }
		    
	}
	
}
