package com.unicom.mm7.util;

import java.util.HashMap;
import java.util.Map;

public class Constants
{
	public static final String NEWLINE=System.getProperty("line.separator");
	
	
	public static void main(String[] args)
	{
		Map<String,String> map = new HashMap<String, String>();
		map.put("1"+"|"+"2","1212");
		System.out.println(map.get("1"+"|"+"2"));
		System.out.println(map.get("1"+"|"+"3"));
	}

}
