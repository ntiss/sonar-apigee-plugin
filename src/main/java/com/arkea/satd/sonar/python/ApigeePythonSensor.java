/*
 * SonarQube Apigee Python Plugin
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
package com.arkea.satd.sonar.python;

import java.util.List;

import org.sonar.api.batch.fs.FilePredicates;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.fs.InputFile.Type;
import org.sonar.api.batch.fs.TextRange;
import org.sonar.api.batch.rule.CheckFactory;
import org.sonar.api.batch.rule.Checks;
import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.api.batch.sensor.issue.NewIssue;
import org.sonar.api.batch.sensor.issue.NewIssueLocation;
import org.sonar.api.issue.NoSonarFilter;
import org.sonar.api.measures.FileLinesContextFactory;
import org.sonar.api.rule.RuleKey;
import org.sonar.plugins.python.Python;
import org.sonar.plugins.python.PythonHighlighter;
import org.sonar.plugins.python.SonarQubePythonFile;
import org.sonar.python.IssueLocation;
import org.sonar.python.PythonCheck;
import org.sonar.python.PythonCheck.PreciseIssue;
import org.sonar.python.PythonConfiguration;
import org.sonar.python.PythonFile;
import org.sonar.python.PythonVisitorContext;
import org.sonar.python.parser.PythonParser;

import com.arkea.satd.sonar.python.checks.CheckRepository;
import com.google.common.collect.ImmutableList;
import com.sonar.sslr.api.Grammar;
import com.sonar.sslr.api.RecognitionException;
import com.sonar.sslr.impl.Parser;

/**
 * @author Nicolas Tisserand
 */
public final class ApigeePythonSensor implements Sensor {

	private final Checks<PythonCheck> checks;
	private final FileLinesContextFactory fileLinesContextFactory;
	private final NoSonarFilter noSonarFilter;

	public ApigeePythonSensor(FileLinesContextFactory fileLinesContextFactory, CheckFactory checkFactory,
			NoSonarFilter noSonarFilter) {
		this.checks = checkFactory.<PythonCheck>create(CheckRepository.REPOSITORY_KEY)
				.addAnnotatedChecks((Iterable<?>) CheckRepository.getChecks());
		this.fileLinesContextFactory = fileLinesContextFactory;
		this.noSonarFilter = noSonarFilter;
	}

	@Override
	public void describe(SensorDescriptor descriptor) {
		descriptor.onlyOnLanguage(Python.KEY).name("Apigee Python Sensor").onlyOnFileType(Type.MAIN);
	}

	@Override
	public void execute(SensorContext context) {
		FilePredicates p = context.fileSystem().predicates();
		List<InputFile> inputFiles = ImmutableList.copyOf(
				context.fileSystem().inputFiles(p.and(p.hasType(InputFile.Type.MAIN), p.hasLanguage(Python.KEY))));
		Parser<Grammar> parser = PythonParser.create(new PythonConfiguration(context.fileSystem().encoding()));

		for (InputFile inputFile : inputFiles) {

			PythonFile pythonFile = SonarQubePythonFile.create(inputFile, context);
			PythonVisitorContext visitorContext;
			try {
				visitorContext = new PythonVisitorContext(parser.parse(pythonFile.content()), pythonFile);
			} catch (RecognitionException e) {
				visitorContext = new PythonVisitorContext(pythonFile, e);
			}

			for (PythonCheck check : checks.all()) {

				saveIssues(context, inputFile, check, check.scanFileForIssues(visitorContext));
			}

			new PythonHighlighter(context, inputFile).scanFile(visitorContext);
		}
	}

	private void saveIssues(SensorContext context, InputFile inputFile, PythonCheck check, List<PreciseIssue> issues) {
		RuleKey ruleKey = checks.ruleKey(check);

		for (PreciseIssue preciseIssue : issues) {

			NewIssue newIssue = context.newIssue().forRule(ruleKey);

			Integer cost = preciseIssue.cost();
			if (cost != null) {
				newIssue.gap(cost.doubleValue());
			}

			newIssue.at(newLocation(inputFile, newIssue, preciseIssue.primaryLocation()));

			for (IssueLocation secondaryLocation : preciseIssue.secondaryLocations()) {
				newIssue.addLocation(newLocation(inputFile, newIssue, secondaryLocation));
			}

			newIssue.save();
		}
	}

	private static NewIssueLocation newLocation(InputFile inputFile, NewIssue issue, IssueLocation location) {
		NewIssueLocation newLocation = issue.newLocation().on(inputFile);
		if (location.startLine() != IssueLocation.UNDEFINED_LINE) {
			TextRange range;
			if (location.startLineOffset() == IssueLocation.UNDEFINED_OFFSET) {
				range = inputFile.selectLine(location.startLine());
			} else {
				range = inputFile.newRange(location.startLine(), location.startLineOffset(), location.endLine(),
						location.endLineOffset());
			}
			newLocation.at(range);
		}

		if (location.message() != null) {
			newLocation.message(location.message());
		}
		return newLocation;
	}

}
