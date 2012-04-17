package com.chinamobile.cmpp2_0.protocol;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.chinamobile.cmpp2_0.protocol.message.APackage;
import com.chinamobile.cmpp2_0.protocol.message.ActiveTestMessage;
import com.chinamobile.cmpp2_0.protocol.message.BasePackage;
import com.chinamobile.cmpp2_0.protocol.message.CommandID;
import com.chinamobile.cmpp2_0.protocol.message.ConnectMessage;
import com.chinamobile.cmpp2_0.protocol.message.ConnectRespMessage;
import com.chinamobile.cmpp2_0.protocol.message.DeliverMessage;
import com.chinamobile.cmpp2_0.protocol.message.DeliverRespMessage;
import com.chinamobile.cmpp2_0.protocol.message.MessageUtil;
import com.chinamobile.cmpp2_0.protocol.message.SubmitMessage;
import com.chinamobile.cmpp2_0.protocol.message.SubmitRespMessage;
import com.chinamobile.cmpp2_0.protocol.message.TerminateMessage;
import com.chinamobile.cmpp2_0.protocol.util.Hex;
import com.chinamobile.cmpp2_0.protocol.util.TypeConvert;

/**
 * 通道类（最重要的类） 约定如下： 1.该类要实现自包含，就是不依赖于包protocol外的自定义类 2.短信的收发功能通过该类方法提供给外部
 * 
 * @author Administrator
 */
public class PChannel
{
	private static final Log log = LogFactory.getLog(PChannel.class);// 记录日志
	
	private  int timeout=60*1000;//60s
	//public static final long MAX_TIMEOUT = 3;// 单位秒
	// 链路最大空闲时间，如果超过这个时间，就发activeTest

	// private final LinkedBlockingQueue<APackage> sendQue = new
	// LinkedBlockingQueue<APackage>();
	// private final LinkedBlockingQueue<APackage> recvQue = new
	// LinkedBlockingQueue<APackage>();
	// private final LinkedBlockingQueue<SubmitMessage> needRespQue = new
	// LinkedBlockingQueue<SubmitMessage>();

