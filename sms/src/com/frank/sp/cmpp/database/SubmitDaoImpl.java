package com.frank.sp.cmpp.database;

import java.util.List;

import com.frank.sp.cmpp.database.pojo.SubmitBean;

/**
 * A data access object (DAO) providing persistence and search support for Sms
 * entities. Transaction control of the save(), update() and delete() operations
 * can directly support Spring container-managed transactions or they can be
 * augmented to handle user-managed Spring transactions. Each of these methods
 * provides additional information for how to configure it for the desired type
 * of transaction control.
 * 
 * @see com.frank.sp.cmpp.database.pojo.Sms
 * @author MyEclipse Persistence Tools
 */

public class SubmitDaoImpl extends DBDaoImpl
{
	//private static final Log log = LogFactory.getLog(SubmitDaoImpl.class);

	private static SubmitDaoImpl dao = new SubmitDaoImpl();

	private SubmitDaoImpl()
	{

	}

	public static SubmitDaoImpl getInstance()
	{
		return dao;
	}
	
	/*public updateReport(Report report)
	{
		
	}*/
	
	public SubmitBean getSubmitBeanByMsgid(String msgid)
	{
		SubmitBean bean=null;
		if(msgid==null||msgid.trim().equals(""))
		{
			return bean;
		}
		List<?> list=this.list("from SubmitBean  obj where obj.msgid='"+msgid+"'");
		if(!list.isEmpty())
		{
			bean =(SubmitBean)list.get(0);
		}
		return bean;
	}
	
	public static void main(String[] args)
	{
		
	}

}
