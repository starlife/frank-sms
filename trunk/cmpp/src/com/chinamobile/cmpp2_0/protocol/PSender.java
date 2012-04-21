package com.chinamobile.cmpp2_0.protocol;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.chinamobile.cmpp2_0.protocol.message.APackage;
import com.chinamobile.cmpp2_0.protocol.message.ActiveTestMessage;
import com.chinamobile.cmpp2_0.protocol.util.Hex;

/**
 * 取得消息并提交给消息发送队列 该类需要定义几个给子类继承的方法： doSubmit();
 * 
 * @author Administrator
 */
public class PSender extends Thread
{
	public static final long HEARTBEAT_TIME = 5000;// 发送心跳包频率（ms）

	private static final Log log = LogFactory.getLog(PSender.class);// 记录日志

	private static final Log lose = LogFactory.getLog("lose");// 记录日志

	// 保存发送提交失败的包，便于重新发送
	private final LinkedBlockingQueue<APackage> buffer = new LinkedBlockingQueue<APackage>(
			10000);

	private volatile boolean stop = false;

	private static final Object lock = new Object();

	private volatile long lastActiveTime = System.currentTimeMillis();// 上一次链路使用时间

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
				// 初始化通道
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

				// 业务逻辑
				try
				{
					// 如果通道不可用，一直等到通道可用
					while (!channel.isLogin())
					{
						try
						{
							log.info("通道不可用，发送线程等待通道变的可用...");
							TimeUnit.SECONDS.sleep(1);
						}
						catch (Exception ex)
						{
							log.error(null, ex);
						}
					}
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
						if (curTime > (lastActiveTime + HEARTBEAT_TIME))
						{
							log.info("链路空闲,发送链路检测包...");
							pack = new ActiveTestMessage();
						}

					}
					if (pack == null)
					{
						continue;
					}
					lastActiveTime = System.currentTimeMillis();
					boolean flag = channel.sendPacket(pack);
					if (!flag)
					{
						if (!buffer.offer(pack))
							;
						{
							// 记录丢失的包到文件中
							lose.info("丢失包"
									+ pack.getHead().getCommandIdString()
									+ ",字节码:" + Hex.rhex(pack.getBytes()));
						}
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