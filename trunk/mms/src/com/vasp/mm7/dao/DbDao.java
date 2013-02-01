package com.vasp.mm7.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.vasp.mm7.dao.jdbc.DbTool;
import com.vasp.mm7.dao.jdbc.JdbcTemplate;
import com.vasp.mm7.database.pojo.SubmitBean;

/**
 * 修改为jdbc实现
 * 
 * @author Administrator
 */
public class DbDao extends JdbcTemplate
{
	private static final Log log = LogFactory.getLog(DbDao.class);

	private static final Log insert = LogFactory.getLog("insertSql");// 记录日志
	private static final Log update = LogFactory.getLog("updateSql");// 记录日志

	public static final String insertSql = "insert into lyear.dbo.s_logmmssubmit (messageid, "
			+ "transactionid, mm7version, to_address, subject,"
			+ " vaspid, vasid, service_code, linkid, sendtime, "
			+ "status, status_text, sessionid) values (?,?,?,?,?,?,?,?,?,?,?,?,?)";

	public static final String updateSql = "update lyear.dbo.s_logmmssubmit set transactionid=?, "
			+ "report_time=?, mm_status=?, mm_status_text=? "
			+ "where messageid=? and to_address=?";

	private static DbDao dao = null;

	private DbDao() throws Exception
	{
		super();
	}

	public static DbDao getInstance()
	{
		if (dao == null)
		{
			try
			{
				dao = new DbDao();
			}
			catch (Exception e)
			{
				// TODO Auto-generated catch block
				log.error(null, e);
			}
		}
		return dao;
	}

	public boolean save(List<SubmitBean> list)
	{
		if (list == null || list.size() == 0)
		{
			return true;
		}
		PreparedStatement stmt = null;
		Connection conn = null;
		boolean exception=false;
		try
		{
			conn = getConnection();
			if (conn == null)
			{
				return false;
			}
			stmt = conn.prepareStatement(insertSql);
			conn.setAutoCommit(false);
			Iterator<SubmitBean> it = list.iterator();
			while (it.hasNext())
			{
				SubmitBean submitBean = it.next();
				stmt.setString(1, submitBean.getMessageid());
				stmt.setString(2, submitBean.getTransactionid());
				stmt.setString(3, submitBean.getMm7version());
				stmt.setString(4, submitBean.getToAddress());
				stmt.setString(5, submitBean.getSubject());
				stmt.setString(6, submitBean.getVaspid());
				stmt.setString(7, submitBean.getVasid());
				stmt.setString(8, submitBean.getServiceCode());
				stmt.setString(9, submitBean.getLinkid());
				stmt.setString(10, submitBean.getSendtime());
				stmt.setInt(11, submitBean.getStatus());
				stmt.setString(12, submitBean.getStatusText());
				if (submitBean.getSessionid() == null)
				{
					stmt.setNull(13, java.sql.Types.BIGINT);
				}
				else
				{
					stmt.setLong(13, submitBean.getSessionid());
				}
				stmt.addBatch();
			}
			int[] rows = stmt.executeBatch();
			conn.commit();
			for (int i = 0; i < rows.length; i++)
			{
				if (rows[i] == 0)
				{
					insert.info(getInsertSqlString(list.get(i)));
				}
			}
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

	public boolean update(List<SubmitBean> list)
	{
		if (list == null || list.size() == 0)
		{
			return true;
		}
		PreparedStatement stmt = null;
		Connection conn = null;
		boolean exception=false;
		try
		{
			conn = getConnection();
			if (conn == null)
			{
				return false;
			}
			stmt = conn.prepareStatement(updateSql);
			conn.setAutoCommit(false);
			Iterator<SubmitBean> it = list.iterator();
			while (it.hasNext())
			{
				SubmitBean submitBean = it.next();
				stmt.setString(1, submitBean.getTransactionid());
				stmt.setString(2, submitBean.getReportTime());
				stmt.setInt(3, submitBean.getMmStatus());
				stmt.setString(4, submitBean.getMmStatusText());
				stmt.setString(5, submitBean.getMessageid());
				stmt.setString(6, submitBean.getToAddress());
				stmt.addBatch();
			}
			int[] rows = stmt.executeBatch();
			conn.commit();
			for (int i = 0; i < rows.length; i++)
			{
				if (rows[i] == 0)
				{
					update.info(getUpdateSqlString(list.get(i)));
				}
			}
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

	public static String getInsertSqlString(SubmitBean submitBean)
	{
		String sql = insertSql;
		try
		{
			sql = sql.replace("?", "'%s'");
			String p = String.format(sql, submitBean.getMessageid(), submitBean
					.getTransactionid(), submitBean.getMm7version(), submitBean
					.getToAddress(), submitBean.getSubject(), submitBean
					.getVaspid(), submitBean.getVasid(), submitBean
					.getServiceCode(), submitBean.getLinkid(), submitBean
					.getSendtime(), submitBean.getStatus(), submitBean
					.getStatusText(), submitBean.getSessionid());
			return p;
		}
		catch (Exception ex)
		{
			return sql + submitBean.toString();
		}

	}

	public static String getUpdateSqlString(SubmitBean submitBean)
	{
		String sql = updateSql;
		try
		{
			sql = sql.replace("?", "'%s'");
			String p = String.format(sql, submitBean
					.getTransactionid(), submitBean.getReportTime(), submitBean
					.getMmStatus(), submitBean.getMmStatusText(), submitBean
					.getMessageid(), submitBean.getToAddress());
			
			return p;
		}
		catch (Exception ex)
		{
			return sql + submitBean.toString();
		}

	}

	public static void main(String[] args) throws Exception
	{

		DbDao dao = DbDao.getInstance();
		int total = 10000;

		List<SubmitBean> list = new ArrayList<SubmitBean>();

		for (int i = 0; i < total; i++)
		{
			SubmitBean submitBean = new SubmitBean();
			submitBean.setMessageid("053101435691006401333");
			submitBean.setTransactionid("1");
			submitBean.setMm7version("6.3.0");
			submitBean.setToAddress("13777802301");
			submitBean.setSubject("zzzz");
			submitBean.setSendtime("20120531014355");
			submitBean.setVaspid("895192");
			submitBean.setVasid("106573061704");
			submitBean.setServiceCode("1113329901");
			submitBean.setLinkid(null);
			submitBean.setStatus(1000);
			submitBean.setStatusText("发送成功");
			submitBean.setSessionid(144L);
			list.add(submitBean);
			/*
			 * if(list.size()%10==0) { template.execute(stmt,list.iterator());
			 * list.clear(); }
			 */
		}
		long begin = System.currentTimeMillis();

		boolean b = dao.save(list);
		System.out.println(b);

		long end = System.currentTimeMillis();
		log.info("jdbc insert " + total + " records takes " + (end - begin)
				+ "ms");

	}

}
