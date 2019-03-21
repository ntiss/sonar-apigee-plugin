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

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.sonar.check.Rule;
import org.sonarsource.analyzer.commons.xml.XmlFile;
import org.sonarsource.analyzer.commons.xml.checks.SonarXmlCheck;
import org.w3c.dom.Document;
import org.w3c.dom.Node;


/**
 * Policy Naming Conventions - type indication
 * It is recommended that the policy name include an indicator of the policy type.
 * Code : PO007
 * @author Nicolas Tisserand
 */
@Rule(key = "PolicyNameConventionCheck")
@SuppressWarnings("squid:S1192")
public class PolicyNameConventionCheck extends SonarXmlCheck {

	/**
	 * Definition of supported policies, and naming convention
	 */
	private static Map<String, List<String>> supportedPolicies = new HashMap<>();

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
		supportedPolicies.put("SharedFlow", Arrays.asList("sf") );
		supportedPolicies.put("StatisticsCollector", Arrays.asList("stats", "statcoll") );
		supportedPolicies.put("XMLThreatProtection", Arrays.asList("xmltp", "tp") );
		supportedPolicies.put("XMLToJSON", Arrays.asList("xmltojson", "x2j", "xtoj") );
		supportedPolicies.put("XSL", Arrays.asList("xsl") );				
		supportedPolicies.put("GenerateJWT", Arrays.asList("jwt") );
		supportedPolicies.put("DecodeJWT", Arrays.asList("jwt") );
		supportedPolicies.put("VerifyJWT", Arrays.asList("jwt") );

	}
	
	
	@Override
	public void scanFile(XmlFile xmlFile) {
		
	    Document document = xmlFile.getDocument();
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
    				reportIssue(rootNode, "Policy " + policyName + " should have an indicative prefix. Typical prefixes include : " + knownPrefixes);
	    		}
	    	}
	    }
	}

}
