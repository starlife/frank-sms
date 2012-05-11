/**
 * File Name:MM7Receiver.java Company: 中国移动集团公司 Date : 2004-2-17
 */

package com.cmcc.mm7.vasp.service;

import java.net.*;
import java.io.*;
import java.text.*;
import java.util.*;
import sun.misc.*;
import java.security.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cmcc.mm7.vasp.message.*;
import com.cmcc.mm7.vasp.conf.*;
import com.cmcc.mm7.vasp.common.*;

public class MM7Receiver implements MM7AbstractReceiver
{
	private static final Log log = LogFactory.getLog(MM7Receiver.class);
	protected MM7Config Config;
	private int port;
	private InetAddress ip;
	// private int maxLongLinkNumber;
	private Thread ListenThread = null;
	private ServerSocket SSocket;
	private int BackLog;
	// private int ConnectCounts = 0;
	private long receiveNumber = 1;
	// private ByteArrayOutputStream Finerbaos;
	private StringBuffer TempBuffer;

	// private SimpleDateFormat Recordsdf;

	private long SameMinuteTime;
	private int SameMMSCID;
	private String MMSCID;

	private String strEnvelope;
	Socket CSocket;
	private boolean LongLinkFlag;
	private boolean isStop;

	public MM7Receiver() // 构造方法
	{
		reset();
		port = 80;
		// maxLongLinkNumber = 20;
		BackLog = 50;
		try
		{
			ip = InetAddress.getLocalHost();
		}
		catch (UnknownHostException uhe)
		{

			log.error(null, uhe);
		}
		catch (Exception e)
		{
			log.error(null, e);
		}
	}

	private void reset()
	{
		port = 80;
		// maxLongLinkNumber = 20;
		BackLog = 50;
		TempBuffer = new StringBuffer();

		SameMinuteTime = System.currentTimeMillis();
		// df = new DecimalFormat("0000");
		// N = 0;
		SameMMSCID = 0;
		strEnvelope = "";
		// Recordsdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		isStop = false;
	}

	public MM7Receiver(MM7Config config) // 构造方法
	{
		reset();
		Config = config;
		ConnectionPool pool = ConnectionPool.getInstance();
		pool.setConfig(config);
		String strKeepAlive = pool.getKeepAlive();
		if (strKeepAlive.equalsIgnoreCase("on"))
			this.setLongLink(true);
		else
			this.setLongLink(false);
		// maxLongLinkNumber = pool.getServerMaxSize();
		port = Config.getListenPort();
		try
		{
			ip = InetAddress.getByName(Config.getListenIP());
		}
		catch (Exception e)
		{
			log.error(null, e);
		}
		BackLog = Config.getBackLog();
	}

	private void setSameMinuteTime(long time)
	{
		SameMinuteTime = time;
	}

	private long getSameMinuteTime()
	{
		return SameMinuteTime;
	}

	private void setSameMMSCID(int mmscid)
	{
		SameMMSCID = mmscid;
	}

	private int getSameMMSCID()
	{
		return SameMMSCID;
	}

	private void setLongLink(boolean longflag)
	{
		LongLinkFlag = longflag;
	}

	private boolean getLongLink()
	{
		return LongLinkFlag;
	}

	public void setConfig(MM7Config config) // 设置MM7Config
	{
		this.Config = config;
		ConnectionPool pool = ConnectionPool.getInstance();
		pool.setConfig(config);
		String strKeepAlive = pool.getKeepAlive();
		if (strKeepAlive.equalsIgnoreCase("on"))
			this.setLongLink(true);
		else
			this.setLongLink(false);
		// maxLongLinkNumber = pool.getServerMaxSize();
		port = Config.getListenPort();
		try
		{
			ip = InetAddress.getByName(Config.getListenIP());
		}
		catch (Exception e)
		{
			log.error(null, e);
		}
		BackLog = Config.getBackLog();
	}

	public MM7Config getConfig() // 获得MM7Config
	{
		return (this.Config);
	}

