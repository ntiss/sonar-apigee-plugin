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

import java.util.HashSet;
import java.util.Set;

import org.sonar.check.Rule;
import org.sonarsource.analyzer.commons.xml.XmlFile;
import org.sonarsource.analyzer.commons.xml.checks.SonarXmlCheck;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 * Encourage the use of target servers.
 * Code : TD002
 * @author Nicolas Tisserand
 */
@Rule(key = "UseTargetServersCheck")
public class UseTargetServersCheck extends SonarXmlCheck {

	@Override
	public void scanFile(XmlFile xmlFile) {
		
	    Document document = xmlFile.getDocument();
	    if (document.getDocumentElement() != null && "ProxyEndpoint".equals(document.getDocumentElement().getNodeName())) {

	    	// Search for last Flow of an ProxyEndpoint document
	    	NodeList targetNodeList = document.getDocumentElement().getElementsByTagName("TargetEndpoint");
	    	NodeList routesNodeList = document.getDocumentElement().getElementsByTagName("RouteRule");
	    	
	    	
	    	boolean hasNoroute = false;
	    	if(routesNodeList.getLength() > targetNodeList.getLength()) {
	    		hasNoroute = true;
	    	}
	    	
	    	Set<String> targetRefSet = new HashSet<>();
	    	int nbOfRouteRulesWithTarget = targetNodeList.getLength();

	    	Node nodeWithIssue = document.getDocumentElement(); // By default
	    	for(int i=0; i<targetNodeList.getLength(); i++) {
		    	targetRefSet.add(targetNodeList.item(i).getTextContent());
	    		// Use the <TargetEndpoint> node, it's a better location to indicate the violation
		    	nodeWithIssue = targetNodeList.item(i);
	    	}

    		// If there are more than ONE TargetEndpoint, this is ok (default behaviour).
	    	// If there is only NoRoute (without TargetEndpoint), then it's not a violation.
    		// If there is only ONE unique TargetEndpoint with a NoRoute route, this is still ok.
    		// If there is only ONE unique TargetEndpoint without a NoRoute route, this is a violation.
	    	if(targetRefSet.size() == 1 && (!hasNoroute || nbOfRouteRulesWithTarget>1) ) {
    			reportIssue(nodeWithIssue, "Encourage the use of target servers.");
    		
	    	}
    	}
	}

}
