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

import com.cmcc.mm7.vasp.MM7Sender;
import com.cmcc.mm7.vasp.common.MMConstants;
import com.cmcc.mm7.vasp.common.MMContent;
import com.cmcc.mm7.vasp.common.MMContentType;
import com.cmcc.mm7.vasp.protocol.message.MM7RSRes;
import com.cmcc.mm7.vasp.protocol.message.MM7SubmitReq;
import com.cmcc.mm7.vasp.protocol.message.MM7SubmitRes;
import com.vasp.mm7.conf.MM7Config;
import com.vasp.mm7.database.MmsFileDaoImpl;
import com.vasp.mm7.database.SubmitDaoImpl1;
import com.vasp.mm7.database.UMmsDaoImpl;
import com.vasp.mm7.database.pojo.MmsFile;
import com.vasp.mm7.database.pojo.SubmitBean;
import com.vasp.mm7.database.pojo.UMms;
import com.vasp.mm7.database.pojo.UploadFile;
import com.vasp.mm7.util.DateUtils;

public class Sender extends MM7Sender
{
	static final Map<String, Long> sessionMap = new HashMap<String, Long>();// 保存sessionid

	private static final Log log = LogFactory.getLog(Sender.class);

	// private static final Log db = LogFactory.getLog("db");
	//private static final Log lose = LogFactory.getLog("lose");// 保存丢失包
	private static final Log sessionLog = LogFactory.getLog("session");// 记录丢弃的session日志
	/**
	 * 待发送彩信消息队列
	 */
	private static final LinkedBlockingQueue<MM7SubmitReq> que = new LinkedBlockingQueue<MM7SubmitReq>();

	public static int maxSrcID = 10;

	private String vaspid = "";// spid
	private String vasid = "";// 接入号
	private String serviceCode = "";

	/**
	 * 数据库访问对象
	 */
	private UMmsDaoImpl ummsDao = UMmsDaoImpl.getInstance();

	private MmsFileDaoImpl mmsFileDao = MmsFileDaoImpl.getInstance();

	private SubmitDaoImpl1 submitDao = SubmitDaoImpl1.getInstance();

	/**
	 * 群发每条彩信最大的接收号码
	 */

	private static int allocTransactionId = 0;

	public Sender(MM7Config mm7Config)
	{

		super(mm7Config.getMMSCIP(), mm7Config.getMMSCURL(), mm7Config
				.getAuthenticationMode(), mm7Config.getUserName(), mm7Config
				.getPassword(), mm7Config.getCharSet(), mm7Config
				.getMaxMsgSize(),mm7Config.getReSendCount(), mm7Config.isKeepAlive(), mm7Config
				.getTimeOut());

		vaspid = mm7Config.getVASPID();
		vasid = mm7Config.getVASID();
		serviceCode = mm7Config.getServiceCode();

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
		// submitReq.setVASPID("895192");
		submitReq.setVASPID(vaspid);
		submitReq.setVASID(vasid);
		submitReq.setServiceCode(serviceCode);
		// submitReq.setVASID("106573061704");
		// submitReq.setServiceCode("1113329901");
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
	
	@Override
	public MM7SubmitReq submit()
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
					TimeUnit.SECONDS.sleep(2);
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
				for (int j = 0; j < numbers.length; j += maxSrcID)
				{
					// 重新赋值submitReq
					String trasactionid = allocTransactionID();

					submitReq = createSubmitReq(trasactionid, mms.getSubject(),
							content);
					for (int k = j; k < Math.min(j + maxSrcID, numbers.length); k++)
					{
						submitReq.addTo(numbers[k]);
					}

					que.add(submitReq);
					// 处理session
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

	
	@Override
	public void doSubmit(MM7SubmitReq submitMsg, MM7RSRes res)
	{
		String messageid=" ";
		if (res instanceof MM7SubmitRes)
		{
			MM7SubmitRes submitRes = (MM7SubmitRes) res;
			messageid = submitRes.getMessageID();
		}
		else
		{
			log.debug("发送失败，收不到的并不是MM7SubmitRes包");
		}
		
		String trasactionid = submitMsg.getTransactionID();
		// 从sessionMap取得对应关系的ummsid
		Long sessionid = null;
		synchronized (sessionMap)
		{
			sessionid = sessionMap.remove(trasactionid);
		}
		List<String> numbers = submitMsg.getTo();
		for (int i = 0; i < numbers.size(); i++)
		{
			SubmitBean submitBean = new SubmitBean();
			submitBean.setMessageid(messageid);
			submitBean.setTransactionid(trasactionid);
			submitBean.setMm7version(submitMsg.getMM7Version());
			submitBean.setToAddress((String) submitMsg.getTo().get(i));
			submitBean.setSubject(submitMsg.getSubject());
			submitBean.setSendtime(DateUtils.getTimestamp14());
			submitBean.setVaspid(submitMsg.getVASPID());
			submitBean.setVasid(submitMsg.getVASID());
			submitBean.setServiceCode(submitMsg.getServiceCode());
			submitBean.setLinkid(submitMsg.getLinkedID());
			submitBean.setStatus(res.getStatusCode());
			submitBean.setStatusText(res.getStatusText());
			submitBean.setSessionid(sessionid);
			// db.info(submitBean);
			log.debug("submitDao.save(submitBean) 之前:"+System.currentTimeMillis());
			submitDao.save(submitBean);
			log.debug("submitDao.save(submitBean) 之后:"+System.currentTimeMillis());
		}

		

	}

	public static void main(String[] args) throws Exception
	{
		MM7Config mm7Config = new MM7Config("./config/mm7Config.xml");
		mm7Config.setConnConfigName("./config/ConnConfig.xml");
		Sender sender = new Sender(mm7Config);
		sender.start();
	}
}
