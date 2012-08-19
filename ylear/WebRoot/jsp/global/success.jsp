<%@ page language="java" pageEncoding="UTF-8"%>
<%@include file="/css.jsp"%>
<%@include file="/js.jsp"%>
<%@include file="/taglib.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>操作成功界面</title>
  </head>
  <body >
       <div style="text-align:center">
		<table class="ui-widget" style="width:80%;text-align:center">
			
			<tr>
				<td class="ui-widget-header"><%=request.getAttribute("message") %></td>
			</tr>
			<tr height="100px;" valign="middle">
				<td class="ui-widget-content">
				<a href='<%=request.getAttribute("backurl") %>'>返回</a>
				</td>
			</tr>
			
		</table>
	</div>
  </body>
</html>
