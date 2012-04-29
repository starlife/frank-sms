package com.chinamobile.cmpp3_0.protocol;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.chinamobile.cmpp3_0.protocol.message.APackage;
import com.chinamobile.cmpp3_0.protocol.message.ActiveTestMessage;
import com.chinamobile.cmpp3_0.protocol.message.ActiveTestRespMessage;
import com.chinamobile.cmpp3_0.protocol.message.BasePackage;
import com.chinamobile.cmpp3_0.protocol.message.CommandID;
import com.chinamobile.cmpp3_0.protocol.message.ConnectRespMessage;
import com.chinamobile.cmpp3_0.protocol.message.DeliverMessage;
import com.chinamobile.cmpp3_0.protocol.message.DeliverRespMessage;
import com.chinamobile.cmpp3_0.protocol.message.SubmitMessage;
import com.chinamobile.cmpp3_0.protocol.message.SubmitRespMessage;
import com.chinamobile.cmpp3_0.protocol.message.TerminateMessage;
import com.chinamobile.cmpp3_0.protocol.message.TerminateRespMessage;
import com.chinamobile.cmpp3_0.protocol.message.bean.Deliver;
import com.chinamobile.cmpp2_0.protocol.util.Hex;

/**
 * 取消息接受队列中的消息并加于处理 该类需要定义几个给子类继承的方法： doDeliver(); doReport();
 * 
 * @author Administrator
 */
public class PReceiver extends Thread
{

	private static final Log log = LogFactory.getLog(PReceiver.class);// 记录日志

	private static final Log discard = LogFactory.getLog("discard");// 记录丢弃包日志

	private volatile boolean stop = false;

	private final LinkedBlockingQueue<SubmitMessage> submitQue = new LinkedBlockingQueue<SubmitMessage>();// 滑动窗口

	public static final long RESP_TIME = 60 * 1000;// 包等待确认时间 超过了重发
	public static final int RESEND_TIME = 3;// 重发次数

	public PReceiver()
	{
		
	}

	@Override
	public void run()
	{
		while (!stop)
		{
			try
			{
				PChannel channel = PChannel.getChannel();
				if (channel == null)
				{
					log.info("通道未建立，等待通道被建立...");
					TimeUnit.SECONDS.sleep(1);// 睡眠1秒钟
					continue;

				}
				APackage p = channel.receive();
				if (p == null)
				{
					continue;
				}
				dealRecv(p);

			}
			catch (Exception ex)
			{
				ex.printStackTrace();
				log.error(null, ex);
			}
		}
	}

	/**
	 * 根据消息类型组装收到的消息
	 * 
	 * @param bp
	 * @return
	 */
	private APackage makePackage(BasePackage bp)
	{

		int commandId = bp.getHead().getCommmandID();

		switch (commandId)
		{
			case CommandID.CMPP_ACTIVE_TEST:
				// sendActiveResp;
				ActiveTestMessage atm = new ActiveTestMessage(bp);
				return atm;
			case CommandID.CMPP_ACTIVE_TEST_RESP:
				ActiveTestRespMessage atrm = new ActiveTestRespMessage(bp);
				return atrm;
			case CommandID.CMPP_TERMINATE:
				TerminateMessage tm = new TerminateMessage(bp);
				return tm;
			case CommandID.CMPP_TERMINATE_RESP:
				TerminateRespMessage trm = new TerminateRespMessage(bp);
				return trm;

			case CommandID.CMPP_CONNECT_RESP: // Login_Resp
				ConnectRespMessage lrm = new ConnectRespMessage(bp);
				return lrm;
			case CommandID.CMPP_SUBMIT_RESP: // Submit_Resp
				SubmitRespMessage srm = new SubmitRespMessage(bp);
				return srm;

			case CommandID.CMPP_DELIVER: // Deliver
				DeliverMessage dm = new DeliverMessage(bp);
				return dm;
			default:
				return bp;
		}

	}

	private boolean dealRecv(APackage recv) throws InterruptedException
	{

		if (!(recv instanceof BasePackage))
		{
			log.error("收到异常包");
			return false;
		}
		BasePackage bp = (BasePackage) recv;

		APackage ap = this.makePackage(bp);

		// 对收到的包记录二进制信息
		if (log.isDebugEnabled())
		{
			log.debug("收到包：" + ap.getHead() + " " + Hex.rhex(ap.getBytes()));
		}

		// 打印日志
		log.info(String.format("收到包：%s", ap.getHead().getCommandIdString()));
		log.info(ap);

		PChannel channel = PChannel.getChannel();

		if (ap instanceof ActiveTestMessage)
		{
			ActiveTestRespMessage atrm = new ActiveTestRespMessage(
					(ActiveTestMessage) ap);
			// 发送ActiveTestRespMessage
			if (channel != null)
			{
				channel.send(atrm);

			}
		}
		else if (ap instanceof ActiveTestRespMessage)
		{
			;
		}
		else if (ap instanceof TerminateMessage)
		{
			TerminateRespMessage trm = new TerminateRespMessage(
					(TerminateMessage) ap);
			// 发送TerminateRespMessage
			if (channel != null)
			{
				channel.send(trm);
				channel.close();

			}

		}
		else if (ap instanceof TerminateRespMessage)
		{
			if (channel != null)
			{
				channel.close();

			}

		}
		else if (ap instanceof ConnectRespMessage)
		{
			;

		}
		else if (ap instanceof SubmitRespMessage)
		{
			SubmitRespMessage srm = (SubmitRespMessage) ap;
			dealSubmitResp(srm);

		}
		else if (ap instanceof DeliverMessage)
		{
			DeliverRespMessage drm = new DeliverRespMessage((DeliverMessage) ap);
			// 发送DeliverRespMessage
			if (channel != null)
			{
				channel.send(drm);

			}
			// 处理deliver消息
			dealDeliver((DeliverMessage) ap);

		}

		return true;
	}

