package com.chinamobile.cmpp2_0.protocol.util;

/**
 *
 * @author Administrator
 */
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
	//public static final String sformat = "yyyy-MM-dd HH:mm:ss";
	//public static final String format1 = "yyyyMMddHHmmss";
	public static final String TIMESTAMP = "MMddHHmmss";

	public static String getTimeStamp() {
		SimpleDateFormat sdf = new SimpleDateFormat(TIMESTAMP);
		return sdf.format(new Date());

	}

	/*public static String getNowString() {
		return getNowString(sformat);
	}*/

	/*public static String getNowString(String fromatte) {
		java.util.Date date = new java.util.Date();
		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat(
				fromatte);
		return formatter.format(date);
	}*/

	/*public static long getTimeLong(String dateString, String format) {
		// yyyyMMddHHmmss
		long time = 0;
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try {
			time = sdf.parse(dateString).getTime();
		} catch (ParseException ex) {
		}
		return time;
	}*/

	/*public static String getTimeString(String dateString, String format,
			String newFormat) {
		SimpleDateFormat oldFmt = new SimpleDateFormat(format);
		SimpleDateFormat newFmt = new SimpleDateFormat(newFormat);
		try {
			Date d = oldFmt.parse(dateString);
			return newFmt.format(d);
		} catch (ParseException ex) {
			return "ParseError " + dateString;
		}
	}*/

	
}
