package com.cmcc.mm7.vasp.common;


import java.net.Socket;

public class ConnectionWrap
{
	
	private Socket socket = null;

	private long activeTime = System.currentTimeMillis();

	

	public Socket getSocket()
	{
		return socket;
	}

	

	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("SocketAddress=" + this.getSocket().getRemoteSocketAddress()
				+ "\r\n");
		sb.append("�ϴμ���ʱ�䵽����=" + (System.currentTimeMillis() - activeTime)
				+ "\r\n");
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
