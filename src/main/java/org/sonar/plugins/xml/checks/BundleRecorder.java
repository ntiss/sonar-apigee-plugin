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
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;

/**
 * This class records each files discovered in the bundle directory
 * @author Nicolas Tisserand
 */
public class BundleRecorder {

	private static Map<String, XmlSourceCode> storage = new HashMap<String, XmlSourceCode>();
	private static Map<String, XmlSourceCode> proxiesEndpoint = new HashMap<String, XmlSourceCode>();
	
	public static void clear() {
			storage.clear();
			proxiesEndpoint.clear();
	}
	
	
	public static void storeFile(XmlSourceCode xmlSourceCode) {
		
		// Full storage
		String fileName = xmlSourceCode.getInputFile().wrapped().relativePath();
		storage.put(fileName, xmlSourceCode);
		
		// ProxyEndpoint storage
		xmlSourceCode.parseSource();
		Document document = xmlSourceCode.getDocument(false);
	    if (document.getDocumentElement() != null && "ProxyEndpoint".equals(document.getDocumentElement().getNodeName())) {
	    	proxiesEndpoint.put(fileName, xmlSourceCode);
    	}
	}
	
	
	/**
	 * Returns all XmlSourceCode containing a Step with the Name stepName
	 * @param stepName
	 * @return
	 */
	public static List<XmlSourceCode> searchByStepName(String stepName) {
		
		List<XmlSourceCode> matchingXmlSourceCode = new ArrayList<XmlSourceCode>();
		
		for(XmlSourceCode currentXml : proxiesEndpoint.values()) {
			
			Document document = currentXml.getDocument(false);
			try {
			    XPathFactory xPathfactory = XPathFactory.newInstance();
			    XPath xpath = xPathfactory.newXPath();
				
				XPathExpression exprName = xpath.compile("boolean( //Step/Name[text() = '"+stepName+"'])");
				boolean hasMatchingStep = (boolean)exprName.evaluate(document, XPathConstants.BOOLEAN);
			    if(hasMatchingStep) {
			    	matchingXmlSourceCode.add(currentXml);
			    }
			} catch (XPathExpressionException e) {
			}			
		}
		
		return matchingXmlSourceCode;
		
		
	}

		
		
	
}
