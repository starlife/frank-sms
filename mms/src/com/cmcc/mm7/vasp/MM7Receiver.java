/**
 * File Name:MM7Receiver.java Company: 中国移动集团公司 Date : 2004-2-17
 */

package com.cmcc.mm7.vasp;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.charset.Charset;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cmcc.mm7.vasp.http.HttpRequest;
import com.cmcc.mm7.vasp.protocol.DecodeMM7;
import com.cmcc.mm7.vasp.protocol.MM7Helper;
import com.cmcc.mm7.vasp.protocol.message.MM7DeliverReq;
import com.cmcc.mm7.vasp.protocol.message.MM7DeliverRes;
import com.cmcc.mm7.vasp.protocol.message.MM7DeliveryReportReq;
import com.cmcc.mm7.vasp.protocol.message.MM7DeliveryReportRes;
import com.cmcc.mm7.vasp.protocol.message.MM7RSReq;
import com.cmcc.mm7.vasp.protocol.message.MM7ReadReplyReq;
import com.cmcc.mm7.vasp.protocol.message.MM7ReadReplyRes;
import com.cmcc.mm7.vasp.protocol.message.MM7VASPErrorRes;
import com.cmcc.mm7.vasp.protocol.message.MM7VASPRes;
import com.cmcc.mm7.vasp.protocol.util.LogHelper;

public class MM7Receiver extends Thread implements MM7AbstractReceiver
{
	private static final Log log = LogFactory.getLog(MM7Receiver.class);

	// private static final java.util.concurrent.ExecutorService exec =
	// java.util.concurrent.Executors
	// .newSingleThreadExecutor();
	private InetAddress ip = null;
	private int listenPort = 80;
	private int backLog = 50;

	private boolean keepAlive = false;
	private int timeout = 30000;
	private Charset charset = null;
	private volatile boolean stop = false;

	public MM7Receiver(String listenIP, int ListenPort, int backLog,
			boolean keepAlive, int timeout, String charset) // 构造方法
	{
		super("MM7Receiver");
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
		this.keepAlive = keepAlive;
		this.timeout = timeout;
		this.charset = Charset.forName(charset);
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

	/**
	 * 接收一个MM7RSReq包并回应一个MM7VASPRes包
	 * 
	 * @param client
	 * @throws IOException
	 */
	private void dealRecv(final Socket client)
	{
		new Thread(new Runnable()
		{

			@Override
			public void run()
			{
				// TODO Auto-generated method stub
				// TODO Auto-generated method stub

				try
				{
					log.debug("新线程处理socket连接："
							+ client.getRemoteSocketAddress());
					while (true)
					{
						MM7VASPRes res = null;
						HttpRequest http = new HttpRequest();
						if (http.recvData(client.getInputStream()))
						{
							// log.info("接收到数据包：" +
							// com.cmcc.mm7.vasp.protocol.util.Hex.rhex(http.getData()));

							MM7RSReq req = DecodeMM7.decodeReqMessage(http
									.getBody(), charset.toString(), DecodeMM7
									.getBoundary(http.getHeader()));

							log.info("收到RSReq包 " + LogHelper.logMM7RSReq(req));

							if (req instanceof MM7DeliverReq)
							{

								res = doDeliver((MM7DeliverReq) req);

							}
							else if (req instanceof MM7DeliveryReportReq)
							{

								res = doDeliveryReport((MM7DeliveryReportReq) req);

							}
							else if (req instanceof MM7ReadReplyReq)
							{
								res = doReadReply((MM7ReadReplyReq) req);

							}
							else
							{
								// 发送一个错误回应包
								res = new MM7VASPErrorRes();
								res.setStatusCode(-109);
								res.setStatusText("消息解析失败！");
							}

						}
						else
						{
							// 发送一个错误回应包
							res = new MM7VASPErrorRes();
							res.setStatusCode(-102);
							res.setStatusText("接收回应消息失败！");
						}
						log.info("发送VaspRes包 " + LogHelper.logMM7VaspRes(res));
						byte[] msgByte = MM7Helper.getMM7Message(res, charset,
								keepAlive);
						client.getOutputStream().write(msgByte);
						client.getOutputStream().flush();
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
					log.info("接收socket关闭" + client.getRemoteSocketAddress()
							+ "(" + timeout + "ms)");

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

		}).start();

	}

	public void myStop() // 停止接收器
	{
		stop = true;
	}

	// 处理到VASP的传送（deliver）多媒体消息
	public MM7VASPRes doDeliver(MM7DeliverReq mm7DeliverReq)
	{
		MM7DeliverRes res = new MM7DeliverRes();
		// res.setServiceCode(mm7DeliverReq.get)
		res.setTransactionID(mm7DeliverReq.getTransactionID());
		res.setStatusCode(1000);
		return res;
	}

	public MM7VASPRes doDeliveryReport(MM7DeliveryReportReq mm7DeliveryReportReq)
	{
		MM7DeliveryReportRes res = new MM7DeliveryReportRes();
		res.setTransactionID(mm7DeliveryReportReq.getTransactionID());
		res.setStatusCode(1000);
		return res;
	}

	// 抽象方法。处理到VASP的读后回复报告
	public MM7VASPRes doReadReply(MM7ReadReplyReq mm7ReadReplyReq)
	{
		MM7ReadReplyRes res = new MM7ReadReplyRes();
		res.setTransactionID(mm7ReadReplyReq.getTransactionID());
		res.setStatusCode(1000);
		return res;
	}

}
