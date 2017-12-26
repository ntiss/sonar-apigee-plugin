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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;
import org.w3c.dom.Document;
import org.w3c.dom.Node;


/**
 * Definition : The Description tag should be compliant with the pattern defined in the rule parameter ""
 * Code : BN501
 * @author Nicolas Tisserand
 */
@Rule(key = "DescriptionPatternCheck")
public class DescriptionPatternCheck extends AbstractXmlCheck {

	@RuleProperty(
	    defaultValue = ".*",
	    description = "Pattern of the APIProxy description tag")
	protected String regexPattern = ".*";
	
	public String getRegexPattern() {
		return regexPattern;
	}

	public void setRegexPattern(String regexPattern) {
		this.regexPattern = regexPattern;
	}

	@Override
	public void validate(XmlSourceCode xmlSourceCode) {
	    setWebSourceCode(xmlSourceCode);
	    
	    Document document = getWebSourceCode().getDocument(false);
	    if (document.getDocumentElement() != null && "APIProxy".equals(document.getDocumentElement().getNodeName())) {

		    XPathFactory xPathfactory = XPathFactory.newInstance();
		    XPath xpath = xPathfactory.newXPath();
		    
		    try {
		    	// Select in one shot the Description which are too short
		    	Node descriptionNode = (Node)xpath.evaluate("//Description", document, XPathConstants.NODE);
		    	
		    	if(descriptionNode!=null) {
		    		String desc = descriptionNode.getTextContent();
		    		
		    		Pattern pattern = Pattern.compile(regexPattern);
		    		Matcher matcher = pattern.matcher(desc);
		    		
		    		if(!matcher.matches()) {
		    			createViolation(getWebSourceCode().getLineForNode(descriptionNode), "Description is not compliant with the pattern " + regexPattern);
		    		}
		    	}
			} catch (XPathExpressionException e) {
			}
	    }
		    
	}

}
