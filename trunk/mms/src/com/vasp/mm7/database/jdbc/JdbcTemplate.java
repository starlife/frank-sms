package com.vasp.mm7.database.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class JdbcTemplate
{
	private static final Log log = LogFactory.getLog(JdbcTemplate.class);
	private DataSource ds = null;
	private Connection conn = null;

	// private final List<String> sqlQue=new LinkedList<String>();

	public JdbcTemplate(DataSource ds) throws Exception
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

	private boolean connect()
	{

		String url = ds.getUrl();
		String username = ds.getUsername();
		String password = ds.getPassword();
		try
		{
			conn = DriverManager.getConnection(url, username, password);
			return true;
		}
		catch (SQLException se)
		{
			log.error("数据库连接失败！", se);
			return false;
		}
	}

	/*
	 * 每一次都会重新建立连接
	 */
	private int update1(String sql)
	{
		// 失败返回0，成功返回1
		int row = 0;

		if (!connect())
		{
			return 0;
		}

		Statement stmt = null;
		try
		{
			stmt = conn.createStatement();
			row = stmt.executeUpdate(sql);
		}
		catch (SQLException ex)
		{

			log.error(null, ex);
		}
		finally
		{
			if (stmt != null)
			{
				try
				{
					stmt.close();
				}
				catch (SQLException e)
				{
					// TODO Auto-generated catch block
					log.error(null, e);
				}
			}
		}

		return row;
	}

	public int update(String sql)
	{
		// 失败返回0，成功返回1
		int row = 0;
		if (conn == null)
		{
			if (!connect())
			{
				return 0;
			}
		}
		Statement stmt = null;
		try
		{
			stmt = conn.createStatement();

			row = stmt.executeUpdate(sql);

		}
		catch (SQLException ex)
		{
			update1(sql);
			log.error(null, ex);
		}
		finally
		{
			if (stmt != null)
			{
				try
				{
					stmt.close();
				}
				catch (SQLException e)
				{
					// TODO Auto-generated catch block
					log.error(null, e);
				}
			}
		}

		return row;
	}
	
	public Connection getConnection()
	{
		if (conn == null)
		{
			connect();
			
		}
		return conn;
	}

}
