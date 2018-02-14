<%@ include file="init.jsp" %>
<portlet:actionURL var="sendDataActionURL" windowState="normal" name="saveIpcParam">
</portlet:actionURL>
<input type="hidden" id="namespaceSender" value='<portlet:namespace/>' />
<p class="sender-head">Public Render Parameters IPC Sender Portlet</p>

<aui:form cssClass="sender-form" action="<%=sendDataActionURL%>" method="post" name="senderForm" >
	
	<div class="row">
		<div class="col-xs-3">
			<aui:input name="customerNum" id="customerNum" label="Customer Number">
				<aui:validator name="required" />
				<aui:validator name="customerNum"></aui:validator>
			</aui:input>
		</div>
		<div class="col-xs-3">
			<aui:select name="countryName" id="countryName" label="Select Country :">
					<aui:option value="USA">USA</aui:option>
					<aui:option value="France">France</aui:option>
					<aui:option value="Australia">Australia</aui:option>
					<aui:option value="Norway">Norway</aui:option>
					<aui:option value="Poland">Poland</aui:option>
					<aui:option value="Germany">Germany</aui:option>
					<aui:option value="Denmark">Denmark</aui:option>
					<aui:option value="Sweden">Sweden</aui:option>
					<aui:option value="Singapore">Singapore</aui:option>
					<aui:option value="Japan">Japan</aui:option>
					<aui:option value="Finland">Finland</aui:option>
			</aui:select>
		</div>
		<div class="col-xs-3">
			<aui:input name="empCode" id="empCode" label="Employee code">
				<aui:validator name="required" />
				<aui:validator name="empCode"></aui:validator>
			</aui:input>
		</div>
		<div class="col-xs-3">
			<aui:input name="paramName" id="paramName" label="Param Name" type="hidden"
				value="empCode,customerNum,countryName">
				<aui:validator name="required" />
				<aui:validator name="paramName"></aui:validator>
			</aui:input>
			<aui:input name="paramValue" id="paramValue" label="Param Value" type="hidden">
				<aui:validator name="required" />
				<aui:validator name="paramValue"></aui:validator>
			</aui:input>
			<aui:button cssClass="btn-report" type="button" onClick="setValue();" value="Generate Report"></aui:button>
			<aui:button id="generateReport" type="submit" style="display:none;" value="Generate Report"></aui:button>
		</div>
	</div>	
</aui:form>
<script type="text/javascript">

jQuery.noConflict();

(function($) {
	
})(jQuery);

function setValue(){
	
	var nmspaceSender = $("#namespaceSender").val();
	var empCode = $('#'+nmspaceSender+'empCode').val();
	var customerNum = $('#'+nmspaceSender+'customerNum').val();
	var countryName = $('#'+nmspaceSender+'countryName').val();
	
	$('#'+nmspaceSender+'paramValue').val(empCode+","+customerNum+","+countryName);
	
	$('#'+nmspaceSender+'generateReport').click();
} 
</script>
