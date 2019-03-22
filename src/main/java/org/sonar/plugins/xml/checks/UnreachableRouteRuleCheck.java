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
 * Only one RouteRule should be present without a condition
 * Code : PD002
 * @author Nicolas Tisserand
 */
@Rule(key = "UnreachableRouteRuleCheck")
public class UnreachableRouteRuleCheck extends SonarXmlCheck {

	@Override
	public void scanFile(XmlFile xmlFile) {
		
	    Document document = xmlFile.getDocument();
	    if (document.getDocumentElement() != null && "ProxyEndpoint".equals(document.getDocumentElement().getNodeName())) {

		    XPathFactory xPathfactory = XPathFactory.newInstance();
		    XPath xpath = xPathfactory.newXPath();
		    
		    try {
		    	// Select the RouteRule without Condition (or "true")
			    NodeList routeRuleList = (NodeList)xpath.evaluate("//RouteRule[not(Condition) or Condition/text()='true' or string-length(Condition/text())=0]", document, XPathConstants.NODESET);
		    	if(routeRuleList!=null && routeRuleList.getLength()>1) {
		    		for(int i=0; i<routeRuleList.getLength(); i++) {
		    			Node routeRuleNode = routeRuleList.item(i);

	    				// Create a violation if flow node is not the last one
		    			reportIssue(routeRuleNode, "Only one RouteRule should be present without a condition.");
		    		}
		    	}

			} catch (XPathExpressionException e) {
				// Nothing to do
			}			    	
	    }
	}

}
