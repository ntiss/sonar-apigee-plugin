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
 * Unreachable Route Rules - empty conditions go last
 * Check RouteRules in a ProxyEndpoint to ensure that empty condition is last.
 * Code : PD003
 * @author Nicolas Tisserand
 */
@Rule(key = "EmptyRouteRuleLastCheck")
public class EmptyRouteRuleLastCheck extends AbstractXmlCheck {

	@Override
	public void validate(XmlSourceCode xmlSourceCode) {
	    setWebSourceCode(xmlSourceCode);

	    Document document = getWebSourceCode().getDocument(false);
    	// Search for last Flow of an ProxyEndpoint document
	    if (document.getDocumentElement() != null && "ProxyEndpoint".equals(document.getDocumentElement().getNodeName())) {
	    	

			    XPathFactory xPathfactory = XPathFactory.newInstance();
			    XPath xpath = xPathfactory.newXPath();
	    		
				try {
					NodeList flowNodeList = (NodeList)xpath.evaluate("//RouteRule", document, XPathConstants.NODESET);
		    	
			    	if(flowNodeList!=null && flowNodeList.getLength() >= 2) {
			    		
			    		// Reversed loop
			    		for(int i=flowNodeList.getLength()-2; i>=0; i--) {
			    			Node routeRuleNode = flowNodeList.item(i);
	
			    			// Search Condition value
			    			Node condition = (Node)xpath.evaluate("Condition", routeRuleNode, XPathConstants.NODE);
			    			
			    			if(condition==null || condition.getTextContent().isEmpty() || "true".equals(condition.getTextContent())) {
			    				// Issue detected
			    				createViolation(getWebSourceCode().getLineForNode(routeRuleNode), "Unreachable Route Rules - empty conditions go last");
			    			}
			    		}
			    	}
				} catch (XPathExpressionException e) {
				}
	    	
	    }
	}

}
