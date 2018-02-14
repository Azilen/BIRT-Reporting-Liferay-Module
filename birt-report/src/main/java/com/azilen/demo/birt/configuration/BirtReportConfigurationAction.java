package com.azilen.demo.birt.configuration;

import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Modified;

import com.azilen.birt.report.constants.BirtReportPortletKeys;
import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.ConfigurationAction;
import com.liferay.portal.kernel.portlet.DefaultConfigurationAction;
import com.liferay.portal.kernel.util.ParamUtil;

@Component(configurationPid = "com.azilen.demo.birt.configuration.BirtReportConfiguration",
        configurationPolicy = ConfigurationPolicy.OPTIONAL,
        immediate = true,
        property = {"javax.portlet.name="+BirtReportPortletKeys.BirtReport,

}, service = ConfigurationAction.class)
public class BirtReportConfigurationAction extends DefaultConfigurationAction {
	
	
	@Override
	public void include(PortletConfig portletConfig, HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) throws Exception {

		httpServletRequest.setAttribute(BirtReportConfiguration.class.getName(), _birtReportConfiguration);

		super.include(portletConfig, httpServletRequest, httpServletResponse);
	}
	
	@Override
	public void processAction(PortletConfig portletConfig, ActionRequest actionRequest, ActionResponse actionResponse)
			throws Exception {

		String reportName = ParamUtil.getString(actionRequest, "reportname");

		String[] paramName = ParamUtil.getStringValues(actionRequest, "paramName");
		String[] paramValue = ParamUtil.getStringValues(actionRequest, "paramValue");
		String[] paramType = ParamUtil.getStringValues(actionRequest, "paramType");
		String[] paramSelection = ParamUtil.getStringValues(actionRequest, "paramSelection");

		if(paramValue!=null && !paramValue.equals("")){
			for (int i = 0; i < paramValue.length; i++) {
				LOG.info("birtReportPortletConfiguration paramName :: " + paramName[i] + " paramValue " + paramValue[i]);
			}
		}
		if(paramType!=null && paramType.length>0){
			for (int i = 0; i < paramType.length; i++) {
				LOG.info("birtReportPortletConfiguration paramType :: " + paramType[i] );
			}
		}
		
		if(paramSelection!=null && paramSelection.length>0){
			for (int i = 0; i < paramSelection.length; i++) {
				LOG.info("birtReportPortletConfiguration paramSelection :: " + paramSelection[i] );
			}
		}
		
		setPreference(actionRequest, "reportname", reportName);
		setPreference(actionRequest, "paramName", paramName);
		setPreference(actionRequest, "paramValue", paramValue);
		setPreference(actionRequest, "paramType", paramType);
		setPreference(actionRequest, "paramSelection", paramSelection);

		super.processAction(portletConfig, actionRequest, actionResponse);
	}

	@Activate
	@Modified
	protected void activate(Map<Object, Object> properties) {
		_birtReportConfiguration = ConfigurableUtil.createConfigurable(
				BirtReportConfiguration.class, properties);
	}
	
	private volatile BirtReportConfiguration _birtReportConfiguration;

	private static final Log LOG = LogFactoryUtil.getLog(BirtReportConfigurationAction.class);
	
}