	/**
	 * 处理Deliver消息
	 * 
	 * @param dm
	 */
	private void dealDeliver(DeliverMessage dm)
	{
		Deliver deliver = dm.getDeliver();
		if (deliver.RegisteredDelivery == 1) // 这里处理状态报告
		{
			doReport(dm);
		}
		else
		{
			doDeliver(dm);

		}

	}

	/**
	 * 处理收到的Submit_Resp包
	 * 
	 * @param pack
	 * @return
	 */
	private boolean dealSubmitResp(SubmitRespMessage srm)
	{
		SubmitMessage p = this.checkPackage(srm);// 得到和该Submit_resp相对应的Submit包

		if (p != null)
		{
			doSubmitResp(p, srm);
			return true;

		}
		else
		{

			log.error(String.format("找不到SequnceID="
					+ srm.getHead().getSequenceID() + "相对应的SubmitMessage包"));
			return false;
		}

	}

	public SubmitMessage checkPackage(SubmitRespMessage ap)
	{
		SubmitMessage p = null;
		// 找五次，如果还找不到，那么返回
		for (int i = 0; i < 5; i++)
		{
			p = checkInSubmitQue(ap);
			if (p != null)
			{
				break;
			}
			try
			{
				TimeUnit.SECONDS.sleep(1);
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
				log.error(null, ex);
			}
		}
		return p;
	}

	/**
	 * 从滑动窗口中寻找SubmitResp对应的Submit包
	 * 
	 * @param submitResp
	 * @return
	 */
	private synchronized SubmitMessage checkInSubmitQue(
			SubmitRespMessage submitResp)
	{
		long curtime = System.currentTimeMillis();

		SubmitMessage p = this.submitQue.poll();

		while (p != null)
		{
			// 如果找到对应的包，则返回
			if (p.getHead().getSequenceID() == submitResp.getHead()
					.getSequenceID())
			{
				return p;

			}
			// 如果找到的包超时了，没有回应，需要重发,超时时间一般设置为1分钟
			if (p.getTimeStamp() + RESP_TIME < curtime)
			{
				// 重发次数一般设置为3
				if (p.getTryTimes() < RESEND_TIME)
				{
					// 重发
					p.addTimes();
					PChannel channel = PChannel.getChannel();
					if (channel != null)
					{
						channel.send(p);
					}

				}
				else
				{
					// 丢弃
					System.out.println("重发次数过多 丢弃");
					if (discard.isInfoEnabled())
					{
						discard.info(p);
					}
				}

			}
			p = this.submitQue.poll();
		}
		// 如果还未找到，那么从NeedResp队列中找
		p = checkInNeedRespQue(submitResp);
		if (p != null)
		{
			return p;
		}
		return null;

	}

	/**
	 * 如果包在滑动窗口中没找到，那么去NeedResp队列找 如果还找不到，返回null
	 * 
	 * @param srm
	 *            SubmitRespMessage
	 * @return
	 */
	private SubmitMessage checkInNeedRespQue(SubmitRespMessage srm)
	{
		PChannel channel = PChannel.getChannel();
		if (channel == null)
		{
			return null;
		}
		LinkedBlockingQueue<SubmitMessage> que = channel.getNeedRespQue();

		SubmitMessage sm = que.poll();
		while (sm != null)
		{
			// 找到，返回
			if (sm.getHead().getSequenceID() == srm.getHead().getSequenceID())
			{
				return sm;
			}
			// 如果找到的包不对，则加入到滑动窗口中
			try
			{
				this.submitQue.put(sm);
			}
			catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				log.error(null, e);
			}
			// 发送包的发送时间大于回应包的接收时间则表明NeedResp里面不可能找到
			if (sm.getTimeStamp() > srm.getTimeStamp())
			{
				return null;
			}
			sm = que.poll();

		}
		return null;
	}

	/**
	 * 默认不实现该方法，该方法由业务子类实现
	 * 
	 * @param dm
	 */
	public void doDeliver(DeliverMessage dm)
	{

	}

	/**
	 * 默认不实现该方法，该方法由业务子类实现
	 * 
	 * @param dm
	 */
	public void doReport(DeliverMessage dm)
	{

	}

	/**
	 * 默认不实现该方法，该方法由业务子类实现
	 * 
	 * @param sm
	 * @param srm
	 */
	public void doSubmitResp(SubmitMessage sm, SubmitRespMessage srm)
	{

	}

	public void myStop()
	{
		stop = true;
	}

	public static void main(String[] args)
	{
		String pack = "0000007700000003006c36be5710610622091487442100003230313030363232303931343535383631333337353831313331360000000000000000313036323131353100000000000000000000000000063630313332380000000000000000000300143036323230393134353830313030383034373039";
		BasePackage bp = new BasePackage(Hex.rstr(pack));
		DeliverMessage dm = new DeliverMessage(bp);
		PReceiver thread = new PReceiver();
		thread.start();
		thread.dealDeliver(dm);
	}

}
