package com.chinamobile.cmpp2_0.protocol.util;

/**
 * @author Administrator
 */
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil
{
	public static final SimpleDateFormat SDF = new SimpleDateFormat(
			"yyyyMMddHHmmss");
	public static final SimpleDateFormat SDF_STRING = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss,SSS");

	// public static final String TIMESTAMP = "MMddHHmmss";

	public static String getTimeStamp(String format)
	{
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(new Date());

	}

	/**
	 * 得到yyyyMMddHHmmss类型的字符串
	 * 
	 * @return
	 */
	public static String getTimeStamp()
	{
		return SDF.format(new Date());

	}

	/**
	 * 得到yyyyMMddHHmmss类型的字符串
	 * 
	 * @return
	 */
	public static String getTimeStamp(long l)
	{
		return SDF.format(new Date(l));

	}

	public static String getTimeString()
	{
		return SDF_STRING.format(new Date());

	}

	/**
	 * 得到yyyy-MM-dd HH:mm:ss类型的字符串
	 * 
	 * @return
	 */
	public static String getTimeString(long l)
	{
		return SDF_STRING.format(new Date(l));

	}

	/*
	 * public static long getTimeLong(String dateString, String format) { //
	 * yyyyMMddHHmmss long time = 0; SimpleDateFormat sdf = new
	 * SimpleDateFormat(format); try { time = sdf.parse(dateString).getTime(); }
	 * catch (ParseException ex) { } return time; }
	 */

	/*
	 * public static String getTimeString(String dateString, String format,
	 * String newFormat) { SimpleDateFormat oldFmt = new
	 * SimpleDateFormat(format); SimpleDateFormat newFmt = new
	 * SimpleDateFormat(newFormat); try { Date d = oldFmt.parse(dateString);
	 * return newFmt.format(d); } catch (ParseException ex) { return "ParseError " +
	 * dateString; } }
	 */

}
