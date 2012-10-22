package com.frank.ylear.modules.phoneaddress.service.impl;

import java.util.List;

import com.frank.ylear.common.model.PageBean;
import com.frank.ylear.modules.base.service.BaseService;
import com.frank.ylear.modules.phoneaddress.entity.UPhoneaddress;
import com.frank.ylear.modules.phoneaddress.service.AddressService;
import com.frank.ylear.modules.unitInfo.entity.TPosition;


public class AddressServiceImpl extends BaseService implements AddressService
{
	
	/**
	 * 查询用户列表 
	 */
	public void getPhoneAddressList(UPhoneaddress address, PageBean pageResult) {
		StringBuffer hql = new StringBuffer("select obj from UPhoneaddress obj where 1=1 ");
		if (null!=address){
			if (isItemNotEmpty(address.getPhonenumber())){
				hql.append("and obj.phonenumber ='"
						+address.getPhonenumber()+"' ");				
			}
			if (isItemNotEmpty(address.getName())){
				hql.append("and obj.name ='"
						+address.getName()+"' ");				
			}
			if (isItemNotEmpty(address.getDepartment())){
				hql.append("and obj.department ='"
						+address.getDepartment()+"' ");				
			}
			if (isItemNotEmpty(address.getArea())){
				hql.append("and obj.area ='"
						+address.getArea()+"' ");				
			}
			if (isItemNotEmpty(address.getPost())){
				hql.append("and obj.post ='"
						+address.getPost()+"' ");				
			}
			
			//拼接单位，部门
			TPosition position = address.getTPosition();
			if(position != null){
				//部门
				if (isItemNotEmpty(position.getUnitname())){
					hql.append("and obj.TPosition.unitname ='"
						+position.getUnitname()+"' ");				
				}
				
				//单位
				TPosition parentPosition = position.getTPositionParent();
				if(isItemNotEmpty(parentPosition.getUnitname())){
					hql.append("and obj.TPosition.TPositionParent.unitname ='"
							+parentPosition.getUnitname()+"' ");	
				}
			}
			
			if (isItemNotEmpty(address.getHomenumber())){
				hql.append("and obj.homenumber ='"
						+address.getHomenumber()+"' ");				
			}
		}
		baseDao.listByPage(hql.toString(),pageResult);
	}
	
	/**
	 * 根据单位id查询用户列表 
	 */
	public List<UPhoneaddress> findAddressListByPosition(Integer position){
		StringBuffer hql = new StringBuffer(" from  UPhoneaddress a where 1=1 ");
		if(position != null){
			hql.append(" and a.TPosition.unitid = ").append(position);
		}
		
		//只过滤正常的数据
		//hql.append(" and a.delFlag = 0 ");
	
		return baseDao.list(hql.toString());
	}
	
	/**
	 * 查询单位列表 
	 */
	public List<TPosition> findAddressPosition() {
		return baseDao.list(" from TPosition t ");
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
	
	/**
	 * 根据属性值查询联系人列表
	 * @param property		属性名
	 * @param propertyValue 属性值(整型)
	 * @return
	 */
	public List<UPhoneaddress> findAddressListByProperty(String property,
			Object propertyValue) {
		StringBuffer hql = new StringBuffer("from UPhoneaddress u where ").append(property).append(" = ").append(propertyValue);
		return baseDao.list(hql.toString());
	}
	
}
