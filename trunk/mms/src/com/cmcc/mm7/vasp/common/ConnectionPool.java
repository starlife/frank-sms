/**
 * File Name:ConnectionPool.java Company: �й��ƶ����Ź�˾ Date : 2004-1-9
 */

package com.cmcc.mm7.vasp.common;

import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ConnectionPool
{
	private static final Log log = LogFactory.getLog(ConnectionPool.class);
	
	private static final Log connLog = LogFactory.getLog("connErr");
	
	private String ip = null;
	private int port = 80;
	private int timeout = 10000;
	private int maxSize=1;

	private static final LinkedBlockingQueue<Socket> que = new LinkedBlockingQueue<Socket>();// ���Ӷ���
	
	private static final Map<Socket,Long> usedMap = new HashMap<Socket,Long>();//������ʹ����socket�Լ����һ��ʹ��ʱ��
	
	

	private static ConnectionPool instance = null;

	private ConnectionPool(String mmscIp, int timeout, int poolSize)
	{
		parse(mmscIp);
		this.timeout = timeout;
		this.maxSize=poolSize;
		/*for (int i = 0; i < maxSize; i++)
		{
			que.offer(new ConnectionWrap());
		}*/
	}

	public static ConnectionPool getPool()
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
			log.debug("������socket���ӳɹ�" + socket.getLocalSocketAddress()
					+ socket.getRemoteSocketAddress());

			return socket;
		}
		catch (Exception e)
		{
			socket = null;
			log.error("����socket����ʧ��", e);
			return null;
		}

	}

	public synchronized Socket getSocket()
	{
		synchronized (que)
		{
			Socket socket = que.poll();
			while(true)
			{	
				if(socket==null)
				{
					if(usedMap.size()<this.maxSize)
					{
						socket = createSocket();
						if(socket!=null)
						{
							//����ʹ�ö���
							usedMap.put(socket,System.currentTimeMillis());
						}
					}
					return socket;
				}
				//socket��Ϊ�գ���������֤socket�Ƿ����	
				if (!ConnectionUtil.isSocketAvail(socket))
				{
					socket=null;
				}else
				{
					//��֤ʱ���Ƿ����
					Long lastActive=usedMap.get(socket);
					if(lastActive!=null)
					{
						long between=(System.currentTimeMillis()-lastActive);
						if(between>timeout)
						{
							ConnectionUtil.closeSocket(socket);
							log.debug("��ǰSocket�ϴ�ʹ��ʱ�䵽�����Ѿ�����" + between + "ms,��Ҫ���½�������");
						}
					}
				}
				
				if(socket!=null)
				{
					//����ʹ�ö���
					usedMap.put(socket,System.currentTimeMillis());
					return socket;
				}				
				socket = que.poll();
			}
		}
	}
	
	/**
	 * ����socket����
	 * @param socket
	 */
	public void freeSocket(Socket socket)
	{
		synchronized (que)
		{
			if(usedMap.size()>100)
			{
				log.error("usedMap��Ҫ���");
				connLog.info(usedMap);
				usedMap.clear();
				
			}
			//������Ӳ����ã�������ۼ���������ӿ��ã���ô���¼��������ʹ��
			if (!ConnectionUtil.isSocketAvail(socket))
			{
				usedMap.remove(socket);
			}else
			{
				if(usedMap.get(socket)!=null)
				{
					que.offer(socket);
				}
			}
		}
	}

	
	
	
	

}
