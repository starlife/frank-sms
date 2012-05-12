/**
 * File Name:MM7Sender.java Company: 中国移动2011
 */
package com.cmcc.mm7.vasp.service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import sun.misc.BASE64Encoder;

import com.cmcc.mm7.vasp.common.ConnectionPool;
import com.cmcc.mm7.vasp.common.ConnectionWrap;
import com.cmcc.mm7.vasp.common.MMConstants;
import com.cmcc.mm7.vasp.common.MMContent;
import com.cmcc.mm7.vasp.common.RetriveApiVersion;
import com.cmcc.mm7.vasp.common.SOAPEncoder;
import com.cmcc.mm7.vasp.conf.MM7Config;
import com.cmcc.mm7.vasp.message.MM7CancelReq;
import com.cmcc.mm7.vasp.message.MM7CancelRes;
import com.cmcc.mm7.vasp.message.MM7RSErrorRes;
import com.cmcc.mm7.vasp.message.MM7RSRes;
import com.cmcc.mm7.vasp.message.MM7ReplaceReq;
import com.cmcc.mm7.vasp.message.MM7ReplaceRes;
import com.cmcc.mm7.vasp.message.MM7SubmitReq;
import com.cmcc.mm7.vasp.message.MM7SubmitRes;
import com.cmcc.mm7.vasp.message.MM7VASPReq;

public class MM7Sender extends Thread
{
	private static final Log log = LogFactory.getLog(MM7Sender.class);
	private MM7Config mm7Config;
	private BufferedOutputStream sender;
	private BufferedInputStream receiver;
	private StringBuffer beforAuth;
	private String AuthInfor;
	private StringBuffer afterAuth;
	private StringBuffer entityBody;
	private ConnectionPool pool;
	private ConnectionWrap connWrap;
	private int ResendCount;
	private Socket client;
	// private int ReadTimeOutCount = 0;
	// private ByteArrayOutputStream sendBaos;
	// private byte[] TimeOutbCount;

	/**
	 * 保存发送提交失败的包，便于重新发送
	 */
	private final LinkedBlockingQueue<MM7VASPReq> buffer = new LinkedBlockingQueue<MM7VASPReq>(
			10000);

	private volatile boolean stop = false;

	public MM7Sender() // 构造方法
	{
		reset();
	}

	@Override
	public void run()
	{
		log.info("正在启动发送线程。。。");
		while (!stop)
		{
			try
			{
				// 取彩信并发送
				// RateControl.controlRate();
				// 取包发送
				MM7VASPReq req = this.doSubmit();
				if (req == null)
				{
					req = buffer.poll();
				}

				if (req == null)
				{
					continue;
				}

				// 发送
				log.debug("发送前");
				MM7RSRes res = this.send(req);
				log.debug("发送后");
				log.info("res.statuscode=" + res.getStatusCode()
						+ ";res.statusText=" + res.getStatusText());
				dealRecv(req, res);

			}
			catch (Exception ex)
			{
				log.error(null, ex);
			}
		}

	}

	public MM7SubmitReq doSubmit()
	{
		return null;
	}

	public void dealRecv(MM7VASPReq req, MM7RSRes res)
	{
		log.info("dealSubmit:" + req + res);
	}

	private void reset()
	{
		// File f;
		mm7Config = new MM7Config();
		sender = null;
		receiver = null;
		AuthInfor = "";
		// beforAuth = new StringBuffer();
		afterAuth = new StringBuffer();
		entityBody = new StringBuffer();
		// res = new MM7RSRes();
		ResendCount = 0;
		connWrap = null;
		// Modify by hudm 2004-06-09
		// pool = ConnectionPool.getInstance();
		pool = ConnectionPool.getInstance();
		// /end Modify by hudm 2004-06-09
		client = null;

	}

	/** 构造方法 */
	public MM7Sender(MM7Config config) throws Exception
	{
		reset();
		mm7Config = config;
		pool.setConfig(mm7Config);
	}

	public void setConfig(MM7Config config) // 设置MM7Config
	{
		mm7Config = config;
		pool.setConfig(mm7Config);
	}

	public MM7Config getConfig() // 获得MM7Config
	{
		return (mm7Config);
	}

