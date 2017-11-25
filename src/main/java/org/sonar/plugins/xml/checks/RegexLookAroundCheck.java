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
 * Definition : Regex Lookahead/Lookbehind are expensive, especially when applied to large text blocks, consider refactoring to a simpler regular expression.
 * Code : PO018
 * @author Nicolas Tisserand
 */
@Rule(key = "RegexLookAroundCheck")
public class RegexLookAroundCheck extends AbstractXmlCheck {

	@Override
	public void validate(XmlSourceCode xmlSourceCode) {
	    setWebSourceCode(xmlSourceCode);
	    
	    Document document = getWebSourceCode().getDocument(false);
	    if (document.getDocumentElement() != null) {
    	
		    XPathFactory xPathfactory = XPathFactory.newInstance();
		    XPath xpath = xPathfactory.newXPath();
		    
		    try {
		    	// Select in one shot the Condition which are too long
			    NodeList patternNodeList = (NodeList) xpath.evaluate("/RegularExpressionProtection//Pattern[contains(text(), '(?') ]", document, XPathConstants.NODESET);
			    
		    	if(patternNodeList!=null) {
		    		for(int i=0 ; i < patternNodeList.getLength(); i++) {
		    	    	Node node = patternNodeList.item(i);
	    				createViolation(getWebSourceCode().getLineForNode(node), "Lookaround in Regex can be inefficient.");
		    		}
		    	}
			} catch (XPathExpressionException e) {
			}
	    }
		    
	}

}
