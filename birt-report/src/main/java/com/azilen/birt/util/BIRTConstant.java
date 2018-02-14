package com.azilen.birt.util;

import com.liferay.portal.kernel.configuration.Configuration;
import com.liferay.portal.kernel.configuration.ConfigurationFactoryUtil;

public class BIRTConstant {
	
	public static final Configuration configuration = ConfigurationFactoryUtil
		    .getConfiguration(BIRTConstant.class.getClassLoader(), "portlet");

	public static String BIRT_SERVER_URL = configuration.get("birtServerUrl");
}
