<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.frank.ylear.modules.mms.entity.*"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/jb-common.tld" prefix="jb" %>
<% 
	MmsFile mms=new MmsFile();
	mms.setContentSize(10000);
	mms.setFrames(5);
	mms.setMmsName("浙江旅游特刊");
	pageContext.setAttribute("item",mms);
%>
<script src="scripts/validate.js" ></script>
<script src="scripts/icommon.js" ></script>
<script src="scripts/DatePicker/WdatePicker.js" ></script>
<script src="scripts/jquery/jquery.js" ></script>
<html:errors/>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html lang="true">
  <head>
    <title>新建/编辑彩信</title>
	<link rel="stylesheet" type="text/css" href="css/styles.css" >
	<style type="text/css">
/*帧框css*/
#frames{height:300px; width:800px; overflow-x:auto;white-space:nowrap;}
/*设置行内联*/
.box{display:inline-block; text-align:left;margin-right: 10px;width:150px;height: 250px;border:red solid thin;
outline-style:dotted;
outline-color:#00ff00;
}
.box{*display:inline;margin-right: 10px;width:150px;height: 250px;
border:red solid thin;
outline-style:dotted;
outline-color:#00ff00;
}
/*每一帧内容*/
.frame{
	margin:10px 2px 1px 2px;
	border:solid thin;
	outline-style:dotted;
	outline-color:#999;
	height:210px;
}

</style>
<script type="text/javascript">
	function addframe()
	{
		var str='<div class="box"> <div id="id1" style="width:150px;height: 200px;background-color: #888;margin-right: 10px;"></div> </div>'
		var inner=document.getElementById("frames").innerHTML;
		document.getElementById("frames").innerHTML=inner+str;
	}
	function delframe()
	{
		alert("delframe");
	}
	
	function selectframe()
	{
		alert("selectframe");
	}
	
	/*上传图片*/
	function uploadPic()
	{
		alert("uploadPic");
	}
	
	/*上传铃声*/
	function uploadMuisc()
	{
		alert("uploadMuisc");
	}
	
</script>	
  </head>

  <body class="main">
  <html:form action="mms" method="post">
  	<html:hidden property="method" value="doAdd" />
  		<span style="font-weight:bold;">新建/编辑彩信</span>
		<table class="input_table" border="0" cellPadding="3" cellSpacing="0">
			<tr>
				<td class="input_title" style="width:20%;">彩信名称:<span style="color:red;">*</span></td>
				<td class="input_content">
					<html:text property="item.mmsName" size="50"/>
					
				</td>
			</tr>
			<tr>
			<td  class="input_title" width="20%;">选择要设置的帧：</td>
			<td class="input_content"><input class="input_content" type="button" value="添加帧" onclick="addframe();"></td>
			</tr>
			<tr height="300px;">
				<td colspan="2" class="input_content">
				<div style="background-color: #999">
				<div class="box"> 
				<div id="id1" style="width:150px;height: 200px;background-color: #888;margin-right: 10px;"></div> 
				
				</div>
					<!-- <iframe src="frames.html" 
					 scrolling="auto" frameborder="0"></iframe> -->
				</div>
				</td>
			</tr>
			<tr>
				<td class="input_title" width="20%;">本彩信总大小：</td>
				<td class="input_content" >93.35KB &nbsp &nbsp(注意：彩信总大小不能超过100KB)</td>
			</tr>
			<tr>
				<td class="input_title" width="20%;">当前帧大小:</td>		
				<td class="input_content" ><html:text property="item.contentSize"/></td>
			</tr>
			<tr>
				<td class="input_title" width="20%;">当前帧播放时间：</td>
				<td class="input_content"><input type="text" size="3">(秒)</td>
			</tr>
			<tr>
				<td class="input_title" width="20%;">设置图片：</td>
				<td class="input_content"><input type="file"  size="35" /></td>
			</tr>
			<tr>
				<td class="input_title" width="20%;">设置铃声：</td>
				<td class="input_content"><input type="file"  size="35" /></td>
			</tr>
			<tr>
				<td class="input_title" width="20%;">设置文字：</td>
				<td class="input_content" ><textarea cols=50 rows=6 onchange=""></textarea></td>
			</tr>
						
		</table>
		<div class="button_bar">
			<button  onclick="save();">保存</button>
		</div>
		<script>
		/*build_validate("item.recipient","接收手机号码不能为空","Limit","1","1000");
		build_validate("item.msgContent","短信内容不能为空","Limit","1","150");*/
		</script>
	</html:form>	
  </body>
</html:html>
