/**
 * File Name:ConnectionPool.java Company: �й��ƶ����Ź�˾ Date : 2004-1-9
 */

package com.cmcc.mm7.vasp.common;

import java.net.Socket;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Connection
{
	private static final Log log = LogFactory.getLog(Connection.class);

	private String ip = null;
	private int port = 80;
	private int timeout = 10000;

	private Socket socket = null;

	private long activeTime = 0;

	public Connection(String mmscIp, int timeout)
	{
		parse(mmscIp);
		this.timeout = timeout;
	}

	private void parse(String mmscIp)
	{

		int index = mmscIp.indexOf(":");
		if (index == -1)
		{
			ip = mmscIp;
			port = 80;
		}
		else
		{
			ip = mmscIp.substring(0, index);
			try
			{
				port = Integer.parseInt(mmscIp.substring(index + 1));
			}
			catch (Exception ex)
			{
				log.error(null, ex);
			}
		}

	}

	private boolean buildLink()
	{
		try
		{
			socket = new Socket(ip, port);
			this.activeTime = System.currentTimeMillis();
			socket.setSoTimeout(timeout);
			log.debug("������socket���ӳɹ�" + socket.getLocalSocketAddress()
					+ socket.getRemoteSocketAddress());

			return true;
		}
		catch (Exception e)
		{
			socket = null;
			log.error("����socket����ʧ��", e);
			return false;
		}

	}

	public synchronized Socket getConn()
	{
		// �����ǰsocket��Ч����ô���´���socket
		if (!isSocketAvail(socket))
		{
			buildLink();
		}
		// �����ǰsocket���ϴ�ʹ��ʱ�䵽�����Ѿ�����timeout����ô��socket��Ч����Ҫ���´���socket
		long between = System.currentTimeMillis() - activeTime;
		if (between > timeout)
		{
			log.debug("��ǰSocket�ϴ�ʹ��ʱ�䵽�����Ѿ�����" + between + "ms,��Ҫ���½�������");
			buildLink();
		}

		// ÿһ��ʹ��֮ǰ���¸�ֵactiveTime
		if (socket != null)
		{
			this.activeTime = System.currentTimeMillis();
		}
		return socket;
	}

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

}
