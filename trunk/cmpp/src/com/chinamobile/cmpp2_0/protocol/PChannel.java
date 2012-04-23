package com.chinamobile.cmpp2_0.protocol;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.chinamobile.cmpp2_0.protocol.message.APackage;
import com.chinamobile.cmpp2_0.protocol.message.BasePackage;
import com.chinamobile.cmpp2_0.protocol.message.CommandID;
import com.chinamobile.cmpp2_0.protocol.message.ConnectMessage;
import com.chinamobile.cmpp2_0.protocol.message.ConnectRespMessage;
import com.chinamobile.cmpp2_0.protocol.message.SubmitMessage;
import com.chinamobile.cmpp2_0.protocol.message.TerminateMessage;
import com.chinamobile.cmpp2_0.protocol.util.ByteConvert;
import com.chinamobile.cmpp2_0.protocol.util.Hex;

/**
 * 通道类，该类维护一个连接服务端网关的通道，提供包的发送和接收功能 约定如下： 1.该类要实现自包含，就是不依赖于包protocol外的自定义类
 * 2.该类对外提供了三个有用的方法: isLogin() 检查当前链路是否可用 sendPacket() 发送包方法 readPacket() 接收包方法
 * 3.该类还维护了一个needRespQue队列（保存待取得SubmitRespMessage的包）
 * 
 * @author Administrator
 */
public class PChannel
{
	private static final Log log = LogFactory.getLog(PChannel.class);// 记录日志

	private static final Object lock = new Object(); // 用来登陆，初始化
	private static final Object lockSend = new Object();
	private static final Object lockRecv = new Object();

	private final LinkedBlockingQueue<SubmitMessage> needRespQue = new LinkedBlockingQueue<SubmitMessage>(
			10000);

	/**
	 * 单实例对象
	 */
	private static PChannel channel = null;

	/**
	 * 连接Socket通道
	 */
	private Socket socket;
	/**
	 * 标识是否登录成功
	 */
	private volatile boolean blogin = false;
	// private volatile long sendPacket;
	// private volatile long revcPacket;
	// private volatile long erroPackage;

	/* 连接ismg网关的参数 */
	private String ip;
	private int port;
	private String loginname;
	private String loginpass;
	private int version;

	/**
	 * 通道超时时间，可设置
	 */
	private int timeout = 60 * 1000;// 60s

	private PChannel(String ip, int port, String loginname, String loginpass,
			int version)
	{
		this.ip = ip;
		this.port = port;
		this.loginname = loginname;
		this.loginpass = loginpass;
		this.version = version;

	}

	/**
	 * 初始化方法
	 * 
	 * @param ip
	 * @param port
	 * @param loginname
	 * @param loginpass
	 * @param version
	 */
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
		log.info(String.format("尝试创建Socket连接到服务器%s:%s", ip, port));

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

		return bret;
	}

	/**
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
		if (this.socket != null)
		{
			this.close();
		}
		if (!createSocket(ip, port))
		{
			log.error(String.format("创建Socket连接%s,%s失败,请检查配置", ip, port));
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
			log.info("收到包 " + curPack);
			if (curPack.getHead().getCommmandId() == CommandID.CMPP_CONNECT_RESP)
			{
				ConnectRespMessage lrm = new ConnectRespMessage(curPack);
				log.info("登陆回应消息字节码：" + Hex.rhex(lrm.getBytes()));
				log.info(lrm);
				if (lrm.getStatus() == 0)
				{
					log.info("登陆成功...");
					this.blogin = true;

					return true;
				}
				else
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
			{
				// 如果通道是通的，发送TerminateMessage中断通道
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
			close();

		}

	}

	/**
	 * 关闭Socket连接
	 */
	public void close()
	{
		synchronized (lock)
		{
			this.blogin = false;// 设置blogin标识为false
			if (socket == null)
			{
				return;
			}
			log.info("尝试关闭socket");
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
				log.info(String.format("登陆失败，尝试第 %d 次 登陆到 %s ：%d", i + 1, ip,
						port));
				i++;
				if (i > 6)
				{
					return false;
				}
				try
				{
					TimeUnit.SECONDS.sleep(10);
				}
				catch (Exception ex)
				{
					// ex.printStackTrace();
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
			int packLen = ByteConvert.byte2int(lenByte);
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
	 * 提供给本包使用
	 * 
	 * @return
	 */
	LinkedBlockingQueue<SubmitMessage> getNeedRespQue()
	{
		return needRespQue;
	}

	/**
	 * 查看当前链路是否是连接上的
	 * 
	 * @return
	 */
	public boolean isLogin()
	{
		synchronized (lock)
		{
			return this.blogin;
		}
	}

	/**
	 * 读一个包，该方法是同步的
	 * 
	 * @return 如果通道不可用 返回空
	 * @throws IOException
	 */
	public BasePackage readPacket() throws IOException
	{
		synchronized (lockRecv)
		{
			return readPacket(this.getInputStream());
		}
	}

	/**
	 * 发送包，该方法是同步的
	 * 
	 * @param send
	 *            包
	 * @return 如果通道不可用 返回false 发送成功返回true
	 * @throws IOException
	 */
	public boolean sendPacket(APackage send) throws IOException
	{
		synchronized (lockSend)
		{
			if(log.isInfoEnabled())
			{
				log.info("发送包："+send);
			}
			OutputStream out = this.getOutPutStream();
			if (out != null)
			{
				out.write(send.getBytes());
				out.flush();
				// 对于已经发送的SubmitMessage包，需要入needRespQue队
				if (send instanceof SubmitMessage)
				{
					needRespQue.offer((SubmitMessage) send);
				}
				return true;
			}
			else
			{
				return false;
			}
		}

	}

	public int getTimeout()
	{
		return timeout;
	}

	public void setTimeout(int timeout)
	{
		this.timeout = timeout;
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
		new PSender(ip, port, loginName, secret, version).start();
		new PReceiver().start();

	}

}
