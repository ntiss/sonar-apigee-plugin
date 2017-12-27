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


/**
 * Definition : Discourage accessing management server from a proxy.
 * Code : TD001 & PO009
 * @author Nicolas Tisserand
 */
@Rule(key = "UseManagementServerCheck")
public class UseManagementServerCheck extends AbstractXmlCheck {

	@Override
	public void validate(XmlSourceCode xmlSourceCode) {
	    setWebSourceCode(xmlSourceCode);
	    
	    Document document = getWebSourceCode().getDocument(false);
	    if (document.getDocumentElement() != null) {
    	
		    XPathFactory xPathfactory = XPathFactory.newInstance();
		    XPath xpath = xPathfactory.newXPath();
		    
		    try {
		    	// Select the URL which points to the management server
			    Node urlNode = (Node)xpath.evaluate("//HTTPTargetConnection/URL[contains(text(), '/v1/organizations') or contains(text(), 'enterprise.apigee.com')]", document, XPathConstants.NODE);
		    	if(urlNode!=null) {
    				createViolation(getWebSourceCode().getLineForNode(urlNode), "HTTPTargetConnection appears to be connecting to Management Server.");
		    	}
			} catch (XPathExpressionException e) {
				// Nothing to do
			}
	    }
	}

}
