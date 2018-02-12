<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.Date"%>
<%@ page import="java.text.DateFormat"%>
<%@ include file="init.jsp"%>
<%@ page import="com.liferay.portal.kernel.util.Constants" %>

<liferay-portlet:actionURL portletConfiguration="<%=true%>"
    var="configurationActionURL" />
 
<liferay-portlet:renderURL portletConfiguration="<%=true%>"
    var="configurationRenderURL" />
    
<liferay-portlet:resourceURL portletName="com_azilen_demo_birt_portlet_BirtPortlet" id="reportFileList"  var="getReportFileList">
</liferay-portlet:resourceURL> 

<liferay-portlet:resourceURL portletName="com_azilen_demo_birt_portlet_BirtPortlet" id="reportParam"  var="getReportParameters">
</liferay-portlet:resourceURL> 
   
<input type="hidden" id="getReportFileList" value="<%=getReportFileList %>" />
<input type="hidden" id="getReportParams" value="<%=getReportParameters %>" />
<input type="hidden" id="getName" value="<%=reportname %>" />

<aui:form action="<%=configurationActionURL%>" method="post" name="fm">
    <aui:input name="<%= Constants.CMD %>" type="hidden"
        value="<%= Constants.UPDATE %>" />

    <aui:input name="redirect" type="hidden"
        value="<%=configurationRenderURL%>" />
 
    <aui:fieldset style="padding: 15px;"> 
        <aui:select name="reportname" label="Report Name" >
        </aui:select>
		<div id="_param" >
		    
		  <%
			if(Validator.isNotNull(paramName) && paramName.length>0 ){
				for(int i=0;i<paramName.length;i++){
					%>
					<div class="row">
						<div class="col-xs-4">
						<b class='control-label'>Param name : </b><input type='text' name='<portlet:namespace />paramName' value='<%=paramName[i] %>' style='order: none; border-color: transparent;text-align:right;width:150px;font-weight:bold;' readonly/> 
						</div>
	        		<%if(paramType[i].equals("String")){ %>
	        			<div class="col-xs-4">
	        			<b class='control-label'>Param value : </b><input type='text' name='<portlet:namespace />paramValue' value='<%= paramValue[i] %>'/>
	        			<input type='hidden' name='<portlet:namespace />paramType' value='String'/>
	        			</div>
	        			<div class="col-xs-4">
	        				<b class='control-label'>Param Selection : </b>	
		        			<select class='paramSelection form-control' style='height: 28px;width:40%;display:inline-block;' name='<portlet:namespace />paramSelection'  label='Param Selection'>
			    				<option value='static' <% if(paramSelection[i].equalsIgnoreCase("static")){%> selected=selected <%} %> >Static</option>
			    				<option value='ipc' <% if(paramSelection[i].equalsIgnoreCase("ipc")){%> selected=selected <%} %> >IPC</option>
							</select>
						</div>
	        		
	        		</br></br>
					
					<%}
	        		else if(paramType[i].equals("Integer")){
	        			%>
						<div class="col-xs-4">	
		        			<b class='control-label'>Param value : </b><input type='text' name='<portlet:namespace />paramValue'  onkeypress='return event.charCode >= 48 && event.charCode <= 57'  value='<%= paramValue[i] %>'/>
		        			<input type='hidden' name='<portlet:namespace />paramType' value='Integer'/>
	        			</div>
	        			<div class="col-xs-4">	
	        			    <b class='control-label'>Param Selection : </b>	
		        			<select class='paramSelection form-control' style='height: 28px;width:40%;display:inline-block;' name='<portlet:namespace />paramSelection'  label='Param Selection'>
			    				<option value='static' <% if(paramSelection[i].equalsIgnoreCase("static")){%> selected=selected <%} %> >Static</option>
			    				<option value='ipc' <% if(paramSelection[i].equalsIgnoreCase("ipc")){%> selected=selected <%} %> >IPC</option>
							</select>
						</div>
	        			</br></br>
	        		<%
	        		}else if(paramType[i].equals("Boolean")){
	        			%>
	        			<div class="col-xs-4">
	        				<b class='control-label'>Param Selection : </b>	   
		        			<label>true
		        			     <input type='radio' name='<portlet:namespace />paramValue' <%if(paramValue[i].equalsIgnoreCase("true")){ %> checked="checked" <%} %> style='margin-left: 5px;' value='true' />
			                </label>
			                <label>false
			                      <input type='radio' name='<portlet:namespace />paramValue' <%if(paramValue[i].equalsIgnoreCase("false")){ %> checked="checked" <%} %> style='margin-left: 5px;'  value='false'/>
			                </label>
		                          <input type='hidden' name='<portlet:namespace />paramType' value='Boolean'/>
	                    </div> 
	                    <div class="col-xs-4"> 
	                   	    <b class='control-label'>Param Selection : </b>	    
		                    <select class='paramSelection form-control' style='height: 28px;width:40%;display:inline-block;' name='<portlet:namespace />paramSelection'  label='Param Selection'>
			    				<option value='static' <% if(paramSelection[i].equalsIgnoreCase("static")){%> selected=selected <%} %> >Static</option>
			    				<option value='ipc' <% if(paramSelection[i].equalsIgnoreCase("ipc")){%> selected=selected <%} %> >IPC</option>
							</select>
						</div>	
	        			</br></br>
	        			<%
	        		}else if(paramType[i].equals("Date")){
	        			%>
	        			<div class="col-xs-4">
	        				<b class='control-label'>Param value : </b><input type='date' name='<portlet:namespace />paramValue' value='<%=paramValue[i]%>'/> 
	        			    <input type='hidden' name='<portlet:namespace />paramType' value='Date'/>
	        			</div>
	        			<div class="col-xs-4">	
		        			<b class='control-label'>Param Selection : </b>	
		        			<select class='paramSelection form-control' style='height: 28px;width:40%;display:inline-block;'  name='<portlet:namespace />paramSelection'  label='Param Selection'>
			    				<option value='static' <% if(paramSelection[i].equalsIgnoreCase("static")){ %> selected=selected <%} %> >Static</option>
			    				<option value='ipc' <% if(paramSelection[i].equalsIgnoreCase("ipc")){%> selected=selected <%} %> >IPC</option>
							</select>
						</div>
						</br></br>
	        		<%}%>
	        		</div>
	        		<% 
	        	} 
			}
		  %>
		</div>
    </aui:fieldset>
    
    <aui:button-row>
        <aui:button type="submit" onClick="validateForm();"></aui:button>
    </aui:button-row>
