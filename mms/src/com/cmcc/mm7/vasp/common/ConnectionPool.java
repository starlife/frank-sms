/**
 * File Name:ConnectionPool.java Company: �й��ƶ����Ź�˾ Date : 2004-1-9
 */

package com.cmcc.mm7.vasp.common;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cmcc.mm7.vasp.protocol.message.MM7VASPReq;

public class ConnectionPool
{
	private static final Log log = LogFactory.getLog(ConnectionPool.class);
	// public static List ClientList;
	//private static final LinkedBlockingQueue<Socket> que = new LinkedBlockingQueue<Socket>(5);
	// private static boolean isCreate;
	// public HashMap hashmap;
	// private long time;
	// private int IPCount;
	// private MM7Config Mm7Config;
	// private String NonceCount;
	//private int ServerMaxSize;
	//private String KeepAlive;
	// private long KeepAliveTimeout;
	// private int minCount;// ��С������
	//private int maxCount;// ���������
	// private int step;// ���ӵ�������
	
	private boolean keepAlive=false;
	private String mmscIp = null;
	private int timeout = 10000;
	
	private Socket socket=null;

	//private static final ConnectionPool m_instance = new ConnectionPool();

	public ConnectionPool(String mmscIp,boolean keepAlive,int timeout)
	{
		this.mmscIp=mmscIp;
		this.keepAlive=keepAlive;
		this.timeout=timeout;
	}

	
	
	
	// �������ļ��л��һЩ������Ϣ
	/*private void init(MM7Config config)
	{
		// hashmap.clear();
		MM7ConfigManager confManager = new MM7ConfigManager();
		this.mmscIp =  config.getMMSCIP();
		this.timeout = config.getTimeOut();
		String name = config.getConnConfigName();
		if (!name.equals(""))
		{
			confManager.load(name);
			// �����ӿ���
			String keepAlive = (String) confManager.map.get("KeepAlive");
			if (keepAlive != null)
			{
				this.setKeepAlive(keepAlive);
			}
			// ��������������
			String serverMaxSize = (String) confManager.map
					.get("ServerMaxKeepAlive");
			if (serverMaxSize != null)
			{
				this.setServerMaxSize(Integer.parseInt(serverMaxSize));
			}
			
			String MaxKeepAliveRequests = (String) confManager.map
					.get("MaxKeepAliveRequests");
			if (MaxKeepAliveRequests != null)
			{
				this.maxCount = Integer.parseInt(MaxKeepAliveRequests);
			}
			

		}

	}*/

	/*public void offer(Socket conn)
	{
		// ����ǳ����ӣ���ô��������Ϊ���ã�����Ƕ����ӣ���ô�ر�
		if (keepAlive)
		{
			que.offer(conn);
		}
		else
		{
			try
			{
				conn.close();
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}*/

	// ��ÿ��е�����
	/*public synchronized Socket poll()
	{
		if (this.mmscIp == null)
		{
			return null;
		}

		ConnectionWrap conn = null;
		// ����ǳ����ӣ���ô�Ӷ������ҿ��õ�����
		if (keepAlive)
		{
			// Ѱ�ҿ��е�����
			synchronized (que)
			{
				Iterator<ConnectionWrap> it = que.iterator();
				while (it.hasNext())
				{
					conn = it.next();
					if (conn == null)
					{
						continue;
					}
					if (!conn.isDel() || conn.getSocket().isClosed())
					{
						// ɾ��ʧЧ������
						log.debug("ɾ��ʧ�����ӣ�" + conn);
						it.remove();
						continue;
					}

					if (!conn.getFree())
					{
						continue;
					}
					conn.setFree(false);
					// conn.setConnectIndex(i);
					conn.activeTime = System.currentTimeMillis();
					return conn;
				}
				
				
					// �ж�Ŀǰ�������Ӽ���Ҫ�½��������Ƿ񳬹����������

					conn = new ConnectionWrap(mmscIp, timeout);
					if (conn.buildLink())
					{
						que.add(conn);
						return conn;
					}

				
			}

		}
		// ��һ��������
		else
		{

			conn = new ConnectionWrap(mmscIp, timeout);
			if (conn.buildLink())
			{
				return conn;
			}

		}
		return null;

	}*/
	private boolean buildLink()
	{
		try
		{
			int index = this.mmscIp.indexOf(":");
			String ip;
			int port;
			if (index == -1)
			{
				ip = mmscIp;
				port = 80;
			}
			else
			{
				ip = mmscIp.substring(0, index);
				port = Integer.parseInt(mmscIp.substring(index + 1));
			}
			socket = new Socket(ip,port);
			socket.setSoTimeout(timeout);
			log.debug("������socket���ӳɹ�" + socket.getLocalSocketAddress()
					+ socket.getRemoteSocketAddress());

			//free = true;// ��ʼ������·Ϊ����
			//activeTime = System.currentTimeMillis();
			return true;
		}
		catch (Exception e)
		{
			log.error(null, e);
			return false;
		}

	}
	
	public synchronized Socket getConn()
	{
		if(!isSocketAvail(socket))
		{
			if(!buildLink())
			{
				log.debug("����socket����ʧ��");
				socket=null;
			}
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
