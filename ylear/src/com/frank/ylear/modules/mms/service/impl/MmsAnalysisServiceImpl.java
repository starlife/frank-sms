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
		//��ѯ�ɹ���¼�����
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
		// ��ѯʧ�ܼ�¼�����
		hql +="and (obj.messageid is null " +
		"or obj.statuscode is null " +
		"or obj.statuscode!=1000 " +
		"or (obj.mmstatus is not null and obj.mmstatus!=1))";
		List list=baseDao.list(hql);
		return (Long)list.get(0);
	}
	
	/**
	 * ��ѯ
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
		//����status
		if(status!=null&&status!=-1)
		{
			if(status==0)
			{
				//��ѯ���ճɹ��ļ�¼
				if(readstatus!=null&&readstatus!=-1)//�ùؼ�����Ч
				{				
					hql += "and obj.readstatus ="+readstatus+" ";
					
				}else//�ùؼ�����Ч
				{
					hql += "and obj.readstatus is not null ";
				}
			}else if(status==1)
			{
				// ��ѯ����ʧ�ܵļ�¼
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
