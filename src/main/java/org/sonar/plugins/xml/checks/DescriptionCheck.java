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
import org.sonar.check.RuleProperty;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 * Definition : The Description tags should meet minimum length requirements to be useful. The default minimum length is 5. This can be modified in the Quality Profile.
 * Code : BN500
 * @author Nicolas Tisserand
 */
@Rule(key = "DescriptionCheck")
public class DescriptionCheck extends AbstractXmlCheck {

	@RuleProperty(
	    defaultValue = "5",
	    description = "Min length allowed for a description tag")
	protected int minDescriptionLength = 5;

	
	
	@Override
	public void validate(XmlSourceCode xmlSourceCode) {
	    setWebSourceCode(xmlSourceCode);
	    
	    Document document = getWebSourceCode().getDocument(false);
	    if (document.getDocumentElement() != null) {
    	
		    XPathFactory xPathfactory = XPathFactory.newInstance();
		    XPath xpath = xPathfactory.newXPath();
		    
		    try {
		    	// Select in one shot the Description which are too short
			    XPathExpression exprDisplayName = xpath.compile("//Description[string-length(text())<="+minDescriptionLength+"]");
			    NodeList descriptionNodeList = (NodeList)exprDisplayName.evaluate(document, XPathConstants.NODESET);

		    	if(descriptionNodeList!=null) {
		    		for(int i=0 ; i < descriptionNodeList.getLength(); i++) {
		    	    	Node node = descriptionNodeList.item(i);
	    				createViolation(getWebSourceCode().getLineForNode(node), "Description is too short.");
		    		}
		    	}
			} catch (XPathExpressionException e) {
				// Nothing to do
			}
	    }
		    
	}

}
