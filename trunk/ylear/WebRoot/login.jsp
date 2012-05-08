<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE HTML PUBLIC "-//W3C//Dtd HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>欢迎登录短彩管理平台</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<link rel="stylesheet" type="text/css" href="css/login.css">
	<%@include file="/jquery.jsp" %>
	<%@include file="/plugin_validate.jsp" %>
	<s:head/>   
	<style type="text/css">
	/*控制struts2错误输出格式*/
.errorMessage {
	list-style: none;
	padding: 0px;
	margin: 0px;
}

/*控制jquery-validate.js错误输出格式*/
form label.error {
	color: red;
	display: none;
}

form input.error {
	border: 1px dotted red;
}
	</style>
	<script>
	
	$(
		function() {
			$("#loginForm").validate({
				rules:{
                      "user.usrName":{required:true},
                      "user.usrPassword":{required:true}
                  },
                messages:{
                  	  "user.usrName":{required:"这个字段你必须填~~！"},
                  	  "user.usrPassword":{required:"这个字段你必须填~~！"}
                  }  
			});       
        }
    )
	function loginLoad()
	{
		if(top.location.href!=self.location.href)
		{
			top.location.href=self.location.href;
		}
	}
	function changeImage(url)
	{
		//alert("ppp");
		document.getElementById("codeimg").src=url+"?sid="+Math.random();
	}
	</script>
  </head>  
  <body  onload="loginLoad();">
  <div>	
  <table id="login" height="100%" cellSpacing="0" cellPadding="0" width="800" align="center">
    <tbody>
      <tr id="main">
        <td>
        <s:form action="loginAction" method="post" theme="simple" id="loginForm">
          <table height="100%" cellSpacing="0" cellPadding="0" width="100%" border="0">
            <tbody>
              <tr>
                <td width="380">&nbsp;</td>
                <td colSpan="3" >&nbsp;
                </td>
              </tr>
              <tr height="30">
                <td width="380">&nbsp;</td>
                <td width="80">&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
              </tr>
              <tr height="40">
                <td rowSpan="4">&nbsp;</td>
                <td>用户名：</td>
                <td class="textbox">
              	 <s:textfield id="usrName" name="user.usrName" size="18"/>	
                </td>
                <td width="120">&nbsp;</td>
              </tr>
              <tr height="40">
                <td>密　码：</td>
                <td class="textbox">
                <s:password id="usrPassword" name="user.usrPassword"/>
         
                </td>
                <td width="120">&nbsp;</td>
              </tr>
              <tr height="40">
                <td>验证码：</td>
                <td vAlign=center colSpan=2>
                  <s:textfield id="validateCode" name="validateCode" size="4"/>
                  &nbsp; 
            		<s:url id="validate" action="genValidateImage"></s:url>
					<img src="${validate}" id="codeimg" alt="请输入此验证码" style="width: 62px; height: 20px; border: 1px solid #abcedf;" />
            		<span id="linkbutton" onclick="javascript:changeImage('${validate}');">不清楚，再来一张</span>
            </td>
              </tr>
              <tr height="40">
                <td><span><s:actionerror /><s:fielderror />&nbsp;</span></td>
                <td align=right style="text-align: center ">
                  <s:submit id="loginBtn" value="登  录"/>
                </td>
                <td width="120">&nbsp;</td>
              </tr>
              <tr height="110">
                <td colSpan="4">&nbsp;</td>
              </tr>
            </tbody>
          </table>
          </s:form>
        </td>
      </tr>
      <tr height="104">
        <td>&nbsp;</td>
      </tr>
    </tbody>
  </table>
</div>
<div id="msg" style="DISPLAY: none"></div>

</body>
</html>
 
  	
  	
	
 