	public void start() // 启动接收器
	{
		try
		{
			SSocket = new ServerSocket(port, BackLog, ip);
			log.info("监听端口：" + SSocket.getLocalSocketAddress());
		}
		catch (UnknownHostException uhe)
		{
			log.error(null, uhe);
		}
		catch (Exception e)
		{
			log.error(null, e);
			return;
		}

		if (ListenThread != null)
			ListenThread.setName("stop");
		ListenThread = new Thread("MM7-Listen")
		{
			public void run()
			{
				/*
				 * PooledExecutor poolexe = new PooledExecutor(new
				 * BoundedBuffer(10),50); poolexe.setMinimumPoolSize(5);
				 * poolexe.createThreads(5); poolexe.setKeepAliveTime(1600);
				 * poolexe.waitWhenBlocked();
				 */
				try
				{
					// 等待连接
					while (1 > 0)
					{
						// System.out.println("进入这里！");
						// SevereBuffer.append("\r\n进入这里！");
						/*
						 * PooledExecutor poolexe = new PooledExecutor(50);
						 * poolexe.setMinimumPoolSize(5);
						 * poolexe.createThreads(5);
						 * poolexe.setKeepAliveTime(1600);
						 * poolexe.waitWhenBlocked();
						 */
						if (!isStop)
						{
							try
							{
								// System.out.println("poolexe.getPoolSize()="+poolexe.getPoolSize());
								// SevereBuffer.append("poolexe.getPoolSize()="+poolexe.getPoolSize()+"\r\n");
								// SevereBuffer.append("enter this! ");
								if (!SSocket.isClosed())
								{
									CSocket = SSocket.accept();
									log.debug("收到客户端连接："
											+ CSocket.getRemoteSocketAddress());
									ServiceConnect(CSocket, 1);
									/*
									 * poolexe.execute(new Runnable() { public
									 * void run() { try {
									 * ServiceConnect(CSocket, 1); } catch
									 * (Exception e) {} } });
									 */
								}
							}
							catch (Exception e)
							{
								log.error(null, e);
							}
						}
					}
				}
				catch (Exception e)
				{
					log.error(null, e);
				}
				finally
				{
					try
					{
						/*
						 * if(poolexe != null)
						 * poolexe.shutdownAfterProcessingCurrentlyQueuedTasks();
						 */
						if (SSocket != null)
							SSocket.close();
					}
					catch (Exception e)
					{
						log.error(null, e);
					}
				}
				// System.out.println("结束监听线程的运行\n");
			}
		};
		ListenThread.setDaemon(true);
		ListenThread.start();
	}

	public void stop() // 停止接收器
	{
		isStop = true;
		/*
		 * try { if(ListenThread.isAlive()) ListenThread.destroy(); if(SSocket !=
		 * null) SSocket.close(); } catch(Exception e) {
		 * SevereBuffer.append("stop().SSocket不能被close.原因："+e); }
		 */
	}

