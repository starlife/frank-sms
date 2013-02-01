package com.vasp.mm7.dao.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.cfg.Configuration;

import com.vasp.mm7.database.HibernateSessionFactory;

public class JdbcTemplate
{
	private static final Log log = LogFactory.getLog(JdbcTemplate.class);

	private ConnectionPool pool = null;

	public JdbcTemplate() throws Exception
	{
		//ConnectionPoolManager manager = new ConnectionPoolManager();
		DataSource ds = new DataSource();
		Configuration config = HibernateSessionFactory.getConfiguration();
		ds.setDriverClass(config.getProperty("connection.driver_class"));
		ds.setUrl(config.getProperty("connection.url"));
		ds.setUsername(config.getProperty("connection.username"));
		ds.setPassword(config.getProperty("connection.password"));
		pool = new ConnectionPool(ds);
	}
	
	public JdbcTemplate(DataSource ds) throws Exception
	{
		pool=new ConnectionPool(ds);
	}

	public Connection getConnection()
	{
		return pool.getConnection();
	}
	
	public void freeConnection(Connection conn)
	{
		pool.freeConnection(conn);
	}
	
	public void releaseConnection(Connection conn)
	{
		pool.releaseConnection(conn);
	}
	
	public boolean execute(String sql)
	{
		Statement stmt = null;
		Connection conn=null;
		boolean exception=false;
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
			exception=true;
			return false;
		}
		finally
		{
			DbTool.closeStatement(stmt);
			if(exception)
			{
				this.releaseConnection(conn);
			}else
			{
				freeConnection(conn);
			}
		}

		return true;
	}

	public boolean execute(Iterator<String> sql)
	{
		Statement stmt = null;
		Connection conn=null;
		boolean exception=false;
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
			exception=true;
			return false;
		}
		finally
		{
			DbTool.closeStatement(stmt);
			if(exception)
			{
				this.releaseConnection(conn);
			}else
			{
				freeConnection(conn);
			}
		}
		return true;
	}

	
	public static void main(String[] args) throws Exception
	{	
		//JdbcTemplate template = new JdbcTemplate();
		
		/*int total = 10000;

		List<String> sqlList = new ArrayList<String>();

		for (int i = 0; i < total; i++)
		{
			String ss = "insert into lyear.dbo.s_logmmssubmit (messageid, "
					+ "transactionid, mm7version, to_address, subject,"
					+ " vaspid, vasid, service_code, linkid, sendtime, "
					+ "status, status_text, sessionid) values ('053101435691006401333','1','6.3.0','13777802301',"
					+ "'zzzz','20120531014355','895192','106573061704','1113329901','',1000,'发送成功',144)";
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
