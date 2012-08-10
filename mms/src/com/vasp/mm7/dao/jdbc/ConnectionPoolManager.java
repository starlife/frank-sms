package com.vasp.mm7.dao.jdbc;

import java.sql.Connection;
import java.util.Random;
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
		pool.mystop();
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

	
	
	
	static class TestThread extends Thread
	{
		ConnectionPool pool = null;
		Random r = new Random(100);

		public TestThread(ConnectionPool pool)
		{
			this.pool = pool;
		}

		public void run()
		{
			while (true)
			{
				Connection conn = pool.getConnection();
				int rom = r.nextInt(10);
				try
				{
					TimeUnit.SECONDS.sleep(rom);
				}
				catch (InterruptedException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				pool.freeConnection(conn);
				try
				{
					TimeUnit.SECONDS.sleep(20);
				}
				catch (InterruptedException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public static void main(String[] args) throws Exception
	{
		ConnectionPoolManager manager = new ConnectionPoolManager();
		ConnectionPool pool = manager.getPool();
		for (int i = 0; i < 5; i++)
		{
			new TestThread(pool).start();
		}
		TimeUnit.SECONDS.sleep(10000);
		manager.mystop();
	}

}
