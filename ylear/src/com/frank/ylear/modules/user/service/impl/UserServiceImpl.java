package com.frank.ylear.modules.user.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.frank.ylear.common.model.PageBean;
import com.frank.ylear.modules.base.service.BaseService;
import com.frank.ylear.modules.user.entity.SysRight;
import com.frank.ylear.modules.user.entity.SysRole;
import com.frank.ylear.modules.user.entity.SysUser;
import com.frank.ylear.modules.user.service.UserService;

public class UserServiceImpl extends BaseService implements UserService
{
	
	

	public SysUser checkSysUserExist(SysUser obj)
	{
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		SysUser sysUser=null;
		String hsql="from SysUser obj where obj.usrName='"+obj.getUsrName()
		+"' and obj.usrPassword='"+obj.getUsrPassword()+"'";
		List list=baseDao.list(hsql);
		if(list.size()==1)
		{
			sysUser=(SysUser)list.get(0);
		}
		return sysUser;
	}
	
	public SysUser checkSysUserExist(String username)
	{
		// TODO Auto-generated method stub
		SysUser sysUser=null;
		String hsql="from SysUser obj where obj.usrName='"+username+"'";
		List list=baseDao.list(hsql);
		if(list.size()==1)
		{
			sysUser=(SysUser)list.get(0);
		}
		return sysUser;
	}

	
	/**
	 * 查询用户列表 
	 */
	public void getUserList(SysUser user, PageBean page)
	{
		String hql = "from SysUser obj where 1=1 ";
		if (null!=user){
			if (isItemNotEmpty(user.getUsrName())){
				hql += "and obj.usrName ='"
						+user.getUsrName()+"' ";				
			}
			if (isItemNotEmpty(user.getUsrRealname())){
				hql += "and obj.usrRealname ='"
						+user.getUsrRealname()+"' ";				
			}
		}
		baseDao.listByPage(hql,page);
	}
	
	/**
	 * 取得单个元素
	 */	
	public SysUser getUser(Long id)
	{
		return (SysUser) baseDao.get(SysUser.class, id);
	}

	/**
	 * 删除
	 */
	public void delUser(Long id)
	{
		baseDao.del(SysUser.class, id);
	}
	
	/**
	 * 保存
	 */	
	public boolean saveUser(SysUser user)
	{
		if(user.getId()!=null)
		{
			baseDao.update(user);
		}else
		{
			baseDao.add(user);
		}
		return true;
	}
	
	
	public List<SysRole> getAllRole()
	{
		String hsql="from SysRole";
		List<SysRole> roles=baseDao.list(hsql);
		return roles;
	}
	
	
	public List<SysRight> getSysRightByRole(Long roleId)
	{
		String hsql="select roleRight.sysRight from SysRoleRight roleRight where roleRight.sysRole.id="+roleId;
		List<SysRight> rights=baseDao.list(hsql);
		Iterator<SysRight> it = rights.iterator();
		while(it.hasNext()){
			SysRight right = it.next();
			String rightCode=right.getRightCode();
			List<SysRight> childRights=getChild(rights, rightCode);
			if(childRights.size()>0){
				right.setParent(true);
			} else {
				right.setParent(false);
			}
			right.setChildRights(childRights);
		}
		return rights;
	}
	
	
	public List<SysRight> getChild(List<SysRight> list, String parentCode) {
		List<SysRight> newList=new ArrayList<SysRight>();
		Iterator<SysRight> it = list.iterator();
		//boolean bool = false;
		while (it.hasNext()) {
			SysRight right=it.next();
			if (parentCode.equals(right.getRightParentCode())) {
				//bool = true;
				newList.add(right);
			}
		}
		return newList;
	}
	/*public SysUser getIncludeSysRight(Long id)
	{
		//SysUser sysUser=baseDao.getSysUserIncludeSysRight(id);
		String hsql="select roleRight.sysRight from SysRoleRight roleRight where roleRight.sysRole.id="+id;
		List<SysRight> list=baseDao.list(hsql);
		if(sysUser!=null)
		{
			SysRole role=sysUser.getSysRole();
			if(role!=null)
			{
				Set roleRights=role.getSysRoleRights();
				if(roleRights!=null&&roleRights.size()>0)
				{
					Iterator<SysRoleRight> it = roleRights.iterator();
					while(it.hasNext()){
						//SysRoleRight roleRight = it.next();
						SysRight right=it.next().getSysRight();
						String rightCode=right.getRightCode();
						System.out.println(rightCode);
						Set<SysRight> childRights=this.getChild(roleRights, rightCode);
						if(childRights.size()>0){
							right.setParent(true);
						} else {
							right.setParent(false);
						}
						right.setChildRights(childRights);
					}
				}
			}
		}
		return sysUser;
		
	}*/

	
	
	
	/*public List<SysRight> getAllSysRight()
	{
		// TODO Auto-generated method stub
		String hsql="select obj from SysRight obj";
		List<SysRight> rights=baseDao.list(hsql);
		Iterator<SysRight> it = rights.iterator();
		while(it.hasNext()){
			SysRight right = it.next();
			String rightCode=right.getRightCode();
			List<SysRight> childRights=getChild(rights, rightCode);
			if(childRights.size()>0){
				right.setParent(true);
			} else {
				right.setParent(false);
			}
			right.setChildRights(childRights);
		}
		return rights;
			
	}*/
	
	
	
}
