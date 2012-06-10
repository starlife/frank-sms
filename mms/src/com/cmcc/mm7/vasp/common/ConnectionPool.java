/**
 * File Name:ConnectionPool.java Company: 中国移动集团公司 Date : 2004-1-9
 */

package com.cmcc.mm7.vasp.common;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ConnectionPool
{
	private static final Log log = LogFactory.getLog(ConnectionPool.class);

	private String ip = null;
	private int port = 80;
	private int timeout = 10000;

	private final List<ConnectionWrap> pool = new ArrayList<ConnectionWrap>();
	private volatile int offset = 0;
	// private Socket socket = null;

	// private long activeTime = 0;

	private static ConnectionPool instance = null;

	private ConnectionPool(String mmscIp, int timeout, int poolSize)
	{
		parse(mmscIp);
		this.timeout = timeout;
		for (int i = 0; i < poolSize; i++)
		{
			pool.add(new ConnectionWrap());
		}
	}

	public static ConnectionPool getConnPool()
	{
		return instance;
	}

	public static void init(String mmscIp, int timeout, int poolSize)
	{
		instance = new ConnectionPool(mmscIp, timeout, poolSize);
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

	private Socket createSocket()
	{
		Socket socket = null;
		try
		{
			socket = new Socket(ip, port);
			socket.setSoTimeout(timeout);
			log.debug("建立新socket连接成功" + socket.getLocalSocketAddress()
					+ socket.getRemoteSocketAddress());

			return socket;
		}
		catch (Exception e)
		{
			socket = null;
			log.error("创建socket连接失败", e);
			return null;
		}

	}

	public synchronized Socket getConn()
	{
		// 从队列中找，如果队列不为空，那么
		ConnectionWrap conn = null;
		synchronized (pool)
		{
			if (pool.size() > 0)
			{
				conn = pool.get(getOffset());
			}
		}
		if (conn == null)
		{
			conn = new ConnectionWrap();
		}
		Socket socket = conn.getSocket();
		// 如果当前socket无效，那么重新创建socket
		if (!isSocketAvail(socket))
		{
			socket = createSocket();
			conn.setSocket(socket);
		}
		// 如果当前socket的上次使用时间到现在已经超过timeout，那么该socket无效，需要重新创建socket
		long between = System.currentTimeMillis() - conn.getActiveTime();
		if (between > timeout)
		{
			log.debug("当前Socket上次使用时间到现在已经超过" + between + "ms,需要重新建立连接");
			socket = createSocket();
			conn.setSocket(socket);
		}

		// 每一次使用之前重新赋值activeTime
		if (socket != null)
		{
			conn.setActiveTime(System.currentTimeMillis());
		}
		return socket;
	}

	private int getOffset()
	{
		offset++;
		offset = offset % pool.size();
		return offset;

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
