<%@ page language="java" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>查询号码</title>
   <meta http-equiv="content-type" content="text/html; charset=UTF-8">
	<link rel="stylesheet" href=/ylear2/css/demo.css" type="text/css">
	<link rel="stylesheet" href="/ylear2/css/zTreeStyle/zTreeStyle.css" type="text/css">
	
	<script type="text/javascript" src="/ylear2/js/jquery-1.4.4.min.js"></script>
	<script type="text/javascript" src="/ylear2/js/jquery.ztree.core-3.4.js"></script>
	<script type="text/javascript" src="/ylear2/js/jquery.ztree.excheck-3.4.js"></script>
    <!--  
	<script type="text/javascript">

	$(document).ready(
		function(){
			//这里写jquery
			$("#queryBtn").button();
			$(".button").button();
			$("input[type='button']").button();
			$("#checkall").click(
				function()
				{
					if ($(this).attr("checked")=="checked")
					{
						$("input[type='checkbox']").slice(1).attr("checked",true);
					}else
					{
						$("input[type='checkbox']").slice(1).attr("checked",false);
					}
				}
			);
		}
		
	);
	function addSelectedPhone()
	{
		var phones="";
		$("input[type='checkbox']:checked").not("#checkall").each(
			function()
			{
				phones= phones+$(this).closest("td").next().text()+",";
			}
		)
		//回调函数
		parent.addSelectedPhone_callback(phones);
	}
	function addQueryPhone()
	{
		//回调函数
		parent.addQueryPhone_callback(phones);
	}
	</script>
	-->
	
	<script type="text/javascript">
			var setting = {
				view: {
					dblClickExpand: false,
					nameIsHTML: true//支持html
				},
				data: {
					simpleData: {
						enable: true
					}
				},
				callback: {
					onClick: onClick //实现单击树节点展开子节点
				}
			};
	
			var zNodes =[
				{ id:0, pId:-1, name:"通讯录", open:true, nocheck:true, icon:"${ctx}/js/JQueryzTreev3.2/css/zTreeStyle/img/diy/address.png"}
				<#if positionList?? && (positionList.size() > 0) >,
					<#list positionList as position>
						{ id:${position.id}, pId:${position.TPositionParent.id}, open:false, icon:"${ctx}/js/JQueryzTreev3.2/css/zTreeStyle/img/diy/folder_user.png", name:"${position.posName}<#if position.memo?? && position.memo!= "" >[${position.memo}]</#if>" }
						<#-- 如果是最后一个不加, -->
						<#if position_has_next>,</#if>
					</#list>
					<#if linkManList?? && (linkManList.size() > 0) >,
						<#list linkManList as linkMan>
						
						{ id:${linkMan.id}, pId:${linkMan.TPosition.id}, 
										open:true, 
										click:"toLinkManDetail(${linkMan.id})",
										icon:"${ctx}/images/<#if (linkMan.gender == "1")>male.png<#else>female.png</#if>", 
										name:"${linkMan.fullName}" }
										
						<#-- 如果是最后一个不加, -->
						<#if linkMan_has_next>,</#if>
					</#list>
					</#if>
				</#if>
			];
			
			/**
			*	实现单击树节点，展开子节点
			**/
			function onClick(e,treeId, treeNode) {
				var zTree = $.fn.zTree.getZTreeObj("treePosition");
				zTree.expandNode(treeNode);
			}
			
			$(document).ready(function(){
				$.fn.zTree.init($("#treePosition"), setting, zNodes);
				var zTree = $.fn.zTree.getZTreeObj("treePosition");
				zTree.reAsyncChildNodes(null, "refresh");
			});
			
			/**
			 *展开/关闭树形结构
			**/
			function openOrCloseTree(bolen){
				var zTree = $.fn.zTree.getZTreeObj("treePosition");
				zTree.expandAll(bolen);
			}
			
			/**
			 *查看联系人详情
			**/
			function toLinkManDetail(aid){
				showModalDialog("${ctx}/address/toLinkManDetail.action?addRessEntity.id="+aid,'联系人详情','dialogWidth:800px;dialogHeight:250px;dialogLeft:350px;dialogTop:250px;center:yes;help:yes;scroll:no;resizable:yes;status:yes');
			}
			
		</script>
  </head>
  
  <body>
		<ul id="treeDemo" class="ztree"></ul>
		<input type="checkbox" id="py" class="checkbox first" checked style="visibility:hidden"/>
		<input type="checkbox" id="sy" class="checkbox first" checked style="visibility:hidden"/>
		<input type="checkbox" id="pn" class="checkbox first" checked style="visibility:hidden"/>
		<input type="checkbox" id="sn" class="checkbox first" checked style="visibility:hidden"/>
  					
					
				
  
  
  
<!-- 
  <div id="container">
  <s:form action="listCustomAddress" method="post" theme="simple">
  	<div>
		<table class="ui-widget" >
			<tr>
				<td >手机号码</td>
				<td >
					<s:textfield  id="queryBean.phonenumber" name="queryBean.phonenumber"/>
				</td>
				<td >用户姓名</td>
				<td  >
					<s:textfield id="queryBean.name" name="queryBean.name"/>
					
				</td>
			</tr>
			<tr>		
				<td >所属部门</td>
				<td >
					<s:textfield id="queryBean.department" name="queryBean.department"/>
				</td>
				<td >地区</td>
				<td >
				<s:textfield id="queryBean.area" name="queryBean.area"/>
				</td>
			</tr>
			<tr>
				<td colspan="4" ><s:submit id="queryBtn" value="查询"/></td>		
				
			</tr>
		</table>
		
	</div>
	<hr/>
	<div>
		<table class="ui-widget">
		<thead class="ui-widget-header">
		<tr>
			<td><s:checkbox id="checkall" name="checkall"/></td>
			<td >手机号码</td>
			<td >姓名</td>
			<td >所属部门</td>
			<td >地区</td>
		</tr>
		</thead>
		<tbody class="ui-widget-content">
		<s:iterator  value="#request.page.list"> 
			<tr class="ui-widget-content" align="center">
				<td><s:checkbox name="check"/></td>
				<td ><s:property value="phonenumber" default=" "/></td>
				<td ><s:property value="name" default=" "/></td>
				<td ><s:property value="department" default=" "/></td>
				<td ><s:property value="area" default=" "/></td>
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
	<div style="text-align:right;padding-top: 5px;font-size: 14px; ">
		<input type="button" class="button" value="添加号码" onclick="addSelectedPhone();"/>&nbsp;
		<s:url id="queryPhoneURL" action="listCustomAddress" method="queryPhone"/>
		<input type="button" class="button" value="添加所有" onclick="doSubmit('<s:property value="%{#queryPhoneURL}"/>')"/>
	</div>			
	</s:form>
	</div>		
	
	  -->
	  
	  <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="tab_cel" >
			<tr>
				<td>
					<ul id="treePosition" class="ztree"></ul>
				</td>
			</tr>
		</table>
  </body>
  
</html>