</aui:form>
<script type="text/javascript">

jQuery.noConflict();

function validateForm() {
	var returnBoolean = true;
	$("input[type='text'][name$=paramValue]").each(function(){
		var paramValue = $(this).val(); // grab name of original
		if(paramValue==null || paramValue== undefined || paramValue=="" ){
			alert("Please fill all parameters if you want static configured values from here ");
			event.preventDefault();
			returnBoolean = false;
			return returnBoolean;
		}
		
	});
	return returnBoolean;
}

function getParameters(reportName){
	
	var reportParamUrl = $("#getReportParams").val();
	
	$.ajax({
		url: reportParamUrl,
	    type: 'GET',
	    dataType: "text",
		data : {'reportname':reportName}, 
		success:function(data){
			var jsonObj = jQuery.parseJSON(data);
			$("#_param").html('');
			var rows=0;
			if(Object.keys(jsonObj).length==0){
				var noParamAvailable="<b class='control-label'>No parameters available for report</b></br></br>";
        		$("#_param").append(noParamAvailable);
			}else{
				$.each(jsonObj, function(idx, obj){ 
					if(rows ==0){
						var TempParamComponent="<b class='control-label'>Parameter Setup</b></br></br>";
		        		$("#_param").append(TempParamComponent);
					}
					
			        $.each(obj, function(key, value){
			        	var inputParamName="";
			        	if(key == 'Name')
		        		{
		        		    inputParamName="<div class='col-xs-4'><b class='control-label'>Param name : </b><input type='text' name='<portlet:namespace />paramName' value='"+value+"' style='order: none; border-color: transparent;text-align:right;width:150px;font-weight:bold;' readonly/></div>";
		        			$("#_param").append(inputParamName);
		        		}
			        	if(key == "Data Type"){
			        		var inputParamValue;
			        		switch(value) {
			        	    case "Boolean":
			        	    	inputParamValue = getRadioButton("true","false",true);
			        	        break;
			        	    case "Integer":
			        	    	inputParamValue = getInteger();
			        	        break;
			        	    case "Date":
			        	    	inputParamValue = getDateInput();
			        	        break;
			        	    default:
			        	    	inputParamValue= getTextInput();
			        	}
		        		$("#_param").append(inputParamValue+"</br></br>");
		        			
		        		}
			        });	
			        rows++;
			    });
				
			}
		},
		error:function(data){
		}
	});
}

