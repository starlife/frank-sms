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
			// ����������
			Class.forName(ds.getDriverClass());
		}
		catch (ClassNotFoundException e)
		{
			throw new Exception("�Ҳ������������� " + ds.getDriverClass() + "����������ʧ�ܣ�");
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
			log.error("���ݿ�����ʧ�ܣ�", se);
			return false;
		}
	}

	/*
	 * ÿһ�ζ������½�������
	 */
	private int update1(String sql)
	{
		// ʧ�ܷ���0���ɹ�����1
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
		// ʧ�ܷ���0���ɹ�����1
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
