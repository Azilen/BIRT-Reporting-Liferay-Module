package com.azilen.birt;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportEngineFactory;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.azilen.birt.lib.OutputType;
import com.azilen.birt.lib.ReportDef;
import com.azilen.birt.lib.ReportEngineException;
import com.azilen.birt.lib.Utils;
import com.azilen.birt.utiil.FileListUtil;

@RestController
public class ReportService {
	Logger LOG = Logger.getLogger(ReportService.class);

	@Autowired
	private BirtConfigs birtConfig;
	
	@RequestMapping(value="/getReportList",method = RequestMethod.GET)
    public String getReportList(HttpServletRequest request,
    		HttpServletResponse response){
		
		response.addHeader("Access-Control-Allow-Origin", "*");
		response.addHeader("Access-Control-Allow-Methods", "GET");
		response.addHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
		ArrayList<String> fileList =FileListUtil.listFilesForFolder(birtConfig.getReportDesignHome());
		StringBuilder responeJson = new StringBuilder();
		
		if(fileList!=null && fileList.size()>0){
			responeJson.append("{\"status\":\"SUCCESS\",\"files\":[");
			for(int i=0;i<fileList.size();i++){
				if(i==(fileList.size()-1)){
					responeJson.append("\""+fileList.get(i)+"\"");
				}else{
					responeJson.append("\""+fileList.get(i)+"\",");
				}
				
			}
			responeJson.append("]}");
		}
		
		return responeJson.toString();
	}
	
	@RequestMapping(value="/getReportDownload",method = RequestMethod.GET)
    public void getReport(@RequestParam(value="reportname",required=true) String reportDesign,
    		@RequestParam(value="attachment", defaultValue="attachment") String attachment,
    		@RequestParam(value="format", defaultValue="HTML") String type,
    		@RequestParam(value="filename", defaultValue="report") String outputFilename,
    		HttpServletRequest request,
    		HttpServletResponse response) {
        
			response.addHeader("Access-Control-Allow-Origin", "*");
			response.addHeader("Access-Control-Allow-Methods", "GET");
			response.addHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
	
			// check parameters
			StringBuffer msg = new StringBuffer();

			String paramNameValue = request.getParameter("paramNameValue");
			
			LOG.info(" reportDesign name "+reportDesign);
			LOG.info(" reportDesign name "+reportDesign);
			LOG.info(" file format type "+type);
			LOG.info(" file outputFilename  "+outputFilename);
			LOG.info(" file paramNameValue name "+paramNameValue);
			
			// checkers
			if (isEmpty(reportDesign)) {
				msg.append("<BR> report can not be empty");
			}

			OutputType outputType = null;

			try {
				outputType = OutputType.valueOf(type.toUpperCase());
			} catch (Exception e) {
				msg.append("Undefined report format: " + type + ". Set format= xlsx,html,pdf,docx");
			}

			// checkers
			if (isEmpty(outputFilename)) {
				msg.append("<BR>filename can not be empty");
			}
			
			try {

				ServletOutputStream out = response.getOutputStream();
				ServletContext context = request.getSession().getServletContext();

				// output error
				if (StringUtils.isNotEmpty(msg.toString())) {
					out.print(msg.toString());
					return;
				}

				ReportDef def = new ReportDef();
				def.setDesignFileName(reportDesign);
				def.setOutputType(outputType);
				
				LOG.info(" setParameters name "+paramNameValue);
				
				def.setParameters(getReportParameters(paramNameValue));
				
				try{
					IReportEngine engine = getReportEngine();
					String createdFile = createReport(def, engine);

					File file = new File(createdFile);

					String mimetype = context.getMimeType(file.getAbsolutePath());

					String inlineOrAttachment = (attachment != null) ? "attachment" : "inline";

					response.setContentType((mimetype != null) ? mimetype : "application/octet-stream");
					response.setContentLength((int) file.length());
					response.setHeader("Content-Disposition", inlineOrAttachment + "; filename=\"" + outputFilename +"."+type+ "\"");

					DataInputStream in = new DataInputStream(new FileInputStream(file));

					byte[] bbuf = new byte[1024];
					int length;
					while ((in != null) && ((length = in.read(bbuf)) != -1)) {
						out.write(bbuf, 0, length);
					}

					in.close();

				} catch (ReportEngineException e) {

					LOG.error("", e);
					out.print(e.getMessage());
					response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				} finally {
					out.flush();
					out.close();
				}

				LOG.info("Free memory: " + (Runtime.getRuntime().freeMemory() / 1024L * 1024L));

			} catch (IOException e) {
				LOG.error("IOException for ServletOutputStream ", e);

			} 
    }
	
