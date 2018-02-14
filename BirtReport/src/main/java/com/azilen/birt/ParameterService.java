package com.azilen.birt;

import java.io.Serializable;
import java.util.HashMap;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.eclipse.birt.report.engine.api.EngineException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.azilen.birt.utiil.ParametersTask;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@RestController
public class ParameterService{
	
	@Autowired
	private BirtConfigs birtConfig;
	
	private static final long serialVersionUID = 1L;

	Logger LOG = Logger.getLogger(ParameterService.class);
	
	@RequestMapping(value="/getParameters",method = RequestMethod.GET)
    public String getParametersFromReport(@RequestParam(value="reportname", defaultValue="user_report.rptdesign") String reportname,HttpServletResponse response) {
        
		response.addHeader("Access-Control-Allow-Origin", "*");
		response.addHeader("Access-Control-Allow-Methods", "GET");
		response.addHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");

		String birtHome= birtConfig.getBirtHome();
		String reportDesignHome= birtConfig.getReportDesignHome();
		String logPath= birtConfig.getLogPath();
		
		HashMap<String, HashMap<String, Serializable>> responseHashMap=null;
		try {
			responseHashMap = ParametersTask.getParametersFromReport(reportname,birtHome,reportDesignHome,logPath);
			LOG.info("response hashmap for parameters "+responseHashMap.toString());
		} catch (EngineException e) {
			LOG.error(e);
		}
		return convertToJson(responseHashMap); 
    }
	
	private String convertToJson(HashMap<String, HashMap<String, Serializable>> map){
		Gson gson = new GsonBuilder().create();
		String jsonString = gson.toJson(map);
		jsonString = jsonString.replace("\\", "");
		jsonString = jsonString.replace("}\"", "}");
		jsonString = jsonString.replace("\"{", "{");
		return jsonString;
	}
}

