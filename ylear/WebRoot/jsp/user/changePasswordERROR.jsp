<%@ page language="java" pageEncoding="UTF-8"%>
<%@include file="/css.jsp"%>
<%@include file="/js.jsp"%>
<%@include file="/taglib.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
   <title>修改密码的页面</title>
	<script type="text/javascript">
	$(document).ready(function(){
		//这里写jquery
		$("input[type='submit']").button();
		setNavTitle("系统管理 >> 修改密码");
	});
	</script>
	<s:head/>
  </head>

  <body>
  <div id="container">
  <table class="ui-state-default" height="200px" width="100%">
  <tr><td align="center">
	  		密码修改失败，请重试！
		  
	 </td></tr></table>
	</div>	
  </body>
</html>


