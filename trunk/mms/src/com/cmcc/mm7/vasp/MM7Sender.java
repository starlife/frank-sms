/**
 * File Name:MM7Sender.java Company: 中国移动2011
 */
package com.cmcc.mm7.vasp;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cmcc.mm7.vasp.common.ConnectionPool;
import com.cmcc.mm7.vasp.common.ConnectionWrap;
import com.cmcc.mm7.vasp.common.MMConstants;
import com.cmcc.mm7.vasp.common.MMContent;
import com.cmcc.mm7.vasp.http.HttpResponse;
import com.cmcc.mm7.vasp.protocol.DecodeMM7;
import com.cmcc.mm7.vasp.protocol.MM7Helper;
import com.cmcc.mm7.vasp.protocol.message.MM7CancelReq;
import com.cmcc.mm7.vasp.protocol.message.MM7RSErrorRes;
import com.cmcc.mm7.vasp.protocol.message.MM7RSRes;
import com.cmcc.mm7.vasp.protocol.message.MM7ReplaceReq;
import com.cmcc.mm7.vasp.protocol.message.MM7SubmitReq;
import com.cmcc.mm7.vasp.protocol.message.MM7VASPReq;
import com.cmcc.mm7.vasp.protocol.util.LogHelper;
import com.vasp.mm7.conf.MM7Config;

public class MM7Sender extends Thread
{
	private static final Log log = LogFactory.getLog(MM7Sender.class);
	//private MM7Config mm7Config;

	/**
	 * 保存发送提交失败的包，便于重新发送
	 */
	private final LinkedBlockingQueue<MM7VASPReq> buffer = new LinkedBlockingQueue<MM7VASPReq>(
			10000);
	
	private volatile boolean stop = false;
	
	
	private String mmscIP=null;
	private String mmscURL="/";
	private int authmode=0;
	private String username=null;
	private String password=null;
	private String charset=null;
	private boolean keepAlive=false;	
	private int MaxMsgSize=102400;
	
	private ConnectionPool connPool=null;

