<!-- jsp/mms/mmseditor.jsp -->
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/WEB-INF/jb-common.tld" prefix="jb"%>
<html>
	<head>
		<title>新建/编辑彩信</title>
		<%@include file="/css.jsp"%>
		<%@include file="/js.jsp"%>
		<style type="text/css">
#imagespan {
	
}

#audiospan {
	
}
</style>

		<script type="text/javascript">
	$(
		function()
		{
			//$("tr>td:odd").addClass("ui-widget-header");
			$("input[type='button']").button();
			$("#ui").attr("src",$("#iframeURL").html());
		}
	)
	
	
	function cancelMethod()
	{
		window.location.href="listMmsFile.action";
	}  	
	
	
	
	function saveMethod()
	{
		var mmsName=$("#mmsName").val();
		if(!mmsName)
		{
			alert("彩信名称不能为空");
			return;
		}
	  	
	  	var url=$("#saveurl").html();
		$.post(url,{"mmsName":mmsName},function(result){
			var values=result.split(",");
			if(values[0]=="false")
  			{
  				var tip="";
  				if(values[1])
  				{
  					tip=values[1];
  					
  				}
  				if(tip=="mmsName.null")
  				{
  						alert("彩信名称不能为空");
  				}
  				else if(tip=="mmsSize.null")
  				{
  					alert("彩信大小不能为空");
  				}
  				else
  				{
  					alert("保存失败，请重试!");
  				}
  			}else
  			{
  				if(window.opener)
  				{
			  		//写数据到调用者页面，然后关闭自己
			  		var retStr=result;
			  		var values=retStr.split(",");
			  		var mmsid= values[1];
			  		var frameCount=values[2];
			  		var mmsSize=$("#mmsSize").html();
			  		//需要写回彩信大小，彩信帧数，彩信名称，彩信id
			  		window.opener.document.getElementById("mmsName").value=mmsName;
			  		window.opener.document.getElementById("mms.mmsid").value=mmsid;
			  		window.opener.document.getElementById("frameCount").innerHTML=frameCount;
			  		window.opener.document.getElementById("mmsSize").innerHTML=mmsSize;
			  		window.close();//关闭窗口
		  		}else
		  		{
		  			//alert("彩信保存成功"+result);
		  			window.location.href="listMmsFile.action";
		  		}
  			}
		})
		
	}
	
	function test()
	{
		if(window.opener)
		{
			var element=window.opener.document.getElementById("mmsName");
			if(element);
			alert(window.opener.document.getElementById("mmsName1").innerHTML);
		}else
		{
			alert("彩信保存成功");
		}
  		//需要写回彩信大小，彩信帧数，彩信名称，彩信id
  		//window.opener.document.getElementById("mmsName").innerHTML=mmsName;
  		//window.opener.document.getElementById("mms.mmsid").value=mmsid;
  		//window.opener.document.getElementById("frameCount").innerHTML=frameCount;
  		//window.opener.document.getElementById("mmsSize").innerHTML=mmsSize;
  		//window.close();//关闭窗口
  		
	}
	

	/**************************下面是所有回调函数****************************/
	/*addFrame,changeFrame,delFrame时回调*/
	function choiceFrame_callback(frameid,mmsSize,frameSize,duringTime,frameText)
	{	
		//alert("changeFrame_callback"+id);
		setCurrentFrameId(frameid);//设置帧
		setMmsSize(mmsSize);
		setCurrentFrameSize(frameSize);//设置帧大小
		setCurrentDuringTime(duringTime);//设置帧播放时间
		setCurrentFrameText(frameText);//设置帧文本
		clearImage();//清空上传文件路径
		clearAudio();//清空上传文件路径
	}
	/*uploadImage,uploadAudio,uploadText时回调*/
	function upload_callback(mmsSize,frameSize)
	{
		//帧大小，彩信大小
		setMmsSize(mmsSize);
		setCurrentFrameSize(frameSize);//设置帧大小
	}
	
	
	/*clearImage时回调*/
	function clearImage_callback(mmsSize,frameSize)
	{
		setMmsSize(mmsSize);
		setCurrentFrameSize(frameSize);//设置帧大小
		clearImage();//清空上传文件路径
	}
	
	/*clearAudio时回调*/
	function clearAudio_callback(mmsSize,frameSize)
	{
		setMmsSize(mmsSize);
		setCurrentFrameSize(frameSize);//设置帧大小
		clearAudio();//清空上传文件路径
	}
	
	/*clearText时回调*/
	function clearText_callback(mmsSize,frameSize)
	{
		setMmsSize(mmsSize);
		setCurrentFrameSize(frameSize);//设置帧大小
		clearText();//清空上传文件路径
	}
	/**************************end****************************/
	
	
	/********************start*****************************/
	function clearImage()
	{
		var html=document.getElementById("imagespan").innerHTML;
		document.getElementById("imagespan").innerHTML=html;
	}
	
	function clearAudio()
	{
		var html=document.getElementById("audiospan").innerHTML;
		document.getElementById("audiospan").innerHTML=html;
	}
	function clearText()
	{
		$("#frameText").val("");
	}
	
	function setCurrentFrameId(frameid)
	{
		$("#frameid").val(frameid);
	}
	
	function setCurrentFrameSize(currentFrameSize)
	{
		$("#currentFrameSize").html(currentFrameSize);
	}
		
	function setCurrentFrameText(currentFrameText)
	{		
		$("#frameText").val(currentFrameText);
	}
	
	function setCurrentDuringTime(currentDuringTime)
	{
		$("#duringTime").val(currentDuringTime);
	}
	
	function setMmsSize(mmsSize)
	{
		$("#mmsSize").html(mmsSize);
	}
	
	/******************** end*****************************/
	
	
	function  addFrameMethod(url)
	{
		document.forms[0].action=url;
		//alert(document.forms[0].action);
		document.forms[0].submit();
	}
	
	/*选择另一帧*/
	function choiceFrameMethod(frameid,url)
	{	
		$("#frameId").val(frameid);
		document.forms[0].action=url;
		//alert(document.forms[0].action);
		document.forms[0].submit();
	
		
	}
	/*删除一帧*/
	function delFrameMethod(frameid,url)
	{	
		if(confirm("您确认删除当前帧吗？"))
		{
			$("#frameId").val(frameid);
			document.forms[0].action=url;
			//alert(document.forms[0].action);
			document.forms[0].submit();
		}
		
	}
	
	function uploadImageMethod(url)
	{
		document.forms[0].action=url;
		//alert(document.forms[0].action);
		document.forms[0].submit();	
	}
	
	function clearImageMethod(url)
	{
		document.forms[0].action=url;
		//alert(document.forms[0].action);
		document.forms[0].submit();
		
	}
	
	function uploadAudioMethod(url)
	{
		document.forms[0].action=url;
		//alert(document.forms[0].action);
		document.forms[0].submit();
		
	}
	
	function clearAudioMethod(url)
	{
		document.forms[0].action=url;
		//alert(document.forms[0].action);
		document.forms[0].submit();
		
	}
	
	function uploadTextMethod(url)
	{
		document.forms[0].action=url;
		//alert(document.forms[0].action);
		document.forms[0].submit();
		
	}
	
	function clearTextMethod(url)
	{
		document.forms[0].action=url;
		//alert(document.forms[0].action);
		document.forms[0].submit();
		
	}
	
	
	function changeDuringTimeMethod(url)
	{
		document.forms[0].action=url;
		//alert(document.forms[0].action);
		document.forms[0].submit();
	}
	
	
	
