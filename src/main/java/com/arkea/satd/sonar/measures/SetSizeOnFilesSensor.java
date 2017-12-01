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
package com.arkea.satd.sonar.measures;

import static com.arkea.satd.sonar.measures.ExampleMetrics.FILENAME_SIZE;

import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.SensorDescriptor;

/**
 * Scanner feeds raw measures on files but must not aggregate values to directories and project.
 * This class emulates loading of file measures from a 3rd-party analyser.
 */
public class SetSizeOnFilesSensor implements Sensor {
  @Override
  public void describe(SensorDescriptor descriptor) {
    descriptor.name("Compute size of file names");
  }

  @Override
  public void execute(SensorContext context) {
    FileSystem fs = context.fileSystem();
    // only "main" files, but not "tests"
    Iterable<InputFile> files = fs.inputFiles(fs.predicates().hasType(InputFile.Type.MAIN));
    for (InputFile file : files) {
      context.<Integer>newMeasure()
        .forMetric(FILENAME_SIZE)
        .on(file)
        .withValue(file.file().getName().length())
        .save();
    }
  }
}
