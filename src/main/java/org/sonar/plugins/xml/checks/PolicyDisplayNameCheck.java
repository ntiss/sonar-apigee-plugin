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
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.sonar.check.Rule;
import org.sonar.plugins.xml.checks.XmlSourceCode;
import org.w3c.dom.Document;
import org.w3c.dom.Node;


/**
 * Policy Naming Conventions
 * It is recommended that the policy name attribute match the display name of the policy.
 * Code : PO008
 * @author Nicolas Tisserand
 */
@Rule(key = "PolicyDisplayNameCheck")
public class PolicyDisplayNameCheck extends AbstractXmlCheck {

	@Override
	public void validate(XmlSourceCode xmlSourceCode) {
	    setWebSourceCode(xmlSourceCode);

	    Document document = getWebSourceCode().getDocument(false);
	    
	    XPathFactory xPathfactory = XPathFactory.newInstance();
	    XPath xpath = xPathfactory.newXPath();
	    
		try {
			XPathExpression exprName = xpath.compile("//@name");
		    String nameAttr = (String)exprName.evaluate(document, XPathConstants.STRING);

		    XPathExpression exprDisplayName = xpath.compile("//DisplayName");
		    Node displayName = (Node)exprDisplayName.evaluate(document, XPathConstants.NODE);
	
		    if(nameAttr!=null && displayName!=null) {
			    	
			    String displayNameText = displayName.getTextContent();
			    
			    if(displayNameText!=null) {
			    	if( !displayNameText.equals(nameAttr)) {
						// Create a violation for the root node
						createViolation(getWebSourceCode().getLineForNode(displayName), "It is recommended that the policy name attribute ("+nameAttr+") match the display name of the policy ("+displayNameText+").");
			    	}
	    		}
		    }
		} catch (XPathExpressionException e) {
		}
	}
	

}
