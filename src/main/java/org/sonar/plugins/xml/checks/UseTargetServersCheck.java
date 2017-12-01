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

import java.util.HashSet;
import java.util.Set;

import org.sonar.check.Rule;
import org.sonar.plugins.xml.checks.XmlSourceCode;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 * Encourage the use of target servers.
 * Code : TD002
 * @author Nicolas Tisserand
 */
@Rule(key = "UseTargetServersCheck")
public class UseTargetServersCheck extends AbstractXmlCheck {

	@Override
	public void validate(XmlSourceCode xmlSourceCode) {
	    setWebSourceCode(xmlSourceCode);

	    Document document = getWebSourceCode().getDocument(false);
	    if (document.getDocumentElement() != null) {

	    	// Search for last Flow of an ProxyEndpoint document
	    	if("ProxyEndpoint".equals(document.getDocumentElement().getNodeName())) {

		    	NodeList targetNodeList = document.getDocumentElement().getElementsByTagName("TargetEndpoint");
		    	
		    	Set<String> targetRefSet = new HashSet<String>();
		    	if(targetNodeList!=null) {
		    		
		    		// Look for each different target endpoint
		    		for(int i=0; i<targetNodeList.getLength(); i++) {
		    			Node targetNode = targetNodeList.item(i);
		    			targetRefSet.add(targetNode.getTextContent());
		    		}
		    	}

		    	// If there is only NoRoute (without TargetEndpoint), then it's not a violation.
	    		// If there is only ONE TargetEndpoint, this is a violation.
	    		// If there are more than ONE TargetEndpoint, this is ok.
		    	if(targetRefSet.size() == 1) {
		    
		    		int lineNumber = 1; // By default

		    		// Search for a <RouteRule> node (it's a better location to indicate the violation
		    		NodeList routeRuleNodeList = document.getDocumentElement().getElementsByTagName("RouteRule");
		    		if(routeRuleNodeList!=null && routeRuleNodeList.getLength()>0) {
		    			lineNumber = getWebSourceCode().getLineForNode(routeRuleNodeList.item(0));
		    		}
		    		
		    		createViolation(lineNumber, "Encourage the use of target servers.");
		    	}
	    	}
	    }
	}

}
