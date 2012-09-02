<%@ page language="java" pageEncoding="UTF-8"%>
<%@include file="/css.jsp"%>
<%@include file="/js.jsp"%>
<%@include file="/taglib.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>选择彩信文件</title>
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
			title:'取彩信对话框',
			closeOnEscape:false,//关闭按 esc 退出
			overlay: {
				backgroundColor: '#000',
				opacity: 0.5
			}
		});
		
		
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
		
		
		setNavTitle("系统管理 >> 彩信管理 >> 彩信列表");
		
	
        		
        		
	});
	
	function doConfirm(url)
	{
		if(confirm("删除该彩信的同时，已发送彩信中的相关记录也会被删除，确认删除吗?"))
		{
			//alert(url);
			redirect(url);
		}
	}	
	
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
			<s:form action="listMmsFile" method="post" theme="simple">
				
				<div>
					<table class="ui-widget ui-widget-header">
						<tr>
							<td>
								开始时间
							</td>
							<td><s:textfield name="queryBean.beginTime"
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
								<td width="45%">
									彩信名称
								</td>
								<td width="10%">
									彩信帧数
								</td>
								<td width="15%">
									彩信大小(KB)
								</td>
								
								<td width="15%">
									创建时间
								</td>
								<td >
									操作
								</td>
								
							</tr>
						</thead>
						<tbody class="ui-widget-content">
							<s:iterator value="#request.page.list">
								<tr>
									<td>
										<s:property value="mmsName" default=" " />
									</td>
									<td>
										<s:property value="frames" default=" " />
									</td>
									<td>
									 	<s:text  name="global.format.size.k">
											<s:param value="mmsSize/1024.0" />
										</s:text> 
									
									</td>
									
									<td>
										${my:getTimestampFull(createtime)}
										
									</td>
									<td>
									<!-- 定义url -->
					<s:url id="editURL" action="mmsEditor">
						<s:param name="id" value="id"/>
					</s:url>
					<s:url id="delURL" action="delMmsFile">
						<s:param name="id" value="id"/>
					</s:url>
					<input type="button"  value="编辑"
					onclick="redirect('<s:property value="%{#editURL}"/>');"/>
					<input type="button"  value="删除"
					onclick="doConfirm('<s:property value="%{#delURL}"/>');"/>
									
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
				<input class="addBtn" type="button" value="增加" onclick="redirect('<s:url  action="mmsEditor"/>');"/>
				<input class="addBtn" type="button" value="从手机报新增" onclick="redirect('<s:url  action="3gMms"/>');"/>
				
				<my:pager/>
	
			</s:form>
		</div>
	</body>
</html>

