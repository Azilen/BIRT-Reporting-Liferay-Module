package com.azilen.birt.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

public class ConnectionUtil {
	
	public static String getFileList(){
		StringBuilder textView = new StringBuilder();
		
		try {

			URI baseUri = new URI(BIRTConstant.BIRT_SERVER_URL+"/getReportList");
			
			HttpURLConnection conn = (HttpURLConnection) baseUri.toURL().openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");
			
			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader(
					(conn.getInputStream())));

			String output;
			while ((output = br.readLine()) != null) {
				 textView.append(output);
			}
			
			conn.disconnect();

		} catch (URISyntaxException | IOException e) {
			LOG.error("getFileList syntax or IO exception",e);
		}
		return textView.toString();
	}
		
	public static String getReportParameter(String reportName){
		StringBuilder textView = new StringBuilder();
		try {

			String[] param = {"reportname",reportName};
			URI baseUri = new URI(BIRTConstant.BIRT_SERVER_URL+"/getParameters");
			URI uri = applyParameters(baseUri,param);
			
			HttpURLConnection conn = (HttpURLConnection) uri.toURL().openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");
			
			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader(
					(conn.getInputStream())));

			String output;
			while ((output = br.readLine()) != null) {
				 textView.append(output);
			}
			conn.disconnect();

		} catch (URISyntaxException | IOException e) {
			LOG.error("getReportParameter syntax or IO exception",e);
			
		}
		return textView.toString();
	}
	
	public static String getReportDownload(String reportName,String format,String attachment,String paramNameValue,String filename){
		StringBuilder textView = new StringBuilder();
		try {

			String[] param = {"reportname",reportName,"format",format,"attachment",attachment,"filename",filename,"paramNameValue",paramNameValue};
			URI baseUri = new URI(BIRTConstant.BIRT_SERVER_URL+"/getReportDownload");
			URI uri = applyParameters(baseUri,param);
			
			HttpURLConnection conn = (HttpURLConnection) uri.toURL().openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");
			
			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}
			
			BufferedReader br = new BufferedReader(new InputStreamReader(
					(conn.getInputStream())));

			String output;
			while ((output = br.readLine()) != null) {
				 textView.append(output);
			}
			
			conn.disconnect();

		} catch (URISyntaxException | IOException e) {
			LOG.error("getReportDownload syntax or IO exception",e);
		}
		return textView.toString();
	}

	private static URI applyParameters(URI baseUri, String[] urlParameters){
		   StringBuilder query = new StringBuilder();
		   boolean first = true;
		   for (int i = 0; i < urlParameters.length; i += 2) {
		     if (first) {
		        first = false;
		     } else {
		        query.append("&");
		     }
		     try {
		        query.append(urlParameters[i]).append("=")
		             .append(URLEncoder.encode(urlParameters[i + 1], "UTF-8"));
		     } catch (UnsupportedEncodingException ex) {
		       throw new RuntimeException(ex);
		     }
		   }
		   try {
		      return new URI(baseUri.getScheme(), baseUri.getAuthority(), 
		    		  baseUri.getPath(), query.toString(), null);
		   } catch (URISyntaxException ex) {
		      throw new RuntimeException(ex);
		   }
		}
	
	private static final Log LOG = LogFactoryUtil.getLog(ConnectionUtil.class);
	 
}
