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
import org.sonar.plugins.xml.checks.XmlSourceCode;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 * Only one unconditional flow will get executed. Error if more than one was detected.
 * Code : FL001
 * @author Nicolas Tisserand
 */
@Rule(key = "UnconditionalFlowCheck")
public class UnconditionalFlowCheck extends AbstractXmlCheck {

	@Override
	public void validate(XmlSourceCode xmlSourceCode) {
	    setWebSourceCode(xmlSourceCode);

	    Document document = getWebSourceCode().getDocument(false);
	    
	    XPathFactory xPathfactory = XPathFactory.newInstance();
	    XPath xpath = xPathfactory.newXPath();
	    
	    try {
	    	// Select the Flow nodes
	    	NodeList flowNodeList = (NodeList)xpath.evaluate("/ProxyEndpoint/Flows/Flow", document, XPathConstants.NODESET);
	    	
	    	int noConditionCount = 0;
    		for(int i=0; i<flowNodeList.getLength(); i++) {
    			Node flowNode = flowNodeList.item(i);

    			// Search Condition value
    			String cond = (String)xpath.evaluate("Condition", flowNode, XPathConstants.STRING);
				if(cond==null || cond.isEmpty() || "true".equalsIgnoreCase(cond)) {
					noConditionCount++;
					
    				// Create a violation for each flow node
	    			if(noConditionCount>1) {
	    				createViolation(getWebSourceCode().getLineForNode(flowNode), "Only one unconditional flow will get executed.");
	    			}
    			}
    		}	    
		} catch (XPathExpressionException e) {
			// Nothing to do
		}
	}

}
