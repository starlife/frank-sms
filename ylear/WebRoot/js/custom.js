/*$(document).ready(function(){

	$("#ding").datepicker();
	$("#tabs").tabs();

	$("#login").click(function(){
		$("#yao").dialog({modal:true,buttons:{确定:function(){$(this).dialog("close");}}  }).dialog("open");	
	}).hover(function(){
		$(this).addClass("ui-state-hover"); 
	},function(){
		$(this).removeClass("ui-state-hover"); 
	}).mousedown(function(){
			$(this).addClass("ui-state-active"); 
		})
		.mouseup(function(){
				$(this).removeClass("ui-state-active");
		});


	$("#themeSwitch").change(function(){
		
		var value = $(this).val();
		var href = $("link:first").attr("href","css/themes/"+ value+"/jquery-ui.css");		
	});
	
	$("#tabs").draggable();
	
	$("#accordion").accordion({autoheight:true});
	
	
	

});

//扩展JQuery，加入自己的函数
$.extend({
	
	图片幻灯
	有两个参数: arg1,arg2
	arg1:#a代表容器
	arg2:3代表从第四张图片开始
	picSwitch:function(div,i){
		if(i!=0)  $(div+">img").eq(i-1).hide();
		else  $(div+">img:last").hide();
		$(div+">img").eq(i).fadeIn("slow",function(){
			i=i==$(div+">img").size()-1?0:i-0+1;
			setTimeout("$.picSwitch('"+div+"','"+i+"')", "1000");
		});		
	}
	
});   
*/

/*打开一个新的窗口*/
function  openWindow(url)
{
	window.open(url,"_blank","toolbar=yes, location=yes, directories=no, status=no, menubar=yes, scrollbars=yes, resizable=no, copyhistory=yes");
}

/*重定向到一个新的地址*/
function redirect(url)
{
	window.location.href=url;
}

function  doSubmit(url)
{
	document.forms[0].action=url;
	document.forms[0].submit();
}


function setNavTitle(title)
{
	try
	{
		window.top.frames["topFrame"].document.getElementById("navtitle").innerHTML=title;
	}
	catch(err)
	{
		//alert("error");
	}
} 

function formatDateStr(str)
{
	var year=str.substring(0,4);
	var month=str.substring(4,6);
	var day=str.substring(6,8);
	var hour=str.substring(8,10);
	var minute=str.substring(10,12);
	var second=str.substring(12,14);
	return year+"-"+month+"-"+day+" "+hour+":"+minute+":"+second;
}

Array.prototype.unique = function() { 
	var a = {}, c = [], l = this.length; 
	for (var i = 0; i < l; i++) { 
		var b = this[i]; 
		var d = (typeof b) + b; 
		if (a[d] === undefined) { 
			c.push(b); 
			a[d] = 1; 
		} 
	} 
	return c; 
} 
