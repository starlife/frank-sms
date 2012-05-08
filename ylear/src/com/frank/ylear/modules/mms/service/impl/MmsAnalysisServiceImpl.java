package com.frank.ylear.modules.mms.service.impl;

import java.util.List;

import com.frank.ylear.common.model.PageBean;
import com.frank.ylear.modules.base.service.BaseService;
import com.frank.ylear.modules.mms.entity.SLogMmssubmit;
import com.frank.ylear.modules.mms.service.MmsAnalysisService;


public class MmsAnalysisServiceImpl extends BaseService implements MmsAnalysisService
{
	
	public Long getAllCount(SLogMmssubmit submit)
	{
		String hql = "select count(*) from SLogMmssubmit obj where 1=1 ";
		if (null!=submit){
			if (isItemNotEmpty(submit.getUmmsid())){
				hql += "and obj.ummsid ="
						+submit.getUmmsid()+" ";				
			}
		}
		List list=baseDao.list(hql);
		return (Long)list.get(0);
	}
	public Long getSuccessCount(SLogMmssubmit submit)
	{
		String hql = "select count(*) from SLogMmssubmit obj where 1=1 ";
		if (null!=submit){
			if (isItemNotEmpty(submit.getUmmsid())){
				hql += "and obj.ummsid ="
						+submit.getUmmsid()+" ";				
			}
		}
		//查询成功记录需加上
		hql += "and obj.readstatus is not null ";
		List list=baseDao.list(hql);
		return (Long)list.get(0);
	}
	
	public Long getFailedCount(SLogMmssubmit submit)
	{
		String hql = "select count(*) from SLogMmssubmit obj where 1=1 ";
		if (null!=submit){
			if (isItemNotEmpty(submit.getUmmsid())){
				hql += "and obj.ummsid ="
						+submit.getUmmsid()+" ";				
			}
		}
		// 查询失败记录需加上
		hql +="and (obj.messageid is null " +
		"or obj.statuscode is null " +
		"or obj.statuscode!=1000 " +
		"or (obj.mmstatus is not null and obj.mmstatus!=1))";
		List list=baseDao.list(hql);
		return (Long)list.get(0);
	}
	
	/**
	 * 查询
	 * @param submit
	 * @param pageResult
	 */
	public void getList(SLogMmssubmit submit,Integer status,Integer readstatus, PageBean pageResult) {
		String hql = "select obj from SLogMmssubmit obj where 1=1 ";
		if (null!=submit){
			if (isItemNotEmpty(submit.getUmmsid())){
				hql += "and obj.ummsid ="
						+submit.getUmmsid()+" ";				
			}
			
			if (isItemNotEmpty(submit.getToAddress())){
				hql += "and obj.toAddress ='"
						+submit.getToAddress()+"' ";				
			}
		}
		//解析status
		if(status!=null&&status!=-1)
		{
			if(status==0)
			{
				//查询接收成功的记录
				if(readstatus!=null&&readstatus!=-1)//该关键字有效
				{				
					hql += "and obj.readstatus ="+readstatus+" ";
					
				}else//该关键字无效
				{
					hql += "and obj.readstatus is not null ";
				}
			}else if(status==1)
			{
				// 查询接收失败的记录
				//messageid==null||statuscode==null||statuscode!=1000
				//||(mmstatus!=null&&mmstatus!=1)
				hql +="and (obj.messageid is null " +
						"or obj.statuscode is null " +
						"or obj.statuscode!=1000 " +
						"or (obj.mmstatus is not null and obj.mmstatus!=1))";
				
				
			}
		}
		baseDao.listByPage(hql,pageResult);
	}
	public void del(Long id)
	{
		// TODO Auto-generated method stub
		baseDao.del(SLogMmssubmit.class, id);
	}
	

}
