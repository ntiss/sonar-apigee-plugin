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
 * RouteRules must map to defined Targets
 * (Apigee UI already prevent this from happening)
 * Code : PD001
 * @author Nicolas Tisserand
 */
@Rule(key = "RouteRulesToTargetCheck")
public class RouteRulesToTargetCheck extends SonarXmlCheck {

	@Override
	public void scanFile(XmlFile xmlFile) {
		
	    Document document = xmlFile.getDocument();
	    if (document.getDocumentElement() != null && "ProxyEndpoint".equals(document.getDocumentElement().getNodeName())) {
    	
		    XPathFactory xPathfactory = XPathFactory.newInstance();
		    XPath xpath = xPathfactory.newXPath();
		    
		    try {
		    	// Select in one shot all the target endpoints
		    	NodeList targetList = (NodeList)xpath.evaluate("//TargetEndpoint", document, XPathConstants.NODESET);
		    	
	    		for(int i=0; i<targetList.getLength(); i++) {
	    			Node targetNode = targetList.item(i);
	    			String targetName = targetNode.getTextContent();

			    	// Verify that there is an existing target
			    	if(BundleRecorder.searchTargetEndpointByName(targetName) == null) {
	    				// Issue detected
		    			reportIssue(targetNode, "RouteRules should map to defined Targets.");
			    		
			    	}
	    		}
			} catch (XPathExpressionException e) {
				// Nothing to do
			}
	    }
	}
}
