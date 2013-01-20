package com.ylear.sp.sgip.frame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.chinaunicom.sgip1_2.protocol.PSender;
import com.chinaunicom.sgip1_2.protocol.message.APackage;
import com.chinaunicom.sgip1_2.protocol.message.Sequence;
import com.chinaunicom.sgip1_2.protocol.message.SubmitMessage;
import com.chinaunicom.sgip1_2.protocol.message.SubmitRespMessage;
import com.chinaunicom.sgip1_2.protocol.util.MessageUtil;
import com.ylear.sp.sgip.bean.USms;
import com.ylear.sp.sgip.conf.Config;

public class Sender extends PSender
{
	private static final Log log = LogFactory.getLog(Sender.class);

	/**
	 * 保存错误的session对应关系
	 */
	private static final Log sessionLog = LogFactory.getLog("session");

	private static final LinkedBlockingQueue<APackage> que = new LinkedBlockingQueue<APackage>();
	public static final LinkedBlockingQueue<USms> smsQue = new LinkedBlockingQueue<USms>();

	/**
	 * 保存submitSeq和sendId之间的关闭，便于和Report消息联系
	 */
	private static final Map<Sequence, String> sessionMap = new HashMap<Sequence, String>();
	/**
	 * 保存submitSeq+号码和sendId之间的关闭，便于和Report消息联系
	 */
	static final Map<String, String> sessionExMap = new HashMap<String, String>();

	private int maxDestId = 10;// 群发每条短信最大的接收号码
	private String spnumber;
	private String spid;
	private String serviceCode;
	private String nodeid;

	// 计费相关
	private boolean feeSwitch = false;
	private int feeType = 1;
	private String feeValue = "";

	public Sender(Config cfg)
	{
		super(cfg.getSMGServer(), cfg.getSMGPort(), cfg.getNodeID(), 2, cfg
			.getLoginName(), cfg.getLoginPass(), cfg.getTimeOut(), cfg
			.getRetryCount());
		this.spid = cfg.getSPID();
		this.spnumber = cfg.getSPNumber();
		this.serviceCode = cfg.getServiceCode();
		this.nodeid = cfg.getNodeID();
		this.feeSwitch = cfg.isFeeSwitch();
		this.feeType = cfg.getFeeType();
		this.feeValue = cfg.getFeeValue();
		this.maxDestId = cfg.getMassCount();

	}

	public APackage doSubmit()
	{
		// 从数据库中取数据,
		// 如果有待发送的数据,则构建Submit消息
		synchronized (que)
		{
			APackage pack = que.poll();
			if (pack == null)
			{
				USms sms = smsQue.poll();
				if (sms == null)
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
				// 短信sendId
				String sendId = sms.getSendid();
				// 解析号码
				String[] numbers = parse(sms.getRecipient());
				List<String> to = new ArrayList<String>();
				for (int j = 0; j < numbers.length; j += maxDestId)
				{
					to.clear();// 每一次都清空
					for (int k = j; k < Math.min(j + maxDestId, numbers.length); k++)
					{
						to.add(numbers[k]);
					}
					SubmitMessage[] smList = createSumbitMessage(to, sms
						.getMsgContent());
					for (SubmitMessage sm : smList)
					{
						que.offer(sm);
						// 处理session
						synchronized (sessionMap)
						{
							// 如果transactionidMap大于100000，说明程序有错误，需要清理
							if (sessionMap.size() > 100000)
							{
								sessionLog.info("sessionMap:" + sessionMap);
								sessionMap.clear();
							}
							sessionMap
								.put(sm.getHead().getSequenceId(), sendId);
						}

					}
				}

			}
			return pack;
		}

	}

	/**
	 * 把Submit消息入库，这里支持群发的处理
	 */
	public void doSubmitResp(SubmitMessage sm, SubmitRespMessage srm)
	{

		log.info("doSubmitResp ok");
		Sequence submitReq = sm.getHead().getSequenceId();
		String sendid = null;
		synchronized (sessionMap)
		{
			sendid = sessionMap.get(submitReq);
		}
		if (sendid == null)
		{
			log.error("sendid为空错误，submitReq=" + submitReq);
			return;
		}
		// 如果提交失败，那么通知提交失败消息
		if (srm.getResult() != 0)
		{
			Result.getInstance().notifySubmitResp(sendid,
				sm.getSubmit().getUserNumber(), srm.getResult());
		}
		else
		{
			// 做messageid和sendid的映射 ，收状态报告处有用

			String[] mobiles = sm.getSubmit().getUserNumber();

			for (String mobile : mobiles)
			{
				synchronized (sessionExMap)
				{
					// 如果messageidMap大于100000，说明程序有错误，需要清理
					if (sessionExMap.size() > 100000)
					{
						sessionLog.info("sessionExMap:" + sessionExMap);
						sessionExMap.clear();
					}
					sessionExMap.put(submitReq.toString() + "," + mobile,
						sendid);
				}
			}

		}

	}

	private SubmitMessage[] createSumbitMessage(List<String> to,
			String msgContent)
	{
		String[] desttermid = new String[to.size()];
		for (int i = 0; i < to.size(); i++)
		{
			desttermid[i] = to.get(i);
		}
		return createSumbitMessage(desttermid, msgContent);

	}

	private SubmitMessage[] createSumbitMessage(String[] desttermid,
			String msgContent)
	{
		String param = "";
		if (feeSwitch)
		{
			param += "FeeType=" + feeType + "\r\n";
			param += "FeeValue=" + feeValue + "\r\n";
		}
		return MessageUtil.createSubmitMessage(nodeid, spid, spnumber,
			serviceCode, desttermid, msgContent, param);

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
