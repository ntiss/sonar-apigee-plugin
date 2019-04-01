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
package com.arkea.satd.sonar.xml.checks;

import java.util.Collections;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.sonar.check.Rule;
import org.sonarsource.analyzer.commons.xml.XmlFile;
import org.sonarsource.analyzer.commons.xml.XmlTextRange;
import org.sonarsource.analyzer.commons.xml.checks.SonarXmlCheck;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * A default flow must be defined.
 * Code : FL500
 * @author Nicolas Tisserand
 */
@Rule(key = "UnknownResourceFlowCheck")
public class UnknownResourceFlowCheck extends SonarXmlCheck {

	@Override
	public void scanFile(XmlFile xmlFile) {
		
	    Document document = xmlFile.getDocument();
	    XPathFactory xPathfactory = XPathFactory.newInstance();
	    XPath xpath = xPathfactory.newXPath();
	    
	    try {
	    	// Select the last Flow node
	    	Node flowsNode = (Node)xpath.evaluate("/ProxyEndpoint/Flows", document, XPathConstants.NODE);
	    	String condition = (String)xpath.evaluate("/ProxyEndpoint/Flows/Flow[last()]/Condition/text()", document, XPathConstants.STRING);
			if(flowsNode!=null && 
				condition!=null && 
				condition.length()>0 && 
				!"true".equalsIgnoreCase(condition)) {
				
				// Find the closing tag
				final XmlTextRange textRange = XmlFile.endLocation((Element)flowsNode);
				reportIssue(textRange, "There is no default flow in this proxy endpoint.", Collections.emptyList());								
			}			    
		} catch (XPathExpressionException e) {
			// Nothing to do
		}
	}

}
