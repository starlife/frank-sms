package com.chinamobile.cmpp2_0.protocol;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.chinamobile.cmpp2_0.protocol.message.APackage;
import com.chinamobile.cmpp2_0.protocol.message.ActiveTestMessage;

/**
 * 取得消息并提交给消息发送队列 该类需要定义几个给子类继承的方法： doSubmit();
 * 
 * @author Administrator
 */
public class PSender extends Thread
{
	public static final long HEARTBEAT_TIME = 5000;// 发送心跳包频率（ms）

	private static final Log log = LogFactory.getLog(PSender.class);// 记录日志

	private volatile boolean stop = false;

	private static final Object lock = new Object();

	private volatile long lastActiveTime;// 上一次链路使用时间

	/* 连接ismg网关的参数 */
	private String ip;
	private int port;
	private String loginname;
	private String loginpass;
	private int version;

	public PSender(String ip, int port, String loginname, String loginpass,
			int version)
	{
		this.ip = ip;
		this.port = port;
		this.loginname = loginname;
		this.loginpass = loginpass;
		this.version = version;
	}

	@Override
	public void run()
	{
		while (!stop)
		{
			try
			{
				PChannel channel = PChannel.getChannel();
				synchronized (lock)
				{
					if (channel == null)
					{
						log.info("通道未初始化，初始化通道...");
						PChannel.init(ip, port, loginname, loginpass, version);
						channel = PChannel.getChannel();

					}
				}

				try
				{
					// 取得消息并通过通道发送
					APackage pack = this.doSubmit();
					if (pack == null)
					{
						long curTime = System.currentTimeMillis();
						if (curTime < (lastActiveTime + HEARTBEAT_TIME))
						{
							continue;
						}
						log.info("链路空闲超过" + HEARTBEAT_TIME + "毫秒,发送链路检测包");
						pack = new ActiveTestMessage();

					}
					if (pack != null)
					{
						lastActiveTime = System.currentTimeMillis();
						channel.sendPacket(pack);
					}
				}
				catch (Exception ex)
				{
					log.error(null, ex);
					channel.close();
				}

			}
			catch (Exception ex)
			{
				log.error(null, ex);
			}

		}

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

}
