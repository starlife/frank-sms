package com.tourzj.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils
{
	private static final SimpleDateFormat SDF_FULL = new SimpleDateFormat(
	"yyyy-MM-dd HH:mm:ss");
	
	private static final SimpleDateFormat SDF_14 = new SimpleDateFormat(
	"yyyyMMddHHmmss");
	
	private static final SimpleDateFormat SDF_10 = new SimpleDateFormat(
	"yyMMddHHmm");
	
	
	public static String getCurrentTimeFull()
	{
		return SDF_FULL.format(new Date());
	}
	
	public static String getTimestamp10()
	{
		return SDF_10.format(new Date());
	}
	
	public static String getTimestamp10(long l)
	{
		return SDF_10.format(new Date(l));
	}
	
	public static String getTimestamp14()
	{
		return SDF_14.format(new Date());
	}
	
	public static String getTimestamp14(long l)
	{
		return SDF_14.format(new Date(l));
	}
	
	public static String getTimestamp14(Date d)
	{
		return SDF_14.format(d);
	}
	
	public static boolean isValidTimestamp14(String timestamp)
	{
		try
		{
			SDF_14.parse(timestamp);
		}
		catch (ParseException e)
		{
			// TODO Auto-generated catch block
			//e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public static String getTimestamp14(String time)
	{
		try
		{
			return SDF_14.format(SDF_FULL.parse(time));
		}catch(ParseException e)
		{
			return null;
		}
	}
	public static String getTimestampFull(String time)
	{
		try
		{
			return SDF_FULL.format(SDF_14.parse(time));
		}catch(Exception e)
		{
			return time;
		}
	}
	
}
