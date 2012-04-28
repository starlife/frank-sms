package com.ylear.sp.cmpp.database;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class DBDaoImpl
{
	private static final Log log = LogFactory.getLog(DBDaoImpl.class);

	public Session getSession()
	{
		return HibernateSessionFactory.getSession();
	}

	/**
	 * 查询多条数据
	 * 
	 * @param queryString
	 * @return
	 */
	public List list(String queryString)
	{
		log.debug("find: " + queryString);
		try
		{
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		}
		catch (RuntimeException re)
		{
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public Object get(Class clazz, Serializable id)
	{
		if (log.isDebugEnabled())
		{
			log.debug("get: " + clazz + " " + id);
		}
		try
		{
			Object instance = getSession().get(clazz, id);
			return instance;
		}
		catch (RuntimeException re)
		{
			log.error("get failed", re);
			throw re;
		}
	}

	public void delete(Object obj)
	{
		if (log.isDebugEnabled())
		{
			log.debug("del: " + obj);
		}

		try
		{
			Transaction t = getSession().beginTransaction();
			getSession().delete(obj);
			t.commit();
		}
		catch (RuntimeException re)
		{
			log.error("get failed", re);
			throw re;
		}
	}

	public Serializable save(Object obj)
	{
		if (log.isDebugEnabled())
		{
			log.debug("save:" + obj);
		}
		try
		{
			Transaction t = getSession().beginTransaction();
			Serializable id = getSession().save(obj);
			t.commit();
			return id;
		}
		catch (RuntimeException re)
		{
			log.error("save failed", re);
			throw re;
		}
	}

	/*
	 * public Object merge(Object obj) { log.debug("merge:"+obj); try { Object
	 * result = getSession().merge(obj); log.debug("merge successful"); return
	 * result; } catch (RuntimeException re) { log.error("merge failed", re);
	 * throw re; } } public void attachDirty(Object obj) {
	 * log.debug("attachDirty:"+obj); try { getSession().saveOrUpdate(obj);
	 * log.debug("attach successful"); } catch (RuntimeException re) {
	 * log.error("attach failed", re); throw re; } } public void
	 * attachClean(Object obj) { log.debug("attachClean:"+obj); try {
	 * getSession().lock(obj, LockMode.NONE); log.debug("attach successful"); }
	 * catch (RuntimeException re) { log.error("attach failed", re); throw re; } }
	 */

}
