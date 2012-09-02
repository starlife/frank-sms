<%@ page language="java" pageEncoding="UTF-8"%>
<%@include file="/css.jsp"%>
<%@include file="/js.jsp"%>
<%@include file="/taglib.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>读取手机报彩信</title>
		<s:head />
		<script>
	//jquery
	$(document).ready(
		function(){
			$("input[type='button']").button();
			$("input[type='reset']").button();
			$("input[type='submit']").button();
			//$("#submitBtn").click(getMms());
			
		}
	);		
		
	
	function getMms()
	{
		var mtype=$('input[name="MType"][checked]').val();
		var title=$("#Title").val();
		if(!title)
		{
			alert("标题不能为空");
			$("#Title").focus();
			return;
		}
	  	var url=$("#url").html();
		$.post(url,{"mType":mtype,"title":title},function(result){
			if(result=="false")
  			{
  				alert("读取彩信失败，请检查输入的手机报标题是否正确");
  			}else
  			{
  				if(window.opener)
  				{
			  		window.close();//关闭窗口
			  		window.opener.location.href="listMmsFile.action";
		  		}else
		  		{
		  			//alert("彩信保存成功"+result);
		  			window.location.href="listMmsFile.action";
		  		}
  			}
		},"text") //加上type="text" 是为了兼容ff浏览器
		
	}
	
	function cancelMethod()
	{
		window.location.href="listMmsFile.action";
	}  
	
	
		
	</script>
	</head>


	<body>
		<div id="container">
			<s:form id="sendForm" action="" method="post"
				theme="simple">
				<table class="ui-widget">
					<thead class="ui-widget-header">
						<tr>
							<td colspan="2">
								读取手机报彩信
							</td>
						</tr>
					</thead>
					<tr>
						<td>
							手机报类型
							<font color="red">*</font>
						</td>
						<td>
							<input type="radio" name="MType" value="0" checked="checked"/>手机报政务版
							<input type="radio" name="MType" value="1"/>手机报专刊

						</td>
					</tr>
					<tr>
						<td>
							手机报标题
							<font color="red">*</font>
						</td>
						<td title="这里填写第几期或者专刊的标题，如果是政务版的,就是:期数  如果是专刊的 就是标题">
							<input  type="text" id="Title" name="Title"/>					
						</td>
					</tr>
					<tr>
						<td colspan="2">
							<span id="url" style="display: none"><s:url
							action="3gMms!mmsFrom3g" /> </span>
							<input type="button" id="submitBtn" value="读 取" onclick="getMms()"/>
							<input type="button" name="cancelBtn" value="返 回"
						onclick="cancelMethod();" />
						</td>
					</tr>
				</table>
			</s:form>
		</div>


	</body>
</html>




