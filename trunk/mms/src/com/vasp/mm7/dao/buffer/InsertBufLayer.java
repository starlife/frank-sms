package com.vasp.mm7.dao.buffer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.vasp.mm7.dao.DbDao;
import com.vasp.mm7.database.pojo.SubmitBean;

/**
 * 
 * @author Administrator
 * 
 * �����Ƕ����ݿ��������buf�㣬��Ҫ�ṩ2�����ܣ�
 * *���ݵĻ��壨�����룩��������ݿ����Ч��
 * *�������ݵĻ��棨�����ݿ�崻���ʱ����԰����ݻ������ڴ��л����ļ��У��ȴ�����ִ�У�
 *
 */
public class InsertBufLayer implements BufferLayer<SubmitBean>
{
	

	//private static final Log log = LogFactory.getLog(InsertBufLayer.class);

	//public  String sql=DbDao.insertSql;
		
	private final List<SubmitBean> list=new ArrayList<SubmitBean>();
	
	
	
	public InsertBufLayer()
	{
		
	}

	
	
	public void add(Iterator<SubmitBean> it)
	{
		synchronized (list)
		{
			while(it.hasNext())
			{
				list.add(it.next());
			}
		}
		
	}
	public void add(SubmitBean bean)
	{
		synchronized (list)
		{
			list.add(bean);
		}
	}
	
	public void commit()
	{
		synchronized (list)
		{
			boolean flag=DbDao.getInstance().save(list);
			//����������ݿ�ɹ�����ô��ոö���
			if(flag)
			{
				list.clear();
			}
		}
	}



	
	
}
