package com.frank.ylear.modules.mms.service;

import java.io.Serializable;

import com.frank.ylear.common.model.PageBean;
import com.frank.ylear.modules.mms.entity.UMms;

public interface MmsService
{
	
	/**
	 *
	 */
	public void getMmsList(UMms mms, PageBean<UMms> pageResult);
	
	/**
	 *ȡ�õ���Ԫ��
	 */	
	public UMms getMms(Long id);
	/**
	 *
	 */
	public void delMms(Long id);
	/**
	 *���
	 */
	public Serializable saveMms(UMms mms);
	
	

}