	private Socket socket;
	private volatile boolean blogin = false;
	private volatile boolean stop = false;
	// private long bconnectTime;
	// private volatile long sendPacket;
	// private volatile long revcPacket;
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

	}

	public static synchronized void init(String ip, int port, String loginname,
			String loginpass, int version)
	{
		if (channel == null)
		{
			channel = new PChannel(ip, port, loginname, loginpass, version);
		}

	}

	public static PChannel getChannel()
	{
		return channel;
	}

	/*
	 * public void run() { while (!stop) { try { // 如果有数据收到，先接收数据，然后在发送 if
	 * (this.getInputStream().available() > 0) { log.info("有包需要接收"); BasePackage
	 * bp = this.readPacket();
	 * log.info("接收包："+bp.getHead().getCommandIdString()); recvQue.put(bp); }
	 * else { APackage p = sendQue.poll(MAX_TIMEOUT,
	 * java.util.concurrent.TimeUnit.SECONDS); //
	 * 如果发送队为空，就发送一个activeTestMessgage包 if (p == null) { log.info("等待3s未收到包"); p =
	 * new ActiveTestMessage(); } p.addTimes(); p.setTimeStamp();
	 * log.info("发送包："+p); this.sendPacket(p); if ((p.getHead().getCommmandID() ==
	 * CommandID.CMPP_SUBMIT) && (p instanceof SubmitMessage)) {
	 * needRespQue.put((SubmitMessage) p); } } } catch (Exception e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); log.error(null, e); } } }
	 */

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
		if (log.isDebugEnabled())
		{
			log.debug(LogConstants.ENTER_METHOD + "createSocket()");
		}
		log.info(String.format("尝试连接远端服务器%s:%s", ip, port));

		boolean bret = false;
		try
		{
			socket = new Socket(addr, port);
			socket.setSoTimeout(this.timeout);// 1分钟没响应断开连接
			this.blogin = false;
			bret = true;
		}
		catch (UnknownHostException ex)
		{
			// ex.printStackTrace();
			log.error(null, ex);
		}
		catch (IOException ex)
		{
			// ex.printStackTrace();
			log.error(null, ex);
		}

		if (log.isDebugEnabled())
		{
			log.debug(LogConstants.EXIT_METHOD + "createSocket()");
		}

		return bret;
	}

	/**
	 * 
	 * 登陆远程服务器：1 建立socket连接 2 发送登陆包 
	 * 如果连接失败，那么设置socket=null，如果登录失败，那么发送断开连接包并设置socket=null
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
		if (log.isDebugEnabled())
		{
			log.debug(LogConstants.ENTER_METHOD + "login()");
		}
		
		if(this.socket!=null)
		{
			this.close();
		}
		if (!createSocket(ip, port))
		{
			log.error(String.format("创建Socket连接%s,%s失败,请检查配置",ip,port));
			if (log.isDebugEnabled())
			{
				log.debug(LogConstants.EXIT_METHOD + "login()");
			}
			return false;
		}
		try
		{
			ConnectMessage lm = new ConnectMessage(loginName, loginPasswd,
					clientVersion);
			socket.getOutputStream().write(lm.getBytes());
			socket.getOutputStream().flush();
			
			log.info("------------------发送登陆包");
			log.info("登陆消息字节码：" + Hex.rhex(lm.getBytes()));
			log.info(lm);
			
			BasePackage curPack = this.readPacket(socket.getInputStream());
			log.info(curPack);
			if (curPack.getHead().getCommmandID() == CommandID.CMPP_CONNECT_RESP)
			{
				ConnectRespMessage lrm = new ConnectRespMessage(curPack);
				log.info("登陆回应消息字节码：" + Hex.rhex(lrm.getBytes()));
				log.info(lrm);
				if (lrm.getStatus() == 0)
				{
					log.info("登陆成功...");
					this.blogin = true;

					if (log.isDebugEnabled())
					{
						log.debug(LogConstants.EXIT_METHOD + "login()");
					}
					
					return true;
				}else
				{
					log.info("登陆失败，需要断开连接");
				}

			}
			// 发送拆解包断开连接
			this.disconnect();

		}
		catch (Exception ex)
		{
			// ex.printStackTrace();
			log.error(null, ex);
			this.close();
		}
		

		if (log.isDebugEnabled())
		{
			log.debug(LogConstants.EXIT_METHOD + "login()");
		}

		return false;

	}

	/*
	 * 断开连接
	 */

	private void disconnect()
	{
		if (socket == null)
		{
			return;
		}

		try
		{
			if (this.blogin && this.isSocketAvail())
			{// 如果通道是通的，发送TerminateMessage中断通道
				TerminateMessage tx = new TerminateMessage();
				log.info(tx);
				socket.getOutputStream().write(tx.getBytes());
				socket.getOutputStream().flush();
			}

		}
		catch (Exception ex)
		{
			// ex.printStackTrace();
			log.error(null, ex);
		}
		finally
		{
			log.info("设置Socket为null");
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
				log.error(null, ex);
			}
			socket = null;

		}

	}

	/**
	 * 关闭Socket连接
	 */
	public void close()
	{
		synchronized (lock)
		{

			if (socket == null)
			{
				return;
			}
			log.info("Socket不为空，关闭socket");
			try
			{
				if (!socket.isInputShutdown())
				{
					socket.shutdownInput();
				}
			}
			catch (IOException ex)
			{
				// ex.printStackTrace();
				log.error(null, ex);
			}
			try
			{
				if (!socket.isOutputShutdown())
				{
					socket.shutdownOutput();
				}
			}
			catch (IOException ex)
			{
				// ex.printStackTrace();
				log.error(null, ex);
			}
			try
			{
				if (!socket.isClosed())
				{
					socket.close();
				}
			}
			catch (IOException ex)
			{
				// ex.printStackTrace();
				log.error(null, ex);
			}
			socket = null;
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
				log.info(String.format("登陆失败，尝试 %d 次 登陆到 %s ：%d", i, ip, port));
				i++;
				if(i>6)
				{
					return false;
				}
				try
				{
					TimeUnit.SECONDS.sleep(10);
				}
				catch (Exception ex)
				{
					//ex.printStackTrace();
					log.error(null, ex);
				}
			}
		}
		return true;

	}

	/**
	 * 返回当前通道的InputSteam，如果当前通道不可用，发送长连接消息进行连接，如果还是未能连接上 返回空
	 * 
	 * @return 返回当前通道的InputSteam
	 */
	private InputStream getInputStream()
	{
		InputStream in = null;
		try
		{
			if (!this.blogin || !this.isSocketAvail())
			{
				if (!doConnect())
				{
					return null;
				}
			}
			in = socket.getInputStream();
		}
		catch (Exception ex)
		{
			this.close();// 关闭socket
			// ex.printStackTrace();
			log.error(null, ex);

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
		
		OutputStream output = null;
		try
		{
			if (!this.blogin || !this.isSocketAvail())
			{
				if (!doConnect())
				{
					return null;
				}
			}
			output = socket.getOutputStream();
		}
		catch (Exception ex)
		{
			this.close();// 关闭socket
			// ex.printStackTrace();
			log.error(null, ex);

		}
		return output;
	}

	

	private BasePackage readPacket(InputStream in) throws IOException
	{
		BasePackage pack = null;
		if (in != null)
		{
			byte[] lenByte = new byte[4];
			in.read(lenByte);// 读前面四个字节，包长度
			int packLen = TypeConvert.byte2int(lenByte);
			byte[] buf = new byte[packLen];
			System.arraycopy(lenByte, 0, buf, 0, 4);// 拷贝
			in.read(buf, 4, buf.length - 4);
			pack = new BasePackage(buf);
		}
		return pack;

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
	 * 读一个包
	 * 
	 * @return 如果通道不可用 返回空
	 * @throws IOException
	 */
	public BasePackage readPacket() throws IOException
	{
		return readPacket(this.getInputStream());
	}
	
	/**
	 * 
	 * @param send  包
	 * @return 如果通道不可用 返回false 发送成功返回true
	 * @throws IOException
	 */
	public boolean sendPacket(APackage send) throws IOException
	{
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
	/*
	 * public void send(APackage ap) { try { sendQue.put(ap); } catch
	 * (InterruptedException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); log.error(null, e); } }
	 */

	/*
	 * public APackage receive() { return recvQue.poll(); }
	 */

	/**
	 * 提供给本包使用
	 * 
	 * @return
	 */
	/*
	 * LinkedBlockingQueue<SubmitMessage> getNeedRespQue() { return
	 * needRespQue; }
	 */

	static class SendThread extends Thread
	{
		public SendThread()
		{
			log.info("初始化发送线程");
		}
		public void run()
		{
			while(true)
			{
				try
				{
					TimeUnit.SECONDS.sleep(3);
					
				}catch(Exception ex)
				{
					
				}
				PChannel channel = PChannel.getChannel();
				if(channel!=null)
				{
					try
					{
						log.info("发送链路检测包");
						channel.sendPacket(new ActiveTestMessage());
						//String[] desttermid="13777802386".split(","); 
						//byte[] msgByte = "this is a test msg from lin".getBytes();//gbk解码
						//String param = ""; 
						//SubmitMessage sm= MessageUtil.createSubmitMessage("911337","106573061704","MZJ3310101", desttermid, msgByte,param);
						//SubmitMessage sm= MessageUtil.createSubmitMessage("","","", desttermid, msgByte,param);
						//log.info("发送 提交包");
						//channel.sendPacket(sm);
					}
					catch (Exception e)
					{
						// TODO Auto-generated catch block
						log.error(null,e);
						channel.close();
					}
				}
				
			}
		}
	}
	static class RecvThread extends Thread
	{
		public RecvThread()
		{
			log.info("初始化接收线程");
		}
		public void run()
		{
			while(true)
			{
				PChannel channel = PChannel.getChannel();
				if(channel!=null)
				{
					BasePackage p=null;
					try
					{
						p=channel.readPacket();
						log.info("收到包："+p);
						if(p.getHead().getCommmandID()==CommandID.CMPP_DELIVER)
						{
							DeliverMessage dm=new DeliverMessage(p);
							log.info(dm);
							DeliverRespMessage drm=new DeliverRespMessage(dm);
							channel.sendPacket(drm);
							if(dm.getDeliver().RegisteredDelivery==0)
							{
								String[] desttermid=dm.getDeliver().SrcTermID.split(",");
								String msg="您好，您发送的消息为："+dm.getDeliver().MsgContent+"!";
								byte[] msgByte = msg.getBytes();//gbk解码
								String param = ""; 						
								SubmitMessage sm= MessageUtil.createSubmitMessage("911337","106573061704","MZJ3310101", desttermid, msgByte,param);
								log.info("发送 提交包:"+sm.getBytes().length +sm);
								channel.sendPacket(sm);
							}
							
						}
						if(p.getHead().getCommmandID()==CommandID.CMPP_SUBMIT_RESP)
						{
							SubmitRespMessage srm=new SubmitRespMessage(p);
							log.info(srm);
							
						}
						
					}
					catch (Exception e)
					{
						// TODO Auto-generated catch block
						log.error(null,e);
						channel.close();
					}
				}
			}
		}
	}
	public static void main(String[] args) throws IOException,
			InterruptedException
	{
		String ip = "211.140.12.45";
		int port = 7890;
		String loginName = "Q61704";
		String secret = "kki890";
		// String secret="Q61704";
		int version = 32;// cmpp 2.0
		PChannel channel = PChannel.getChannel();

		if (channel == null)
		{
			log.info("通道未初始化，初始化通道...");
			PChannel.init(ip, port, loginName, secret, version);
			channel = PChannel.getChannel();

		}
		new SendThread().start();
		new RecvThread().start();
		
		/*TimeUnit.SECONDS.sleep(20);
		System.out.println("休息20s后醒来");
		String[] desttermid = "13777802386".split(",");
		byte[] msgByte = "this is a test msg from lin".getBytes();// gbk解码
		String param = "";*/
		// SubmitMessage sm=
		// MessageUtil.createSubmitMessage("911337","106573061704","MZJ3310101",
		// desttermid, msgByte,param);
		// channel.sendQue.put(new TerminateMessage());
		// APackage p=channel.recvQue.peek();
		// System.out.println("gotttttt:"+p);
		/*
		 * String[] desttermid="13777802386".split(","); byte[]
		 * msgByte = "this is a test msg from lin".getBytes();//
		 * gbk解码 String param = ""; SubmitMessage sm=
		 * MessageUtil.createSubmitMessage("911337","106573061704","MZJ3310101",
		 * desttermid, msgByte,param); log.info("发送 提交包");
		 * socket.getOutputStream().write(sm.getBytes());
		 * socket.getOutputStream().flush();
		 * if(socket.getInputStream().available()<=0) {
		 * log.info("sleep 1s"); TimeUnit.SECONDS.sleep(1); }
		 * System.out.println("socket.getInputStream().available()>0:"+(socket.getInputStream().available()>0));
		 * if(socket.getInputStream().available()>0) {
		 * log.info("socket.getInputStream().available():"+socket.getInputStream().available());
		 * CurPack = this.readPacket(socket.getInputStream());
		 * log.info(CurPack); }
		 */

	}

}
