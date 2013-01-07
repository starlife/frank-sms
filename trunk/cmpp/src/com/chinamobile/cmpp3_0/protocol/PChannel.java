package com.chinamobile.cmpp3_0.protocol;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.chinamobile.cmpp2_0.protocol.util.ByteConvert;
import com.chinamobile.cmpp3_0.protocol.message.APackage;
import com.chinamobile.cmpp3_0.protocol.message.ActiveTestMessage;
import com.chinamobile.cmpp3_0.protocol.message.BasePackage;
import com.chinamobile.cmpp3_0.protocol.message.CommandID;
import com.chinamobile.cmpp3_0.protocol.message.ConnectMessage;
import com.chinamobile.cmpp3_0.protocol.message.ConnectRespMessage;
import com.chinamobile.cmpp3_0.protocol.message.SubmitMessage;
import com.chinamobile.cmpp3_0.protocol.message.TerminateMessage;

/**
 * 通道类（最重要的类） 约定如下： 
 * 1.该类要实现自包含，就是不依赖于包protocol外的自定义类 
 * 2.短信的收发功能通过该类方法提供给外部
 * 
 * @author Administrator
 */
public class PChannel extends Thread
{
	private static final Log log=LogFactory.getLog(PChannel.class);//记录日志
	
	public static final long MAX_TIMEOUT = 30;//单位秒 链路最大空闲时间，如果超过这个时间，就发activeTest
	
	private final  LinkedBlockingQueue<APackage> sendQue = new LinkedBlockingQueue<APackage>();
	private final  LinkedBlockingQueue<APackage> recvQue = new LinkedBlockingQueue<APackage>();
	private final  LinkedBlockingQueue<SubmitMessage> needRespQue = new LinkedBlockingQueue<SubmitMessage>();
	
	private Socket socket;
	private volatile boolean blogin = false;
	private volatile boolean stop = false;
	// private long bconnectTime;
	//private volatile long sendPacket;
	//private volatile long revcPacket;
	// private volatile long erroPackage;
	private static final Object lock = new Object(); // 用来登陆，初始化

	
	private static PChannel channel = null;

	// private Log log;
	/* 连接ismg网关的参数 */
	private String ip;
	private int port;
	private String loginname;
	private String loginpass;
	private int version;

	private PChannel(String ip, int port, String loginname, String loginpass,
			int version)
	{
		this.ip = ip;
		this.port = port;
		this.loginname = loginname;
		this.loginpass = loginpass;
		this.version = version;
		this.start();//启动线程

	}
	