	public MM7RSRes send(MM7VASPReq mm7VASPReq) // 发送消息
	{
		MM7RSRes res = null;
		if (mm7VASPReq == null)
		{
			res = new MM7RSErrorRes();
			res.setStatusCode(-105);
			res.setStatusText("待发送的消息为空!");
			log.error(LogHelper.logRSErrorRes((MM7RSErrorRes) res));
			return res;
		}
		
		Socket client=this.getSocket();
		if(client==null)
		{
			res = new MM7RSErrorRes();
			res.setStatusCode( -104);
			res.setStatusText("创建Socket失败");
			log.error(LogHelper.logRSErrorRes((MM7RSErrorRes) res));
			return res;
		}
		
		beforAuth = new StringBuffer();
		afterAuth = new StringBuffer();
		entityBody = new StringBuffer();

		sender = null;
		receiver = null;
		// reset();

		try
		{
			String mmscURL = mm7Config.getMMSCURL();
			int httpindex = -1;
			int index = -1;
			if (mmscURL == null)
				mmscURL = "/";
			beforAuth.append("POST " + mmscURL + " HTTP/1.1\r\n");
			beforAuth.append("Host:"
					+ client.getInetAddress().getHostAddress()
					+ "\r\n");
			
			// 设置Host结束
			/** 设置ContentType，不带附件为text/xml;带附件为multipart/related */
			if (mm7VASPReq instanceof MM7SubmitReq)
			{
				MM7SubmitReq submitReq = (MM7SubmitReq) mm7VASPReq;

				log.info(LogHelper.logSubmitReq(submitReq));

				if (submitReq.isContentExist())
					beforAuth
							.append("Content-Type:multipart/related; boundary=\"--NextPart_0_2817_24856\";"
									+ "type=\"text/xml\";start=\"</tnn-200102/mm7-vasp>\""
									+ "\r\n");
				else
					beforAuth.append("Content-Type:text/xml;charset=\""
							+ mm7Config.getCharSet() + "\"" + "\r\n");
			}
			else if (mm7VASPReq instanceof MM7ReplaceReq)
			{
				MM7ReplaceReq replaceReq = (MM7ReplaceReq) mm7VASPReq;

				log.info(LogHelper.logReplaceReq(replaceReq));

				if (replaceReq.isContentExist())
					beforAuth
							.append("Content-Type:multipart/related; boundary=\"--NextPart_0_2817_24856\";"
									+ "\r\n");
				else
					beforAuth.append("Content-Type:text/xml;charset=\""
							+ mm7Config.getCharSet() + "\"" + "\r\n");
			}
			else if (mm7VASPReq instanceof MM7CancelReq)
			{
				MM7CancelReq cancelReq = (MM7CancelReq) mm7VASPReq;

				log.info(LogHelper.logCancelReq(cancelReq));

				beforAuth.append("Content-Type:text/xml;charset=\""
						+ mm7Config.getCharSet() + "\"" + "\r\n");
			}
			else
			{
				MM7RSErrorRes ErrorRes = new MM7RSErrorRes();
				ErrorRes.setStatusCode(-106);
				ErrorRes.setStatusText("没有匹配的消息，请确认要发送的消息是否正确！");
				log.error(LogHelper.logRSErrorRes(ErrorRes));
				return ErrorRes;
			}
			// 设置ContentType结束
			// 设置Content-Trans-Encoding
			beforAuth.append("Content-Transfer-Encoding:8bit" + "\r\n");
			AuthInfor = "Authorization:Basic "
					+ getBASE64(mm7Config.getUserName() + ":"
							+ mm7Config.getPassword()) + "\r\n";

			afterAuth.append("SOAPAction:\"\"" + "\r\n");
			RetriveApiVersion apiver = new RetriveApiVersion();
			afterAuth
					.append("MM7APIVersion:" + apiver.getApiVersion() + "\r\n");
			/**
			 * 判断是否是长连接，若是长连接，则将Connection设为keep-alive，
			 * 否则设为close，以告诉服务器端客户端是长连接还是短连接
			 */
			if (pool.getKeepAlive().equals("on"))
			{
				afterAuth.append("Connection: Keep-Alive" + "\r\n");
			}
			else
			{
				afterAuth.append("Connection:Close" + "\r\n");
			}

			byte[] bcontent = getContent(mm7VASPReq);
			if (bcontent.length > mm7Config.getMaxMsgSize())
			{
				MM7RSErrorRes ErrorRes = new MM7RSErrorRes();
				ErrorRes.setStatusCode(-113);
				ErrorRes.setStatusText("消息内容的尺寸超出允许发送的大小！");
				log.error(LogHelper.logRSErrorRes(ErrorRes));
				return ErrorRes;
			}

			// 设置消息体长度
			afterAuth.append("Content-Length:" + bcontent.length + "\r\n");
			afterAuth.append("Mime-Version:1.0" + "\r\n");
			afterAuth.append("\r\n");
			entityBody.append(new String(bcontent));
			ByteArrayOutputStream sendBaos = getSendMessage(beforAuth
					.toString(), AuthInfor, afterAuth.toString(), bcontent);

			if (sendBaos != null)
			{
				res = SendandReceiveMessage(sendBaos);
			}
			else
			{
				MM7RSErrorRes ErrorRes = new MM7RSErrorRes();
				ErrorRes.setStatusCode(-104);
				ErrorRes.setStatusText("Socket不通！");
				log.error(LogHelper.logRSErrorRes(ErrorRes));
				return ErrorRes;
			}

			log.info(bcontent);

			/** ********** */
			if (res instanceof MM7SubmitRes)
			{
				log.info(LogHelper.logSubmitRes((MM7SubmitRes) res));
			}
			else if (res instanceof MM7CancelRes)
			{
				log.info(LogHelper.logCancelRes((MM7CancelRes) res));
			}
			else if (res instanceof MM7ReplaceRes)
			{
				log.info(LogHelper.logReplaceRes((MM7ReplaceRes) res));
			}
			else if (res instanceof MM7RSErrorRes)
			{
				log.error(LogHelper.logRSErrorRes((MM7RSErrorRes) res));
			}

			return res;
		}
		catch (Exception e)
		{
			log.error(null, e);
			MM7RSErrorRes ErrorRes = new MM7RSErrorRes();
			ErrorRes.setStatusCode(-100);
			ErrorRes.setStatusText("系统错误！原因：" + e);
			return ErrorRes;
		}
	}

	private byte[] getContent(MM7VASPReq mm7VASPReq)
	{
		byte[] b = null;
		SOAPEncoder encoder = new SOAPEncoder(mm7Config);
		encoder.setMessage(mm7VASPReq);
		try
		{
			encoder.encodeMessage();
		}
		catch (Exception e)
		{
			log.error(null, e);
		}
		b = encoder.getMessage();
		return (b);
	}

	/** 进行BASE64编码 */
	public static String getBASE64(String value)
	{
		if (value == null)
			return null;
		BASE64Encoder BaseEncode = new BASE64Encoder();
		return (BaseEncode.encode(value.getBytes()));
	}

