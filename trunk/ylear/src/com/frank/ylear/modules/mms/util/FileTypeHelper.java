package com.frank.ylear.modules.mms.util;

import com.frank.ylear.common.constant.Constants;


public class FileTypeHelper
{
	public static final String TEXT = "text/plain";
    public static final String XML = "text/xml";
    public static final String SMIL = "application/smil";
    public static final String AMR = "audio/amr";
    public static final String MIDI ="audio/midi";
    public static final String I_MELODY ="text/i-melody";
    public static final String E_MELODY = "text/e-melody";
    public static final String GIF = "image/gif";
    public static final String JPG = "image/jpg";
    public static final String JPEG = "image/jpeg";
    public static final String WJPEG = "image/pjpeg";//IE才有
    public static final String WBMP = "image/vnd.wap.wbmp";
    public static final String PNG = "image/png";
    public static final String WPNG = "image/x-png";//IE才有
    public static final String MULTIPART_RELATED = "multipart/related;start=<START>";
    public static final String MULTIPART_MIXED = "multipart/mixed";
    public static final String WAP_MULTIPART_MIXED = "application/vnd.wap.multipart.mixed";
    public static final String WAP_MULTIPART_RELATED = "application/vnd.wap.multipart.related;start=<START>";
	
    
    
    public static String decodeFileType(String fileType)
	{
		String contentType = null;
		fileType = fileType.toLowerCase().trim();
		if (GIF.indexOf(fileType)!=-1||JPG.indexOf(fileType)!=-1||JPEG.indexOf(fileType)!=-1||PNG.indexOf(fileType)!=-1||WJPEG.indexOf(fileType)!=-1||WPNG.indexOf(fileType)!=-1)
		{
			contentType = Constants.FILE_TYPE_IMAGE;
		}else if(AMR.indexOf(fileType)!=-1||MIDI.indexOf(fileType)!=-1)
		{
			contentType = Constants.FILE_TYPE_AUDIO;
		}else if(TEXT.indexOf(fileType)!=-1||XML.indexOf(fileType)!=-1||"txt".indexOf(fileType)!=-1)
		{
			contentType = Constants.FILE_TYPE_TEXT;
		}else
		{
			contentType=fileType;
		}
		return contentType;
	}
}
