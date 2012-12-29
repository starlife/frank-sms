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
 * ����֪ͨ���ܻ����Ϊ�˲�Ӱ��Ч����Ҫ���߳�+������е���ʽ ���֪ͨʧ�ܣ���ôд��־
 * 
 * @author Administrator
 */
public class NotifyThread extends Thread
{
	/**
	 * ֪ͨURL
	 */
	static String URL = "http://localhost/ws/services/MmsEngine";

	/**
	 * ��ʱʱ��Ĭ��Ϊ1s
	 */
	static int TIMEOUT = 1000;

	private static final Log log = LogFactory.getLog(NotifyThread.class);

	/**
	 * ֪ͨʧ��д��־
	 */
	private static final Log resultLog = LogFactory.getLog("result");

	/**
	 * ֪ͨ��Ϣ�������
	 */
	private final LinkedBlockingQueue<Request> buffer = new LinkedBlockingQueue<Request>();

	/**
	 * ��ʵ������
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
		log.info("NotifyThread �߳̿���");
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
						// ֪ͨʧ�ܣ�д��־
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
		log.info("NotifyThread �̹߳ر�");
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
			log.debug("DeliverReportReq��ӳɹ��ȴ�֪ͨ");
		}
		else
		{
			log.debug("DeliverReportReq���ʧ��");
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
			log.debug("DeliverReq��ӳɹ��ȴ�֪ͨ");
		}
		else
		{
			log.debug("DeliverReq���ʧ��");
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
