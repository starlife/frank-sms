package com.frank.ylear.modules.user.entity;

import java.util.HashSet;
import java.util.Set;

/**
 * SysRole entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class SysRole implements java.io.Serializable
{

	// Fields

	private Long id;
	private String roleName;
	private String roleDesc;
	private Set sysUsers = new HashSet(0);
	private Set sysRoleRights = new HashSet(0);

	// Constructors

	/** default constructor */
	public SysRole()
	{
	}

	/** minimal constructor */
	public SysRole(String roleName)
	{
		this.roleName = roleName;
	}

	/** full constructor */
	public SysRole(String roleName, String roleDesc, Set sysUsers,
			Set sysRoleRights)
	{
		this.roleName = roleName;
		this.roleDesc = roleDesc;
		this.sysUsers = sysUsers;
		this.sysRoleRights = sysRoleRights;
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

	public String getRoleName()
	{
		return this.roleName;
	}

	public void setRoleName(String roleName)
	{
		this.roleName = roleName;
	}

	public String getRoleDesc()
	{
		return this.roleDesc;
	}

	public void setRoleDesc(String roleDesc)
	{
		this.roleDesc = roleDesc;
	}

	public Set getSysUsers()
	{
		return this.sysUsers;
	}

	public void setSysUsers(Set sysUsers)
	{
		this.sysUsers = sysUsers;
	}

	public Set getSysRoleRights()
	{
		return this.sysRoleRights;
	}

	public void setSysRoleRights(Set sysRoleRights)
	{
		this.sysRoleRights = sysRoleRights;
	}
	
	public String toString()
	{
		return this.getRoleName()+" "+this.getRoleDesc()+" "; 
	}

}
