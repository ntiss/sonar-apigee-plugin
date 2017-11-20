/*
 * SonarQube Apigee Python Plugin
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
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
 * No Condition on FaultRule.
 * Code : FR001
 * @author Nicolas Tisserand
 */
@Rule(key = "FaultRuleConditionCheck")
public class FaultRuleConditionCheck extends AbstractXmlCheck {

	@Override
	public void validate(XmlSourceCode xmlSourceCode) {
	    setWebSourceCode(xmlSourceCode);
	    
	    Document document = getWebSourceCode().getDocument(false);
	    if (document.getDocumentElement() != null) {
    	
		    XPathFactory xPathfactory = XPathFactory.newInstance();
		    XPath xpath = xPathfactory.newXPath();
		    
		    try {
		    	// Select the faultRule
		    	NodeList faultRuleNodeList = (NodeList)xpath.evaluate("//FaultRule", document, XPathConstants.NODESET);
		    	
		    	if(faultRuleNodeList!=null) {
		    		
		    		for(int i=0; i < faultRuleNodeList.getLength(); i++) {
		    			
		    			Node currentFaultRule = faultRuleNodeList.item(i);
		    			
		    			// Check the condition
		    			String condition = (String)xpath.evaluate("Condition", currentFaultRule, XPathConstants.STRING);
		    			
		    			if(condition==null || condition.isEmpty() || "true".equals(condition)) {
			    			createViolation(getWebSourceCode().getLineForNode(currentFaultRule), "FaultRule has no Condition or the Condition is empty.");		    				
		    			}
		    		}
		    	}
			} catch (XPathExpressionException e) {
			}
	    }
		    
	}
	
}
