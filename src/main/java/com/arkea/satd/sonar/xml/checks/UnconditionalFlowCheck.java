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
 * Only one unconditional flow will get executed. Error if more than one was detected.
 * Code : FL001
 * @author Nicolas Tisserand
 */
@Rule(key = "UnconditionalFlowCheck")
public class UnconditionalFlowCheck extends AbstractXmlCheck {

	@Override
	public void validate(XmlSourceCode xmlSourceCode) {
	    setWebSourceCode(xmlSourceCode);

	    Document document = getDocument(false);
	    if (document.getDocumentElement() != null) {
	    	
	    	int noConditionCount = 0;

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
		    			
		    			if(hasNoCondition) {
		    				noConditionCount++;

		    				// Create a violation for each flow node
			    			if(noConditionCount>1) {
			    				createViolation(getWebSourceCode().getLineForNode(flowNode), "Only one unconditional flow will get executed.");
			    			}
		    			}
		    		}
		    	}
	    	}
	    	
	    	
	    	
	    }
	}

}
