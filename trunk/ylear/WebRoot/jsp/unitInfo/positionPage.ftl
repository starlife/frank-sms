<#include "/template/common/public.ftl" />


<@html>
	<@head title="单位维护">
	
		<link rel="stylesheet" href=${ctx}/css/demo.css" type="text/css">
		<link rel="stylesheet" href="${ctx}/css/zTreeStyle/zTreeStyle.css" type="text/css">
		
		<script type="text/javascript" src="${ctx}/js/jquery-1.7.1.min.js"></script>
		<script type="text/javascript" src="${ctx}/js/global.js"></script>
		
		<script type="text/javascript" src="${ctx}/js/jquery.ztree.core-3.4.js"></script>
		<script type="text/javascript" src="${ctx}/js/jquery.ztree.excheck-3.4.js"></script>
		
		<script type="text/javascript">
			$(document).ready(function(){
				setNavTitle("系统管理 >> 单位维护 >> 单位列表");
			});
		
			function setNavTitle(title){
				try{
					window.top.frames["topFrame"].document.getElementById("navtitle").innerHTML=title;
				}
				catch(err){
				//alert("error");
				}
			} 
		</script>
	</@head>
	
	<#assign positions = ["通讯录","单位维护"] >
	
	<@body posArr = positions >
		<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" >
		  <tr>
		    <td width="2"></td>
		    <td >
			    <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
			      <tr>
			        <td>
			        	<a href="javascript:openOrCloseTree(true)">全部展开</a> | <a href="javascript:openOrCloseTree(false)">全部关闭</a>
			        </td>
			        <td align="right">
			        	<input class="btn_bar" onclick="addPosition()" type="button" value="新增"/>
			            <input class="btn_bar" onclick="updatePosition()" type="button" value="修改"/>
			            <input class="btn_bar" onclick="delPosition()" type="button" value="删除"/>
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
					onCheck : radioTreeOnCheck //得到选中树节点ID
				}
			};
	
			var zNodes =[
				{ id:0, pId:-1, name:"单位目录", open:true, nocheck:true, icon:"${ctx}/css/zTreeStyle/img/diy/address.png"}
				<#if positionList?? && (positionList.size() > 0) >,
					<#list positionList as position>
						{ id:${position.unitid}, 
							pId:<#if (position.TPositionParent)?? >${position.TPositionParent.unitid}<#else>0</#if>, 
							open:false, icon:"${ctx}/css/zTreeStyle/img/diy/folder_user.png", name:"${position.unitname}[${position.unitcode}]" }
						<#-- 如果是最后一个不加, -->
						<#if position_has_next>,</#if>
					</#list>
				</#if>
			];
			
			var positionId;
			/**
			*	得到单选树形对应的ID
			**/
			function radioTreeOnCheck(event, treeId, treeNode){
				//把选中的单选框对应的角色ID赋给变量
				positionId = treeNode.id;
			}
			
			$(document).ready(function(){
				$.fn.zTree.init($("#treePosition"), setting, zNodes);
				var zTree = $.fn.zTree.getZTreeObj("treePosition");
				zTree.reAsyncChildNodes(null, "refresh");
			});
			
			/**
			*	展开/关闭树形结构
			**/
			function openOrCloseTree(bolen){
				var zTree = $.fn.zTree.getZTreeObj("treePosition");
				zTree.expandAll(bolen);
			}
			
			/**************************相关业务js function*******************************/
			
			/**
			*添加单位
			**/
			function addPosition(){
				if(positionId != undefined){
					
					location.href="${ctx}/unitInfo/toAddPosition.action?position.unitid="+positionId;
					
					return ;
				}
			
				location.href="${ctx}/unitInfo/toAddPosition.action";
			}
			
			/**
			*修改单位
			**/
			function updatePosition(){
				if(positionId == undefined){
					alert("请选择操作数据！");
					return;
				}
				location.href="${ctx}/unitInfo/toUpdatePosition.action?position.unitid="+positionId;
			}
			
			/**
			*删除单位
			**/
			function delPosition(){
				if(positionId == undefined){
					alert("请选择操作数据！");
					return;
				}
				if(!confirm("您确定删除该选中单位吗？")){
					return ;
				}
				var url = '${ctx}/unitInfo/delPosition.action';
				$.post(url,{'position.unitid':positionId},function(data){
					var intData = parseInt(data,10);
					if( intData == SYS.RESULT_SUCC){
						alert("删除成功！");
						location.href='${ctx}/unitInfo/position.action';
						return ;
					}else if( intData == SYS.RESULT_WARN){
						alert("该单位下还有子单位或联系人，不能删除！！");
					}else if( intData == SYS.RESULT_FAIL){
						alert("删除操作异常！");
						return ;
					}
					
				});
			}
			
		</script>
	
	</@body>
</@html>	
