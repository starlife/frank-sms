package com.frank.ylear.modules.phoneaddress.service;

import com.frank.ylear.common.model.PageBean;
import com.frank.ylear.modules.phoneaddress.entity.UPhoneaddress;

public interface AddressService
{
	
	/**
	 * 查询用户列表 
	 */
	public void getPhoneAddressList(UPhoneaddress address, PageBean pageResult);
	
	/**
	 * 取得单个元素
	 */	
	public UPhoneaddress getPhoneAddress(Long id);
	/**
	 * 删除
	 */
	public void delPhoneAddress(Long id);
	
	/**
	 * 保存
	 */	
	public void savePhoneAddress(UPhoneaddress address);
	
	public boolean checkPhoneNumberExist(UPhoneaddress address);
	
	public String getPhoneNumber(UPhoneaddress address);
	
}
