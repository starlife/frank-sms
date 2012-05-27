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
		
		
	</script>
  </head>
  <body>
<div id="container">
	<div>
		<table class="ui-widget ui-widget-header" >
			<tr height="25px;">
				<td width="20%">总发送号码个数：</td>
				<td width="30%"><s:property value="reportBean.allCount+'（个）'" default=" "/><s:hidden name="reportBean.allCount"/>	
				</td>
				<td width="20%">发送成功数：</td>
				<td width="30%" >
					<s:property value="reportBean.successCount+'（个）'" default=" "/><s:hidden name="reportBean.successCount"/>		
				</td>					
			</tr>
			<tr height="25px;">
				<td >未获取发送结果数：</td>
				<td >
					<s:property value="reportBean.unknowCount+'（个）'" default=" "/><s:hidden name="reportBean.unknowCount"/>	
				</td>
				<td >发送失败数：</td>
				<td >
					<s:property value="reportBean.failCount+'（个）'" default=" "/>	<s:hidden name="reportBean.failCount"/>	
				</td>					
			</tr>
		</table>
		
	</div>
	<s:form action="mmsReport" method="post" theme="simple">
	<s:hidden name="queryBean.sessionid"/>
	<s:hidden name="reportBean.allCount"/>
	<s:hidden name="reportBean.successCount"/>	
	<s:hidden name="reportBean.unknowCount"/>	
	<s:hidden name="reportBean.failCount"/>	
	<div>
		<table class="ui-widget ui-widget-header" >
			<tr>
				<td >接收号码：</td>
				<td >
					<s:textfield name="queryBean.toAddress" />
				</td>
				<td >发送状态：<s:select list="#{-1:'全部',0:'发送成功',1:'发送失败',2:'未获取发送结果'}" 
				name="status" listKey="key" listValue="value"></s:select> 
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
			<s:if test="mmStatus==1">
				<s:set id="status" value="'发送成功'"/>
				<s:set id="statusText" value="'无'"/>
			</s:if>
			<!-- 如果没收到发送回应包或者收到了回应失败包（不等于1000） -->
			<s:elseif test="mmStatus==null">
				<s:set id="status" value="'未获取状态报告'"/>
				<s:set id="statusText" value="'无'"/>
			</s:elseif>
			<!-- 如果收到了回应成功包，且收到了提交状态包但提交失败，还是应该标记为发送失败 -->
			<s:elseif test="mmStatusText=='6640'">
				<s:set id="status" value="'发送失败'"/>
				<s:set id="statusText" value="'6640(用户未点播)'"/>
			</s:elseif>
			<s:else>
				<s:set id="status" value="'发送失败'"/>
				<s:set id="statusText" value="%{mmStatusText}"/>
			</s:else>
			<tr class="ui-widget-content" align="center">
				<td ><s:property value="toAddress"/></td>
				<td ><s:property value="#status"/></td>
				<td ><s:property value="#statusText"/></td>
				<td ><script>
						document.write(formatDateStr('<s:property value="sendtime"/>'));
					</script>
				</td>
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
		
		</tbody> 
		</table>
		<s:if test="#request.page.list.size()==0">
						<table class="ui-widget">
							<tr>
								<td style="text-align: center; height: 40px;">
									没有记录
								</td>
							</tr>
						</table>
					</s:if>
	</div>
  
		<table width="100%" style="border: 0px;font-size: 14px;"><tr style="border: 0px;"><td style="border: 0px;">
				</td><td style="border: 0px;">
					<jb:pager/>
					</td>
				</tr></table>	
		
		
	</s:form>	
</div>
 </body>
</html>
  	
