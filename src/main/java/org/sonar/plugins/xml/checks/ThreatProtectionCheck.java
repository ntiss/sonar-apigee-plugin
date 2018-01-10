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


/**
 * JSON Threat Protection or XML Threat Protection
 * A check for a body element should be performed before policy execution.
 * Code : PO001 & PO002
 * @author Nicolas Tisserand
 */
@Rule(key = "ThreatProtectionCheck")
public class ThreatProtectionCheck extends AbstractBodyCheck {

	@Override
	public void validate(XmlSourceCode xmlSourceCode) {
	    setWebSourceCode(xmlSourceCode);

	    Document document = getWebSourceCode().getDocument(false);
	    
	    XPathFactory xPathfactory = XPathFactory.newInstance();
	    XPath xpath = xPathfactory.newXPath();
	    
	    try {
		    String nameAttr = (String)xpath.evaluate("//*[name() = 'JSONThreatProtection' or name() = 'XMLThreatProtection']/@name", document, XPathConstants.STRING);
		
			checkConditionInStepOrParent(nameAttr, 
										"(response.content|response.form|request.content|request.form|message.content|message.form|message.verb|request.verb|request.header.Content-Length|response.header.Content-Length)");
		} catch (XPathExpressionException e) {
			// Nothing to do
		}
	}
	
}
