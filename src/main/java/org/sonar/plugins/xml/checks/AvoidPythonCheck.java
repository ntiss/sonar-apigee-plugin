/*
 * SonarQube Apigee Python Plugin
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
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * Avoid Python language
 * Code : PY500
 * @author Nicolas Tisserand
 */
@Rule(key = "AvoidPythonCheck")
public class AvoidPythonCheck extends AbstractXmlCheck {

	@Override
	public void validate(XmlSourceCode xmlSourceCode) {
	    setWebSourceCode(xmlSourceCode);
	    
	    Document document = getWebSourceCode().getDocument(false);
	    if (document.getDocumentElement() != null) {
    	
		    XPathFactory xPathfactory = XPathFactory.newInstance();
		    XPath xpath = xPathfactory.newXPath();
		    
		    try {
		    	// Select in one shot the Description which are too short
			    XPathExpression exprDisplayName = xpath.compile("//Script//ResourceURL[starts-with(., 'py://')]");
			    Node resourceURLNode = (Node)exprDisplayName.evaluate(document, XPathConstants.NODE);

		    	if(resourceURLNode!=null) {
	    			createViolation(getWebSourceCode().getLineForNode(resourceURLNode), "Avoid Python language.");
		    	}
			} catch (XPathExpressionException e) {
			}
	    }
		    
	}
	
}
