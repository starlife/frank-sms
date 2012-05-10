<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/WEB-INF/jb-common.tld" prefix="jb" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>查看发信箱</title>
    <%@include file="/css.jsp" %>		
	<%@include file="/js.jsp" %>
	<script type="text/javascript">
	$(document).ready(function(){
		//这里写jquery
		$("#queryBtn").button();
		$("input[type='button']").button();
		
		$("#dialog:ui-dialog" ).dialog( "destroy" );
		$("#dialog").dialog({
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
		});
		
		
	});
		setNavTitle("系统管理 >> 彩信管理 >> 彩信列表");	
		function showMmsMethod(url)
		{
			alert(url);
			$("#showMms").attr("src",url);
			$("#dialog").dialog("open");
		}
	</script>
  </head>
  <body>
<div id="container">
<div id="dialog" style="display:none" >
	<iframe id="showMms" name="showMms" 
	src='' width="600px"
	 height="300px" frameborder="0" scrolling="none"></iframe>
</div>
	<s:form action="listMms" method="post" theme="simple">
	<div>
		<table class="ui-widget ui-widget-header">
			<tr>
				<td >开始时间</td>
				<td >
					<s:textfield name="queryBean.beginTime" 
					onfocus="WdatePicker({el:this,skin:'default',dateFmt:'yyyy-MM-dd HH:mm:ss'})"/>
				</td>
				<td >结束时间</td>
				<td  >
					<s:textfield name="queryBean.endTime" 
					onfocus="WdatePicker({el:this,skin:'default',dateFmt:'yyyy-MM-dd HH:mm:ss'})"/>
		
				<td ><s:submit id="queryBtn" value="查询"/></td>		
				
			</tr>
		</table>
		
	</div>
  	<hr/>
	<!-- 列表数据 -->
	<div>
		<table class="ui-widget">
		<thead class="ui-widget-header">
		<tr>
			<td width="15%">彩信标题</td>
			<td width="20%">彩信收件人</td>
			<td width="6%">彩信大小</td>
			<td width="6%">彩信帧数</td>
			<td width="15%">发送时间</td>
			<td width="10%">发送状态</td>	
			<td >操作</td>
		</tr>
		</thead>
		<tbody class="ui-widget-content">
		<s:iterator value="#request.page.list"> 
			<tr>
				<td ><s:property value="subject" default=" "/></td>
				<td ><s:property value="recipient" default=" "/></td>
				<td ><s:text name="global.format.size.k">
					<s:param value="mmsFile.mmsSize/1024.0" /></s:text></td>
				<td ><s:property value="mmsFile.frames" default=" "/></td>
				<td ><s:property value="sendtime" default=" "/></td>
				<td >
					<s:if test="status==0">未发送</s:if>
					<s:else>已发送</s:else>
					
				</td>
				<td>
				<!-- 定义url -->
				<s:url id="delURL" action="delMms">
					<s:param name="id" value="id"/>
				</s:url>
				<s:url id="showURL" action="showMms">
					<s:param name="id" value="mmsFile.id"/>
				</s:url>
				<s:url id="analysisURL" action="mmsAnalysis">
					<s:param name="ummsid" value="id"/>
				</s:url>
					<input type="button" value="查看彩信"
						onclick="showMmsMethod('<s:property value="%{#showURL}"/>')"/>
					<input type="button" value="删除"
						onclick="window.location.href='<s:property value="%{#delURL}"/>'"/>	
					
					<s:if test="status==1">
						<input type="button" value="发送报告"
							onclick="window.location.href='<s:property value="%{#analysisURL}"/>'"/>	
					</s:if>
				</td>
			</tr>
		</s:iterator>
		</tbody>
		</table>
		<s:if test="#request.page.list.size()==0">
		<table><tr ><td style="text-align:center;height:40px;">没有记录</td></tr></table>
		</s:if> 
		
	</div>
  	
		<jb:pager/>
			

		
	</s:form>	
</div>
 </body>
</html>
  	
