package com.ylear.sp.cmpp.database;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Example;

public class BaseDaoImpl
{
	private static final Log log = LogFactory.getLog(BaseDaoImpl.class);
	
	public Session getSession() {
		return HibernateSessionFactory.getSession();
	}
	
	
	
	public List find(String queryString)
	{
		log.debug("find: "+queryString);
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
	
	

	

	public Serializable save(Object obj)
	{
		log.debug("save:"+obj);
		try
		{
			Serializable id=getSession().save(obj);
			log.debug("save successful");
			return id;
		}
		catch (RuntimeException re)
		{
			log.error("save failed", re);
			throw re;
		}
	}

	
	public void delete(Object obj)
	{
		log.debug("delete:"+obj);
		try
		{
			getSession().delete(obj);
			log.debug("delete successful");
		}
		catch (RuntimeException re)
		{
			log.error("delete failed", re);
			throw re;
		}
	}

	public Object get(Class clazz,Serializable id)
	{
		if(log.isDebugEnabled())
		{
			log.debug("get: " +clazz +" "+ id);
		}
		try
		{
			Object instance =  getSession().get(clazz, id);
			return instance;
		}
		catch (RuntimeException re)
		{
			log.error("get failed", re);
			throw re;
		}
	}

	

	public Object merge(Object obj)
	{
		log.debug("merge:"+obj);
		try
		{
			Object result = getSession().merge(obj);
			log.debug("merge successful");
			return result;
		}
		catch (RuntimeException re)
		{
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Object obj)
	{
		log.debug("attachDirty:"+obj);
		try
		{
			getSession().saveOrUpdate(obj);
			log.debug("attach successful");
		}
		catch (RuntimeException re)
		{
			log.error("attach failed", re);
			throw re;
		}
	}

	
	public void attachClean(Object obj)
	{
		log.debug("attachClean:"+obj);
		try
		{
			getSession().lock(obj, LockMode.NONE);
			log.debug("attach successful");
		}
		catch (RuntimeException re)
		{
			log.error("attach failed", re);
			throw re;
		}
	}
	
	

	
}
