<%@ page language="java" pageEncoding="UTF-8"%>
<%@include file="/css.jsp"%>
<%@include file="/js.jsp"%>
<%@include file="/taglib.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>增加通讯录</title>
	<script type="text/javascript">
	$(document).ready(function(){
		//这里写jquery
		$("#submitBtn").button();
		$("#cancelBtn").button();
		setNavTitle("系统管理 >> 通讯录管理 >> 添加通讯录");
	});
	
		/**
		*选择父级单位
		**/
		function selectPosition(){
			var results =showModalDialog('com/frank/ylear/modules/unitInfo/selectParentPosition.action?date='+new Date(),'单位列表选择','dialogWidth:400px;dialogHeight:500px;dialogLeft:700px;dialogTop:250px;center:yes;help:yes;resizable:yes;status:yes');
			if(results!="" && results != undefined){
				var res = results.split(",");
				$("#parentPosName").val(res[1]);
				$("#positionID").val(res[0]);
			}
		}
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
	  	<td>用户姓名</td>
	  	<td><s:textfield name="phoneAddress.name" /></td>
	  </tr>
	   <tr >
	  	<td>职务</td>
	  	<td><s:textfield name="phoneAddress.post" /></td>
	  </tr>
	   <tr >
	  	<td>工作单位</td>
	  	<td >
			<s:textfield id="parentPosName" name="phoneAddress.TPosition.unitname" readonly="true" onclick="selectPosition()" maxLength="50"/>
			<s:hidden id="positionID" name="phoneAddress.TPosition.unitid" />
		</td>
	  </tr>
	  
	  
	  <!--
	  <tr >
	  	<td>部门</td>
	  	<td><s:textfield name="phoneAddress.department" /></td>
	  </tr>
	    -->
	  
	    
	  <tr >
	  	<td>手机号码</td>
	  	<td><s:textfield name="phoneAddress.phonenumber" />
	  	<s:fielderror name="phoneAddress.phonenumber"/></td>
	  </tr>
	   <tr >
	  	<td>电话号码</td>
	  	<td><s:textfield name="phoneAddress.homenumber" />
	  	<s:fielderror name="phoneAddress.homenumber"/></td>
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
