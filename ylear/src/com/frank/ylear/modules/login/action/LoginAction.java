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
	
	/*ȡ�����н�ɫ���ظ�ui����*/
	private List<SysRole> roleList;
	//��½��֤��
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
		//��½�ɹ���ȡ���û���bean�Ѿ�Ȩ�޲˵�
		List<SysRight> rights=userService.getSysRightByRole(sysuser.getSysRole().getId());
		sysuser.setRights(rights);
		this.getSession().put(sessionUserKey, sysuser);
	}
	/**
	 * ��¼
	 */
	public String login() throws Exception
	{
		return SUCCESS;
	}

	/**
	 * ע��
	 */
	public String logout() throws Exception
	{
		this.getSession().remove(sessionUserKey);
		return "index";
	}
}
