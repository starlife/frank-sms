package com.frank.ylear.modules.mms.service.impl;

import java.io.Serializable;

import com.frank.ylear.common.model.PageBean;
import com.frank.ylear.modules.base.service.BaseService;
import com.frank.ylear.modules.mms.entity.UMms;
import com.frank.ylear.modules.mms.service.MmsService;

public class MmsServiceImpl extends BaseService implements MmsService
{

	/**
	 * 
	 */
	public void getMmsList(UMms mms, PageBean<UMms> pageResult)
	{
		String hql = "select obj from UMms obj where 1=1 ";
		if (null != mms)
		{
			if (isItemNotEmpty(mms.getBeginTime()))
			{
				hql += "and obj.sendtime >='" + mms.getBeginTime() + "' ";
			}
			if (isItemNotEmpty(mms.getEndTime()))
			{
				hql += "and obj.sendtime <='" + mms.getEndTime() + "235959' ";
			}
		}
        hql+=" order by obj.id desc";
		baseDao.listByPage(hql, pageResult);
	}

	/**
	 * 
	 */
	public void delMms(Long id)
	{
		baseDao.del(UMms.class, id);
	}

	/**
	 * ���
	 */
	public Serializable saveMms(UMms mms)
	{
		// ��ʱ����д���
		// ���
		return baseDao.add(mms);
	}

	/**
	 * ȡ�õ���Ԫ��
	 */
	public UMms getMms(Long id)
	{
		UMms obj = (UMms) baseDao.get(UMms.class, id);
		return obj;
	}

}
