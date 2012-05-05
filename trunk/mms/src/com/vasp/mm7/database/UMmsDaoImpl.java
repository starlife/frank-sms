package com.vasp.mm7.database;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Transaction;

import com.vasp.mm7.database.pojo.UMms;

public class UMmsDaoImpl extends DBDaoImpl
{
	private static final Log log = LogFactory.getLog(UMmsDaoImpl.class);

	private static UMmsDaoImpl dao = new UMmsDaoImpl();

	private UMmsDaoImpl()
	{

	}

	public static UMmsDaoImpl getInstance()
	{
		return dao;
	}

	/**
	 * ȡ�������͵ļ�¼����ɾ����Щ��¼��ͨ������sendtime������ʱ�䣩���ж�
	 * @param value  ʱ���
	 * @return
	 */
	public List<UMms> getReadySendSms(String value)
	{
		List<UMms> list = new ArrayList<UMms>();
		try
		{

			String sql = "from UMms obj where  obj.status=0 and obj.sendtime< '"
					+ value + "'";
			List<?> temp = this.list(sql);

			if (temp.size() > 0)
			{
				// ״̬��Ϊ1
				Transaction t = getSession().beginTransaction();
				try
				{
					sql = "update UMms obj set obj.status=1 where  obj.status=0 and obj.sendtime< ?";
					Query queryObject = getSession().createQuery(sql);
					queryObject.setParameter(0, value);
					int i = queryObject.executeUpdate();
					log.info("update:"+i);
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
				list.add((UMms) temp.get(i));
			}
			temp.clear();

		}
		catch (Exception re)
		{
			log.error("find by property name failed", re);
		}
		return list;
	}
	
	

}