	// 处理到VASP的传送（deliver）多媒体消息
	public MM7VASPRes doDeliver(MM7DeliverReq mm7DeliverReq)
	{
		MM7DeliverRes res = new MM7DeliverRes();
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

	private void ServiceConnect(Socket client, int nc) throws IOException
	{
		boolean longLink = false;
		longLink = this.getLongLink();
		DataInputStream in = null;

		boolean bHead = false;
		int index1 = 0, index2 = 0, index3 = 0;
		int length = 0;
		try
		{
			// client.setSoTimeout(Config.getTimeOut());

			// client.setSoTimeout(0);
			log.debug("recv client=" + client + "\r\n");
			in = new DataInputStream(new BufferedInputStream(client
					.getInputStream()));
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] b = new byte[1024];
			int readlen = 0;
			int totallen = 0;
			while (1 > 0)
			{
				// System.out.println("开始接收");
				readlen = in.read(b);
				if (readlen < 0)
					return;
				baos.write(b, 0, readlen);
				totallen = totallen + readlen;
				if (bHead == false && baos.toString().indexOf("\r\n\r\n") < 0)
					continue;

				if (bHead == false)
				{
					index1 = baos.toString().toLowerCase().indexOf(
							MMConstants.CONTENT_LENGTH);
					index2 = baos.toString().indexOf("\r", index1);
					if (index1 == -1 || index2 == -1)
					{
						baos.reset();
						totallen = 0;
						continue;
					}
					length = Integer.parseInt(baos.toString().substring(
							(index1 + MMConstants.CONTENT_LENGTH.length() + 1),
							index2).trim());
					index3 = baos.toString().indexOf("\r\n\r\n");
					bHead = true;
				}
				if (totallen < index3 + length + 4)
					continue;
				bHead = false;
				if (Config.getAuthenticationMode() == 1)
				{
					if (basicAuth(client, baos) == false)
					{
						baos.reset();
						totallen = 0;
						continue;
					}
				}
				else if (Config.getAuthenticationMode() == 2)
				{
					if (digestAuth(client, baos, nc) == false)
					{
						baos.reset();
						totallen = 0;
						continue;
					}
				}
				// System.out.println("baos="+baos.toString());
				SOAPDecoder soapDecoder = new SOAPDecoder();
				soapDecoder.setMessage(baos);
				try
				{
					soapDecoder.decodeMessage();
					// System.out.println("xml处理结束");
					log.debug("end dealing with xml!");
					MM7RSReq req = soapDecoder.getMessage();
					if (req == null)
					{
						baos.reset();
						totallen = 0;
						log.debug("[Comments=解析后的消息为空！]");
						continue;
					}
					if (soapDecoder.getMessageName().equals(
							MMConstants.DELIVERREQ))
					{
						log.debug("DeliverReq");
						MM7DeliverReq deliverReq = (MM7DeliverReq) req;

						TempBuffer.append("[TransactionID="
								+ deliverReq.getTransactionID() + "]");
						TempBuffer.append("[Message_Type=MM7DeliverReq]");
						TempBuffer.append("[Sender_Address="
								+ deliverReq.getSender() + "]");
						TempBuffer.append("[Recipient_Address={");
						if (deliverReq.isToExist())
						{
							TempBuffer.append("To={");
							List to = new ArrayList();
							to = deliverReq.getTo();
							for (int i = 0; i < to.size(); i++)
								TempBuffer.append((String) to.get(i) + ",");
							TempBuffer.append("}");
						}
						if (deliverReq.isCcExist())
						{
							TempBuffer.append("Cc={");
							List cc = new ArrayList();
							cc = deliverReq.getCc();
							for (int i = 0; i < cc.size(); i++)
								TempBuffer.append((String) cc.get(i) + ",");
							TempBuffer.append("}");
						}
						if (deliverReq.isBccExist())
						{
							TempBuffer.append("Bcc={");
							List bcc = new ArrayList();
							bcc = deliverReq.getBcc();
							for (int i = 0; i < bcc.size(); i++)
								TempBuffer.append((String) bcc.get(i) + ",");
							TempBuffer.append("}");
						}
						TempBuffer.append("}]");
						log.debug(TempBuffer);
						log.debug("\r\n" + soapDecoder.getEnvelope()); // Envelope消息
						// String timelevel = "\r\n\r\n["+Recordsdf.format(new
						// Date(
						// System.currentTimeMillis()))+"][6]";

						log.debug(soapDecoder.getSoapStream().toString());
						TempBuffer = new StringBuffer();
						MM7DeliverRes deliverRes = (MM7DeliverRes) doDeliver(deliverReq);
						// log.debug("\r\n\r\n"+"["+Recordsdf.format(new Date(
						// System.currentTimeMillis()))+"]"+"[3]");
						// String tempres = "\r\n\r\n["+Recordsdf.format(new
						// Date(
						// System.currentTimeMillis()))+"][6]";

						TempBuffer.append("[Message_Type=MM7DeliverRes]");
						TempBuffer.append("[Comments={"
								+ deliverRes.getStatusCode() + ";"
								+ deliverRes.getStatusText() + "}]\r\n");
						log.debug(TempBuffer);

						TempBuffer = new StringBuffer();

						send(client, deliverRes, longLink);
					}
					else if (soapDecoder.getMessageName().equals(
							MMConstants.DELIVERYREPORTREQ))
					{
						// System.out.println("MMConstants.DELIVERYREPORTREQ");
						log.debug("  DeliveryReportReq");
						MM7DeliveryReportReq deliverReportReq = (MM7DeliveryReportReq) req;
						// log.debug("\r\n\r\n["+Recordsdf.format(new Date(
						// System.currentTimeMillis()))+"][3]");
						TempBuffer.append("[TransactionID="
								+ deliverReportReq.getTransactionID() + "]");
						TempBuffer
								.append("[Message_Type=MM7DeliveryReportReq]");
						TempBuffer.append("[Sender_Address="
								+ deliverReportReq.getSender() + "]");
						log.debug(TempBuffer);
						log.debug("\r\n" + soapDecoder.getEnvelope()); // Envelope消息
						// String timelevel = "\r\n\r\n["+Recordsdf.format(new
						// Date(
						// System.currentTimeMillis()))+"][6]";

						TempBuffer = new StringBuffer();
						MM7DeliveryReportRes deliverReportRes = (MM7DeliveryReportRes) doDeliveryReport(deliverReportReq);

						// log.debug("\r\n\r\n"+"["+Recordsdf.format(new Date(
						// System.currentTimeMillis()))+"]"+"[3]");
						// String tempres = "\r\n\r\n["+Recordsdf.format(new
						// Date(
						// System.currentTimeMillis()))+"][6]";

						TempBuffer.append("[Message_Type=MM7DeliverRes]");
						TempBuffer.append("[Comments={"
								+ deliverReportRes.getStatusCode() + ";"
								+ deliverReportRes.getStatusText() + "}]\r\n");
						log.debug(TempBuffer);

						TempBuffer = new StringBuffer();

						send(client, deliverReportRes, longLink);

					}
					else if (soapDecoder.getMessageName().equals(
							MMConstants.READREPLYREQ))
					{
						log.debug("  ReadReplyReq");
						MM7ReadReplyReq readReplyReq = (MM7ReadReplyReq) req;

						// log.debug("\r\n\r\n["+Recordsdf.format(new Date(
						// System.currentTimeMillis()))+"][3]");
						TempBuffer.append("[TransactionID="
								+ readReplyReq.getTransactionID() + "]");
						TempBuffer.append("[Message_Type=MM7ReadReplyReq]");
						TempBuffer.append("[Sender_Address="
								+ readReplyReq.getSender() + "]");
						log.debug(TempBuffer);
						log.debug("\r\n" + soapDecoder.getEnvelope()); // Envelope消息
						// String timelevel = "\r\n\r\n["+Recordsdf.format(new
						// Date(
						// System.currentTimeMillis()))+"][6]";

						TempBuffer = new StringBuffer();
						MM7ReadReplyRes readReplyRes = (MM7ReadReplyRes) doReadReply(readReplyReq);
						// log.debug("\r\n\r\n"+"["+Recordsdf.format(new Date(
						// System.currentTimeMillis()))+"]"+"[3]");
						// String tempres = "\r\n\r\n[" + Recordsdf.format(new
						// Date(
						// System.currentTimeMillis())) + "][6]";

						TempBuffer.append("[Message_Type=MM7DeliverRes]");
						TempBuffer.append("[Comments={"
								+ readReplyRes.getStatusCode() + ";"
								+ readReplyRes.getStatusText() + "}]\r\n");
						log.debug(TempBuffer);

						TempBuffer = new StringBuffer();

						send(client, readReplyRes, longLink);
					}
					log.debug(strEnvelope);

					// String time = "[" + Recordsdf.format(new
					// Date(System.currentTimeMillis())) + "]";

					MMSCID = Config.getMmscId();

					log.debug("manager count=" + (receiveNumber++));

				}
				catch (SOAPDecodeException e)
				{
					log.error(null, e);
				}
				// System.out.println("处理数目="+(receiveNumber++));
				if (totallen > index3 + length + 4)
				{
					baos.write(b, (MMConstants.ARRAYLIMIT - (totallen - index3
							- length - 4)), totallen - index3 - length - 4);
					baos.reset();
					totallen = totallen - index3 - length - 4;
					baos.write(b, 0, totallen);
				}
				else
				{
					baos.reset();
					totallen = 0;
				}
				bHead = false;
				if (longLink == false)
					break;
			}
		}
		catch (IOException ioe)
		{
			log.error(null, ioe);
		}
		finally
		{
			// 清除
			if (in != null)
				in.close();
			if (client != null)
				client.close();
		}
	}

