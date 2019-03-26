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

import org.sonar.check.Rule;
import org.sonarsource.analyzer.commons.xml.XmlFile;
import org.sonarsource.analyzer.commons.xml.checks.SonarXmlCheck;
import org.w3c.dom.Document;

/**
 * Unattached resources are dead code and should be removed from production bundles.
 * Code : BN502
 * @author Nicolas Tisserand
 */
@Rule(key = "UnattachedResourceCheck")
public class UnattachedResourceCheck extends SonarXmlCheck {

	@Override
	public void scanFile(XmlFile xmlFile) {
		
	    Document document = xmlFile.getDocument();
	    if (document.getDocumentElement() != null) {
	    	
	    	String rootNodeName = document.getDocumentElement().getNodeName();

	    	if("xsl:stylesheet".equals(rootNodeName) || "wsdl:definitions".equals(rootNodeName) || "xs:schema".equals(rootNodeName)) {
		    	
		    	// Deal with XSL/WSDL/WS resource files
		    	String protocol = rootNodeName.substring(0, rootNodeName.indexOf(':')) + "://";
		    	if("xs:schema".equals(rootNodeName)) {
		    		protocol = "xsd://";
		    	}
		    	
		    	String resourceURL = protocol + xmlFile.getInputFile().filename();
		    	
		    	// Verify that there is a least a policy :
		    	if(BundleRecorder.searchPoliciesByResourceURL(resourceURL).isEmpty()) {
		    		reportIssue(document.getDocumentElement(), "This resource is not attached to a Policy in the bundle.");
		    	}
		    }
	    }
	}

}
