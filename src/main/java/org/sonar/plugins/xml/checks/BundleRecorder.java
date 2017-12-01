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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;

import com.google.common.collect.Iterables;

/**
 * This class records each files discovered in the bundle directory
 * @author Nicolas Tisserand
 */
public class BundleRecorder {

	private static Map<String, XmlSourceCode> proxiesEndpoint = new HashMap<String, XmlSourceCode>();
	private static Map<String, XmlSourceCode> targetsEndpoint = new HashMap<String, XmlSourceCode>();
	private static Map<String, XmlSourceCode> policies = new HashMap<String, XmlSourceCode>();
	
	public static void clear() {
			proxiesEndpoint.clear();
			targetsEndpoint.clear();
			policies.clear();
	}
	
	/**
	 * Stores the xmlSourceCode in static Maps depending on their type
	 * @param xmlSourceCode
	 */
	public static void storeFile(XmlSourceCode xmlSourceCode) {

		// Force parsing at the beginning
		xmlSourceCode.parseSource();
		Document document = xmlSourceCode.getDocument(false);
		String fileName = xmlSourceCode.getInputFile().wrapped().relativePath();
		
	    if (document.getDocumentElement() != null) {
	    	
	    	String rootNodeName = document.getDocumentElement().getNodeName();
	    	
	    	if("ProxyEndpoint".equals(rootNodeName)) {
				// ProxyEndpoint storage
		    	proxiesEndpoint.put(fileName, xmlSourceCode);
	    	} else if ("TargetEndpoint".equals(rootNodeName)) {
				// TargetEndpoint storage
		    	targetsEndpoint.put(fileName, xmlSourceCode);
	    	} else if ("APIProxy".equals(rootNodeName)) {
				// APIProxy storage
		    	// No need to store for the moment 
	    	} else {
	    		// Policy storage
	    		policies.put(fileName, xmlSourceCode);
	    	}
	    }
	}
	
	
	/**
	 * Returns all XmlSourceCode containing a Step with the Name stepName
	 * @param stepName
	 * @return
	 */
	public static List<XmlSourceCode> searchByStepName(String stepName) {
		
		List<XmlSourceCode> matchingXmlSourceCode = new ArrayList<XmlSourceCode>();
		
		// Search for "stepName" in both ProxyEndpoint and TargetEndpoint
		for(XmlSourceCode currentXml : Iterables.concat(proxiesEndpoint.values(), targetsEndpoint.values())) {
			Document document = currentXml.getDocument(false);
			try {
			    XPathFactory xPathfactory = XPathFactory.newInstance();
			    XPath xpath = xPathfactory.newXPath();
				
				boolean hasMatchingStep = (boolean)xpath.evaluate("boolean( //Step/Name[text() = '"+stepName+"'])", document, XPathConstants.BOOLEAN);
			    if(hasMatchingStep) {
			    	matchingXmlSourceCode.add(currentXml);
			    }
			} catch (XPathExpressionException e) {
			}			
		}
		
		return matchingXmlSourceCode;
	}
	
	
	/**
	 * Returns all XmlSourceCode containing a Policy of type policyType
	 * @param policyType
	 * @return
	 */
	public static List<XmlSourceCode> searchPoliciesByType(String policyType) {
		
		List<XmlSourceCode> matchingXmlSourceCode = new ArrayList<XmlSourceCode>();
		
		for(XmlSourceCode currentXml : policies.values()) {
			Document document = currentXml.getDocument(false);
			try {
			    XPathFactory xPathfactory = XPathFactory.newInstance();
			    XPath xpath = xPathfactory.newXPath();
				
				boolean isMatchingType = (boolean)xpath.evaluate("boolean(/"+policyType+")", document, XPathConstants.BOOLEAN);
			    if(isMatchingType) {
			    	matchingXmlSourceCode.add(currentXml);
			    }
			} catch (XPathExpressionException e) {
			}			
		}
		
		return matchingXmlSourceCode;
	}
	
	/**
	 * Returns the XmlSourceCode containing the Policy of name policyName
	 * Policy names are unique
	 * @param policyType
	 * @return
	 */
	public static XmlSourceCode searchPoliciesByName(String policyName) {
		
		for(XmlSourceCode currentXml : policies.values()) {
			Document document = currentXml.getDocument(false);
			try {
			    XPathFactory xPathfactory = XPathFactory.newInstance();
			    XPath xpath = xPathfactory.newXPath();
				
				String attrName = (String)xpath.evaluate("/*/@name", document, XPathConstants.STRING);
			    if(policyName!=null && policyName.equals(attrName)) {
			    	return currentXml;
			    }
			} catch (XPathExpressionException e) {
			}			
		}
		
		return null;
	}		
	
}
