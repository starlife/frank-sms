<%@ page language="java" pageEncoding="UTF-8"%>
<%@include file="/css.jsp"%>
<%@include file="/js.jsp"%>
<%@include file="/taglib.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>查看收信箱</title>
	<script type="text/javascript">
	$(document).ready(function(){
		//这里写jquery
		$("#queryBtn").button();
		$("input[type='button']").button();
		
		$("tr td").each(function(i){   
         	//获取td当前对象的文本
         	var text=$.trim($(this).text());   
         	//如果长度大于25;
         	if(text.length>25){   
                //给td设置title属性,并且设置td的完整值.给title属性.   
    			$(this).attr("title",text);     
             	//重新为td赋值;   
             	$(this).text(text.substring(0,25)+"...");   
         	}   
      	});   
		
		
      	
      	setNavTitle("系统管理 >> 短信管理 >> 短信列表");	
		
	});
	
	
		
		
	</script>
  </head>
  
<body>
<div id="container">
	<s:form action="listSms" method="post" theme="simple">
	<div>
		<table class="ui-widget ui-widget ui-widget-header">
			<tr>
				<td >开始时间</td>
				<td >
					<s:textfield name="queryBean.beginTime" 
					onfocus="WdatePicker({el:this,skin:'default',dateFmt:'yyyyMMdd'})"/>
				</td>
				<td >结束时间</td>
				<td  >
					<s:textfield name="queryBean.endTime" 
					onfocus="WdatePicker({el:this,skin:'default',dateFmt:'yyyyMMdd'})"/>
		
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
			<td width="28%">接收手机号码</td>
			<td width="35%">短信内容</td>
			<td width="15%">发送时间</td>
			<td width="12%">发送状态</td>
			<td >操作</td>
		</tr>
		</thead>
		<tbody class="ui-widget-content">
		<s:iterator  value="#request.page.list"> 
			<tr>
				<td ><s:property value="recipient" default=" "/></td>
				<td ><s:property value="msgContent" default=" "/></td>
				<td >
					
					${my:getTimestampFull(sendtime)}
				</td>
				<td >
					<s:url id="reportURL" action="smsReport" >
											<s:param name="sessionid" value="id" />
										</s:url>
				<s:if test="status==0">未发送</s:if>
					<s:else>已发送(<a href="#"  onclick="openWindow('<s:property value="%{#reportURL}"/>')">发送报告</a>)</s:else></td>
				<td>
				<!-- 定义url -->
				<s:url id="delURL" action="delSms">
					<s:param name="id" value="id"/>
				</s:url>
					<input type="button"  value="删除"
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
  	
		<my:pager/>
					

		
	</s:form>	
</div>
 </body>
</html>
