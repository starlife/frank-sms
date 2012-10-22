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
	 *ͨ��id����bean����
	 */
	public Object get(Class clazz, Serializable id)
	{
		return getHibernateTemplate().get(clazz, id);
	}

	/**
	 * ��bean����
	 */
	/*
	 * public SysUser getSysUserIncludeSysRight( Serializable id){ SysUser
	 * obj=null; Session session=null;
	 * System.out.println("getSysUserIncludeSysRight��ʼ"); try { //ȡ��Session
	 * session=getSession(); String hql="from SysUser where id="+id; Query query =
	 * session.createQuery(hql); List<SysUser> ret = query.list();
	 * if(ret!=null&&ret.size()>0) { obj=ret.get(0); Set
	 * roleRights=obj.getSysRole().getSysRoleRights();
	 * Hibernate.initialize(roleRights); Iterator<SysRoleRight>
	 * it=roleRights.iterator(); while(it.hasNext()) { SysRoleRight
	 * roleRight=it.next(); Hibernate.initialize(roleRight.getSysRight()); } } }
	 * catch(Exception ex) { ex.printStackTrace(); } finally { if(session!=null) {
	 * session.close(); } } System.out.println("getSysUserIncludeSysRight����");
	 * return obj; }
	 */

	/**
	 * ͨ��idɾ���bean����
	 */
	public void del(Class clazz, Serializable id)
	{
		getHibernateTemplate().delete(this.get(clazz, id));
	}

	/**
	 * 
	 */
	public void del(Object entity)
	{
		getHibernateTemplate().delete(entity);
	}

	
	/**
	 * ���¶���
	 */
	public void update(Object entity)
	{
		getHibernateTemplate().update(entity);
	}

	/**
	 * ���
	 */
	public List list(String hql)
	{
		return getHibernateTemplate().find(hql);
	}

	/**
	 * ���ļ�¼��
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
			// ȡ��Session
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
			// ȥ��inner join
			int j = queryString.toUpperCase().lastIndexOf("INNER");
			if (j != -1)
			{
				queryString = queryString.substring(0, j);
			}
			// ȥ��ordery by
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
		// ȡ��Session
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
