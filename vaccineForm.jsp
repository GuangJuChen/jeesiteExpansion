<%@page import="java.util.Date" contentType="text/html;charset=UTF-8" %>
<%@include file="/WEB-INF/views/include/taglib.jsp"%>
<%Date date = new Date();%>
<html>
	<head>
		<title>疫苗登记管理</title>
		<meta name="decorator" content="default"/>
		<link href="${ctxCss}/common/table.css" rel="stylesheet" />
		<script type="text/javascript">
			var entVaccineNameList = "${entVaccineNameList}";
			var vaccineSpecsCodeList = "${vaccineSpecsCodeList}";
			var imgSrc = "${ctxStatic}/images/delete.png";
			var dateTime = new Date("yyyy-MM-dd");
			$(document).ready(function() {
				//初始化企业
				initVaccine();
				//初始化行
				initVaccineRow();
			});
			function page(n,s){
				$("#pageNo").val(n);
				$("#pageSize").val(s);
				$("#searchForm").submit();
				return false;
			}
			//初始化时间控件
			function vaccineTime() {
				WdatePicker({skin:'twoer',minDate:"1900-01-01",maxDate:'%y-%M-%d',readOnly:true,dateFmt:"yyyy-MM-dd"});
			}
		</script>
	</head>
	<body>
		<ul class="nav nav-tabs">
			<li class="active"><a href="${ctx}/imm/vaccine/form">疫苗登记</a></li>
			<li><a href="${ctx}/imm/vaccine/list">疫苗记录</a></li>
		</ul>
		<div id="searchForm" modelAttribute="vaccine" action="javascript:void(0);" method="post" class="breadcrumb form-search">
			<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
			<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
			<ul class="ul-form">
				<li class="btns" style="float:right;"><input id="btnSave" class="btn btn-primary" type="button" onclick="vaccineSave()" value="保 存"/></li>
				<li class="btns" style="float:right;"><input id="btnAdd" class="btn btn-primary" type="button" onclick="addVaccineRow()" value="新 增"/></li>
			</ul>
			<table id="contentTable" class="table table-striped table-bordered table-condensed">
				<thead>
					<tr>
						<th width="3%">序号</th>
						<th width="7%">企业代码</th>
						<th width="15%">企业名称</th>
						<th width="10%">疫苗名称</th>
						<th width="10%">生产批号</th>
						<th width="10%">疫苗有效期(月)</th>
						<th width="10%">生产日期</th>
						<th width="10%">疫苗数量</th>
						<th width="6%">单位</th>
						<th width="10%">规格</th>
						<shiro:hasPermission name="imm:vaccine:edit"><th width="8%">操作</th></shiro:hasPermission>
					</tr>
				</thead>
				<tbody id="tbody">
				</tbody>
			</table>
		</div>
		<script src="${ctxScript}/imm/vaccine.js?<%=date.getTime()%>"></script>
		<script type="text/javascript" src="${ctxStatic}/layer/layer.js?<%=date.getTime()%>"></script>
		<script type="text/javascript" src="${ctxScript}/confirmAndAlertLayer.js?<%=date.getTime()%>"></script>
		<script src="${ctxStatic}/My97DatePicker/WdatePicker.js?<%=date.getTime()%>" type="text/javascript"></script>
		<script src="${ctxStatic}/webuploader/webupload.js?<%=date.getTime()%>" type="text/javascript"></script>
		<div class="pagination">${page}</div>
	</body>
</html>