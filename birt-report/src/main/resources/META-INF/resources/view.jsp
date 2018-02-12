<%@ page import="com.liferay.portal.kernel.util.ParamUtil"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.Date"%>
<%@ page import="java.text.DateFormat"%>
<%@ include file="init.jsp" %>

<portlet:resourceURL id="reportDownload" var="getReportDownload" ></portlet:resourceURL>
<input id="getReportDownload" style="display:none;" value="<%=getReportDownload.toString() %>" />
<%if(Validator.isNotNull(reportname)) {
	String[] report= reportname.split("\\.");
	String paramNameIPC=ParamUtil.getString(renderRequest,"paramName",null);
	String paramValueIPC=ParamUtil.getString(renderRequest,"paramValue",null);
	if(report!=null && report.length>0){
	%>
	<p style="font-weight: bold;font-size: 16pt;color:#8B8B8B;" >Download Report: <span ><%= report[0] %></span><span id="reportname" style="display:none"><%= reportname %></span>
	</p><%} else{%>
	<p style="font-weight: bold;font-size: 16pt;color:#8B8B8B;" >Download Report: <span id="reportname" ><%= reportname %></span>
	</p>
<%} %>

</br>

	<div class="row">
	    <div class="col-xs-10">
	         <a href="javascript:void(0);" id="downloadPdf" class="download-link">
	         	<span class="inner">
		        	 <img src="/o/com.azilen.birt.report/img/pdf_normal.png" style="width: 20px;">
		         </span>
		         <p class="link" >PDF</p>
	         </a>
	         <a href="javascript:void(0);" id="downloadHtml" class="download-link">
	         	<span class="inner">
		        	 <img src="/o/com.azilen.birt.report/img/html_normal.png" style="width: 20px;">
		         </span>
		         <p class="link" >HTML</p>
	         </a>
	         <a href="javascript:void(0);" id="downloadDoc" class="download-link">
	         	<span class="inner">
		        	 <img src="/o/com.azilen.birt.report/img/doc_normal.png" style="width: 20px;">
		         </span>
		         <p class="link">Doc</p>
	         </a>
	         <a href="javascript:void(0);" id="downloadXls" class="download-link">
	         	<span class="inner">
		        	 <img src="/o/com.azilen.birt.report/img/excel_normal.png" style="width: 20px;">
		         </span>
		         <p class="link">Excel</p>
	         </a>
		</div>
   </div>
	<%
	StringBuilder paramNameValue = new StringBuilder();
	if(Validator.isNotNull(paramName) && Validator.isNotNull(paramValue) &&
			Validator.isNotNull(paramType) && paramName.length>0){
		for(int i=0;i<paramName.length;i++){
			
			String pValue="";
			
			if(paramSelection!=null && paramSelection[i].equalsIgnoreCase("ipc") && paramNameIPC!=null && paramValueIPC!=null){
				String[] paramNameIPCArray = paramNameIPC.split(",");
				String[] paramValueIPCArray = paramValueIPC.split(",");
				if(paramNameIPCArray!=null && !paramNameIPCArray.equals("") && paramValueIPCArray!=null && !paramValueIPCArray.equals("")){
					for(int j=0; j <paramNameIPCArray.length;j++){
						if(paramNameIPCArray[j].equalsIgnoreCase(paramName[i])){
							
							if(paramType[i].equalsIgnoreCase("Date")){
								DateFormat dateFormat= new SimpleDateFormat("MM/dd/yyyy");
								Date date = dateFormat.parse(paramValueIPCArray[j]);
								pValue = String.valueOf(date.getTime());
							}else{
								pValue = paramValueIPCArray[j];
							}
						}
					}
				}
			}else if(paramSelection[i].equalsIgnoreCase("static") || paramNameIPC==null || paramValueIPC==null){
				if(paramType[i].equalsIgnoreCase("Date")){
					DateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd");
					Date date = dateFormat.parse(paramValue[i]);
					pValue = String.valueOf(date.getTime());
				}else{
					pValue = paramValue[i];
				}
			}
			
			if(i!=0){
				paramNameValue.append(",").append(paramName[i]).append("-").append(pValue).append("-").append(paramType[i]);
			}else{
				paramNameValue.append(paramName[i]).append("-").append(pValue).append("-").append(paramType[i]);
			}
			
			
		} 
	}
	%>
	<input id="namespace" type="hidden" value='<portlet:namespace/>' />
	<input id="paramNameValue" type="hidden" value='<%=paramNameValue %>' />
	</br>
	<img id="loaderImg" alt="" style="margin-left: 40%;width: 15%;display:none;" src="/o/com.azilen.birt.report/img/loading.gif" /> 
    <div id="reportRender"> 
	</div>
	<a href="" target="_blank" id="downloadLink" style="display:none;" >
	Download Report 
	</a>
	
<%}else{%>
<p style="font-weight: bold;font-size: 16pt;color:#8B8B8B;" >Please select Report from portlet configuration. 
</p>
<%} %>

