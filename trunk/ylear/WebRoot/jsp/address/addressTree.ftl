<#include "/template/common/public.ftl" />
<@html>
	<@head title="树状通讯录">
	
		<link rel="stylesheet" href="${ctx}/css/demo.css" type="text/css">
	<link rel="stylesheet" href="${ctx}css/zTreeStyle/zTreeStyle.css" type="text/css">
	
	<script type="text/javascript" src="${ctx}js/jquery-1.4.4.min.js"></script>
	<script type="text/javascript" src="${ctx}js/jquery.ztree.core-3.4.js"></script>
	<script type="text/javascript" src="${ctx}js/jquery.ztree.excheck-3.4.js"></script>
		
	</@head>
	
	<#assign positions = ["通讯录","树状通讯录"] >
	
	<@body posArr = positions >
		<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="tab_cel" >
			<tr>
				<td>
				[ <a id="checkAllTrue" href="#"  onclick="return false;">全部勾选</a> ]
					&nbsp;&nbsp;&nbsp;&nbsp;[ <a id="checkAllFalse" href="#" onclick="return false;">全部取消</a> ]
					<ul id="treePosition" class="ztree"></ul>
					
					<div style="text-align:right;padding-top: 5px;font-size: 14px; ">
						<input type="button" class="button" value="确定号码" onclick="addSelectedPhone();"/>&nbsp;
						
					</div>
	
				</td>
			</tr>
		</table>
		<script type="text/javascript">
			$(document).ready(function(){
				//添加全部勾选和取消的点击事件
				$("#checkAllTrue").bind("click", {type:"checkAllTrue"}, checkNode);
				$("#checkAllFalse").bind("click", {type:"checkAllFalse"}, checkNode);
				
				$.fn.zTree.init($("#treePosition"), setting, zNodes);
				var zTree = $.fn.zTree.getZTreeObj("treePosition");
				zTree.reAsyncChildNodes(null, "refresh");
			});
			
			var setting = {
				view: {
					dblClickExpand: false,
					nameIsHTML: true//支持html
				},
				check: {
					enable: true
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
				{ id:0, pId:-1, name:"通讯录", open:true, nocheck:true, icon:"${ctx}/css/zTreeStyle/img/diy/address.png"}
				<#if positionList?? && (positionList.size() > 0) >,
					<#list positionList as position>
						{ id:${position.unitid}, 
							pId:<#if (position.TPositionParent)?? >${position.TPositionParent.unitid}<#else>0</#if>,
							open:false, icon:"${ctx}/css/zTreeStyle/img/diy/folder_user.png", name:"${position.unitname}" }
						<#-- 如果是最后一个不加, -->
						<#if position_has_next>,</#if>
					</#list>
					<#if linkManList?? && (linkManList.size() > 0) >,
						<#list linkManList as linkMan>
						{ id:${linkMan.id}, pId:${linkMan.TPosition.unitid}, 
										open:true,
										target:${linkMan.phonenumber},//存取号码
										icon:"${ctx}/images/men.png",
										name:"${linkMan.name}" }
										
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
			
			
			
			/**
			 *展开/关闭树形结构
			**/
			function openOrCloseTree(bolen){
				var zTree = $.fn.zTree.getZTreeObj("treePosition");
				zTree.expandAll(bolen);
			}
			
			
			function addSelectedPhone(){
			
				var zTree = $.fn.zTree.getZTreeObj("treePosition");
				var phones="";
				//得到选中的树节点
				var checkedNodes = zTree.getCheckedNodes(true);
				var nodeLen = checkedNodes.length;
				
				if( nodeLen == 0){
					alert("请选择操作数据！");
					return ;
				}else{
					//拼接树节点ID
					for(var i=0; i < nodeLen; i++ ){
						var phoneNo = checkedNodes[i].target;
						if(phoneNo!=undefined){
							phones += phoneNo;
							if(i < (nodeLen-1)){
								phones += ",";
							}
						}
					}
				}
				//判断是否选中号码  过滤最后一个是逗号的情况
				if(phones.length==0){
					alert("请选择发送联系人！");
					return;
				}else{
					var dou = phones.substring(phones.length-1);
					if(dou == ','){
						phones =  phones.substring(0,phones.length-1);
					}
				}
				
				
				//回调函数
				parent.addSelectedPhone_callback(phones);
			}
		
			//节点选取 触发事件
			function checkNode(e) {
				var zTree = $.fn.zTree.getZTreeObj("treePosition"),
				type = e.data.type,
				nodes = zTree.getSelectedNodes();
				if (type.indexOf("All")<0 && nodes.length == 0) {
					alert("请先选择一个节点");
				}
	
				if (type == "checkAllTrue") {
					zTree.checkAllNodes(true);
				} else if (type == "checkAllFalse") {
					zTree.checkAllNodes(false);
				}
			}
		</script>
	</@body>
</@html>	
