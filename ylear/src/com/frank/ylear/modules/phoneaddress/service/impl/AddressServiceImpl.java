package com.frank.ylear.modules.phoneaddress.service.impl;

import java.util.List;

import com.frank.ylear.common.model.PageBean;
import com.frank.ylear.modules.base.service.BaseService;
import com.frank.ylear.modules.phoneaddress.entity.UPhoneaddress;
import com.frank.ylear.modules.phoneaddress.service.AddressService;


public class AddressServiceImpl extends BaseService implements AddressService
{
	
	/**
	 * 查询用户列表 
	 */
	public void getPhoneAddressList(UPhoneaddress address, PageBean pageResult) {
		String hql = "select obj from UPhoneaddress obj where 1=1 ";
		if (null!=address){
			if (isItemNotEmpty(address.getPhonenumber())){
				hql += "and obj.phonenumber ='"
						+address.getPhonenumber()+"' ";				
			}
			if (isItemNotEmpty(address.getName())){
				hql += "and obj.name ='"
						+address.getName()+"' ";				
			}
			if (isItemNotEmpty(address.getDepartment())){
				hql += "and obj.department ='"
						+address.getDepartment()+"' ";				
			}
			if (isItemNotEmpty(address.getArea())){
				hql += "and obj.area ='"
						+address.getArea()+"' ";				
			}
		}
		baseDao.listByPage(hql,pageResult);
	}
	/**
	 * 删除
	 */
	public void delPhoneAddress(Long id){
		baseDao.del(UPhoneaddress.class, id);
	}
	
	public boolean checkPhoneNumberExist(UPhoneaddress address)
	{
		List list = baseDao.list("select obj from UPhoneaddress obj where obj.phonenumber = '" 
				+ address.getPhonenumber() +"'");
		return !list.isEmpty();
	}
	/**
	 * 更新
	 */
	public void savePhoneAddress(UPhoneaddress address){
		if(address.getId()==null)
		{
			baseDao.add(address);
			
		}else
		{
			baseDao.update(address);
			
		}	
	}
	
	/**
	 * 取得单个元素
	 */	
	public UPhoneaddress getPhoneAddress(Long id){
		UPhoneaddress obj=(UPhoneaddress) baseDao.get(UPhoneaddress.class, id);
		return obj;
	}
	public String getPhoneNumber(UPhoneaddress address)
	{
		// TODO Auto-generated method stub
		StringBuffer phones=new StringBuffer();
		String hql = "select obj.phonenumber from UPhoneaddress obj where 1=1 ";
		if (null!=address){
			if (isItemNotEmpty(address.getPhonenumber())){
				hql += "and obj.phonenumber ='"
						+address.getPhonenumber()+"' ";				
			}
			if (isItemNotEmpty(address.getName())){
				hql += "and obj.name ='"
						+address.getName()+"' ";				
			}
			if (isItemNotEmpty(address.getDepartment())){
				hql += "and obj.department ='"
						+address.getDepartment()+"' ";				
			}
			if (isItemNotEmpty(address.getArea())){
				hql += "and obj.area ='"
						+address.getArea()+"' ";				
			}
		}
		List list=baseDao.list(hql);
		for(int i=0;i<list.size();i++)
		{
			phones.append(list.get(i)+",");
		}
		return phones.toString();
	}
	
}
