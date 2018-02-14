package com.azilen.birt.report.portlet;

import java.io.IOException;
import java.util.Map;

import javax.portlet.Portlet;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;

import com.azilen.birt.report.constants.BirtReportPortletKeys;
import com.azilen.birt.util.ConnectionUtil;
import com.azilen.demo.birt.configuration.BirtReportConfiguration;
import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.util.ParamUtil;

/**
 * @author virendralimbad
 */
@Component(configurationPid = "com.azilen.demo.birt.configuration.BirtReportPortletConfiguration",
	immediate = true,
	property = {
		"com.liferay.portlet.display-category=category.sample",
		"com.liferay.portlet.instanceable=true",
		"javax.portlet.display-name=birt-report Portlet",
		"javax.portlet.init-param.template-path=/",
		"javax.portlet.init-param.config-template=/configuration.jsp",
		"javax.portlet.init-param.view-template=/view.jsp",
		"javax.portlet.name=" + BirtReportPortletKeys.BirtReport,
		"javax.portlet.resource-bundle=content.Language",
		"javax.portlet.security-role-ref=power-user,user",
		"com.liferay.portlet.requires-namespaced-parameters=false",
		"com.liferay.portlet.footer-portlet-javascript=/js/jquery-1.11.3.min.js",
		"com.liferay.portlet.footer-portlet-css=css/birt-viewer.css",
		"javax.portlet.supported-public-render-parameter=paramName",
		"javax.portlet.supported-public-render-parameter=paramValue"
	},
	service = Portlet.class
)
public class BirtReportPortlet extends MVCPortlet {
		
	@Override
	public void serveResource(ResourceRequest resourceRequest,
			ResourceResponse resourceResponse) throws IOException,
			PortletException {

		if(resourceRequest.getResourceID().equalsIgnoreCase("reportFileList")){
			String fileListString= ConnectionUtil.getFileList();
			resourceResponse.getWriter().write(fileListString.toString());
		}else if(resourceRequest.getResourceID().equalsIgnoreCase("reportParam")){
			String reportName = ParamUtil.getString(resourceRequest, "reportname");
			String response = ConnectionUtil.getReportParameter(reportName);
			resourceResponse.getWriter().write(response);
		}else if(resourceRequest.getResourceID().equalsIgnoreCase("reportDownload")){
			String reportName = ParamUtil.getString(resourceRequest, "reportname");
			String format = ParamUtil.getString(resourceRequest, "format");
			String attachment = ParamUtil.getString(resourceRequest, "attachment");
			String paramNameValue = ParamUtil.getString(resourceRequest, "paramNameValue");
			String outputFileName = ParamUtil.getString(resourceRequest, "filename"); 
			String response = ConnectionUtil.getReportDownload(reportName,format,attachment,paramNameValue,outputFileName);
			resourceResponse.getWriter().write(response);
		}
		super.serveResource(resourceRequest, resourceResponse);
	}
	
	@Override
    public void doView(
            RenderRequest renderRequest, RenderResponse renderResponse)
        throws IOException, PortletException {
        
		renderRequest.setAttribute(BirtReportConfiguration.class.getName(), _birtReportConfiguration);
 
        super.doView(renderRequest, renderResponse);
    }
	
	/**
     * 
     * (1)If a method is annoted with @Activate then the method will be called at the time of activation of the component
     *  so that we can perform initialization task
     *  
     * (2) This class is annoted with @Component where we have used configurationPid with id com.proliferay.configuration.DemoConfiguration
     * So if we modify any configuration then this method will be called. 
     */
	
    @Activate
    @Modified
    protected void activate(Map<Object, Object> properties) {
        
        _birtReportConfiguration = ConfigurableUtil.createConfigurable(BirtReportConfiguration.class, properties);
        
    }
 
    private static final Log LOG = LogFactoryUtil.getLog(BirtReportPortlet.class);
 
    private volatile BirtReportConfiguration _birtReportConfiguration;  
}