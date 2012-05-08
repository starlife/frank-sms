<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>短彩管理平台</title>
</head>
<s:if test="#session.USER==null">
	<%response.sendRedirect("login.jsp"); %>
</s:if>
<frameset rows="127,*,11" frameborder="no" border="0" framespacing="0">
  <frame src="<s:url action="homeAction!header"/>" name="topFrame" scrolling="no" noresize="noresize" id="topFrame" />
  <frameset  cols="8,147,10,*,8">
  	<frame src="borderleft.html" scrolling="no" frameborder="no" border="0" framespacing="0"/>
  	<frame src="<s:url action="homeAction!menu"/>" name="menuFrame" id="menuFrame" />
  	<frame src="borderdivide.html" scrolling="no" frameborder="no" border="0" framespacing="0"/>
  	<frame src="<s:url action="homeAction!welcome"/>" name="mainFrame" id="mainFrame" />
  	<frame src="borderright.html" scrolling="no" frameborder="no" border="0" framespacing="0"/>
  </frameset>
  <frame src="<s:url action="homeAction!footer"/>" name="bottomFrame" scrolling="no" noresize="noresize" id="bottomFrame" />
</frameset>
<noframes>
<body>
<div id="dialog" style="display:none" >
	<iframe id="myiframe" name="myiframe" 
	src='' width="600px"
	 height="300px" frameborder="0" scrolling="none"></iframe>
</div>
</body>
</noframes>
</html>
