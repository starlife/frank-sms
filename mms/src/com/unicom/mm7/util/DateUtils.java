package com.unicom.mm7.util;

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
}
