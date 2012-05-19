package com.frank.ylear.modules.mms.service.impl;

import java.io.Serializable;
import java.util.List;

import com.frank.ylear.common.model.PageBean;
import com.frank.ylear.modules.base.service.BaseService;
import com.frank.ylear.modules.mms.entity.MmsFile;
import com.frank.ylear.modules.mms.service.MmsFileService;

public class MmsFileServiceImpl extends BaseService  implements MmsFileService
{
	public MmsFile getMms(Long id)
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
	
	/**
	 * 添加
	 */
	public Serializable add(MmsFile mmsfile){
		//对时间进行处理
		
		//添加	
		return baseDao.add(mmsfile);		
	}

	public List<MmsFile> getRecentMms(int num)
	{
		// TODO Auto-generated method stub
		String sql="from MmsFile";
		return baseDao.listByPage(sql,0,num);
	}
	
	/**
	 * 
	 */
	public void getMmsFileList(PageBean<MmsFile> pageResult)
	{
		String hql = "from MmsFile order by id desc";
		baseDao.listByPage(hql, pageResult);
	}


	
}
