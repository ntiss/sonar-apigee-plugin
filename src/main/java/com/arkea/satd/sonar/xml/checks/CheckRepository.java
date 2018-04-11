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
import java.util.stream.Collectors;

import org.sonar.plugins.xml.checks.AbstractXmlCheck;
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
import org.sonar.plugins.xml.checks.UnattachedPolicyCheck;
import org.sonar.plugins.xml.checks.UnconditionalFlowCheck;
import org.sonar.plugins.xml.checks.UnknownResourceFlowCheck;
import org.sonar.plugins.xml.checks.UnreachableFlowCheck;
import org.sonar.plugins.xml.checks.UnreachableRouteRuleCheck;
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
	public static List<Class> getChecks() {
		return getClassesChecks().stream().map(AbstractXmlCheck::getClass).collect(Collectors.toList());
	}
  
	public static List<AbstractXmlCheck> getClassesChecks() {
		return Arrays.asList(
			      new AvoidPythonCheck(),
			      new CacheCoherenceCheck(),
			      new ConditionLengthCheck(),
			      new DescriptionCheck(),
			      new DescriptionPatternCheck(),
			      new EmptyRouteRuleLastCheck(),
			      new EmptyStepCheck(),
			      new ExtractVariablesCheck(),
			      new FaultRuleConditionCheck(),
			      new PolicyDisplayNameCheck(),
			      new PolicyNameConventionCheck(),
			      new RegexLookAroundCheck(),
			      new RouteRulesToTargetCheck(),
			      new ServiceCalloutRequestVariableNameCheck(),
			      new ServiceCalloutResponseVariableNameCheck(),
			      new ThreatProtectionCheck(),
			      new UnattachedPolicyCheck(),
			      new UnconditionalFlowCheck(),
			      new UnknownResourceFlowCheck(),
			      new UnreachableFlowCheck(),
			      new UnreachableRouteRuleCheck(),
			      new UseManagementServerCheck(),
			      new UseTargetServersCheck()
			);
	}
}
