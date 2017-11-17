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

import org.sonar.check.Rule;
import org.sonar.plugins.xml.checks.XmlSourceCode;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 * Definition : The Description tag should have more than 5 chars to be useful.
 * Code : BN500
 * @author Nicolas Tisserand
 */
@Rule(key = "DescriptionCheck")
public class DescriptionCheck extends AbstractXmlCheck {

	private static int MIN_LENGTH = 5; 
	
	@Override
	public void validate(XmlSourceCode xmlSourceCode) {
	    setWebSourceCode(xmlSourceCode);
	    
	    Document document = getWebSourceCode().getDocument(false);
	    if (document.getDocumentElement() != null) {
    	
	    	NodeList descriptionNodeList = document.getDocumentElement().getElementsByTagName("Description");
	    	
	    	if(descriptionNodeList!=null && descriptionNodeList.getLength() > 0) {
	    		for(int i=0 ; i < descriptionNodeList.getLength(); i++) {
	    	    	Node node = descriptionNodeList.item(i);
	    	    	String desc = node.getTextContent();
	    			if(desc == null || desc.isEmpty() || desc.length() <= MIN_LENGTH) {
	    				createViolation(getWebSourceCode().getLineForNode(node), "Description is too short.");
	    			}
	    		}
	    	}
	    }
	}

}
