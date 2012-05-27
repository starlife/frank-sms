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
	
	/*�޸������ظ�������֤*/
	private String reUsrPassword;
	/*ȡ�����н�ɫ���ظ�ui����*/
	private List<SysRole> roleList;
	//��½��֤��
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
		//��½�ɹ���ȡ���û���bean�Ѿ�Ȩ�޲˵�
		List<SysRight> rights=userService.getSysRightByRole(sysuser.getSysRole().getId());
		sysuser.setRights(rights);
		this.getSession().put(Constants.SESSION_USER, sysuser);
	}
	
	
	public void validateSave()
	{
		if(this.hasErrors())
		{
			//ȡ������role
			this.setRoleList(userService.getAllRole());
			return;
		}
		if(this.getUser().getId()==null)
		{
			//����û������ڣ���ô�����û��Ѿ�����
			SysUser user=userService.checkSysUserExist(this.getUser().getUsrName());
			if(user!=null)
			{
				//���û��Ѿ�����
				this.addFieldError("user.usrName","���û��Ѿ�����");
				//ȡ������role
				this.setRoleList(userService.getAllRole());
				return;
			}
		}
		
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
		this.getSession().remove(Constants.SESSION_USER);
		return "index";
	}

	
	/**
	 * �б���ʾ,����ѯ����
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
	 * ��ӻ��޸ģ���ת���÷���
	 */
	public String input()
			throws Exception
	{
		//ȡ������role
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
	 * ɾ��
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
	 * ����
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
