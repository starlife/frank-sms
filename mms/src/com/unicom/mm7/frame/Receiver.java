package com.unicom.mm7.frame;

import java.io.File;
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
import com.cmcc.mm7.vasp.protocol.util.Hex;
import com.unicom.mm7.bean.MmsFile;
import com.unicom.mm7.bean.UMms;
import com.unicom.mm7.bean.UploadFile;
import com.unicom.mm7.conf.MM7Config;
import com.unicom.mm7.util.DateUtils;
import com.unicom.mm7.util.FileUtil;
import com.unicom.mm7.util.SmilParser;

public class Receiver extends MM7Receiver
{

	private static final Log log = LogFactory.getLog(Receiver.class);

	// private static final Log db = LogFactory.getLog("db");

	// private SubmitDaoImpl submitDao = SubmitDaoImpl.getInstance();
	/**
	 * �����������messageid��sendid�Ķ�Ӧ��ϵ
	 */
	static final Map<String, String> messageidMap = new HashMap<String, String>();
	
	//public static final LinkedBlockingQueue<UMms> mmsQue = new LinkedBlockingQueue<UMms>();

	/**
	 * ���ݿ���ʶ���
	 */
	// private SubmitDaoImpl1 dao = SubmitDaoImpl1.getInstance();
	public Receiver(MM7Config config)
	{
		super(config.getListenIP(), config.getListenPort(),
				config.getBackLog(), config.isKeepAlive(), config.getTimeOut(),
				config.getCharSet());
	}

	/**
	 * ��дdeliver��Ϣ
	 */
	@Override
	public MM7VASPRes doDeliver(MM7DeliverReq request)
	{
		log.info("�յ��ֻ�" + request.getSender() + "�ύ����Ϣ������Ϊ��"
				+ request.getSubject());
		log.info("MMSC�ı�ʶ��Ϊ��" + request.getMMSRelayServerID());

		dealDeliver(request);

		// �ظ���Ϣ
		MM7DeliverRes mm7DeliverRes = new MM7DeliverRes();
		mm7DeliverRes.setTransactionID(request.getTransactionID());
		mm7DeliverRes.setServiceCode("��Ʒ����"); // ����ServiceCode����ѡ
		mm7DeliverRes.setStatusCode(MMConstants.RequestStatus.SUCCESS);
		mm7DeliverRes.setStatusText(MMConstants.RequestStatus.TEXT_SUCCESS);
		return mm7DeliverRes;
	}

	/**
	 * ��дdeliver_report��Ϣ
	 */
	@Override
	public MM7VASPRes doDeliveryReport(MM7DeliveryReportReq mm7DeliveryReportReq)
	{
		// ����д����״̬�����Ĵ������
		dealDeliveryReport(mm7DeliveryReportReq);

		// �ظ���Ϣ
		MM7DeliveryReportRes res = new MM7DeliveryReportRes();
		res.setTransactionID(mm7DeliveryReportReq.getTransactionID());
		res.setStatusCode(MMConstants.RequestStatus.SUCCESS);
		res.setStatusText(MMConstants.RequestStatus.TEXT_SUCCESS);
		return res;
	}

	/**
	 * ��дReadReply��Ϣ
	 */
	@Override
	public MM7VASPRes doReadReply(MM7ReadReplyReq mm7ReadReplyReq)
	{
		// ����д���ն������Ĵ������
		dealReadReply(mm7ReadReplyReq);

		// �ظ���Ϣ
		MM7ReadReplyRes res = new MM7ReadReplyRes();
		res.setTransactionID(mm7ReadReplyReq.getTransactionID());
		res.setStatusCode(MMConstants.RequestStatus.SUCCESS);
		res.setStatusText(MMConstants.RequestStatus.TEXT_SUCCESS);
		return res;
	}

