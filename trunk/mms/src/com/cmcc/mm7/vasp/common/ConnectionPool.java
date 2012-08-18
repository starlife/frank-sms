/**
 * File Name:ConnectionPool.java Company: �й��ƶ����Ź�˾ Date : 2004-1-9
 */

package com.cmcc.mm7.vasp.common;

import java.net.Socket;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
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
	private int maxSize = 1;

	private static final LinkedBlockingQueue<Socket> que = new LinkedBlockingQueue<Socket>();// ���Ӷ���

	private static final List<ConnectionWrap> track = new LinkedList<ConnectionWrap>();// ����ʹ�úۼ�
	private int delCount = 0;// ��Ǳ�δɾ����������

	private static ConnectionPool instance = null;

	private ConnectionPool(String mmscIp, int timeout, int poolSize)
	{
		parse(mmscIp);
		this.timeout = timeout;
		this.maxSize = poolSize;
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
			while (true)
			{
				if (socket == null)
				{
					//�����ǰ��������û�ﵽ���ֵ����ô����һ��
					if ((track.size() - delCount) < this.maxSize)
					{
						socket = createSocket();
						if (socket != null)
						{
							// log.debug("���·����"+socket+"����켣������");
							track.add(new ConnectionWrap(socket, System
									.currentTimeMillis()));
						}
					}
					return socket;
				}

				ConnectionWrap wrap = this.findWrap(socket);
				if (wrap != null && wrap.isDel())
				{
					// socket��Ϊ�գ���������֤socket�Ƿ����
					if (!ConnectionUtil.isSocketAvail(socket))
					{
						socket = null;
					}
					else
					{
						// ��֤ʱ���Ƿ����
						long between = System.currentTimeMillis()
								- wrap.getActiveTime();
						if (between > timeout)
						{
							ConnectionUtil.closeSocket(socket);
							socket = null;
							log.debug("��ǰSocket�ϴ�ʹ��ʱ�䵽�����Ѿ�����" + between
									+ "ms,��Ҫ���½�������");
						}
					}
					// ��αɾ����wrapɾ��
					synchronized (track)
					{
						track.remove(wrap);
						delCount--;
					}
				}
				if (socket != null)
				{
					// log.debug("�Ӷ�����ȡ�õ�"+socket+"����켣������");
					track.add(new ConnectionWrap(socket, System
							.currentTimeMillis()));
					return socket;
				}
				socket = que.poll();
			}
		}
	}

	private ConnectionWrap findWrap(Socket socket)
	{
		if (socket == null)
		{
			return null;
		}
		ConnectionWrap wrap = null;
		synchronized (track)
		{
			Iterator<ConnectionWrap> it = track.iterator();
			while (it.hasNext())
			{
				wrap = it.next();
				if (wrap.getSocket() == socket)
				{
					return wrap;
				}
			}
		}
		return null;
	}

	/**
	 * ����socket����
	 * 
	 * @param socket
	 */
	public void freeSocket(Socket socket)
	{
		synchronized (que)
		{
			connLog.debug("que.size():" + que.size() + ",track.size():"
					+ track.size() + "===" + track);

			// ��ʹ�ù켣�����в���
			ConnectionWrap wrap = this.findWrap(socket);
			if (wrap != null)
			{
				if (!ConnectionUtil.isSocketAvail(socket))
				{
					log.debug("���յ�����" + socket + "�����ã�ֱ��ɾ��");
					synchronized (track)
					{
						track.remove(wrap);
					}
				}
				else
				{
					// log.debug("���յ�����"+socket+"���ã�αɾ��");
					wrap.setDel(true);
					delCount++;
					if (!que.contains(socket))
					{
						que.offer(socket);
					}
				}

			}

		}

	}

	public static void main(String[] args)
	{
		ConnectionPool pool = ConnectionPool.getPool();
		if (pool == null)
		{
			ConnectionPool.init("211.140.27.30:5700", 5000, 5);
			pool = ConnectionPool.getPool();
		}
		while (true)
		{
			Socket socket = pool.getSocket();
			try
			{
				java.util.concurrent.TimeUnit.SECONDS.sleep(1);
			}
			catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			pool.freeSocket(socket);
		}
	}

}
