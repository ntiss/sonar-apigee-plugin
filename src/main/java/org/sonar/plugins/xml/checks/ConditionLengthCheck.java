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
 * Definition : Overly long conditions on Steps are difficult to debug and maintain.
 * Code : CC003
 * @author Nicolas Tisserand
 */
@Rule(key = "ConditionLengthCheck")
public class ConditionLengthCheck extends AbstractXmlCheck {

	private static int MAX_LENGTH = 256; 
	
	@Override
	public void validate(XmlSourceCode xmlSourceCode) {
	    setWebSourceCode(xmlSourceCode);
	    
	    Document document = getWebSourceCode().getDocument(false);
	    if (document.getDocumentElement() != null) {
    	
		    XPathFactory xPathfactory = XPathFactory.newInstance();
		    XPath xpath = xPathfactory.newXPath();
		    
		    try {
		    	// Select in one shot the Condition which are too long
			    NodeList conditionNodeList = (NodeList) xpath.evaluate("//Condition[string-length(text()) > "+MAX_LENGTH+"]", document, XPathConstants.NODESET);
			    
		    	if(conditionNodeList!=null) {
		    		for(int i=0 ; i < conditionNodeList.getLength(); i++) {
		    	    	Node node = conditionNodeList.item(i);
	    				createViolation(getWebSourceCode().getLineForNode(node), "Condition is " + node.getTextContent().length() + " characters.");
		    		}
		    	}
			} catch (XPathExpressionException e) {
			}
	    }
		    
	}

}
