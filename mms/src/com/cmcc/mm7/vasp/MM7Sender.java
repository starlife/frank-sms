/**
 * File Name:MM7Sender.java Company: �й��ƶ�2011
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

	private static final Log lose = LogFactory.getLog("lose");// ��¼��־

	/**
	 * ���淢���ύʧ�ܵİ����������·���
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

	/** ���췽�� */
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
		log.info("���������߳�...");
		while (!stop)
		{
			try
			{
				// ȡ���Ų�����
				// RateControl.controlRate();
				// ȡ������
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

				log.info("�յ���Ӧ�� " + LogHelper.logMM7RSRes(res));

				dealRecv(req, res);

			}
			catch (Exception ex)
			{
				log.error(null, ex);
			}
		}

	}

	private MM7RSRes send(MM7VASPReq mm7VASPReq) // ������Ϣ
	{
		// �ж������Ƿ���ȷ���ж���Ϣ��С�Ƿ���ϣ���ȡ�����Ƿ�ɹ�������
		MM7RSRes res = null;
		if (!((mm7VASPReq instanceof MM7SubmitReq)
				|| (mm7VASPReq instanceof MM7ReplaceReq) || (mm7VASPReq instanceof MM7CancelReq)))
		{
			res = new MM7RSErrorRes();
			res.setStatusCode(-106);
			res.setStatusText("�����ܵķ�����Ϣ���ͣ�");
			return res;
		}

		try
		{

			// ����
			if (mm7VASPReq.getBytes() == null)
			{

				// �õ���Ϣ��byte
				log.debug("getMM7Message֮ǰ");
				byte[] msgByte = MM7Helper.getMM7Message(mm7VASPReq, mmscIP,
						mmscURL, authmode, username, password, keepAlive,
						charset);

				if (msgByte.length > this.MaxMsgSize)
				{
					res = new MM7RSErrorRes();
					res.setStatusCode(-113);
					res.setStatusText("��Ϣ��С���������͵Ĵ�С��");
					return res;
				}
				mm7VASPReq.setBytes(msgByte);
			}
			log.info("׼�����Ͱ� " + LogHelper.logMM7VASPReq(mm7VASPReq));
			mm7VASPReq.addTimes();
			res = send(mm7VASPReq.getBytes());

			return res;
		}
		catch (Exception e)
		{
			log.error(null, e);
			MM7RSErrorRes ErrorRes = new MM7RSErrorRes();
			ErrorRes.setStatusCode(-100);
			ErrorRes.setStatusText("ϵͳ����ԭ��" + e);
			return ErrorRes;
		}
	}

	private MM7RSRes send(byte[] msgByte)
	{
		// �õ�ͨ������⵱ǰͨ���Ƿ���ã���������ã���ô�رո�ͨ������ʾΪɾ��״̬
		//log.debug(new String(msgByte));
		MM7RSRes res = null;
		Socket socket = ConnectionPool.getConnPool().getConn();
		if (socket == null)
		{
			res = new MM7RSErrorRes();
			res.setStatusCode(-104);
			res.setStatusText("����Socketͨ��ʧ��");
			return res;
		}

		try
		{
			log.debug("��ʼ���Ͱ�...");
			OutputStream sender = socket.getOutputStream();
			sender.write(msgByte);
			sender.flush();

			// �������ǽ��ջ�Ӧ��
			log.debug("��ʼ����(SubmitRsp)");
			HttpResponse http = new HttpResponse();
			if (!http.recvData(socket.getInputStream()))
			{
				res = new MM7RSErrorRes();
				res.setStatusCode(-102);
				res.setStatusText("���ջ�Ӧ��Ϣʧ�ܣ�");
				return res;

			}
			log.debug("�������(SubmitRsp)");
			// ������ɣ��ͻ�ȥ

			log.debug("MM7Sender�յ���Ϣ��" + http.toString());

			if (http.getStatusCode() != 200)
			{
				res = new MM7RSErrorRes();
				res.setStatusCode(-108);
				res.setStatusText("��Ӧ��Ϣ״̬Ϊ" + http.getReasonPhrase());
				return res;
			}
			if (http.getBody().length == 0)
			{
				res = new MM7RSErrorRes();
				res.setStatusCode(-108);
				res.setStatusText("��Ϣ��Ϊ��");
				return res;
			}
			if (!keepAlive)
			{			
				try
				{
					
					log.debug("��ǰ���ö����ӣ����ͽ��������Թر�socket");
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
				log.info("IOException ���Թر�socket");
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
			res.setStatusText("����ʧ��getException��");
			return res;

		}

	}

	private void dealRecv(MM7VASPReq req, MM7RSRes res)
	{
		if (res instanceof MM7RSErrorRes)
		{
			// �������ʧ�ܣ���ô���뵽���Ͷ������·���
			if (req.getTimes() < retryCount)
			{
				req.addTimes();
				buffer.offer(req);
			}
			else
			{
				lose.info("���ﵽ����ʹ�����" + retryCount + "�Σ�,����" + req);
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
			log.error("����δ֪�ύ��" + req + "," + res);
		}

	}

	/** ����string���������MD5�����String */

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
		submit.setSubject("����");
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
		log.debug("��ʼ����");
		MM7RSRes res = mm7Sender.send(submit);
		log.debug("���ͽ���");
		System.out.println("#####################");
		// System.out.println(res);
		System.out.println("res.statuscode=" + res.getStatusCode()
				+ ";res.statusText=" + res.getStatusText());
	}
}
