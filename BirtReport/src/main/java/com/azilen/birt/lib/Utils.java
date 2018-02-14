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

package com.azilen.birt.lib;

import java.io.File;
import java.util.UUID;

import org.eclipse.birt.report.engine.api.DocxRenderOption;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.engine.api.PDFRenderOption;
import org.eclipse.birt.report.engine.api.RenderOption;

public class Utils {

	public static IRenderOption getRenderOptions(OutputType outputType, String outputFolder) {

		IRenderOption options = null;
		String uuid = UUID.randomUUID().toString();
		
		switch (outputType) {
		case HTML:
			options = new HTMLRenderOption();
			options.setOutputFormat(HTMLRenderOption.OUTPUT_FORMAT_HTML);
			options.setOutputFileName(outputFolder + File.separator + uuid + ".html");
			break;

		case PDF:
			options = new PDFRenderOption();
			options.setOutputFormat(PDFRenderOption.OUTPUT_FORMAT_PDF);
			options.setOutputFileName(outputFolder + File.separator + uuid + ".pdf");
			break;

		case XLSX:
			options = new RenderOption();
			options.setOutputFormat("xlsx");
			options.setOutputFileName(outputFolder + File.separator + uuid + ".xlsx");
			break;
			
		case DOCX:
			options = new DocxRenderOption();
			options.setOutputFormat("docx");	
			options.setOutputFileName(outputFolder + File.separator + uuid + ".docx");
			break;
			
		default:
			break;
		}

		return options;

	}

	public static String getFileExtension(String fileName) {

		int dotPos = fileName.lastIndexOf(".");
		return fileName.substring(dotPos + 1);
	}

}
