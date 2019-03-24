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

import java.util.List;

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
 * Cache Coherence
 * Code : BN003
 * @author Nicolas Tisserand
 */
@Rule(key = "CacheCoherenceCheck")
public class CacheCoherenceCheck extends SonarXmlCheck {

	@Override
	public void scanFile(XmlFile xmlFile) {
		
	    Document document = xmlFile.getDocument();
	    
	    checkIt(document, "PopulateCache", "LookupCache");
	    checkIt(document, "LookupCache", "PopulateCache");
	}
	
	
	private void checkIt(Document type1Document, String type1, String type2) {

	    if (type1Document.getDocumentElement() != null && type1.equals(type1Document.getDocumentElement().getNodeName())) {
	    	
		    XPathFactory xPathfactory = XPathFactory.newInstance();
		    XPath xpath = xPathfactory.newXPath();
		    
		    try {
		    	// Select CacheKey Node 
		    	Node cacheNode1 = (Node)xpath.evaluate( "//CacheKey",type1Document, XPathConstants.NODE);		        
		        String compositeKey1 = computeKey(xpath, cacheNode1);
		        
		        // Now search for a LookupCache policy
		        List<XmlFile> type2Policies = BundleRecorder.searchPoliciesByType(type2);

	        	boolean hasMatchingKey = false;
	        	for(XmlFile type2XmlFile : type2Policies) {
	        		
	        		Document type2Document = type2XmlFile.getDocument();
			    	Node cacheNode2 = (Node)xpath.evaluate( "//CacheKey",type2Document, XPathConstants.NODE);		        
			        String compositeKey2 = computeKey(xpath, cacheNode2);
			        
			        if(compositeKey1.equals(compositeKey2)) {
			        	hasMatchingKey = true;
			        }
	        	}
	        	
        		// Violation
	        	if(!hasMatchingKey) {
	        		reportIssue(cacheNode1, type1 + " may not have a corresponding "+ type2 + ".");
	        	}
		        
			} catch (XPathExpressionException e) {
				// Nothing to do
			}
	    }
		
		
		
		
		
		
	}


	private String computeKey(XPath xpath, Node cacheNode) throws XPathExpressionException {
        String prefix = (String)xpath.evaluate( "./Prefix/text()", cacheNode, XPathConstants.STRING);
        String keyFragmentRef = (String)xpath.evaluate( "./KeyFragment/@ref/text()", cacheNode, XPathConstants.STRING);
        String keyFragment = (String)xpath.evaluate( "./KeyFragment/text()", cacheNode, XPathConstants.STRING);
        
        // Composite key
       return (prefix==null?"":prefix) + "-" + (keyFragmentRef==null?"":keyFragmentRef) + "-" + (keyFragment==null?"":keyFragment);

	}
	
}
