package com.frank.ylear.common.taglib;

import com.frank.ylear.common.util.DateUtils;

public class Functions
{
	public static String getTimestampFull(String timestamp14)
	{
		return DateUtils.getTimestampFull(timestamp14);
	}
	
	public static String replaceFileSep(String filename)
	{
		filename=filename.replace("\\\\","/");
		filename=filename.replace("\\","/");
		return filename;
	}
	
	public static void main(String[] args)
	{
		System.out.println(replaceFileSep("upload\\\\1.txt"));
	}
}
