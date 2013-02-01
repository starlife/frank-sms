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
	
	private int batch=1000;//每批多少条数据
	private int interval=60000;//时间间隔 60s
	
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
		//如果记录大于1000条，自动提交
		if(list.size()>=batch)
		{
			commit();
		}
		//如果1分钟没提交了，那么也自动提交
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
			//如果插入数据库成功，那么清空该队列
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
