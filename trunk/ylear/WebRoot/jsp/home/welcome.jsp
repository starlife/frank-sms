<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html lang="true">
  <head>
    <title>欢迎使用！</title>
	<%@include file="/css.jsp" %>		
	<%@include file="/js.jsp" %>
	<script>
	$(
		function()
		{
			/*$(window.parent.document).find("#dialog:ui-dialog").dialog( "destroy" );
			$(window.parent.document).find("#dialog").dialog({
				bgiframe: true,
				autoOpen:false,
				resizable: false,
				height:400,
				width:600,
				modal: true,
				title:'查看彩信内容',
				closeOnEscape:false,//关闭按 esc 退出
				overlay: {
					backgroundColor: '#000',
					opacity: 0.5
				},
				buttons: {
					'关闭': function() {
						$(this).dialog('close');
					}
				}
			});*/
			
			
		}
		
	)
	function openwin()
	{
		alert('open');
		//$("#showMms").attr("src",url);
		//$(window.parent.document).find("#dialog").dialog("open");
		alert($(window.parent.document).find("#dialog")[0]);
	}
	</script>
  </head>
  <body>
  <hr size="1">
 	<p align="center">欢迎使用本管理平台</p>
  <hr size="1">
  </body>
</html:html>
