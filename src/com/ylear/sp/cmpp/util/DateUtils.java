package com.ylear.sp.cmpp.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils
{
	private static final SimpleDateFormat SDF_FULL = new SimpleDateFormat(
	"yyyy-MM-dd HH:mm:ss");
	
	private static final SimpleDateFormat SDF_SHORT = new SimpleDateFormat(
	"yyyyMMddHHmmss");
	
	
	public static String getCurrentTimeFull()
	{
		return SDF_FULL.format(new Date());
	}
	
	public static String getCurrentTimeShort()
	{
		return SDF_SHORT.format(new Date());
	}
}
