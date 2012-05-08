package com.frank.ylear.modules.base.dao;

import java.io.Serializable;
import java.util.List;

import com.frank.ylear.common.model.PageBean;
import com.frank.ylear.modules.user.entity.SysUser;

public interface BaseDao
{
	public Object get(Class  clazz,Serializable id);
	public Serializable add(Object o);
	public void del(Class  clazz,Serializable id);
	public void del(Object entity);
	public void update(Object o);
	public List  list(String sql);
	public void listByPage(String hql, PageBean  pageResult);
	/*Ãÿ ‚∑Ω∑®*/
	//public SysUser getSysUserIncludeSysRight( Serializable id);

}
