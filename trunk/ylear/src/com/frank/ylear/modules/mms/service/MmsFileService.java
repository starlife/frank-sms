package com.frank.ylear.modules.mms.service;

import java.io.Serializable;
import java.util.List;

import com.frank.ylear.common.model.PageBean;
import com.frank.ylear.modules.mms.entity.MmsFile;
import com.frank.ylear.modules.mms.entity.UMms;

public interface MmsFileService
{

	/**
	 * 查询用户列表 
	 */
	public void getMmsFileList(MmsFile mmsFile, PageBean<MmsFile> pageResult);
	
	/**
	 * 取得单个元素
	 */	
	public MmsFile getMmsFile(Long id);
	/**
	 * 删除
	 */
	public void delMmsFile(Long id);
	/**
	 * 添加
	 */
	public Serializable saveMmsFile(MmsFile mmsFile);
	
}
