package com.frank.ylear.modules.login.action;

import java.util.List;

import com.frank.ylear.common.constant.Constants;
import com.frank.ylear.modules.base.action.BaseAction;
import com.frank.ylear.modules.user.entity.SysRight;
import com.frank.ylear.modules.user.entity.SysRole;
import com.frank.ylear.modules.user.entity.SysUser;
import com.frank.ylear.modules.user.service.UserService;

public class LoginAction extends BaseAction
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final String sessionUserKey ="USER";
	
	/*取得所有角色返回给ui界面*/
	private List<SysRole> roleList;
	//登陆验证码
	private String validateCode;
	
	private SysUser user = null;
	private UserService userService;
	
	public void validateLogin()
	{
		if(this.hasErrors())
		{
			return;
		}
		String validateText=(String) this.getSession().get(Constants.VALIDATE_CODE_KEY);
		if(validateText==null||validateCode==null||!validateText.equals(validateCode.trim()))
		{
			this.addActionError(this.getText("validateCode.error"));
			return;
		}
		SysUser sysuser = userService.checkSysUserExist(user);
		if(sysuser==null)
		{
			this.addActionError(this.getText("user.checkuser.error"));
			return;
		}
		//登陆成功，取得用户的bean已经权限菜单
		List<SysRight> rights=userService.getSysRightByRole(sysuser.getSysRole().getId());
		sysuser.setRights(rights);
		this.getSession().put(sessionUserKey, sysuser);
	}
	/**
	 * 登录
	 */
	public String login() throws Exception
	{
		return SUCCESS;
	}

	/**
	 * 注销
	 */
	public String logout() throws Exception
	{
		this.getSession().remove(sessionUserKey);
		return "index";
	}
}
