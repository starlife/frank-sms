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
	 * 查询
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
		//解析status
		if(status!=null&&status!=-1)
		{
			if(status==0)
			{
				//查询发送成功
				hql += " and obj.mmStatus =1";
			}else if(status==1)
			{
				// 查询发送失败
				hql +=" and ( obj.status!=1000 or (obj.status=1000 and  obj.mmStatus!=1 )) ";				
			}else if(status==2)
			{
				//查询未收到状态报告
				hql +="and obj.status=1000 and obj.mmStatus is null ";
			}else
			{
				hql +="and obj.mmStatusText="+status;
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
		//select status,mm_status,count(*) from [lyear].[dbo].[s_logmmssubmit] 
		//where sessionid=27 group by status,mm_status
		String hql = "select status,mmStatus,count(*) from SubmitBean obj where 1=1 ";
		if (null!=submit){
			if (isItemNotEmpty(submit.getSessionid())){
				hql += "and obj.sessionid ="
						+submit.getSessionid()+" ";				
			}
		}
		hql +="group by status,mmStatus";
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
			Integer t2=(Integer)tuple[1];
			Long t3=(Long)tuple[2];
			if(t1==1000)//表示提交成功
			{
				if(t2==null)
				{
					unknow=t3;
				}else if(t2==1)
				{
					success=t3;
				}else
				{
					failed+=t3;
				}
			}else
			{
				//提交失败的就一定失败
				failed+=t3;
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
