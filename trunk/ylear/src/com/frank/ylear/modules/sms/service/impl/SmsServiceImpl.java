package com.frank.ylear.modules.sms.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.frank.ylear.common.model.PageBean;
import com.frank.ylear.modules.base.service.BaseService;
import com.frank.ylear.modules.sms.entity.USms;
import com.frank.ylear.modules.sms.service.SmsService;


public class SmsServiceImpl extends BaseService implements SmsService
{
	
	/**
	 * ��ѯ�û��б� 
	 */
	public void getSmsList(USms sms, PageBean pageResult) {
		String hql = "select obj from USms obj where 1=1 ";
		if (null!=sms){
			if (isItemNotEmpty(sms.getBeginTime())){
				hql += "and obj.sendtime >='"
						+sms.getBeginTime()+"' ";				
			}
			if (isItemNotEmpty(sms.getEndTime())){
				hql += "and obj.sendtime <='"
						+sms.getEndTime()+"' ";				
			}
		}
		hql+=" order by obj.id desc";
		baseDao.listByPage(hql,pageResult);
	}
	/**
	 * ɾ��
	 */
	public void delSms(Long id){
		baseDao.del(USms.class, id);
	}
	/**
	 * ���
	 */
	public void saveSms(USms sms){
		if(sms.getId()==null)
		{
			baseDao.add(sms);
		}else
		{
			baseDao.update(sms);
		}
			
		
	}
	/**
	 * ȡ�õ���Ԫ��
	 */	
	public USms getSms(Long id){
		USms obj=(USms) baseDao.get(USms.class, id);
		return obj;
	}
	

}
