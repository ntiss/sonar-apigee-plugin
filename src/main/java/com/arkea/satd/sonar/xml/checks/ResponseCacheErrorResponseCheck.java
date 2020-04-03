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

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.sonar.check.Rule;
import org.sonarsource.analyzer.commons.xml.XmlFile;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * By default the ResponseCache policy will cache non 200 responses.
 * Either create a condition or use policy configuration options to exclude non 200 responses.
 * Code : PO024
 * @author Nicolas Tisserand
 */
@Rule(key = "ResponseCacheErrorResponseCheck")
public class ResponseCacheErrorResponseCheck extends AbstractBodyCheck {
	
	public ResponseCacheErrorResponseCheck() {
		super();
	}
	
	@Override
	public void scanFile(XmlFile xmlFile) {
	    Document document = xmlFile.getDocument();
	    if (document.getDocumentElement() != null) {
    	
	    	String rootNodeName = document.getDocumentElement().getNodeName();
	    	if("ResponseCache".equals(rootNodeName) ) {

			    XPathFactory xPathfactory = XPathFactory.newInstance();
			    XPath xpath = xPathfactory.newXPath();
			    
			    try {
			    	// Select in one shot the Distributed
			    	Node excludeErrorNode = (Node)xpath.evaluate("/ResponseCache/ExcludeErrorResponse", document, XPathConstants.NODE);
	
			    	Node errorLocation = null;
			    	if(excludeErrorNode==null) {
			    		// Report the issue at the file level
			    		errorLocation = document.getDocumentElement();
			    	} else {
			    		String excludeErrorValue = (String)xpath.evaluate("/ResponseCache/ExcludeErrorResponse/text()", document, XPathConstants.STRING);
			    		if(excludeErrorValue==null || excludeErrorValue.isEmpty() || !"true".equalsIgnoreCase(excludeErrorValue)) {
			    			
			    			// The future error location
			    			errorLocation = excludeErrorNode;
			    		}
			    	}
			    	
	    			// Hey, the flag is not enabled, but let's check if there is a condition that checks response.* at the step
			    	if(errorLocation!=null) {
		    			String policyAttrName = (String)xpath.evaluate("/*/@name", document, XPathConstants.STRING);
		    			
		    			// Try to detect in Step or parent level
		    			boolean hasIssueAtStepOrFlowLevel = checkConditionInStepOrParent(policyAttrName, "response\\..*");
		    			
		    			// If there is a condition at the step or parent level, 
		    			// then revert the decision : don't report the issue at the <ExcludeErrorResponse> tag level
		    			if(!hasIssueAtStepOrFlowLevel) {
		    				errorLocation = null;
		    			}
			    	}
			    	// There is an issue to report
			    	if(errorLocation!=null) {
			    		reportIssue(errorLocation, "ExcludeErrorResponse is not enabled.");
			    	}
			    	
				} catch (XPathExpressionException e) {
					// Nothing to do
				}
	    	}
	    }		
	}
	
}
