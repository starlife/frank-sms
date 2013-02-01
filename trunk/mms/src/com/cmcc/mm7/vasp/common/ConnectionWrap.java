package com.cmcc.mm7.vasp.common;

import java.net.Socket;

public class ConnectionWrap
{

	private Socket socket = null;

	private long activeTime = System.currentTimeMillis();

	private boolean del = false;// 标记该连接等待被删除

	public ConnectionWrap(Socket s, long time)
	{
		this.socket = s;
		this.activeTime = time;
	}

	public ConnectionWrap(Socket s, long time, boolean isdel)
	{
		this.socket = s;
		this.activeTime = time;
		this.del = isdel;
	}

	public boolean isDel()
	{
		return del;
	}

	public void setDel(boolean del)
	{
		this.del = del;
	}

	public Socket getSocket()
	{
		return socket;
	}

	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("SocketAddress=" + this.getSocket()
				+ "\r\n");
		sb.append("上次激活时间到现在=" + (System.currentTimeMillis() - activeTime)
				+ "\r\n");
		sb.append("IsDel=" + this.isDel() + "\r\n");
		return sb.toString();
	}

	public long getActiveTime()
	{
		return activeTime;
	}

	public void setActiveTime(long activeTime)
	{
		this.activeTime = activeTime;
	}

	public void setSocket(Socket socket)
	{
		this.socket = socket;
	}

}
