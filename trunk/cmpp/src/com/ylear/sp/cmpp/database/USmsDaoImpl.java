package com.ylear.sp.cmpp.database;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Transaction;

import com.ylear.sp.cmpp.database.pojo.USms;
import com.ylear.sp.cmpp.util.DateUtils;

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

public class USmsDaoImpl extends DBDaoImpl
{
	private static final Log log = LogFactory.getLog(USmsDaoImpl.class);

	private static USmsDaoImpl dao = new USmsDaoImpl();

	private USmsDaoImpl()
	{

	}

	public static USmsDaoImpl getInstance()
	{
		return dao;
	}

	/**
	 * 取出待发送的记录，并删除这些记录，通过属性sendtime（发送时间）来判断
	 * 
	 * @param propertyName
	 *            发送时间
	 * @param value
	 *            发送时间
	 * @return
	 */
	public List<USms> getReadySendSms(String value)
	{
		List<USms> list = new ArrayList<USms>();
		try
		{

			String sql = "from USms obj where  obj.status=0 and obj.sendtime< '"
					+ value + "'";
			List temp = this.list(sql);

			if (temp.size() > 0)
			{
				// 状态改为1
				Transaction t = getSession().beginTransaction();
				try
				{
					sql = "update USms obj set obj.status=1 where  obj.status=0 and obj.sendtime< ?";
					Query queryObject = getSession().createQuery(sql);
					queryObject.setParameter(0, value);
					int i = queryObject.executeUpdate();
					t.commit();
				}
				catch (Exception ex)
				{
					log.error(null, ex);
					t.rollback();
				}

			}
			for (int i = 0; i < temp.size(); i++)
			{
				list.add((USms) temp.get(i));
			}
			temp.clear();

		}
		catch (Exception re)
		{
			log.error("find by property name failed", re);
		}
		return list;
	}

	public static void main(String[] args)
	{
		USmsDaoImpl impl = new USmsDaoImpl();
		List<USms> list = impl.getReadySendSms(DateUtils.getCurrentTimeShort());
		for (int i = 0; i < list.size(); i++)
		{
			System.out.println("###########################" + list.get(i));
		}
	}

}
