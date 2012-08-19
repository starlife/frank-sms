package com.frank.ylear.modules.mms.service.impl;

import java.io.Serializable;
import java.util.List;

import com.frank.ylear.common.model.PageBean;
import com.frank.ylear.modules.base.service.BaseService;
import com.frank.ylear.modules.mms.entity.MmsFile;
import com.frank.ylear.modules.mms.service.MmsFileService;

public class MmsFileServiceImpl extends BaseService  implements MmsFileService
{
	public MmsFile getMmsFile(Long id)
	{
		MmsFile obj=null;
		String hql="select obj from MmsFile obj inner join fetch obj.uploadFiles where obj.id="+id;
		List<MmsFile> list=baseDao.list(hql);
		if(list.size()>0)
		{
			obj= list.get(0);
		}
		return obj;
	}
	
	public MmsFile getMmsFile(String mmsName)
	{
		MmsFile obj=null;
		String hql="from MmsFile obj where obj.mmsName='"+mmsName+"'";
		List<MmsFile> list=baseDao.list(hql);
		if(list.size()>0)
		{
			obj= list.get(0);
		}
		return obj;
	}
	
	/**
	 * 添加
	 */
	public Serializable saveMmsFile(MmsFile mmsfile){
		//对时间进行处理
		//添加	
		if(mmsfile.getId()!=null)
		{
			baseDao.update(mmsfile);
			return null;
		}
		return baseDao.add(mmsfile);		
	}

	
	public void delMmsFile(Long id)
	{
		// TODO Auto-generated method stub
		try
		{
			String hql="delete UMms where mmsid="+id;
			baseDao.execute(hql);
			baseDao.del(MmsFile.class, id);
		}catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
	}

	public void getMmsFileList(MmsFile mmsFile, PageBean<MmsFile> pageResult)
	{
		// TODO Auto-generated method stub
		String hql = "select obj from MmsFile obj where 1=1 ";
		if (null != mmsFile)
		{
			if (isItemNotEmpty(mmsFile.getBeginTime()))
			{
				hql += "and obj.createtime >='" + mmsFile.getBeginTime() + "' ";
			}
			if (isItemNotEmpty(mmsFile.getEndTime()))
			{
				hql += "and obj.createtime <='" + mmsFile.getEndTime() + "235959' ";
			}
		}
        hql+=" order by obj.id desc";
		baseDao.listByPage(hql, pageResult);
		
	}


	
}
