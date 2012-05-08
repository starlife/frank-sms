package com.frank.ylear.modules.user.service;

import java.util.List;

import com.frank.ylear.common.model.PageBean;
import com.frank.ylear.modules.phoneaddress.entity.UPhoneaddress;
import com.frank.ylear.modules.user.entity.SysRight;
import com.frank.ylear.modules.user.entity.SysRole;
import com.frank.ylear.modules.user.entity.SysUser;

public interface UserService
{
	/**
	 * 验证用户名密码
	 * */
	public SysUser checkSysUserExist(SysUser obj);
	/**
	 * 列出所有权限
	 * @return
	 */
	//public List<SysRight> getAllSysRight();
	
	
	/**
	 * 查询用户列表 
	 */
	public void getUserList(SysUser user, PageBean page);
	
	/**
	 * 取得单个元素
	 */	
	public SysUser getUser(Long id);
	
	
	/**
	 * 删除
	 */
	public void delUser(Long id);
	
	/**
	 * 保存
	 */	
	public boolean saveUser(SysUser user);
	
	
	public List<SysRole> getAllRole();
	
	//public SysUser getIncludeSysRight(Long id);
	public List<SysRight> getSysRightByRole(Long roleid);
}
