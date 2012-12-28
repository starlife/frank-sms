package com.unicom.mm7.frame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cmcc.mm7.vasp.MM7Sender;
import com.cmcc.mm7.vasp.common.MMConstants;
import com.cmcc.mm7.vasp.common.MMContent;
import com.cmcc.mm7.vasp.common.MMContentType;
import com.cmcc.mm7.vasp.protocol.message.MM7RSRes;
import com.cmcc.mm7.vasp.protocol.message.MM7SubmitReq;
import com.cmcc.mm7.vasp.protocol.message.MM7SubmitRes;
import com.unicom.mm7.bean.MmsFile;
import com.unicom.mm7.bean.UMms;
import com.unicom.mm7.bean.UploadFile;
import com.unicom.mm7.conf.MM7Config;

public class Sender extends MM7Sender
{
	private static final Log log = LogFactory.getLog(Sender.class);

	/**
	 * 保存错误的session对应关系
	 */
	private static final Log sessionLog = LogFactory.getLog("session");

	/**
	 * 用来保存彩信transactionid和sendid的对应关系
	 */
	static final Map<String, String> transactionidMap = new HashMap<String, String>();

	/**
	 * 用来保存彩信messageid和sendid的对应关系
	 */
	static final Map<String, String> messageidMap = new HashMap<String, String>();

	/**
	 * 待发送彩信消息队列
	 */
	private static final LinkedBlockingQueue<MM7SubmitReq> que = new LinkedBlockingQueue<MM7SubmitReq>();

	public static final LinkedBlockingQueue<UMms> mmsQue = new LinkedBlockingQueue<UMms>();

	private String vaspid = "";// spid
	private String vasid = "";// 接入号
	private String serviceCode = "";
	private boolean chargedPartyExist = false;
	private int chargedParty = 0;// 计费 0表示发送方计费
	private int maxSrcID = 10;// 群发号码数

	/**
	 * 群发每条彩信最大的接收号码
	 */
	private static int allocTransactionId = 0;

	public Sender(MM7Config mm7Config)
	{

		super(mm7Config.getMMSCIP(), mm7Config.getMMSCURL(), mm7Config
				.getAuthenticationMode(), mm7Config.getUserName(), mm7Config
				.getPassword(), mm7Config.getCharSet(), mm7Config
				.getMaxMsgSize(), mm7Config.getReSendCount(), mm7Config
				.isKeepAlive(), mm7Config.getTimeOut(), mm7Config.getPoolSize());

		vaspid = mm7Config.getVASPID();
		vasid = mm7Config.getVASID();
		serviceCode = mm7Config.getServiceCode();
		chargedPartyExist = mm7Config.isChargedPartyExist();
		chargedParty = mm7Config.getChargedParty();
		maxSrcID = mm7Config.getMassCount();

	}

	/**
	 * 创建消息
	 * 
	 * @param trasactionid
	 *            流水号
	 * @param subject
	 *            消息主题
	 * @param content
	 *            消息内容
	 * @return
	 */
	private MM7SubmitReq createSubmitReq(String trasactionid, String subject,
			MMContent content)
	{
		MM7SubmitReq submitReq = new MM7SubmitReq();
		submitReq.setTransactionID(trasactionid);
		submitReq.setVASPID(vaspid);
		submitReq.setVASID(vasid);
		submitReq.setServiceCode(serviceCode);
		// submitReq.setChargedPartyID("8613276811362");
		if (chargedPartyExist)
		{
			submitReq.setChargedParty((byte) chargedParty);
		}
		submitReq.setSenderAddress(vasid);
		submitReq.setDeliveryReport(true);
		submitReq.setReadReply(true);
		submitReq.setSubject(subject);
		submitReq.setContent(content);
		return submitReq;
	}

	/**
	 * 创建消息内容体
	 * 
	 * @param mmsFile
	 *            包含smil和所有附件
	 * @return
	 */
	private MMContent createSubmitReqContent(MmsFile mmsFile)
	{

		MMContent content = new MMContent();
		content.setContentType(MMConstants.ContentType.MULTIPART_MIXED);
		// 添加smil和各个附件
		MMContent subSmil = MMContent.createFromString(mmsFile.getSmildata());
		subSmil.setContentID(mmsFile.getSmilname());
		subSmil.setContentType(MMConstants.ContentType.SMIL);
		content.addSubContent(subSmil);
		Iterator<UploadFile> attachs = mmsFile.getUploadFiles().iterator();
		while (attachs.hasNext())
		{
			UploadFile attach = attachs.next();
			try
			{
				MMContent sub = MMContent.createFromBytes(attach.getFiledata());
				String filename = attach.getFilename();
				sub.setContentID(filename);
				sub.setContentType(getContentType(attach.getFiletype()));
				content.addSubContent(sub);
			}
			catch (Exception e)
			{
				// TODO Auto-generated catch block
				log.error(null, e);
			}
		}
		return content;
	}