	/**
	 * ��װUMms����
	 * 
	 * @param mmsFile
	 * @param deliverReq
	 * @return
	 */
	public static UMms makeMms(MmsFile mmsFile, MM7DeliverReq deliverReq)
	{
		UMms mms = new UMms();
		mms.setMmsFile(mmsFile);
		mms.setRecipient(deliverReq.getSender());
		mms.setSubject(deliverReq.getSubject());
		mms.setSendtime(DateUtils.getTimestamp14());
		// �������sendid
		// mms.setSendID("senid");
		return mms;
	}

	

	/**
	 * ��װMmsFile����
	 * 
	 * @param deliverReq
	 * @return
	 */
	public static MmsFile makeMmsFile(MM7DeliverReq deliverReq)
	{
		MmsFile mmsFile = new MmsFile();
		if (deliverReq.getContent() != null)
		{
			// �����༭�ļ�����Upload����Ķ�Ӧ��ϵ
			Map<String, UploadFile> fileNameMap = new HashMap<String, UploadFile>();
			long uploadFileSize = 0;// ������¼���������Ĵ�С
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
			// smil��Ϣ������
			for (int i = 0; i < list.size(); i++)
			{
				MMContent content = list.get(i);
				if (content.getContentType().equals(
						MMConstants.ContentType.SMIL))
				{
					// ����SMIL
					String smil = content.getContentAsString();
					SmilParser parser = new SmilParser(smil);
					parser.parse();//����
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
		mmsFile.setMmsName(deliverReq.getSubject());// ���ò��ű���
		return mmsFile;
	}

	private void dealDeliver(MM7DeliverReq deliverReq)
	{
		// ���
		/*
		 * DeliverBean deliver = new DeliverBean();
		 * deliver.setTransactionid(deliverReq.getTransactionID());
		 * deliver.setLinkId(deliverReq.getLinkedID());
		 * deliver.setMm7version(deliverReq.getMM7Version());
		 * deliver.setSubject(deliverReq.getSubject()); //
		 * deliver.setRecvTime(deliverReq.getTimeStamp());
		 * deliver.setRecvTime(DateUtils.getTimestamp14()); //
		 * dao.save(deliver);
		 */
		MmsFile mmsFile = makeMmsFile(deliverReq);
		UMms mms = makeMms(mmsFile, deliverReq);
		//log.info(mms);
		Result.getInstance().notifyResult(mms);
		// log.info(deliverReq);
	}

	private void dealDeliveryReport(MM7DeliveryReportReq deliverReportReq)
	{
		log.info("�յ����ͱ���");

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
		synchronized (messageidMap)
		{
			sendid = messageidMap.remove(messageid + "|" + to);

		}
		// ״̬����֪ͨ
		Result.getInstance().notifyResult(sendid, to, mmStatus, mmStatusText, reportTime);

	}

	private void dealReadReply(MM7ReadReplyReq mm7ReadReplyReq)
	{
		// �������s_log_mmssubmit��
		/*
		 * String messageid=mm7ReadReplyReq.getMessageID();
		 * if(!Tools.isBlank(messageid)) { SubmitBean
		 * bean=dao.getSubmitBeanByMessageid(messageid); if(bean!=null) {
		 * bean.setReadyreplyTime(mm7ReadReplyReq.getTimeStamp());
		 * bean.setReadstatus((int)mm7ReadReplyReq.getMMStatus());
		 * bean.setReadstatustext(mm7ReadReplyReq.getStatusText());
		 * dao.update(bean); } }
		 */
		// ��������¼��s_log_mmsreadreply
	}

	// Main����
	public static void main(String[] args) throws Exception
	{
		// ��ʼ��VASP
		MM7Config mm7Config = new MM7Config("./config/mm7Config.xml");
		// ����ConnConfig.xml�ļ���·��
		mm7Config.setConnConfigName("./config/ConnConfig.xml"); // �ر�
		// ����MyReceiver
		Receiver receiver = new Receiver(mm7Config);
		// ����������
		receiver.start();
		for (;;)
			;
	}

}