	public void send(Socket socket, MM7VASPRes mm7VASPRes, boolean longLink)
	{
		StringBuffer beforAuth = new StringBuffer();
		StringBuffer afterAuth = new StringBuffer();
		StringBuffer sb = new StringBuffer();
		StringBuffer entityBody = new StringBuffer();
		BufferedOutputStream sender = null;

		try
		{
			log.debug("  send ack socket is " + socket + "\r\n");
			sender = new BufferedOutputStream(socket.getOutputStream());
			beforAuth.append("HTTP/1.1 200 OK\r\n");
			if (longLink == true)
				beforAuth.append("Connection: Keep-Alive\r\n");
			else
				beforAuth.append("Connection: close\r\n");
			// beforAuth.append("Content-Type:
			// text/xml;charset=\""+Config.getCharSet()+"\""+"\r\n");
			beforAuth.append("Content-Type: text/xml;charset="
					+ Config.getCharSet() + "\r\n");
			// 设置鉴权
			byte[] bcontent = getContent(mm7VASPRes);
			// 设置消息体长度
			afterAuth.append("Content-Length: " + bcontent.length + "\r\n");
			// /////////
			// afterAuth.append("Connection: close\r\n");
			// ////////
			// afterAuth.append("Mime-Version:1.0"+"\r\n");
			// afterAuth.append("SOAPAction: \"\""+"\r\n");
			afterAuth.append("\r\n");
			sb.append(beforAuth);
			sb.append(afterAuth);
			entityBody.append(new String(bcontent));
			sb.append(entityBody);
			sender.write(sb.toString().getBytes());
			sender.flush();
			// System.out.println("发送完毕");
			log.debug("end sending ack!");
			return;
		}
		catch (InterruptedIOException iioe)
		{
			log.error(null, iioe);
			return;
		}
		catch (Exception e)
		{
			log.error(null, e);
			return;
		}
	}

