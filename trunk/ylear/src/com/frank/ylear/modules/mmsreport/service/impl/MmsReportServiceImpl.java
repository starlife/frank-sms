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
				hql +=" and  obj.mmStatus!=1 ";				
			}else if(status==2)
			{
				//查询未收到状态报告
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
		String hql = "select mmStatus,count(*) count from SubmitBean obj where 1=1 ";
		if (null!=submit){
			if (isItemNotEmpty(submit.getSessionid())){
				hql += "and obj.sessionid ="
						+submit.getSessionid()+" ";				
			}
		}
		hql +="group by mmStatus";
		List list=baseDao.list(hql);
		for(int i=0;i<list.size();i++)
		{
			list.get(0);
		}
		ReportBean report=new ReportBean();
		report.setAllCount(1000L);
		report.setFailCount(100L);
		report.setSuccessCount(700L);
		report.setUnknowCount(200L);
		return null;
	}
	

}
