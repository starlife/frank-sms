<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/WEB-INF/jb-common.tld" prefix="jb" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>发送结果分析</title>
	<%@include file="/css.jsp" %>		
	<%@include file="/js.jsp" %>
	<script type="text/javascript">
	$(document).ready(function(){
		//这里写jquery
		$("#queryBtn").button();
		$("#backBtn").button();
		$("input[type='button']").button();
		
	});
		
		function doShowMms(mmsid)
		{
			window.location.href = "?method=doShowMms&mmsid=" + mmsid;
		}
	</script>
  </head>
  <body>
<div id="container">
	<div>
		<table class="ui-widget ui-widget-header" >
			<tr height="25px;">
				<td width="20%">总接收号码个数：</td>
				<td width="30%"><s:property value="analysisBean.allCount+'（个）'" default=" "/>	
				</td>
				<td width="20%">接收成功数：</td>
				<td width="30%" >
					<s:property value="analysisBean.successCount+'（个）'" default=" "/>	
				</td>					
			</tr>
			<tr height="25px;">
				<td >等待接收数：</td>
				<td >
					<s:property value="analysisBean.unknowCount+'（个）'" default=" "/>
				</td>
				<td >接收失败数：</td>
				<td >
					<s:property value="analysisBean.failCount+'（个）'" default=" "/>	
				</td>					
			</tr>
		</table>
		
	</div>
	<s:form action="mmsAnalysis" method="post" theme="simple">
	<s:hidden name="queryBean.ummsid"/>
	<div>
		<table class="ui-widget ui-widget-header" >
			<tr>
				<td >接收号码：</td>
				<td >
					<s:textfield name="queryBean.toAddress" />
				</td>
				<td >发送状态：<s:select list="#{-1:'全部',0:'接收成功',1:'接收失败'}" 
				name="status" listKey="key" listValue="value"></s:select> &nbsp;
					用户读状态:<s:select list="#{-1:'全部',0:'已读取',1:'未读被删'}" 
				name="readstatus" listKey="key" listValue="value"></s:select>
				</td>
				<td ><s:submit id="queryBtn" value="查询"/></td>		
				
			</tr>
		</table>
		
	</div>
  	<hr/>
	<!-- 列表数据 -->
	<div>
		<table class="ui-widget">
		<thead class="ui-widget-header" >
		<tr>
			<td >手机号码</td>
			<td >发送状态</td>
			<td >状态描述</td>
			<td >发送时间</td>
			<td >操作</td>
		</tr>
		</thead>
		<tbody class="ui-widget-content">
		<s:iterator value="#request.page.list">
			<!-- 已经收到了读报告，则发送成功 -->
			<s:if test="readstatus!=null">
				<s:set id="status" value="'接收成功'"/>
				<s:set id="readstatus" value="%{readstatustext}"/>
			</s:if>
			<!-- 如果没收到发送回应包或者收到了回应失败包（不等于1000） -->
			<s:elseif test="messageid==null||statuscode==null||statuscode!=1000">
				<s:set id="status" value="'接收失败'"/>
				<s:set id="readstatus" value="'发送失败'"/>
			</s:elseif>
			<!-- 如果收到了回应成功包，且收到了提交状态包但提交失败，还是应该标记为发送失败 -->
			<s:elseif test="mmstatus!=null&&mmstatus!=1">
				<s:set id="status" value="'接收失败'"/>
				<s:set id="readstatus" value="%{mmstatustext}"/>
			</s:elseif>
			<!-- 如果以上条件都不符合，那么就只有收到了回应成功包，但是提交报表未收到 -->
			<s:else>
				<s:set id="status" value="'等待接收'"/>
				<s:set id="readstatus" value="'未获取状态报告'"/>
			</s:else>
			<tr class="ui-widget-content" align="center">
				<td ><s:property value="toAddress"/></td>
				<td ><s:property value="#status"/></td>
				<td ><s:property value="#readstatus" default="无"/></td>
				<td ><s:date name="sendtime" format="yyyy-MM-dd HH:mm:ss"/></td>
				<td>
				<!-- 定义url -->
				<s:url id="delURL" action="delMmsSubmit">
					<s:param name="id" value="id"/>
				</s:url>
					<input type="button" value="删除"
					onclick="window.location.href='<s:property value="%{#delURL}"/>'"/>
					
				</td>
			</tr>
		</s:iterator>
		<s:if test="#request.page.list.size()==0">
		<tr><td class="data_cell" colspan="20" style="text-align:center;height:40px;">没有记录</td></tr>
		</s:if>
		</tbody> 
		</table>
	</div>
  
		<table width="100%" style="border: 0px;font-size: 14px;"><tr style="border: 0px;"><td style="border: 0px;">
				<s:url id="back" action="listMms"/>
				<s:a id="backBtn" href="%{#back}">返回</s:a>
				</td><td style="border: 0px;">
					<jb:pager/>
					</td>
				</tr></table>	
		
		
	</s:form>	
</div>
 </body>
</html>
  	
