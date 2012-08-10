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
	
	private int batch=1000;//每批多少条数据
	private int interval=60000;//时间间隔 60s
	
	
	
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
		//如果记录大于1000条，自动提交
		if(list.size()>=batch)
		{
			commit();
		}
		//如果1分钟没提交了，那么也自动提交
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
			//如果插入数据库成功，那么清空该队列
			if(flag)
			{
				commitTime=System.currentTimeMillis();
				list.clear();
			}
		}
	}
}
