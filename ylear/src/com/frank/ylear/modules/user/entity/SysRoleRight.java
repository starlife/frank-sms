package com.frank.ylear.modules.user.entity;

/**
 * SysRoleRight entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class SysRoleRight implements java.io.Serializable
{

	// Fields

	private Long id;
	private SysRole sysRole;
	private SysRight sysRight;

	// Constructors

	/** default constructor */
	public SysRoleRight()
	{
	}

	/** full constructor */
	public SysRoleRight(SysRole sysRole, SysRight sysRight)
	{
		this.sysRole = sysRole;
		this.sysRight = sysRight;
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

	public SysRight getSysRight()
	{
		return this.sysRight;
	}

	public void setSysRight(SysRight sysRight)
	{
		this.sysRight = sysRight;
	}

}
