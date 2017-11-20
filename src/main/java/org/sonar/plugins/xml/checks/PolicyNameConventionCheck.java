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

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.sonar.check.Rule;
import org.sonar.plugins.xml.checks.XmlSourceCode;
import org.w3c.dom.Document;
import org.w3c.dom.Node;


/**
 * Policy Naming Conventions - type indication
 * It is recommended that the policy name include an indicator of the policy type.
 * Code : PO007
 * @author Nicolas Tisserand
 */
@Rule(key = "PolicyNameConventionCheck")
public class PolicyNameConventionCheck extends AbstractXmlCheck {

	/**
	 * Definition of supported policies, and naming convention
	 */
	private static Map<String, List<String>> supportedPolicies = new HashMap<String, List<String>>();
	static {
		supportedPolicies.put("AccessControl", Arrays.asList("accesscontrol", "ac", "accessc") );
		supportedPolicies.put("AccessEntity", Arrays.asList("accessentity", "ae", "accesse") );
		supportedPolicies.put("AssignMessage", Arrays.asList("assignmessage", "am", "assign", "build", "set", "response", "send", "add") );
		supportedPolicies.put("BasicAuthentication", Arrays.asList("encode", "basicauth", "ba", "auth") );
		supportedPolicies.put("InvalidateCache", Arrays.asList("invalidatecache", "invalidate", "ic", "cache") );
		supportedPolicies.put("LookupCache", Arrays.asList("lookup", "lu", "lucache", "cache", "lc") );
		supportedPolicies.put("PopulateCache", Arrays.asList("populate", "pop", "populatecache", "pc", "cache") );
		supportedPolicies.put("ResponseCache", Arrays.asList("responsecache", "rc", "cache") );
		supportedPolicies.put("ExtractVariables", Arrays.asList("extract", "ev", "vars") );
		supportedPolicies.put("FlowCallout", Arrays.asList("flowcallout", "flow", "fc") );
		supportedPolicies.put("JavaCallout", Arrays.asList("javacallout", "java", "javac") );
		supportedPolicies.put("Javascript", Arrays.asList("jsc", "js", "javascript") );
		supportedPolicies.put("JSONThreatProtection", Arrays.asList("jsonthreat", "threat", "jtp", "tp") );
		supportedPolicies.put("JSONToXML", Arrays.asList("jsontoxml", "j2x", "jtox") );
		supportedPolicies.put("KeyValueMapOperations", Arrays.asList("keyvaluemapoperations", "kvm", "kvmops") );
		supportedPolicies.put("Ldap", Arrays.asList("ldap") );
		supportedPolicies.put("MessageLogging", Arrays.asList("messagelogging", "logging", "ml") );
		supportedPolicies.put("MessageValidation", Arrays.asList("messagevalidation", "mv", "messval") );
		supportedPolicies.put("OAuthV1", Arrays.asList("oauthv1", "oauth", "oa", "accesstoken", "verify") );
		supportedPolicies.put("OAuthV2", Arrays.asList("oauthv2", "oauth", "oa", "accesstoken", "verify") );
		supportedPolicies.put("GetOAuthV1Info", Arrays.asList("oauthv1", "getoauth", "getoa") );
		supportedPolicies.put("GetOAuthV2Info", Arrays.asList("oauthv2info", "oauthinfo", "oai", "accesstoken") );
		supportedPolicies.put("VerifyAPIKey", Arrays.asList("verifyapikey", "apikey", "va", "verify") );
		supportedPolicies.put("SpikeArrest", Arrays.asList("spikearrest", "spike", "sa") );
		supportedPolicies.put("RaiseFault", Arrays.asList("raisefault", "rf", "fault") );
		supportedPolicies.put("RegularExpressionProtection", Arrays.asList("regex", "re", "tp") );
		supportedPolicies.put("GenerateSAMLAssertion", Arrays.asList("saml", "sa") );
		supportedPolicies.put("Quota", Arrays.asList("quota", "q") );
		supportedPolicies.put("Script", Arrays.asList("script", "scr") );
		supportedPolicies.put("ServiceCallout", Arrays.asList("callout", "sc") );
		supportedPolicies.put("StatisticsCollector", Arrays.asList("stats", "statcoll") );
		supportedPolicies.put("XMLThreatProtection", Arrays.asList("xmltp", "tp") );
		supportedPolicies.put("XMLToJSON", Arrays.asList("xmltojson", "x2j", "xtoj") );
		supportedPolicies.put("XSL", Arrays.asList("xsl") );				
	}
	
	
	@Override
	public void validate(XmlSourceCode xmlSourceCode) {
	    setWebSourceCode(xmlSourceCode);

	    Document document = getWebSourceCode().getDocument(false);
	    Node rootNode = document.getDocumentElement();
	    if (rootNode != null) {
	    	
	    	String policyName = rootNode.getNodeName();
	    	
	    	if(supportedPolicies.containsKey(policyName)) {
	    		
	    		String nameAttr = rootNode.getAttributes().getNamedItem("name").getNodeValue();
	    		List<String> knownPrefixes = supportedPolicies.get(policyName);

	    		boolean isCompliant = false;
	    		for(String prefix : knownPrefixes) {
	    			Pattern pattern = Pattern.compile(prefix + "[_-]?.*", Pattern.CASE_INSENSITIVE);
	    			Matcher  matcher = pattern.matcher(nameAttr);
	    			
	    			if(matcher.matches()) {
	    				isCompliant = true;
	    				break;
	    			}
	    		}
	    		
	    		if(!isCompliant) {
    				// Create a violation for the root node
    				createViolation(getWebSourceCode().getLineForNode(rootNode), "Policy " + policyName + " should have an indicative prefix. Typical prefixes include : " + knownPrefixes);
	    		}
	    	}
	    }
	}

}
