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
 * 该类是对数据库插入做了buf层，主要提供2个功能：
 * *数据的缓冲（批插入）以提高数据库操作效率
 * *错误数据的缓存（在数据库宕机的时候可以把数据缓存在内存中或者文件中，等待重新执行）
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
			//如果插入数据库成功，那么清空该队列
			if(flag)
			{
				list.clear();
			}
		}
	}



	
	
}
