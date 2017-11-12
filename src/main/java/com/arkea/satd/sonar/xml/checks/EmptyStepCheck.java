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
 * Definition : Empty steps clutter a bundle. Performance is not degraded.
 * Codes : ST001
 * @author Nicolas Tisserand
 */
@Rule(key = "EmptyStepCheck")
public class EmptyStepCheck extends AbstractXmlCheck {

	@Override
	public void validate(XmlSourceCode xmlSourceCode) {
	    setWebSourceCode(xmlSourceCode);

	    Document document = getDocument(false);
	    if (document.getDocumentElement() != null && "ProxyEndpoint".equals(document.getDocumentElement().getNodeName())) {
	    	
	    	// Search for <Step> nodes in a ProxyEndpoint document
	    	NodeList stepNodeList = document.getDocumentElement().getElementsByTagName("Step");
	    	
	    	if(stepNodeList != null) {
	    		for(int i=0; i < stepNodeList.getLength(); i++) {
		    		Node stepNode = stepNodeList.item(i);

		    		// if the node has no child : throw an issue
	    			boolean hasElement = false;
		    		if(stepNode != null) {
		    			
		    			NodeList stepChildrenNodes = stepNode.getChildNodes();
		    			for(int j=0; j < stepChildrenNodes.getLength(); j++) {
		    				Node childNode = stepChildrenNodes.item(j);
		    				if(childNode.getNodeType() == Node.ELEMENT_NODE) {
		    					hasElement = true;
		    					break;
		    				}
		    			}
		    		}
	    			
	    			if(!hasElement) {
	    				createViolation(getWebSourceCode().getLineForNode(stepNode), "Empty steps clutter a bundle. Performance is not degraded.");
	    			}
		    		
	    		}
	    	}
	    }
	}

}
