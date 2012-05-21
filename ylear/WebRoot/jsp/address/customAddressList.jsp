<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/WEB-INF/jb-common.tld" prefix="jb" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>查询号码</title>
	<%@include file="/css.jsp" %>		
	<%@include file="/js.jsp" %>
	<script type="text/javascript">
	$(document).ready(
		function(){
			//这里写jquery
			$("#queryBtn").button();
			$(".button").button();
			$("#checkall").click(
				function()
				{
					if ($(this).attr("checked")=="checked")
					{
						$("input[type='checkbox']").slice(1).attr("checked",true);
					}else
					{
						$("input[type='checkbox']").slice(1).attr("checked",false);
					}
				}
			);
		}
		
	);
	function addSelectedPhone()
	{
		var phones="";
		$("input[type='checkbox']:checked").not("#checkall").each(
			function()
			{
				phones= phones+$(this).closest("td").next().text()+",";
			}
		)
		//回调函数
		parent.addSelectedPhone_callback(phones);
	}
	function addQueryPhone()
	{
		//回调函数
		parent.addQueryPhone_callback(phones);
	}
	</script>
  </head>
  
  <body>
  <div id="container">
  <s:form action="listCustomAddress" method="post" theme="simple">
  	<!-- 查询条件 -->
  	<div>
		<table class="ui-widget" >
			<tr>
				<td >手机号码</td>
				<td >
					<s:textfield  id="queryBean.phonenumber" name="queryBean.phonenumber"/>
				</td>
				<td >用户姓名</td>
				<td  >
					<s:textfield id="queryBean.name" name="queryBean.name"/>
					
				</td>
			</tr>
			<tr>		
				<td >所属部门</td>
				<td >
					<s:textfield id="queryBean.department" name="queryBean.department"/>
				</td>
				<td >地区</td>
				<td >
				<s:textfield id="queryBean.area" name="queryBean.area"/>
				</td>
			</tr>
			<tr>
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
			<td><s:checkbox id="checkall" name="checkall"/></td>
			<td >手机号码</td>
			<td >姓名</td>
			<td >所属部门</td>
			<td >地区</td>
		</tr>
		</thead>
		<tbody class="ui-widget-content">
		<s:iterator  value="#request.page.list"> 
			<tr class="ui-widget-content" align="center">
				<td><s:checkbox name="check"/></td>
				<td ><s:property value="phonenumber" default=" "/></td>
				<td ><s:property value="name" default=" "/></td>
				<td ><s:property value="department" default=" "/></td>
				<td ><s:property value="area" default=" "/></td>
			</tr>
		</s:iterator>
		<s:if test="#request.page.list.size()==0">
		<tr><td  style="text-align:center;height:40px;">没有记录</td></tr>
		</s:if> 
		</tbody>
		</table>
	</div>
		
	<jb:pager/>
	<div style="text-align:right;padding-top: 5px;font-size: 14px; ">
		<input type="button" class="button" value="添加号码" onclick="addSelectedPhone();"/>&nbsp;
		<s:url id="queryPhoneURL" action="listCustomAddress" method="queryPhone"/>
		<input type="button" class="button" value="添加所有" onclick="doSubmit('<s:property value="%{#queryPhoneURL}"/>')"/>
	</div>			
	</s:form>
	</div>		
  </body>
</html>

