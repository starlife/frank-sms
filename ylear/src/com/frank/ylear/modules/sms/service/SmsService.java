package com.frank.ylear.modules.sms.service;

import com.frank.ylear.common.model.PageBean;
import com.frank.ylear.modules.sms.entity.USms;

public interface SmsService
{
	
	/**
	 * 查询用户列表 
	 */
	public void getSmsList(USms address, PageBean pageResult);
	
	/**
	 * 取得单个元素
	 */	
	public USms getSms(Long id);
	/**
	 * 删除
	 */
	public void delSms(Long id);
	/**
	 * 添加
	 */
	public void saveSms(USms address);
	
	
	
}
