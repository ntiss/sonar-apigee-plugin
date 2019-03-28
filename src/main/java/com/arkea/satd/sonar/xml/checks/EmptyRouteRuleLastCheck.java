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
 * Unreachable Route Rules - empty conditions go last
 * Check RouteRules in a ProxyEndpoint to ensure that empty condition is last.
 * Code : PD003
 * @author Nicolas Tisserand
 */
@Rule(key = "EmptyRouteRuleLastCheck")
public class EmptyRouteRuleLastCheck extends SonarXmlCheck {

	@Override
	public void scanFile(XmlFile xmlFile) {
		
	    Document document = xmlFile.getDocument();
    	// Search for last Flow of an ProxyEndpoint document
	    if (document.getDocumentElement() != null && "ProxyEndpoint".equals(document.getDocumentElement().getNodeName())) {
	    	
		    XPathFactory xPathfactory = XPathFactory.newInstance();
		    XPath xpath = xPathfactory.newXPath();
    		
			try {
				NodeList flowNodeList = (NodeList)xpath.evaluate("//RouteRule", document, XPathConstants.NODESET);
	    	
		    	if(flowNodeList!=null && flowNodeList.getLength() >= 2) {
		    		
		    		// Reversed loop
		    		for(int i=flowNodeList.getLength()-2; i>=0; i--) {
		    			Node routeRuleNode = flowNodeList.item(i);

		    			// Search Condition value
		    			Node condition = (Node)xpath.evaluate("Condition", routeRuleNode, XPathConstants.NODE);
		    			
		    			if(condition==null || condition.getTextContent().isEmpty() || "true".equals(condition.getTextContent())) {
		    				// Issue detected
		    				reportIssue(routeRuleNode, "Unreachable Route Rules - empty conditions go last");
		    			}
		    		}
		    	}
			} catch (XPathExpressionException e) {
				// Nothing to do
			}
	    	
	    }
	}

}
