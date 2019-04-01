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

import java.util.List;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.sonar.check.Rule;
import org.sonarsource.analyzer.commons.xml.XmlFile;
import org.sonarsource.analyzer.commons.xml.checks.SonarXmlCheck;
import org.w3c.dom.Document;

import com.arkea.satd.sonar.xml.BundleRecorder;

/**
 * Unattached policies are dead code and should be removed from production bundles.
 * Code : BN005
 * @author Nicolas Tisserand
 */
@Rule(key = "UnattachedPolicyCheck")
public class UnattachedPolicyCheck extends SonarXmlCheck {

	@Override
	public void scanFile(XmlFile xmlFile) {
		
	    Document document = xmlFile.getDocument();
	    if (document.getDocumentElement() != null) {
	    	
	    	String rootNodeName = document.getDocumentElement().getNodeName();

	    	if(!"Manifest".equals(rootNodeName) && !"xsl:stylesheet".equals(rootNodeName) && !"wsdl:definitions".equals(rootNodeName) && !"xs:schema".equals(rootNodeName) ) {

			    XPathFactory xPathfactory = XPathFactory.newInstance();
			    XPath xpath = xPathfactory.newXPath();
			    
			    try {
			    	// Select in one shot the name attribute
			    	String attrName = (String)xpath.evaluate("/*/@name", document, XPathConstants.STRING);
			    	
			    	// Verify that this is a policy :
			    	if(BundleRecorder.searchPoliciesByName(attrName) != null) {
			    		
			    		// Search for a step with the same name
			    		List<XmlFile> stepsList = BundleRecorder.searchByStepName(attrName);
			    		if(stepsList==null || stepsList.isEmpty()) {
			    			reportIssue(document.getDocumentElement(), "This policy is not attached to a Step in the bundle.");
			    		}
			    	}
			    	
				} catch (XPathExpressionException e) {
					// Nothing to do
				}
		    }
	    }
		    
	}

}
