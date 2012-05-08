<!-- jsp/user/userForm.jsp -->
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/WEB-INF/jb-common.tld" prefix="jb" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
   <title>修改密码的页面</title>
	<%@include file="/css.jsp" %>		
	<%@include file="/js.jsp" %>
	<script type="text/javascript">
	$(document).ready(function(){
		//这里写jquery
		$("input[type='submit']").button();
	});
	setNavTitle("系统管理 >> 修改密码");
	</script>
	<s:head/>
  </head>

  <body>
  <div id="container">
  <s:form action="changePasswordAction!savePassword" method="post" theme="simple">
  <table class="ui-widget" width="100%" >
  <thead class="ui-widget-header">
	  <tr >
	  	<td colspan="2">
	  		密码修改
		  
	  	</td>
	  </tr>
	 </thead>
	  
	  	<tr class="ui-widget-content">
		  	<td align="right">输入新密码：</td>
		  	<td ><s:password name="user.usrPassword" /><s:fielderror fieldName="user.usrPassword" /></td>
		  </tr>
		  <tr class="ui-widget-content">
		  	<td align="right">再输入一次：</td>
		  	<td><s:password name="reUsrPassword" /><s:fielderror fieldName="reUsrPassword" /></td>
		  </tr>
	  		
	  
	  <tr class="ui-widget-content">
	  	<td colspan="2" align="center"><s:submit value="提 交"/>
	  	</td>
	  </tr>
	  </table>
	</s:form>
	</div>	
  </body>
</html>


