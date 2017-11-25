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
package com.arkea.satd.sonar.xml.checks;

import java.util.List;

import org.sonar.plugins.xml.checks.AbstractXmlCheck;
import org.sonar.plugins.xml.checks.AvoidPythonCheck;
import org.sonar.plugins.xml.checks.ConditionLengthCheck;
import org.sonar.plugins.xml.checks.DescriptionCheck;
import org.sonar.plugins.xml.checks.EmptyRouteRuleLastCheck;
import org.sonar.plugins.xml.checks.EmptyStepCheck;
import org.sonar.plugins.xml.checks.ExtractVariablesCheck;
import org.sonar.plugins.xml.checks.FaultRuleConditionCheck;
import org.sonar.plugins.xml.checks.PolicyDisplayNameCheck;
import org.sonar.plugins.xml.checks.PolicyNameConventionCheck;
import org.sonar.plugins.xml.checks.RegexLookAroundCheck;
import org.sonar.plugins.xml.checks.ServiceCalloutRequestVariableNameCheck;
import org.sonar.plugins.xml.checks.ServiceCalloutResponseVariableNameCheck;
import org.sonar.plugins.xml.checks.ThreatProtectionCheck;
import org.sonar.plugins.xml.checks.UnconditionalFlowCheck;
import org.sonar.plugins.xml.checks.UnknownResourceFlowCheck;
import org.sonar.plugins.xml.checks.UnreachableFlowCheck;
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

	public static List<Class> getChecks() {
		return ImmutableList.<Class>builder().addAll(getClassesChecks()).build();
	}
  
	public static List<Class<? extends AbstractXmlCheck>> getClassesChecks() {
		return ImmutableList.<Class<? extends AbstractXmlCheck>>builder()
				.add(AvoidPythonCheck.class)
				.add(ConditionLengthCheck.class)
				.add(DescriptionCheck.class)
				.add(EmptyRouteRuleLastCheck.class)
				.add(EmptyStepCheck.class)
				.add(ExtractVariablesCheck.class)
				.add(FaultRuleConditionCheck.class)
				.add(PolicyDisplayNameCheck.class)
				.add(PolicyNameConventionCheck.class)
				.add(RegexLookAroundCheck.class)
				.add(ServiceCalloutRequestVariableNameCheck.class)
				.add(ServiceCalloutResponseVariableNameCheck.class)
				.add(ThreatProtectionCheck.class)
				.add(UnconditionalFlowCheck.class)
				.add(UnknownResourceFlowCheck.class)
				.add(UnreachableFlowCheck.class)
				.add(UseManagementServerCheck.class)
				.add(UseTargetServersCheck.class)
				
				.build();
	}
	


}
