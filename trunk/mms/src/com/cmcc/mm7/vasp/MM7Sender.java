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

public class MM7Sender extends Thread implements MM7AbstractSender
{
	private static final Log log = LogFactory.getLog(MM7Sender.class);

	private static final Log lose = LogFactory.getLog("lose");// 记录日志

	/**
	 * 保存发送提交失败的包，便于重新发送
	 */
	private final LinkedBlockingQueue<MM7VASPReq> buffer = new LinkedBlockingQueue<MM7VASPReq>(
			10000);

	private volatile boolean stop = false;

	private String mmscIP = null;
	private String mmscURL = "/";
	private int authmode = 0;
	private String username = null;
	private String password = null;
	private Charset charset = Charset.defaultCharset();
	private boolean keepAlive = false;
	private int MaxMsgSize = 102400;
	private int retryCount = 3;

	//private ConnectionPool conn = null;

	/** 构造方法 */
	public MM7Sender(String mmscIP, String mmscURL, int authmode,
			String username, String password, String charset, int maxMsgSize,
			int reSendCount, boolean keepAlive, int timeout,int poolSize)
	{
		super("MM7Sender");
		ConnectionPool conn=ConnectionPool.getConnPool();
		if(conn==null)
		{
			ConnectionPool.init(mmscIP, timeout, poolSize);
		}
		this.mmscIP = mmscIP;
		this.mmscURL = mmscURL;
		this.authmode = authmode;
		this.username = username;
		this.password = password;
		this.charset = Charset.forName(charset);
		this.MaxMsgSize = maxMsgSize;
		this.retryCount = reSendCount;
		this.keepAlive = keepAlive;

	}

	@Override
	public void run()
	{
		log.info("启动发送线程...");
		while (!stop)
		{
			try
			{
				// 取彩信并发送
				// RateControl.controlRate();
				// 取包发送
				MM7VASPReq req = this.submit();
				if (req == null)
				{
					req = buffer.poll();
				}

				if (req == null)
				{
					continue;
				}

				MM7RSRes res = this.send(req);

				log.info("收到回应包 " + LogHelper.logMM7RSRes(res));

				dealRecv(req, res);

			}
			catch (Exception ex)
			{
				log.error(null, ex);
			}
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

		try
		{

			// 设置
			if (mm7VASPReq.getBytes() == null)
			{

				// 得到消息的byte
				log.debug("getMM7Message之前");
				byte[] msgByte = MM7Helper.getMM7Message(mm7VASPReq, mmscIP,
						mmscURL, authmode, username, password, keepAlive,
						charset);

				if (msgByte.length > this.MaxMsgSize)
				{
					res = new MM7RSErrorRes();
					res.setStatusCode(-113);
					res.setStatusText("消息大小超出允许发送的大小！");
					return res;
				}
				mm7VASPReq.setBytes(msgByte);
			}
			log.info("准备发送包 " + LogHelper.logMM7VASPReq(mm7VASPReq));
			mm7VASPReq.addTimes();
			res = send(mm7VASPReq.getBytes());

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

	private MM7RSRes send(byte[] msgByte)
	{
		// 得到通道，检测当前通道是否可用，如果不可用，那么关闭该通道并标示为删除状态
		//log.debug(new String(msgByte));
		MM7RSRes res = null;
		Socket socket = ConnectionPool.getConnPool().getConn();
		if (socket == null)
		{
			res = new MM7RSErrorRes();
			res.setStatusCode(-104);
			res.setStatusText("创建Socket通道失败");
			return res;
		}

		try
		{
			log.debug("开始发送包...");
			OutputStream sender = socket.getOutputStream();
			sender.write(msgByte);
			sender.flush();

			// 接下来是接收回应包
			log.debug("开始接收(SubmitRsp)");
			HttpResponse http = new HttpResponse();
			if (!http.recvData(socket.getInputStream()))
			{
				res = new MM7RSErrorRes();
				res.setStatusCode(-102);
				res.setStatusText("接收回应消息失败！");
				return res;

			}
			log.debug("接收完成(SubmitRsp)");
			// 接收完成，送回去

			log.debug("MM7Sender收到消息：" + http.toString());

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
			if (!keepAlive)
			{			
				try
				{
					
					log.debug("当前配置短连接，发送结束，尝试关闭socket");
					socket.close();
				}
				catch (IOException e)
				{
					// TODO Auto-generated catch block
					log.error(null, e);
				}				
				
			}
			res = DecodeMM7.decodeResMessage(http.getBody(), charset);
			return res;
		}
		catch (IOException ex)
		{
			try
			{
				log.info("IOException 尝试关闭socket");
				socket.close();
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				log.error(null, e);
			}
			log.error(null, ex);
			res = new MM7RSErrorRes();
			res.setStatusCode(-102);
			res.setStatusText("接收失败getException！");
			return res;

		}

	}

	private void dealRecv(MM7VASPReq req, MM7RSRes res)
	{
		if (res instanceof MM7RSErrorRes)
		{
			// 如果发送失败，那么加入到发送队列重新发送
			if (req.getTimes() < retryCount)
			{
				req.addTimes();
				buffer.offer(req);
			}
			else
			{
				lose.info("包达到最大发送次数（" + retryCount + "次）,丢弃" + req);
			}

		}
		if ((req instanceof MM7SubmitReq))
		{
			doSubmit((MM7SubmitReq) req, res);
		}
		else if (req instanceof MM7ReplaceReq)
		{
			doReplace((MM7ReplaceReq) req, res);
		}
		else if (req instanceof MM7CancelReq)
		{
			doCancel((MM7CancelReq) req, res);
		}
		else
		{
			log.error("处理未知提交包" + req + "," + res);
		}

	}

	/** 输入string，输出经过MD5编码的String */

	public void myStop()
	{
		this.stop = true;
	}

	public MM7SubmitReq submit()
	{
		return null;
	}

	public void doSubmit(MM7SubmitReq req, MM7RSRes res)
	{
		log.debug("dealSubmit" + req + "," + res);
	}

	public void doReplace(MM7ReplaceReq req, MM7RSRes res)
	{
		log.debug("dealReplace" + req + "," + res);
	}

	public void doCancel(MM7CancelReq req, MM7RSRes res)
	{
		log.debug("dealCancel" + req + "," + res);
	}

	public static void main(String[] args) throws Exception
	{
		String mmscIP = "211.140.27.30:5700";
		String mmscURL = "/";
		int authmode = 1;
		String username = "C61704";
		String password = "sdop77";
		String charset = "GB2312";
		boolean keepAlive = true;
		int timeout = 900000;
		int maxMsgSize = 102400;
		int reSendCount = 3;
		MM7Sender mm7Sender = new MM7Sender(mmscIP, mmscURL, authmode,
				username, password, charset, maxMsgSize, reSendCount,
				keepAlive, timeout,2);
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
