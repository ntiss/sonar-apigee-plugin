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

import static com.arkea.satd.sonar.measures.ExampleMetrics.FILENAME_SIZE_RATING;
import static com.arkea.satd.sonar.measures.ExampleMetrics.FILENAME_SIZE;

import org.sonar.api.ce.measure.Measure;
import org.sonar.api.ce.measure.MeasureComputer;

/**
 * Rating is computed from value of metric {@link ExampleMetrics#FILENAME_SIZE}.
 */
public class ComputeSizeRating implements MeasureComputer {

  private static final int THRESHOLD = 20;
  private static final int RATING_A = 1;
  private static final int RATING_B = 2;

  @Override
  public MeasureComputerDefinition define(MeasureComputerDefinitionContext def) {
    return def.newDefinitionBuilder()
      .setInputMetrics(FILENAME_SIZE.key())
      .setOutputMetrics(FILENAME_SIZE_RATING.key())
      .build();
  }

  @Override
  public void compute(MeasureComputerContext context) {
    Measure size = context.getMeasure(FILENAME_SIZE.key());
    if (size != null) {
      // rating values are currently implemented as integers in API
      int rating = RATING_A;
      if (size.getIntValue() > THRESHOLD) {
        rating = RATING_B;
      }
      context.addMeasure(FILENAME_SIZE_RATING.key(), rating);
    }
  }
}
