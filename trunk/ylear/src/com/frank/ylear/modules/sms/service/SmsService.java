package com.frank.ylear.modules.sms.service;

import com.frank.ylear.common.model.PageBean;
import com.frank.ylear.modules.sms.entity.USms;

public interface SmsService
{
	
	/**
	 * ��ѯ�û��б� 
	 */
	public void getSmsList(USms address, PageBean pageResult);
	
	/**
	 * ȡ�õ���Ԫ��
	 */	
	public USms getSms(Long id);
	/**
	 * ɾ��
	 */
	public void delSms(Long id);
	/**
	 * ���
	 */
	public void saveSms(USms address);
	
	
	
}
