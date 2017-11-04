/*
 * SonarQube XML Plugin
 * Copyright (C) 2010-2017 SonarSource SA
 * mailto:info AT sonarsource DOT com
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

import com.google.common.collect.ImmutableList;

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
				.add(UnknownResourceFlowCheck.class)
				.add(DescriptionCheck.class)
				.build();
	}
	
  /*
  
  public static List<Class<? extends AbstractXmlCheck>> getChecks() {
    return Arrays.asList(
//      new IllegalTabCheck(),
//      new IndentCheck(),
//      new NewlineCheck(),
//      new XmlSchemaCheck(),
//      new CharBeforePrologCheck(),
//      new ParsingErrorCheck(),
		new UnknownResourceFlowCheck(),
		new DescriptionCheck()
      );
  }
/*
	public static List<Class> getChecks() {
		return ImmutableList.<Class>builder().addAll(getJavaChecks()).addAll(getJavaTestChecks()).build();
	}

	public static List<Class<? extends JavaCheck>> getJavaChecks() {
		return ImmutableList.<Class<? extends JavaCheck>>builder()
				.add(MyFirstCustomCheck.class)
				.build();
	}  
  */
  /*
  public static List<Class> getCheckClasses() {
	return ImmutableList.<AbstractXmlCheck>builder().addAll(getChecks()).build();
    //return getChecks().stream().map(AbstractXmlCheck::getClass).collect(Collectors.toList());
	  
	  /*
	List<Class> list = new ArrayList<Class>();
	for(AbstractXmlCheck check : getChecks()) {
		list.add(check.getClass());
	}
	return list;
  }*/

}
