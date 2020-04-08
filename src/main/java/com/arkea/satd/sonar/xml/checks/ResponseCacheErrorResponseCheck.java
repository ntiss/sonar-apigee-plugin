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
	    if (document.getDocumentElement() != null && "ResponseCache".equals(document.getDocumentElement().getNodeName())) {
    	
		    XPathFactory xPathfactory = XPathFactory.newInstance();
		    XPath xpath = xPathfactory.newXPath();
		    
		    try {

		    	Node errorLocation = null;
	    		String excludeErrorValue = (String)xpath.evaluate("/ResponseCache/ExcludeErrorResponse/text()", document, XPathConstants.STRING);
	    		
	    		// If there is no condition or "false" value  (i.e not "true")
	    		if(excludeErrorValue==null || excludeErrorValue.isEmpty() || !"true".equalsIgnoreCase(excludeErrorValue)) {
	
	    			// Select in one shot the ExcludeErrorResponse tag
	    			Node excludeErrorNode = (Node)xpath.evaluate("/ResponseCache/ExcludeErrorResponse", document, XPathConstants.NODE);
    			
	    			// The future error location
	    			errorLocation = excludeErrorNode!=null ? excludeErrorNode : document.getDocumentElement();
	    		}
		    	
    			// Hey, the flag is not enabled, but let's check if there is a condition that checks response.* at the step
		    	if(errorLocation!=null) {
	    			String policyAttrName = (String)xpath.evaluate("/*/@name", document, XPathConstants.STRING);
	    			
	    			// Try to detect in Step or parent level
	    			boolean hasIssueAtStepOrFlowLevel = checkConditionInStepOrParent(policyAttrName, "response\\..*");
	    			
	    			// Report the issue at the <ExcludeErrorResponse> tag level only if there is no condition at the step or parent level
	    			if(hasIssueAtStepOrFlowLevel) {
	    				reportIssue(errorLocation, "ExcludeErrorResponse is not enabled.");
	    			}
	    			
		    	}
		    	
			} catch (XPathExpressionException e) {
				// Nothing to do
			}
	    }		
	}
	
}
