package com.vasp.mm7.dao.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class JdbcTemplate
{
	private static final Log log = LogFactory.getLog(JdbcTemplate.class);

	private ConnectionPool pool = null;

	public JdbcTemplate() throws Exception
	{
		ConnectionPoolManager manager = new ConnectionPoolManager();
		pool=manager.getPool();
	}
	
	public JdbcTemplate(DataSource ds) throws Exception
	{
		ConnectionPoolManager manager = new ConnectionPoolManager(ds);
		pool=manager.getPool();
	}

	public Connection getConnection()
	{
		return pool.getConnection();
	}
	
	public void freeConnection(Connection conn)
	{
		pool.freeConnection(conn);
	}
	
	public boolean execute(String sql)
	{
		Statement stmt = null;
		Connection conn=null;
		try
		{
			conn = getConnection();
			if(conn==null)
			{
				return false;
			}
			conn.setAutoCommit(false);
			stmt = conn.createStatement();
			stmt.execute(sql);
			conn.commit();
		}
		catch (SQLException ex)
		{
			log.error(null, ex);
			return false;
		}
		finally
		{
			DbTool.closeStatement(stmt);
			freeConnection(conn);
		}

		return true;
	}

	public boolean execute(Iterator<String> sql)
	{
		Statement stmt = null;
		Connection conn=null;
		try
		{
			
			conn = getConnection();
			if(conn==null)
			{
				return false;
			}
			conn.setAutoCommit(false);
			stmt = conn.createStatement();
			while (sql.hasNext())
			{
				stmt.addBatch(sql.next());
			}

			int[] rows=stmt.executeBatch();
			for(int i=0;i<rows.length;i++)
			{
				System.out.println("rows["+i+"]="+rows[i]);
			}
			conn.commit();

		}
		catch (SQLException ex)
		{
			log.error(null, ex);
			return false;
		}
		finally
		{
			DbTool.closeStatement(stmt);
			freeConnection(conn);
		}
		return true;
	}

	
	public static void main(String[] args) throws Exception
	{	
		JdbcTemplate template = new JdbcTemplate();
		List<String> sqlList = new ArrayList<String>();
		
		String ss1 = "update lyear.dbo.s_logmmssubmit set subject='linxinzheng' where subject='linxin'";
		String ss2 = "update lyear.dbo.s_logmmssubmit set subject='linxinzheng6' where id='6'";
		String ss3 = "update lyear.dbo.s_logmmssubmit set subject='linxinzheng7' where id='7'";
		sqlList.add(ss2);
		sqlList.add(ss1);
		sqlList.add(ss3);
	
		
		template.execute(sqlList.iterator());
		/*int total = 10000;

		List<String> sqlList = new ArrayList<String>();

		for (int i = 0; i < total; i++)
		{
			String ss = "insert into lyear.dbo.s_logmmssubmit (messageid, "
					+ "transactionid, mm7version, to_address, subject,"
					+ " vaspid, vasid, service_code, linkid, sendtime, "
					+ "status, status_text, sessionid) values ('053101435691006401333','1','6.3.0','13777802301',"
					+ "'zzzz','20120531014355','895192','106573061704','1113329901','',1000,'·¢ËÍ³É¹¦',144)";
			sqlList.add(ss);
			if (sqlList.size() % 10 == 0)
			{
				long begin = System.currentTimeMillis();
				template.execute(sqlList.iterator());
				sqlList.clear();
				long end = System.currentTimeMillis();
				log.info("jdbc insert " + total + " records takes "
						+ (end - begin) + "ms");
				Thread.sleep(1000);
				System.out.println("sleep 1s");
			}
			

		}*/

	}

}
