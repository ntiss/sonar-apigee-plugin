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
		
	    if (document.getDocumentElement() != null && "ProxyEndpoint".equals(document.getDocumentElement().getNodeName())) {
			// ProxyEndpoint storage
	    	proxiesEndpoint.put(fileName, xmlSourceCode);
    	} else if (document.getDocumentElement() != null && "TargetEndpoint".equals(document.getDocumentElement().getNodeName())) {
			// TargetEndpoint storage
	    	targetsEndpoint.put(fileName, xmlSourceCode);
    	} else {
    		// Policy storage
    		policies.put(fileName, xmlSourceCode);
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
}
