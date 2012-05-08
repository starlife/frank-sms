<!-- jsp/address/add.jsp -->
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/WEB-INF/jb-common.tld" prefix="jb" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>增加通讯录</title>
    <%@include file="/css.jsp" %>		
	<%@include file="/js.jsp" %>
	<script type="text/javascript">
	$(document).ready(function(){
		//这里写jquery
		$("#submitBtn").button();
		$("#cancelBtn").button();
	});
	setNavTitle("系统管理 >> 通讯录管理 >> 添加通讯录");
	</script>
	<s:head/>
  </head>

  <body>
  <div id="container">
  <s:form action="crudAddress!save" method="post" theme="simple">
  <table class="ui-widget">
  <thead class="ui-widget-header">
	  <tr>
	  	<td colspan="2">
	  	<s:if test="phoneAddress==null||phoneAddress.id==null">
	  		添加通讯录
	  	</s:if>
	  	<s:else>
		  		修改通讯录
		  		<s:hidden name="phoneAddress.id" value="%{phoneAddress.id}" />
		  	</s:else>
	  	</td>
	  </tr>
	  </thead>
	  <tr >
	  	<td>手机号码</td>
	  	<td><s:textfield name="phoneAddress.phonenumber" />
	  	<s:fielderror name="phoneAddress.phonenumber"/></td>
	  </tr>
	  <tr >
	  	<td>用户姓名</td>
	  	<td><s:textfield name="phoneAddress.name" /></td>
	  </tr>
	  <tr >
	  	<td>所属部门</td>
	  	<td><s:textfield name="phoneAddress.department" /></td>
	  </tr>
	  <tr >
	  	<td>所在地区</td>
	  	<td><s:textfield name="phoneAddress.area" /></td>
	  </tr>
	  <tr>
	  	<td colspan="2"><s:submit id="submitBtn" value="提 交"/>
	  	<s:submit value="返 回" id="cancelBtn" action="listAddress"/>
	  	</td>
	  </tr>
	  </table>
	</s:form>
	</div>	
  </body>
</html>
