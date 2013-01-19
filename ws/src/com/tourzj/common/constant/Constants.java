package com.tourzj.common.constant;

public class Constants
{
	
	/**
	 * 全局字符串编码类型UTF-8
	 */
	public static final String CHARSET="UTF-8";
	
	/**
	 * 换行符
	 */
	public static final String NEWLINE = System.getProperty("line.separator");
	
	/**
	 * webservice接口返回成功常量
	 */
	public static final int SUCCESS = 0;
	
	/**
	 * webservice接口返回失败常量
	 */
	public static final int ERROR = -1;
	
	


	/**
	 * 上传的文件保存temp目录
	 */
	public static final String UPLOAD_FILE_DIR = "uploadFile";
	
	/**
	 * xml文件保存目录
	 */
	public static final String XML_FILE_DIR = "xml";

	public static final String SMIL_NAME = "smil.xml";
	/**
	 * 彩信文件类型 Text
	 */
	public static final String FILE_TYPE_TEXT = "text";

	public static final String FILE_TYPE_IMAGE = "image";

	public static final String FILE_TYPE_AUDIO = "audio";
	
	// public static final SimpleDateFormat SDF=new SimpleDateFormat("yyyy-MM-dd
	// hh:mm:ss");

	public static final int FRAME_DURING = 20;

	public static final String SMS_SUBMIT_SUCCESS = "DELIVRD";


}