	private MM7RSRes parseXML(ByteArrayOutputStream baos)
	{
		SAXBuilder sax = new SAXBuilder();
		log.info("recv=" + baos.toString() + "\r\n");
		try
		{
			if (baos.toString() == null || baos.toString().equals(""))
			{
				MM7RSErrorRes errRes = new MM7RSErrorRes();
				errRes.setStatusCode(-107);
				errRes.setStatusText("错误！接收到的消息为空！");
				log.error(LogHelper.logRSErrorRes(errRes));
				return errRes;
			}
			else
			{
				// System.out.println("baos="+baos.toString());
				int index = -1;
				int xmlend = -1;
				index = baos.toString().indexOf(MMConstants.BEGINXMLFLAG);
				if (index == -1)
				{
					int httpindex = -1;
					httpindex = baos.toString().indexOf("HTTP1.1");
					String strstat = "";
					if (httpindex >= 0)
					{
						int index11 = baos.toString().indexOf("\r\n");
						strstat = baos.toString().substring(httpindex + 7,
								index11);
					}
					MM7RSErrorRes err = new MM7RSErrorRes();
					err.setStatusCode(-108);
					if (!strstat.equals(""))
						err.setStatusText(strstat);
					else
						err.setStatusText("Bad Request");
					log.error(LogHelper.logRSErrorRes(err));
					return err;
				}
				String xmlContent = baos.toString().substring(index,
						baos.toString().length());
				String xmlContentTemp = "";
				byte[] byteXML = baos.toByteArray();
				int index1 = xmlContent.indexOf("encoding=\"UTF-8\"");
				if (index1 > 0)
				{
					xmlContentTemp = new String(byteXML, "UTF-8");
					int xmlind = xmlContentTemp
							.indexOf(MMConstants.BEGINXMLFLAG);
					int xmlindend = xmlContentTemp.lastIndexOf("Envelope>");
					if (xmlindend > 0)
					{
						xmlContentTemp = xmlContentTemp.substring(xmlind,
								xmlindend + 9);
						String xml = xmlContentTemp.substring(0, index1)
								+ "encoding=\"GB2312\""
								+ xmlContentTemp.substring(index1
										+ "encoding=\"UTF-8\"".length());
						xmlContent = xml;
					}
				}
				log.info("!xmlContent=" + xmlContent + "!\r\n");
				ByteArrayInputStream in = new ByteArrayInputStream(xmlContent
						.getBytes());
				Document doc = sax.build(in);
				Element root = doc.getRootElement();
				Element first = (Element) root.getChildren().get(0);
				if (first.getName().equalsIgnoreCase("body"))
				{
					Element Message = (Element) first.getChildren().get(0);
					if (Message.getName().equals("Fault"))
					{
						MM7RSErrorRes errRes = new MM7RSErrorRes();
						errRes.setStatusCode(-110);
						errRes
								.setStatusText("Server could not fulfill the request");
						log.error(LogHelper.logRSErrorRes(errRes));
						return errRes;
					}
					else
					{
						MM7RSErrorRes errRes = new MM7RSErrorRes();
						errRes.setStatusCode(-111);
						errRes.setStatusText("Server error");
						log.error(LogHelper.logRSErrorRes(errRes));
						return errRes;
					}
				}
				else
				{
					Element envBody = (Element) root.getChildren().get(1);
					Element Message = (Element) envBody.getChildren().get(0);
					Element envHeader = (Element) root.getChildren().get(0);
					Element transID = (Element) envHeader.getChildren().get(0);
					String transactionID = transID.getTextTrim();
					int size = Message.getChildren().size();
					log.info("Message.getName()=" + Message.getName() + "\r\n");
					if (Message.getName().equals("SubmitRsp"))
					{
						MM7SubmitRes submitRes = new MM7SubmitRes();
						submitRes.setTransactionID(transactionID);
						for (int i = 0; i < size; i++)
						{
							Element ele = (Element) Message.getChildren()
									.get(i);
							if (ele.getName().equals(MMConstants.STATUS))
							{
								for (int j = 0; j < ele.getChildren().size(); j++)
								{
									Element subEle = (Element) ele
											.getChildren().get(j);
									if (subEle.getName().equals(
											MMConstants.STATUSCODE))
										submitRes
												.setStatusCode(Integer
														.parseInt(subEle
																.getTextTrim()));
									else if (subEle.getName().equals(
											MMConstants.STATUSTEXT))
										submitRes.setStatusText(subEle
												.getTextTrim());
									else if (subEle.getName().equals(
											MMConstants.STATUSDETAIL))
										submitRes.setStatusDetail(subEle
												.getTextTrim());
								}
							}
							else if (ele.getName()
									.equals(MMConstants.MESSAGEID))
							{
								submitRes.setMessageID(ele.getTextTrim());
							}
						}
						return submitRes;
					}
					else if (Message.getName().equals("CancelRsp"))
					{
						MM7CancelRes cancelRes = new MM7CancelRes();
						cancelRes.setTransactionID(transactionID);
						for (int i = 0; i < size; i++)
						{
							Element ele = (Element) Message.getChildren()
									.get(i);
							if (ele.getName().equals(MMConstants.STATUS))
							{
								for (int j = 0; j < ele.getChildren().size(); j++)
								{
									Element subEle = (Element) ele
											.getChildren().get(j);
									if (subEle.getName().equals(
											MMConstants.STATUSCODE))
										cancelRes
												.setStatusCode(Integer
														.parseInt(subEle
																.getTextTrim()));
									else if (subEle.getName().equals(
											MMConstants.STATUSTEXT))
										cancelRes.setStatusText(subEle
												.getTextTrim());
									else if (subEle.getName().equals(
											MMConstants.STATUSDETAIL))
										cancelRes.setStatusDetail(subEle
												.getTextTrim());
								}
							}
						}
						return cancelRes;
					}
					else if (Message.getName().equals("ReplaceRsp"))
					{
						MM7ReplaceRes replaceRes = new MM7ReplaceRes();
						replaceRes.setTransactionID(transactionID);
						for (int i = 0; i < size; i++)
						{
							Element ele = (Element) Message.getChildren()
									.get(i);
							if (ele.getName().equals(MMConstants.STATUS))
							{
								for (int j = 0; j < ele.getChildren().size(); j++)
								{
									Element subEle = (Element) ele
											.getChildren().get(j);
									if (subEle.getName().equals(
											MMConstants.STATUSCODE))
										replaceRes
												.setStatusCode(Integer
														.parseInt(subEle
																.getTextTrim()));
									else if (subEle.getName().equals(
											MMConstants.STATUSTEXT))
										replaceRes.setStatusText(subEle
												.getTextTrim());
									else if (subEle.getName().equals(
											MMConstants.STATUSDETAIL))
										replaceRes.setStatusDetail(subEle
												.getTextTrim());
								}
							}
						}
						return replaceRes;
					}
					else
					{
						MM7RSRes res = new MM7RSRes();
						res.setTransactionID(transactionID);
						for (int i = 0; i < size; i++)
						{
							Element ele = (Element) Message.getChildren()
									.get(i);
							if (ele.getName().equals(MMConstants.STATUS))
							{
								for (int j = 0; j < ele.getChildren().size(); j++)
								{
									Element subEle = (Element) ele
											.getChildren().get(j);
									if (subEle.getName().equals(
											MMConstants.STATUSCODE))
										res
												.setStatusCode(Integer
														.parseInt(subEle
																.getTextTrim()));
									else if (subEle.getName().equals(
											MMConstants.STATUSTEXT))
										res.setStatusText(subEle.getTextTrim());
									else if (subEle.getName().equals(
											MMConstants.STATUSDETAIL))
										res.setStatusDetail(subEle
												.getTextTrim());
								}
							}
						}
						return res;
					}
				}
			}
		}
		catch (JDOMException jdome)
		{
			MM7RSErrorRes error = new MM7RSErrorRes();
			log.error(null, jdome);
			error.setStatusCode(-109);
			error.setStatusText("XML解析错误！原因：" + jdome);
			log.error(LogHelper.logRSErrorRes(error));
			return error;
		}
		catch (Exception e)
		{
			log.error(null, e);
			MM7RSErrorRes error = new MM7RSErrorRes();
			error.setStatusCode(-100);
			error.setStatusText("系统错误！原因：" + e);
			return error;
		}
	}

