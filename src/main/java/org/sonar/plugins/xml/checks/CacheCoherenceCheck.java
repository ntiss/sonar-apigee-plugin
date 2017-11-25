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

import java.util.List;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.sonar.check.Rule;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * Cache Coherence
 * Code : BN003
 * @author Nicolas Tisserand
 */
@Rule(key = "CacheCoherenceCheck")
public class CacheCoherenceCheck extends AbstractXmlCheck {

	@Override
	public void validate(XmlSourceCode xmlSourceCode) {
	    setWebSourceCode(xmlSourceCode);
	    Document document = getWebSourceCode().getDocument(false);
	    
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
		        List<XmlSourceCode> type2Policies = BundleRecorder.searchPoliciesByType(type2);

	        	boolean hasMatchingKey = false;
	        	for(XmlSourceCode type2XmlSourceCode : type2Policies) {
	        		
	        		Document type2Document = type2XmlSourceCode.getDocument(false);
			    	Node cacheNode2 = (Node)xpath.evaluate( "//CacheKey",type2Document, XPathConstants.NODE);		        
			        String compositeKey2 = computeKey(xpath, cacheNode2);
			        
			        if(compositeKey1.equals(compositeKey2)) {
			        	hasMatchingKey = true;
			        }
	        	}
	        	
        		// Violation
	        	if(!hasMatchingKey) {
	        		createViolation(getWebSourceCode().getLineForNode(cacheNode1), type1 + " may not have a corresponding "+ type2 + ".");
	        	}
		        
			} catch (XPathExpressionException e) {
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
