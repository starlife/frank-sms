package com.frank.sp.sgip.frame;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.frank.sp.sgip.util.DateUtils;
import com.tourzj.sms.SmsEngineSoapBindingStub;
import com.tourzj.sms.req.ReportReq;
import com.tourzj.sms.req.DeliverReq;
import com.tourzj.sms.req.Request;
import com.tourzj.sms.rsp.ReportRsp;
import com.tourzj.sms.rsp.DeliverRsp;

/**
 * 这里通知可能会很慢为了不影响效率需要用线程+缓冲队列的形式 如果通知失败，那么写日志
 * 
 * @author Administrator
 */
public class NotifyThread extends Thread
{
	/**
	 * 通知URL
	 */
	static String URL = "http://localhost/ws/services/SmsEngine";

	/**
	 * 超时时间默认为1s
	 */
	static int TIMEOUT = 1000;

	private static final Log log = LogFactory.getLog(NotifyThread.class);

	/**
	 * 通知失败写日志
	 */
	private static final Log resultLog = LogFactory.getLog("result");

	/**
	 * 通知消息缓冲队列
	 */
	private final LinkedBlockingQueue<Request> buffer = new LinkedBlockingQueue<Request>();

	/**
	 * 单实例对象
	 */
	private static NotifyThread instance = null;

	private static Object lock = new Object();

	private volatile boolean stop = false;

	private NotifyThread()
	{
		start();
	}

	public void run()
	{
		log.info("NotifyThread 线程开启");
		while (!stop)
		{
			try
			{
				Request req = buffer.poll();
				if (req == null)
				{
					synchronized (this)
					{
						this.wait();
					}
					continue;
				}
				if (req instanceof ReportReq)
				{
					ReportReq reportReq = (ReportReq) req;
					if (!notifyReport(reportReq))
					{
						// 通知失锟杰ｏ拷写锟斤拷志
						resultLog.info("sendid:" + reportReq.getSendid()
								+ "|mobile:" + reportReq.getMobile()
								+ "|result:" + reportReq.getResult()
								+ "|resultMessage:"
								+ reportReq.getResultMessage() + "|reportTime:"
								+ reportReq.getReportTime());
					}
				}
				else if (req instanceof DeliverReq)
				{
					DeliverReq deliverReq = (DeliverReq) req;
					if (!notifyDeliver(deliverReq))
					{
						resultLog.info("deliver:" + deliverReq.getSendid()
								+ "|" + deliverReq.getMsgContent() + "|"
								+ deliverReq.getRecipient());
					}
				}
			}
			catch (Exception e)
			{
				log.error(null, e);
			}
		}
		log.info("NotifyThread 线程关闭");
	}

	public static NotifyThread getInstance()
	{
		synchronized (lock)
		{
			if (instance == null)
			{
				instance = new NotifyThread();
			}
			return instance;
		}
	}

	private boolean notifyReport(ReportReq req)
	{
		ReportRsp rsp = null;
		try
		{
			log.debug("notify webservice report msg");
			SmsEngineSoapBindingStub stub = new SmsEngineSoapBindingStub(
				new URL(URL), null);
			stub.setTimeout(TIMEOUT);
			rsp = stub.report(req);
		}
		catch (MalformedURLException ex)
		{
			log.error(null, ex);
		}
		catch (RemoteException ex)
		{
			log.error(null, ex);
		}
		return rsp != null && rsp.getResultCode() == 0;

	}

	private boolean notifyDeliver(DeliverReq req)
	{
		DeliverRsp rsp = null;
		try
		{
			log.debug("notify webservice deliver msg");
			SmsEngineSoapBindingStub stub = new SmsEngineSoapBindingStub(
				new URL(URL), null);
			stub.setTimeout(TIMEOUT);
			rsp = stub.deliver(req);
		}
		catch (MalformedURLException ex)
		{
			log.error(null, ex);
		}
		catch (RemoteException ex)
		{
			log.error(null, ex);
		}
		return rsp != null && rsp.getResultCode() == 0;
	}

	public void myStop()
	{
		this.stop = true;
	}

	public static void notifyReport(String sendid, String mobile, int result,
			String resultMessage, String reportTime)
	{
		ReportReq req = new ReportReq();
		req.setSendid(sendid);
		req.setMobile(mobile);
		req.setResult(result);
		req.setResultMessage(resultMessage);
		req.setReportTime(reportTime);

		if (getInstance().buffer.offer(req))
		{
			synchronized (getInstance())
			{
				getInstance().notify();
			}
			log.debug("ReportReq入队成功等待通知");
		}
		else
		{
			log.debug("ReportReq入队失败");
		}
	}

	public static void notifyDeliver(String sendid, String msgContent,
			String recipient)
	{
		DeliverReq req = new DeliverReq();
		req.setSendid(sendid);
		req.setMsgContent(msgContent);
		req.setRecipient(recipient);
		if (getInstance().buffer.offer(req))
		{
			synchronized (getInstance())
			{
				getInstance().notify();
			}
			log.debug("DeliverReq入队成功等待通知");
		}
		else
		{
			log.debug("DeliverReq入队成功等待通知");
		}

	}

	public static void main(String[] args)
	{
		// NotifyThread client = new NotifyThread();
		// notifyDeliver("5d9ee15e-fb8f-49cf-a397-adde8e4999ae");
		NotifyThread.URL = "http://localhost/ws/services/SmsEngine";
		notifyReport("34c53ce1-5656-4e78-b16d-93ffe84e9445", "1377802386", 0,
			"1000", DateUtils.getTimestamp14());
		// getInstance().myStop();
	}
}
