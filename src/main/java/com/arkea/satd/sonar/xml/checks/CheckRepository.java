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
package com.arkea.satd.sonar.xml.checks;

import java.util.Arrays;
import java.util.List;

import org.sonar.plugins.xml.checks.AvoidPythonCheck;
import org.sonar.plugins.xml.checks.CacheCoherenceCheck;
import org.sonar.plugins.xml.checks.ConditionLengthCheck;
import org.sonar.plugins.xml.checks.DescriptionCheck;
import org.sonar.plugins.xml.checks.DescriptionPatternCheck;
import org.sonar.plugins.xml.checks.EmptyRouteRuleLastCheck;
import org.sonar.plugins.xml.checks.EmptyStepCheck;
import org.sonar.plugins.xml.checks.ExtractVariablesCheck;
import org.sonar.plugins.xml.checks.FaultRuleConditionCheck;
import org.sonar.plugins.xml.checks.PolicyDisplayNameCheck;
import org.sonar.plugins.xml.checks.PolicyNameConventionCheck;
import org.sonar.plugins.xml.checks.RegexLookAroundCheck;
import org.sonar.plugins.xml.checks.RouteRulesToTargetCheck;
import org.sonar.plugins.xml.checks.ServiceCalloutRequestVariableNameCheck;
import org.sonar.plugins.xml.checks.ServiceCalloutResponseVariableNameCheck;
import org.sonar.plugins.xml.checks.ThreatProtectionCheck;
import org.sonar.plugins.xml.checks.TooMuchProxyEndpointsCheck;
import org.sonar.plugins.xml.checks.TooMuchTargetEndpointsCheck;
import org.sonar.plugins.xml.checks.UnattachedPolicyCheck;
import org.sonar.plugins.xml.checks.UnattachedResourceCheck;
import org.sonar.plugins.xml.checks.UnconditionalFlowCheck;
import org.sonar.plugins.xml.checks.UnknownResourceFlowCheck;
import org.sonar.plugins.xml.checks.UnreachableFlowCheck;
import org.sonar.plugins.xml.checks.UnreachableRouteRuleCheck;
import org.sonar.plugins.xml.checks.UseFaultRulesCheck;
import org.sonar.plugins.xml.checks.UseManagementServerCheck;
import org.sonar.plugins.xml.checks.UseTargetServersCheck;


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
		return Arrays.asList(
				AvoidPythonCheck.class,
				CacheCoherenceCheck.class,
				ConditionLengthCheck.class,
				DescriptionCheck.class,
				DescriptionPatternCheck.class,
				EmptyRouteRuleLastCheck.class,
				EmptyStepCheck.class,
				ExtractVariablesCheck.class,
				FaultRuleConditionCheck.class,
				PolicyDisplayNameCheck.class,
				PolicyNameConventionCheck.class,
				RegexLookAroundCheck.class,
				RouteRulesToTargetCheck.class,
				ServiceCalloutRequestVariableNameCheck.class,
				ServiceCalloutResponseVariableNameCheck.class,
				ThreatProtectionCheck.class,
				TooMuchProxyEndpointsCheck.class,
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
