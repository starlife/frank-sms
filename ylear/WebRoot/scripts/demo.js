$(document).ready(function(){
	
	$("#browser").treeview();
	
	$("#browser a").mouseover(function(){
		window.status=this.title;
		return true;
	}).mouseout(function(){
		window.status="Done";
		return true;
	});
});
