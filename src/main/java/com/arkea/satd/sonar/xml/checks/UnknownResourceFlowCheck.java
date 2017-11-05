/*
 * SonarQube Apigee XML Plugin
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package com.arkea.satd.sonar.xml.checks;

import org.sonar.check.Rule;
import org.sonar.plugins.xml.checks.AbstractXmlCheck;
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
	    if (document.getDocumentElement() != null) {
	    	
	    	// Search for last Flow of an ProxyEndpoint document
	    	if("ProxyEndpoint".equals(document.getDocumentElement().getNodeName())) {

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

}
