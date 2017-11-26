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
import org.sonar.plugins.xml.checks.XmlSourceCode;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Only one RouteRule should be present without a condition
 * Code : PD002
 * @author Nicolas Tisserand
 */
@Rule(key = "UnreachableRouteRuleCheck")
public class UnreachableRouteRuleCheck extends AbstractXmlCheck {

	@Override
	public void validate(XmlSourceCode xmlSourceCode) {
	    setWebSourceCode(xmlSourceCode);

	    Document document = getWebSourceCode().getDocument(false);
	    if (document.getDocumentElement() != null) {

	    	// Search for last Flow of an ProxyEndpoint document
	    	if("ProxyEndpoint".equals(document.getDocumentElement().getNodeName())) {

			    XPathFactory xPathfactory = XPathFactory.newInstance();
			    XPath xpath = xPathfactory.newXPath();
			    
			    try {
			    	// Select the RouteRule without Condition (or "true")
				    NodeList routeRuleList = (NodeList)xpath.evaluate("//RouteRule[not(Condition) or Condition/text()='true' or string-length(Condition/text())=0]", document, XPathConstants.NODESET);
			    	if(routeRuleList!=null && routeRuleList.getLength()>1) {
			    		for(int i=0; i<routeRuleList.getLength(); i++) {
			    			Node routeRuleNode = routeRuleList.item(i);
	
		    				// Create a violation if flow node is not the last one
			    			createViolation(getWebSourceCode().getLineForNode(routeRuleNode), "Only one RouteRule should be present without a condition.");
			    		}
			    	}

				} catch (XPathExpressionException e) {
				}	    		
			    	
	    	}
	    }
	}

}
