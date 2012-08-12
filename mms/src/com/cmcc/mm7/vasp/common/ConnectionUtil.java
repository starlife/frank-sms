package com.cmcc.mm7.vasp.common;

import java.io.IOException;
import java.net.Socket;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ConnectionUtil
{
	private static final Log log = LogFactory
	.getLog(ConnectionUtil.class);
	
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
	
	public static void closeSocket(Socket socket)
	{
		if (socket != null)
		{
			try
			{
				socket.close();
				socket=null;
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				log.error(null, e);
			}
		}
	}
	
	
}
