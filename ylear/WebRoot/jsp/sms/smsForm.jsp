<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/WEB-INF/jb-common.tld" prefix="jb"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>发送短信</title>
		<s:head />
		<%@include file="/css.jsp"%>
		<%@include file="/js.jsp"%>
		<script>
	//jquery
	$(
		function(){
			$("#submitBtn").button();
			$("#reset").button();
			$("#addressBtn").button();
			
			$("#sms\\.sendtime").hide();//初始设为隐藏
			//立即发送
			$("#radio1").click(
				function(){
					$("#sms\\.sendtime").hide();
					$("#sms\\.sendtime").val("");
				}
			);
			//定时发送
			$("#radio2").click(
				function(){
					$("#sms\\.sendtime").css({"display":""});
				}
			);
			
			//初始化通讯类工具窗口
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
                      "sms.msgContent":{
                      	required:true,
                      	maxlength:300
                      },
                      "sms.recipient":{
                      	required:true,
                      	maxphonecount:5,
                      	phonenumber:true
                      }
                  },
                messages:{
                  	  "sms.msgContent":{
                  	  	required:"短信内容不能为空！",
                  	  	maxlength:'短信不能超过300个文字！'
                  	  },
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
			
			
			
			
		}
	);
	setNavTitle("系统管理 >> 短信管理 >> 群发短信");		
	
	/**
	* 添加号码回调函数
	*/
	function addSelectedPhone_callback(phones)
	{
		var obj=$("#sms\\.recipient");
		var value=$(obj).val()+phones;
		$(obj).val(value.split(",").unique().join(","));
		$("#dialog").dialog("close");
	}
	/**
	* 添加所有符合号码回调函数
	*/
	function addQueryPhone_callback(phones)
	{
		var obj=$("#sms\\.recipient");
		var value=$(obj).val()+phones;
		//过滤重复值
		$(obj).val(value.split(",").unique().join(","));
		$("#dialog").dialog("close");
	}
	function beforeSend()
	{
	 	var obj=document.getElementById("sendby2");
	 	//定时发送
	 	if(obj.checked)
	 	{
	 		var sendtime=document.getElementById("sms.sendtime");
	 		if(!sendtime.value)
	 		{
	 			alert("您选择了定时发送，请输入发送时间");
	 			return false;
	 		}
	 	}
	 	return true;
	 	
	}
	
	</script>
	</head>

	<body>
		<div id="container">

			<div id="dialog" style="display: none;">
				<iframe id="addressTool" name="addressTool" src="" width="100%"
					height="100%" frameborder="0" scrolling="no"></iframe>
			</div>
			<s:form id="sendForm" action="crudSms!save" method="post" theme="simple">
				<table class="ui-widget">
					<thead class="ui-widget-header">
						<tr>
							<td colspan="2">
								短信群发
							</td>
						</tr>
					</thead>
					<tbody class="ui-widget-content">
						<tr>
							<td>
								短信内容
								<font color="red">*</font>
							</td>
							<td title="必填,长度超过70个字符将自动用长短信形式下发">
								<s:textarea name="sms.msgContent" cols="100" rows="5"></s:textarea>
								<span>短信内容小于300个字符</span>
								<s:fielderror fieldName="sms.msgContent"/>
							</td>
						</tr>
						<tr>
							<td>
								接收手机号码
								<font color="red">*</font>
							</td>
							<td title="必填，可以手动输入，也可以从通讯录中查找，多个手机号码之间以逗号分隔">
								<s:textarea id="sms.recipient" name="sms.recipient" cols="100"
									rows="8"></s:textarea>
								<input id="addressBtn" type="button" class="button"
									value="打开通讯录" />
								<s:fielderror fieldName="sms.recipient" />
							</td>
						</tr>
						<tr>
							<td>
								发送方式
							</td>
							<td>
								<input type=radio id="radio1" name="sendby" checked />
								立即发送
								<input type=radio id="radio2" name="sendby" />
								定时发送
								<s:textfield id="sms.sendtime" name="sms.sendtime"
									size="25"
									onfocus="WdatePicker({el:this,skin:'default',dateFmt:'yyyy-MM-dd HH:mm:ss'})" />
							</td>

						</tr>
						<tr>
							<td colspan="2">
								<s:submit id="submitBtn" cssClass="button" value="保 存"/>
								<input type="reset" id="reset" value="取 消" />
							</td>
						</tr>
					</tbody>
				</table>
			</s:form>
		</div>
	</body>
</html>
