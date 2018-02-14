<%@ page import="com.azilen.demo.birt.configuration.BirtReportConfiguration" %>
<%@ page import="com.azilen.birt.util.BIRTConstant" %>
<%@ page import="com.liferay.portal.kernel.util.Validator" %>
<%@ page import="com.liferay.portal.kernel.util.StringPool" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://liferay.com/tld/aui" prefix="aui" %>
<%@ taglib uri="http://liferay.com/tld/portlet" prefix="liferay-portlet" %>
<%@ taglib uri="http://liferay.com/tld/theme" prefix="liferay-theme" %>
<%@ taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui" %>

<liferay-theme:defineObjects />
<portlet:defineObjects />

<%
BirtReportConfiguration configuration = (BirtReportConfiguration)
    renderRequest.getAttribute(BirtReportConfiguration.class.getName());

String reportname = StringPool.BLANK;
String[] paramName = new String[]{};
String[] paramValue = new String[]{};
String[] paramType = new String[]{};
String[] paramSelection = new String[]{};

if (Validator.isNotNull(configuration)) {
	reportname = portletPreferences.getValue("reportname", configuration.reportname());
	paramName = portletPreferences.getValues("paramName",configuration.paramName());
	paramValue = portletPreferences.getValues("paramValue",configuration.paramValue());
	paramType = portletPreferences.getValues("paramType",configuration.paramType());
	paramSelection = portletPreferences.getValues("paramSelection",configuration.paramSelection());
}
%>

<input type="hidden" id="birtServerUrl" value=<%=BIRTConstant.BIRT_SERVER_URL %> />