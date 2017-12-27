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
				// Nothing to do
			}
	    }
		    
	}

}
