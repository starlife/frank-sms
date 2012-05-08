<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
    <title>发送彩信</title>
    <s:head/>
	<%@include file="/css.jsp" %>		
	<%@include file="/js.jsp" %>
	<script>
	//jquery
	$(document).ready(
		function(){
			
			$("#submitBtn").button();
			$("#cancelBtn").button();
			$("#addressBtn").button();
			$("#addMmsBtn").button();
			$("#mms\\.sendtimeStr").css({"display":"none"});//初始设为隐藏
			$("#sendby1").click(function(){$("#mms\\.sendtimeStr").css({"display":"none"});});
			$("#sendby2").click(function(){$("#mms\\.sendtimeStr").css({"display":""});});
			////初始化通讯类工具窗口
			$("#dialog:ui-dialog" ).dialog( "destroy" );
			$("#dialog").dialog({
				autoOpen:false,
				resizable: false,
				height:600,
				width:"60%",
				modal: true,
				title:'选择符合条件的号码',
				closeOnEscape:false,//关闭按 esc 退出
				overlay: {
					backgroundColor: '#000',
					opacity: 0.5
				}
			});
			
			//点击窗口打开iframe
			$("#addressBtn").click(
				function(){
					$("#addressTool").attr("src","listCustomAddress.action");
					$("#dialog").dialog("open");
				}
			);
		}
	);		
	setNavTitle("系统管理 >> 彩信管理 >> 群发彩信");		
	function  openWindow(url)
	{
		//window.open("mms.do?method=toMmsEdit","_blank",
		//alert(url);
		window.open(url,"_blank",
		"toolbar=yes, location=yes, directories=no, status=no, menubar=yes, scrollbars=yes, resizable=no, copyhistory=yes");
	}
	
	function addSelectedPhone_callback(phones)
	{
		var obj=$("#mms\\.recipient");
		var value=$(obj).val()+phones;
		$(obj).val(value.split(";").unique().join(";"));
		$("#dialog").dialog("close");
	}
	
	function addQueryPhone_callback(phones)
	{
		var obj=$("#mms\\.recipient");
		var value=$(obj).val()+phones;
		//过滤重复值
		$(obj).val(value.split(";").unique().join(";"));
		$("#dialog").dialog("close");
	}
		
	</script>
  </head>


<body>
<div id="container">
  <div id="dialog" style="font-size: 14px;display: none;">
	<iframe id="addressTool" name="addressTool" 
	src="" width="100%" 
	height="100%" frameborder="0" scrolling="no" ></iframe>
</div>

<s:form action="crudMms!save" method="post" theme="simple">
  <table class="ui-widget">
  <thead class="ui-widget-header">
	  <tr>
	  	<td colspan="2">彩信群发</td>
	  </tr>
	  </thead>
	  <tr >
	  	<td>彩信标题<font color="red">*</font></td>
	  	<td>
	  	<s:textarea name="mms.subject" cols="100" rows="3"></s:textarea>
	  	<s:fielderror   fieldName="mms.subject"/>
	  	
	  	</td>
	  </tr>
	  <tr>
	  	<td>彩信收件人<font color="red">*</font></td>
	  	<td><s:textarea id="mms.recipient"  name="mms.recipient" cols="100" rows="8"></s:textarea>
	  	<input id="addressBtn" type="button" value="打开通讯录" />
	  	<s:fielderror  fieldName="mms.recipient"/>	
		</td>
	  </tr>
	  
	  <tr>
	  	<td>彩信名称<font color="red">*</font></td>
	  	<td><div id="mmsName"></div>
	  	<s:textfield name="mms.mmsFile.id" id="mms.mmsFile.id"></s:textfield>
		<input id="addMmsBtn" type="button" value="新建编辑彩信" 
		onclick="openWindow('<s:url action="mmsEditor"/>');"/>	
		<s:fielderror  fieldName="mms.mmsFile.id"/>
		</td>
	  </tr>
	  
	  
	  <tr>
	  	<td>彩信大小(KB)<font color="red">*</font></td>
	  	<td><div id="mmsSize"></div>
		</td>
	  </tr>
	  
	   <tr>
	  	<td>帧数<font color="red">*</font></td>
	  	<td><div id="frameCount"></div>
		</td>
	  </tr>
	  
	  
	  <tr >
	  	<td>发送方式</td>
	  	<td><input type=radio id="sendby1" name="sendby"  checked />立即发送 
				<input type=radio id="sendby2" name="sendby" />定时发送
				<s:textfield id="mms.sendtimeStr" name="mms.sendtimeStr" size="25" 
				onfocus="WdatePicker({el:this,skin:'default',dateFmt:'yyyy-MM-dd HH:mm:ss'})"/>
				
		</td>
	  	
	  </tr>
	  <tr >
	  	<td colspan="2"><s:submit id="submitBtn" value="保 存"/>
	  	<s:submit value="取 消" id="cancelBtn" action="listMms"/>
	  	</td>
	  </tr>
	  </table>
	</s:form>
	</div>	
 
 
  </body>
</html>
  
  
			
		
