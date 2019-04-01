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

import org.sonar.check.Rule;
import org.sonarsource.analyzer.commons.xml.XmlFile;
import org.sonarsource.analyzer.commons.xml.checks.SonarXmlCheck;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;


/**
 * Force the use of fault rules.
 * Code : FR501
 * @author Nicolas Tisserand
 */
@Rule(key = "UseFaultRulesCheck")
public class UseFaultRulesCheck extends SonarXmlCheck {

	@Override
	public void scanFile(XmlFile xmlFile) {
		
	    Document document = xmlFile.getDocument();
	    if (document.getDocumentElement() != null && "ProxyEndpoint".equals(document.getDocumentElement().getNodeName())) {
	    	// Search for FaultRule of a ProxyEndpoint document
	    	NodeList faultRuleNodeList = document.getDocumentElement().getElementsByTagName("FaultRule");
	    	NodeList defaultFaultRuleNodeList = document.getDocumentElement().getElementsByTagName("DefaultFaultRule");
	    	
	    	// If there is neither FaultRule or DefaultFaultRule, this is a violation.
	    	if(faultRuleNodeList.getLength() + defaultFaultRuleNodeList.getLength() == 0) {	    
	    		reportIssue(document, "FaultRules or DefaultFaultRule must be used.");
	    	}
    	}
	}

}