	/** 构造方法 */
	public MM7Sender(String mmscIP,String mmscURL,int authmode,String username,String password,String charset,boolean keepAlive,int maxMsgSize) throws Exception
	{
		connPool=new ConnectionPool(mmscIP,10000,keepAlive);
		this.mmscIP=mmscIP;
		this.mmscURL=mmscURL;
		this.authmode=authmode;
		this.username=username;
		this.password=password;
		this.charset=charset;
		this.keepAlive=keepAlive;
		//mm7Config = config;
		//ConnectionPool.getInstance().setConfig(mm7Config);
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
				MM7RSRes res = this.send(req);
				log.info("收到回应包 res.statuscode=" + res.getStatusCode()
						+ ";res.statusText=" + res.getStatusText());
				dealRecv(req, res);

			}
			catch (Exception ex)
			{
				log.error(null, ex);
			}
		}

	}

	private void dealRecv(MM7VASPReq req, MM7RSRes res)
	{

		log.debug(LogHelper.logMM7RSRes(res));

		if ((req instanceof MM7SubmitReq))
		{
			dealSubmit((MM7SubmitReq) req, res);
		}
		else if (req instanceof MM7ReplaceReq)
		{
			dealReplace((MM7ReplaceReq) req, res);
		}
		else if (req instanceof MM7CancelReq)
		{
			dealCancel((MM7CancelReq) req, res);
		}
		else
		{
			log.debug("处理未知包" + req + "," + res);
		}

	}

	private MM7RSRes send(MM7VASPReq mm7VASPReq) // 发送消息
	{
		// 判断类型是否正确，判断消息大小是否符合，获取连接是否成功，发送
		MM7RSRes res = null;
		if (!((mm7VASPReq instanceof MM7SubmitReq)
				|| (mm7VASPReq instanceof MM7ReplaceReq) || (mm7VASPReq instanceof MM7CancelReq)))
		{
			res = new MM7RSErrorRes();
			res.setStatusCode(-106);
			res.setStatusText("不接受的发送消息类型！");
			return res;
		}

		log.info(LogHelper.logMM7VASPReq(mm7VASPReq));

		try
		{

			// 设置
			
			// 验证后
			byte[] msgByte = MM7Helper.getMM7Message(mm7VASPReq,mmscIP,mmscURL,
					username, password,keepAlive,
					Charset.forName(charset));

			if (msgByte.length > this.MaxMsgSize)
			{
				res = new MM7RSErrorRes();
				res.setStatusCode(-113);
				res.setStatusText("消息大小超出允许发送的大小！");
				return res;
			}
			res = send(msgByte);

			return res;
		}
		catch (Exception e)
		{
			log.error(null, e);
			MM7RSErrorRes ErrorRes = new MM7RSErrorRes();
			ErrorRes.setStatusCode(-100);
			ErrorRes.setStatusText("系统错误！原因：" + e);
			log.info(LogHelper.logMM7RSRes(res));
			return ErrorRes;
		}
	}

	private boolean isSocketAvail(Socket socket)
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

	private MM7RSRes send(byte[] msgByte)
	{
		// 得到通道，检测当前通道是否可用，如果不可用，那么关闭该通道并标示为删除状态
		MM7RSRes res = null;
		ConnectionWrap conn = connPool.poll();
		try
		{
			if (conn == null)
			{
				res = new MM7RSErrorRes();
				res.setStatusCode(-104);
				res.setStatusText("创建Socket通道失败");
				return res;
			}

			if (!isSocketAvail(conn.getSocket()))
			{
				conn.close();
				res = new MM7RSErrorRes();
				res.setStatusCode(-104);
				res.setStatusText("Socket不可用");
				return res;
			}

			OutputStream sender = conn.getSocket().getOutputStream();
			sender.write(msgByte);
			sender.flush();

			// 接下来是接收回应包
			log.debug("开始接收");
			HttpResponse http = new HttpResponse();
			if (!http.recvData(conn.getSocket().getInputStream()))
			{
				res = new MM7RSErrorRes();
				res.setStatusCode(-102);
				res.setStatusText("接收回应消息失败！");
				return res;

			}
			log.debug("接收完成");
			// 接收完成，送回去
			connPool.offer(conn);

			log.info("收到消息：" + http.toString());

			if (http.getStatusCode() != 200)
			{
				res = new MM7RSErrorRes();
				res.setStatusCode(-108);
				res.setStatusText("回应消息状态为" + http.getReasonPhrase());
				return res;
			}
			if (http.getBody().length == 0)
			{
				res = new MM7RSErrorRes();
				res.setStatusCode(-108);
				res.setStatusText("消息体为空");
				return res;
			}

			res = DecodeMM7.decodeResMessage(http.getBody(), Charset.forName(charset));
			return res;
		}
		catch (IOException ex)
		{
			conn.close();
			log.error(null, ex);
			res = new MM7RSErrorRes();
			res.setStatusCode(-102);
			res.setStatusText("接收失败getException！");
			return res;

		}

	}

	/** 输入string，输出经过MD5编码的String */

	public void myStop()
	{
		this.stop = true;
	}

	public MM7SubmitReq doSubmit()
	{
		return null;
	}

	public void dealSubmit(MM7SubmitReq req, MM7RSRes res)
	{
		log.debug("doCanel" + req + "," + res);
	}

	public void dealReplace(MM7ReplaceReq req, MM7RSRes res)
	{
		log.debug("doCanel" + req + "," + res);
	}

	public void dealCancel(MM7CancelReq req, MM7RSRes res)
	{
		log.debug("doCanel" + req + "," + res);
	}

	public static void main(String[] args) throws Exception
	{
		MM7Config mm7Config = new MM7Config("./config/mm7Config.xml");
		mm7Config.setConnConfigName("./config/ConnConfig.xml");
		String mmscIP=mm7Config.getMMSCIP();
		String mmscURL=mm7Config.getMMSCURL();
		int authmode=mm7Config.getAuthenticationMode();
		String username=mm7Config.getUserName();
		String password=mm7Config.getPassword();
		String charset=mm7Config.getCharSet();
		boolean keepAlive=false;
		int maxMsgSize=mm7Config.getMaxMsgSize();
		MM7Sender mm7Sender = new MM7Sender(mmscIP,mmscURL,authmode,username,password,charset,keepAlive,maxMsgSize);
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
		log.debug("开始发送");
		MM7RSRes res = mm7Sender.send(submit);
		log.debug("发送结束");
		System.out.println("#####################");
		// System.out.println(res);
		System.out.println("res.statuscode=" + res.getStatusCode()
				+ ";res.statusText=" + res.getStatusText());
	}
}
