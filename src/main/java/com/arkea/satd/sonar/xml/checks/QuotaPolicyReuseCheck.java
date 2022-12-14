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

import java.util.Map;
import java.util.Set;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.sonar.api.batch.sensor.issue.NewIssue;
import org.sonar.api.batch.sensor.issue.NewIssueLocation;
import org.sonar.check.Rule;
import org.sonarsource.analyzer.commons.xml.XmlFile;
import org.sonarsource.analyzer.commons.xml.XmlTextRange;
import org.sonarsource.analyzer.commons.xml.checks.SonarXmlCheck;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.arkea.satd.sonar.xml.ApigeeXmlSensor;
import com.arkea.satd.sonar.xml.BundleRecorder;

/**
 * When the same Quota policy is used more than once you must ensure that the conditions of execution are mutually exclusive 
 * or that you intend for a call to count more than once per message processed.",
 * Code : PO023
 * @author Nicolas Tisserand
 */
@Rule(key = "QuotaPolicyReuseCheck")
public class QuotaPolicyReuseCheck extends SonarXmlCheck {
	
	public QuotaPolicyReuseCheck() {
		super();
	}
	
	@Override
	public void scanFile(XmlFile xmlFile) {
	    Document document = xmlFile.getDocument();
	    if (document.getDocumentElement() != null) {
    	
	    	String rootNodeName = document.getDocumentElement().getNodeName();
	    	if("Quota".equals(rootNodeName) ) {
			    
			    try {
			    	// Select in one shot the name of the policy
				    XPathFactory xPathfactory = XPathFactory.newInstance();
				    XPath xpath = xPathfactory.newXPath();
			    	String currentPolicyAttrName = (String)xpath.evaluate("/Quota/@name", document, XPathConstants.STRING);
			    	
	    			Map<Node, XmlFile> quotaStepsMap = BundleRecorder.searchStepsByName(currentPolicyAttrName);

			    	if(quotaStepsMap.size()>1) {
			    		// Report the issue at the file level
			    		reportIssue(document.getDocumentElement(), "Quota Policy Reuse.");
			    		
						// Report also the issue next to the Step node
		    			Set<Node> quotaStepsSet = quotaStepsMap.keySet();
	    				
	    				for(Node step : quotaStepsSet) {
							final NewIssue issueStepI = ApigeeXmlSensor.getContext().newIssue();
							final XmlTextRange textRangeStepI = XmlFile.nodeLocation(step);
							
							XmlFile stepIXmlFile = quotaStepsMap.get(step);
							NewIssueLocation locationStepI = issueStepI.newLocation()
									.on(stepIXmlFile.getInputFile())
									.at(stepIXmlFile.getInputFile().newRange(textRangeStepI.getStartLine(), textRangeStepI.getStartColumn(), textRangeStepI.getEndLine(), textRangeStepI.getEndColumn()))
									.message("Quota Policy Reuse.");

							issueStepI.at(locationStepI)
								.forRule(ruleKey())
								.save(); // Mandatory to "commit" the issue in the final report		
	    				}
			    	}
			    	
				} catch (XPathExpressionException e) {
					// Nothing to do
				}
	    	}
	    }		
	}
	
}
