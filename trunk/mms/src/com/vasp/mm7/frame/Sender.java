package com.vasp.mm7.frame;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cmcc.mm7.vasp.common.MMConstants;
import com.cmcc.mm7.vasp.common.MMContent;
import com.cmcc.mm7.vasp.common.MMContentType;
import com.cmcc.mm7.vasp.conf.MM7Config;
import com.cmcc.mm7.vasp.message.MM7RSRes;
import com.cmcc.mm7.vasp.message.MM7SubmitReq;
import com.cmcc.mm7.vasp.message.MM7SubmitRes;
import com.cmcc.mm7.vasp.service.MM7Sender;
import com.vasp.mm7.database.MmsFileDaoImpl;
import com.vasp.mm7.database.UMmsDaoImpl;
import com.vasp.mm7.database.pojo.MmsFile;
import com.vasp.mm7.database.pojo.SubmitBean;
import com.vasp.mm7.database.pojo.UMms;
import com.vasp.mm7.database.pojo.UploadFile;
import com.vasp.mm7.util.DateUtils;

public class Sender extends Thread
{
	static final Map<String, Long> sessionMap = new HashMap<String, Long>();// 保存sessionid

	private static final Log log = LogFactory.getLog(Sender.class);

	private static final Log db = LogFactory.getLog("db");
	private static final Log sessionLog = LogFactory.getLog("session");// 记录丢弃的session日志
	/**
	 * 待发送彩信消息队列
	 */
	private static final LinkedBlockingQueue<MM7SubmitReq> que = new LinkedBlockingQueue<MM7SubmitReq>();

	private MM7Config config = null;
	/**
	 * 发送对象
	 */
	private MM7Sender sender = null;
	/**
	 * 数据库访问对象
	 */
	private UMmsDaoImpl ummsDao = UMmsDaoImpl.getInstance();

	private MmsFileDaoImpl mmsFileDao = MmsFileDaoImpl.getInstance();

	/**
	 * 群发每条彩信最大的接收号码
	 */
	public static final int MAX_NUMBER = 10;

	private static int allocTransactionId = 0;

	private volatile boolean stop = false;

	public Sender(MM7Config config) throws Exception
	{
		this.config = config;
		sender = new MM7Sender(config);
	}

	public void run()
	{
		while (!stop)
		{
			// 取彩信并发送
			MM7SubmitReq submitMsg = this.doSubmit();
			if (submitMsg != null)
			{
				// 发送
				MM7RSRes res = sender.send(submitMsg);
				log.info("res.statuscode=" + res.getStatusCode()
						+ ";res.statusText=" + res.getStatusText());
				// 写数据库日志
				dealSubmit(submitMsg, res);
			}
		}
	}

	public MM7SubmitReq doSubmit()
	{
		// 从队列中取数据，如果没有，那么从数据库中取并加入到队列中
		MM7SubmitReq pack = que.poll();
		if (pack == null)
		{
			List<UMms> list = ummsDao.getReadySendSms(DateUtils
					.getTimestamp14());
			if (list.size() == 0)
			{
				// 休息1s
				try
				{
					TimeUnit.SECONDS.sleep(1);
				}
				catch (InterruptedException e)
				{
					// TODO Auto-generated catch block
					log.error(null, e);
				}
			}
			for (int i = 0; i < list.size(); i++)
			{
				UMms mms = (UMms) list.get(i);
				// 彩信sessionid
				Long sessionid = mms.getId();
				// 解析号码
				String[] numbers = parse(mms.getRecipient());
				// 取得彩信内容并组装好
				MmsFile mmsFile = mmsFileDao.getMmsFile(mms.getMmsid());
				MMContent content = createSubmitReqContent(mmsFile);

				MM7SubmitReq submitReq = null;
				for (int j = 0; j < numbers.length; j++)
				{
					// 重新赋值submitReq
					String trasactionid = allocTransactionID();

					submitReq = createSubmitReq(trasactionid, mms.getSubject(),
							content);
					submitReq.addTo(numbers[j]);
					que.add(submitReq);

					synchronized (sessionMap)
					{
						// 如果sessionMap大于100000，说明程序有错误，需要清理
						if (sessionMap.size() > 100000)
						{
							sessionLog.info(sessionMap);
							sessionMap.clear();
						}
						sessionMap.put(trasactionid, sessionid);
					}
				}
			}
		}
		return pack;
	}

