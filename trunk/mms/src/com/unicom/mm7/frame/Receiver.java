package com.unicom.mm7.frame;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cmcc.mm7.vasp.MM7Receiver;
import com.cmcc.mm7.vasp.common.MMConstants;
import com.cmcc.mm7.vasp.common.MMContent;
import com.cmcc.mm7.vasp.protocol.message.MM7DeliverReq;
import com.cmcc.mm7.vasp.protocol.message.MM7DeliverRes;
import com.cmcc.mm7.vasp.protocol.message.MM7DeliveryReportReq;
import com.cmcc.mm7.vasp.protocol.message.MM7DeliveryReportRes;
import com.cmcc.mm7.vasp.protocol.message.MM7ReadReplyReq;
import com.cmcc.mm7.vasp.protocol.message.MM7ReadReplyRes;
import com.cmcc.mm7.vasp.protocol.message.MM7VASPRes;
import com.unicom.mm7.bean.MmsFile;
import com.unicom.mm7.bean.UMms;
import com.unicom.mm7.bean.UploadFile;
import com.unicom.mm7.conf.MM7Config;
import com.unicom.mm7.util.DateUtils;
import com.unicom.mm7.util.SmilParser;

public class Receiver extends MM7Receiver
{

	private static final Log log = LogFactory.getLog(Receiver.class);

	public Receiver(MM7Config config)
	{
		super(config.getListenIP(), config.getListenPort(),
				config.getBackLog(), config.isKeepAlive(), config.getTimeOut(),
				config.getCharSet());
	}

	/**
	 * 重写deliver消息
	 */
	@Override
	public MM7VASPRes doDeliver(MM7DeliverReq request)
	{
		log.info("收到手机" + request.getSender() + "提交的消息，标题为："
				+ request.getSubject());
		log.info("MMSC的标识符为：" + request.getMMSRelayServerID());

		dealDeliver(request);

		// 回复消息
		MM7DeliverRes mm7DeliverRes = new MM7DeliverRes();
		mm7DeliverRes.setTransactionID(request.getTransactionID());
		mm7DeliverRes.setServiceCode("产品代码"); // 设置ServiceCode，可选
		mm7DeliverRes.setStatusCode(MMConstants.RequestStatus.SUCCESS);
		mm7DeliverRes.setStatusText(MMConstants.RequestStatus.TEXT_SUCCESS);
		return mm7DeliverRes;
	}

	/**
	 * 重写deliver_report消息
	 */
	@Override
	public MM7VASPRes doDeliveryReport(MM7DeliveryReportReq mm7DeliveryReportReq)
	{
		// 这里写接收状态报告后的处理过程
		dealDeliveryReport(mm7DeliveryReportReq);

		// 回复消息
		MM7DeliveryReportRes res = new MM7DeliveryReportRes();
		res.setTransactionID(mm7DeliveryReportReq.getTransactionID());
		res.setStatusCode(MMConstants.RequestStatus.SUCCESS);
		res.setStatusText(MMConstants.RequestStatus.TEXT_SUCCESS);
		return res;
	}

	/**
	 * 重写ReadReply消息
	 */
	@Override
	public MM7VASPRes doReadReply(MM7ReadReplyReq mm7ReadReplyReq)
	{
		// 这里写接收读报告后的处理过程
		dealReadReply(mm7ReadReplyReq);

		// 回复消息
		MM7ReadReplyRes res = new MM7ReadReplyRes();
		res.setTransactionID(mm7ReadReplyReq.getTransactionID());
		res.setStatusCode(MMConstants.RequestStatus.SUCCESS);
		res.setStatusText(MMConstants.RequestStatus.TEXT_SUCCESS);
		return res;
	}

	/**
	 * 组装UMms对象
	 * 
	 * @param mmsFile
	 * @param deliverReq
	 * @return
	 */
	public static UMms makeMms(MmsFile mmsFile, MM7DeliverReq deliverReq)
	{
		UMms mms = new UMms();
		mms.setMmsFile(mmsFile);
		mms.setRecipient(deliverReq.getSender());//这里是发送号码 不是接收号码
		mms.setSubject(deliverReq.getSubject());
		mms.setSendtime(DateUtils.getTimestamp14());
		// 随机分配sendid
		mms.setSendID(java.util.UUID.randomUUID().toString());
		return mms;
	}

