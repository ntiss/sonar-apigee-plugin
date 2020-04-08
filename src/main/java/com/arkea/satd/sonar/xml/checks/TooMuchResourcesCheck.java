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
import org.w3c.dom.Node;


/**
 * Check number of resource callouts present in the bundle.
 * A high number of resource callouts is indicative of underutilizing out of the box Apigee policies or over orchestration in the API tier.
 * Code : BN007
 * @author Nicolas Tisserand
 */
@Rule(key = "TooMuchResourcesCheck")
public class TooMuchResourcesCheck extends SonarXmlCheck {

	@RuleProperty(type = "INTEGER",
		    defaultValue = "20",
		    description = "Maximum resources count allowed in a proxy")
	protected int maxAllowedResources = 20;
	
	public int getMaxAllowedResources() {
		return maxAllowedResources;
	}

	public void setMaxAllowedResources(int maxAllowedResources) {
		this.maxAllowedResources = maxAllowedResources;
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
			    XPathExpression exprDisplayName = xpath.compile("count(/*/Resources/Resource)");
			    double resourcesCount = (double)exprDisplayName.evaluate(document, XPathConstants.NUMBER);
			    
		    	// If there are more than 'maxAllowedResources' resources, this is a violation.
		    	if(resourcesCount > maxAllowedResources) {
		    
		    		// Search for the <ProxyEndpoints> node (it's a better location to indicate the violation
		    		Node resourcesNode = (Node)xpath.evaluate("/*/Resources", document, XPathConstants.NODE);
		    		reportIssue(resourcesNode, "A high number of resource callouts is indicative of underutilizing out of the box Apigee policies.");
		    	}
			} catch (XPathExpressionException e) {
				// Nothing to do
			}	    	
	    	
    	}
	}

}
