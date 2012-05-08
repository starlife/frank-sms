package com.frank.ylear.modules.home.action;

import com.opensymphony.xwork2.ActionSupport;

public class HomeAction extends ActionSupport
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String main()
	{
		return "main";
	}
	
	public String header()
	{
		return "header";
	}
	
	public String menu()
	{
		return "menu";
	}
	public String footer()
	{
		return "footer";
	}
	public String welcome()
	{
		return "welcome";
	}
}
