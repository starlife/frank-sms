package com.vasp.mm7.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.vasp.mm7.database.pojo.SubmitBean;

/**
 * 修改为jdbc实现
 * 
 * @author Administrator
 */
public class SubmitDaoImpl
{
	private static final Log log = LogFactory.getLog(SubmitDaoImpl.class);

	private static SubmitDaoImpl dao = new SubmitDaoImpl();

	private PreparedStatement insertStat = null;

	private PreparedStatement updateStat = null;

	private SubmitDaoImpl()
	{

	}

	public static SubmitDaoImpl getInstance()
	{
		return dao;
	}

	
	public int save(final List<SubmitBean> list) throws Exception
	{
		Connection conn = DbUtil.getJdbcTemplate().getConnection();
		conn.setAutoCommit(false);
		if (insertStat == null)
		{
			String sql = "insert into lyear.dbo.s_logmmssubmit (messageid, "
					+ "transactionid, mm7version, to_address, subject,"
					+ " vaspid, vasid, service_code, linkid, sendtime, "
					+ "status, status_text, sessionid) values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
			log.debug(System.currentTimeMillis() + " " + sql);
			insertStat = conn.prepareStatement(sql);
		}
		for (int i = 0; i < list.size(); i++)
		{
			SubmitBean submitBean = list.get(i);
			insertStat.setString(1, submitBean.getMessageid());
			insertStat.setString(2, submitBean.getTransactionid());
			insertStat.setString(3, submitBean.getMm7version());
			insertStat.setString(4, submitBean.getToAddress());
			insertStat.setString(5, submitBean.getSubject());
			insertStat.setString(6, submitBean.getVaspid());
			insertStat.setString(7, submitBean.getVasid());
			insertStat.setString(8, submitBean.getServiceCode());
			insertStat.setString(9, submitBean.getLinkid());
			insertStat.setString(10, submitBean.getSendtime());
			insertStat.setInt(11, submitBean.getStatus());
			insertStat.setString(12, submitBean.getStatusText());
			if(submitBean.getSessionid()==null)
			{
				insertStat.setNull(13,java.sql.Types.BIGINT);
			}else
			{
				insertStat.setLong(13, submitBean.getSessionid());
			}
			insertStat.addBatch();
		}
		int[] rows = insertStat.executeBatch();
		conn.commit();
		int ret = 0;
		for (int i = 0; i < rows.length; i++)
		{
			ret += rows[i];
		}
		return ret;

	}

	/**
	 * messageid和to唯一的确定一条记录
	 * 
	 * @param messageid
	 * @param to
	 * @return
	 * @throws Exception 
	 */
	public int update(String messageid, String to, String transcationid,
			String reportTime, Integer mmStatus, String mmStatusText) throws Exception
	{
		Connection conn = DbUtil.getJdbcTemplate().getConnection();
		conn.setAutoCommit(false);
		if (updateStat == null)
		{
			
			String sql = "update lyear.dbo.s_logmmssubmit set transactionid=?, " +
					"report_time=?, mm_status=?, mm_status_text=? " +
					"where messageid=? and to_address=?";
			log.debug(System.currentTimeMillis() + " " + sql);
			updateStat = conn.prepareStatement(sql);
		}
		updateStat.setString(1, transcationid);
		updateStat.setString(2,reportTime);
		updateStat.setInt(3, mmStatus);
		updateStat.setString(4, mmStatusText);
		updateStat.setString(5, messageid);
		updateStat.setString(6, to);
		int row= updateStat.executeUpdate();
		conn.commit();
		return row;
		
	}

}
