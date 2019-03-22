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

import org.sonarsource.analyzer.commons.xml.XmlFile;
import org.w3c.dom.Document;

/**
 * This class records each files discovered in the bundle directory
 * @author Nicolas Tisserand
 */
public class BundleRecorder {

	private static Map<String, XmlFile> proxiesEndpoint = new HashMap<>();
	private static Map<String, XmlFile> targetsEndpoint = new HashMap<>();
	private static Map<String, XmlFile> resources = new HashMap<>();
	private static Map<String, XmlFile> policies = new HashMap<>();
	
	private BundleRecorder() {
		// Private constructor
	}
	
	public static void clear() {
			proxiesEndpoint.clear();
			targetsEndpoint.clear();
			resources.clear();
			policies.clear();
	}
	
	/**
	 * Stores the XmlFile in static Maps depending on their type
	 * @param xmlFile
	 */
	public static void storeFile(XmlFile xmlFile) {

		Document document = xmlFile.getDocument();
		String fileName = xmlFile.getInputFile().uri().getPath();
		//fileName = xmlFile.getInputFile().relativePath();
		
	    if (document.getDocumentElement() != null) {
	    	
	    	String rootNodeName = document.getDocumentElement().getNodeName();
	    	
	    	if("ProxyEndpoint".equals(rootNodeName)) {
				// ProxyEndpoint storage
		    	proxiesEndpoint.put(fileName, xmlFile);
	    	} else if ("TargetEndpoint".equals(rootNodeName)) {
				// TargetEndpoint storage
		    	targetsEndpoint.put(fileName, xmlFile);
	    	} else if ("APIProxy".equals(rootNodeName) || "Manifest".equals(rootNodeName)) {
				// APIProxy & Manifest storage
		    	// No need to store for the moment 
	    	} else if ("xsl:stylesheet".equals(rootNodeName) || "wsdl:definitions".equals(rootNodeName) || "xs:schema".equals(rootNodeName) ) {
				// Resource storage
	    		resources.put(fileName, xmlFile);
	    	} else {
	    		// Policy storage
	    		policies.put(fileName, xmlFile);
	    	}
	    }

	}
	
	
	/**
	 * Returns all XmlFile containing a Step with the Name stepName
	 * @param stepName
	 * @return
	 */
	public static List<XmlFile> searchByStepName(String stepName) {
		
		List<XmlFile> matchingXmlFile = new ArrayList<>();

		// Concat full list
		List<XmlFile> fullXmlFile = new ArrayList<>();
		fullXmlFile.addAll(proxiesEndpoint.values());
		fullXmlFile.addAll(targetsEndpoint.values());
		
		
		// Search for "stepName" in both ProxyEndpoint and TargetEndpoint
		for(XmlFile currentXml : fullXmlFile) {
			Document document = currentXml.getDocument();
			try {
			    XPathFactory xPathfactory = XPathFactory.newInstance();
			    XPath xpath = xPathfactory.newXPath();
				
				boolean hasMatchingStep = (boolean)xpath.evaluate("boolean( //Step/Name[text() = '"+stepName+"'])", document, XPathConstants.BOOLEAN);
			    if(hasMatchingStep) {
			    	matchingXmlFile.add(currentXml);
			    }
			} catch (XPathExpressionException e) {
				// Nothing to do
			}
		}
		
		return matchingXmlFile;
	}
	
	
	/**
	 * Returns all XmlFile containing a Policy of type policyType
	 * @param policyType
	 * @return
	 */
	public static List<XmlFile> searchPoliciesByType(String policyType) {
		
		List<XmlFile> matchingXmlFile = new ArrayList<>();
		
		for(XmlFile currentXml : policies.values()) {
			Document document = currentXml.getDocument();
			try {
			    XPathFactory xPathfactory = XPathFactory.newInstance();
			    XPath xpath = xPathfactory.newXPath();
				
				boolean isMatchingType = (boolean)xpath.evaluate("boolean(/"+policyType+")", document, XPathConstants.BOOLEAN);
			    if(isMatchingType) {
			    	matchingXmlFile.add(currentXml);
			    }
			} catch (XPathExpressionException e) {
				// Nothing to do
			}			
		}
		
		return matchingXmlFile;
	}
	
	/**
	 * Returns the XmlFile containing the Policy of name policyName
	 * Policy names are unique
	 * @param policyType
	 * @return
	 */
	public static XmlFile searchPoliciesByName(String policyName) {
		
		for(XmlFile currentXml : policies.values()) {
			Document document = currentXml.getDocument();
			try {
			    XPathFactory xPathfactory = XPathFactory.newInstance();
			    XPath xpath = xPathfactory.newXPath();
				
				String attrName = (String)xpath.evaluate("/*/@name", document, XPathConstants.STRING);
			    if(attrName!=null && attrName.equals(policyName)) {
			    	return currentXml;
			    }
			} catch (XPathExpressionException e) {
				// Nothing to do
			}			
		}
		
		return null;
	}
	
	/**
	 * Returns the XmlFile containing the TargetEndpoint of name targetName
	 * TargetEndpoint names are unique
	 * @param targetName
	 * @return
	 */
	public static XmlFile searchTargetEndpointByName(String targetName) {
		
		for(XmlFile currentXml : targetsEndpoint.values()) {
			Document document = currentXml.getDocument();
			try {
			    XPathFactory xPathfactory = XPathFactory.newInstance();
			    XPath xpath = xPathfactory.newXPath();
				
				String attrName = (String)xpath.evaluate("/*/@name", document, XPathConstants.STRING);
			    if(attrName!=null && attrName.equals(targetName)) {
			    	return currentXml;
			    }
			} catch (XPathExpressionException e) {
				// Nothing to do
			}			
		}
		
		return null;
	}			

	
	/**
	 * Returns the XmlFile list containing the policies having a link to the resourceURL
	 * @param resourceURL
	 * @return
	 */
	public static List<XmlFile> searchPoliciesByResourceURL(String resourceURL) {
		
		List<XmlFile> matchingXmlFile = new ArrayList<>();
		
		for(XmlFile currentXml : policies.values()) {
			Document document = currentXml.getDocument();
			try {
			    XPathFactory xPathfactory = XPathFactory.newInstance();
			    XPath xpath = xPathfactory.newXPath();
				
				String resourceLink = (String)xpath.evaluate("//ResourceURL", document, XPathConstants.STRING);

				// Test based only on the last part of the path
			    if(resourceURL.equals(resourceLink)) {
			    	matchingXmlFile.add(currentXml);
			    }

			} catch (XPathExpressionException e) {
				// Nothing to do
			}			
		}
		
		return matchingXmlFile;
	}
	
	
}
