package com.chinaunicom.sgip1_2.protocol;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.chinaunicom.sgip1_2.protocol.message.APackage;
import com.chinaunicom.sgip1_2.protocol.message.SubmitMessage;
import com.chinaunicom.sgip1_2.protocol.message.SubmitRespMessage;
import com.chinaunicom.sgip1_2.protocol.util.Constants;
import com.chinaunicom.sgip1_2.protocol.util.Hex;
import com.chinaunicom.sgip1_2.protocol.util.MessageUtil;
import com.chinaunicom.sgip1_2.protocol.util.TpsTool;

/**
 * 取得消息并提交给消息发送队列 该类需要定义几个给子类继承的方法： doSubmit();
 * 
 * @author Administrator
 */
public class PSender extends Thread implements AbstractSender
{

	private static final Log log = LogFactory.getLog(PSender.class);// 记录日志

	private static final Log discard = LogFactory.getLog("discard");// 记录日志

	/**
	 * 链路超时时间
	 */
	private int timeout = Constants.TIMEOUT;

	/**
	 * 发送重试次数
	 */
	private int retryTimes = Constants.RESEND_TIME;

	/**
	 * 保存发送提交失败的包，便于重新发送
	 */
	private final LinkedBlockingQueue<APackage> buffer = new LinkedBlockingQueue<APackage>(
		10000);

	private volatile boolean stop = false;

	private volatile long lastActiveTime = System.currentTimeMillis();// 上一次链路使用时间

	private PChannel channel;

	public PSender(String ip, int port, String nodeid, int loginType,
			String loginname, String loginpass, int timeout, int retryTimes)
	{
		channel = new PChannel(ip, port, nodeid, loginType, loginname,
			loginpass, timeout);
		this.timeout = timeout;
		this.retryTimes = retryTimes;
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
				TpsTool.limitTPS();
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
					if (curTime > (lastActiveTime + timeout))
					{
						if (channel.isLogin())
						{
							// 链路已经空闲超过60s，且连接还未断开，需要发送UnbindMessage包
							log.info("链路空闲超过" + timeout
									+ "ms，发送UnbindMessage包断开连接");
							channel.unbind();
						}

					}

				}
				if (pack == null)
				{
					continue;
				}
				// 设置发送时间
				lastActiveTime = System.currentTimeMillis();
				try
				{
					boolean flag = sendPacket(pack);
					if (!flag)
					{
						buffer.offer(pack);
					}
				}
				catch (IOException ex)
				{
					// 发送失败重发
					log.error(null, ex);
					if (pack.getTryTimes() < retryTimes)
					{
						buffer.offer(pack);
					}
					else
					{
						// 记录丢失的包到文件中
						discard.info("丢失包，重试多次失败"
								+ pack.getHead().getCommandIdString() + ",字节码:"
								+ Hex.rhex(pack.getBytes()));
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
		log.info("发送SubmitMessage包：" + pack);
		APackage recv = channel.send(pack);
		log.info("收到SubmitRespMessage包：" + recv);
		if (recv == null)
		{
			return false;
		}
		// 对收到的包记录二进制信息

		if ((pack instanceof SubmitMessage)
				&& (recv instanceof SubmitRespMessage))
		{
			doSubmitResp((SubmitMessage) pack, (SubmitRespMessage) recv);
			return true;
		}
		else
		{
			log.error("发送包和接受包都有误：" + pack + recv);
			return false;
		}

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

	public static void main(String[] args) throws IOException
	{
		String ip = "211.90.245.97";
		int port = 8801;
		int loginType = 1;
		String loginname = "106550577371";
		String loginpass = "123456";
		String nodeid = "61153";
		int timeout = 60000;
		int retryTimes = 3;
		//
		String spid = "61153";
		String spnumber = "106550577371";
		String serviceCode = "1";
		String[] desttermid = "13003664740".split(",");
		String message = "测试消息";
		String param = "";
		PSender sender = new PSender(ip, port, nodeid, loginType, loginname,
			loginpass, timeout, retryTimes);
		SubmitMessage[] msgs = MessageUtil.createSubmitMessage(nodeid, spid,
			spnumber, serviceCode, desttermid, message, param);
		for (SubmitMessage pack : msgs)
		{
			sender.sendPacket(pack);
		}
	}

}
