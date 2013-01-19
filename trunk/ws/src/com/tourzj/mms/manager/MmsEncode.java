package com.tourzj.mms.manager;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.DOMBuilder;

import com.tourzj.common.constant.Constants;
import com.tourzj.common.util.DateUtils;
import com.tourzj.common.util.FileUtil;
import com.tourzj.common.util.ObjectUtils;
import com.tourzj.mms.helper.FileTypeHelper;
import com.tourzj.mms.helper.Smil;
import com.unicom.mm7.bean.MmsFile;
import com.unicom.mm7.bean.UMms;
import com.unicom.mm7.bean.UploadFile;

public class MmsEncode {
	private static final Log log = LogFactory.getLog(MmsEncode.class);

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
	public static MmsFile makeMmsFile(List<Element> mobileNewsInfos,
			String mmsName) throws MalformedURLException, IOException {
		MmsFile mmsFile = new MmsFile();
		int frames = mobileNewsInfos.size();
		long mmsSize = 0;
		long smilSize = 0;

		Smil smil = new Smil();
		smil.smilAddHead();// 并添加文件头部信息

		for (int i = 0; i < mobileNewsInfos.size(); i++) {
			Element MobileNewsInfo = mobileNewsInfos.get(i);
			int frameid = i + 1;

			smil.setSmilParStart(Constants.FRAME_DURING);// 彩信一帧开始标记

			Element ImgFile = MobileNewsInfo.getChild("ImgFile");
			Element Content = MobileNewsInfo.getChild("Content");
			if (ImgFile != null) {
				String imageFile = ImgFile.getTextTrim();
				// 读取文件内容
				byte[] data = FileUtil
						.readData(new URL(imageFile).openStream());
				UploadFile uploadFile = new UploadFile();
				uploadFile.setFilename(new File(imageFile).getName());// 设置文件名称
				uploadFile.setFiledata(data);// 设置文件内容
				uploadFile.setFilesize((long) data.length);// 设置文件大小
				uploadFile.setFiletype(FileTypeHelper.JPEG);// 设置文件类型
				uploadFile.setFrameid(frameid);// 设置帧号
				mmsFile.getUploadFiles().add(uploadFile);

				smil.smilAddImg(uploadFile.getFilename());
				mmsSize += uploadFile.getFilesize();
			}
			if (Content != null) {
				String text = Content.getTextTrim();
				// 这里做处理文本信息
				text = parse(text);
				// 读取文件内容
				byte[] data = text.getBytes(Constants.CHARSET);

				UploadFile uploadFile = new UploadFile();
				uploadFile.setFilename(frameid + ".txt");// 设置文件名
				uploadFile.setFiledata(data);// 设置文件内容
				uploadFile.setFilesize((long) data.length);// 设置文件大小
				uploadFile.setCharset(Constants.CHARSET);
				uploadFile.setFrameid(frameid);// 设置帧号
				uploadFile.setFiletype(FileTypeHelper.TEXT);
				mmsFile.getUploadFiles().add(uploadFile);

				smil.smilAddText(uploadFile.getFilename());
				mmsSize += uploadFile.getFilesize();

			}
			smil.setSmilParEnd();// 彩信一帧结束标记

		}

		smil.smilAddFoot();
		String smilStr = smil.getSmil();// 创建smil

		smilSize = smilStr.getBytes(Constants.CHARSET).length;

		mmsFile.setMmsName(mmsName);
		mmsFile.setFrames(frames);
		mmsFile.setSmildata(smilStr);
		mmsFile.setSmilname(Constants.SMIL_NAME);
		mmsFile.setSmilsize(smilSize);
		mmsFile.setMmsSize(smilSize + mmsSize);
		// mmsFile.setCreatetime(DateUtils.getTimestamp14());

		return mmsFile;

	}

	public static UMms makeMms(String url) {
		// 从给定的url中读取彩信内容
		UMms mms = new UMms();

		String mmsName = null;

		String sendID;
		try {
			DOMBuilder domb = new DOMBuilder();
			Document doc = domb.build(new URL(url));
			Element root = doc.getRootElement();
			// MobileHeaderInfo节点
			Element MobileHeaderInfo = root.getChild("MobileHeaderInfo");
			Element PapTitle = MobileHeaderInfo.getChild("PapTitle");
			mmsName = PapTitle.getTextTrim();
			if (mmsName.equals("")) {
				log.error("读取手机报时缺少彩信标题");
				return null;
			}
			// MobileNewsInfo节点
			List<Element> mobileNewsInfos = root.getChildren("MobileNewsInfo");
			if (mobileNewsInfos == null || mobileNewsInfos.size() == 0) {
				log.error("读取手机报时缺少彩信内容");
				return null;
			}
			// MobileSendID节点
			Element MobileSendID = root.getChild("MobileSendID");
			Element SendID = MobileSendID.getChild("SendID");
			sendID = SendID.getTextTrim();
			if (sendID.equals("")) {
				log.error("读取手机报时缺少SendID节点");
				return null;
			}
			// MobilePhoneNo
			 Element MobilePhoneNo = root.getChild("MobilePhoneNo");
			 Element PhoneNoUrl = MobilePhoneNo.getChild("PhoneNoUrl");
			 String phoneNoUrl = PhoneNoUrl.getTextTrim();
			 if (phoneNoUrl.equals("")) {
				 log.error("读取手机报时缺少PhoneNoUrl节点");
				 return null;
			 }
			// 电话号码
			String recipient = FileUtil.getData(
			new URL(phoneNoUrl).openStream(), Constants.CHARSET);
			//recipient = "13003664740\r\n";
			// 设置mms
			/**
			 * private MmsFile mmsFile; private String sendID; private String
			 * subject; private String recipient; private String sendtime;
			 */
			MmsFile mmsFile = makeMmsFile(mobileNewsInfos, mmsName);
			mms.setMmsFile(mmsFile);
			mms.setSubject(mmsName);
			mms.setRecipient(recipient);
			mms.setSendID(sendID);
			mms.setSendtime(DateUtils.getTimestamp14());

		} catch (Exception ex) {
			log.error("解析彩信时Exception", ex);
			return null;

		} finally {
			;
		}
		return mms;
	}

	public static String parse(String paper) {
		paper = paper.replaceAll("<br/>", Constants.NEWLINE);
		paper = paper.replaceAll("&nbsp;", " ");
		return paper;
	}

	public static void main(String[] args) throws IOException {
		String sendid = "34c53ce1-5656-4e78-b16d-93ffe84e9445";
		 //String url =
		 //"http://interface.tourzj.gov.cn/MobilePaper/default.aspx?sendid="
		 //+ sendid;
		String url = "http://localhost/ws/index.do?sendid=34c53ce1-5656-4e78-b16d-93ffe84e9445";
		UMms mms = MmsEncode.makeMms(url);
		ObjectUtils.writeObject(new File("E:/google_svn/frank-sms/trunk/mms/MO/" + sendid), mms);

	}

}