	private ByteArrayOutputStream getSendMessage(String beforAuth,
			String AuthInfor, String afterAuth, byte[] bcontent)
	{
		ByteArrayOutputStream sendBaos = new ByteArrayOutputStream();
		StringBuffer sb = new StringBuffer();
		sb.append(beforAuth);
		sb.append(AuthInfor);
		sb.append(afterAuth);
		try
		{
			log.debug("!part of send message is:" + sb.toString() + "!\r\n");
			sendBaos.write(sb.toString().getBytes());
			sendBaos.write(bcontent);
			return sendBaos;
		}
		catch (IOException e)
		{
			log.error(null, e);
			return null;
		}
	}

	private Socket getSocket()
	{
		Socket client = null;
		try
		{
			/**
			 * 在此加以判断，如果是长连接，则得到以前的连接，否则新建连接
			 */
			if (pool.getKeepAlive().equals("on"))
			{
				// SevereBuffer.append("!767!TimeOutFlag==false");
				connWrap = pool.getConnWrap();

				if (connWrap != null)
				{
					
					client = connWrap.getSocket();
	
					
					if (connWrap.getUserfulFlag() == false
							|| client.isConnected() == false)
					{
						log
								.debug("!771!connWrap.getUserfulFlag() == false || client.isConnected() == false");
						pool.deleteURL(connWrap);
						connWrap = pool.getConnWrap();

					}
					if (connWrap.getAuthFlag() == true)
					{
						AuthInfor = connWrap.getDigestInfor();
					}
					// ///end add by hudm
				}
				else
					client = null;

			}
			else
			{
				String MMSCIP = (String) mm7Config.getMMSCIP().get(0);
				int index = MMSCIP.indexOf(":");
				String ip;
				int port;
				if (index == -1)
				{
					ip = MMSCIP;
					port = 80;
				}
				else
				{
					ip = MMSCIP.substring(0, index);
					port = Integer.parseInt(MMSCIP.substring(index + 1));
				}
				client = new Socket(ip, port);
			}
			if (client != null)
			{
				log.debug("!829!client != null and equals" + client);
				sender = new BufferedOutputStream(client.getOutputStream());
				receiver = new BufferedInputStream(client.getInputStream());
				client.setSoTimeout(mm7Config.getTimeOut());
				client.setKeepAlive(true);

			}
			else
			{
				log.debug("!853!client == null");

			}
		}
		catch (Exception e)
		{
			// System.out.println("SocketException!原因："+se);
			log.error(null, e);
		}
		return client;
	}

	
	private MM7RSRes SendandReceiveMessage(ByteArrayOutputStream sendbaos)
	{
		MM7RSRes res = null;
		try
		{
			// ///////
			// SevereBuffer.append("sendbaos="+sendbaos.toString());
			// //////
			sender.write(sendbaos.toByteArray());
			sender.flush();
			log.debug("before receiveMessge");
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			if (this.receiveMessge(baos))
			{
				log.info("收到消息：" + baos.toString());
				// /log
				/*
				 * String env = ""; int envbeg =
				 * baos.toString().indexOf(MMConstants.BEGINXMLFLAG); int envend =
				 * baos.toString().indexOf("</env:Envelope>"); if (envbeg > 0 &&
				 * envend > 0) env = baos.toString().substring(envbeg);
				 * log.info(env);
				 */

				log.debug("after receiveMessge");
				log.debug("before parseXML");
				res = parseXML(baos);
				log.debug("after parseXML");
				return res;
			}
			else
			{
				MM7RSErrorRes error = new MM7RSErrorRes();
				error.setStatusCode(-102);
				error.setStatusText("接收失败！");
				log.info(LogHelper.logRSErrorRes(error));
				return error;
			}
		}
		catch (IOException ioe)
		{
			if (pool.deleteURL(connWrap))
			{
				log.debug("  pool.deleteURL(TimeOutWrap)");
			}
			else
			{
				log.debug("deleteURL is false!");
			}
			log.debug("TimeOutWrap=" + connWrap);
			log.error(null, ioe);

		}
		return res;
	}

