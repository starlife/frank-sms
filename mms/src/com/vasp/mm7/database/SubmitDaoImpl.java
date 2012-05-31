package com.vasp.mm7.database;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.vasp.mm7.database.pojo.SubmitBean;
import com.vasp.mm7.database.sql.SqlUpdate;

/**
 * 修改为jdbc实现
 * @author Administrator
 *
 */
public class SubmitDaoImpl
{
	private static final Log log = LogFactory.getLog(SubmitDaoImpl.class);

	private static SubmitDaoImpl dao = new SubmitDaoImpl();

	private SubmitDaoImpl()
	{

	}

	public static SubmitDaoImpl getInstance()
	{
		return dao;
	}

	private static void addFields(StringBuffer sb, Object field, boolean last)
	{
		if (field == null)
		{
			sb.append("null");
		}
		else if (field instanceof String)
		{
			sb.append("'").append((String) field).append("'");
		}
		else
		{
			sb.append(field);
		}
		if (!last)
		{
			sb.append(",");
		}
	}

	public int save(SubmitBean submitBean)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("insert into lyear.dbo.s_logmmssubmit (messageid, "
				+ "transactionid, mm7version, to_address, subject,"
				+ " vaspid, vasid, service_code, linkid, sendtime, "
				+ "status, status_text, report_time, mm_status,"
				+ " mm_status_text, sessionid) values (");
		addFields(sql, submitBean.getMessageid(), false);
		addFields(sql, submitBean.getTransactionid(), false);
		addFields(sql, submitBean.getMm7version(), false);
		addFields(sql, submitBean.getToAddress(), false);
		addFields(sql, submitBean.getSubject(), false);
		addFields(sql, submitBean.getVaspid(), false);
		addFields(sql, submitBean.getVasid(), false);
		addFields(sql, submitBean.getServiceCode(), false);
		addFields(sql, submitBean.getLinkid(), false);
		addFields(sql, submitBean.getSendtime(), false);
		addFields(sql, submitBean.getStatus(), false);
		addFields(sql, submitBean.getStatusText(), false);
		addFields(sql, submitBean.getReportTime(), false);
		addFields(sql, submitBean.getMmStatus(), false);
		addFields(sql, submitBean.getMmStatusText(), false);
		addFields(sql, submitBean.getSessionid(), true);
		sql.append(")");
		log.debug(sql);
		return DbUtil.execute(sql.toString());
	}

	/**
	 * messageid和to唯一的确定一条记录
	 * 
	 * @param messageid
	 * @param to
	 * @return
	 */
	public int update(String messageid, String to, String transcationid,
			String reportTime, Integer mmStatus, String mmStatusText)
	{

		SqlUpdate sql = new SqlUpdate();
		sql.setTableName("lyear.dbo.s_logmmssubmit");
		sql.addSetField("transactionid", transcationid);
		sql.addSetField("report_time", reportTime);
		sql.addSetField("mm_status", mmStatus);
		sql.addSetField("mm_status_text", mmStatusText);
		sql.addWhereField("messageid", messageid);
		sql.addWhereField("to_address", to);
		log.debug(sql);
		return DbUtil.execute(sql.toString());

	}

}
