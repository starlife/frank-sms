/**
 * File Name:MM7Receiver.java Company: 中国移动集团公司 Date : 2004-2-17
 */

package com.cmcc.mm7.vasp.service;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.Charset;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cmcc.mm7.vasp.http.HttpRequest;
import com.cmcc.mm7.vasp.message.MM7DeliverReq;
import com.cmcc.mm7.vasp.message.MM7DeliverRes;
import com.cmcc.mm7.vasp.message.MM7DeliveryReportReq;
import com.cmcc.mm7.vasp.message.MM7DeliveryReportRes;
import com.cmcc.mm7.vasp.message.MM7RSReq;
import com.cmcc.mm7.vasp.message.MM7ReadReplyReq;
import com.cmcc.mm7.vasp.message.MM7ReadReplyRes;
import com.cmcc.mm7.vasp.message.MM7VASPErrorRes;
import com.cmcc.mm7.vasp.message.MM7VASPRes;
import com.cmcc.mm7.vasp.protocol.MM7Helper;
import com.cmcc.mm7.vasp.protocol.DecodeMM7;

public class MM7Receiver extends Thread implements MM7AbstractReceiver
{
	private static final Log log = LogFactory.getLog(MM7Receiver.class);
	private int listenPort = 80;
	private InetAddress ip = null;

	private int BackLog = 50;

	private boolean LongLinkFlag = false;
	private String listenIP = null;
	private Charset charset = null;
	private boolean isStop = false;

	public MM7Receiver(String listenIP, int ListenPort, int backLog,
			boolean keepAlive, String charset) // 构造方法
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
		BackLog = backLog;
		LongLinkFlag = keepAlive;
		this.charset = Charset.forName(charset);
	}

	public void start() // 启动接收器
	{
		log.info("启动接收线程");
		ServerSocket s = null;
		try
		{
			s = new ServerSocket(listenPort, this.BackLog, this.ip);
			log.info("监听端口：" + s.getLocalSocketAddress());
			while (!isStop)
			{
				try
				{
					Socket client = s.accept();
					log.debug("收到客户端连接：" + client.getRemoteSocketAddress());
					dealRecv(client);
					/*
					 * if(this.timeout>0) { client.setSoTimeout(timeout); }
					 * dealRecv(client);
					 */
				}
				catch (IOException e)
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
	 * @param client
	 * @throws IOException
	 */
	private void dealRecv(Socket client) throws IOException
	{
		MM7VASPRes res=null;
		try
		{
			HttpRequest http = new HttpRequest();
			if (!http.recvData(client.getInputStream()))
			{
				// 发送一个错误回应包
				res = new MM7VASPErrorRes();
				res.setStatusCode(-102);
				res.setStatusText("接收回应消息失败！");
			}

			log.debug("接收到数据包：" + http);

			
			MM7RSReq req = DecodeMM7.decodeReqMessage(http.getBody(),
					charset.toString(),DecodeMM7.getBoundary(http.getHeader()));

			if (req == null)
			{

				log.debug("[Comments=解析后的消息为空！]");
				return;
			}

			//log.info(LogHelper.logMM7RSRes(req));

			
			if (req instanceof MM7DeliverReq)
			{

				res = doDeliver((MM7DeliverReq) req);

			}
			else if (req instanceof MM7DeliveryReportReq)
			{

				res = (MM7DeliveryReportRes) doDeliveryReport((MM7DeliveryReportReq) req);

			}
			else if (req instanceof MM7ReadReplyReq)
			{
				res = (MM7ReadReplyRes) doReadReply((MM7ReadReplyReq) req);

			}
			byte[] msgByte = MM7Helper.getMM7Message(res, charset,this.LongLinkFlag);
			client.getOutputStream().write(msgByte);
			client.getOutputStream().flush();

		}
		finally
		{
			client.close();
		}

	}

	public void mystop() // 停止接收器
	{
		isStop = true;
	}

	// 处理到VASP的传送（deliver）多媒体消息
	public MM7VASPRes doDeliver(MM7DeliverReq mm7DeliverReq)
	{
		MM7DeliverRes res = new MM7DeliverRes();
		//res.setServiceCode(mm7DeliverReq.get)
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
