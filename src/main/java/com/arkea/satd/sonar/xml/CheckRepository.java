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
package com.arkea.satd.sonar.xml;

import java.util.Arrays;
import java.util.List;

import com.arkea.satd.sonar.xml.checks.AvoidPythonCheck;
import com.arkea.satd.sonar.xml.checks.CacheCoherenceCheck;
import com.arkea.satd.sonar.xml.checks.ConditionLengthCheck;
import com.arkea.satd.sonar.xml.checks.DescriptionCheck;
import com.arkea.satd.sonar.xml.checks.DescriptionPatternCheck;
import com.arkea.satd.sonar.xml.checks.EmptyRouteRuleLastCheck;
import com.arkea.satd.sonar.xml.checks.EmptyStepCheck;
import com.arkea.satd.sonar.xml.checks.ExtractVariablesCheck;
import com.arkea.satd.sonar.xml.checks.FaultRuleAndDefaultFaultRuleCheck;
import com.arkea.satd.sonar.xml.checks.FaultRuleConditionCheck;
import com.arkea.satd.sonar.xml.checks.NondistributedQuotaCheck;
import com.arkea.satd.sonar.xml.checks.PolicyDisplayNameCheck;
import com.arkea.satd.sonar.xml.checks.PolicyNameConventionCheck;
import com.arkea.satd.sonar.xml.checks.RegexLookAroundCheck;
import com.arkea.satd.sonar.xml.checks.RouteRulesToTargetCheck;
import com.arkea.satd.sonar.xml.checks.ServiceCalloutRequestVariableNameCheck;
import com.arkea.satd.sonar.xml.checks.ServiceCalloutResponseVariableNameCheck;
import com.arkea.satd.sonar.xml.checks.ThreatProtectionCheck;
import com.arkea.satd.sonar.xml.checks.TooMuchPoliciesCheck;
import com.arkea.satd.sonar.xml.checks.TooMuchProxyEndpointsCheck;
import com.arkea.satd.sonar.xml.checks.TooMuchResourcesCheck;
import com.arkea.satd.sonar.xml.checks.TooMuchTargetEndpointsCheck;
import com.arkea.satd.sonar.xml.checks.UnattachedPolicyCheck;
import com.arkea.satd.sonar.xml.checks.UnattachedResourceCheck;
import com.arkea.satd.sonar.xml.checks.UnconditionalFlowCheck;
import com.arkea.satd.sonar.xml.checks.UnknownResourceFlowCheck;
import com.arkea.satd.sonar.xml.checks.UnreachableFlowCheck;
import com.arkea.satd.sonar.xml.checks.UnreachableRouteRuleCheck;
import com.arkea.satd.sonar.xml.checks.UseFaultRulesCheck;
import com.arkea.satd.sonar.xml.checks.UseManagementServerCheck;
import com.arkea.satd.sonar.xml.checks.UseTargetServersCheck;


/**
 * @author Nicolas Tisserand
 */
public class CheckRepository {

	public static final String REPOSITORY_KEY = "apigee-xml";
	public static final String REPOSITORY_NAME = "Apigee XML";

	private CheckRepository() {
	}

	@SuppressWarnings("rawtypes")
	public static List<Class> getCheckClasses() {
		
		// IMPORTANT !! 
		// If you add a new check class here, don't forget to add it also in src/main/resources/org/sonar/l10n/xml/rules/xml/__Sonar_way_apigee_profile.json
		//
		return Arrays.asList(
				AvoidPythonCheck.class,
				CacheCoherenceCheck.class,
				ConditionLengthCheck.class,
				DescriptionCheck.class,
				DescriptionPatternCheck.class,
				EmptyRouteRuleLastCheck.class,
				EmptyStepCheck.class,
				ExtractVariablesCheck.class,
				FaultRuleAndDefaultFaultRuleCheck.class,
				FaultRuleConditionCheck.class,
				NondistributedQuotaCheck.class,
				PolicyDisplayNameCheck.class,
				PolicyNameConventionCheck.class,
				RegexLookAroundCheck.class,
				RouteRulesToTargetCheck.class,
				ServiceCalloutRequestVariableNameCheck.class,
				ServiceCalloutResponseVariableNameCheck.class,
				ThreatProtectionCheck.class,
				TooMuchPoliciesCheck.class,
				TooMuchProxyEndpointsCheck.class,
				TooMuchResourcesCheck.class,
				TooMuchTargetEndpointsCheck.class,
				UnattachedPolicyCheck.class,
				UnattachedResourceCheck.class,
				UnconditionalFlowCheck.class,
				UnknownResourceFlowCheck.class,
				UnreachableFlowCheck.class,
				UnreachableRouteRuleCheck.class,
				UseFaultRulesCheck.class,
				UseManagementServerCheck.class,
				UseTargetServersCheck.class
			);
	}	
	
}
