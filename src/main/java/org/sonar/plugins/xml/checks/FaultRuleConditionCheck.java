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
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * No Condition on FaultRule.
 * Code : FR001
 * @author Nicolas Tisserand
 */
@Rule(key = "FaultRuleConditionCheck")
public class FaultRuleConditionCheck extends AbstractXmlCheck {

	@Override
	public void validate(XmlSourceCode xmlSourceCode) {
	    setWebSourceCode(xmlSourceCode);
	    
	    Document document = getWebSourceCode().getDocument(false);
	    if (document.getDocumentElement() != null) {
    	
		    XPathFactory xPathfactory = XPathFactory.newInstance();
		    XPath xpath = xPathfactory.newXPath();
		    
		    try {
		    	// Select the faultRule
		    	NodeList faultRuleNodeList = (NodeList)xpath.evaluate("//FaultRule", document, XPathConstants.NODESET);
		    	
		    	if(faultRuleNodeList!=null) {
		    		
		    		for(int i=0; i < faultRuleNodeList.getLength(); i++) {
		    			
		    			Node currentFaultRule = faultRuleNodeList.item(i);
		    			
		    			// Check the condition
		    			String condition = (String)xpath.evaluate("Condition", currentFaultRule, XPathConstants.STRING);
		    			
		    			if(condition==null || condition.isEmpty() || "true".equals(condition)) {
			    			createViolation(getWebSourceCode().getLineForNode(currentFaultRule), "FaultRule has no Condition or the Condition is empty.");		    				
		    			}
		    		}
		    	}
			} catch (XPathExpressionException e) {
				// Nothing to do
			}
	    }
		    
	}
	
}
