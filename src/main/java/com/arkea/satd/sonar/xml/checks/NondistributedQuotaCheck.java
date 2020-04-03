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
import org.sonarsource.analyzer.commons.xml.checks.SonarXmlCheck;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * When using nondistributed quota the number of allowed calls is influenced by the number of Message Processors (MPs) deployed. 
 * This may lead to higher than expected transactions for a given quota as MPs now autoscale.
 * Code : PO022
 * @author Nicolas Tisserand
 */
@Rule(key = "NondistributedQuotaCheck")
public class NondistributedQuotaCheck extends SonarXmlCheck {
	
	public NondistributedQuotaCheck() {
		super();
	}
	
	@Override
	public void scanFile(XmlFile xmlFile) {
	    Document document = xmlFile.getDocument();
	    if (document.getDocumentElement() != null) {
    	
	    	String rootNodeName = document.getDocumentElement().getNodeName();
	    	if("Quota".equals(rootNodeName) ) {

			    XPathFactory xPathfactory = XPathFactory.newInstance();
			    XPath xpath = xPathfactory.newXPath();
			    
			    try {
			    	// Select in one shot the Distributed which are too short
			    	Node distributedNode = (Node)xpath.evaluate("/Quota/Distributed", document, XPathConstants.NODE);
	
			    	if(distributedNode==null) {
			    		// Report the issue at the file level
			    		reportIssue(document.getDocumentElement(), "Distributed quota is not enabled.");
			    	} else {
			    		String distributedValue = (String)xpath.evaluate("/Quota/Distributed/text()", document, XPathConstants.STRING);
			    		if(distributedValue==null || distributedValue.isEmpty() || !"true".equalsIgnoreCase(distributedValue)) {
			    			// Report the issue at the <Distributed> tag level
				    		reportIssue(distributedNode, "Distributed quota is not enabled.");
			    		}
			    	}
				} catch (XPathExpressionException e) {
					// Nothing to do
				}
	    	}
	    }		
	}
	
}
