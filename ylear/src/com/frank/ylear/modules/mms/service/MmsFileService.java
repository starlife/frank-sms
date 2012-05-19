package com.frank.ylear.modules.mms.service;

import java.io.Serializable;
import java.util.List;

import com.frank.ylear.modules.mms.entity.MmsFile;

public interface MmsFileService
{

	public Serializable add(MmsFile mmsfile);
	
	public MmsFile getMms(Long id);
	
	public List<MmsFile> getRecentMms(int num);
	
}
