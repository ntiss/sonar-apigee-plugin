/*
 * Copyright 2017 Credit Mutuel ArkeahasNoCondition
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

/**
 * Flow without a condition must be last.
 * Code : FL501
 * @author Nicolas Tisserand
 */
@Rule(key = "UnreachableFlowCheck")
public class UnreachableFlowCheck extends SonarXmlCheck {

	@Override
	public void scanFile(XmlFile xmlFile) {
		
	    Document document = xmlFile.getDocument();
	    XPathFactory xPathfactory = XPathFactory.newInstance();
	    XPath xpath = xPathfactory.newXPath();
	    
	    try {
	    	// Select the Flow nodes
	    	NodeList flowNodeList = (NodeList)xpath.evaluate("/ProxyEndpoint/Flows/Flow", document, XPathConstants.NODESET);
	    	
    		for(int i=0; i<flowNodeList.getLength(); i++) {
    			Node flowNode = flowNodeList.item(i);

    			// Search Condition value
    			String cond = (String)xpath.evaluate("Condition", flowNode, XPathConstants.STRING);
				if(i < flowNodeList.getLength()-1 &&
					(cond==null || cond.isEmpty() || "true".equalsIgnoreCase(cond)) ) {

					// Create a violation if flow node is not the last one
					reportIssue(flowNode, "Flow without a condition should be last.");
    			}
    		}	    
		} catch (XPathExpressionException e) {
			// Nothing to do
		}
	}

}