	/**
	 * 组装MmsFile对象
	 * 
	 * @param deliverReq
	 * @return
	 */
	public static MmsFile makeMmsFile(MM7DeliverReq deliverReq)
	{
		MmsFile mmsFile = new MmsFile();
		if (deliverReq.getContent() != null)
		{
			// 用来编辑文件名和Upload对象的对应关系
			Map<String, UploadFile> fileNameMap = new HashMap<String, UploadFile>();
			long uploadFileSize = 0;// 用来记录各个附件的大小
			List<MMContent> list = deliverReq.getContent().getSubContents();
			for (int i = 0; i < list.size(); i++)
			{
				MMContent content = list.get(i);
				if (content.getContentType().equals(
						MMConstants.ContentType.TEXT)
						|| content.getContentType().equals(
								MMConstants.ContentType.XML)
						|| content.getContentType().equals(
								MMConstants.ContentType.AMR)
						|| content.getContentType().equals(
								MMConstants.ContentType.MIDI)
						|| content.getContentType().equals(
								MMConstants.ContentType.JPEG)
						|| content.getContentType().equals(
								MMConstants.ContentType.PNG)
						|| content.getContentType().equals(
								MMConstants.ContentType.GIF)

				)
				{
					UploadFile upload = new UploadFile();
					upload.setFiledata(content.getContent());
					upload.setCharset(content.getCharset());
					upload.setFilename(content.getContentLocation());
					upload.setFilesize((long) content.getContent().length);
					upload.setFiletype(content.getContentType().toString());
					fileNameMap.put(upload.getFilename(), upload);
					uploadFileSize += upload.getFilesize();
					mmsFile.addUploadFile(upload);
				}
			}
			// smil消息最后解析
			for (int i = 0; i < list.size(); i++)
			{
				MMContent content = list.get(i);
				if (content.getContentType().equals(
						MMConstants.ContentType.SMIL))
				{
					// 解析SMIL
					String smil = content.getContentAsString();
					SmilParser parser = new SmilParser(smil);
					parser.parse();// 解析
					for (int j = 0; j < parser.getFrames().size(); j++)
					{
						SmilParser.Frame fr = parser.getFrames().get(j);
						if (fr.getImagesrc() != null)
						{
							String fileName = fr.getImagesrc();
							UploadFile upload = fileNameMap.get(fileName);
							if (upload != null)
							{
								upload.setFrameid(fr.getFramenumber());
							}
						}
						if (fr.getTxtsrc() != null)
						{
							String fileName = fr.getTxtsrc();
							UploadFile upload = fileNameMap.get(fileName);
							if (upload != null)
							{
								upload.setFrameid(fr.getFramenumber());
							}
						}
						if (fr.getAudiosrc() != null)
						{
							String fileName = fr.getAudiosrc();
							UploadFile upload = fileNameMap.get(fileName);
							if (upload != null)
							{
								upload.setFrameid(fr.getFramenumber());
							}
						}
					}
					mmsFile.setFrames(parser.getFrames().size());
					mmsFile.setSmilname(content.getContentLocation());
					mmsFile.setSmildata(content.getContentAsString());
					mmsFile.setSmilsize((long) content.getContent().length);
					mmsFile.setMmsSize(mmsFile.getSmilsize() + uploadFileSize);
					break;
				}
			}
		}
		mmsFile.setMmsName(deliverReq.getSubject());// 设置彩信标题
		return mmsFile;
	}

	private void dealDeliver(MM7DeliverReq deliverReq)
	{
		MmsFile mmsFile = makeMmsFile(deliverReq);
		UMms mms = makeMms(mmsFile, deliverReq);
		Result.getInstance().notifyResult(mms);
		// log.info(deliverReq);
	}

	private void dealDeliveryReport(MM7DeliveryReportReq deliverReportReq)
	{
		log.info("收到发送报告");

		String messageid = deliverReportReq.getMessageID();
		String to = deliverReportReq.getRecipient();
		if (to.startsWith("+86"))
		{
			to = to.substring(3, to.length());
		}
		else if (to.startsWith("86"))
		{
			to = to.substring(2, to.length());
		}
		// String transcationid=deliverReportReq.getTransactionID();
		String reportTime = DateUtils.getTimestamp14(deliverReportReq
				.getTimeStamp());
		Integer mmStatus = (int) deliverReportReq.getMMStatus();
		String mmStatusText = deliverReportReq.getStatusText();

		String sendid = null;
		synchronized (Sender.messageidMap)
		{
			sendid = Sender.messageidMap.remove(messageid + "|" + to);

		}
		// 状态报告通知
		Result.getInstance().notifyResult(sendid, to, mmStatus, mmStatusText,
				reportTime);

	}

	private void dealReadReply(MM7ReadReplyReq mm7ReadReplyReq)
	{
		// 这里更新s_log_mmssubmit表
		/*
		 * String messageid=mm7ReadReplyReq.getMessageID();
		 * if(!Tools.isBlank(messageid)) { SubmitBean
		 * bean=dao.getSubmitBeanByMessageid(messageid); if(bean!=null) {
		 * bean.setReadyreplyTime(mm7ReadReplyReq.getTimeStamp());
		 * bean.setReadstatus((int)mm7ReadReplyReq.getMMStatus());
		 * bean.setReadstatustext(mm7ReadReplyReq.getStatusText());
		 * dao.update(bean); } }
		 */
		// 这里插入记录到s_log_mmsreadreply
	}

	// Main方法
	public static void main(String[] args) throws Exception
	{
		// 初始化VASP
		MM7Config mm7Config = new MM7Config("./config/mm7Config.xml");
		// 构造MyReceiver
		Receiver receiver = new Receiver(mm7Config);
		// 启动接收器
		receiver.start();
		for (;;)
			;
	}

}
