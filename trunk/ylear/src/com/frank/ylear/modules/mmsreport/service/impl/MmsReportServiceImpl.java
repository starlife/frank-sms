package com.frank.ylear.modules.mmsreport.service.impl;

import java.util.List;

import com.frank.ylear.common.model.PageBean;
import com.frank.ylear.modules.base.service.BaseService;
import com.frank.ylear.modules.mmsreport.entity.ReportBean;
import com.frank.ylear.modules.mmsreport.entity.SubmitBean;
import com.frank.ylear.modules.mmsreport.service.MmsReportService;


public class MmsReportServiceImpl extends BaseService implements MmsReportService
{
	
	/**
	 * ��ѯ
	 * @param submit
	 * @param pageResult
	 */
	public void getList(SubmitBean submit,Integer status,PageBean pageResult) {
		String hql = "select obj from SubmitBean obj where 1=1 ";
		if (null!=submit){
			if (isItemNotEmpty(submit.getSessionid())){
				hql += "and obj.sessionid ="
						+submit.getSessionid()+" ";				
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
				//��ѯ���ͳɹ�
				hql += " and obj.mmStatus =1";
			}else if(status==1)
			{
				// ��ѯ����ʧ��
				hql +=" and  obj.mmStatus!=1 ";				
			}else if(status==2)
			{
				//��ѯδ�յ�״̬����
				hql +=" and obj.mmStatus is null ";
			}
		}
		baseDao.listByPage(hql,pageResult);
	}
	public void del(Long id)
	{
		// TODO Auto-generated method stub
		baseDao.del(SubmitBean.class, id);
	}
	public ReportBean getReport(SubmitBean submit)
	{
		// TODO Auto-generated method stub
		String hql = "select mmStatus,count(*) from SubmitBean obj where 1=1 ";
		if (null!=submit){
			if (isItemNotEmpty(submit.getSessionid())){
				hql += "and obj.sessionid ="
						+submit.getSessionid()+" ";				
			}
		}
		hql +="group by mmStatus";
		List list=baseDao.list(hql);
		long total=0;
		long success=0;
		long failed=0;
		long unknow=0;
		for(int i=0;i<list.size();i++)
		{
			Object line=list.get(i);
			Object[] tuple=(Object[])line;
			Integer t1=(Integer)tuple[0];
			Long t2=(Long)tuple[1];
			if(t1==null)
			{
				unknow=t2;
			}else if(t1==1)
			{
				success=t2;
			}else
			{
				failed+=t2;
			}
		}
		ReportBean report=new ReportBean();
		report.setAllCount(success+failed+unknow);
		report.setFailCount(failed);
		report.setSuccessCount(success);
		report.setUnknowCount(unknow);
		return report;
	}
	

}
