package com.chinamobile.cmpp2_0.protocol;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.chinamobile.cmpp2_0.protocol.message.APackage;
import com.chinamobile.cmpp2_0.protocol.message.ActiveTestMessage;
import com.chinamobile.cmpp2_0.protocol.message.ActiveTestRespMessage;
import com.chinamobile.cmpp2_0.protocol.message.BasePackage;
import com.chinamobile.cmpp2_0.protocol.message.CommandID;
import com.chinamobile.cmpp2_0.protocol.message.ConnectRespMessage;
import com.chinamobile.cmpp2_0.protocol.message.DeliverMessage;
import com.chinamobile.cmpp2_0.protocol.message.DeliverRespMessage;
import com.chinamobile.cmpp2_0.protocol.message.SubmitMessage;
import com.chinamobile.cmpp2_0.protocol.message.SubmitRespMessage;
import com.chinamobile.cmpp2_0.protocol.message.TerminateMessage;
import com.chinamobile.cmpp2_0.protocol.message.TerminateRespMessage;
import com.chinamobile.cmpp2_0.protocol.message.bean.Deliver;
import com.chinamobile.cmpp2_0.protocol.util.Constants;
import com.chinamobile.cmpp2_0.protocol.util.DateUtil;
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

	private static final List<SubmitMessage> SLIDE = new LinkedList<SubmitMessage>();// 滑动窗口
	private static final Object lock = new Object();

	private volatile boolean stop = false;

	public PReceiver()
	{

	}

	@Override
	public void run()
	{
		while (!stop)
		{
			PChannel channel = PChannel.getChannel();
			if (channel == null)
			{
				log.info("通道未建立，等待通道被建立...");
				try
				{
					TimeUnit.SECONDS.sleep(1);// 睡眠1秒钟
				}
				catch (InterruptedException e)
				{
					// TODO Auto-generated catch block
					log.error(null, e);
				}
				continue;

			}
			BasePackage p = null;
			try
			{
				p = channel.readPacket();
				if (p == null)
				{
					continue;
				}
				dealRecv(p);

			}
			catch (Exception e)
			{
				// TODO Auto-generated catch block
				log.error(null, e);
				channel.close();
			}

		}
	}

	private boolean dealRecv(BasePackage bp) throws InterruptedException,
			IOException
	{
		log.info("test PReceiver begin");

		APackage ap = this.makePackage(bp);

		// 对收到的包记录二进制信息
		if (log.isInfoEnabled())
		{
			log.info("收到包(" + DateUtil.getTimeString(bp.getTimeStamp()) + "):"
					+ ap.getHead().getCommandIdString() + " "
					+ Hex.rhex(ap.getBytes()));
			log.info(ap);
		}

		PChannel channel = PChannel.getChannel();
		if (channel == null)
		{
			return false;
		}

		if (ap instanceof ActiveTestMessage)
		{
			ActiveTestRespMessage atrm = new ActiveTestRespMessage(
					(ActiveTestMessage) ap);
			// 发送ActiveTestRespMessage
			channel.sendPacket(atrm);

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
			channel.sendPacket(trm);
			channel.close();

		}
		else if (ap instanceof TerminateRespMessage)
		{
			channel.close();
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
			channel.sendPacket(drm);
			// 处理deliver消息
			dealDeliver((DeliverMessage) ap);

		}

		log.info("test PReceiver end");
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
		if (deliver.getRegisteredDelivery() == 1) // 这里处理状态报告
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
					+ srm.getHead().getSequenceId() + "相对应的SubmitMessage包"));
			return false;
		}

	}

	private SubmitMessage checkPackage(SubmitRespMessage ap)
	{
		SubmitMessage p = null;
		if (log.isDebugEnabled())
		{

			log.debug("checkPackage处理包【SubmitRespMessage(" + ap.getSequenceID()
					+ ")】");

		}
		p = checkInSlide(ap);

		if (log.isDebugEnabled())
		{
			if (p != null)
			{
				log.debug("checkPackage找到了包【SubmitMessage("
						+ p.getHead().getSequenceId() + ")】");
			}
			else
			{
				log.debug("checkPackage没找到【SubmitRespMessage("
						+ ap.getSequenceID() + ")】的对应包");
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
	private SubmitMessage checkInSlide(SubmitRespMessage submitResp)
	{
		synchronized (lock)
		{
			long curtime = System.currentTimeMillis();

			// 先在滑动窗口找，如果没找到，那么再needRespQue队列找
			synchronized (SLIDE)
			{
				Iterator<SubmitMessage> it = SLIDE.iterator();
				while (it.hasNext())
				{
					SubmitMessage p = it.next();
					if (p.getHead().getSequenceId() == submitResp.getHead()
							.getSequenceId())
					{
						it.remove();
						if (log.isDebugEnabled())
						{
							log.debug("从滑动窗口中取出包SubmitMessage("
									+ p.getHead().getSequenceId()
									+ ")，当前滑动窗口长度：" + SLIDE.size());

						}
						return p;
					}
					// 如果找到的包超时了，没有回应，需要重发,超时时间一般设置为1分钟
					if ((curtime - p.getTimeStamp()) > Constants.RESP_TIME)
					{
						it.remove();
						// 重发次数一般设置为3
						if (p.getTryTimes() < Constants.RESEND_TIME)
						{
							// 重发
							log.info("该包超时未收到回应，需要重发" + p);
							p.addTimes();
							PChannel channel = PChannel.getChannel();
							if (channel != null)
							{
								try
								{
									channel.sendPacket(p);
								}
								catch (Exception e)
								{
									// TODO Auto-generated catch block
									log.info(null, e);
								}
							}

						}
						else
						{
							// 丢弃
							log.info("重发次数过多 丢弃");
							if (discard.isInfoEnabled())
							{
								discard.info(p);
							}
						}

					}
				}
			}

			// 如果还未找到，那么从NeedResp队列中找
			SubmitMessage p = checkInNeedRespQue(submitResp);
			if (p != null)
			{
				return p;
			}
			return null;
		}

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
			if (sm.getHead().getSequenceId() == srm.getHead().getSequenceId())
			{
				return sm;
			}
			// 如果找到的包不对，则加入到滑动窗口中
			if (log.isDebugEnabled())
			{
				log.debug("在NeedRespQue队列中找到包【SubmitMessage("
						+ sm.getHead().getSequenceId() + ")】,入滑动窗口，滑动窗口加1");
			}
			synchronized (SLIDE)
			{
				SLIDE.add(sm);
			}

			// 发送包的发送时间大于回应包的接收时间则表明NeedResp里面不可能找到
			if (sm.getTimeStamp() > srm.getTimeStamp())
			{
				if (log.isDebugEnabled())
				{
					log.debug("在NeedRespQue队列中找不到(原因是当前队列中所有包都是在之后发送的)");
				}
				return null;
			}
			sm = que.poll();

		}
		if (log.isDebugEnabled())
		{
			log.debug("在NeedRespQue队列中找不到(原因是NeedRespQue队列为空)");
		}
		return null;
	}

	/**
	 * 根据消息类型组装收到的消息
	 * 
	 * @param bp
	 * @return
	 */
	private APackage makePackage(BasePackage bp)
	{

		int commandId = bp.getHead().getCommmandId();

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

	/**
	 * 默认不实现该方法，该方法由业务子类实现
	 * 
	 * @param dm
	 */
	public void doDeliver(DeliverMessage dm)
	{
		log.debug("进入doDeliver方法");
	}

	/**
	 * 默认不实现该方法，该方法由业务子类实现
	 * 
	 * @param dm
	 */
	public void doReport(DeliverMessage dm)
	{
		log.debug("进入doReport方法");
	}

	/**
	 * 默认不实现该方法，该方法由业务子类实现
	 * 
	 * @param sm
	 * @param srm
	 */
	public void doSubmitResp(SubmitMessage sm, SubmitRespMessage srm)
	{
		log.debug("进入doSubmitResp方法");
	}

	public void myStop()
	{
		stop = true;
	}

}
