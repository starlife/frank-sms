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
		$("input[type='button']").button();
		$("#queryBtn").button();
		$("#addBtn").button();
		setNavTitle("系统管理 >> 用户管理 >> 用户列表");
	});
	
	
	
	</script>
  </head>
  
  <body>
  <div id="container">
  <s:form action="listUser" method="post" theme="simple" >
  	<!-- 查询条件 -->
  	<div>
		<table class="ui-widget ui-widget ui-widget-header">
			<tr>
				<td >用户名</td>
				<td >
					<s:textfield name="queryBean.usrName"/>
				</td>
				<td >真实姓名</td>
				<td >
					<s:textfield name="queryBean.usrRealname"/>
				</td>
				<td ><s:submit id="queryBtn" value="查询"/></td>		
				
			</tr>
		</table>
		
	</div>
	<hr/>
	<!-- 列表数据 -->
	<div >
		<table class="ui-widget">
		<thead class="ui-widget-header">
		<tr >
			<td >用户名</td>
			<td >真实姓名</td>
			<td >角色</td>
			<td >角色描述</td>
			<td >操作</td>
		</tr>
		</thead>
		<tbody class="ui-widget-content">
		<s:iterator  value="#request.page.list"> 
			<tr>
				<td ><s:property value="usrName" default=" "/></td>
				<td ><s:property value="usrRealname" default=" "/></td>
				<td ><s:property value="sysRole.roleName" default=" "/></td>
				<td ><s:property value="sysRole.roleDesc" default=" "/></td>
				<td>
				<!-- 定义url -->
				<s:url id="editURL" action="crudUser" method="input">
					<s:param name="id" value="id"/>
				</s:url>
				<s:url id="delURL" action="delUser">
					<s:param name="id" value="id"/>
				</s:url>
					<s:if test="usrName=='admin'">
					<span style="line-height:2.0">无操作</span>
					</s:if>
					<s:else>
					
					<input type="button"  value="编辑"
					onclick="redirect('<s:property value="%{#editURL}"/>');"/>
					<input type="button"  value="删除"
					onclick="redirect('<s:property value="%{#delURL}"/>');"/>
					
					</s:else>
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
	<input class="addBtn" type="button" value="增加" onclick="redirect('<s:url action="crudUser" method="input"/>');"/>
				
		<jb:pager/>
	
	</s:form>
	</div>		
  </body>
</html>
