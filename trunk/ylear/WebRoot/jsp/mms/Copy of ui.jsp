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
			<s:hidden id="currentFrameId" name="currentFrameId" />
			<!-- ======================================================= -->

			<table>
				<tr>

					<s:iterator value="#mmsFile.frameMap">
						<!-- ======================================================= -->
						<!-- 记录frameSize -->
						<span id='frsize${key}' style="display: none"> <s:text
								name="global.format.size.k">
								<s:param value="value.frameSize/1024.0" />
							</s:text> </span>
						<!-- 记录帧播放时间 -->
						<span id='frameDuringTime${key}' style="display: none">${value.durTime}</span>
						<!-- ======================================================= -->

						<td>

							<table>
								<tr>
									<td align="center">
										<input name="radioselect" type="radio"
											<s:if test="key==currentFrameId">
								checked="checked"
							</s:if>
											value="${key}" onclick="chooseFrame(${key});" />
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

