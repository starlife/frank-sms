package com.frank.ylear.modules.base.service;

import com.frank.ylear.modules.base.dao.BaseDao;


public abstract class BaseService {
	protected BaseDao baseDao = null;

	public BaseDao getBaseDao() {
		return baseDao;
	}

	public void setBaseDao(BaseDao commonDAO) {
		this.baseDao = commonDAO;
	}
	
	public static boolean isItemNotEmpty(Object obj){
		if (null==obj){
			return false;
		}
		if(obj instanceof String)
		{
			String str=(String)obj;
			if(str.trim().equals(""))
			{
				return false;
			}
		}
		return true;
	} 
	
	/*public static String BuildDateClause(String dateString,String fieldName){
		String ret = "";
		if (null==dateString){
			return ret;
		}
		String[] arr = dateString.split("-");
		for(String s : arr){
			int i = parseInt(s);
			if (-1!=i){
				ret += "and " + fieldName + " like '%" + i + "%' ";
			}
		}
		return ret;
	}
	public static int parseInt(String s){
		int i = -1;
		try{
			i = Integer.parseInt(s);
		}catch(NumberFormatException e){
			e.printStackTrace();
		}
		return i;
	}*/
}
