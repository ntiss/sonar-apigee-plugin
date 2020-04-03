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

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.sonar.api.batch.fs.InputComponent;
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
 * Warn on duplicate policies when no conditions are present or conditions are duplicates.
 * Only one StatisticsCollector Policy will be executed.
 * Therefore, if you include multiple StatisticsCollector Policies then you must have conditions on each one.
 * Code : BN009
 * @author Nicolas Tisserand
 */
@Rule(key = "MultipleStatsCollectorsCheck")
public class MultipleStatsCollectorsCheck extends SonarXmlCheck {

	@Override
	public void scanFile(XmlFile xmlFile) {
		
	    Document document = xmlFile.getDocument();
	    if (document.getDocumentElement() != null) {
	    	
	    	String rootNodeName = document.getDocumentElement().getNodeName();
	    	if("StatisticsCollector".equals(rootNodeName) ) {

			    XPathFactory xPathfactory = XPathFactory.newInstance();
			    XPath xpath = xPathfactory.newXPath();

	    		// Search for other collectors policies
	    		List<XmlFile> collectorsList = BundleRecorder.searchPoliciesByType("StatisticsCollector");
	    		Set<String> collectorsNamesSet = new HashSet<>();
	    						    			
			    try {
			    	
			    	// Select in one shot the name attribute and the Statistics node
			    	String currentPolicyAttrName = (String)xpath.evaluate("/*/@name", document, XPathConstants.STRING);
	    			Node statisticsNode = (Node)xpath.evaluate("//Statistics", document, XPathConstants.NODE);
	    			
	    			//
	    			// Search for duplicates of this policy  (ie. same <Statistics> elements)
	    			//
	    			for(XmlFile otherPolicy : collectorsList) {
	    				
	    				Document otherDocument = otherPolicy.getDocument();
				    	String otherPolicyAttrName = (String)xpath.evaluate("/*/@name", otherDocument, XPathConstants.STRING);
				    	
				    	// Useful later
				    	collectorsNamesSet.add(otherPolicyAttrName);
				    	
	    				// Exclude the current policy itself, of course.
	    				if(!currentPolicyAttrName.equals(otherPolicyAttrName)) {
	    					Node otherStatisticsNode = (Node)xpath.evaluate("//Statistics", otherDocument, XPathConstants.NODE);
	    					otherStatisticsNode.normalize();
	    					statisticsNode.normalize();
	    					
	    					// Check if the Statistics nodes are equivalent
	    					if(statisticsNode.isEqualNode(otherStatisticsNode)) {
	    						reportIssue(document.getDocumentElement(), "This policy is duplicated in the bundle.");
	    					}
	    				}
	    			}
	    			
	    			//
	    			// Now work based on steps
	    			//
	    			Map<Node, XmlFile> collectorsStepsMap = new HashMap<>();
	    			for(String collectorName : collectorsNamesSet) {
	    				collectorsStepsMap.putAll(BundleRecorder.searchStepsByName(collectorName));
	    			}
	    			
	    			// If there's more than one Step using a StatisticsCollector
	    			Set<Node> collectorsStepsSet = collectorsStepsMap.keySet();
	    			if(collectorsStepsSet.size() > 1) {
	    				
	    				// Flag used to trigger an issue only once at the policy level
	    				boolean hasIssueAtPolicyLevel = false;
	    				
	    				// Double iteration to compare steps between then
	    				for(Node stepI : collectorsStepsSet) {
	    					
	    					String stepIName = (String)xpath.evaluate("Name/text()", stepI, XPathConstants.STRING);
	    					String stepIParentNodeName = (String)xpath.evaluate("name(..)", stepI, XPathConstants.STRING);
	    					String stepIGrandParentNodeName = (String)xpath.evaluate("name(../..)", stepI, XPathConstants.STRING);
	    					boolean isIAttachedToFlow = "Flow".equals(stepIGrandParentNodeName);
	    					boolean isIAttachedToGlobalFlow = "PreFlow".equals(stepIGrandParentNodeName) || "PostFlow".equals(stepIGrandParentNodeName);
	    					boolean isIAttachedToFault = "FaultRule".equals(stepIParentNodeName);
	    					boolean isIAttachedToDefaultFault = "DefaultFaultRule".equals(stepIParentNodeName);
	    					String stepIFlowName = "";
	    					if(isIAttachedToFlow || isIAttachedToFault) {
	    						stepIFlowName = (String)xpath.evaluate("../../@name", stepI, XPathConstants.STRING);
	    					}
	    					String stepIBreadcrumb = stepIGrandParentNodeName + "/" + stepIParentNodeName + "@" + stepIFlowName + "/" + stepI.hashCode();
	    					
		    				for(Node stepJ : collectorsStepsSet) {
	
		    					String stepJParentNodeName = (String)xpath.evaluate("name(..)", stepJ, XPathConstants.STRING);
		    					String stepJGrandParentNodeName = (String)xpath.evaluate("name(../..)", stepJ, XPathConstants.STRING);
		    					boolean isJAttachedToFlow = "Flow".equals(stepJGrandParentNodeName);
		    					boolean isJAttachedToGlobalFlow = "PreFlow".equals(stepJGrandParentNodeName) || "PostFlow".equals(stepJGrandParentNodeName);
		    					boolean isJAttachedToFault = "FaultRule".equals(stepJParentNodeName);
		    					boolean isJAttachedToDefaultFault = "DefaultFaultRule".equals(stepJParentNodeName);
		    					String stepJFlowName = "";
		    					if(isIAttachedToFlow || isIAttachedToFault) {
		    						stepJFlowName = (String)xpath.evaluate("../../@name", stepJ, XPathConstants.STRING);
		    					}	
		    					String stepJBreadcrumb = stepJGrandParentNodeName + "/" + stepJParentNodeName + "@" + stepJFlowName + "/" + stepJ.hashCode();
		    					
		    					if(!stepIBreadcrumb.equals(stepJBreadcrumb)) {  // Exclude the comparison with itself
			    					// And now the big condition bunch...
			    					if ((isIAttachedToGlobalFlow   & isJAttachedToGlobalFlow )   ||                                 // Two policies are in the same global flow
			    						(isIAttachedToDefaultFault & isJAttachedToDefaultFault ) ||                                 // Two policies are in the same defaultFaultRule
			    						(isIAttachedToFlow         & isJAttachedToFlow  & stepIFlowName.equals(stepJFlowName) ) ||  // Two policies are in the same flow (exact name)
			    						(isIAttachedToFault        & isJAttachedToFault & stepIFlowName.equals(stepJFlowName) ) ||  // Two policies are in the same faultRule (exact name)
			    						(isIAttachedToFlow         & isJAttachedToGlobalFlow )   ||                                 // A policy in a Flow, the other in PreFlow or PostFlow
			    						(isIAttachedToGlobalFlow         & isJAttachedToFlow )                                      // A policy in PreFlow or PostFlow, the other in a Flow
			    						) {
			    						
			    						// Check that all steps have a non-null <Condition>
    									String conditionI = (String)xpath.evaluate("Condition/text()", stepI, XPathConstants.STRING);
    									boolean noConditionForStepI = conditionI==null || conditionI.isEmpty() || "true".equals(conditionI);
    									
    									String conditionJ = (String)xpath.evaluate("Condition/text()", stepJ, XPathConstants.STRING);
    									boolean noConditionForStepJ = conditionJ==null || conditionJ.isEmpty() || "true".equals(conditionJ);
    									
    									if(noConditionForStepI || noConditionForStepJ) {
    										
	    									// Report the issue at the policy level
	    									if(!hasIssueAtPolicyLevel && stepIName.equals(currentPolicyAttrName)) {
	    										reportIssue(document.getDocumentElement(), "This policy is attached to a step without a condition. If you have more than two Statistics Collector policies, only the last one in the flow will execute.  Include a condition to make sure the correct one executes.");
	    										// Don't trigger this issue multiple times
	    										hasIssueAtPolicyLevel = true;
	    									}
    										
    										// Report also the issue next to the StepI node
	    									final NewIssue issueStepI = ApigeeXmlSensor.getContext().newIssue();
	    									final XmlTextRange textRangeStepI = XmlFile.nodeLocation(stepI);
	    									
	    									XmlFile stepIXmlFile = collectorsStepsMap.get(stepI);
	    									NewIssueLocation locationStepI = issueStepI.newLocation()
	    											.on((InputComponent) stepIXmlFile.getInputFile())
	    											.at(stepIXmlFile.getInputFile().newRange(textRangeStepI.getStartLine(), textRangeStepI.getStartColumn(), textRangeStepI.getEndLine(), textRangeStepI.getEndColumn()))
	    											.message("This policy is attached to a step without a condition. If you have more than two Statistics Collector policies, only the last one in the flow will execute.  Include a condition to make sure the correct one executes.");

	    									issueStepI.at(locationStepI)
	    										.forRule(ruleKey())
	    										.save(); // Mandatory to "commit" the issue in the final report		
    						
    										// Don't report the issue  to the StepJ node
	    									// It will be done during the double loop iteration
	
    									}
			    					}

		    					}
		    					
		    				}	    					
	    				}
	    			}
			    
				} catch (XPathExpressionException e) {
					// Nothing to do
				}	    			
		    }
	    }
		    
	}
	
}
