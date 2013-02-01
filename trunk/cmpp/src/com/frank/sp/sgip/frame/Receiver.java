package com.frank.sp.sgip.frame;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.chinaunicom.sgip1_2.protocol.PReceiver;
import com.chinaunicom.sgip1_2.protocol.message.DeliverMessage;
import com.chinaunicom.sgip1_2.protocol.message.ReportMessage;
import com.chinaunicom.sgip1_2.protocol.message.Sequence;
import com.frank.sp.sgip.bean.USms;
import com.frank.sp.sgip.conf.Config;

/**
 * 短信接收类，接收到短信，并且入库
 * 
 * @version 1.0
 * @version 1.1 frank 支持群发
 * @author Administrator
 */
public class Receiver extends PReceiver
{
	private static final Log log = LogFactory.getLog(Receiver.class);

	public Receiver(Config cfg)
	{
		super(cfg.getListenAddr(), cfg.getListenPort(), cfg.getBackLog(), cfg
			.getNodeID(), 2, cfg.getLoginName(), cfg.getLoginPass(), cfg
			.getServerTimeOut());
		// TODO Auto-generated constructor stub
	}

	public void doDeliver(DeliverMessage req)
	{
		USms sms = new USms();
		sms.setSendid("");
		sms.setMsgContent(req.getMsgContent());
		sms.setRecipient(req.getUserNumber());
		Result.getInstance().notifyDeliver(sms);
		log.info("doDeliver ok");

	}

	public void doReport(ReportMessage req)
	{
		Sequence submitReq = req.getSubmitSeq();
		String mobile = req.getUserNumber();
		if (mobile.startsWith("+86"))
		{
			mobile = mobile.substring(3, mobile.length());
		}
		else if (mobile.startsWith("86"))
		{
			mobile = mobile.substring(2, mobile.length());
		}
		int errorCode = req.getErrorCode();
		String sendid = null;
		synchronized (Sender.sessionExMap)
		{
			sendid = Sender.sessionExMap.remove(submitReq + "," + mobile);
		}
		if (sendid == null)
		{
			log
				.error("sendid为空错误，submitReq+mobile=" + submitReq + ","
						+ mobile);
			return;
		}
		Result.getInstance().notifyReport(sendid, mobile, errorCode);
		log.info("doReport ok");
	}

}
