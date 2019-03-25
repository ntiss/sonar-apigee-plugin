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
package org.sonar.plugins.xml.checks;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.sonarsource.analyzer.commons.xml.XmlFile;
import org.sonarsource.analyzer.commons.xml.checks.SonarXmlCheck;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.arkea.satd.sonar.xml.ApigeeXmlSensor;


/**
 * Abstract class to factorize the check on the body content
 * @author Nicolas Tisserand
 */
public abstract class AbstractBodyCheck extends SonarXmlCheck {

	/**
	 * This method performs a check on the Condition tag that is applied to the concerned step 
	 * @param stepName name of the step to work on
	 * @param pattern the regex to check
	 */
	protected void checkConditionInStepOrParent(String stepName, String pattern) {

		XPathFactory xPathfactory = XPathFactory.newInstance();
	    XPath xpath = xPathfactory.newXPath();

	    // Search for the associated step in the full storage		    	
	    List<XmlFile> listProxiesEndpoint = BundleRecorder.searchByStepName(stepName);

	    Pattern ptrn = Pattern.compile(pattern);
		try {
		    
		    // Now check the Condition of the matching Steps
		    for(XmlFile currentXml : listProxiesEndpoint) {
		    	
				XPathExpression exprSteps = xpath.compile("//Step[Name[text() = '"+stepName+"']]");
				NodeList stepNodes = (NodeList)exprSteps.evaluate(currentXml.getDocument(), XPathConstants.NODESET);
				
				for(int i=0; i<stepNodes.getLength(); i++) {
					Node currentStep = stepNodes.item(i);
					
					String condition = (String)xpath.evaluate("Condition/text()", currentStep, XPathConstants.STRING);
					
					// Analyse the content of the condition
					Matcher matcher = ptrn.matcher(condition);	    
					boolean hasIssue = !matcher.find();
					
					// Check also on flow condition :
					// if the parent is a flow we might revert the decision if it has an appropriate condition
					if(hasIssue) {
						// Search the condition of the parent Node (Flow, but not PreFlow or PostFlow
						String flowCondition = (String)xpath.evaluate("../../../*[name() = 'Flow']/Condition/text()", currentStep, XPathConstants.STRING);						
						matcher = ptrn.matcher(flowCondition);	    
						hasIssue = !matcher.find();
					}
					
					// Finally : Create issue if needed
					if(hasIssue) {
						XmlIssue issue = new XmlIssue(getRuleKey(), currentXml.getLineForNode(currentStep), "An appropriate check for a message body was not found on the enclosing Step or Flow.");
						currentXml.addViolation(issue); // Useful for JUnit test
						ApigeeXmlSensor.saveIssue(ApigeeXmlSensor.getContext(), currentXml); // Mandatory to "commit" the issue in the final report
					}
				}
		    }
		} catch (XPathExpressionException e) {
			// Nothing to do
		}
	}
	
}
