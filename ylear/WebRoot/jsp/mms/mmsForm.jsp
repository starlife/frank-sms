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
			$("input[type='button']").button();
			$("#submitBtn").button();
			$("#cancelBtn").button();
			//$("#addressBtn").button();
			//$("#addMmsBtn").button();
			$("#mms\\.sendtime").css({"display":"none"});//初始设为隐藏
			$("#sendby1").click(function(){$("#mms\\.sendtime").css({"display":"none"});});
			$("#sendby2").click(function(){$("#mms\\.sendtime").css({"display":""});});
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
			
			$("#dialog1").dialog({
				autoOpen:false,
				resizable: false,
				height:600,
				width:"60%",
				modal: true,
				title:'选择彩信',
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
			
			$("#selectMmsBtn").click(
				function(){
					$("#dialog1frame").attr("src","customListMmsFile.action");
					$("#dialog1").dialog("open");
				}
			);
			
			//表单验证
			jQuery.validator.addMethod("phonenumber", function(phone_number, element) {
					phone_number = phone_number.replace(/\s+/g, "");
					var patten = new RegExp(/^(((086)|(86))?[0-9]{11},*)+$/);
					return patten.test(phone_number);
				}, "电话号码格式有误");
			
			jQuery.validator.addMethod("maxphonecount", function(phone_number, element,param) {
					var array=phone_number.split(",");
					var count=0;
					for(i=0;i<array.length;i++)
					{
					 	if(array[i]!="")
					 	{
					 	 	count++;
					 	}
					}
					return (count<=param);
				}, "输入号码个数过多");
			
			var validator = $("#sendForm").validate({
				debug:false,
				rules:{
                      "mms.recipient":{
                      	required:true,
                      	maxphonecount:5,
                      	phonenumber:true
                      }
                  },
                messages:{
                  	  "sms.recipient":{
                  	  required:"接收手机号码不能为空！",
                  	  maxphonecount:"最多只能输入10000个号码",
                  	  phonenumber:"手机号码有误，请检查，多个号码之间以','分隔！"
                  	  }
                  }  
			});
			$("#reset").click(function() {
        		validator.resetForm();
    		});
    		setNavTitle("系统管理 >> 彩信管理 >> 群发彩信");	
		}
	);		
		
	
	function addSelectedPhone_callback(phones)
	{
		var obj=$("#mms\\.recipient");
		var value=$(obj).val()+phones;
		$(obj).val(value.split(",").unique().join(","));
		$("#dialog").dialog("close");
	}
	
	function addQueryPhone_callback(phones)
	{
		var obj=$("#mms\\.recipient");
		var value=$(obj).val()+phones;
		//过滤重复值
		$(obj).val(value.split(",").unique().join(","));
		$("#dialog").dialog("close");
	}
	
	function choiceMmsFile_callback(id,mmsName,frames,mmsSize)
	{
		document.getElementById("mmsName").value=mmsName;
		document.getElementById("mms.mmsid").value=id;
		document.getElementById("frameCount").innerHTML=frames;
		document.getElementById("mmsSize").innerHTML=mmsSize;
		$("#dialog1").dialog("close");
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

<div id="dialog1" style="font-size: 14px;display: none;">
	<iframe id="dialog1frame" name="dialog1frame" 
	src="" width="100%" 
	height="100%" frameborder="0" scrolling="no"  ></iframe>
</div>

<s:form id="sendForm" action="crudMms!save" method="post" theme="simple">
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
	  	<td >彩信收件人<font color="red">*</font></td>
	  	<td title="必填，可以手动输入，也可以从通讯录中查找，多个手机号码之间以逗号分隔"><s:textarea id="mms.recipient"  name="mms.recipient" cols="100" rows="8"></s:textarea>
	  	<input id="addressBtn" type="button" value="打开通讯录" />
	  	<s:fielderror  fieldName="mms.recipient"/>	
		</td>
	  </tr>
	  
	  <tr>
	  	<td>彩信名称<font color="red">*</font></td>
	  	<td>
	  	<s:textfield name="mmsName" id="mmsName"></s:textfield>
	  	<input id="selectMmsBtn" type="button" value="选择彩信" />	
		<div style="display: none"><s:textfield name="mms.mmsid" id="mms.mmsid"></s:textfield></div>
		<s:fielderror  fieldName="mms.mmsid"/>
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
				<s:textfield id="mms.sendtime" name="mms.sendtime" size="25" 
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
  
  
			
		
