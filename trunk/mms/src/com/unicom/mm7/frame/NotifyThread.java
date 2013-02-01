package com.unicom.mm7.frame;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.tourzj.mms.MmsEngineSoapBindingStub;
import com.tourzj.mms.req.DeliverReportReq;
import com.tourzj.mms.req.DeliverReq;
import com.tourzj.mms.req.Request;
import com.tourzj.mms.rsp.DeliverReportRsp;
import com.tourzj.mms.rsp.DeliverRsp;

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
	static String URL = "http://localhost/ws/services/MmsEngine";

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
				if (req instanceof DeliverReportReq)
				{
					DeliverReportReq deliverReportReq = (DeliverReportReq) req;
					if (!notifyDeliverReport(deliverReportReq))
					{
						// 通知失败，写日志
						resultLog.info("sendid:" + deliverReportReq.getSendId()
								+ "|mobile:" + deliverReportReq.getMobile()
								+ "|result:" + deliverReportReq.getResult()
								+ "|resultMessage:"
								+ deliverReportReq.getResultMessage()
								+ "|reportTime:"
								+ deliverReportReq.getReportTime());
					}
				}
				else if (req instanceof DeliverReq)
				{
					DeliverReq deliverReq = (DeliverReq) req;
					if (!notifyDeliver(deliverReq))
					{
						resultLog.info("deliver:" + deliverReq.getSendId());
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

	private boolean notifyDeliverReport(DeliverReportReq req)
	{
		DeliverReportRsp rsp = null;
		try
		{
			log.debug("DeliverReport notify");
			MmsEngineSoapBindingStub stub = new MmsEngineSoapBindingStub(
					new URL(URL), null);
			stub.setTimeout(TIMEOUT);
			rsp = stub.deliverReport(req);
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
			log.debug("Deliver notify");
			MmsEngineSoapBindingStub stub = new MmsEngineSoapBindingStub(
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

	public static void notifyDeliverReport(String sendid, String mobile,
			int result, String resultMessage, String reportTime)
	{
		DeliverReportReq req = new DeliverReportReq();
		req.setSendId(sendid);
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
			log.debug("DeliverReportReq入队成功等待通知");
		}
		else
		{
			log.debug("DeliverReportReq入队失败");
		}
	}

	public static void notifyDeliver(String sendid)
	{
		DeliverReq req = new DeliverReq();
		req.setSendId(sendid);
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
			log.debug("DeliverReq入队失败");
		}

	}

	public static void main(String[] args)
	{
		// NotifyThread client = new NotifyThread();
		// notifyDeliver("5d9ee15e-fb8f-49cf-a397-adde8e4999ae");
		NotifyThread.URL = "http://60.191.70.231/ws/services/MmsEngine";
		for (int i = 0; i < 10; i++)
		{
			// notifyDeliver("5d9ee15e-fb8f-49cf-a397-adde8e4999ae");
			notifyDeliverReport("34c53ce1-5656-4e78-b16d-93ffe84e9445",
					"1377802386", 0, "1000", com.unicom.mm7.util.DateUtils
							.getTimestamp14());
		}
		while (getInstance().buffer.size() != 0)
		{
			try
			{
				Thread.sleep(10);
			}
			catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try
		{
			Thread.sleep(10000);
		}
		catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		notifyDeliverReport("34c53ce1-5656-4e78-b16d-93ffe84e9445",
				"1377802386", 0, "1000", com.unicom.mm7.util.DateUtils
						.getTimestamp14());
		// getInstance().myStop();
	}
}
