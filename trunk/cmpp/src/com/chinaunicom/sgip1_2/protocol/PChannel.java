package com.chinaunicom.sgip1_2.protocol;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.chinaunicom.sgip1_2.protocol.message.APackage;
import com.chinaunicom.sgip1_2.protocol.message.BasePackage;
import com.chinaunicom.sgip1_2.protocol.message.BindMessage;
import com.chinaunicom.sgip1_2.protocol.message.BindRespMessage;
import com.chinaunicom.sgip1_2.protocol.message.CommandID;
import com.chinaunicom.sgip1_2.protocol.message.DeliverMessage;
import com.chinaunicom.sgip1_2.protocol.message.ReportMessage;
import com.chinaunicom.sgip1_2.protocol.message.SubmitRespMessage;
import com.chinaunicom.sgip1_2.protocol.message.UnbindMessage;
import com.chinaunicom.sgip1_2.protocol.message.UnbindRespMessage;
import com.chinaunicom.sgip1_2.protocol.util.ByteConvert;
import com.chinaunicom.sgip1_2.protocol.util.Constants;
import com.chinaunicom.sgip1_2.protocol.util.Hex;

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
	private static final Log hex = LogFactory.getLog("hex");

	private static final Object lock = new Object(); // 用来登陆，初始化
	private static final Object lockSend = new Object();
	private static final Object lockRecv = new Object();

	/**
	 * 连接Socket通道
	 */
	private Socket socket;
	/**
	 * 标识是否登录成功
	 */
	private volatile boolean blogin = false;

	/* 连接ismg网关的参数 */
	private String ip;
	private int port;
	private int loginType;
	private String loginname;
	private String loginpass;
	private String nodeid;

	/**
	 * 通道超时时间，可设置
	 */
	private int timeout = Constants.TIMEOUT;// 60s

	PChannel(String ip, int port, String nodeid, int loginType,
			String loginname, String loginpass)
	{
		this.ip = ip;
		this.port = port;
		this.nodeid = nodeid;
		this.loginType = loginType;
		this.loginname = loginname;
		this.loginpass = loginpass;
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
		log.info(String.format("尝试创建Socket连接到服务器%s:%s", addr, port));

		boolean bret = false;
		try
		{
			socket = new Socket(addr, port);
			// socket.setTcpNoDelay(true);// 数据立即发送
			// socket.setTrafficClass(0x04 | 0x10);
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
	 * @param port
	 * @param nodeid
	 * @param loginType
	 * @param loginName
	 * @param loginPasswd
	 * @return 成功返回true 失败 清理登陆痕迹
	 */
	private boolean login(String ip, int port, String nodeid, int loginType,
			String loginName, String loginPasswd)
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
			BindMessage bm = new BindMessage(nodeid, loginType, loginName,
				loginPasswd);
			log.info("------------------发送登陆包" + bm);
			sendPacket(socket.getOutputStream(), bm);
			APackage curPack = readPacket(socket.getInputStream());
			log.info("收到登录回应包 " + curPack);
			if (curPack instanceof BindRespMessage)
			{
				BindRespMessage brm = (BindRespMessage) curPack;
				if (brm.getResult() == 0)
				{
					log.info("登陆成功...");
					this.blogin = true;
					return true;
				}
				else
				{
					log.info("登陆失败，Result=" + brm.getResult());
				}

			}
			else
			{
				log.info("登陆失败，收到的回应包不是BindRespMessage包");
			}
		}
		catch (Exception ex)
		{
			// ex.printStackTrace();
			log.error(null, ex);
		}
		close();
		return false;

	}

	/**
	 * 连接smgw
	 * 
	 * @return true 连接成功
	 */
	private boolean doConnect()
	{

		int i = 1;
		while (!login(ip, port, nodeid, loginType, loginname, loginpass))
		{
			log
				.info(String
					.format("登陆失败，尝试第 %d 次 登陆到 %s ：%d", i + 1, ip, port));
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

		return true;

	}

	/**
	 * 得到可用的通道
	 * 
	 * @return Socket
	 */
	private Socket getConnection()
	{

		// 如果连接不可用，那么连接
		if (!this.isLogin())
		{
			if (!doConnect())
			{
				return null;
			}
		}
		return socket;

	}

	/**
	 * 接受线程接受到的包是如下几种 bind unbind deliver report 发送线程接受到包邮如下几种 bindresp
	 * unbindresp submitresp
	 * 
	 * @param recvPack
	 * @return
	 */
	static APackage makePackage(BasePackage recvPack)
	{

		APackage pk = null;
		int commandId = recvPack.getHead().getCommmandId();
		if (commandId == CommandID.SGIP_BIND)
		{
			pk = new BindMessage(recvPack);
		}
		else if (commandId == CommandID.SGIP_BIND_RESP)
		{
			pk = new BindRespMessage(recvPack);
		}
		else if (commandId == CommandID.SGIP_UNBIND)
		{
			pk = new UnbindMessage(recvPack);
		}
		else if (commandId == CommandID.SGIP_UNBIND_RESP)
		{
			pk = new UnbindRespMessage(recvPack);
		}
		else if (commandId == CommandID.SGIP_SUBMIT_RESP)
		{
			pk = new SubmitRespMessage(recvPack);
		}
		else if (commandId == CommandID.SGIP_DELIVER)
		{
			pk = new DeliverMessage(recvPack);
		}
		else if (commandId == CommandID.SGIP_REPORT)
		{
			pk = new ReportMessage(recvPack);
		}
		else
		{
			pk = recvPack;
		}
		return pk;
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
			return this.blogin && isSocketAvail(socket);
		}
	}

	/**
	 * 断开连接
	 */
	public void unbind()
	{
		if (isLogin())
		{
			try
			{
				APackage pack = new UnbindMessage(nodeid);
				log.info("发送断开连接包：" + pack);
				sendPacket(socket.getOutputStream(), pack);
			}
			catch (IOException ex)
			{
				log.error(null, ex);
			}
			finally
			{
				this.close();
			}

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
	 * 发送包，该方法是同步的
	 * 
	 * @param pack
	 *            包
	 * @return 如果通道不可用 返回false 发送成功返回true
	 * @throws IOException
	 */
	public APackage send(APackage pack) throws IOException
	{
		synchronized (lock)
		{
			Socket socket = this.getConnection();
			if (socket != null)
			{
				pack.setTimeStamp();// 设置包的发送时间
				pack.addTimes();// 设置包的发送次数
				sendPacket(socket.getOutputStream(), pack);
				return readPacket(socket.getInputStream());
			}
			else
			{
				return null;
			}
		}

	}

	/**
	 * 读一个包，该方法是同步的
	 * 
	 * @return 如果通道不可用 返回空
	 * @throws IOException
	 */
	public APackage readPacket() throws IOException
	{
		synchronized (lockRecv)
		{
			Socket socket = this.getConnection();
			if (socket != null)
			{
				return readPacket(socket.getInputStream());
			}
			return null;
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
	public boolean sendPacket(APackage pack) throws IOException
	{
		synchronized (lockSend)
		{
			Socket socket = this.getConnection();
			if (socket != null)
			{
				pack.setTimeStamp();// 设置包的发送时间
				pack.addTimes();// 设置包的发送次数
				sendPacket(socket.getOutputStream(), pack);
				return true;
			}
			else
			{
				return false;
			}
		}

	}

	public static APackage readPacket(InputStream in) throws IOException
	{
		APackage pack = null;
		if (in != null)
		{
			byte[] lenByte = new byte[4];
			in.read(lenByte);// 读前面四个字节，包长度
			int packLen = ByteConvert.byte2int(lenByte);
			byte[] buf = new byte[packLen];
			System.arraycopy(lenByte, 0, buf, 0, 4);// 拷贝
			in.read(buf, 4, buf.length - 4);
			pack = makePackage(new BasePackage(buf));
			if (hex.isDebugEnabled())
			{
				hex.debug("收到包(" + pack.getHead().getCommandIdString() + "):"
						+ Hex.rhex(pack.getBytes()));
			}
		}
		return pack;

	}

	public static void sendPacket(OutputStream out, APackage pack)
			throws IOException
	{
		if (out != null && pack != null)
		{
			if (hex.isDebugEnabled())
			{
				hex.debug("发送包(" + pack.getHead().getCommandIdString() + "):"
						+ Hex.rhex(pack.getBytes()));
			}
			out.write(pack.getBytes());
			out.flush();
		}
	}

	/**
	 * 判断socket是否可用
	 * 
	 * @param socket
	 * @return true 可用
	 */
	public static boolean isSocketAvail(Socket socket)
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
		/*
		 * String ip = "211.140.12.45"; int port = 7890; String loginName =
		 * "Q61704"; String secret = "kki890"; // String secret="Q61704"; int
		 * version = 32;// cmpp 2.0 new PSender(ip, port, loginName, secret,
		 * version).start(); new PReceiver().start();
		 */

	}

}
