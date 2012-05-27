<!-- jsp/mms/mmseditor.jsp -->
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/WEB-INF/jb-common.tld" prefix="jb"%>
<html>
	<head>
		<title>彩信查看器</title>
		<%@include file="/css.jsp"%>
		<%@include file="/js.jsp"%>
		<style type="text/css">
/*帧框css*/ /*这个iframe居左，页撑满后出现水平滚动条*/
#mycontainer {
	text-align: left;
	overflow: auto;
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

.framediscription {
	width: 128px;
	text-align: center;
}

.link {
	cursor: pointer;;
	text-decoration: underline;
}
</style>
		<script type="text/javascript">
	$(
		function()
		{
			//$("tr>td:odd").addClass("ui-widget-header");
			$(".button").button();
			$("input.button").css("font-size","16px");
		}
	)
	
	/*点击单选框 选中某一帧时调用*/
	function chooseFrame(id)
	{
		//alert(id);
		//setCurrentFrameSize(id);//设置帧大小
		//setCurrentDuringTime(id);//设置帧播放时间
		$("#currentFrameSize").html($("#frameSize"+id).html());
		$("#currentFrameDuringTime").html($("#frameDuringTime"+id).html());
	}
	
	
	/*function setCurrentFrameSize(id)
	{
		var currentFrameSize=document.getElementById("frameSize"+id).innerHTML;
		document.getElementById("currentFrameSize").innerHTML=currentFrameSize;
		
	}*/
	
	
	/*function setCurrentDuringTime(id)
	{
		var currentDuringTime=document.getElementById("frameDuringTime"+id).innerHTML;
		alert(currentDuringTime);
		document.getElementById("currentFrameDuringTime").innerHTML=currentDuringTime;
	}*/
	
	
	
	
</script>
	</head>
	<body>
		<div id="container">
			<s:set id="mmsFile" value="session.showmms"></s:set>
			<table class="ui-widget">
				<tr>
					<td>
						彩信名称:
						<font color="red">*</font>
					</td>
					<td>
						<s:property value="#mmsFile.mmsName" />
					</td>
				</tr>

				<tr>
					<td colspan="2">
						<div id="mycontainer">

							<table class="reset">
								<tr>
									<s:iterator value="#mmsFile.frameMap">
										<!-- 记录frameSize -->
										<span id='frameSize${key}' style="display: none"> <s:text
												name="global.format.size.k">
												<s:param value="value.frameSize/1024.0" />
											</s:text> </span>
										<!-- 记录帧播放时间 -->
										<span id='frameDuringTime${key}' style="display: none">${value.duringTime}</span>
										<td>

											<table style="border: 0px;">
												<tr>
													<td align="center">
														<input name="radioselect" type="radio"
															<s:if test="key==1">
														checked="checked"
													</s:if>
															value="${key}" onclick="chooseFrame(${key});" />
														选择
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
																					<pre id='frtxt${key}'>${value.text}</pre>
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
													<td class="framediscription">
														第${key}帧
													</td>
												</tr>

											</table>
										</td>
									</s:iterator>

								</tr>
							</table>
						</div>

					</td>
				</tr>
				<tr>
					<td width="20%;">
						本彩信总大小：
					</td>
					<td class="input_content">
						<span id="mmsSize"> <s:text name="global.format.size.k">
								<s:param value="#session.showmms.mmsSize/1024.0" />
							</s:text> </span>
					</td>
				</tr>
				<tr>
					<td>
						当前帧大小:
					</td>
					<td>
						<span id="currentFrameSize"></span>
					</td>
				</tr>
				<tr>
					<td>
						当前帧播放时间：
					</td>
					<td>
						<span id="currentFrameDuringTime"></span>(秒)
					</td>
				</tr>

			</table>


		</div>
		<s:if test="session.showmms.frameMap.size()>0">
			<script>chooseFrame(1)</script>
		</s:if>
	</body>
</html>
