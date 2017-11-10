package com.arkea.satd.sonar.helpers;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLHelper {

	/**
	 * Helper to find the first child of a node
	 * @param parent
	 * @param tagName
	 * @return
	 */
	public static Node getFirstChildByTagName(Node parent, String tagName) {
		
		NodeList childNodeList = parent.getChildNodes();
		if(childNodeList!=null) {
			for(int i=0; i < childNodeList.getLength(); i++) {
				Node current = childNodeList.item(i);
				if(current.getNodeName().equals(tagName)) {
					return current;
				}
			}
		}	
		return null;
	}	
	
}
