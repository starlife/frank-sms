package com.frank.ylear.modules.phoneaddress.service;

import java.util.List;

import com.frank.ylear.common.model.PageBean;
import com.frank.ylear.modules.phoneaddress.entity.UPhoneaddress;
import com.frank.ylear.modules.unitInfo.entity.TPosition;

public interface AddressService
{
	
	/**
	 * 查询用户列表 
	 */
	public void getPhoneAddressList(UPhoneaddress address, PageBean pageResult);
	
	/**
	 * 根据单位id查询用户列表 
	 */
	public List<UPhoneaddress> findAddressListByPosition(Integer position);
	
	/**
	 * 查询单位列表 
	 */
	public List<TPosition> findAddressPosition();
	
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
	
	
	/**
	 * 根据属性值查询联系人列表
	 * @param property		属性名
	 * @param propertyValue 属性值(整型)
	 * @return
	 */
	public List<UPhoneaddress> findAddressListByProperty(String property, Object propertyValue);
}
