/**
 * File Name:MM7Sender.java Company: �й��ƶ�2011
 */
package com.cmcc.mm7.vasp.service;

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
import com.cmcc.mm7.vasp.conf.MM7Config;
import com.cmcc.mm7.vasp.http.HttpResponse;
import com.cmcc.mm7.vasp.message.MM7CancelReq;
import com.cmcc.mm7.vasp.message.MM7RSErrorRes;
import com.cmcc.mm7.vasp.message.MM7RSRes;
import com.cmcc.mm7.vasp.message.MM7ReplaceReq;
import com.cmcc.mm7.vasp.message.MM7SubmitReq;
import com.cmcc.mm7.vasp.message.MM7VASPReq;
import com.cmcc.mm7.vasp.protocol.DecodeMM7;
import com.cmcc.mm7.vasp.protocol.MM7Helper;
import com.cmcc.mm7.vasp.util.LogHelper;

public class MM7Sender extends Thread
{
	private static final Log log = LogFactory.getLog(MM7Sender.class);
	private MM7Config mm7Config;

	/**
	 * ���淢���ύʧ�ܵİ����������·���
	 */
	private final LinkedBlockingQueue<MM7VASPReq> buffer = new LinkedBlockingQueue<MM7VASPReq>(
			10000);

	private volatile boolean stop = false;

	/** ���췽�� */
	public MM7Sender(MM7Config config) throws Exception
	{

		mm7Config = config;
		ConnectionPool.getInstance().setConfig(mm7Config);
	}

	@Override
	public void run()
	{
		log.info("�������������̡߳�����");
		while (!stop)
		{
			try
			{
				// ȡ���Ų�����
				// RateControl.controlRate();
				// ȡ������
				MM7VASPReq req = this.doSubmit();
				if (req == null)
				{
					req = buffer.poll();
				}

				if (req == null)
				{
					continue;
				}

				// ����
				MM7RSRes res = this.send(req);
				log.info("�յ���Ӧ�� res.statuscode=" + res.getStatusCode()
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
			log.debug("����δ֪��" + req + "," + res);
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

		log.info(LogHelper.logMM7VASPReq(mm7VASPReq));

		try
		{

			// ����
			String host = mm7Config.getMMSCIP();
			String url = mm7Config.getMMSCURL();

			// ��֤��
			byte[] msgByte = MM7Helper.getMM7Message(mm7VASPReq, host, url,
					mm7Config.getUserName(), mm7Config.getPassword(),false,
					Charset.forName(mm7Config.getCharSet()));

			if (msgByte.length > mm7Config.getMaxMsgSize())
			{
				res = new MM7RSErrorRes();
				res.setStatusCode(-113);
				res.setStatusText("��Ϣ��С���������͵Ĵ�С��");
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
			ErrorRes.setStatusText("ϵͳ����ԭ��" + e);
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
		// �õ�ͨ������⵱ǰͨ���Ƿ���ã���������ã���ô�رո�ͨ������ʾΪɾ��״̬
		MM7RSRes res = null;
		ConnectionWrap conn = ConnectionPool.getInstance().poll();
		try
		{
			if (conn == null)
			{
				res = new MM7RSErrorRes();
				res.setStatusCode(-104);
				res.setStatusText("����Socketͨ��ʧ��");
				return res;
			}

			if (!isSocketAvail(conn.getSocket()))
			{
				conn.close();
				res = new MM7RSErrorRes();
				res.setStatusCode(-104);
				res.setStatusText("Socket������");
				return res;
			}

			OutputStream sender = conn.getSocket().getOutputStream();
			sender.write(msgByte);
			sender.flush();

			// �������ǽ��ջ�Ӧ��
			log.debug("��ʼ����");
			HttpResponse http = new HttpResponse();
			if (!http.recvData(conn.getSocket().getInputStream()))
			{
				res = new MM7RSErrorRes();
				res.setStatusCode(-102);
				res.setStatusText("���ջ�Ӧ��Ϣʧ�ܣ�");
				return res;

			}
			log.debug("�������");
			// ������ɣ��ͻ�ȥ
			ConnectionPool.getInstance().offer(conn);

			log.info("�յ���Ϣ��" + http.toString());

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

			res = DecodeMM7.decodeResMessage(http.getBody(), Charset.forName(this.mm7Config
					.getCharSet()));
			return res;
		}
		catch (IOException ex)
		{
			conn.close();
			log.error(null, ex);
			res = new MM7RSErrorRes();
			res.setStatusCode(-102);
			res.setStatusText("����ʧ��getException��");
			return res;

		}

	}

	/** ����string���������MD5�����String */

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
		MM7Sender mm7Sender = new MM7Sender(mm7Config);
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
