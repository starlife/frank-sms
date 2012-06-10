package com.frank.ylear.modules.base.dao.impl;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.frank.ylear.common.model.PageBean;
import com.frank.ylear.modules.base.dao.BaseDao;
import com.frank.ylear.modules.user.entity.SysRoleRight;
import com.frank.ylear.modules.user.entity.SysUser;

/**
 * @author Administrator
 */
public class BaseDaoImpl extends HibernateDaoSupport implements BaseDao
{

	public Serializable add(Object entity)
	{
		return getHibernateTemplate().save(entity);
	}

	/**
	 * 通过id查询该bean对象
	 */
	public Object get(Class clazz, Serializable id)
	{
		return getHibernateTemplate().get(clazz, id);
	}

	/**
	 * 通过id查询该bean对象
	 */
	/*
	 * public SysUser getSysUserIncludeSysRight( Serializable id){ SysUser
	 * obj=null; Session session=null;
	 * System.out.println("getSysUserIncludeSysRight开始"); try { //取得Session
	 * session=getSession(); String hql="from SysUser where id="+id; Query query =
	 * session.createQuery(hql); List<SysUser> ret = query.list();
	 * if(ret!=null&&ret.size()>0) { obj=ret.get(0); Set
	 * roleRights=obj.getSysRole().getSysRoleRights();
	 * Hibernate.initialize(roleRights); Iterator<SysRoleRight>
	 * it=roleRights.iterator(); while(it.hasNext()) { SysRoleRight
	 * roleRight=it.next(); Hibernate.initialize(roleRight.getSysRight()); } } }
	 * catch(Exception ex) { ex.printStackTrace(); } finally { if(session!=null) {
	 * session.close(); } } System.out.println("getSysUserIncludeSysRight结束");
	 * return obj; }
	 */

	/**
	 * 通过id删除该bean对象
	 */
	public void del(Class clazz, Serializable id)
	{
		getHibernateTemplate().delete(this.get(clazz, id));
	}

	/**
	 * 删除对象
	 */
	public void del(Object entity)
	{
		getHibernateTemplate().delete(entity);
	}

	
	/**
	 * 更新对象
	 */
	public void update(Object entity)
	{
		getHibernateTemplate().update(entity);
	}

	/**
	 * 通过hql查询结果
	 */
	public List list(String hql)
	{
		return getHibernateTemplate().find(hql);
	}

	/**
	 * 查询一页的记录数
	 */
	public void listByPage(String hql, PageBean page)
	{
		if (null == hql)
		{
			return;
		}
		Session session = null;
		try
		{
			// 取得Session
			session = getSession();
			Query query = session.createQuery(hql);
			query.setFirstResult(page.getOffset());
			query.setMaxResults(page.getPageSize());
			List ret = query.list();
			page.setList(ret);
			String queryString = "";
			if (hql.toUpperCase().indexOf("SELECT") != -1)
			{
				int i = query.getQueryString().toUpperCase().indexOf("FROM");
				queryString = "Select count(*) "
						+ hql.substring(i, hql.length());
			}
			else
			{
				queryString = "Select count(*) " + hql;
			}
			// 去除inner join
			int j = queryString.toUpperCase().lastIndexOf("INNER");
			if (j != -1)
			{
				queryString = queryString.substring(0, j);
			}
			// 去除ordery by
			j = queryString.toUpperCase().lastIndexOf("ORDER");
			if (j != -1)
			{
				queryString = queryString.substring(0, j);
			}
			Query cquery = session.createQuery(queryString);
			cquery.setCacheable(true);
			int recTotal = ((Long) cquery.iterate().next()).intValue();
			page.setTotalCount(recTotal);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		finally
		{
			if (session != null)
			{
				session.close();
			}
		}

	}

	/**
	 * 分页查询
	 * 
	 * @param hql
	 *            查询的条件
	 * @param offset
	 *            开始记录
	 * @param length
	 *            一次查询几条记录
	 * @return
	 */
	public List listByPage(final String hql, final int offset, final int length)
	{
		List list = getHibernateTemplate().executeFind(new HibernateCallback()
		{
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException
			{
				Query query = session.createQuery(hql);
				query.setFirstResult(offset);
				query.setMaxResults(length);
				List list = query.list();
				return list;
			}
		});
		return list;
	}
	
	public void execute(String hql)
	{
		// 取得Session
		Session session =null;
		Transaction t = null;
		try
		{
			session = getSession();
			t = session.beginTransaction();
			Query q = session.createQuery(hql);
			// q.setParameter("id", id);
			q.executeUpdate();
			t.commit();
		}
		catch (Exception ex)
		{
			if (t != null)
			{
				t.rollback();
			}
		}
		finally
		{
			if (session != null)
			{
				session.close();
			}
		}
	}


}
