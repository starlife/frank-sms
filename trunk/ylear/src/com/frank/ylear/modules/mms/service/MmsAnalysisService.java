package com.frank.ylear.modules.mms.service;

import com.frank.ylear.common.model.PageBean;
import com.frank.ylear.modules.mms.entity.SLogMmssubmit;

public interface MmsAnalysisService
{
	
	/**
	 * ��ѯ�û��б� 
	 */
	public void getList(SLogMmssubmit submit,Integer status,Integer readstatus, PageBean pageResult);
	
	public void del(Long id);
	
	public Long getFailedCount(SLogMmssubmit submit);
	public Long getSuccessCount(SLogMmssubmit submit);
	public Long getAllCount(SLogMmssubmit submit);

}
