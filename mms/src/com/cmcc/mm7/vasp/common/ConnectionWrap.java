/**
 * File Name:MM7Config.java Company: 中国移动集团公司 Date : 2004-1-3
 */

package com.cmcc.mm7.vasp.common;

import java.net.InetSocketAddress;
import java.net.Socket;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ConnectionWrap
{
	private static final Log log = LogFactory.getLog(ConnectionWrap.class);
	private String mmscIP = null;
	private Socket socket = null;
	private int timeout = 10000;// 超时时间
	public long activeTime = System.currentTimeMillis();
	// private boolean AuthFlag; // 标志是否经过了摘要鉴权

	private boolean free = false; // 标志是否空闲
	/**
	 * 表示链路是否有效（false表示该链路将会被删除）
	 */
	private boolean del = false;

	public ConnectionWrap(String mmscIP, int timeout)
	{
		this.mmscIP = mmscIP;
		this.timeout = timeout;

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
			socket = new Socket();
			// socket.setKeepAlive(true);
			socket.setSoTimeout(timeout);
			socket.connect(new InetSocketAddress(ip, port), timeout);
			log.debug("建立新socket连接成功" + socket.getLocalSocketAddress()
					+ socket.getRemoteSocketAddress());

			free = true;// 初始设置链路为可用
			activeTime = System.currentTimeMillis();
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
		this.del=true;

		if (socket == null)
		{
			return;
		}
		if (!socket.isClosed())
		{
			try
			{
				socket.close();
			}
			catch (Exception ex)
			{
				log.error(null, ex);
			}
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

	
	public String toString()
	{
		StringBuffer sb=new StringBuffer();
		sb.append("SocketAddress="+this.getSocket().getRemoteSocketAddress()+"\r\n");
		sb.append("SoTimeout="+timeout+"\r\n");
		sb.append("IsDel="+this.isDel()+"\r\n");
		sb.append("Free:"+this.getFree()+"\r\n");
		return sb.toString();
	}

	public static void main(String[] args)
	{
		ConnectionWrap conn = new ConnectionWrap("211.140.27.30:5700", 1000);
		if (conn.buildLink())
		{
			while (true)
			{
				try
				{
					Thread.sleep(10);
				}
				catch (InterruptedException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		else
		{
			log.info("false");
		}
	}
}
