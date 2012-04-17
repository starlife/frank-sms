package com.chinamobile.cmpp2_0.protocol;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.chinamobile.cmpp2_0.protocol.message.APackage;
import com.chinamobile.cmpp3_0.protocol.message.MessageUtil;

/**
 * 取得消息并提交给消息发送队列
 * 该类需要定义几个给子类继承的方法：
 * 	doSubmit();
 * @author Administrator
 */
public class PSender extends Thread
{
	private static final Log log=LogFactory.getLog(PReceiver.class);//记录日志
	
	private volatile boolean stop = false;
	
	private static final Object lock=new Object();
	
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
		//this.start();//启动线程
	}

	@Override
	public void run()
	{
		while (!stop)
		{
			// 取得消息并发送给提交给消息发送队列
			try
			{
				PChannel channel=PChannel.getChannel();
				synchronized (lock)
				{
					if(channel==null)
					{
						log.info("通道未初始化，初始化通道...");
						PChannel.init(ip, port, loginname, loginpass, version);
						channel=PChannel.getChannel();
						
					}
				}				
				
				APackage pack = this.doSubmit();
				if (pack != null)
				{
					//channel.send(pack);
					System.out.println("###########");
					
					
				}
				

			}
			catch (Exception ex)
			{
				ex.printStackTrace();
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
	
	public static void main(String[] args)
	{
		
		//PChannel.getChannel();
		//String ip="211.141.90.122";
		//int port=7890;
		//String loginname="914071";
		//String loginpass="yjxt";
		//int version=48;//cmpp 3.0
		String ip="211.140.12.45";
		int port=7890;
		String loginName="Q61704";
		String secret ="kki890";
		//String secret="Q61704";
		int version=32;//cmpp 2.0
		PSender send=new PSender(ip,port,loginName,secret,version);
		send.start();
		
	}
	
	
}
