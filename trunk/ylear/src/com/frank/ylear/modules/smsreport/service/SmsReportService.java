package com.frank.ylear.modules.smsreport.service;

import com.frank.ylear.common.model.PageBean;
import com.frank.ylear.modules.mmsreport.entity.ReportBean;
import com.frank.ylear.modules.smsreport.entity.SmsSubmitBean;

public interface SmsReportService
{
	
	/**
	 * ��ѯ�û��б� 
	 */
	public void getList(SmsSubmitBean submit,Integer status,PageBean pageResult);
	
	public void del(Long id);
	
	public ReportBean getReport(SmsSubmitBean submit);

}
