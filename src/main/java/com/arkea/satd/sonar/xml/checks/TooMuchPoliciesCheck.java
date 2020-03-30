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
 * Check number of policies present in the bundle.
 * Large bundles can be problematic in development and difficult to maintain.
 * Code : BN006
 * @author Nicolas Tisserand
 */
@Rule(key = "TooMuchPoliciesCheck")
public class TooMuchPoliciesCheck extends SonarXmlCheck {

	@RuleProperty(type = "INTEGER",
		    defaultValue = "20",
		    description = "Maximum policies count allowed in a proxy")
	protected int maxAllowedPolicies = 20;
	
	public int getMaxAllowedPolicies() {
		return maxAllowedPolicies;
	}

	public void setMaxAllowedPolicies(int maxAllowedPolicies) {
		this.maxAllowedPolicies = maxAllowedPolicies;
	}

	@Override
	public void scanFile(XmlFile xmlFile) {
		
	    Document document = xmlFile.getDocument();
	    if (document.getDocumentElement() != null && ("APIProxy".equals(document.getDocumentElement().getNodeName()) || 
				  									  "SharedFlowBundle".equals(document.getDocumentElement().getNodeName()))) {
	    	
	    	// Search for policies declaration of an APIProxy document
		    XPathFactory xPathfactory = XPathFactory.newInstance();
		    XPath xpath = xPathfactory.newXPath();
		    
		    try {
			    XPathExpression exprDisplayName = xpath.compile("count(/*/Policies/Policy)");
			    double proxiesCount = (double)exprDisplayName.evaluate(document, XPathConstants.NUMBER);
			    
		    	// If there are more than 'maxAllowedPolicies' policies, this is a violation.
		    	if(proxiesCount > maxAllowedPolicies) {
		    
		    		// Search for the <ProxyEndpoints> node (it's a better location to indicate the violation
		    		NodeList policiesNodeList = document.getDocumentElement().getElementsByTagName("Policies");
		    		if(policiesNodeList!=null && policiesNodeList.getLength()>0) {
		    			reportIssue(policiesNodeList.item(0), "Large bundles can be problematic in development and difficult to maintain.");
		    		} else {
		    			reportIssue(document, "Large bundles can be problematic in development and difficult to maintain.");
		    		}	    		
		    	}
			} catch (XPathExpressionException e) {
				// Nothing to do
			}	    	
	    	
    	}
	}

}
