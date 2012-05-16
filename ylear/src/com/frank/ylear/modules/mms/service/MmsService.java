package com.frank.ylear.modules.mms.service;

import java.io.Serializable;

import com.frank.ylear.common.model.PageBean;
import com.frank.ylear.modules.mms.entity.UMms;

public interface MmsService
{
	
	/**
	 * 查询用户列表 
	 */
	public void getMmsList(UMms mms, PageBean<UMms> pageResult);
	
	/**
	 * 取得单个元素
	 */	
	public UMms getMms(Long id);
	/**
	 * 删除
	 */
	public void delMms(Long id);
	/**
	 * 添加
	 */
	public Serializable saveMms(UMms mms);
	
	

}
