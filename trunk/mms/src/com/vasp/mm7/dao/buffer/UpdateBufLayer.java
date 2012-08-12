package com.vasp.mm7.dao.buffer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.vasp.mm7.dao.DbDao;
import com.vasp.mm7.database.pojo.SubmitBean;

public class UpdateBufLayer extends Thread implements BufferLayer<SubmitBean>
{
	private static final Log log = LogFactory.getLog(UpdateBufLayer.class);

	//public  String sql=DbDao.updateSql;
		
	private final List<SubmitBean> list=new ArrayList<SubmitBean>();
	
	private long commitTime=System.currentTimeMillis();
	
	private int batch=1000;//ÿ������������
	private int interval=60000;//ʱ���� 60s
	
	private volatile boolean stop = false;
	
	
	
	public UpdateBufLayer()
	{
		this.start();
	}

	
	
	public void run()
	{
		while(!stop)
		{
			try
			{
				java.util.concurrent.TimeUnit.SECONDS.sleep(60);
			}
			catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				log.error(null,e);
			}
			autoCommit();
		}
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
		autoCommit();
	}
	
	
	public void autoCommit()
	{
		//System.out.println("autocomit...");
		//�����¼����1000�����Զ��ύ
		if(list.size()>=batch)
		{
			commit();
		}
		//���1����û�ύ�ˣ���ôҲ�Զ��ύ
		//System.out.println(System.currentTimeMillis()-commitTime);
		if((System.currentTimeMillis()-commitTime)>=interval)
		{
			commit();
		}
	}
	
	public void commit()
	{
		log.debug("commit...");
		synchronized (list)
		{
			boolean flag=DbDao.getInstance().update(list);
			//����������ݿ�ɹ�����ô��ոö���
			if(flag)
			{
				commitTime=System.currentTimeMillis();
				list.clear();
			}
		}
	}
	public void myStop()
	{
		this.stop = true;
	}
	
	
}
