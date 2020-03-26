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
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;
import org.sonarsource.analyzer.commons.xml.XmlFile;
import org.sonarsource.analyzer.commons.xml.checks.SonarXmlCheck;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;


/**
 * Discourage the declaration of multiple proxy endpoints in a same proxy.
 * Code : PD501
 * @author Nicolas Tisserand
 */
@Rule(key = "TooMuchProxyEndpointsCheck")
public class TooMuchProxyEndpointsCheck extends SonarXmlCheck {

	@RuleProperty(type = "INTEGER",
		    defaultValue = "2",
		    description = "Maximum endpoints count allowed in a proxy")
		protected int maxAllowedEndpoints = 2;
		
	public int getMaxAllowedEndpoints() {
		return maxAllowedEndpoints;
	}

	public void setMaxAllowedEndpoints(int maxAllowedTargets) {
		this.maxAllowedEndpoints = maxAllowedTargets;
	}

	@Override
	public void scanFile(XmlFile xmlFile) {
		
	    Document document = xmlFile.getDocument();
	    if (document.getDocumentElement() != null && "APIProxy".equals(document.getDocumentElement().getNodeName())) {
	    	
	    	// Search for targets definitions of an APIProxy document
		    XPathFactory xPathfactory = XPathFactory.newInstance();
		    XPath xpath = xPathfactory.newXPath();
		    
		    try {
			    XPathExpression exprDisplayName = xpath.compile("count(/APIProxy/ProxyEndpoints/ProxyEndpoint)");
			    double proxiesCount = (double)exprDisplayName.evaluate(document, XPathConstants.NUMBER);
			    
		    	// If there are more than 'maxAllowedEndpoints' ProxyEndpoint, this is a violation.
		    	if(proxiesCount > maxAllowedEndpoints) {
		    
		    		// Search for the <ProxyEndpoints> node (it's a better location to indicate the violation
		    		NodeList proxyEndpointsNodeList = document.getDocumentElement().getElementsByTagName("ProxyEndpoints");
		    		if(proxyEndpointsNodeList!=null && proxyEndpointsNodeList.getLength()>0) {
		    			reportIssue(proxyEndpointsNodeList.item(0), "Discourage the declaration of multiple proxy endpoints in a same proxy.");
		    		} else {
		    			reportIssue(document, "Discourage the declaration of multiple proxy endpoints in a same proxy.");
		    		}	    		
		    	}
			} catch (XPathExpressionException e) {
				// Nothing to do
			}	    	
	    	
    	}
	}

}
