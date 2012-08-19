<%@ page language="java" pageEncoding="UTF-8"%>
<%@include file="/css.jsp"%>
<%@include file="/js.jsp"%>
<%@include file="/taglib.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>发送结果分析</title>
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
			<!-- 这里定义几个参数 -->
			<s:set id="mmStatusText0" value="'(过期未提取)'"/>
			<s:set id="mmStatusText2" value="'(系统拒绝)'"/>
			<s:set id="mmStatusText3" value="'(用户拒绝)'"/>
			<s:set id="mmStatusText4" value="'(未知)'"/>
			<s:set id="mmStatusText5" value="'(转发)'"/>
			<!-- 如果不等于1000，那么都是发送失败 -->
			<s:if test="status!=1000">
				<s:set id="mystatus" value="'发送失败'"/>
				<s:set id="mystatusText" value="%{statusText}"/>
			</s:if>
			<!-- 下面是提交成功的 -->
			<s:elseif test="mmStatus==1">
				<s:set id="mystatus" value="'发送成功'"/>
				<s:set id="mystatusText" value="'--'"/>
			</s:elseif>
			<!-- 如果没收到发送回应包或者收到了回应失败包（不等于1000） -->
			<s:elseif test="mmStatus==null">
				<s:set id="mystatus" value="'未获取状态报告'"/>
				<s:set id="mystatusText" value="'无'"/>
			</s:elseif>
			<s:elseif test="mmStatus==0">
				<s:set id="mystatus" value="'发送失败'"/>
				<s:set id="mystatusText" value="%{mmStatusText+#mmStatusText0}"/>
			</s:elseif>
			<s:elseif test="mmStatus==2">
				<s:if test="mmStatusText=='6640'">
					<s:set id="mystatus" value="'发送失败'"/>
					<s:set id="mystatusText" value="'6640(用户未点播)'"/>
				</s:if>
				<s:elseif test="mmStatusText=='4414'">
					<s:set id="mystatus" value="'发送失败'"/>
					<s:set id="mystatusText" value="'4414(用户拒绝)'"/>
				</s:elseif>
				<s:else>
					<s:set id="mystatus" value="'发送失败'"/>
					<s:set id="mystatusText" value="%{mmStatusText+#mmStatusText2}"/>
				</s:else>
			</s:elseif>
			<s:elseif test="mmStatus==3">
				<s:set id="mystatus" value="'发送失败'"/>
				<s:set id="mystatusText" value="%{mmStatusText+#mmStatusText3}"/>
			</s:elseif>
			<s:elseif test="mmStatus==4">
				<s:set id="mystatus" value="'发送失败'"/>
				<s:set id="mystatusText" value="%{mmStatusText+#mmStatusText4}"/>
			</s:elseif>
			<s:elseif test="mmStatus==5">
				<s:set id="mystatus" value="'发送失败'"/>
				<s:set id="mystatusText" value="%{mmStatusText+#mmStatusText5}"/>
			</s:elseif>
			<s:else>
				<s:set id="mystatus" value="'发送失败'"/>
				<s:set id="mystatusText" value="%{mmStatusText}"/>
			</s:else>
			
			<tr class="ui-widget-content" align="center">
				<td ><s:property value="toAddress"/></td>
				<td ><s:property value="#mystatus"/></td>
				<td ><s:property value="#mystatusText"/></td>
				<td >
					${my:getTimestampFull(sendtime)}
				</td>
				<td>
				<!-- 定义url -->
				<s:url id="delURL" action="delMmsSubmit">
					<s:param name="id" value="id"/>
				</s:url>
					<input type="button" value="删除"
					onclick="redirect('<s:property value="%{#delURL}"/>');"/>
					
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
					<my:pager/>
					</td>
				</tr></table>	
		
		
	</s:form>	
</div>
 </body>
</html>
  	
