package com.arkea.satd.sonar.xml.checks;

import org.sonar.check.Rule;
import org.sonar.plugins.xml.checks.AbstractXmlCheck;
import org.sonar.plugins.xml.checks.XmlSourceCode;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

@Rule(key = "DescriptionCheck")
public class DescriptionCheck extends AbstractXmlCheck {

	
	@Override
	public void validate(XmlSourceCode xmlSourceCode) {
	    setWebSourceCode(xmlSourceCode);
	    
	    Document document = getWebSourceCode().getDocument(false);
	    if (document.getDocumentElement() != null) {
	    	Node node = document.getDocumentElement().getFirstChild();
    	
	    	boolean found = false;
	    	while(node!=null && !found) {
	    		if("Description".equals(node.getNodeName())) {
	    			String desc = node.getTextContent();
	    			if(desc == null || desc.isEmpty() || desc.length() <= 5) {
	    				createViolation(getWebSourceCode().getLineForNode(node), "Description is too short.");
	    			}
	    			
	    			found = true;
	    		} else {
	    			node = node.getNextSibling();
	    		}
	    	}
	    }
	}

}
