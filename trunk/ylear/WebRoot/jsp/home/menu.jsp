<%@ page language="java" pageEncoding="UTF-8"%>
<%@include file="/css.jsp"%>
<%@include file="/js.jsp"%>
<%@include file="/taglib.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>无标题文档</title>
		<style type="text/css">
<!--
body {
	margin-left: 0px;
	margin-top: 0px;
	margin-right: 0px;
	margin-bottom: 0px;
}

a {
	text-decoration: none;
	color: #000000;
}

a:hover {
	color: #000000;
}


.STYLE3 {
	font-size: 12px;
	color: #435255;
}

#navigation {
	margin: 0px;
	padding: 0px;
	width: 147px;
	text-align: center;
}

#navigation span.head {
	cursor: pointer;
	background: url(images/main_34.gif) no-repeat scroll;
	display: block;
	font-weight: bold;
	margin: 0px;
	padding: 5px 0px 5px 0px;
	text-align: center;
	font-size: 14px;
	text-decoration: none;
	height: 33px;
}

#navigation ul {
	border-width: 0px;
	margin: 0px;
	padding: 0px;
	text-indent: 0px;
}

#navigation li {
	list-style: none;
	display: inline;
}

#navigation ul ul {
	list-style-image: url(images/menu_icon.gif) no-repeat;
}	
#navigation li li a {
	display: block;
	font-size: 12px;
	text-decoration: none;
	text-align: center;
	padding: 5px;
}

#navigation li li a:HOVER {
	background: url(images/tab_bg.gif);
	border: #adb9c2 solid 1px;
}
-->
</style>
<script type="text/javascript">
</script>
	</head>
	<body>
		<s:if test="#session.USER != null">
			<div id="navigation">
				<ul>
				<s:set name="rights" value="#session.USER.rights"/>
				<s:iterator value="rights">
						<s:if test="rightParentCode != 'ROOT_MENU'">
						
							<s:if test="parent==true && rightCode!='L01'">

								<li>
									<span class="head">${rightText}</span>
									<ul>
										<s:iterator  value="childRights">
											<li>
												<a title="${rightText}"
													href="${pageContext.request.contextPath}/${rightUrl}"
													target="mainFrame"> ${rightText} </a>
											</li>
										</s:iterator>
									</ul>
								</li>

							</s:if>
						</s:if>
					</s:iterator>
				</ul>
			</div>
		</s:if>
	</body>
</html>








