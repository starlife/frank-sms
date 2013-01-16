package com.ylear.sp.sgip.frame;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.chinamobile.cmpp2_0.protocol.PSender;
import com.chinamobile.cmpp2_0.protocol.message.APackage;
import com.chinamobile.cmpp2_0.protocol.message.SubmitMessage;
import com.chinamobile.cmpp2_0.protocol.util.MessageUtil;
import com.ylear.sp.cmpp.database.USmsDaoImpl;
import com.ylear.sp.cmpp.database.pojo.USms;
import com.ylear.sp.cmpp.util.DateUtils;

public class Sender extends PSender
{
	static final Map<Integer, Long> sessionMap = new HashMap<Integer, Long>();// 保存sessionid
	static final LinkedBlockingQueue<APackage> que = new LinkedBlockingQueue<APackage>();
	private static final Log sessionLog = LogFactory.getLog("session");// 记录丢弃包日志
	private USmsDaoImpl dao = USmsDaoImpl.getInstance();
	private String[] dest_term_id = null;
	private int maxDestId = 10;// 群发每条短信最大的接收号码
	private String spnumber;
	private String spid;
	private String service_id;
	private String feetype;
	private String feecode;

	public Sender(Config cfg)
	{
		super(cfg.getRemoteServer(), cfg.getRemotePort(), cfg.getLoginName(),
				cfg.getLoginPswd(), cfg.getVersion());
		this.maxDestId = cfg.getMultisendMaxTel();
		this.spid = cfg.getSpid();
		this.spnumber = cfg.getSpnumber();
		this.service_id = cfg.getServicecode();
		this.feecode = cfg.getFeecode();
		this.feetype = cfg.getFeetype();
	}

	public APackage doSubmit()
	{
		// 从数据库中取数据,
		// 如果有待发送的数据,则构建Submit消息
		APackage pack = que.poll();
		if (pack == null)
		{
			List<USms> list = dao.getReadySendSms(DateUtils
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
					e.printStackTrace();
				}
			}
			for (int i = 0; i < list.size(); i++)
			{
				USms sms = list.get(i);
				// 短信sessionid
				Long sessionid = sms.getId();
				// 短信内容
				//sms.getMsgContent();
				// 解析号码
				String[] numbers = parse(sms.getRecipient());
				for (int j = 0; j < numbers.length; j++)
				{
					SubmitMessage[] smList = createSumbitMessage(numbers[j],
							sms.getMsgContent());
					for (SubmitMessage sm : smList)
					{
						que.offer(sm);
						// 保存短信和sessionid之间的关系
						synchronized (sessionMap)
						{
							// 如果sessionMap大于100000，说明程序有错误，需要清理
							if (sessionMap.size() > 100000)
							{
								sessionLog.info(sessionMap);
								sessionMap.clear();
							}
							sessionMap.put(sm.getHead().getSequenceId(),
									sessionid);
						}

					}
				}

			}

		}
		return pack;
	}

	private SubmitMessage[] createSumbitMessage(String to, String msgContent)
	{
		String[] desttermid = to.split(",");
		return createSumbitMessage(desttermid, msgContent);

	}

	private SubmitMessage[] createSumbitMessage(String[] desttermid,
			String msgContent)
	{
		String param = "";
		return MessageUtil.createSubmitMessage(spid, spnumber, service_id,
				desttermid, msgContent, param);

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

}
