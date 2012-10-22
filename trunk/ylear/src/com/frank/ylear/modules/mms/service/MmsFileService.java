package com.frank.ylear.modules.mms.service;

import java.io.Serializable;
import java.util.List;

import com.frank.ylear.common.model.PageBean;
import com.frank.ylear.modules.mms.entity.MmsFile;
import com.frank.ylear.modules.mms.entity.UMms;

public interface MmsFileService
{

	/**
	 *
	 */
	public void getMmsFileList(MmsFile mmsFile, PageBean<MmsFile> pageResult);
	
	/**
	 * ȡ�õ���Ԫ��
	 */	
	public MmsFile getMmsFile(Long id);
	
	/**
	 * ��ŵ���Ʋ�����Ƿ����
	 * @param mmsName
	 * @return
	 */
	public MmsFile getMmsFile(String mmsName);
	/**
	 * 
	 */
	public void delMmsFile(Long id);
	/**
	 * ���
	 */
	public Serializable saveMmsFile(MmsFile mmsFile);
	
}
