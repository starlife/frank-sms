package com.ylear.sp.cmpp.database;


/**
 * A data access object (DAO) providing persistence and search support for Sms
 * entities. Transaction control of the save(), update() and delete() operations
 * can directly support Spring container-managed transactions or they can be
 * augmented to handle user-managed Spring transactions. Each of these methods
 * provides additional information for how to configure it for the desired type
 * of transaction control.
 * 
 * @see com.ylear.sp.cmpp.database.pojo.Sms
 * @author MyEclipse Persistence Tools
 */

public class DeliverDaoImpl extends DBDaoImpl
{
	//private static final Log log = LogFactory.getLog(DeliverDaoImpl.class);

	private static DeliverDaoImpl dao = new DeliverDaoImpl();

	private DeliverDaoImpl()
	{

	}

	public static DeliverDaoImpl getInstance()
	{
		return dao;
	}
	
	
	public static void main(String[] args)
	{
		
	}

}
