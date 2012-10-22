/**全局js**/
function SYS() {
	/*全局路径*/
	this.WEBAPP_NAME = '/SYS';
	this.WEBAPP_HOST = "http://localhost:8081";
 	
	/*信息等级提示*/
	this.MESSAGE_LEVEL_FIRST = '信息';
	this.MESSAGE_LEVEL_SECOND = '提示';
	this.MESSAGE_LEVEL_THIRD = '警告';
	
	/*信息反馈码*/
	this.RESULT_FAIL = 0;//失败码
	this.RESULT_SUCC = 1;//成功码
	this.RESULT_WARN = -1;//异常码
	
}
var SYS = new SYS();

/**
 * 检查多选框或多选框是否选中（如果type=edit那么只能选一个）
 * @param curObj	选框对象
 * @param checkName 选框的name
 * @param type		执行动作类型
 * @param alertNone 当没有选择数据时是否弹出信息提示框
 * @return
 */
function checkTableForm(curObj,checkName,type, alertNone) {
	var flag = false;
	var alertBool = (alertNone != undefined ) ? alertNone : true;
	for ( var i = 0; i < curObj.length; i++ ) {
		if (curObj[i].name == checkName
				&& (curObj[i].type == 'checkbox' || curObj[i].type == 'radio')) {
			flag += curObj[i].checked;
		}
	}
	if (flag == 0) {
		if(alertBool){
			alert("请选择操作数据！");
		}
		return false;
	}
	if (type == "edit") {
		if (flag > 1) {
			alert("只能选择一条数据！");
			return false;
		}
	}
	return true;
}

/**
 * 得到多选框或者单选框的选中值
 * @param curObj		选框对象
 * @param checkName 选框名称
 * @return
 */
function getSelectedDataId(curObj,checkName) {
	var resultid = "";
	for (i = 0; i < curObj.length; i++ ) {
		if (curObj[i].name == checkName
				&& (curObj[i].type == 'checkbox' || curObj[i].type == 'radio')
				&& curObj[i].checked == true) {
			resultid+= curObj[i].value ;
		}
	}
	return resultid;
}

/**
 * 重置按钮
 * @return
 */
function reSet(){
	//清空input text标签的值
	 var objInput = document.getElementsByTagName("input");
	 for(var i =0;i<objInput.length;i++){
	 if(objInput[i].type=="text"){
	   objInput[i].value ="";
	 }
	   
	 } 
	 //清空select标签的值
	 var objInput = document.getElementsByTagName("select");
	 for(var i =0;i<objInput.length;i++){
	   document.getElementById(objInput[i].id).options[0].selected = true;
	   
	 } 
	 
	 var objInput = document.getElementsByTagName("textarea");
	 for(var i =0;i<objInput.length;i++){ 
		   objInput[i].value ="";
	 } 

}