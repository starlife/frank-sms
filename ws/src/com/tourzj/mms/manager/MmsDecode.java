package com.tourzj.mms.manager;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Document;
import org.jdom.Element;

import com.tourzj.common.constant.Constants;
import com.tourzj.common.util.FileUtil;
import com.tourzj.common.util.JDomHelper;
import com.tourzj.common.util.ObjectUtils;
import com.tourzj.common.util.Tools;
import com.tourzj.mms.helper.SmilParser;
import com.unicom.mm7.bean.MmsFile;
import com.unicom.mm7.bean.UMms;
import com.unicom.mm7.bean.UploadFile;

public class MmsDecode {
	private static final Log log = LogFactory.getLog(MmsDecode.class);

	/**
	 * 读取彩信的帧内容
	 * 
	 * @param mobileNewsInfos
	 *            彩信帧节点
	 * @param mmsName
	 *            彩信标题
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public static List<Element> decodeMmsFile(MmsFile mmsFile, String uploadDir) {
		List<Element> mobileNewsInfos = new ArrayList<Element>();
		Map<String, UploadFile> fileNameMap = new HashMap<String, UploadFile>();
		Iterator<UploadFile> it = mmsFile.getUploadFiles().iterator();
		while (it.hasNext()) {
			UploadFile upload = it.next();
			fileNameMap.put(upload.getFilename(), upload);
		}
		SmilParser parser = new SmilParser(mmsFile.getSmildata());
		parser.parse();
		for (int i = 0; i < parser.getFrames().size(); i++) {
			SmilParser.Frame fr = parser.getFrames().get(i);
			Element MobileNewsInfo = new Element("MobileNewsInfo");
			if (fr.getImagesrc() != null) {
				Element ImgFile = new Element("ImgFile");
				String fileName = fr.getImagesrc();
				// 保存图片附件
				UploadFile upload = fileNameMap.get(fileName);
				if (upload != null) {
					String newName=java.util.UUID.randomUUID().toString()+"."+Tools.getFileExt(fileName);
					File f = new File(uploadDir + File.separator + newName);
					FileUtil.saveData(f, upload.getFiledata());
					ImgFile.setText(Constants.UPLOAD_FILE_DIR + "/" + newName);
				} else {
					log.error("uload is null while find " + fileName);
				}
				MobileNewsInfo.addChild(ImgFile);
			}
			if (fr.getTxtsrc() != null) {
				// System.out.println(fr.getTxtsrc());
				Element Content = new Element("Content");
				UploadFile upload = fileNameMap.get(fr.getTxtsrc());
				if (upload != null) {
					String paper = null;
					if (upload.getCharset() != null) {
						paper = new String(upload.getFiledata(),
								Charset.forName(upload.getCharset()));
					} else {
						paper = new String(upload.getFiledata());
					}
					Content.setText(parse(paper));
				} else {
					log.error("uload is null while find " + fr.getTxtsrc());
				}
				MobileNewsInfo.addChild(Content);
			}
			mobileNewsInfos.add(MobileNewsInfo);
		}
		return mobileNewsInfos;

	}

	public static Document decodeMms(UMms mms, String uploadDir) {
		// 从给定的url中读取彩信内容
		Document doc = null;
		try {
			// doc =new Document(root);
			Element root = new Element("MobilePaper");
			// MobileHeaderInfo节点
			Element MobileHeaderInfo = new Element("MobileHeaderInfo");
			Element PapTitle = new Element("PapTitle");
			PapTitle.setText(mms.getSubject());
			MobileHeaderInfo.addChild(PapTitle);
			root.addChild(MobileHeaderInfo);
			// MobileNewsInfo节点
			List<Element> mobileNewsInfos = decodeMmsFile(mms.getMmsFile(),
					uploadDir);
			for (int i = 0; i < mobileNewsInfos.size(); i++) {
				root.addChild(mobileNewsInfos.get(i));
			}
			// MobileSendID节点
			Element MobileSendID = new Element("MobileSendID");
			Element SendID = new Element("SendID");
			SendID.setText(mms.getSendID());
			MobileSendID.addChild(SendID);
			root.addChild(MobileSendID);
			// MobilePhoneNo
			Element MobilePhoneNo = new Element("MobilePhoneNo");
			Element PhoneNumber = new Element("PhoneNumber");
			PhoneNumber.setText(mms.getRecipient());
			MobilePhoneNo.addChild(PhoneNumber);
			root.addChild(MobilePhoneNo);
			doc = new Document(root);

		} catch (Exception ex) {
			log.error("解析彩信时Exception", ex);
			return null;

		} finally {
			;
		}
		return doc;
	}

	public static String parse(String paper) {
		paper = paper.replaceAll(Constants.NEWLINE, "<br/>");
		paper = paper.replaceAll(" ", "&nbsp;");
		return paper;
	}

	public static void main(String[] args) throws Exception {
		String sendid = "34c53ce1-5656-4e78-b16d-93ffe84e9445";
		Object obj = ObjectUtils.readObject(new File("E:/" + sendid));
		String dir = "E:/workspace/.metadata/.plugins/org.eclipse.wst.server.core/tmp1/wtpwebapps/ws/xml";
		// System.out.println(obj);
		if (obj instanceof UMms) {
			UMms mms = (UMms) obj;
			Document doc = decodeMms(mms, dir);
			// System.out.println(doc);
			String str = JDomHelper.doc2String(doc, "UTF-8");
			// str=str.replaceAll("<", "&lt;");
			// str=str.replaceAll(">", "&gt;");
			FileUtil.saveData(new File(dir + "/" + sendid),
					str.getBytes(Charset.forName("UTF-8")));
			// JDomHelper.doc2XML(doc, dir+"/"+sendid,"UTF-8");
			// System.out.println(str);
		}
	}

}