	private byte[] encodeMessage(MM7VASPRes mm7VASPRes)
	{
		ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
		StringBuffer sb = new StringBuffer();

		sb
				.append("<?xml version=\"1.0\"?><env:Envelope xmlns:env=\"http://schemas.xmlsoap.org/soap/envelope/\"><env:Header>");//
		if (mm7VASPRes.isTransactionIDExist()) //
			sb
					.append("<mm7:TransactionID xmlns:mm7=\"http://www.3gpp.org/ftp/Specs/archive/23_series/23.140/schema/REL-6-MM7-1-0\">"
							+ mm7VASPRes.getTransactionID()
							+ "</mm7:TransactionID>");
		else
			System.err.println("TransactionID 不许为空！");
		sb.append("</env:Header><env:Body>");

		/**
		 * 若发送的消息为MM7SubmitReq
		 */
		if (mm7VASPRes instanceof MM7DeliverRes)
		{
			MM7DeliverRes deliverRes = (MM7DeliverRes) mm7VASPRes;
			sb
					.append("<DeliverRsp xmlns=\"http://www.3gpp.org/ftp/Specs/archive/23_series/23.140/schema/REL-6-MM7-1-0\">");
			if (deliverRes.isMM7VersionExist())
			{
				sb.append("<MM7Version>" + deliverRes.getMM7Version()
						+ "</MM7Version>");
			}
			else
			{
				System.err.println("MM7Version 不许为空！");
			}

			if (deliverRes.isServiceCodeExist())
			{
				sb.append("<ServiceCode>" + deliverRes.getServiceCode()
						+ "</ServiceCode>");
			}

			sb.append("<Status>");
			if (deliverRes.isStatusCodeExist())
			{
				sb.append("<StatusCode>" + deliverRes.getStatusCode()
						+ "</StatusCode>");
			}
			else
			{
				System.err.println("StatusCode 不许为空");
			}

			if (deliverRes.isStatusTextExist())
			{
				sb.append("<StatusText>" + deliverRes.getStatusText()
						+ "</StatusText>");
			}
			if (deliverRes.isStatusDetailExist())
			{
				sb.append("<Details>" + deliverRes.getStatusDetail()
						+ "</Details>");
			}
			sb.append("</Status>");

			sb.append("</DeliverRsp>");
			sb.append("</env:Body></env:Envelope>");
			sb.append("\r\n");
		}
		else if (mm7VASPRes instanceof MM7DeliveryReportRes)
		{
			MM7DeliveryReportRes deliveryReportRes = (MM7DeliveryReportRes) mm7VASPRes;
			sb
					.append("<DeliveryReportRsp xmlns=\"http://www.3gpp.org/ftp/Specs/archive/23_series/23.140/schema/REL-6-MM7-1-0\">");
			if (deliveryReportRes.isMM7VersionExist())
			{
				sb.append("<MM7Version>" + deliveryReportRes.getMM7Version()
						+ "</MM7Version>");
			}
			else
			{
				System.err.println("MM7Version 不许为空！");
			}

			sb.append("<Status>");
			if (deliveryReportRes.isStatusCodeExist())
			{
				sb.append("<StatusCode>" + deliveryReportRes.getStatusCode()
						+ "</StatusCode>");
			}
			else
			{
				System.err.println("StatusCode 不许为空");
			}

			if (deliveryReportRes.isStatusTextExist())
			{
				sb.append("<StatusText>" + deliveryReportRes.getStatusText()
						+ "</StatusText>");
			}
			if (deliveryReportRes.isStatusDetailExist())
			{
				sb.append("<Details>" + deliveryReportRes.getStatusDetail()
						+ "</Details>");
			}
			sb.append("</Status>");

			sb.append("</DeliveryReportRsp>");
			sb.append("</env:Body></env:Envelope>");
			sb.append("\r\n");
		}
		else if (mm7VASPRes instanceof MM7ReadReplyRes)
		{
			MM7ReadReplyRes readReplyRes = (MM7ReadReplyRes) mm7VASPRes;
			sb
					.append("<ReadReplyRsp xmlns=\"http://www.3gpp.org/ftp/Specs/archive/23_series/23.140/schema/REL-6-MM7-1-0\">");
			if (readReplyRes.isMM7VersionExist())
			{
				sb.append("<MM7Version>" + readReplyRes.getMM7Version()
						+ "</MM7Version>");
			}
			else
			{
				System.err.println("MM7Version 不许为空！");
			}

			sb.append("<Status>");
			if (readReplyRes.isStatusCodeExist())
			{
				sb.append("<StatusCode>" + readReplyRes.getStatusCode()
						+ "</StatusCode>");
			}
			else
			{
				System.err.println("StatusCode 不许为空");
			}

			if (readReplyRes.isStatusTextExist())
			{
				sb.append("<StatusText>" + readReplyRes.getStatusText()
						+ "</StatusText>");
			}
			if (readReplyRes.isStatusDetailExist())
			{
				sb.append("<Details>" + readReplyRes.getStatusDetail()
						+ "</Details>");
			}
			sb.append("</Status>");

			sb.append("</ReadReplyRsp>");
			sb.append("</env:Body></env:Envelope>");
			sb.append("\r\n");
		}
		try
		{
			byteOutput.write(sb.toString().getBytes());
		}
		catch (Exception e)
		{
			log.error(null, e);
		}

		return byteOutput.toByteArray();
	}

