package com.azilen.birt.report.portlet;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.Portlet;
import javax.portlet.ProcessAction;

import org.osgi.service.component.annotations.Component;

import com.azilen.birt.report.constants.BirtReportPortletKeys;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.util.ParamUtil;

@Component(
immediate = true,
property = {
	"com.liferay.portlet.display-category=category.sample",
	"com.liferay.portlet.instanceable=true",
	"javax.portlet.display-name=birt-report Sender Portlet",
	"javax.portlet.init-param.template-path=/",
	"javax.portlet.init-param.view-template=/viewSender.jsp",
	"javax.portlet.name=" + BirtReportPortletKeys.BirtReportSender,
	"javax.portlet.resource-bundle=content.Language",
	"javax.portlet.security-role-ref=power-user,user",
	"com.liferay.portlet.requires-namespaced-parameters=true",
	"com.liferay.portlet.footer-portlet-javascript=/js/jquery-1.11.3.min.js",
	"com.liferay.portlet.footer-portlet-css=css/sender.css",
	"javax.portlet.supported-public-render-parameter=paramName",
	"javax.portlet.supported-public-render-parameter=paramValue"
},
service = Portlet.class
)
public class BirtReportSenderPortlet extends MVCPortlet{

	
	@ProcessAction(name="saveIpcParam")
	public void saveIpcParams(ActionRequest actionRequest,
			ActionResponse actionResponse) {
		
		String paramName = ParamUtil.getString(actionRequest,"paramName","");
		String paramValue = ParamUtil.getString(actionRequest,"paramValue","");
		
		actionResponse.setRenderParameter("paramName", paramName);
		actionResponse.setRenderParameter("paramValue", paramValue);
	}
}
