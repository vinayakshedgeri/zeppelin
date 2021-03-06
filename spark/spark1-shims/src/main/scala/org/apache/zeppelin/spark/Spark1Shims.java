/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package org.apache.zeppelin.spark;

import org.apache.spark.SparkContext;
import org.apache.spark.scheduler.SparkListenerJobStart;
import org.apache.spark.ui.jobs.JobProgressListener;

import java.util.Properties;

/**
 * Shims for Spark 1.x
 */
public class Spark1Shims extends SparkShims {

  public Spark1Shims(Properties properties) {
    super(properties);
  }

  public void setupSparkListener(final String master, final String sparkWebUrl) {
    final SparkContext sc = SparkContext.getOrCreate();
    sc.addSparkListener(new JobProgressListener(sc.getConf()) {
      @Override
      public void onJobStart(SparkListenerJobStart jobStart) {
        if (sc.getConf().getBoolean("spark.ui.enabled", true) &&
            !Boolean.parseBoolean(properties.getProperty("zeppelin.spark.ui.hidden", "false"))) {
          buildSparkJobUrl(master, sparkWebUrl, jobStart.jobId(), jobStart.properties());
        }
      }
    });
  }
}