	public boolean receiveMessge(ByteArrayOutputStream baos) throws IOException
	{
		if (baos == null)
		{
			return false;
		}
		try
		{
			baos.reset();
			boolean bReceive = false;
			byte[] data = new byte[MMConstants.ARRAYLIMIT];
			int word = -1;
			int readline = -1;
			int totalline = 0;
			boolean flag = false;
			boolean bHead = false;
			int readlineOut = 1;
			int envelopecount = 0;
			while (1 > 0)
			{
				if (flag)
					break;
				readline = receiver.read(data);
				if (readline != -1)
				{
					baos.write(data, 0, readline);
					// System.out.println("baos==="+baos.toString());
					totalline = totalline + readline;
					if (baos.toString().indexOf("\r\n\r\n") < 0)
						continue;
					if (bHead == false)
					{
						int httpindex = baos.toString().indexOf("HTTP/1.1");
						if (httpindex != -1)
						{
							String httpCode = baos.toString().substring(
									httpindex + 8, httpindex + 12).trim();
							int httpsepindex = baos.toString().indexOf(
									"\r\n\r\n");
							if (httpCode.equals("401"))
							{
								if (baos.toString().indexOf("Digest") != -1)
								{
									if (ResendCount < mm7Config
											.getReSendCount())
									{
										ResendCount = ResendCount + 1;
										pool.setInitNonceCount();
										String clientAuthInfor = "";
										String authInfor = "";
										int authindex = baos
												.toString()
												.indexOf(
														MMConstants.AUTHENTICATION);
										if (authindex > 0)
										{
											int lineend = baos.toString()
													.indexOf("\r",
															authindex + 1);
											int linebeg = authindex
													+ MMConstants.AUTHENTICATION
															.length() + 1;
											authInfor = baos
													.toString()
													.substring(linebeg, lineend);
											clientAuthInfor = setDigestAuth(authInfor);
										}
										int connectionindex = baos.toString()
												.toLowerCase().indexOf(
														MMConstants.CONNECTION);
										int connlength = connectionindex
												+ MMConstants.CONNECTION
														.length() + 1;
										int connectionend = baos.toString()
												.indexOf("\r\n",
														connectionindex);
										String ConnectionFlag = "";
										if (connectionindex != -1
												&& connectionend != -1)
											ConnectionFlag = baos.toString()
													.substring(connlength,
															connectionend);
										StringBuffer sb = new StringBuffer();
										sb.append(beforAuth);
										sb.append(clientAuthInfor);
										sb.append(afterAuth);
										sb.append(entityBody);
										sender.write(sb.toString().getBytes());
										sender.flush();
										baos = new ByteArrayOutputStream();
										data = new byte[MMConstants.ARRAYLIMIT];
										int resline = -1;
										int totalresline = 0;
										boolean excuteFlag = false;
										int httpseper = -1;
										int contlen3 = -1;
										while (1 > 0)
										{
											resline = receiver.read(data);
											if (resline == -1)
												break;
											baos.write(data, 0, resline);
											totalresline += resline;
											if (baos.toString().indexOf(
													"\r\n\r\n") < 0)
												continue;
											if (excuteFlag == false)
											{
												httpindex = baos.toString()
														.indexOf("HTTP/1.1");
												httpCode = baos.toString()
														.substring(
																httpindex + 8,
																httpindex + 12)
														.trim();
												int conlen1 = baos
														.toString()
														.toLowerCase()
														.indexOf(
																MMConstants.CONTENT_LENGTH);
												int conlen2 = baos.toString()
														.indexOf("\r", conlen1);
												contlen3 = Integer
														.parseInt(baos
																.toString()
																.substring(
																		(conlen1
																				+ MMConstants.CONTENT_LENGTH
																						.length() + 1),
																		conlen2)
																.trim());
												httpseper = baos.toString()
														.indexOf("\r\n\r\n");
												if (httpCode.equals("200"))
												{
													// 还要再加判断客户端是否是长连接
													ResendCount = 0;
													excuteFlag = true;
													if (ConnectionFlag
															.trim()
															.toLowerCase()
															.equals(
																	"keep-alive"))
													{
														pool
																.setNonceCount(Integer
																		.toString(Integer
																				.parseInt(
																						pool
																								.getNonceCount(),
																						8) + 1));
														connWrap
																.setDigestInfor(setDigestAuth(authInfor));
														continue;
													}
												}
											}
											// 开始接收http体
											if (totalresline == httpseper
													+ contlen3 + 4)
											{
												/*
												 * if(this.TimeOutFlag==true){
												 * SevereBuffer.append("baos.tostring()=="+baos.toString()); }
												 */
												if (pool.getKeepAlive().equals(
														"off"))
												{
													sender.close();
													receiver.close();
													client.close();
												}
												else
												{
													connWrap
															.setUserfulFlag(true);
													connWrap.setFree(true);
												}
												flag = true;
												bReceive = true;
												break;
											}
										}
									}
									else if (baos.toString().indexOf("Basic") != -1)
									{
										if (ResendCount < mm7Config
												.getReSendCount())
										{
											ResendCount = ResendCount + 1;
											receiveMessge(baos);
										}
										else
										{
											bReceive = false;
											break;
										}
									}
								}
								else
								{
									bReceive = false;
									break;
								}
							}
							else
							{
								int index1 = baos.toString().toLowerCase()
										.indexOf(MMConstants.CONTENT_LENGTH);
								int index2 = baos.toString().indexOf("\r",
										index1);
								int contlength = 0;
								if (index1 == -1 || index2 == -1)
								{
									int encodingindex = baos.toString()
											.toLowerCase().indexOf(
													"transfer-encoding:");
									if (encodingindex >= 0)
									{
										int encodingend = baos.toString()
												.indexOf("\r\n", encodingindex);
										if (encodingend >= 0)
										{
											String strenc = baos
													.toString()
													.substring(
															encodingindex
																	+ "transfer-encoding:"
																			.length(),
															encodingend).trim();
											if (strenc
													.equalsIgnoreCase("chunked"))
											{
												// //在这里可以增加chunked的处理

												int endencindex2 = baos
														.toString()
														.indexOf("\r\n",
																encodingend + 1);
												if (endencindex2 >= 0)
												{
													int xmlbeg = baos
															.toString()
															.indexOf(
																	MMConstants.BEGINXMLFLAG,
																	endencindex2 + 1);
													if (xmlbeg > 0)
													{
														String strTempContLen = baos
																.toString()
																.substring(
																		endencindex2,
																		xmlbeg)
																.trim();
														contlength = Integer
																.parseInt(
																		strTempContLen,
																		16);
													}
												}
											}
											else
											{
												bReceive = false;
												break;
											}
										}
										else
										{
											bReceive = false;
											break;
										}
									}
									else
									{
										continue;
									}

									if (totalline >= httpsepindex + contlength
											+ 8)
									{
										log.debug("receive end");
										log.debug("baos.toString()=="
												+ baos.toString());
										if (pool.getKeepAlive().equals("off"))
										{
											sender.close();
											receiver.close();
											client.close();
										}
										else
										{
											connWrap.setUserfulFlag(true);
											connWrap.setFree(true);
										}
										bReceive = true;
										break;
									}

								}
								else
								{
									contlength = Integer
											.parseInt(baos
													.toString()
													.substring(
															(index1
																	+ MMConstants.CONTENT_LENGTH
																			.length() + 1),
															index2).trim());
									// 开始接收http体
									if (totalline == httpsepindex + contlength
											+ 4)
									{
										// System.out.println("正常接收结束");
										// add by hudm to test 104 problem
										// 2004-06-09

										log.debug("baos.tostring()=="
												+ baos.toString());
										// end add by hudm
										if (pool.getKeepAlive().equals("off"))
										{
											sender.close();
											receiver.close();
											client.close();
										}
										else
										{
											connWrap.setUserfulFlag(true);
											connWrap.setFree(true);
										}
										bReceive = true;
										break;
									}
								}
							}
						}
						else
						{
							bReceive = false;
							break;
						}

					}
				}

			}
			return bReceive;
		}
		catch (SocketTimeoutException ste)
		{
			// 这里要把发送失败的重新添加到队列
			MM7RSRes res = null;
			// System.out.println("第1100行。");
			log.debug("TimeOutWrap=" + connWrap);
			if (pool.deleteURL(connWrap))
			{
				log.debug("  pool.deleteURL(TimeOutWrap)");
			}
			else
			{
				log.debug("deleteURL is false!");
			}

			res.setStatusCode(-101);
			res.setStatusText("超时发送失败！");
			log.debug("[Comments={-101;" + res.getStatusText() + "}]");

			return false;
		}
	}
	
	
	public boolean receiveMessge_temp(ByteArrayOutputStream baos) throws IOException
	{
		if (baos == null)
		{
			return false;
		}
		try
		{
			baos.reset();
			boolean bReceive = false;
			byte[] data = new byte[MMConstants.ARRAYLIMIT];
			int word = -1;
			int readline = -1;
			int totalline = 0;
			boolean flag = false;
			boolean bHead = false;
			int readlineOut = 1;
			int envelopecount = 0;
			while (1 > 0)
			{
				if (flag)
					break;
				readline = receiver.read(data);
				if (readline != -1)
				{
					baos.write(data, 0, readline);
					// System.out.println("baos==="+baos.toString());
					totalline = totalline + readline;
					if (baos.toString().indexOf("\r\n\r\n") < 0)
						continue;
					if (bHead == false)
					{
						int httpindex = baos.toString().indexOf("HTTP/1.1");
						if (httpindex != -1)
						{
							String httpCode = baos.toString().substring(
									httpindex + 8, httpindex + 12).trim();
							int httpsepindex = baos.toString().indexOf(
									"\r\n\r\n");
							if (httpCode.equals("401"))
							{
								if (baos.toString().indexOf("Digest") != -1)
								{
									if (ResendCount < mm7Config
											.getReSendCount())
									{
										ResendCount = ResendCount + 1;
										pool.setInitNonceCount();
										String clientAuthInfor = "";
										String authInfor = "";
										int authindex = baos
												.toString()
												.indexOf(
														MMConstants.AUTHENTICATION);
										if (authindex > 0)
										{
											int lineend = baos.toString()
													.indexOf("\r",
															authindex + 1);
											int linebeg = authindex
													+ MMConstants.AUTHENTICATION
															.length() + 1;
											authInfor = baos
													.toString()
													.substring(linebeg, lineend);
											clientAuthInfor = setDigestAuth(authInfor);
										}
										int connectionindex = baos.toString()
												.toLowerCase().indexOf(
														MMConstants.CONNECTION);
										int connlength = connectionindex
												+ MMConstants.CONNECTION
														.length() + 1;
										int connectionend = baos.toString()
												.indexOf("\r\n",
														connectionindex);
										String ConnectionFlag = "";
										if (connectionindex != -1
												&& connectionend != -1)
											ConnectionFlag = baos.toString()
													.substring(connlength,
															connectionend);
										StringBuffer sb = new StringBuffer();
										sb.append(beforAuth);
										sb.append(clientAuthInfor);
										sb.append(afterAuth);
										sb.append(entityBody);
										sender.write(sb.toString().getBytes());
										sender.flush();
										baos = new ByteArrayOutputStream();
										data = new byte[MMConstants.ARRAYLIMIT];
										int resline = -1;
										int totalresline = 0;
										boolean excuteFlag = false;
										int httpseper = -1;
										int contlen3 = -1;
										while (1 > 0)
										{
											resline = receiver.read(data);
											if (resline == -1)
												break;
											baos.write(data, 0, resline);
											totalresline += resline;
											if (baos.toString().indexOf(
													"\r\n\r\n") < 0)
												continue;
											if (excuteFlag == false)
											{
												httpindex = baos.toString()
														.indexOf("HTTP/1.1");
												httpCode = baos.toString()
														.substring(
																httpindex + 8,
																httpindex + 12)
														.trim();
												int conlen1 = baos
														.toString()
														.toLowerCase()
														.indexOf(
																MMConstants.CONTENT_LENGTH);
												int conlen2 = baos.toString()
														.indexOf("\r", conlen1);
												contlen3 = Integer
														.parseInt(baos
																.toString()
																.substring(
																		(conlen1
																				+ MMConstants.CONTENT_LENGTH
																						.length() + 1),
																		conlen2)
																.trim());
												httpseper = baos.toString()
														.indexOf("\r\n\r\n");
												if (httpCode.equals("200"))
												{
													// 还要再加判断客户端是否是长连接
													ResendCount = 0;
													excuteFlag = true;
													if (ConnectionFlag
															.trim()
															.toLowerCase()
															.equals(
																	"keep-alive"))
													{
														pool
																.setNonceCount(Integer
																		.toString(Integer
																				.parseInt(
																						pool
																								.getNonceCount(),
																						8) + 1));
														connWrap
																.setDigestInfor(setDigestAuth(authInfor));
														continue;
													}
												}
											}
											// 开始接收http体
											if (totalresline == httpseper
													+ contlen3 + 4)
											{
												/*
												 * if(this.TimeOutFlag==true){
												 * SevereBuffer.append("baos.tostring()=="+baos.toString()); }
												 */
												if (pool.getKeepAlive().equals(
														"off"))
												{
													sender.close();
													receiver.close();
													client.close();
												}
												else
												{
													connWrap
															.setUserfulFlag(true);
													connWrap.setFree(true);
												}
												flag = true;
												bReceive = true;
												break;
											}
										}
									}
									else if (baos.toString().indexOf("Basic") != -1)
									{
										if (ResendCount < mm7Config
												.getReSendCount())
										{
											ResendCount = ResendCount + 1;
											receiveMessge(baos);
										}
										else
										{
											bReceive = false;
											break;
										}
									}
								}
								else
								{
									bReceive = false;
									break;
								}
							}
							else
							{
								int index1 = baos.toString().toLowerCase()
										.indexOf(MMConstants.CONTENT_LENGTH);
								int index2 = baos.toString().indexOf("\r",
										index1);
								int contlength = 0;
								if (index1 == -1 || index2 == -1)
								{
									int encodingindex = baos.toString()
											.toLowerCase().indexOf(
													"transfer-encoding:");
									if (encodingindex >= 0)
									{
										int encodingend = baos.toString()
												.indexOf("\r\n", encodingindex);
										if (encodingend >= 0)
										{
											String strenc = baos
													.toString()
													.substring(
															encodingindex
																	+ "transfer-encoding:"
																			.length(),
															encodingend).trim();
											if (strenc
													.equalsIgnoreCase("chunked"))
											{
												// //在这里可以增加chunked的处理

												int endencindex2 = baos
														.toString()
														.indexOf("\r\n",
																encodingend + 1);
												if (endencindex2 >= 0)
												{
													int xmlbeg = baos
															.toString()
															.indexOf(
																	MMConstants.BEGINXMLFLAG,
																	endencindex2 + 1);
													if (xmlbeg > 0)
													{
														String strTempContLen = baos
																.toString()
																.substring(
																		endencindex2,
																		xmlbeg)
																.trim();
														contlength = Integer
																.parseInt(
																		strTempContLen,
																		16);
													}
												}
											}
											else
											{
												bReceive = false;
												break;
											}
										}
										else
										{
											bReceive = false;
											break;
										}
									}
									else
									{
										continue;
									}

									if (totalline >= httpsepindex + contlength
											+ 8)
									{
										log.debug("receive end");
										log.debug("baos.toString()=="
												+ baos.toString());
										if (pool.getKeepAlive().equals("off"))
										{
											sender.close();
											receiver.close();
											client.close();
										}
										else
										{
											connWrap.setUserfulFlag(true);
											connWrap.setFree(true);
										}
										bReceive = true;
										break;
									}

								}
								else
								{
									contlength = Integer
											.parseInt(baos
													.toString()
													.substring(
															(index1
																	+ MMConstants.CONTENT_LENGTH
																			.length() + 1),
															index2).trim());
									// 开始接收http体
									if (totalline == httpsepindex + contlength
											+ 4)
									{
										// System.out.println("正常接收结束");
										// add by hudm to test 104 problem
										// 2004-06-09

										log.debug("baos.tostring()=="
												+ baos.toString());
										// end add by hudm
										if (pool.getKeepAlive().equals("off"))
										{
											sender.close();
											receiver.close();
											client.close();
										}
										else
										{
											connWrap.setUserfulFlag(true);
											connWrap.setFree(true);
										}
										bReceive = true;
										break;
									}
								}
							}
						}
						else
						{
							bReceive = false;
							break;
						}

					}
				}

			}
			return bReceive;
		}
		catch (SocketTimeoutException ste)
		{
			// 这里要把发送失败的重新添加到队列
			MM7RSRes res = null;
			// System.out.println("第1100行。");
			log.debug("TimeOutWrap=" + connWrap);
			if (pool.deleteURL(connWrap))
			{
				log.debug("  pool.deleteURL(TimeOutWrap)");
			}
			else
			{
				log.debug("deleteURL is false!");
			}

			res.setStatusCode(-101);
			res.setStatusText("超时发送失败！");
			log.debug("[Comments={-101;" + res.getStatusText() + "}]");

			return false;
		}
	}