	private byte[] getContent(MM7VASPRes mm7VASPRes)
	{
		byte[] b = null;

		b = encodeMessage(mm7VASPRes);
		String strMessage = new String(b);
		int index = strMessage.indexOf(MMConstants.BEGINXMLFLAG);
		strEnvelope = strMessage.substring(index);
		return b;
		/*
		 * byte[] b = null; SOAPEncoder encoder = new SOAPEncoder();
		 * encoder.setMessage(mm7VASPRes); try{ encoder.encodeMessage();
		 * }catch(Exception e){ System.err.println(e); } b =
		 * encoder.getMessage(); return(b);
		 */
	}

	public static String getBASE64(String value)
	{
		if (value == null)
			return null;
		BASE64Encoder BaseEncode = new BASE64Encoder();
		return (BaseEncode.encode(value.getBytes()));
	}

	private String calcMD5(String str)
	{
		try
		{
			MessageDigest alga = MessageDigest.getInstance("MD5");
			alga.update(str.getBytes());
			byte[] digesta = alga.digest();
			return byte2hex(digesta);
		}
		catch (NoSuchAlgorithmException ex)
		{
			// System.out.println("出错了！！");
		}
		return "NULL";
	}

	// byte[]数组转成字符串
	public String byte2hex(byte[] b)
	{
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++)
		{
			stmp = (Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1)
				hs = hs + "0" + stmp;
			else
				hs = hs + stmp;
			if (n < b.length - 1)
				hs = hs + "";
		}
		return hs;
	}

	private boolean digestAuth(Socket socket, ByteArrayOutputStream baos, int nc)
	{
		int index1 = 0, index2 = 0;
		String digest, temp;
		String username, realm, qop, nonce, ncValue, cnonce, response, uri;
		String MD5A1, MD5A2;
		index1 = baos.toString().indexOf("Authorization");
		index2 = baos.toString().indexOf("\r\n", index1);
		if (index1 == -1 || index2 == -1)
		{
			sendDigestReq(socket);
			return false;
		}
		index1 = index1 + "Authorization".length() + 1;
		digest = baos.toString().substring(index1, index2);

		if (digest.indexOf("Digest") == -1)
		{
			sendDigestReq(socket);
			return false;
		}

		/* 校验username */
		if ((index1 = digest.indexOf("username=\"")) == -1)
		{
			sendDigestReq(socket);
			return false;
		}
		index1 = index1 + new String("username=\"").length();
		if ((index2 = digest.indexOf("\"", index1)) == -1)
		{
			sendDigestReq(socket);
			return false;
		}
		username = digest.substring(index1, index2);
		if (!username.equals(Config.getUserName()))
		{
			sendDigestReq(socket);
			return false;
		}

		/* 校验realm */
		if ((index1 = digest.indexOf("realm=\"")) == -1)
		{
			sendDigestReq(socket);
			return false;
		}
		index1 = index1 + new String("realm=\"").length();
		if ((index2 = digest.indexOf("\"", index1)) == -1)
		{
			sendDigestReq(socket);
			return false;
		}
		realm = digest.substring(index1, index2);
		if (!realm.equals(MMConstants.REALM))
		{
			// System.out.println("realm错误");
			sendDigestReq(socket);
			return false;
		}

		/* 校验qop */
		if ((index1 = digest.indexOf("qop=\"")) == -1)
		{
			sendDigestReq(socket);
			return false;
		}
		index1 = index1 + new String("qop=\"").length();
		if ((index2 = digest.indexOf("\"", index1)) == -1)
		{
			sendDigestReq(socket);
			return false;
		}
		qop = digest.substring(index1, index2);
		if (!qop.equals("auth"))
		{
			sendDigestReq(socket);
			return false;
		}

		/* 取得uri */
		if ((index1 = digest.indexOf("uri=\"")) == -1)
		{
			sendDigestReq(socket);
			return false;
		}
		index1 = index1 + new String("uri=\"").length();
		if ((index2 = digest.indexOf("\"", index1)) == -1)
		{
			sendDigestReq(socket);
			return false;
		}
		uri = digest.substring(index1, index2);
		/* 校验nonce */
		if ((index1 = digest.indexOf("nonce=\"")) == -1)
		{
			sendDigestReq(socket);
			return false;
		}
		index1 = index1 + new String("nonce=\"").length();
		if ((index2 = digest.indexOf("\"", index1)) == -1)
		{
			sendDigestReq(socket);
			return false;
		}
		nonce = digest.substring(index1, index2);
		if (!nonce.equals(getBASE64("--NextPart_0_2817_24856")))
		{
			sendDigestReq(socket);
			return false;
		}

		/* 校验nc */
		if ((index1 = digest.indexOf("nc=")) == -1)
		{
			sendDigestReq(socket);
			return false;
		}
		index1 = index1 + new String("nc").length() + 1;
		if ((index2 = digest.indexOf(",", index1)) == -1)
		{
			sendDigestReq(socket);
			return false;
		}
		ncValue = digest.substring(index1, index2);
		if (Integer.parseInt(ncValue) != nc)
		{
			sendDigestReq(socket);
			return false;
		}

		/* 取得cnonce */
		if ((index1 = digest.indexOf("cnonce=\"")) == -1)
		{
			sendDigestReq(socket);
			return false;
		}
		index1 = index1 + new String("cnonce=\"").length();
		if ((index2 = digest.indexOf("\"", index1)) == -1)
		{
			sendDigestReq(socket);
			return false;
		}
		cnonce = digest.substring(index1, index2);
		/* 校验response */
		if ((index1 = digest.indexOf("response=\"")) == -1)
		{
			sendDigestReq(socket);
			return false;
		}
		index1 = index1 + new String("response=\"").length();
		if ((index2 = digest.indexOf("\"", index1)) == -1)
		{
			sendDigestReq(socket);
			return false;
		}
		response = digest.substring(index1, index2).trim();
		MD5A1 = calcMD5(Config.getUserName() + ":" + MMConstants.REALM + ":"
				+ Config.getPassword());
		MD5A2 = calcMD5("POST" + ":" + uri);
		temp = calcMD5(MD5A1 + ":" + nonce + ":" + ncValue + ":" + cnonce + ":"
				+ qop + ":" + MD5A2);
		if (!temp.trim().equals(response))
		{
			sendDigestReq(socket);
			return false;
		}

		return true;
	}

	private boolean basicAuth(Socket socket, ByteArrayOutputStream baos)
	{
		int index1 = 0, index2 = 0, index3 = 0;
		String auth = "Authorization";
		String basic, temp;

		index1 = baos.toString().indexOf(auth);
		index2 = baos.toString().indexOf("\r\n", index1);
		int index = index1 + auth.length() + 1;
		if (index1 == -1 || index2 == -1)
		{
			sendBasicReq(socket);
			return false;
		}
		basic = baos.toString().substring(index, index2);

		if (basic.indexOf("Basic") == -1)
		{
			sendBasicReq(socket);
			return false;
		}
		if (basic.indexOf(getBASE64(Config.getUserName() + ":"
				+ Config.getPassword())) == -1)
		{
			sendBasicReq(socket);
			return false;
		}

		return true;
	}

	private void sendBasicReq(Socket socket)
	{
		StringBuffer sb = new StringBuffer();
		sb.append("HTTP/1.1 401 Authorization Required\r\n");
		sb.append("WWW-Authenticate: Basic ");
		sb.append("realm=\"" + MMConstants.REALM + "\"\r\n\r\n");

		try
		{
			BufferedOutputStream sender = new BufferedOutputStream(socket
					.getOutputStream());
			sender.write(sb.toString().getBytes());
			sender.flush();
		}
		catch (Exception e)
		{
			System.err.println(e);
		}
	}

	private void sendDigestReq(Socket socket)
	{
		StringBuffer sb = new StringBuffer();
		sb.append("HTTP/1.1 401 Authorization Required\r\n");
		sb.append("WWW-Authenticate: Digest ");
		sb.append("realm=\"" + MMConstants.REALM + "\", ");
		sb.append("nonce=\"" + getBASE64("--NextPart_0_2817_24856") + "\", ");
		sb.append("algorithm=MD5, qop=\"auth\"\r\n\r\n");

		try
		{
			BufferedOutputStream sender = new BufferedOutputStream(socket
					.getOutputStream());
			sender.write(sb.toString().getBytes());
			sender.flush();
		}
		catch (Exception e)
		{
			System.err.println(e);
		}
	}
}
