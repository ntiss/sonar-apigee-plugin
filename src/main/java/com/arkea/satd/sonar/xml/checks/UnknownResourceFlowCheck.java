package com.arkea.satd.sonar.xml.checks;

import org.sonar.check.Rule;
import org.sonar.plugins.xml.checks.AbstractXmlCheck;
import org.sonar.plugins.xml.checks.XmlSourceCode;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

@Rule(key = "UnknownResourceFlowCheck")
public class UnknownResourceFlowCheck extends AbstractXmlCheck {

	@Override
	public void validate(XmlSourceCode xmlSourceCode) {
	    setWebSourceCode(xmlSourceCode);

	    Document document = getWebSourceCode().getDocument(false);
	    if (document.getDocumentElement() != null) {
	    	
	    	// Search for last Flow of an ProxyEndpoint document
	    	if("ProxyEndpoint".equals(document.getDocumentElement().getNodeName())) {

		    	NodeList flowsNodeList = document.getDocumentElement().getElementsByTagName("Flows");
		    	
		    	if(flowsNodeList != null && flowsNodeList.getLength()>0) {
		    		Node flowsNode = flowsNodeList.item(0);
		    		if(flowsNode != null) {
		    			// Go to the last defined flow
		    			Node lastFlowNode = flowsNode.getLastChild();

		    			// Loop to get rid of #text nodes
		    			int loop = 0;
		    			while(!"Flow".equals(lastFlowNode.getNodeName()) && loop < flowsNode.getChildNodes().getLength()) {
		    				lastFlowNode = lastFlowNode.getPreviousSibling();
		    			}
		    		
		    			// Search and test Condition value
		    			NodeList lastFlowChilds = lastFlowNode.getChildNodes();
		    			for(int i = 0; i < lastFlowChilds.getLength(); i++) {
		    				Node currentChild = lastFlowChilds.item(i);
		    				if("Condition".equals(currentChild.getNodeName())) {
		    					String cond = currentChild.getTextContent();
		    					if(!"true".equalsIgnoreCase(cond)) {
		    						createViolation(getWebSourceCode().getLineForNode(flowsNode.getNextSibling()) - 1, "There is no default flow in this proxy endpoint.");
		    					}
		    				}
		    			}
		    		}
		    	}
	    	}
	    }
	}

}
