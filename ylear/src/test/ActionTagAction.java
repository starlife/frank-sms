package test;

import com.opensymphony.xwork2.ActionSupport;

public class ActionTagAction extends ActionSupport{
	 
	public String execute() {
		return SUCCESS;
	}
 
	public String sayHello(){
		return "sayHello";
	}
 
	public String sayStruts2(){
		return "sayStruts2";
	}
 
	public String saySysOut(){
		System.out.println("SysOut SysOut SysOut");
		return "saySysOut";
	}
 
}
