/*
 *
 *  * Licensed to the Apache Software Foundation (ASF) under one or more
 *  * contributor license agreements.  See the NOTICE file distributed with
 *  * this work for additional information regarding copyright ownership.
 *  * The ASF licenses this file to You under the Apache License, Version 2.0
 *  * (the "License"); you may not use this file except in compliance with
 *  * the License.  You may obtain a copy of the License at
 *  *
 *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *  *
 *  **************************************************************************
 *  * SBRS  - Simple Birt Report Server
 *  *
 *  *
 *
 */

package com.azilen.birt;

import java.io.File;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class BirtConfigs {

	private static Logger logger = Logger.getLogger(BirtConfigs.class);

	private @Value("${logPath}") String logPath;

	private @Value("${birtHome}") String birtHome;

	private @Value("${outPath}") String outputFolder;

	private @Value("${reportDesignHome}") String reportDesignHome;

	public String getBirtHome() {

		return birtHome;
	}

	public void setBirtHome(String birtHome) {
		this.birtHome = birtHome;
	}

	public String getLogPath() {
		return logPath;
	}

	public void setLogPath(String logPath) {
		this.logPath = logPath;
	}

	public String getOutputFolder() {
		return outputFolder;
	}

	public void setOutputFolder(String outputFolder) {
		this.outputFolder = outputFolder;
	}
	
	public String getReportDesignHome() {
		return reportDesignHome;
	}

	public void setReportDesignHome(String reportDesignHome) {
		this.reportDesignHome = reportDesignHome;
	}

	public void checkConfigs() throws Exception {

		if (!new File(logPath).isDirectory()) {
			String msg = "Error path doesn't exists logFolder: " + logPath;
			logger.error(msg);
			throw new Exception(msg);
		}

		if (!new File(birtHome).isDirectory()) {
			String msg = "Error path doesn't exists birtEngineHome: " + birtHome;
			logger.error(msg);
		    throw new Exception(msg);
		}

		if (!new File(outputFolder).exists()) {
			String msg = "Error path doesn't exists outputFolder: " + outputFolder;
			logger.error(msg);
			throw new Exception(msg);
		}
		if (!new File(reportDesignHome).exists()) {
			String msg = "Error path doesn't exists reportDesignHome: " + reportDesignHome;
			logger.error(msg);
			throw new Exception(msg);
		}

	}

}
