/*
 * SonarQube Python Plugin
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
package com.arkea.satd.sonar.python.checks;

import java.util.List;

import org.sonar.python.PythonCheck;

import com.google.common.collect.ImmutableList;

public class CheckRepository {

	  public static final String REPOSITORY_KEY = "apigee-python";
	  public static final String REPOSITORY_NAME = "Apigee Python";
	
	
  private CheckRepository() {
  }

	public static List<Class> getChecks() {
		return ImmutableList.<Class>builder().addAll(getClassesChecks()).build();
	}
  
	public static List<Class<? extends PythonCheck>> getClassesChecks() {
		return ImmutableList.<Class<? extends PythonCheck>>builder()
				.add(FileExistsCheck.class)
				.build();
	}

}
