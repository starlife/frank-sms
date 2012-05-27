/**
 * File Name:ConnectionPool.java Company: 中国移动集团公司 Date : 2004-1-9
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
			log.debug("建立新socket连接成功" + socket.getLocalSocketAddress()
					+ socket.getRemoteSocketAddress());

			return true;
		}
		catch (Exception e)
		{
			socket = null;
			log.error("创建socket连接失败", e);
			return false;
		}

	}

	public synchronized Socket getConn()
	{
		// 如果当前socket无效，那么重新创建socket
		if (!isSocketAvail(socket))
		{
			buildLink();
		}
		// 如果当前socket的上次使用时间到现在已经超过timeout，那么该socket无效，需要重新创建socket
		long between = System.currentTimeMillis() - activeTime;
		if (between > timeout)
		{
			log.debug("当前Socket上次使用时间到现在已经超过" + between + "ms,需要重新建立连接");
			buildLink();
		}

		// 每一次使用之前重新赋值activeTime
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
