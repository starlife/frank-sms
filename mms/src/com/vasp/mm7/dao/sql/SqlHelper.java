package com.vasp.mm7.dao.sql;

public class SqlHelper
{
	
	public static String field(String name,Object value)
	{
		StringBuffer sb=new StringBuffer();
		if(value==null)
		{
			sb.append(name).append("=null");
		}
		else if(value instanceof String)
		{
			sb.append(name).append("='").append((String)value).append("'");
		}else
		{
			sb.append(name).append("=").append(value);
		}
		return sb.toString();
	}
}