	/** 输入string，输出经过MD5编码的String */
	public String calcMD5(String str)
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
			System.out.println("出错了！！没有这种算法！");
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

	private String setDigestAuth(String authinfor)
	{
		String auth = authinfor + "\r\n";
		int equal = -1;
		int comma = -1;
		StringBuffer authBuffer = new StringBuffer();
		authBuffer.append("Authorization: Digest ");
		String qopValue;
		String MD5A2;
		String algValue;
		String cnonceValue = "";
		String ncValue;
		String responseValue;
		String uri = mm7Config.getMMSCURL();
		if (uri == null)
			uri = "";
		String username = mm7Config.getUserName();
		authBuffer.append("uri=\"" + uri + "\",");
		authBuffer.append("username=\"" + username + "\",");

		String passwd = mm7Config.getPassword();
		// 获得realm的值
		int realmindex = auth.indexOf(MMConstants.REALM);
		equal = auth.indexOf("=", realmindex + 1);
		comma = auth.indexOf("\"", equal + 2);
		String realmValue = auth.substring(equal + 1, comma);
		if (realmValue.startsWith("\""))
			realmValue = realmValue.substring(1, realmValue.length());
		authBuffer.append("realm=\"" + realmValue + "\",");
		String MD5A1;
		// 取得nonce的值
		int nonceindex = auth.indexOf(MMConstants.NONCE);
		equal = auth.indexOf("=", nonceindex + 1);
		comma = auth.indexOf("\"", equal + 2);
		String nonceValue = auth.substring(equal + 1, comma);
		if (nonceValue.startsWith("\""))
			nonceValue = nonceValue.substring(1, nonceValue.length());
		authBuffer.append("nonce=\"" + nonceValue + "\",");
		// 判断有没有opaque，若有，则原封不动地返回给服务器
		int opaqueindex = auth.indexOf(MMConstants.OPAQUE);
		if (opaqueindex > 0)
		{
			equal = auth.indexOf("=", opaqueindex + 1);
			comma = auth.indexOf("\"", equal + 2);
			authBuffer.append("opaque=" + auth.substring(equal + 1, comma + 1));
		}
		// 取得algorithm的值
		int algindex = auth.indexOf(MMConstants.ALGORITHM);
		if (algindex > 0)
		{
			equal = auth.indexOf("=", algindex);
			comma = auth.indexOf(",", equal + 2);
			if (comma >= 0)
			{
				algValue = auth.substring(equal + 1, comma);
				if (algValue.startsWith("\""))
					algValue = algValue.substring(1, algValue.length() - 1);
			}
			else
			{
				comma = auth.indexOf("/r", equal);
				algValue = auth.substring(equal + 1, comma);
				if (algValue.startsWith("\""))
					algValue = algValue.substring(1, algValue.length());
			}
		}
		else
		{
			algValue = "MD5";
		}
		// 取得qop的值
		int qopindex = auth.indexOf(MMConstants.QOP);
		if (algValue.equals("MD5") || algValue.equals("MD5-sess"))
		{
			MD5A1 = calcMD5(username + ":" + realmValue + ":" + passwd);
			// 服务器存在qop这个字段
			if (qopindex > 0)
			{
				equal = auth.indexOf("=", qopindex);
				comma = auth.indexOf("\"", equal + 2);
				qopValue = auth.substring(equal + 1, comma);
				if (qopValue.startsWith("\""))
					qopValue = qopValue.substring(1, qopValue.length());
				/**
				 * 表明服务器给出了两种qop，为auth和auth-int， 这是应该是客户端自己选择采用哪种方式进行鉴权
				 */
				if (qopValue.indexOf(",") > 0)
				{
					if (mm7Config.getDigest().equals("auth-int"))
					{
						MD5A2 = calcMD5("POST" + ":" + uri + ":"
								+ calcMD5(entityBody.toString()));
					}
					else
					{
						MD5A2 = calcMD5("POST" + ":" + uri);
					}
					ncValue = String.valueOf(pool.getNonceCount());
					cnonceValue = getBASE64(ncValue);
					responseValue = calcMD5(MD5A1 + ":" + nonceValue + ":"
							+ ncValue + ":" + cnonceValue + ":" + qopValue
							+ ":" + MD5A2);
					authBuffer.append("qop=\"" + mm7Config.getDigest() + "\",");
					authBuffer.append("nc=" + ncValue + ",");
					authBuffer.append("cnonce=\"" + cnonceValue + "\",");
					authBuffer.append("response=\"" + responseValue + "\",");
				}
				// 服务器端只有一种qop方式。
				else
				{
					if (qopValue.equals("auth-int"))
					{
						MD5A2 = calcMD5("POST" + ":" + uri + ":"
								+ calcMD5(entityBody.toString()));
					}
					else
						MD5A2 = calcMD5("POST" + ":" + uri);
					ncValue = String.valueOf(pool.getNonceCount());
					cnonceValue = getBASE64(ncValue);
					responseValue = calcMD5(MD5A1 + ":" + nonceValue + ":"
							+ ncValue + ":" + cnonceValue + ":" + qopValue
							+ ":" + MD5A2);
					authBuffer.append("qop=\"" + qopValue + "\",");
					authBuffer.append("nc=" + ncValue + ",");
					authBuffer.append("cnonce=\"" + cnonceValue + "\",");
					authBuffer.append("response=\"" + responseValue + "\",");
				}
			}
			// 服务器端不存在对qop的方式的选择
			else
			{
				MD5A2 = calcMD5("POST" + ":" + uri);
				responseValue = calcMD5(MD5A1 + ":" + nonceValue + ":" + MD5A2);
				authBuffer.append("response=\"" + responseValue + "\",");
			}
		}
		// 去掉最后一个逗号
		int lastcommaindex = authBuffer.lastIndexOf(",");
		authBuffer.delete(lastcommaindex, lastcommaindex + 1);
		authBuffer.append("\r\n");
		return authBuffer.toString();
	}

