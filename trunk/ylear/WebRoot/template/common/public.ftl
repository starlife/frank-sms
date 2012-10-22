<#--头部宏-->
<#macro html charset="UTF-8" lang="zh-CN">
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" >  
 	<#nested>
</html>
</#macro>

<#--head宏-->
<#macro head title="后台管理系统" >
  <head>
    
    <title>${title}</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	
	<#assign ctx = "${request.contextPath}" >
	
	<#nested>
	
  </head>
</#macro>

<#--body宏-->
<#macro body posArr=[] pos1Url="#" position1="系统首页">
  <body>
  	<#nested>
  <body>
</#macro>

<#--
----------------------------------------------------
-引入formvalidator验证宏
-注意，此宏是为了统一引入表单验证控件，地址，必须保证页面
-已经引入jquery核心js,另外必须保证页面已经引入head宏，推
-荐此宏写到head内
----------------------------------------------------
@author Memory @modified date 2012-05-21
-->
<#macro formValidator>
	<script type="text/javascript" src="${ctx}/js/formvalidator4.1.1/formValidator-4.1.1.js" charset="UTF-8"></script>
	<script type="text/javascript" src="${ctx}/js/formvalidator4.1.1/formValidatorRegex.js" charset="UTF-8"></script>
</#macro>


