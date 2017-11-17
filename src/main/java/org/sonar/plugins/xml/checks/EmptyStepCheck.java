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

	    Document document = getWebSourceCode().getDocument(false);
	    
	    XPathFactory xPathfactory = XPathFactory.newInstance();
	    XPath xpath = xPathfactory.newXPath();
	    
	    try {
	    	// Select in one shot the Step nodes which are empty
		    XPathExpression exprDisplayName = xpath.compile("//ProxyEndpoint//Step[not(normalize-space())]");
		    NodeList descriptionNodeList = (NodeList)exprDisplayName.evaluate(document, XPathConstants.NODESET);

	    	if(descriptionNodeList!=null) {
	    		for(int i=0 ; i < descriptionNodeList.getLength(); i++) {
	    	    	Node stepNode = descriptionNodeList.item(i);
    				createViolation(getWebSourceCode().getLineForNode(stepNode), "Empty steps clutter a bundle. Performance is not degraded.");
	    		}
	    	}
		} catch (XPathExpressionException e) {
		}

	}

}