(function($) {

	var nmspace=$(".portlet-configuration-setup").find(".form ").attr("data-fm-namespace");
	var fileListUrl = $("#getReportFileList").val();
		$.ajax({
			url: fileListUrl ,
		    type: 'GET',
		    dataType: "text",
			success:function(data){
				var jsonObj = jQuery.parseJSON(data);
				var optionHtml;
				if(jsonObj != "" && jsonObj.status=="SUCCESS"){
					
					for(var i=0;i<jsonObj.files.length;i++){
						var reportName = jsonObj.files[i];
						var report = reportName.split(".");
						optionHtml += "<option class='' value="+jsonObj.files[i]+">"+report[0]+"</option>";
					}
					$("#"+nmspace+"reportname").html(optionHtml);
					var reportDesignName = $("#getName").val();
					if(reportDesignName!=null && reportDesignName!=undefined && reportDesignName){
						$("#"+nmspace+"reportname").val(reportDesignName);
					}else{
						getParameters(jsonObj.files[0]);
					}
					
				}
			},
			error:function(data){
			}
		});
	
		$("#"+nmspace+"reportname").on('change',function(event){
			getParameters($("#"+nmspace+"reportname").val());
	    });
		
})(jQuery);

function getRadioButton(optionOne,optionTwo,isCheckedFirst){
	var responseHtml ="<div class='col-xs-4'><b class='control-label'>Param Selection : </b>"+optionOne
		   +"<input type='radio' name='<portlet:namespace />paramValue' checked='checked' style='margin-left: 5px;'  value='true' />"+
		"</label> <label>"+optionTwo+
		   "<input type='radio' name='<portlet:namespace />paramValue' style='margin-left: 5px;'  value='false'/>"+
		"</label></div>"+
		"<div class='col-xs-4'><b class='control-label'>Param Selection : </b>	<select class='paramSelection' style='height: 28px;width:40%;display:inline-block;' name='<portlet:namespace />paramSelection'  label='Param Selection'>"+
		    "<option value='static'>Static</option>"+
		    "<option value='ipc'>IPC</option>"+
		"</select>"+
		"<input type='hidden' name='<portlet:namespace />paramType' value='Boolean'/></div>";
		
	return responseHtml;
}

function getTextInput(){
	var responseHtml ="<div class='col-xs-4'><b class='control-label'>Param value : </b><input type='text' name='<portlet:namespace />paramValue' value=''/></div>"+
		"<div class='col-xs-4'><b class='control-label'>Param Selection : </b>	<select class='paramSelection' style='height: 28px;width:40%;display:inline-block;' name='<portlet:namespace />paramSelection'  label='Param Selection'>"+
		    "<option value='static'>Static</option>"+
		    "<option value='ipc'>IPC</option>"+
		"</select>"+
		"<input type='hidden' name='<portlet:namespace />paramType' value='String'/></div>";
	return responseHtml;
}

function getInteger(){
	var responseHtml ="<div class='col-xs-4'><b class='control-label'>Param value : </b><input type='text' onkeypress='return event.charCode >= 48 && event.charCode <= 57' name='<portlet:namespace />paramValue' value=''/></div>"+
		"<div class='col-xs-4'><b class='control-label'>Param Selection : </b>	<select class='paramSelection' style='height: 28px;width:40%;display:inline-block;' name='<portlet:namespace />paramSelection'  label='Param Selection'>"+
		    "<option value='static'>Static</option>"+
		    "<option value='ipc'>IPC</option>"+
		"</select>"+
		"<input type='hidden' name='<portlet:namespace />paramType' value='Integer'/></div>";
	return responseHtml;
}

function getDateInput(){
	var responseHtml ="<div class='col-xs-4'><b class='control-label'>Param value : </b><input type='date' style='width:50%;' name='<portlet:namespace />paramValue' value=''/></div>"+
		"<div class='col-xs-4'><b class='control-label'>Param Selection : </b><select class='paramSelection' style='height: 28px;width:40%;display:inline-block;' name='<portlet:namespace />paramSelection'  label='Param Selection'>"+
		    "<option value='static'>Static</option>"+
		    "<option value='ipc'>IPC</option>"+
		"</select>"+
		"<input type='hidden' name='<portlet:namespace />paramType' value='Date'/></div>";
	return responseHtml;
}

</script>	
