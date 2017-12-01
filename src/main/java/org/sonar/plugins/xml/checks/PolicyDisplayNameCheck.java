/*
 * Copyright 2017 Credit Mutuel Arkea
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
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
			XPathExpression exprName = xpath.compile("/*/@name");
		    String nameAttr = (String)exprName.evaluate(document, XPathConstants.STRING);
			
			XPathExpression exprDisplayName = xpath.compile("//DisplayName[text() != /*/@name]");
		    Node displayNameNode = (Node)exprDisplayName.evaluate(document, XPathConstants.NODE);
	
		    if(displayNameNode!=null) {
			    String displayNameText = displayNameNode.getTextContent();
			    
				// Create a violation for the root node
				createViolation(getWebSourceCode().getLineForNode(displayNameNode), "It is recommended that the policy name attribute ("+nameAttr+") match the display name of the policy ("+displayNameText+").");
		    }
		} catch (XPathExpressionException e) {
		}
	}
	

}
