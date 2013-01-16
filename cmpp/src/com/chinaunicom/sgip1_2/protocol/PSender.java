package com.chinaunicom.sgip1_2.protocol;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.chinaunicom.sgip1_2.protocol.message.APackage;
import com.chinaunicom.sgip1_2.protocol.message.BasePackage;
import com.chinaunicom.sgip1_2.protocol.message.CommandID;
import com.chinaunicom.sgip1_2.protocol.message.SubmitMessage;
import com.chinaunicom.sgip1_2.protocol.message.SubmitRespMessage;
import com.chinaunicom.sgip1_2.protocol.util.Constants;
import com.chinaunicom.sgip1_2.protocol.util.DateUtil;
import com.chinaunicom.sgip1_2.protocol.util.Hex;
import com.chinaunicom.sgip1_2.protocol.util.RateControl;

/**
 * 取得消息并提交给消息发送队列 该类需要定义几个给子类继承的方法： doSubmit();
 * 
 * @author Administrator
 */
public class PSender extends Thread implements AbstractSender
{

	private static final Log log = LogFactory.getLog(PSender.class);// 记录日志

	private static final Log lose = LogFactory.getLog("lose");// 记录日志

	/**
	 * 保存发送提交失败的包，便于重新发送
	 */
	private final LinkedBlockingQueue<APackage> buffer = new LinkedBlockingQueue<APackage>(
			10000);

	private volatile boolean stop = false;

	private volatile long lastActiveTime = System.currentTimeMillis();// 上一次链路使用时间

	private PChannel channel;

	public PSender(String ip, int port, String nodeid, int loginType,
			String loginname, String loginpass)
	{
		channel = new PChannel(ip, port, nodeid, loginType, loginname,
				loginpass);
	}

	@Override
	public void run()
	{
		while (!stop)
		{
			// 业务逻辑
			try
			{
				// 这里做流量控制
				RateControl.controlRate();
				// 取包发送
				APackage pack = this.doSubmit();
				if (pack == null)
				{
					pack = buffer.poll();
				}
				if (pack == null)
				{
					// 确认是否需要发送链路检测包
					long curTime = System.currentTimeMillis();
					if (curTime > (lastActiveTime + Constants.TIMEOUT))
					{
						// 链路已经空闲超过60s，且连接还未断开，需要发送UnbindMessage包
						log.info("链路空闲超过60s，发送UnbindMessage包断开连接");
						channel.unbind();

					}

				}
				if (pack == null)
				{
					continue;
				}
				// 设置发送时间
				lastActiveTime = System.currentTimeMillis();
				boolean flag = sendPacket(pack);
				if (!flag)
				{
					if (!buffer.offer(pack))
					{
						// 记录丢失的包到文件中
						lose.info("丢失包" + pack.getHead().getCommandIdString()
								+ ",字节码:" + Hex.rhex(pack.getBytes()));
					}
				}

			}
			catch (Exception ex)
			{
				log.error(null, ex);
				// 发生异常，强制关闭连接
				channel.close();
			}

		}

	}

	/**
	 * 发送包，该方法是同步的
	 * 
	 * @param pack
	 *            可能是submit报或者unbind包 包
	 * @return 如果通道不可用 返回false 发送成功返回true
	 * @throws IOException
	 * @throws IOException
	 */
	private boolean sendPacket(APackage pack) throws IOException
	{

		BasePackage recv = channel.send(pack);
		if (recv == null)
		{
			return false;
		}
		// 对收到的包记录二进制信息
		if (log.isInfoEnabled())
		{
			log.info("收到包(" + DateUtil.getTimeString(recv.getTimeStamp())
					+ "):" + recv.getHead().getCommandIdString() + " "
					+ Hex.rhex(recv.getBytes()));
			log.info(recv);
		}

		if (pack instanceof SubmitMessage)
		{
			if (recv.getHead().getCommmandId() == CommandID.SGIP_SUBMIT_RESP)
			{
				SubmitRespMessage srm = new SubmitRespMessage(recv);
				doSubmitResp((SubmitMessage) pack, srm);
				return true;
			}
			else
			{
				log.error("收到的包并不是SubmitRespMessage包，" + recv);
			}

		}

		return false;

	}

	/**
	 * 把Submit消息入库，这里支持群发的处理
	 */
	public void doSubmitResp(SubmitMessage sm, SubmitRespMessage srm)
	{
		log.debug("进入doSubmitResp方法");
	}

	/**
	 * 该方法提供给运用程序继承
	 * 
	 * @return
	 */
	public APackage doSubmit()
	{
		return null;
	}

	public void myStop()
	{
		stop = true;
	}

	public static void main(String[] args)
	{
		String ip = "211.140.12.45";
		int port = 8801;
		int loginType = 1;
		String loginname = "Q61704";
		String loginpass = "kki890";
		String nodeid = "123444";
		PSender sender=new PSender(ip, port, nodeid, loginType, loginname, loginpass);
	}

}