	public static synchronized void init(String ip, int port, String loginname, String loginpass,
			int version)
	{
		if(channel==null)
		{
			channel=new PChannel(ip,port,loginname,loginpass,version);
		}
		
	}
	public static PChannel getChannel()
	{
		return channel;
	}

	
	public void run()
	{
		while (!stop)
		{
			try
			{
				// 如果有数据收到，先接收数据，然后在发送
				if (this.getInputStream().available() > 0)
				{
					BasePackage bp = this.readPacket();
					recvQue.put(bp);
				}
				else
				{

					APackage p = sendQue.poll(MAX_TIMEOUT,
							java.util.concurrent.TimeUnit.SECONDS);
					// 如果发送队为空，就发送一个activeTestMessgage包
					if (p == null)
					{
						p = new ActiveTestMessage();
					}
					p.addTimes();
					p.setTimeStamp();
					this.sendPacket(p);
					if ((p.getHead().getCommmandID() == CommandID.CMPP_SUBMIT)
							&& (p instanceof SubmitMessage))
					{
						needRespQue.put((SubmitMessage) p);
					}
				}
			}
			catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				log.error(null, e);
			}
		}
	}

	/**
	 * 建立socket连接
	 * 
	 * @param addr
	 *            远程地址
	 * @param port
	 *            远程端口
	 * @return 成功返回true
	 */
	private boolean createSocket(String addr, int port)
	{
		if(log.isDebugEnabled())
		{
			log.debug(LogConstants.ENTER_METHOD+"createSocket()");
		}
		log.info(String.format("尝试连接远端服务器%s:%s",ip,port));
		
		boolean bret = false;
		try
		{
			socket = new Socket(addr, port);
			socket.setSoTimeout(1000 * 60 * 1);// 五分钟没响应断开连接
			this.blogin = false;
			bret = true;
		}
		catch (UnknownHostException ex)
		{
			ex.printStackTrace();
			log.error(null,ex);
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
			log.error(null,ex);
		}
		
		if(log.isDebugEnabled())
		{
			log.debug(LogConstants.EXIT_METHOD+"createSocket()");
		}
		
		return bret;
	}

	/**
	 * 登陆远程服务器：1 建立socket连接 2 发送登陆包
	 * 
	 * @param ip
	 *            远程Ip
	 * @param port
	 *            远程端口
	 * @param loginName
	 *            登陆名
	 * @param loginPasswd
	 *            登陆密码
	 * @param loginMode
	 *            登陆模式
	 * @param clientVersion
	 *            客服端最高版本
	 * @return 成功返回true 失败 清理登陆痕迹
	 */
	private boolean login(String ip, int port, String loginName,
			String loginPasswd, int clientVersion)
	{
		if(log.isDebugEnabled())
		{
			log.debug(LogConstants.ENTER_METHOD+"login()");
		}
		
		if (!createSocket(ip, port))
		{
			log.error("创建Socket连接失败");
			if(log.isDebugEnabled())
			{
				log.debug(LogConstants.EXIT_METHOD+"login()");
			}
			return false;
		}
		try
		{
			ConnectMessage lm = new ConnectMessage(loginName, loginPasswd,
					clientVersion);
			log.info("------------------发送登陆包");
			log.info(lm);
			socket.getOutputStream().write(lm.getBytes());
			socket.getOutputStream().flush();
			BasePackage CurPack = this.readPacket(socket.getInputStream());
			if (CurPack.getHead().getCommmandID() == CommandID.CMPP_CONNECT_RESP)
			{
				ConnectRespMessage lrm = new ConnectRespMessage(CurPack);
				log.info(lm);
				if (lrm.getStatus() == 0)
				{
					log.info("登陆成功...");
					this.blogin = true;
					
					if(log.isDebugEnabled())
					{
						log.debug(LogConstants.EXIT_METHOD+"login()");
					}
					
					return true;
				}

			}

		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			log.error(null,ex);
		}
		// 登陆失败则断开连接
		this.close(true);
		
		if(log.isDebugEnabled())
		{
			log.debug(LogConstants.EXIT_METHOD+"login()");
		}
		
		return false;

	}

	/*
	 * 手动关闭 和 自动关闭 如果auto=true 自动关闭 auto=false 手动关闭
	 */

	private void close(boolean auto)
	{
		if (socket == null)
		{
			return;
		}
		synchronized (lock)
		{
			try
			{
				if (auto&& this.blogin && this.isSocketAvail())
				{// 如果通道是通的，发送TerminateMessage中断通道
					TerminateMessage tx = new TerminateMessage();
					log.info(tx);
					socket.getOutputStream().write(tx.getBytes());
					socket.getOutputStream().flush();
				}

			}
			catch (Exception ex)
			{
				ex.printStackTrace();
				log.error(null,ex);
			}
			finally
			{
				try
				{
					if (!socket.isInputShutdown())
					{
						socket.shutdownInput();
					}
					if (!socket.isOutputShutdown())
					{
						socket.shutdownOutput();
					}
					if (!socket.isClosed())
					{
						socket.close();
					}
				}
				catch (IOException ex)
				{
					ex.printStackTrace();
					log.error(null,ex);
				}
				socket = null;

			}
		}
	}

	/**
	 * 关闭Socket连接
	 */
	public synchronized void close()
	{
		close(false);
	}

	/**
	 * 判断socket是否可用
	 * 
	 * @param socket
	 * @return true 可用
	 */
	private boolean isSocketAvail()
	{
		if (socket == null)
		{
			return false;
		}
		else
		{
			return socket.isConnected() && !socket.isClosed()
					&& !socket.isInputShutdown() && !socket.isOutputShutdown();
		}
	}

	/**
	 * 连接smgw
	 * 
	 * @return true 连接成功
	 */
	private boolean doConnect()
	{
		synchronized (lock)
		{
			if (this.blogin && this.isSocketAvail())
			{
				return true;
			}
			int i = 1;
			while (!login(ip, port, loginname, loginpass, version))
			{
				log.info(String.format("登陆失败，尝试 %d 次 登陆到 %s ：%d", i, ip,port));
				i++;
				try
				{
					TimeUnit.SECONDS.sleep(10);
				}
				catch (Exception ex)
				{
					ex.printStackTrace();
					log.error(null,ex);
				}
			}
			return true;

		}

	}

	private InputStream getInputStream()
	{
		InputStream in = null;
		if (!this.blogin || !this.isSocketAvail())
		{
			if (!doConnect())
			{
				return null;
			}
		}
		try
		{
			in = socket.getInputStream();
		}
		catch (IOException ex)
		{
			this.close(true);
			ex.printStackTrace();
			log.error(null,ex);
			
		}
		return in;

	}

	/**
	 * 得到可用的通道
	 * 
	 * @param socket
	 * @return
	 */
	private OutputStream getOutPutStream()
	{
		if (!this.blogin || !this.isSocketAvail())
		{
			if (!doConnect())
			{
				return null;
			}
		}
		OutputStream output = null;
		try
		{
			output = socket.getOutputStream();
		}
		catch (IOException ex)
		{
			this.close();
			ex.printStackTrace();
			log.error(null,ex);
			
		}
		return output;
	}

	private boolean sendPacket(APackage send) throws IOException
	{

		/*
		 * if (this.log!= null) { log.write("[" + DateUtil.getStringTime() + "]" +
		 * "Send:" + Hex.rhex(send.getBuf())); }
		 */
		OutputStream out = this.getOutPutStream();
		if (out != null)
		{
			out.write(send.getBytes());
			out.flush();
			
			return true;
		}
		else
		{
			return false;
		}

	}
	
	/**
	 * 读一个包
	 * @return
	 * @throws IOException
	 */
	private BasePackage readPacket() throws IOException
	{
		return readPacket(this.getInputStream());
	}

	
	private BasePackage readPacket(InputStream in) throws IOException
	{
		BasePackage pack=null;
		if(in!=null)
		{
			byte[] lenByte = new byte[4];
			in.read(lenByte);//读前面四个字节，包长度
			int PackLen = ByteConvert.byte2int(lenByte);
			byte[] buf = new byte[PackLen];
			System.arraycopy(lenByte, 0, buf, 0, 4);//拷贝
			in.read(buf, 4, buf.length - 4);
			pack = new BasePackage(buf);
		}
		return pack;

	}


	
	
	
	
	public void send(APackage ap)
	{
		try
		{
			sendQue.put(ap);
		}
		catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error(null, e);
		}
	}
	
	public APackage receive()
	{
		return recvQue.poll();
	}
	
	
	
	/**
	 * 提供给本包使用
	 * @return
	 */
	 LinkedBlockingQueue<SubmitMessage> getNeedRespQue()
	{
		return needRespQue;
	}
	
	
	
	public static void main(String[] args) throws IOException
	{
		String ip="211.140.12.45";
		int port=7890;
		String spid="914071";
		String secret ="q61132";
		int version=48;//cmpp 3.0
		PChannel.init(ip, port,spid, secret, version);
		//PChannel.getChannel();
	}
	
}
