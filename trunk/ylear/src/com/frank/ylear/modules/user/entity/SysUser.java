package com.frank.ylear.modules.user.entity;

import java.util.List;

/**
 * SysUser entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class SysUser implements java.io.Serializable
{

	// Fields

	private Long id;
	private SysRole sysRole;
	private String usrName;
	private String usrPassword;
	private String usrRealname;

	
	private List<SysRight> rights;
	// Constructors

	/** default constructor */
	public SysUser()
	{
	}

	/** minimal constructor */
	public SysUser(String usrName, String usrPassword)
	{
		this.usrName = usrName;
		this.usrPassword = usrPassword;
	}

	/** full constructor */
	public SysUser(SysRole sysRole, String usrName, String usrPassword,
			String usrRealname)
	{
		this.sysRole = sysRole;
		this.usrName = usrName;
		this.usrPassword = usrPassword;
		this.usrRealname = usrRealname;
	}

	// Property accessors

	public Long getId()
	{
		return this.id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public SysRole getSysRole()
	{
		return this.sysRole;
	}

	public void setSysRole(SysRole sysRole)
	{
		this.sysRole = sysRole;
	}

	public String getUsrName()
	{
		return this.usrName;
	}

	public void setUsrName(String usrName)
	{
		this.usrName = usrName;
	}

	public String getUsrPassword()
	{
		return this.usrPassword;
	}

	public void setUsrPassword(String usrPassword)
	{
		this.usrPassword = usrPassword;
	}

	public String getUsrRealname()
	{
		return this.usrRealname;
	}

	public void setUsrRealname(String usrRealname)
	{
		this.usrRealname = usrRealname;
	}
	
	public String toString()
	{
		return this.getId()+" "+this.getUsrName()+" "+this.getUsrPassword()
		+" "+this.getUsrRealname()+"( "+this.getSysRole()+")";
	}

	public List<SysRight> getRights()
	{
		return rights;
	}

	public void setRights(List<SysRight> rights)
	{
		this.rights = rights;
	}

}
