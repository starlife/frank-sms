package com.cmcc.mm7.vasp.common;

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

	
	public static String getTimeStamp()
	{
		return SDF.format(new Date());

	}

	
	public static String getTimeStamp(long l)
	{
		return SDF.format(new Date(l));

	}

	public static String getTimeString()
	{
		return SDF_STRING.format(new Date());

	}

	
	public static String getTimeString(long l)
	{
		return SDF_STRING.format(new Date(l));

	}


}