	private boolean isEmpty(String in) {
		return (in == null || "".equals(in));
	}
	
	public String createReport(ReportDef reportDef,IReportEngine engine) throws ReportEngineException {

		IReportRunnable design = null;
		IRunAndRenderTask task;

		String generatedReportFile;
		try {

			String reportDesignPath = birtConfig.getReportDesignHome() + reportDef.getDesignFileName();
			LOG.info("starting report design: " + reportDesignPath);
			design = engine.openReportDesign(reportDesignPath);

		} catch (EngineException e) {
			LOG.error("EngineException while opening report design path ", e);
			throw new ReportEngineException(e.toString(), e);
		}

		task = engine.createRunAndRenderTask(design);
		IRenderOption options = Utils.getRenderOptions(reportDef.getOutputType(),birtConfig.getOutputFolder());
		
		task.setRenderOption(options);
		task.setParameterValues(reportDef.getParameters());
		try {

			task.run();

			generatedReportFile = task.getRenderOption().getOutputFileName();
			
			LOG.info("generatedReport: " + generatedReportFile + ", report count created after uptime: ");

		} catch (EngineException e) {
			LOG.error("EngineException while running report engine for generating report ", e);
			throw new ReportEngineException(e.toString(), e);

		} finally {

			design = null;
			task.cancel();
			task.close();
			task = null;
		}
		return generatedReportFile;
	}
	
	private IReportEngine getReportEngine(){

		IReportEngine engine=null;
		EngineConfig config = null;
		try{
			config = new EngineConfig();
			config.setEngineHome(birtConfig.getBirtHome());
			config.setLogConfig(birtConfig.getLogPath(), Level.FINE);

			Platform.startup(config);
			IReportEngineFactory factory = (IReportEngineFactory) Platform
					.createFactoryObject( IReportEngineFactory.EXTENSION_REPORT_ENGINE_FACTORY );
			engine = factory.createReportEngine(config);
			engine.changeLogLevel(Level.WARNING);

		}catch(BirtException ex){
			LOG.error("BirtException while creating report engine ",ex);
		}
		
		return engine;
	}
	
	private  Map<String, Object> getReportParameters(String parameNameValue){
		HashMap<String, Object> response = new HashMap<String, Object>();
		
		try {
			String parameNameValueDecoded = URLDecoder.decode(parameNameValue, "UTF-8");
			if(parameNameValueDecoded!=null && !parameNameValueDecoded.equals("")){
				String[] parameters = parameNameValueDecoded.split(",");
				for(int i=0 ; i<parameters.length;i++){
					String oneParam = parameters[i];
					String[] keyValue = oneParam.split("-");
					if(keyValue!=null && keyValue.length>0){
						Object paramValue = getParameterValueByType(keyValue[1], keyValue[2]);
						response.put(keyValue[0], paramValue);
					}
				}
			}
		} catch (UnsupportedEncodingException e) {
			LOG.error("ParamNameValue decoding exception ",e);
		}
		return response;
	}
	
	private static Object getParameterValueByType(String parameValue,String paramType){
		Object reponseObject = new Object();
		
		switch (paramType) {
		case "String":
			reponseObject = parameValue;
			break;
		case "Boolean":
			reponseObject = Boolean.parseBoolean(parameValue);
			break;	
		case "Integer":
			reponseObject = Integer.parseInt(parameValue);
			break;	
		case "Date":
			reponseObject = new Date(Long.parseLong(parameValue));
			break;		

		default:
			break;
		}
		return reponseObject;
	}
}
