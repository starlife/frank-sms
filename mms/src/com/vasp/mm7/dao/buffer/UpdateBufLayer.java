package com.vasp.mm7.dao.buffer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.vasp.mm7.dao.DbDao;
import com.vasp.mm7.database.pojo.SubmitBean;

public class UpdateBufLayer
{
	//private static final Log log = LogFactory.getLog(UpdateBufLayer.class);

	public  String sql=DbDao.updateSql;
		
	private final List<SubmitBean> list=new ArrayList<SubmitBean>();
	
	private long commitTime=System.currentTimeMillis();
	
	private int batch=1000;//ÿ������������
	private int interval=60000;//ʱ���� 60s
	
	
	
	public UpdateBufLayer()
	{
		super();
		// TODO Auto-generated constructor stub
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
		//�����¼����1000�����Զ��ύ
		if(list.size()>=batch)
		{
			commit();
		}
		//���1����û�ύ�ˣ���ôҲ�Զ��ύ
		if((System.currentTimeMillis()-commitTime)>interval)
		{
			commit();
		}
	}
	
	
	public void commit()
	{
		synchronized (list)
		{
			boolean flag=DbDao.getInstance().update(list.iterator());
			//����������ݿ�ɹ�����ô��ոö���
			if(flag)
			{
				commitTime=System.currentTimeMillis();
				list.clear();
			}
		}
	}
}
