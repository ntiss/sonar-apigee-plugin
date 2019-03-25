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
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.sonar.check.Rule;
import org.sonarsource.analyzer.commons.xml.XmlFile;
import org.sonarsource.analyzer.commons.xml.checks.SonarXmlCheck;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;


/**
 * Discourage the use of numerous target endpoints.
 * Code : TD501
 * @author Nicolas Tisserand
 */
@Rule(key = "TooMuchTargetEndpointsCheck")
public class TooMuchTargetEndpointsCheck extends SonarXmlCheck {

	@Override
	public void scanFile(XmlFile xmlFile) {
		
	    Document document = xmlFile.getDocument();
	    if (document.getDocumentElement() != null && "APIProxy".equals(document.getDocumentElement().getNodeName())) {

	    	
	    	// Search for targets definitions of an APIProxy document
		    XPathFactory xPathfactory = XPathFactory.newInstance();
		    XPath xpath = xPathfactory.newXPath();
		    
		    try {
			    XPathExpression exprDisplayName = xpath.compile("count(/APIProxy/TargetEndpoints/TargetEndpoint)");
			    double targetsCount = (double)exprDisplayName.evaluate(document, XPathConstants.NUMBER);
			    
		    	// If there are more than 5 TargetEndpoint, this is a violation.
		    	if(targetsCount > 5) {
		    		// Search for the <TargetEndpoints> node (it's a better location to indicate the violation
		    		NodeList targetEndpointsNodeList = document.getDocumentElement().getElementsByTagName("TargetEndpoints");
		    		if(targetEndpointsNodeList!=null && targetEndpointsNodeList.getLength()>0) {
		    			reportIssue(targetEndpointsNodeList.item(0), "Discourage the use of numerous target endpoints.");
		    		} else {
		    			reportIssue(document, "Discourage the use of numerous target endpoints.");
		    		}
		    	}
			} catch (XPathExpressionException e) {
				// Nothing to do
			}	    	
	    	
    	}
	}

}
