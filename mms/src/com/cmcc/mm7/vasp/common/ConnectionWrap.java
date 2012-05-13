/**
 * File Name:MM7Config.java Company: 中国移动集团公司 Date : 2004-1-3
 */

package com.cmcc.mm7.vasp.common;

import java.net.Socket;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ConnectionWrap
{
	private static final Log log = LogFactory.getLog(ConnectionWrap.class);
	private String mmscIP = null;
	private Socket socket = null;
	private boolean free = false; // 标志是否空闲
	public long activeTime = System.currentTimeMillis();
	// private boolean AuthFlag; // 标志是否经过了摘要鉴权

	/**
	 * 表示链路是否有效（false表示该链路将会被删除）
	 */
	private boolean del = false;

	public ConnectionWrap(String mmscIP)
	{
		this.mmscIP = mmscIP;

	}

	public boolean buildLink()
	{
		try
		{
			int index = mmscIP.indexOf(":");
			String ip;
			int port;
			if (index == -1)
			{
				ip = mmscIP;
				port = 80;
			}
			else
			{
				ip = mmscIP.substring(0, index);
				port = Integer.parseInt(mmscIP.substring(index + 1));
			}
			socket = new Socket(ip, port);
			socket.setKeepAlive(true);

			free = true;// 初始设置链路为可用
			activeTime = 0;
			return true;
		}
		catch (Exception e)
		{
			log.error(null, e);
			return false;
		}

	}

	public void close()
	{
		this.setDel(true);
		try
		{
			if (socket == null)
			{
				return;
			}
			socket.getInputStream().close();
			socket.getOutputStream().close();
			socket.close();

		}
		catch (Exception ex)
		{
			log.error(null, ex);
		}

	}

	public Socket getSocket()
	{
		return socket;
	}

	public boolean getFree()
	{
		return free;
	}

	public void setFree(boolean bfree)
	{
		free = bfree;
	}

	/*
	 * public void setAuthFlag(boolean authflag) { AuthFlag = authflag; } public
	 * boolean getAuthFlag() { return AuthFlag; }
	 */

	public boolean isDel()
	{
		return del;
	}

	public void setDel(boolean del)
	{
		this.del = del;
	}
}
