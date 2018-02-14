package com.azilen.demo.birt.configuration;

import aQute.bnd.annotation.metatype.Meta;

@Meta.OCD(id = "com.azilen.demo.birt.configuration.BirtReportConfiguration")
public interface BirtReportConfiguration {

	@Meta.AD(required=false)
	public String reportname();
	
	@Meta.AD(required=false)
	public String[] paramName();
	
	@Meta.AD(required=false)
	public String[] paramValue();
	
	@Meta.AD(required=false)
	public String[] paramType();
	
	@Meta.AD(required=false)
	public String[] paramSelection();
	
}
