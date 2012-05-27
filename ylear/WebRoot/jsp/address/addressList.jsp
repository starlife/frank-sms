<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/WEB-INF/jb-common.tld" prefix="jb" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>用户管理</title>
	<%@include file="/css.jsp" %>		
	<%@include file="/js.jsp" %>
	<script type="text/javascript">
	$(document).ready(function(){
		//这里写jquery
		$("#queryBtn").button();
		$("#addBtn").button();
		$("input[type='button']").button();
	});
	setNavTitle("系统管理 >> 通讯录管理 >> 通讯录列表");
	</script>
  </head>
  
  <body>
  <div id="container">
  <s:form action="listAddress" method="post" theme="simple">
  	<!-- 查询条件 -->
  	<div>
		<table class="ui-widget ui-widget-header" >
			<tr>
				<td >手机号码</td>
				<td >
					<s:textfield name="queryBean.phonenumber"/>
				</td>
				<td >用户姓名</td>
				<td  >
					<s:textfield name="queryBean.name"/>
					
				</td>
				<td >所属部门</td>
				<td >
					<s:textfield name="queryBean.department"/>
				</td>
			</tr>
			<tr>
				<td >地区</td>
				<td >
				<s:textfield name="queryBean.area"/>
				</td>
				<td colspan="4" ><s:submit id="queryBtn" value="查询"/></td>		
				
			</tr>
		</table>
		
	</div>
	<hr/>
	<!-- 列表数据 -->
	<div>
		<table class="ui-widget">
		<thead class="ui-widget-header">
		<tr>
			<td >手机号码</td>
			<td >姓名</td>
			<td >所属部门</td>
			<td >地区</td>
			<td >操作</td>
		</tr>
		</thead>
		<tbody class="ui-widget-content">
		<s:iterator value="#request.page.list"> 
			<tr>
				<td ><s:property value="phonenumber" default=" "/></td>
				<td ><s:property value="name" default=" "/></td>
				<td ><s:property value="department" default=" "/></td>
				<td ><s:property value="area" default=" "/></td>
				<td>
				<!-- 定义url -->
				<s:url id="editURL" action="crudAddress" method="input">
					<s:param name="id" value="id"/>
				</s:url>
				<s:url id="delURL" action="delAddress">
					<s:param name="id" value="id"/>
				</s:url>
					<input type="button"  value="编辑"
					onclick="window.location.href='<s:property value="%{#editURL}"/>'"/>
					<input type="button"  value="删除"
					onclick="window.location.href='<s:property value="%{#delURL}"/>'"/>
				</td>
			</tr>
		</s:iterator>
		
		</tbody>
		</table>
					<s:if test="#request.page.list.size()==0">
						<table class="ui-widget">
							<tr>
								<td style="text-align: center; height: 40px;">
									没有记录
								</td>
							</tr>
						</table>
					</s:if>
				</div>
		<table width="100%" style="font-size: 14px;"><tr ><td>
				<s:url id="addURL" action="crudAddress" method="input"/>
				<s:a id="addBtn" href="%{#addURL}">增加</s:a>
				</td><td>
					<jb:pager/>
					</td>
				</tr></table>	
	</s:form>
	</div>		
  </body>
</html>
