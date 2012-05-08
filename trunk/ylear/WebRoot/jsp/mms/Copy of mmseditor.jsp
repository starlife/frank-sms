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
		}
	)
	
	function GetXmlHttpObject()
	{
  		var xmlHttp=null;
  		try
    	{
	    // Firefox, Opera 8.0+, Safari
	    xmlHttp=new XMLHttpRequest();
	    }
 	 	catch (e)
    	{
		    // Internet Explorer
		    try
		      {
		      xmlHttp=new ActiveXObject("Msxml2.XMLHTTP");
		      }
		    catch (e)
		      {
		      xmlHttp=new ActiveXObject("Microsoft.XMLHTTP");
		      }
    	}
 		 return xmlHttp;
	}
	function cancelMethod()
	{

	  	xmlHttp=GetXmlHttpObject()
	  
	  	if (xmlHttp==null)
	    {
		    alert ("您的浏览器不支持AJAX！请升级您的浏览器");
		    return;
	    }
	
		var url=document.getElementById("closeurl").innerHTML;
		xmlHttp.onreadystatechange=callbackClose;
		xmlHttp.open("GET",url,true);
		xmlHttp.send("sid="+Math.random());
	}
	function callbackClose() 
	{ 
	  if (xmlHttp.readyState==4)
	  { 
	  	alert(xmlHttp.responseText);
	  	if("true"==xmlHttp.responseText)
	  	{
	  		//关闭窗口
	  		window.close();
	  	}
	  }
	}
	
	
	function saveMethod()
	{
		var mmsName=document.getElementById("mmsName").value;
		if(!mmsName)
		{
			alert("彩信名称不能为空");
			return;
		}
	  	xmlHttp=GetXmlHttpObject();
	  
	  	if (xmlHttp==null)
	    {
		    alert ("您的浏览器不支持AJAX！请升级您的浏览器");
		    return;
	    }
	
		var url=document.getElementById("saveurl").innerHTML;
		var mmsName=document.getElementById("mmsName").value;
		var param="mmsName="+mmsName;
		xmlHttp.open("POST",url,true);  
       	xmlHttp.setRequestHeader("Content-Type","application/x-www-form-urlencoded");  
        xmlHttp.onreadystatechange=callbackSave;
		xmlHttp.send(param); 
		
	}
	
	
	function callbackSave() 
	{ 
	  if (xmlHttp.readyState==4)
	  { 
	  	if("false"==xmlHttp.responseText)
	  	{
	  		alert("保存失败，请重试!");
	  	}else
	  	{
	  		//写数据到调用者页面，然后关闭自己
	  		var retStr=xmlHttp.responseText;
	  		var values=retStr.split(",");
	  		var mmsid= values[1];
	  		var frames=values[2];
	  		var mmsName=document.getElementById("mmsName").value;
	  		var mmsSize=document.getElementById("mmsSize").innerHTML;
	  		//需要写会彩信大小，彩信帧数，彩信名称
	  		window.opener.document.getElementById("mms.mmsName").innerHTML=mmsName;
	  		window.opener.document.getElementById("mms.mmsFile.id").value=mmsid;
	  		window.opener.document.getElementById("frames").innerHTML=frames;
	  		window.opener.document.getElementById("mmsSize").innerHTML=mmsSize;
	  		window.close();//关闭窗口
	  	}
	  }
	}
	
	
	/*addFrame,changeFrame,delFrame时回调*/
	function changeFrame_callback(frameid,frameSize,duringTime,frameText)
	{	
		//alert("changeFrame_callback"+id);
		setCurrentFrameId(frameid);//设置帧
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
	function chooseFrameMethod(frameid,url)
	{	
		$("#frameId").val(frameid);
		document.forms[0].action=url;
		//alert(document.forms[0].action);
		document.forms[0].submit();
	
		
	
	/*删除一帧*/
	function delFrameMethod(url)
	{	
		if(confirm("您确认删除当前帧吗？"))
		{
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
							<iframe id="ui" name="ui" src='<s:url action="mmsEditor!show"/>'
								width="100%" height="280px" frameborder="0" scrolling="none"></iframe>

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

							<span id="imagespan"><s:file id="image" name="image"
									size="35" /> </span>

							
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
									size="35"/> </span>
							
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
					<span id="closeurl" style="display: none"><s:url
							action="mmsEditor!close" /> </span>
					<input type="button" name="saveBtn" value="保存"
						onclick="saveMethod();" />
					<input type="button" name="cancelBtn" value="关闭"
						onclick="cancelMethod();" />
				</div>
			</s:form>
		</div>
	</body>
</html>
