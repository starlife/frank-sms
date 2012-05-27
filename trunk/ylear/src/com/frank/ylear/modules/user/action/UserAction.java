package com.frank.ylear.modules.user.action;

import java.util.List;

import com.frank.ylear.common.constant.Constants;
import com.frank.ylear.modules.base.action.BaseAction;
import com.frank.ylear.modules.user.entity.SysRight;
import com.frank.ylear.modules.user.entity.SysRole;
import com.frank.ylear.modules.user.entity.SysUser;
import com.frank.ylear.modules.user.service.UserService;

public class UserAction extends BaseAction
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//public static final String sessionUserKey ="USER";
	
	private SysUser user = null;
	private SysUser queryBean=null;
	private UserService userService;
	
	private String id=null;
	
	/*修改密码重复密码验证*/
	private String reUsrPassword;
	/*取得所有角色返回给ui界面*/
	private List<SysRole> roleList;
	//登陆验证码
	private String validateCode;
	
	
	
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
		this.getSession().put(Constants.SESSION_USER, sysuser);
	}
	
	
	public void validateSave()
	{
		if(this.hasErrors())
		{
			//取得所有role
			this.setRoleList(userService.getAllRole());
			return;
		}
		if(this.getUser().getId()==null)
		{
			//如果用户名存在，那么返回用户已经存在
			SysUser user=userService.checkSysUserExist(this.getUser().getUsrName());
			if(user!=null)
			{
				//该用户已经存在
				this.addFieldError("user.usrName","该用户已经存在");
				//取得所有role
				this.setRoleList(userService.getAllRole());
				return;
			}
		}
		
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
		this.getSession().remove(Constants.SESSION_USER);
		return "index";
	}

	
	/**
	 * 列表显示,带查询功能
	 * @return
	 * @throws Exception
	 */
	public String list()
			throws Exception
	{
		userService.getUserList(this.getQueryBean(),this.getPage());
		return SUCCESS;
	}
	
	
	
	/**
	 * 添加或修改，跳转到该方法
	 */
	public String input()
			throws Exception
	{
		//取得所有role
		this.setRoleList(userService.getAllRole());
		if(this.getId()!=null)
		{
			SysUser fetched=userService.getUser(Long.parseLong(this.getId()));
			if(fetched!=null)
			{
				this.setUser(fetched);
				this.setReUsrPassword(fetched.getUsrPassword());
			}
		}
		return INPUT;
		
	}

	/**
	 * 删除
	 */
	public String del()
			throws Exception
	{	
		//prepare();
		if(this.getId()!=null)
		{
			userService.delUser(Long.parseLong(this.getId()));
		}
		return SUCCESS;
	}
	
	/**
	 * 保存
	 * @return
	 * @throws Exception
	 */
	public String save()
			throws Exception
	{
		if(this.getUser()!=null)
		{
			userService.saveUser(getUser());
		}
		return Constants.SUCCESS;
	}
	
	public String browse()
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

	public String getValidateCode()
	{
		return validateCode;
	}

	public void setValidateCode(String validateCode)
	{
		this.validateCode = validateCode;
	}

	public String getReUsrPassword()
	{
		return reUsrPassword;
	}

	public void setReUsrPassword(String reUsrPassword)
	{
		this.reUsrPassword = reUsrPassword;
	}

	public List<SysRole> getRoleList()
	{
		return roleList;
	}

	public void setRoleList(List<SysRole> roleList)
	{
		this.roleList = roleList;
	}

	public SysUser getQueryBean()
	{
		return queryBean;
	}

	public void setQueryBean(SysUser queryBean)
	{
		this.queryBean = queryBean;
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

}
