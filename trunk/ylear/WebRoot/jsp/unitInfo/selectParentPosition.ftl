<#include "/template/common/public.ftl" />
<@html>
	<@head title="单位维护">
	
		
		
		<link rel="stylesheet" href=${ctx}/css/demo.css" type="text/css">
		<link rel="stylesheet" href="${ctx}/css/zTreeStyle/zTreeStyle.css" type="text/css">
		<script type="text/javascript" src="${ctx}/js/jquery-1.7.1.min.js"></script>
		<script type="text/javascript" src="${ctx}/js/jquery.ztree.core-3.4.js"></script>
		<script type="text/javascript" src="${ctx}/js/jquery.ztree.excheck-3.4.js"></script>
		
		<style>
			body{
				width:94%;
				margin:0px;
			}
		</style>
	</@head>
	
	<#assign positions = ["通讯录","通讯录单位选择"] >
	
	<@body posArr = positions >
		<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
		  <tr>
		    <td width="2"></td>
		    <td style="background:url(${ctx}/images/info_title_bg.gif) repeat-x;">
			    <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
			      <tr>
			        <td style="padding-top:4px; font-weight:bold; color:#333;">
			        	<a href="javascript:openOrCloseTree(true)">全部展开</a> | <a href="javascript:openOrCloseTree(false)">全部关闭</a>
			        </td>
		        	<td align="right">
			        	<input class="btn_bar" onclick="getPosition()" type="button" value="选 择"/>
			        </td>
			      </tr>
			    </table>
		    </td>
		    <td width="2">
		    	<img src="${ctx}/images/info_title_right.gif" width="2" height="28">
		    </td>
		</table>
		<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="tab_cel" >
			<tr>
				<td>
					<ul id="treePosition" class="ztree"></ul>
				</td>
			</tr>
		</table>
		<script type="text/javascript">
			var setting = {
				view: {
					dblClickExpand: false,
					selectedMulti: false
				},
				check: {
					enable: true,
					chkStyle: "radio",
					radioType: "all"
				},
				data: {
					simpleData: {
						enable: true
					}
				},
				callback: {
					onCheck : radioTreeOnCheck, //得到选中树节点ID
					onClick: onClick //实现单击树节点展开子节点
				}
			};
	
			var zNodes =[
				{ id:0, pId:-1, name:"所有单位", open:true, icon:"${ctx}/css/zTreeStyle/img/diy/address.png"}
				<#if positionList?? && (positionList.size() > 0) >,
					<#list positionList as position>
						{ id:${position.unitid}, 
								pId:<#if (position.TPositionParent)?? >${position.TPositionParent.unitid}<#else>0</#if>, 
								icon:"${ctx}/css/zTreeStyle/img/diy/folder_user.png", 
								name:"${position.unitname}" }
						<#-- 如果是最后一个不加, -->
						<#if position_has_next>,</#if>
					</#list>
				</#if>
			];
			
			/**
			*	实现单击树节点，展开子节点
			**/
			function onClick(e,treeId, treeNode) {
				var zTree = $.fn.zTree.getZTreeObj("treePosition");
				zTree.expandNode(treeNode);
			}
			
			var positionId;
			var positionName;
			/**
			*	得到单选树形对应的ID
			**/
			function radioTreeOnCheck(event, treeId, treeNode){
				//把选中的单选框对应的角色ID赋给变量
				positionId = treeNode.id;
				positionName = treeNode.name;
			}
			
			$(document).ready(function(){
				$.fn.zTree.init($("#treePosition"), setting, zNodes);
			});
			
			/**
			*	展开/关闭树形结构
			**/
			function openOrCloseTree(bolen){
				var zTree = $.fn.zTree.getZTreeObj("treePosition");
				zTree.expandAll(bolen);
			}
			
			//返回部门ＩＤ
			function getPosition(){
				if(positionId == undefined){
					alert("请选择单位！");
					return ;
				}
			  	window.returnValue = positionId+","+positionName;   //返回值
		    	window.close(); 
			}
		</script>
	</@body>
</@html>	
