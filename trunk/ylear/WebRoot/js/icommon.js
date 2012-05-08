function doSubmit(op){
	var oForm = document.forms[0];
	if ( Validator.Validate(oForm,3) ){
		oForm.elements["method"].value = op;
		oForm.submit();
	}
}
function doDel(url,op){
	if (!op){
		op = "doDel";
	}
	if (confirm("确认删除？")){
		window.location.href = "?method="+op+"&"+url;
	}
}

function CloseDiv(id){
	var oDiv = document.getElementById(id);
	if (oDiv){
		oDiv.style.display = "none";
	}
}
function ShowDiv(id){
	var oDiv = document.getElementById(id);
	if (oDiv){
		oDiv.style.display = "block";
	}
}

function  ForDight(Dight,How)  
{  
	Dight  =  Math.round  (Dight*Math.pow(10,How))/Math.pow(10,How);  
	return  Dight;  
} 

document.onkeydown=function(e){   
	e   =   window.event   ||   e;    
	//if (e.keyCode=65){e.keyCode=9;}
 }  


// page 
		var pageTotal = 1;
		var recTotal = 0;
		function page_go()
		{
			page_validate();		
			document.forms[0].submit();
		}
		function page_first()
		{
			document.forms[0].elements["pageResult.pageNo"].value = 1;
			document.forms[0].submit();
		}
		function page_pre()
		{
			var pageNo = document.forms[0].elements["pageResult.pageNo"].value;
			document.forms[0].elements["pageResult.pageNo"].value = parseInt(pageNo) - 1;
			page_validate();
			document.forms[0].submit();
		}
		function page_next()
		{
			var pageNo = document.forms[0].elements["pageResult.pageNo"].value;
			document.forms[0].elements["pageResult.pageNo"].value = parseInt(pageNo) + 1;
			page_validate();
			document.forms[0].submit();
		}
		function page_last()
		{
			document.forms[0].elements["pageResult.pageNo"].value = pageTotal;
			document.forms[0].submit();
		}
		function page_validate()
		{
			var pageNo = document.forms[0].elements["pageResult.pageNo"].value;
			if (pageNo<1)pageNo=1;
			if (pageNo>pageTotal)pageNo=pageTotal;
			document.forms[0].elements["pageResult.pageNo"].value = pageNo;
			
			var pageSize = document.forms[0].elements["pageResult.pageSize"].value;
			if (pageSize<1)pageSize=1;
			document.forms[0].elements["pageResult.pageSize"].value = pageSize;
		}
		function order_by(field){
			document.forms[0].elements["pageResult.orderBy"].value = field;
			page_first();
		}