package com.frank.ylear.modules.mmsreport.service;

import com.frank.ylear.common.model.PageBean;
import com.frank.ylear.modules.mmsreport.entity.ReportBean;
import com.frank.ylear.modules.mmsreport.entity.SubmitBean;

public interface MmsReportService
{
	
	/**
	 * 查询用户列表 
	 */
	public void getList(SubmitBean submit,Integer status,PageBean pageResult);
	
	public void del(Long id);
	
	public ReportBean getReport(SubmitBean submit);

}
