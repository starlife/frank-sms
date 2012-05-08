<!-- jsp/user/userForm.jsp -->
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/WEB-INF/jb-common.tld" prefix="jb" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>增加用户</title>
	<%@include file="/css.jsp" %>		
	<%@include file="/js.jsp" %>
	<script type="text/javascript">
	$(document).ready(function(){
		//这里写jquery
		$("input[type='submit']").button();
	});
	setNavTitle("系统管理 >> 用户管理 >> 新增用户");
	</script>
	<s:head/>
  </head>

  <body>
  <div id="container">
  <s:form action="crudUser!save" method="post" theme="simple">
  <table class="ui-widget" width="100%">
  <thead class="ui-widget-header">
	  <tr >
	  	<td colspan="2">
		  	<s:if test="user==null||user.id==null">
		  		添加用户
		  	</s:if>
		  	<s:else>
		  		修改用户
		  		<s:hidden name="user.id" value="%{user.id}" />
		  	</s:else>
	  	</td>
	  </tr>
	 </thead>
	  <tr class="ui-widget-content">
	  	<td>用户名</td>
	  	<td>
		  	<s:if test="user==null||user.id==null">
		  		<s:textfield name="user.usrName" /><s:fielderror fieldName="user.usrName" />
		  	</s:if>
		  	<s:else>
		  		<s:property value="user.usrName"/>
		  		<s:hidden name="user.usrName" value="%{user.usrName}"/>
		  	</s:else>  	
	  	</td>
	  </tr>
	  	<s:if test="user==null||user.id==null">
	  		<tr class="ui-widget-content">
		  	<td>密码</td>
		  	<td><s:password name="user.usrPassword" /><s:fielderror fieldName="user.usrPassword" /></td>
		  </tr>
		  <tr class="ui-widget-content">
		  	<td>重复密码</td>
		  	<td><s:password name="reUsrPassword" /><s:fielderror fieldName="reUsrPassword" /></td>
		  </tr>
	  	</s:if>
	  	<s:else>
	  		<s:hidden name="user.usrPassword" value="%{user.usrPassword}"/>
	  		<s:hidden name="reUsrPassword" value="%{reUsrPassword}"/>
		 </s:else>  	
	  <tr class="ui-widget-content">
	  	<td>真实姓名</td>
	  	<td><s:textfield name="user.usrRealname" /></td>
	  </tr>
	  <tr class="ui-widget-content">
	  	<td>角色</td>
	  	<s:set name="roleList1" value="roleList.{?#this.id>1}"></s:set>
	  	<td><s:select name="user.sysRole.id" list="roleList1" listKey="id" listValue="roleName" label="选择一个角色" /> </td>
	  </tr>
	  <tr class="ui-widget-content">
	  	<td colspan="2"><s:submit value="提 交"/>
	  	<s:submit value="返 回" action="listUser"/>
	  	</td>
	  </tr>
	  </table>
	</s:form>
	</div>	
  </body>
</html>
