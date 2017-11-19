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
package com.arkea.satd.sonar.xml;

import static org.sonar.plugins.xml.compat.CompatibilityHelper.wrap;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang.StringUtils;
import org.sonar.api.batch.fs.FilePredicate;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.rule.CheckFactory;
import org.sonar.api.batch.rule.Checks;
import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.api.batch.sensor.highlighting.NewHighlighting;
import org.sonar.api.batch.sensor.issue.NewIssue;
import org.sonar.api.batch.sensor.issue.NewIssueLocation;
import org.sonar.api.measures.FileLinesContextFactory;
import org.sonar.api.rule.RuleKey;
import org.sonar.api.utils.AnnotationUtils;
import org.sonar.api.utils.Version;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.plugins.xml.checks.AbstractXmlCheck;
import org.sonar.plugins.xml.checks.BundleRecorder;
import org.sonar.plugins.xml.checks.ParsingErrorCheck;
import org.sonar.plugins.xml.checks.XmlFile;
import org.sonar.plugins.xml.checks.XmlIssue;
import org.sonar.plugins.xml.checks.XmlSourceCode;
import org.sonar.plugins.xml.compat.CompatibleInputFile;
import org.sonar.plugins.xml.highlighting.HighlightingData;
import org.sonar.plugins.xml.highlighting.XMLHighlighting;
import org.sonar.plugins.xml.language.Xml;
import org.sonar.plugins.xml.parsers.ParseException;
import org.sonar.squidbridge.api.AnalysisException;
import org.w3c.dom.Node;

import com.arkea.satd.sonar.xml.checks.CheckRepository;
import com.google.common.annotations.VisibleForTesting;

/**
 * ApigeeXmlSensor provides analysis of xml files.
 * 
 * @author Matthijs Galesloot
 * @author Nicolas Tisserand
 */
public class ApigeeXmlSensor implements Sensor {

	private static final Version V6_0 = Version.create(6, 0);

	/**
	 * Use Sonar logger instead of SL4FJ logger, in order to be able to unit test
	 * the logs.
	 */
	private static final Logger LOG = Loggers.get(ApigeeXmlSensor.class);

	private final Checks<Object> checks;
	private final FileSystem fileSystem;
	private final FilePredicate mainFilesPredicate;
	private final FileLinesContextFactory fileLinesContextFactory;

	private static SensorContext CONTEXT;

	private static String annotatedEngineKey(Object annotatedClassOrObject) {
		String key = null;
		org.sonar.check.Rule ruleAnnotation = AnnotationUtils.getAnnotation(annotatedClassOrObject,
				org.sonar.check.Rule.class);
		if (ruleAnnotation != null) {
			key = ruleAnnotation.key();
		}
		Class clazz = annotatedClassOrObject.getClass();
		if (annotatedClassOrObject instanceof Class) {
			clazz = (Class) annotatedClassOrObject;
		}
		return StringUtils.defaultIfEmpty(key, clazz.getCanonicalName());
	}

	public ApigeeXmlSensor(FileSystem fileSystem, CheckFactory checkFactory,
			FileLinesContextFactory fileLinesContextFactory) {
		this.fileLinesContextFactory = fileLinesContextFactory;
		this.checks = checkFactory.create(CheckRepository.REPOSITORY_KEY)
				.addAnnotatedChecks((Iterable<?>) CheckRepository.getChecks());
		this.fileSystem = fileSystem;
		this.mainFilesPredicate = fileSystem.predicates().and(fileSystem.predicates().hasType(InputFile.Type.MAIN),
				fileSystem.predicates().hasLanguage(Xml.KEY));
	}

	public void analyse(SensorContext sensorContext) {
		execute(sensorContext);
	}

	private void runChecks(SensorContext context, XmlFile xmlFile) {
		XmlSourceCode sourceCode = new XmlSourceCode(xmlFile);

		// Do not execute any XML rule when an XML file is corrupted (SONARXML-13)
		if (sourceCode.parseSource()) {
			for (Object check : checks.all()) {
				((AbstractXmlCheck) check).setRuleKey(checks.ruleKey(check));
				((AbstractXmlCheck) check).validate(sourceCode);
			}
			saveIssue(context, sourceCode);
			try {
				saveSyntaxHighlighting(context, new XMLHighlighting(xmlFile).getHighlightingData(),
						xmlFile.getInputFile().wrapped());
			} catch (IOException e) {
				throw new IllegalStateException("Could not analyze file " + xmlFile.getAbsolutePath(), e);
			}
		}
	}

