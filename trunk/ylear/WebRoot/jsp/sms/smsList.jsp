<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/WEB-INF/jb-common.tld" prefix="jb" %>
<script src="scripts/validate.js" ></script>
<script src="scripts/icommon.js" ></script>
<script src="scripts/DatePicker/WdatePicker.js" ></script>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>查看收信箱</title>
	<%@include file="/css.jsp" %>		
	<%@include file="/js.jsp" %>
	<script type="text/javascript">
	$(document).ready(function(){
		//这里写jquery
		$("#queryBtn").button();
		$("input[type='button']").button();
		
	});
	setNavTitle("系统管理 >> 短信管理 >> 短信列表");		
	</script>
  </head>
  
<body>
<div id="container">
	<s:form action="listSms" method="post" theme="simple">
	<div>
		<table class="ui-widget ui-widget ui-widget-header">
			<tr>
				<td >开始时间</td>
				<td >
					<s:textfield name="queryBean.beginTime" 
					onfocus="WdatePicker({el:this,skin:'default',dateFmt:'yyyy-MM-dd HH:mm:ss'})"/>
				</td>
				<td >结束时间</td>
				<td  >
					<s:textfield name="queryBean.endTime" 
					onfocus="WdatePicker({el:this,skin:'default',dateFmt:'yyyy-MM-dd HH:mm:ss'})"/>
		
				<td ><s:submit id="queryBtn" value="查询"/></td>		
				
			</tr>
		</table>
		
	</div>
	<hr/>
	<!-- 列表数据 -->
	<div>
		<table class="ui-widget">
		<thead class="ui-widget-header">
		<tr>
			<td width="25%">接收手机号码</td>
			<td >短信内容</td>
			<td width="15%">发送时间</td>
			<td width="10%">发送状态</td>
			<td width="10%">操作</td>
		</tr>
		</thead>
		<tbody class="ui-widget-content">
		<s:iterator  value="#request.page.list"> 
			<tr>
				<td ><s:property value="recipient" default=" "/></td>
				<td ><s:property value="msgContent" default=" "/></td>
				<td ><s:date name="sendtime" format="yyyy-MM-dd hh:mm:ss"/></td>
				<td ><s:if test="status==0">未发送</s:if>
					<s:else>已发送</s:else></td>
				<td>
				<!-- 定义url -->
				<s:url id="delURL" action="delSms">
					<s:param name="id" value="id"/>
				</s:url>
					<input type="button"  value="删除"
					onclick="window.location.href='<s:property value="%{#delURL}"/>'"/>
				</td>
			</tr>
		</s:iterator>
		<s:if test="#request.page.list.size()==0">
		<tr><td style="text-align:center;height:40px;">没有记录</td></tr>
		</s:if> 
		</tbody>
		</table>
	</div>
  	
		<jb:pager/>
					

		
	</s:form>	
</div>
 </body>
</html>
