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

import org.sonar.check.Rule;
import org.sonar.plugins.xml.checks.XmlSourceCode;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * A default flow should be defined.
 * Code : FL500
 * @author Nicolas Tisserand
 */
@Rule(key = "UnknownResourceFlowCheck")
public class UnknownResourceFlowCheck extends AbstractXmlCheck {

	@Override
	public void validate(XmlSourceCode xmlSourceCode) {
	    setWebSourceCode(xmlSourceCode);

	    Document document = getWebSourceCode().getDocument(false);
	    if (document.getDocumentElement() != null && "ProxyEndpoint".equals(document.getDocumentElement().getNodeName())) {

	    	NodeList flowsNodeList = document.getDocumentElement().getElementsByTagName("Flows");
	    	
	    	if(flowsNodeList != null && flowsNodeList.getLength()>0) {
	    		Node flowsNode = flowsNodeList.item(0);
	    		if(flowsNode != null) {
	    			// Go to the last defined flow
	    			Node lastFlowNode = flowsNode.getLastChild();

	    			// Loop to get rid of #text nodes
	    			int loop = 0;
	    			while(!"Flow".equals(lastFlowNode.getNodeName()) && loop < flowsNode.getChildNodes().getLength()) {
	    				lastFlowNode = lastFlowNode.getPreviousSibling();
	    				loop++;
	    			}
	    		
	    			// Search and test Condition value
	    			NodeList lastFlowChilds = lastFlowNode.getChildNodes();
	    			for(int i = 0; i < lastFlowChilds.getLength(); i++) {
	    				Node currentChild = lastFlowChilds.item(i);
	    				if("Condition".equals(currentChild.getNodeName())) {
	    					String cond = currentChild.getTextContent();
	    					if(!"true".equalsIgnoreCase(cond)) {
	    						createViolation(getWebSourceCode().getLineForNode(flowsNode.getNextSibling()) - 1, "There is no default flow in this proxy endpoint.");
	    					}
	    				}
	    			}
	    		}
	    	}
	    }
	}

}
