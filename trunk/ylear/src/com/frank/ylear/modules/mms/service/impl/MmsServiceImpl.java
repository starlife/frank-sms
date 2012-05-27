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
	 * 删除
	 */
	public void delMms(Long id)
	{
		baseDao.del(UMms.class, id);
	}

	/**
	 * 添加
	 */
	public Serializable saveMms(UMms mms)
	{
		// 对时间进行处理
		// 添加
		return baseDao.add(mms);
	}

	/**
	 * 取得单个元素
	 */
	public UMms getMms(Long id)
	{
		UMms obj = (UMms) baseDao.get(UMms.class, id);
		return obj;
	}

}