<script type="text/javascript">

jQuery.noConflict();

(function($) {
	
		$('a.download-link').hover(function() {
		    $('img', this).attr('src', ($('img', this).attr('src').replace("_normal", "_click")));
		}, function() {
		    $('img', this).attr('src', ($('img', this).attr('src').replace("_click", "_normal")));
		});
		
	    var prnmspace=$("#namespace").val();
	    var reportName= $("#reportname").html();
	    var outputFileName = generateName();
	    var paramNameValue = $("#paramNameValue").val();
	    var reportRenderUrl = $("#getReportDownload").val() +"&reportname="+reportName+"&format=html&filename="+outputFileName+"&attachment=inline&paramNameValue="+paramNameValue;
	   
	    $("#loaderImg").show();
	    $.ajax({
			url: reportRenderUrl,
		    type: 'GET',
		    dataType: "text",
			success:function(data){
				 $("#loaderImg").hide();
				$("#reportRender").html('');
				$("#reportRender").html(data);
			},
			error:function(data){
			}
		});
	    
		$("#downloadPdf").on('click',function(){
			
			var outputFileName = generateName();
			var reportDownloadUrl = $("#birtServerUrl").val()+"/getReportDownload?reportname="+reportName+"&format=pdf&filename="+outputFileName+"&attachment=attachment&paramNameValue="+$("#paramNameValue").val();
			
			$("#downloadLink").attr("href",reportDownloadUrl);
			$("#downloadLink")[0].click();
	
	    });
		$("#downloadHtml").on('click',function(){
			var outputFileName = generateName();
			
			var reportDownloadUrl = $("#birtServerUrl").val()+"/getReportDownload?reportname="+reportName+"&format=html&filename="+outputFileName+"&attachment=attachment&paramNameValue="+$("#paramNameValue").val();
			
			$("#downloadLink").attr("href",reportDownloadUrl);
			$("#downloadLink")[0].click();

		});
		$("#downloadDoc").on('click',function(){
			
			var outputFileName = generateName();
			var reportDownloadUrl = $("#birtServerUrl").val()+"/getReportDownload?reportname="+reportName+"&format=docx&filename="+outputFileName+"&attachment=attachment&paramNameValue="+$("#paramNameValue").val();
			
			$("#downloadLink").attr("href",reportDownloadUrl);
			$("#downloadLink")[0].click();
		
		});
		
        $("#downloadXls").on('click',function(){
			
			var outputFileName = generateName();
			var reportDownloadUrl = $("#birtServerUrl").val()+"/getReportDownload?reportname="+reportName+"&format=xlsx&filename="+outputFileName+"&attachment=attachment&paramNameValue="+$("#paramNameValue").val();
			
			$("#downloadLink").attr("href",reportDownloadUrl);
			$("#downloadLink")[0].click();
		
		});
        
        function generateName(){
        	
        	var currentDate = new Date();
            var day = currentDate.getDate();
            var month = currentDate.getMonth();
            var year = currentDate.getFullYear();
            var hours = currentDate.getHours();
            var minutes = currentDate.getMinutes();
            month = month+1;
            return "report-"+year+month+day+hours+minutes;
        }
		
})(jQuery);

</script>	

