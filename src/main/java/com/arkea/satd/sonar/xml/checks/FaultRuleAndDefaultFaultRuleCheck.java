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
 * DefaultFaultRule defined and FaultRule without condition.
 * Code : FR502
 * @author Nicolas Tisserand
 */
@Rule(key = "FaultRuleAndDefaultFaultRuleCheck")
public class FaultRuleAndDefaultFaultRuleCheck extends SonarXmlCheck {

	@Override
	public void scanFile(XmlFile xmlFile) {
		
	    Document document = xmlFile.getDocument();
	    if (document.getDocumentElement() != null) {
    	
		    XPathFactory xPathfactory = XPathFactory.newInstance();
		    XPath xpath = xPathfactory.newXPath();
		    
		    try {
		    	NodeList faultRuleNodeList = (NodeList)xpath.evaluate("//FaultRule", document, XPathConstants.NODESET);
				Node defaultFaultRuleNode = (Node)xpath.evaluate("//DefaultFaultRule", document, XPathConstants.NODE);
		    	
		    	if(defaultFaultRuleNode!=null && faultRuleNodeList!=null) {
		    		
			    	// Search for a faultRule without any condition
		    		for(int i=0; i < faultRuleNodeList.getLength(); i++) {
		    			
		    			Node currentFaultRule = faultRuleNodeList.item(i);
		    			
		    			// Check the condition
		    			String condition = (String)xpath.evaluate("Condition", currentFaultRule, XPathConstants.STRING);
		    			if(condition==null || condition.isEmpty() || "true".equals(condition)) {
		    				reportIssue(currentFaultRule, "DefaultFaultRule defined and FaultRule without condition.");
		    				reportIssue(defaultFaultRuleNode, "DefaultFaultRule defined and FaultRule without condition.");
		    				break;
		    			}
		    		}
		    	}

			} catch (XPathExpressionException e) {
				// Nothing to do
			}
	    }
		    
	}
	
}
