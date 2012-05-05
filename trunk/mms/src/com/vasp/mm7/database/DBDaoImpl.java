package com.vasp.mm7.database;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
	 * 取一批数据
	 * 
	 * @param queryString
	 * @return
	 */
	public List<?> list(String queryString)
	{
		if (log.isDebugEnabled())
		{
			log.debug("find: " + queryString);
		}

		List<?> list = new ArrayList<Object>();
		try
		{
			Query queryObject = getSession().createQuery(queryString);
			list = queryObject.list();
		}
		catch (Exception re)
		{
			log.error("list failed", re);
		}
		return list;
	}

	/**
	 * 取一条数据
	 * 
	 * @param clazz
	 * @param id
	 * @return
	 */
	public Object get(Class<?> clazz, Serializable id)
	{
		if (log.isDebugEnabled())
		{
			log.debug("get: " + clazz + " " + id);
		}
		Object instance = null;
		try
		{
			instance = getSession().get(clazz, id);
		}
		catch (Exception re)
		{
			log.error("get failed", re);
		}
		return instance;
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
		catch (Exception re)
		{
			log.error("delete failed", re);
		}
	}

	public Serializable save(Object obj)
	{
		if (log.isDebugEnabled())
		{
			log.debug("save:" + obj);
		}
		Serializable id = null;
		try
		{
			Transaction t = getSession().beginTransaction();
			id = getSession().save(obj);
			t.commit();
		}
		catch (Exception re)
		{
			log.error("save failed", re);
		}
		return id;
	}

	public void update(Object obj)
	{
		if (log.isDebugEnabled())
		{
			log.debug("update:" + obj);
		}
		try
		{
			Transaction t = getSession().beginTransaction();
			getSession().update(obj);
			t.commit();
		}
		catch (Exception re)
		{
			log.error("update failed", re);
		}
	}

	public Object merge(Object obj)
	{
		if (log.isDebugEnabled())
		{
			log.debug("merge:" + obj);
		}
		Object o = null;
		try
		{
			Transaction t = getSession().beginTransaction();
			o = getSession().merge(obj);
			t.commit();
		}
		catch (Exception re)
		{
			log.error("merge failed", re);
		}
		return o;
	}

}
