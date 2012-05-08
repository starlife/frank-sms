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
	 * ��֤�û�������
	 * */
	public SysUser checkSysUserExist(SysUser obj);
	/**
	 * �г�����Ȩ��
	 * @return
	 */
	//public List<SysRight> getAllSysRight();
	
	
	/**
	 * ��ѯ�û��б� 
	 */
	public void getUserList(SysUser user, PageBean page);
	
	/**
	 * ȡ�õ���Ԫ��
	 */	
	public SysUser getUser(Long id);
	
	
	/**
	 * ɾ��
	 */
	public void delUser(Long id);
	
	/**
	 * ����
	 */	
	public boolean saveUser(SysUser user);
	
	
	public List<SysRole> getAllRole();
	
	//public SysUser getIncludeSysRight(Long id);
	public List<SysRight> getSysRightByRole(Long roleid);
}
