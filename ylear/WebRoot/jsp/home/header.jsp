<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<script src="scripts/validate.js"></script>
<script src="scripts/icommon.js"></script>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<title>欢迎使用！</title>
		<%@include file="/css.jsp" %>	
	<%@include file="/js.jsp" %>
		<style type="text/css">
<!--
body {
	margin-left: 0px;
	margin-top: 0px;
	margin-right: 0px;
	margin-bottom: 0px;
}

.STYLE1 {
	font-size: 14px;
	color: #000000;
}

.STYLE5 {
	font-size: 12
}

.STYLE6 {
	font-size: 12px;
	color: #FFFFFF;
}

.STYLE7 {
	font-size: 14px;
	color: #FFFFFF;
}
#navtitle
{
padding-left: 10px;
font-size: 14px;
}
-->
</style>
	<script type="text/javascript">
	$(document).ready(function(){
		//这里写jquery
		$("#queryBtn").button();
		$("#addBtn").button();
		$(".editBtn").button();
		$(".delBtn").button();
		
	});
	self.setInterval("clock()",1000);
	function clock()
	{
	  var t=new Date();
	  var time=t.getFullYear()+"-"+(t.getMonth()+1)+"-"+t.getDate()+" "+t.toLocaleTimeString();
	  var objClock=document.getElementById("clock");
	  if(objClock)
	  {
	  	objClock.innerHTML="服务器时间："+time;
	  }
	}
	
	function logout(url)
	{
		top.location.href=url;
	}
	function changePassword(url)
	{
		top.frames["mainFrame"].location.href=url;
	}
	
</script>
	</head>
	<body>
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td height="57" background="images/main_03.gif">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td width="378" height="57" background="images/main_01.gif">
								&nbsp;
							</td>
							<td>
								&nbsp;
							</td>
							<td width="281" valign="bottom">
								<table width="100%" border="0" cellspacing="0" cellpadding="0">
									<tr>
										<td width="33" height="27">
											<img src="images/main_05.gif" width="33" height="27" />
										</td>
										<td width="248" background="images/main_06.gif">
											<table width="225" border="0" align="center" cellpadding="0"
												cellspacing="0">
												<tr>
													<td height="17">
														<div style="text-align: center; cursor: pointer;">
															<img src="images/pass.gif" width="69" height="17" 
															onclick="changePassword('<s:url action="changePasswordAction"/>');" />
														</div>
													</td>
													<td>
														<div style="text-align: center; cursor: pointer;">
															<img src="images/quit.gif" width="69" height="17"
																onclick="logout('<s:url action="logoutAction"/>');" />
														</div>
													</td>
												</tr>
											</table>
										</td>
									</tr>
								</table>
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td height="40" background="images/main_10.gif">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td width="194" height="40" background="images/main_07.gif">
								&nbsp;
							</td>
							<td>
								<table width="100%" border="0" cellspacing="0" cellpadding="0">
									<tr>
										<td width="21">
											&nbsp;
										</td>
										<td width="35" class="STYLE7">
											&nbsp;
										</td>
										<td width="21" class="STYLE7">
											&nbsp;
										</td>
										<td width="35" class="STYLE7">
											&nbsp;
										</td>
										<td width="21" class="STYLE7">
											&nbsp;
										</td>
										<td width="35" class="STYLE7">
											&nbsp;
										</td>
										<td width="21" class="STYLE7">
											&nbsp;
										</td>
										<td width="35" class="STYLE7">
											&nbsp;
										</td>
										<td width="21" class="STYLE7">
											&nbsp;
										</td>
										<td width="35" class="STYLE7">
											&nbsp;
										</td>
										<td>
											&nbsp;
										</td>
									</tr>
								</table>
							</td>
							<td width="248" background="images/main_11.gif">
								<table width="100%" border="0" cellspacing="0" cellpadding="0">
									<tr>
										<td width="16%">
											<span class="STYLE5"></span>
										</td>
										<td width="75%">
											<div align="center">
												<span class="STYLE6" id="clock"></span>
											</div>
										</td>
										<td width="9%">
											&nbsp;
										</td>
									</tr>
								</table>
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td height="30" background="images/main_31.gif">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td width="8" height="30">
								<img src="images/main_28.gif" width="8" height="30" />
							</td>
							<td width="147" background="images/main_29.gif">
								<table width="100%" border="0" cellspacing="0" cellpadding="0">
									<tr>
										<td width="24%">
											&nbsp;
										</td>
										<td width="43%" height="20" valign="bottom" class="STYLE1">
											管理菜单
										</td>
										<td width="33%">
											&nbsp;
										</td>
									</tr>
								</table>
							</td>
							<td width="39">
								<img src="images/main_30.gif" width="39" height="30" />
							</td>
							<td>
								<table width="100%" border="0" cellspacing="0" cellpadding="0">
									<tr>
										<td height="20" valign="bottom"><div id="navtitle"></div></td>
										<td valign="bottom" align="right">
											<span class="STYLE1">当前登录用户：${sessionScope.USER.usrName
												} &nbsp;(管理员)</span>
										</td>
									</tr>
								</table>
							</td>
							<td width="17">
								<img src="images/main_32.gif" width="17" height="30" />
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</body>
</html>
