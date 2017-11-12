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
import org.sonar.plugins.xml.checks.XmlSourceCode;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Flow without a condition should be last.
 * Code : FL501
 * @author Nicolas Tisserand
 */
@Rule(key = "UnreachableFlowCheck")
public class UnreachableFlowCheck extends AbstractXmlCheck {

	@Override
	public void validate(XmlSourceCode xmlSourceCode) {
	    setWebSourceCode(xmlSourceCode);

	    Document document = getDocument(false);
	    if (document.getDocumentElement() != null) {

	    	// Search for last Flow of an ProxyEndpoint document
	    	if("ProxyEndpoint".equals(document.getDocumentElement().getNodeName())) {

		    	NodeList flowNodeList = document.getDocumentElement().getElementsByTagName("Flow");
		    	
		    	if(flowNodeList!=null) {
		    		for(int i=0; i<flowNodeList.getLength(); i++) {
		    			Node flowNode = flowNodeList.item(i);

		    			// Search Condition value
		    			NodeList flowChilds = flowNode.getChildNodes();
		    			boolean hasNoCondition = true;
		    			for(int j = 0; j < flowChilds.getLength() && hasNoCondition; j++) {
		    				Node currentChild = flowChilds.item(j);
		    				if("Condition".equals(currentChild.getNodeName())) {
		    					String cond = currentChild.getTextContent();
		    					if(!"true".equalsIgnoreCase(cond) && cond.length()>0) {
		    						hasNoCondition = false;
		    					}
		    				}
		    			}
		    			
	    				// Create a violation if flow node is not the last one
		    			if(hasNoCondition && i < flowNodeList.getLength()-1) {
			    			createViolation(getWebSourceCode().getLineForNode(flowNode), "Flow without a condition should be last.");
		    			}
		    		}
		    	}
	    	}
	    }
	}

}
