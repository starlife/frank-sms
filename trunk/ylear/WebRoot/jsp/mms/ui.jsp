<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/WEB-INF/jb-common.tld" prefix="jb"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>彩信预览</title>
		<%@include file="/css.jsp"%>
		<style type="text/css">
/*帧框css*/ /*这个iframe居左，页撑满后出现水平滚动条*/
body {
	font-size: 14px;
	padding: 0px;
}

#container {
	position:absolute;
	top:5px;
	left:5px;
	padding: 0px;
	text-align: left;
	/*background-color:#eee;*/
	/*overflow: auto;*/
}

/*每一帧内容*/
.framecontent {
	width: 130px;
	height: 194px;
	text-align: center;
	vertical-align: top;
	border: 1px solid black;
	padding-top: 1px;
}

.framecontent.image {
	width: 126px;
	height: 115px;
	border-width: 0px;
	cursor: hand;
}

.framecontent.txt {
	width: 126px;
	height: 62px;
	text-align: left;
	overflow: auto;
	word-wrap: break-word;
	word-break: normal;
}

.framecontent.txt1 {
	width: 126px;
	height: 100%;
	text-align: left;
	overflow: auto;
	word-wrap: break-word;
	word-break: normal;
}

.framedesc {
	width: 128px;
	text-align: center;
}

.link {
	cursor: pointer;
	text-decoration: underline;
}
</style>
		<script type="text/javascript">
	/**************************下面是所有回调函数****************************/
	function chooseFrame_callback()
	{
		var frameid=document.getElementById("currentFrameId").innerHTML;
		var mmsSize=document.getElementById("mmsSize").innerHTML;
		var frameSize=document.getElementById("currentFrameSize").innerHTML;
		var duringTime=document.getElementById("currentDuringTime").innerHTML;
		var frameText=document.getElementById("currentFrameText").innerHTML;
		parent.chooseFrame_callback(frameid,mmsSize,frameSize,duringTime,frameText);
	}
	
	/*uploadImage,uploadAudio,uploadText时回调*/
	function upload_callback()
	{
		//帧大小，彩信大小
		var mmsSize=document.getElementById("mmsSize").innerHTML;
		var frameSize=document.getElementById("currentFrameSize").innerHTML;
		parent.upload_callback(mmsSize,frameSize);
	}
	
	
	/*clearImage时回调*/
	function clearImage_callback()
	{
		var mmsSize=document.getElementById("mmsSize").innerHTML;
		var frameSize=document.getElementById("currentFrameSize").innerHTML;
		parent.clearImage_callback(mmsSize,frameSize);
	}
	
	/*clearAudio时回调*/
	function clearAudio_callback()
	{
		var mmsSize=document.getElementById("mmsSize").innerHTML;
		var frameSize=document.getElementById("currentFrameSize").innerHTML;
		parent.clearAudio_callback(mmsSize,frameSize);
	}
	
	/*clearText时回调*/
	function clearText_callback()
	{
		var mmsSize=document.getElementById("mmsSize").innerHTML;
		var frameSize=document.getElementById("currentFrameSize").innerHTML;
		parent.clearText_callback(mmsSize,frameSize);
	}
	/**************************end****************************/
	</script>
	</head>
	<body>
		<div id="container">
			<s:set id="mmsFile" value="session.mmsfile"></s:set>
			<!-- ======================================================= -->
			<!-- 这里是一些需要保存的变量 -->
			<span id="mmsSize" style="display: none"> <s:text
					name="global.format.size.k">
					<s:param value="#mmsFile.mmsSize/1024.0" />
				</s:text> </span>
				
			<span id="currentFrameSize" style="display: none"> <s:text
								name="global.format.size.k">
								<s:param value="#mmsFile.currentFrameSize/1024.0" />
							</s:text> </span>	
			<span id="currentFrameId" style="display: none"/>${mmsFile.currentFrameId}</span>
			<span id="currentDuringTime" style="display: none"/>${mmsFile.currentDuringTime}</span>
			<span id="currentFrameText" style="display: none"/>${mmsFile.currentFrameText}</span>
			<!-- ======================================================= -->

			<table>
				<tr>

					<s:iterator value="#mmsFile.frameMap">
						
						<td>

							<table>
								<tr>
									<td align="center">
										<input name="radioselect" type="radio"
											<s:if test="key==#mmsFile.currentFrameId">
								checked="checked"
							</s:if>
											value="${key}" onclick="parent.chooseFrameMethod(${key},'<s:url action="mmsEditor!chooseFrame"/>');" />
										选择|
										<a class="link"
											onclick="parent.delFrameMethod(${key},'<s:url action="mmsEditor!delFrame"/>');">删除</a>

									</td>
								</tr>
								<tr>
									<td>
										<!-- 下面是每一帧的内容 -->
										<div class="framecontent">
											<s:if test="value.image!=null||value.text!=null">
												<table width="130px" height="188px;">
													<s:if test="value.image!=null">
														<s:set name="txtclass" value="'txt'" />
														<tr>
															<td>
																<img class="image" src="${value.image}" />
															</td>
														</tr>
													</s:if>
													<s:else>
														<s:set name="txtclass" value="'txt1'" />
													</s:else>
													<s:if test="value.text!=null">
														<tr>
															<td>
																<div class="<s:property value="#txtclass"/>">
																	<pre id='frameText${key}'>${value.text}</pre>
																</div>
															</td>
														</tr>
													</s:if>
												</table>
											</s:if>
										</div>
										<!-- 每一帧内容结束 -->
									</td>
								</tr>
								<tr>
									<td class="framedesc" >
										第${key}帧
									</td>
								</tr>

							</table>
						</td>
					</s:iterator>

				</tr>
			</table>
		</div>
		<!-- 执行回调函数 -->
		<s:if test="callbackMsg!=null">
			<script>${callbackMsg}</script>
		</s:if>


	</body>
</html>