	public static void main(String[] args) throws Exception
	{
		MM7Config mm7Config = new MM7Config("./config/mm7Config.xml");
		mm7Config.setConnConfigName("./config/ConnConfig.xml");
		MM7Sender mm7Sender = new MM7Sender(mm7Config);
		MM7SubmitReq submit = new MM7SubmitReq();
		submit.setTransactionID("11111111");
		submit.setVASPID("895192");
		submit.addTo("13777802386");
		submit.setVASID("106573061704");
		submit.setServiceCode("1113329901");
		submit.setSubject("测试");
		MMContent content = new MMContent();
		content.setContentType(MMConstants.ContentType.MULTIPART_MIXED);

		MMContent smilSub = MMContent.createFromFile("D:/test/smil.xml");
		smilSub.setContentType(MMConstants.ContentType.SMIL);
		smilSub.setContentID("smil.xml");
		content.addSubContent(smilSub);

		MMContent sub1 = MMContent.createFromFile("D:/test/1.txt");
		// MMContent.createFromStream(in)
		sub1.setContentID("1.txt");
		sub1.setContentType(MMConstants.ContentType.TEXT);
		content.addSubContent(sub1);

		MMContent sub3 = MMContent.createFromFile("D:/test/img_09.jpg");
		sub3.setContentID("img_09.jpg");
		sub3.setContentType(MMConstants.ContentType.JPEG);
		content.addSubContent(sub3);

		MMContent sub2 = MMContent.createFromFile("D:/test/2.txt");
		sub2.setContentID("2.txt");
		sub2.setContentType(MMConstants.ContentType.TEXT);
		content.addSubContent(sub2);

		MMContent sub4 = MMContent.createFromFile("D:/test/img_13.jpg");
		sub4.setContentID("img_13.jpg");
		sub4.setContentType(MMConstants.ContentType.JPEG);
		content.addSubContent(sub4);

		submit.setContent(content);
		// System.out.println(submit);
		MM7RSRes res = mm7Sender.send(submit);
		// System.out.println("#####################");
		// System.out.println(res);
		// System.out.println("res.statuscode=" + res.getStatusCode()
		// / + ";res.statusText=" + res.getStatusText());
	}
}
