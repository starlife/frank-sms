package com.frank.ylear.common.util;

public class StringTool
{
	public static boolean include(String[] contain, String item)
	{
		for(int i=0;i<contain.length;i++)
		{
			if(item.equals(contain[i]))
			{
				return true;
			}
		}
		return false;
	}
	
	public static boolean notInclude(String[] contain, String item)
	{
		return include(contain,item);
	}
}
