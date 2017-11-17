/*
 * SonarQube Apigee XML Plugin
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
