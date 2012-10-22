<#macro pageTag actUrl pageName pageObj cssClass="pageTable" cssStyle="">
<#--
---------------------------------------------------
-自定义分页标签
-@parma actUrl		必选参数 ，action请求地址
-@parma pageName	必选参数 ，分页bean对象属性名字
-@parma pageObj		必选参数 ，分页bean对象
-@parma cssClass	分页表格class样式
-@parma cssStyle	分页表格style样式
---------------------------------------------------
@author Memory @modified date 2012-04-12
-->
	<table class="${cssClass}" style="${cssStyle}" cellspacing="0" cellpadding="0" align="center" >
    	<tbody>
    		<tr>
    			<td>总计：<span class="red_color">${pageObj.totalCount}</span>条记录  &nbsp;页数：
    					<span  class="red_color"> ${pageObj.pageNo} </span>/ ${pageObj.totalPages} 页 &nbsp;
    					<a href="${actUrl}?${pageName}.pageNo=1">第一页 </a>
    					<#--判断是否有上一页-->
    					<#if pageObj.hasPre >
    						<a href="${actUrl}?${pageName}.pageNo=${pageObj.prePage}">上一页 </a>&nbsp;
    					<#else>
    						上一页&nbsp;
    					</#if>
    					<#--判断是否有下一页-->
    					<#if pageObj.hasNext >
    						<a href="${actUrl}?${pageName}.pageNo=${pageObj.nextPage}">下一页 </a>&nbsp;
    					<#else>
    						下一页&nbsp;
    					</#if>
    					
    					<a href="${actUrl}?${pageName}.pageNo=${pageObj.totalPages}">最后一页</a>
    			</td>
    		</tr>
    	</tbody>
   	</table>
</#macro>