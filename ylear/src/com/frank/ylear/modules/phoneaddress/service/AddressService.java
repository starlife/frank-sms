package com.frank.ylear.modules.phoneaddress.service;

import com.frank.ylear.common.model.PageBean;
import com.frank.ylear.modules.phoneaddress.entity.UPhoneaddress;

public interface AddressService
{
	
	/**
	 * ��ѯ�û��б� 
	 */
	public void getPhoneAddressList(UPhoneaddress address, PageBean pageResult);
	
	/**
	 * ȡ�õ���Ԫ��
	 */	
	public UPhoneaddress getPhoneAddress(Long id);
	/**
	 * ɾ��
	 */
	public void delPhoneAddress(Long id);
	
	/**
	 * ����
	 */	
	public void savePhoneAddress(UPhoneaddress address);
	
	public boolean checkPhoneNumberExist(UPhoneaddress address);
	
	public String getPhoneNumber(UPhoneaddress address);
	
}
