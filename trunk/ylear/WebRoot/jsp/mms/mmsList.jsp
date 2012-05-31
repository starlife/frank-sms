<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/WEB-INF/jb-common.tld" prefix="jb"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>查看发信箱</title>
		<%@include file="/css.jsp"%>
		<%@include file="/js.jsp"%>
		<script type="text/javascript">
	$(document).ready(function(){
		//这里写jquery
		$("#queryBtn").button();
		$("input[type='button']").button();
		
		$("#dialog:ui-dialog" ).dialog( "destroy" );
		$("#dialog").dialog({
			autoOpen:false,
			resizable: false,
			height:600,
			width:"60%",
			modal: true,
			title:'查看彩信内容',
			closeOnEscape:false,//关闭按 esc 退出
			overlay: {
				backgroundColor: '#000',
				opacity: 0.5
			}
		});
		
		
		$("tr td").each(function(i){   
         	//获取td当前对象的文本,如果长度大于25;   
         	if($(this).text().length>25){   
                //给td设置title属性,并且设置td的完整值.给title属性.   
    			$(this).attr("title",$(this).text());   
                //获取td的值,进行截取。赋值给text变量保存.   
    			var text=$(this).text().substring(0,25)+"...";   
             	//重新为td赋值;   
             	$(this).text(text);   
         	}   
      	});   
		
		setNavTitle("系统管理 >> 彩信管理 >> 彩信列表");	
		
	});
		
		function showMmsMethod(url)
		{
			//alert(url);
			$("#showMms").attr("src",url);
			$("#dialog").dialog("open");
		}
	</script>
	</head>
	<body>
		<div id="container">
			<div id="dialog" style="display: none">
				<iframe id="showMms" name="showMms" src='' width="100%"
					height="100%" frameborder="0" scrolling="auto"></iframe>
			</div>
			<s:form action="listMms" method="post" theme="simple">
				<div>
					<table class="ui-widget ui-widget-header">
						<tr>
							<td>
								开始时间
							</td>
							<td>
								<s:textfield name="queryBean.beginTime"
									onfocus="WdatePicker({el:this,skin:'default',dateFmt:'yyyyMMdd'})" />
							</td>
							<td>
								结束时间
							</td>
							<td>
								<s:textfield name="queryBean.endTime"
									onfocus="WdatePicker({el:this,skin:'default',dateFmt:'yyyyMMdd'})" />
							<td>
								<s:submit id="queryBtn" value="查询" />
							</td>

						</tr>
					</table>

				</div>
				<hr />
				<!-- 列表数据 -->
				<div>
					<table class="ui-widget">
						<thead class="ui-widget-header">
							<tr>
								<td width="20%">
									彩信标题
								</td>
								<td width="25%">
									彩信收件人
								</td>
								<td width="10%">
									彩信内容
								</td>
								
								<td width="15%">
									发送时间
								</td>
								<td width="15%">
									发送状态
								</td>
								<td>
									操作
								</td>
							</tr>
						</thead>
						<tbody class="ui-widget-content">
							<s:iterator value="#request.page.list">
								<tr>
									<td>
										<s:property value="subject" default=" " />
									</td>
									<td>
										<s:property value="recipient" default=" " />
									</td>
									<td>
									<s:url id="showURL" action="mmsViewer" >
											<s:param name="id" value="mmsid" />
									</s:url>
									<a href="#"  onclick="showMmsMethod('<s:property value="%{#showURL}"/>')">查看彩信</a>
										
									</td>
									
									<td>
										<script>
											document.write(formatDateStr('<s:property value="sendtime" default=" " />'));
										</script>
										
									</td>
									<td>
										<s:url id="reportURL" action="mmsReport" >
											<s:param name="sessionid" value="id" />
										</s:url>
										<s:if test="status==0">未发送</s:if>
										<s:else>已发送(<a href="#"  onclick="openWindow('<s:property value="%{#reportURL}"/>')">发送报告</a>)</s:else>

									</td>
									<td>
										<!-- 定义url -->
										<s:url id="delURL" action="delMms">
											<s:param name="id" value="id" />
										</s:url>
										
										<input type="button" value="删除"
											onclick="redirect('<s:property value="%{#delURL}"/>');" />

										
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

				<jb:pager />



			</s:form>
		</div>
	</body>
</html>

