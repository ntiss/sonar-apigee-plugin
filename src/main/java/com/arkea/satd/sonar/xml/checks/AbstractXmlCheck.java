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

import java.io.IOException;

import org.sonar.plugins.xml.parsers.SaxParser;
import org.w3c.dom.Document;

/**
 * The only purpose of this class is to provide the method getDocument(boolean) 
 * because org.sonar.plugins.xml.checks.XmlSourceCode.getDocument(boolean) is protected and cannot be used in custom checks
 * Disadvantage : the inputStrean is (probably) parsed a second time.
 * @author Nicolas Tisserand
 *
 */
public abstract class AbstractXmlCheck extends org.sonar.plugins.xml.checks.AbstractXmlCheck {

	protected Document getDocument(boolean namespaceAware) {
		try {
			return new SaxParser().parseDocument(getWebSourceCode().getInputFile().inputStream(), namespaceAware);
		} catch (IOException e) {
			return null;
		}
	}
	
}