	/**
	 * 记录表日志
	 * 
	 * @param submitMsg
	 * @param res
	 */
	public void dealSubmit(MM7SubmitReq submitMsg, MM7RSRes res)
	{
		String trasactionid = submitMsg.getTransactionID();
		// 从sessionMap取得对应关系的ummsid
		Long sessionid = null;
		synchronized (Sender.sessionMap)
		{
			sessionid = Sender.sessionMap.remove(trasactionid);
		}

		String messageid = null;
		if (res instanceof MM7SubmitRes)
		{
			MM7SubmitRes submitRes = (MM7SubmitRes) res;
			messageid = submitRes.getMessageID();

		}
		SubmitBean submitBean = new SubmitBean();
		submitBean.setTransactionid(trasactionid);
		submitBean.setMm7version(submitMsg.getMM7Version());
		submitBean.setToAddress((String) submitMsg.getTo().get(0));
		// submitBean.setCcAddress(submitMsg.getCc());
		// submitBean.setBccAddress(submitMsg.getBcc());
		submitBean.setSubject(submitMsg.getSubject());
		submitBean.setSendtime(DateUtils.getTimestamp14(submitMsg
				.getTimeStamp()));
		submitBean.setVaspid(submitMsg.getVASPID());
		submitBean.setVasid(submitMsg.getVASID());
		submitBean.setServiceCode(submitMsg.getServiceCode());
		submitBean.setLinkid(submitMsg.getLinkedID());
		submitBean.setStatus(res.getStatusCode());
		submitBean.setStatusText(res.getStatusText());
		submitBean.setSessionid(sessionid);
		submitBean.setMessageid(messageid);
		db.info(submitBean);
		// ummsDao.save(submitBean);
		/*
		 * for(int i=0;i<submitMsg.getTo().size();i++) { SLogMmssubmit
		 * submitBean=new SLogMmssubmit();
		 * submitBean.setTransactionid(trasactionid);
		 * submitBean.setMm7version(submitMsg.getMM7Version());
		 * submitBean.setToAddress((String)submitMsg.getTo().get(i));
		 * //submitBean.setCcAddress(submitMsg.getCc());
		 * //submitBean.setBccAddress(submitMsg.getBcc());
		 * submitBean.setSubject(submitMsg.getSubject());
		 * submitBean.setSendtime(submitMsg.getTimeStamp());
		 * submitBean.setVaspid(submitMsg.getVASPID());
		 * submitBean.setVasid(submitMsg.getVASID());
		 * submitBean.setServicecode(submitMsg.getServiceCode());
		 * submitBean.setLinkedid(submitMsg.getLinkedID());
		 * submitBean.setIsdeliverreport(Tools.bool2int(submitMsg.getDeliveryReport()));
		 * submitBean.setIsreadreply(Tools.bool2int(submitMsg.getReadReply()));
		 * submitBean.setStatuscode(res.getStatusCode());
		 * submitBean.setStatustext(res.getStatusText());
		 * submitBean.setUmmsid(ummsid);
		 * //设置messageid，值为一个messageid~~messageid+n if(messageid!=null) {
		 * submitBean.setMessageid(Tools.stringPlus(messageid, i)); }
		 * dao.save(submitBean); }
		 */

	}

	/**
	 * 解析号码列表
	 * 
	 * @param recipient
	 * @return
	 */
	private String[] parse(String recipient)
	{
		String[] numbers = recipient.split("[,；，;]");
		return numbers;
	}

	/**
	 * 创建消息
	 * 
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
		// submitReq.addTo("13915002000");
		submitReq.setVASID("895192");
		submitReq.setVASID("106573061704");
		submitReq.setServiceCode("1113329901");
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
		MMContent subSmil = MMContent.createFromString(mmsFile.getSmildate());
		subSmil.setContentID(mmsFile.getSmilname());
		subSmil.setContentType(MMConstants.ContentType.SMIL);
		content.addSubContent(subSmil);
		Iterator<UploadFile> attachs = mmsFile.getUploadFiles().iterator();
		while (attachs.hasNext())
		{
			UploadFile attach = attachs.next();
			MMContent sub;
			try
			{
				sub = MMContent.createFromStream(attach.getFiledata()
						.getBinaryStream());
				String filename = attach.getFilename();
				sub.setContentID(filename);
				// String ext=filename.substring(filename.lastIndexOf(".")+1);
				// sub.setContentType(getContentType(ext));
				sub.setContentType(getContentType(attach.getFiletype()));
				content.addSubContent(sub);
			}
			catch (SQLException e)
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

	public synchronized void mystop()
	{
		this.stop = true;
	}

	public static void main(String[] args) throws Exception
	{
		MM7Config mm7Config = new MM7Config("./config/mm7Config.xml");
		mm7Config.setConnConfigName("./config/ConnConfig.xml");
		MM7Sender mm7Sender = new MM7Sender(mm7Config);
		MM7SubmitReq submit = new MM7SubmitReq();
		submit.setTransactionID("11111111");
		submit.setVASPID("895192");
		submit.addTo("13777802386");
		submit.setVASID("106573061704");
		submit.setServiceCode("1113329901");
		submit.setSubject("测试");
		MMContent content = new MMContent();
		content.setContentType(MMConstants.ContentType.MULTIPART_MIXED);

		MMContent smilSub = MMContent.createFromFile("D:/test/smil.xml");
		smilSub.setContentType(MMConstants.ContentType.SMIL);
		smilSub.setContentID("smil.xml");
		content.addSubContent(smilSub);

		MMContent sub1 = MMContent.createFromFile("D:/test/1.txt");
		// MMContent.createFromStream(in)
		sub1.setContentID("1.txt");
		sub1.setContentType(MMConstants.ContentType.TEXT);
		content.addSubContent(sub1);

		MMContent sub3 = MMContent.createFromFile("D:/test/img_09.jpg");
		sub3.setContentID("img_09.jpg");
		sub3.setContentType(MMConstants.ContentType.JPEG);
		content.addSubContent(sub3);

		MMContent sub2 = MMContent.createFromFile("D:/test/2.txt");
		sub2.setContentID("2.txt");
		sub2.setContentType(MMConstants.ContentType.TEXT);
		content.addSubContent(sub2);

		MMContent sub4 = MMContent.createFromFile("D:/test/img_13.jpg");
		sub4.setContentID("img_13.jpg");
		sub4.setContentType(MMConstants.ContentType.JPEG);
		content.addSubContent(sub4);

		submit.setContent(content);
		System.out.println(submit);
		MM7RSRes res = mm7Sender.send(submit);
		System.out.println("#####################");
		System.out.println(res);
		System.out.println("res.statuscode=" + res.getStatusCode()
				+ ";res.statusText=" + res.getStatusText());
	}
}
