package com.frank.ylear.modules.smsreport.service.impl;

import java.util.List;

import com.frank.ylear.common.constant.Constants;
import com.frank.ylear.common.model.PageBean;
import com.frank.ylear.modules.base.service.BaseService;
import com.frank.ylear.modules.mmsreport.entity.ReportBean;
import com.frank.ylear.modules.smsreport.entity.SmsSubmitBean;
import com.frank.ylear.modules.smsreport.service.SmsReportService;

public class SmsReportServiceImpl extends BaseService implements
		SmsReportService
{

	/**
	 * 查询
	 * 
	 * @param submit
	 * @param pageResult
	 */
	public void getList(SmsSubmitBean submit, Integer status,
			PageBean pageResult)
	{
		String hql = "select obj from SmsSubmitBean obj where 1=1 ";
		if (null != submit)
		{
			if (isItemNotEmpty(submit.getSessionid()))
			{
				hql += "and obj.sessionid =" + submit.getSessionid() + " ";
			}

			if (isItemNotEmpty(submit.getDestId()))
			{
				hql += "and obj.destId ='" + submit.getDestId() + "' ";
			}
		}
		// 解析status
		if (status != null && status != -1)
		{
			if (status == 0)
			{
				// 查询发送成功
				hql += " and obj.stat ='" + Constants.SMS_SUBMIT_SUCCESS + "'";
			}
			else if (status == 1)
			{
				// 查询发送失败
				hql += " and  obj.stat!='" + Constants.SMS_SUBMIT_SUCCESS + "'";
			}
			else if (status == 2)
			{
				// 查询未收到状态报告
				hql += " and obj.stat is null ";
			}
		}
		baseDao.listByPage(hql, pageResult);
	}

	public void del(Long id)
	{
		// TODO Auto-generated method stub
		baseDao.del(SmsSubmitBean.class, id);
	}

	public ReportBean getReport(SmsSubmitBean submit)
	{
		// TODO Auto-generated method stub
		String hql = "select stat,count(*) from SmsSubmitBean obj where 1=1 ";
		if (null != submit)
		{
			if (isItemNotEmpty(submit.getSessionid()))
			{
				hql += "and obj.sessionid =" + submit.getSessionid() + " ";
			}
		}
		hql += "group by stat";
		List list = baseDao.list(hql);
		long total = 0;
		long success = 0;
		long failed = 0;
		long unknow = 0;
		for (int i = 0; i < list.size(); i++)
		{
			Object line = list.get(i);
			Object[] tuple = (Object[]) line;
			String t1 = (String) tuple[0];
			Long t2 = (Long) tuple[1];
			if (t1 == null)
			{
				unknow = t2;
			}
			else if (t1.equals(Constants.SMS_SUBMIT_SUCCESS))
			{
				success = t2;
			}
			else
			{
				failed += t2;
			}
		}
		ReportBean report = new ReportBean();
		report.setAllCount(success + failed + unknow);
		report.setFailCount(failed);
		report.setSuccessCount(success);
		report.setUnknowCount(unknow);
		return report;
	}

}
