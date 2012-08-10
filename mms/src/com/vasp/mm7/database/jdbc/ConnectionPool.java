package com.vasp.mm7.database.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ConnectionPool
{
	private static final Log log = LogFactory.getLog(ConnectionPool.class);
	private DataSource ds = null;// 配置文件
	private static final LinkedBlockingQueue<Connection> que = new LinkedBlockingQueue<Connection>();// 连接队列
	private static final List<Connection> usedList = new LinkedList<Connection>();
	private int maxConn = 5;// 最大连接数

	// private int mixConn=5;//最小连接数
	// private int used=0;//这在使用的连接数

	// private static final Object lock=new Object();

	public ConnectionPool(DataSource ds) throws Exception
	{
		this.ds = ds;
		try
		{
			// 加载驱动类
			Class.forName(ds.getDriverClass());
		}
		catch (ClassNotFoundException e)
		{
			throw new Exception("找不到驱动程序类 " + ds.getDriverClass() + "，加载驱动失败！");
		}

	}

	private Connection createConnection()
	{
		//log.info("createConnection");
		//log.info("que.size():"+que.size()+"-------userList.size():"+usedList.size());
		Connection conn = null;
		String url = ds.getUrl();
		String username = ds.getUsername();
		String password = ds.getPassword();
		try
		{
			conn = DriverManager.getConnection(url, username, password);
			log.info("createConnection "+conn);

		}
		catch (SQLException se)
		{
			log.error("数据库连接失败！", se);

		}
		return conn;
	}

	public Connection getConnection()
	{
		//log.info("getConnection");
		synchronized (que)
		{
			Connection conn = que.poll();
			if (conn != null)
			{
				usedList.add(conn);
			}
			else
			{
				if (usedList.size() < this.maxConn)
				{
					conn = this.createConnection();
					if (conn != null)
					{
						usedList.add(conn);
					}
				}
			}
			/*if(conn!=null)
			{
				log.info("getConnection "+conn);
			}*/
			return conn;
		}

	}

	public void freeConnection(Connection conn)
	{
		this.freeConnection(conn,true);
	}
	
	public void freeConnection(Connection conn,boolean valid)
	{	
		synchronized (que)
		{
			if (conn != null)
			{
				//log.info("freeConnection "+conn);
				if(!valid)
				{	log.info("remove "+conn);			
					usedList.remove(conn);		
				}else
				{
					if (usedList.contains(conn) && !que.contains(conn))
					{
						que.offer(conn);
						usedList.remove(conn);
					}
				}
				//log.info("que.size():"+que.size()+"-------userList.size():"+usedList.size());

			}
		}
		
	}
	
	public void mystop()
	{
		Connection conn=this.getConnection();
		while(conn!=null)
		{
			DbTool.closeConnection(conn);
			conn=this.getConnection();
		}
		usedList.clear();
	}

	public boolean testConnection()
	{
		Statement stmt = null;
		Connection conn = null;
		boolean success=false;
		try
		{
			conn = this.getConnection();
			if (conn == null)
			{
				return false;
			}
			stmt = conn.createStatement();
			stmt.execute("SELECT getDate()");
			success=true;
		}
		catch (SQLException ex)
		{
			log.error(null, ex);
			return false;
		}
		finally
		{
			DbTool.closeStatement(stmt);	
			this.freeConnection(conn,success);
			
		}
		return true;
	}

}
