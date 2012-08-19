<%@ page language="java" pageEncoding="UTF-8"%>
<%@include file="/css.jsp"%>
<%@include file="/js.jsp"%>
<%@include file="/taglib.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>用户管理</title>
	<script type="text/javascript">
	$(document).ready(function(){
		//这里写jquery
		$("#queryBtn").button();
		$("input[type='button']").button();
		setNavTitle("系统管理 >> 通讯录管理 >> 通讯录列表");
	});
	
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
					onclick="redirect('<s:property value="%{#editURL}"/>');"/>
					<input type="button"  value="删除"
					onclick="redirect('<s:property value="%{#delURL}"/>');"/>
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
				<input class="addBtn" type="button" value="增加" onclick="redirect('<s:url action="crudAddress" method="input"/>');"/>
				
				<my:pager/>
	</s:form>
	</div>		
  </body>
</html>
