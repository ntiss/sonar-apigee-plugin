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


/**
 * Definition : Reserved words as variables - ServiceCallout Response
 * Code : PO020
 * @author Nicolas Tisserand
 */
@Rule(key = "ServiceCalloutResponseVariableNameCheck")
public class ServiceCalloutResponseVariableNameCheck extends AbstractXmlCheck {

	@Override
	public void validate(XmlSourceCode xmlSourceCode) {
	    setWebSourceCode(xmlSourceCode);
	    
	    Document document = getWebSourceCode().getDocument(false);
	    if (document.getDocumentElement() != null) {
    	
		    XPathFactory xPathfactory = XPathFactory.newInstance();
		    XPath xpath = xPathfactory.newXPath();
		    
		    try {
		    	// Select in one shot the Response which have variable equals to 'response'
			    Node responseNode = (Node)xpath.evaluate("/ServiceCallout/Response[text() = 'response']", document, XPathConstants.NODE);
		    	if(responseNode!=null) {
    				createViolation(getWebSourceCode().getLineForNode(responseNode), "Using response for the Response name causes unexepected side effects.");
		    	}
			    
			} catch (XPathExpressionException e) {
			}
	    }
		    
	}

}