	/**
	 * 根据文件的后缀名赋值MMContentType
	 * 
	 * @param ext
	 * @return
	 */
	private MMContentType getContentType(String ext)
	{
		MMContentType contentType = null;
		String type = ext.toLowerCase();
		if ("image/gif".equals(type) || (type.indexOf("gif") != -1))
		{
			contentType = MMConstants.ContentType.GIF;
		}
		else if ("image/pjpeg".equals(type) || "image/jpeg".equals(type)
				|| (type.indexOf("jpeg") != -1) || (type.indexOf("jpg") != -1))
		{
			contentType = MMConstants.ContentType.JPEG;

		}
		else if ("image/x-png".equals(type) || "image/png".equals(type)
				|| (type.indexOf("png") != -1))
		{
			contentType = MMConstants.ContentType.PNG;
		}
		else if ("amr".equals(type))
		{
			contentType = MMConstants.ContentType.AMR;
		}
		else if ("audio/x-midi".equals(type) || "audio/midi".equals(type)
				|| (type.indexOf("mid") != -1) || (type.indexOf("midi") != -1))
		{
			contentType = MMConstants.ContentType.MIDI;
		}
		else if ("text/plain".equals(type) || (type.indexOf("txt") != -1)
				|| (type.indexOf("text") != -1))
		{
			contentType = MMConstants.ContentType.TEXT;
		}
		else
		{
			contentType = MMConstants.ContentType.TEXT;
		}
		return contentType;
	}

	/**
	 * 分配流水号
	 * 
	 * @return
	 */
	public static synchronized String allocTransactionID()
	{
		allocTransactionId = (allocTransactionId + 1) & 0xffffffff;
		return String.valueOf(allocTransactionId);
	}

	@Override
	public MM7SubmitReq submit()
	{
		// 从队列中取数据，如果没有，那么彩信队列中去取然后组装
		synchronized (que)
		{
			MM7SubmitReq pack = que.poll();
			if (pack == null)
			{
				UMms mms = mmsQue.poll();
				if (mms == null)
				{
					// 休息1s
					try
					{
						TimeUnit.SECONDS.sleep(2);
					}
					catch (InterruptedException e)
					{
						// TODO Auto-generated catch block
						log.error(null, e);
					}
				}
				else
				{
					// 彩信sendid
					String sendid = mms.getSendID();
					String subject = mms.getSubject();
					// 解析号码
					String[] numbers = parse(mms.getRecipient());
					// 取得彩信内容并组装好
					MMContent content = createSubmitReqContent(mms.getMmsFile());

					MM7SubmitReq submitReq = null;
					for (int j = 0; j < numbers.length; j += maxSrcID)
					{
						// 重新赋值submitReq
						String trasactionid = allocTransactionID();

						submitReq = createSubmitReq(trasactionid, subject,
								content);
						for (int k = j; k < Math.min(j + maxSrcID,
								numbers.length); k++)
						{
							submitReq.addTo(numbers[k]);
						}

						que.offer(submitReq);
						// 处理session
						synchronized (transactionidMap)
						{
							// 如果transactionidMap大于100000，说明程序有错误，需要清理
							if (transactionidMap.size() > 100000)
							{
								sessionLog.info("transactionidMap:"
										+ transactionidMap);
								transactionidMap.clear();
							}
							transactionidMap.put(trasactionid, sendid);
						}
					}
				}
			}
			return pack;
		}

	}

	@Override
	public void doSubmit(MM7SubmitReq submitMsg, MM7RSRes res)
	{
		// 成功的情况：不处理，只写messageid和sendid之间的关系
		String messageid = null;
		String sendid = null;

		// 读取sendid
		String transactionid = submitMsg.getTransactionID();
		synchronized (transactionidMap)
		{
			sendid = transactionidMap.remove(transactionid);
		}

		if (res instanceof MM7SubmitRes)
		{
			MM7SubmitRes submitRes = (MM7SubmitRes) res;
			messageid = submitRes.getMessageID();
		}
		else
		{
			log.error("发送失败，收不到的并不是MM7SubmitRes包");

		}
		// 记录失败消息
		if (res.getStatusCode() != 1000)
		{
			// 发送失败消息处理
			Result.getInstance().notifyResult(sendid, submitMsg.getTo(),
					res.getStatusCode());
		}

		// 做messageid和sendid的映射 ，收状态报告处有用
		if (messageid != null && sendid != null)
		{
			List<String> mobiles = submitMsg.getTo();
			for (int i = 0; i < mobiles.size(); i++)
			{
				String mobile = mobiles.get(i);
				synchronized (messageidMap)
				{
					// 如果messageidMap大于100000，说明程序有错误，需要清理
					if (messageidMap.size() > 100000)
					{
						sessionLog.info("messageidMap:" + messageidMap);
						messageidMap.clear();
					}
					messageidMap.put(messageid + "|" + mobile, sendid);
				}

			}
		}

	}

	public void myStop()
	{
		super.myStop();

	}

	/**
	 * 解析号码列表
	 * 
	 * @param recipient
	 * @return
	 */
	public static String[] parse(String recipient)
	{
		List<String> list = new ArrayList<String>();
		String[] numbers = recipient.split("\r\n?|[,；，;]");
		for (int i = 0; i < numbers.length; i++)
		{
			if (numbers[i] != null && numbers[i].trim().length() > 0)
			{
				list.add(numbers[i].trim());
			}
		}
		numbers = new String[list.size()];
		for (int i = 0; i < numbers.length; i++)
		{
			numbers[i] = list.get(i);
		}
		list.clear();
		return numbers;
	}

	public static void main(String[] args) throws Exception
	{
		MM7Config mm7Config = new MM7Config("./config/mm7Config.xml");
		Sender sender = new Sender(mm7Config);
		sender.start();
	}
}