</script>
	</head>


	<body>
		<div id="container">
			<s:form action="mmsEditor!save" method="post" theme="simple"
				target="ui" enctype="multipart/form-data">
				<s:hidden id="frameId" name="frameId" />
				<h1 class="ui-state-default">
					新建/编辑彩信
				</h1>
				<table class="ui-widget">
					<tr>
						<td>
							彩信名称:
							<font color="red">*</font>
						</td>
						<td>
							<s:textfield name="mmsName" size="50" id="mmsName" />
						</td>
					</tr>
					<tr>
						<td>
							选择要设置的帧：
						</td>
						<td>
							<input type="button" name="addFrameBtn" value="添加帧"
								onclick="addFrameMethod('<s:url action="mmsEditor!addFrame"/>');" />

						</td>
					</tr>
					<tr>
						<td colspan="2">
							<span id="iframeURL" style="display:none"><s:url action="mmsEditor!showui"/></span>
							<iframe id="ui" name="ui" src=''
								width="100%" height="280px" frameborder="0" scrolling="auto"></iframe>

						</td>
					</tr>
					<tr>
						<td class="input_title" width="20%;">
							本彩信总大小：
						</td>
						<td class="input_content">
							<span id="mmsSize"></span> (注意：彩信总大小不能超过100KB)
						</td>
					</tr>
					<tr>
						<td>
							当前帧大小:
						</td>
						<td class="input_content">
							<span id="currentFrameSize"></span>

						</td>
					</tr>
					<tr>
						<td>
							当前帧播放时间：
						</td>
						<td>
							<s:textfield name="duringTime" size="3" id="duringTime" />
							(秒)
							<input type="button" name="changeDuringTimeBtn" value="更改"
								onclick="changeDuringTimeMethod('<s:url action="mmsEditor!changeDuringTime"/>');" />
						</td>
					</tr>
					<tr>
						<td>
							设置图片：
						</td>
						<td>

							<span id="imagespan" class="ui-state-default"><s:file id="image" name="image"
									size="35" class="ui-state-default"/> </span>


							<input type="button" name="uploadImageBtn" value="上传"
								onclick="uploadImageMethod('<s:url action="mmsEditor!uploadImage"/>');" />

							&nbsp;
							<input type="button" name="clearImageBtn" value="清除"
								onclick="clearImageMethod('<s:url action="mmsEditor!clearImage"/>');" />
						</td>
					</tr>
					<tr>
						<td>
							设置铃声：
						</td>
						<td>
							<span id="audiospan"><s:file id="audio" name="audio"
									size="35" /> </span>

							<input type="button" name="uploadAudioBtn" value="上传"
								onclick="uploadAudioMethod('<s:url action="mmsEditor!uploadAudio"/>');" />

							&nbsp;
							<input type="button" name="clearAudioBtn" value="清除"
								onclick="clearAudioMethod('<s:url action="mmsEditor!clearAudio"/>');" />


						</td>
					</tr>
					<tr>
						<td>
							设置文字：
						</td>
						<td class="input_content">
							<s:textarea name="frameText" cols="50" rows="6" id="frameText"></s:textarea>

							<input type="button" name="uploadTextBtn" value="确认"
								onclick="uploadTextMethod('<s:url action="mmsEditor!uploadText"/>');" />

							&nbsp;
							<input type="button" name="clearTextBtn" value="清空"
								onclick="clearTextMethod('<s:url action="mmsEditor!clearText"/>');" />

						</td>
					</tr>

				</table>
				<div style="padding-top: 10px;">
					<span id="saveurl" style="display: none"><s:url
							action="mmsEditor!save" /> </span>
					
					<input type="button" name="saveBtn" value="保 存"
						onclick="saveMethod();" />
					<input type="button" name="cancelBtn" value="返 回"
						onclick="cancelMethod();" />
						
						
				</div>
			</s:form>
		</div>
	</body>
</html>
