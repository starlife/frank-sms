/*$(document).ready(function(){

	$("#ding").datepicker();
	$("#tabs").tabs();

	$("#login").click(function(){
		$("#yao").dialog({modal:true,buttons:{ȷ��:function(){$(this).dialog("close");}}  }).dialog("open");	
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

//��չJQuery�������Լ��ĺ���
$.extend({
	
	ͼƬ�õ�
	����������: arg1,arg2
	arg1:#a��������
	arg2:3����ӵ�����ͼƬ��ʼ
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