	private static void saveSyntaxHighlighting(SensorContext context, List<HighlightingData> highlightingDataList,
			InputFile inputFile) {
		NewHighlighting highlighting = context.newHighlighting().onFile(inputFile);

		for (HighlightingData highlightingData : highlightingDataList) {
			highlighting.highlight(highlightingData.startOffset(), highlightingData.endOffset(),
					highlightingData.highlightCode());
		}
		highlighting.save();
	}

	@VisibleForTesting
	public static void saveIssue(SensorContext context, XmlSourceCode sourceCode) {
		if(context!=null) {
			for (XmlIssue xmlIssue : sourceCode.getXmlIssues()) {
				NewIssue newIssue = context.newIssue().forRule(xmlIssue.getRuleKey());
				NewIssueLocation location = newIssue.newLocation().on(sourceCode.getInputFile().wrapped())
						.message(xmlIssue.getMessage());
				if (xmlIssue.getLine() != null) {
					location.at(sourceCode.getInputFile().selectLine(xmlIssue.getLine()));
				}
				newIssue.at(location).save();
			}
		}
	}

	@Override
	public String toString() {
		return getClass().getSimpleName();
	}

	@Override
	public void describe(SensorDescriptor descriptor) {
		descriptor.onlyOnLanguage(Xml.KEY).name("Apigee XML Sensor");
	}

	@Override
	public void execute(SensorContext context) {
		// Optional<RuleKey> parsingErrorKey = getParsingErrorKey();

		CONTEXT = context;
		
		// First loop to store ALL files.
		for (CompatibleInputFile inputFile : wrap(fileSystem.inputFiles(mainFilesPredicate), context)) {
			XmlFile xmlFile = new XmlFile(inputFile, fileSystem);
			XmlSourceCode xmlSourceCode = new XmlSourceCode(xmlFile);
			BundleRecorder.storeFile(xmlSourceCode);
		}
		
		// Second loop to checks files one by one.
		for (CompatibleInputFile inputFile : wrap(fileSystem.inputFiles(mainFilesPredicate), context)) {
			XmlFile xmlFile = new XmlFile(inputFile, fileSystem);
			try {
				runChecks(context, xmlFile);
			} catch (ParseException e) {
				// processParseException(e, context, inputFile, parsingErrorKey);
			} catch (RuntimeException e) {
				processException(e, context, inputFile);
			}
		}
	}

	public static SensorContext getContext() {
		return CONTEXT;
	}

	private Optional<RuleKey> getParsingErrorKey() {
		for (Object obj : checks.all()) {
			AbstractXmlCheck check = (AbstractXmlCheck) obj;
			if (check instanceof ParsingErrorCheck) {
				return Optional.of(checks.ruleKey(check));
			}
		}
		return Optional.empty();
	}

	private static void processParseException(ParseException e, SensorContext context, CompatibleInputFile inputFile,
			Optional<RuleKey> parsingErrorKey) {
		reportAnalysisError(e, context, inputFile);

		LOG.warn("Unable to parse file {}", inputFile.absolutePath());
		LOG.warn("Cause: {}", e.getMessage());

		if (parsingErrorKey.isPresent()) {
			// the ParsingErrorCheck rule is activated: we create a beautiful issue
			NewIssue newIssue = context.newIssue();
			NewIssueLocation primaryLocation = newIssue.newLocation().message("Parse error: " + e.getMessage())
					.on(inputFile.wrapped());
			newIssue.forRule(parsingErrorKey.get()).at(primaryLocation).save();
		}
	}

	private static void processException(RuntimeException e, SensorContext context, CompatibleInputFile inputFile) {
		reportAnalysisError(e, context, inputFile);

		throw new AnalysisException("Unable to analyse file " + inputFile.absolutePath(), e);
	}

	private static void reportAnalysisError(RuntimeException e, SensorContext context, CompatibleInputFile inputFile) {
		if (context.getSonarQubeVersion().isGreaterThanOrEqual(V6_0)) {
			context.newAnalysisError().onFile(inputFile.wrapped()).message(e.getMessage()).save();
		}
	}

}
