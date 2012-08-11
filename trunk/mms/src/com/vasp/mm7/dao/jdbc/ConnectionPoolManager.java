package com.vasp.mm7.dao.jdbc;

import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.cfg.Configuration;

import com.vasp.mm7.database.HibernateSessionFactory;

public class ConnectionPoolManager extends Thread
{
	private static final Log log = LogFactory
			.getLog(ConnectionPoolManager.class);

	private ConnectionPool pool = null;

	private volatile boolean stop = false;

	ConnectionPoolManager() throws Exception
	{

		init();
		this.start();

	}

	ConnectionPoolManager(DataSource ds) throws Exception
	{

		pool = new ConnectionPool(ds);
		this.start();

	}

	public ConnectionPool getPool()
	{
		return pool;
	}

	public void run()
	{
		try
		{
			while (!stop)
			{
				pool.testConnection();
				// log.info("testConnection "+ b);
				try
				{
					TimeUnit.SECONDS.sleep(1);
					// System.out.println("sleep 5s");
				}
				catch (InterruptedException e)
				{
					// TODO Auto-generated catch block
					log.error(null, e);
				}
			}
		}
		finally
		{

		}
	}

	public void mystop()
	{
		this.stop = true;
		pool.close();
	}

	private void init() throws Exception
	{
		DataSource ds = new DataSource();
		Configuration config = HibernateSessionFactory.getConfiguration();
		ds.setDriverClass(config.getProperty("connection.driver_class"));
		ds.setUrl(config.getProperty("connection.url"));
		ds.setUsername(config.getProperty("connection.username"));
		ds.setPassword(config.getProperty("connection.password"));
		pool = new ConnectionPool(ds);
	}

	public static void main(String[] args) throws Exception
	{
		//ConnectionPoolManager manager = new ConnectionPoolManager();
		//ConnectionPool pool = manager.getPool();
		//manager.mystop();
	}

}
