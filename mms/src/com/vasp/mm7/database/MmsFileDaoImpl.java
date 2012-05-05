package com.vasp.mm7.database;

import com.vasp.mm7.database.pojo.MmsFile;

public class MmsFileDaoImpl extends DBDaoImpl
{
	//private static final Log log = LogFactory.getLog(MmsFileDaoImpl.class);

	private static MmsFileDaoImpl dao = new MmsFileDaoImpl();

	private MmsFileDaoImpl()
	{

	}

	public static MmsFileDaoImpl getInstance()
	{
		return dao;
	}

	public MmsFile getMmsFile(Long id)
	{
		return (MmsFile) this.get(MmsFile.class, id);
	}
	
	

}
