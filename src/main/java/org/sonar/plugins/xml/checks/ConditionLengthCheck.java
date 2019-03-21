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
import org.sonar.check.RuleProperty;
import org.sonarsource.analyzer.commons.xml.XmlFile;
import org.sonarsource.analyzer.commons.xml.checks.SonarXmlCheck;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 * Definition : Overly long conditions on Steps are difficult to debug and maintain.
 * Code : CC003
 * @author Nicolas Tisserand
 */
@Rule(key = "ConditionLengthCheck")
public class ConditionLengthCheck extends SonarXmlCheck {

	@RuleProperty(
	    defaultValue = "256",
	    description = "Max length allowed for a condition tag")
	protected int maxConditionLength = 256;
	
	@Override
	public void scanFile(XmlFile xmlFile) {
		
	    Document document = xmlFile.getDocument();
		
	    if (document.getDocumentElement() != null) {
    	
		    XPathFactory xPathfactory = XPathFactory.newInstance();
		    XPath xpath = xPathfactory.newXPath();
		    
		    try {
		    	// Select in one shot the Condition which are too long
			    NodeList conditionNodeList = (NodeList) xpath.evaluate("//Condition[string-length(text()) > "+maxConditionLength+"]", document, XPathConstants.NODESET);
			    
		    	if(conditionNodeList!=null) {
		    		for(int i=0 ; i < conditionNodeList.getLength(); i++) {
		    	    	Node node = conditionNodeList.item(i);
		    	    	reportIssue(node, "Condition is " + node.getTextContent().length() + " characters.");
		    		}
		    	}
			} catch (XPathExpressionException e) {
				// Nothing to do
			}
	    }
		    
	}

}
