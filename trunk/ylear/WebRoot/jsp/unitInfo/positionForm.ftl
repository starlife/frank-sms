<#include "/template/common/public.ftl" />
<@html>
	<@head title="单位表单">
	
		<link href="${ctx}/css/themes/redmond/jquery.ui.all.css" type="text/css" rel="stylesheet">
		<link href="${ctx}/css/custom.css" type="text/css" rel="stylesheet">
		<link href="${ctx}/css/reset.css" type="text/css" rel="stylesheet">
		
		<script type="text/javascript" src="${ctx}/js/global.js"></script>
		<script type="text/javascript" src="${ctx}/js/jquery-1.7.1.min.js"></script>
		<script type="text/javascript" src="${ctx}/js/jquery.ztree.core-3.4.js"></script>
		<script type="text/javascript" src="${ctx}/js/jquery.ztree.excheck-3.4.js"></script>
		
		<#--formvalidator验证-->
		<@formValidator />

		<@s.head />
		
	<script type="text/javascript">
		$(document).ready(function(){
			<#if Session["flag"] == 'add'>
	    		setNavTitle("系统管理 >> 单位维护 >> 新增单位");
	    	<#else>
	    		setNavTitle("系统管理 >> 单位维护 >> 修改单位");
	    	</#if>
		});
	
		function setNavTitle(title){
			try{
				window.top.frames["topFrame"].document.getElementById("navtitle").innerHTML=title;
			}
			catch(err){
			//alert("error");
			}
		} 
	</script>
	</@head>
	
	<#if Session["flag"] == 'add'>
		<#assign positions = ["通讯录","单位维护","新增单位"] />
	<#else>
		<#assign positions = ["通讯录","单位维护","修改单位"] />
	</#if>
	
	<@body posArr = positions>
		<#-- 消息提示 -->	
		<@s.actionerror />
		<@s.fielderror />
		<#-- 表单 -->
		<@s.form name="positionForm" id="positionForm" action="savePosition" validate="flase" theme="simple">
		<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0"  class="ui-widget">
			<thead class="ui-widget-header">
			  <tr>
			  	<td colspan="3">
			  		<#if Session["flag"] == 'add'>
			        	新增单位
			        <#else>
			        	修改单位
			        </#if>
			  	</td>
			  </tr>
	  		</thead>
	  		 <tr >
			  	<td class="odd_td" width="150px;">
			      	<label><font color="red" size="2">*</font>&nbsp;单位名称：</label>
		        </td>
			    <td width="300px;">
			      	<@s.textfield id="posName" name="position.unitname" maxlength="50" />
			    </td>
			    <td>
			      	<div id="posNameTip"></div>
			    </td>
			  </tr>
			   <tr >
			  	<td class="odd_td" width="300px;"><LABEL><font color="red" size="2">*</font>&nbsp;上级单位：</LABEL></td>
				<td >
					<@s.textfield id="parentPosName" name="position.TPositionParent.unitname" readonly="true" onclick="selectPosition()" maxLength="50"/>
					<@s.hidden id="positionID" name="position.TPositionParent.unitid" />
				</td>
				<td>
			      	<div id="parentPosNameTip"></div>
			    </td>
			  </tr>
			  <tr>
			  	<td colspan="3" align="center">
			  		<input  name="button" type="submit" class="ui-button ui-widget ui-state-default ui-corner-all"  value="保 存" />
		          	<input name="reset" type="reset" class="ui-button ui-widget ui-state-default ui-corner-all" value="返 回" onClick="history.back();" />
			  	</td>
			  </tr>
		</table>
		</@s.form>
	</@body>
	<script type="text/javascript">
		$(document).ready(function(){
			$.formValidator.initConfig({formID:"positionForm",theme:"ArrowSolidBox",submitOnce:true,
				onError:function(){
					alert("校验不通过，请参考页面错误提示！");
				},
				ajaxPrompt : '有数据正在异步验证，请稍等...'
			});
			
			$("#posName").formValidator({onFocus:"请输入单位名称,不能为空"})
						.inputValidator({min:1, max:50, empty:{leftEmpty:false,rightEmpty:false,emptyError:"单位名词两边不能有空符号"},
										onErrorMin:"单位名称不能为空,请填写", onErrorMax:"单位名词最多50个字符"});
						
			$("#parentPosName").formValidator({onFocus:"请选择上级单位"})
								.inputValidator({min:1,
								  					empty:{leftEmpty:false,rightEmpty:false,emptyError:"上级单位两边不能有空符号"},
								  					onErrorMin:"上级单位不能为空"});
			
			$("#unitTel").formValidator({empty:true, onFocus:"请输入单位固话，格式如：0571-00000000"})
						.regexValidator({regExp:"tel", dataType:"enum", onError:"单位固话格式不规范，请参考：0571-00000000"});
						
			$("#unitFax").formValidator({empty:true, onFocus:"请输入单位传真，格式如：0571-00000000"})
						.regexValidator({regExp:"tel", dataType:"enum", onError:"单位传真格式不规范,请参考：0571-00000000"});
						
			$("#orderNum").formValidator({empty:true, onFocus:"请输入单位排序，可由系统自动生成排序"})
						.regexValidator({regExp:"num1", dataType:"enum", onError:"单位排序必须是正整数"});					
		});
		
		/**
		*选择父级单位
		**/
		function selectPosition(){
			var results =showModalDialog('../unitInfo/selectParentPosition.action?date='+new Date(),'上级单位选择','dialogWidth:400px;dialogHeight:500px;dialogLeft:700px;dialogTop:250px;center:yes;help:yes;resizable:yes;status:yes');
			if(results!="" && results != undefined){
				var res = results.split(",");
				$("#parentPosName").val(res[1]);
				$("#positionID").val(res[0]);
			}
		}
		
		
	</script>
</@html>	
