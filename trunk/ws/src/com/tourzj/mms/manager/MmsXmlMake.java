package com.tourzj.mms.manager;

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Document;

import com.tourzj.common.constant.Constants;
import com.tourzj.common.util.JDomHelper;
import com.unicom.mm7.bean.UMms;

public class MmsXmlMake {

	private static final Log log = LogFactory.getLog(MmsXmlMake.class);
	
	public static boolean make(UMms mms, String uploadDir, String xmlDir, String sendid) {
		// TODO Auto-generated method stub
		checkUploadDir(uploadDir);
		checkXmlDir(xmlDir);
		Document doc = MmsDecode.decodeMms(mms, uploadDir);
		try {
			JDomHelper.doc2XML(doc,
					new File(xmlDir + "/" + sendid).getAbsolutePath(),
					Constants.CHARSET);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error(null, e);
			return false;
		}
	}

	public static void checkUploadDir(String uploadDir) {
		File dir = new File(uploadDir);
		if (!dir.exists()) {
			dir.mkdirs();
		}
	}

	
	public static void checkXmlDir(String xmlDir) {
		File dir = new File(xmlDir);
		if (!dir.exists()) {
			dir.mkdirs();
		}
	}

}
