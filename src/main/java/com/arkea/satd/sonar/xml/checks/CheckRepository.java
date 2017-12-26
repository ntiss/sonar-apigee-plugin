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

import java.util.List;

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

import com.google.common.collect.ImmutableList;

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
		return ImmutableList.<Class>builder().addAll(getClassesChecks()).build();
	}
  
	public static List<Class<? extends AbstractXmlCheck>> getClassesChecks() {
		return ImmutableList.<Class<? extends AbstractXmlCheck>>builder()
				.add(AvoidPythonCheck.class)
				.add(CacheCoherenceCheck.class)
				.add(ConditionLengthCheck.class)
				.add(DescriptionCheck.class)
				.add(DescriptionPatternCheck.class)
				.add(EmptyRouteRuleLastCheck.class)
				.add(EmptyStepCheck.class)
				.add(ExtractVariablesCheck.class)
				.add(FaultRuleConditionCheck.class)
				.add(PolicyDisplayNameCheck.class)
				.add(PolicyNameConventionCheck.class)
				.add(RegexLookAroundCheck.class)
				.add(RouteRulesToTargetCheck.class)
				.add(ServiceCalloutRequestVariableNameCheck.class)
				.add(ServiceCalloutResponseVariableNameCheck.class)
				.add(ThreatProtectionCheck.class)
				.add(UnattachedPolicyCheck.class)
				.add(UnconditionalFlowCheck.class)
				.add(UnknownResourceFlowCheck.class)
				.add(UnreachableFlowCheck.class)
				.add(UnreachableRouteRuleCheck.class)
				.add(UseManagementServerCheck.class)
				.add(UseTargetServersCheck.class)
				
				.build();
	}
}
