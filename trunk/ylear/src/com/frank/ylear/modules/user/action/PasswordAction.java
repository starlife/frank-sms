package com.frank.ylear.modules.user.action;

import com.frank.ylear.common.constant.Constants;
import com.frank.ylear.modules.base.action.BaseAction;
import com.frank.ylear.modules.user.entity.SysUser;
import com.frank.ylear.modules.user.service.UserService;

public class PasswordAction extends BaseAction
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//public static final String sessionUserKey ="USER";
	
	private SysUser user = null;
	private UserService userService;
	
	
	/*修改密码重复密码验证*/
	private String reUsrPassword;
	/*取得所有角色返回给ui界面*/
	//private List<SysRole> roleList;
	//登陆验证码
	//private String validateCode;
	
	
	
	public void validateSavePassword()
	{
		if(this.hasErrors())
		{
			//取得所有role
			System.out.println("error");
			return;
		}
	}
	
	
	
	
	
	public String input()
	{
		return INPUT;
	}
	
	
	public String savePassword()
	{
		SysUser sessionUser=(SysUser) this.getSession().get(Constants.SESSION_USER);
		if(sessionUser==null)
		{
			return ERROR;
		}
		sessionUser.setUsrPassword(this.getUser().getUsrPassword());
		userService.saveUser(sessionUser);
		return SUCCESS;
	}
	
	
	
	
	public UserService getUserService()
	{
		return userService;
	}

	public void setUserService(UserService userService)
	{
		this.userService = userService;
	}

	public SysUser getUser()
	{
		return user;
	}

	public void setUser(SysUser user)
	{
		this.user = user;
	}

	

	public String getReUsrPassword()
	{
		return reUsrPassword;
	}

	public void setReUsrPassword(String reUsrPassword)
	{
		this.reUsrPassword = reUsrPassword;
	}

	

}
