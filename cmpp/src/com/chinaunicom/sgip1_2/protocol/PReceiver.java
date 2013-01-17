/**
 * File Name:MM7Receiver.java Company: 中国移动集团公司 Date : 2004-2-17
 */

package com.chinaunicom.sgip1_2.protocol;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.chinaunicom.sgip1_2.protocol.message.APackage;
import com.chinaunicom.sgip1_2.protocol.message.BindMessage;
import com.chinaunicom.sgip1_2.protocol.message.BindRespMessage;
import com.chinaunicom.sgip1_2.protocol.message.DeliverMessage;
import com.chinaunicom.sgip1_2.protocol.message.DeliverRespMessage;
import com.chinaunicom.sgip1_2.protocol.message.ReportMessage;
import com.chinaunicom.sgip1_2.protocol.message.ReportRespMessage;
import com.chinaunicom.sgip1_2.protocol.message.UnbindMessage;
import com.chinaunicom.sgip1_2.protocol.message.UnbindRespMessage;
import com.chinaunicom.sgip1_2.protocol.util.Constants;

public class PReceiver extends Thread implements AbstractReceiver
{
	private static final Log log = LogFactory.getLog(PReceiver.class);

	private InetAddress ip = null;
	private int listenPort = 8801;
	private int backLog = 50;

	private String nodeid;
	private int loginType = 2;
	private String loginname;
	private String loginpass;

	private int timeout = Constants.TIMEOUT;

	private volatile boolean stop = false;

	public PReceiver(String listenIP, int ListenPort, int backLog,
			String nodeid, int loginType, String loginname, String loginpass) // 构造方法
	{
		listenPort = ListenPort;
		try
		{
			ip = InetAddress.getByName(listenIP);
		}
		catch (Exception e)
		{
			log.error(null, e);
		}
		this.backLog = backLog;
		this.nodeid = nodeid;
		this.loginType = loginType;
		this.loginname = loginname;
		this.loginpass = loginpass;
	}

	public void run() // 启动接收器
	{
		log.info("启动接收线程...");
		ServerSocket s = null;
		try
		{
			s = new ServerSocket(listenPort, backLog, ip);
			log.info("监听端口：" + s.getLocalSocketAddress());
			while (!stop)
			{
				try
				{
					Socket client = s.accept();
					log.info("收到客户端连接：" + client.getRemoteSocketAddress());
					if (this.timeout > 0)
					{
						client.setSoTimeout(timeout);
					}
					dealRecv(client);

				}
				catch (Exception e)
				{
					// TODO Auto-generated catch block
					log.error(null, e);
				}
			}
		}
		catch (UnknownHostException e)
		{
			// TODO Auto-generated catch block
			log.error(null, e);
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			log.error(null, e);
		}
		finally
		{
			if (s != null)
			{
				try
				{
					s.close();
				}
				catch (IOException e)
				{
					// TODO Auto-generated catch block
					log.error(null, e);
				}
			}
		}

	}

	class Handler extends Thread
	{
		private Socket client = null;
		private volatile boolean bLogin = false;
		private volatile boolean bClose = false;

		public Handler(Socket socket)
		{
			this.client = socket;
		}

		public boolean checkLogin(APackage pk)
		{
			if (!this.bLogin)
			{
				log.error("未登录收到包:" + pk);
			}
			return true;
		}

		@Override
		public void run()
		{
			// TODO Auto-generated method stub
			try
			{
				log.debug("新线程处理socket连接：" + client.getRemoteSocketAddress());
				while (true)
				{
					APackage res = null;
					APackage pk = PChannel.readPacket(client.getInputStream());
					log.info("收到包: " + pk);
					if (pk instanceof BindMessage)
					{
						res = doBind((BindMessage) pk);
						if (((BindRespMessage) res).getResult() == 0)
						{
							this.bLogin = true;
						}
						else
						{
							log.info("登录失败");
						}

					}
					else if (pk instanceof UnbindMessage)
					{
						checkLogin(pk);
						res = doUnbind((UnbindMessage) pk);
						this.bClose = true;// 关闭标志位设置为true
					}
					else if (pk instanceof DeliverMessage)
					{
						checkLogin(pk);
						res = doDeliver((DeliverMessage) pk);
					}
					else if (pk instanceof ReportMessage)
					{
						checkLogin(pk);
						res = doReport((ReportMessage) pk);
					}
					else
					{
						// 收到的是一个错误错误包，需要断开连接
						log.error("收到的是一个错误错误包，需要断开连接");
						res = new UnbindMessage(nodeid);
						this.bClose = true;// 关闭标志位设置为true
					}

					log.info("发送回应包：" + res);
					PChannel.sendPacket(client.getOutputStream(), res);
					if (this.bClose)
					{
						log.info("连接需要关闭");
						break;
					}
				}

			}
			catch (Exception ex)
			{
				// log.error(null, ex);
				int timeout = 0;
				try
				{
					timeout = client.getSoTimeout();
				}
				catch (SocketException e)
				{
					// TODO Auto-generated catch block
					log.error(null, ex);
				}
				log.info("接收socket关闭" + client.getRemoteSocketAddress() + "("
						+ timeout + "ms)");

			}
			finally
			{
				try
				{
					client.close();
				}
				catch (IOException e)
				{
					// TODO Auto-generated catch block
					log.error(null, e);
				}
			}

		}
	}

	/**
	 * @param client
	 */
	private void dealRecv(Socket client)
	{
		new Handler(client).start();

	}

	public void myStop() // 停止接收器
	{
		stop = true;
	}

	private BindRespMessage doBind(BindMessage req)
	{

		BindRespMessage res = null;
		if (req.getLoginType() != loginType)
		{
			res = new BindRespMessage(req, 4);
		}
		else if (!req.getLoginName().equals(loginname)
				|| !req.getLoginPass().equals(loginpass))
		{
			res = new BindRespMessage(req, 1);
		}
		else
		{
			res = new BindRespMessage(req, 0);
		}
		return res;

	}

	private UnbindRespMessage doUnbind(UnbindMessage req)
	{
		UnbindRespMessage res = new UnbindRespMessage(req);
		return res;

	}

	// 处理到VASP的传送（deliver）多媒体消息
	public DeliverRespMessage doDeliver(DeliverMessage req)
	{
		log.debug("进入doDeliver方法");
		DeliverRespMessage res = new DeliverRespMessage(req);
		return res;
	}

	public ReportRespMessage doReport(ReportMessage req)
	{
		log.debug("进入doReport方法");
		ReportRespMessage res = new ReportRespMessage(req);
		return res;
	}

	public static void main(String[] args)
	{
		String listenIP = "192.168.100.239";
		// String listenIP = "192.168.147.175";
		int ListenPort = 8801;
		int backLog = 50;
		int loginType = 2;
		String loginname = "106550577371";
		String loginpass = "123456";
		String nodeid = "61153";
		new PReceiver(listenIP, ListenPort, backLog, nodeid, loginType,
			loginname, loginpass).start();
	}

}
