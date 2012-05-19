<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/WEB-INF/jb-common.tld" prefix="jb"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>选择彩信文件</title>
		<%@include file="/css.jsp"%>
		<%@include file="/js.jsp"%>
		<script type="text/javascript">
		$(document).ready(function(){
			//这里写jquery
			$("input[name='id']").click(
				function()
				{
					var id=$("input[name='id']:checked");
					var first=id.closest("td");
					var mmsName=first.next().html();
					var frames=first.next().next().html();
					var mmsSize=first.next().next().next().html();
					//如果是顶层页面
					if(window==window.parent)
					{
						alert("彩信："+id.val()+",彩信名称："+mmsName+",帧数："+frames+",大小："+mmsSize);
						//$(vals).each(function(){
	    					//alert($(this).html())
	  					//});
					}
					else
					{
						parent.choiceMmsFile_callback(id.val(),mmsName,frames,mmsSize);
  					}
					
				}
			)
			
        		
        
    
			
	});
	
		
	</script>
	</head>
	<body>
		<div id="container">
			<s:form action="listMms" method="post" theme="simple">
				
				<hr />
				<!-- 列表数据 -->
				<div>
					<table class="ui-widget" id="ppp">
						<thead class="ui-widget-header">
							<tr>
								<td width="5%">
									&nbsp;
								</td>
								<td width="45%">
									彩信名称
								</td>
								<td width="10%">
									彩信帧数
								</td>
								<td width="15%">
									彩信大小(KB)
								</td>
								
								<td >
									创建时间
								</td>
								
							</tr>
						</thead>
						<tbody class="ui-widget-content">
							<s:iterator value="#request.page.list">
								<tr>
									<td >
										<input type = "radio"  name = "id" value = '<s:property value="id" default=" " />' />
									</td>
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
										<script>
											document.write(formatDateStr('<s:property value="createtime" default=" " />'));
										</script>
									</td>
									
									
								</tr>
							</s:iterator>
						</tbody>
					</table>
					<s:if test="#request.page.list.size()==0">
						<table>
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

