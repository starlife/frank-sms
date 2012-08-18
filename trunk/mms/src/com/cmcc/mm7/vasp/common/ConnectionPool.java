/**
 * File Name:ConnectionPool.java Company: 中国移动集团公司 Date : 2004-1-9
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

	private static final LinkedBlockingQueue<Socket> que = new LinkedBlockingQueue<Socket>();// 连接队列

	private static final List<ConnectionWrap> track = new LinkedList<ConnectionWrap>();// 保存使用痕迹
	private int delCount = 0;// 标记被未删除的连接数

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

	public synchronized Socket getSocket()
	{
		synchronized (que)
		{
			Socket socket = que.poll();
			while (true)
			{
				if (socket == null)
				{
					//如果当前连接数还没达到最大值，那么创建一个
					if ((track.size() - delCount) < this.maxSize)
					{
						socket = createSocket();
						if (socket != null)
						{
							// log.debug("把新分配的"+socket+"加入轨迹队列中");
							track.add(new ConnectionWrap(socket, System
									.currentTimeMillis()));
						}
					}
					return socket;
				}

				ConnectionWrap wrap = this.findWrap(socket);
				if (wrap != null && wrap.isDel())
				{
					// socket不为空，下面是验证socket是否可用
					if (!ConnectionUtil.isSocketAvail(socket))
					{
						socket = null;
					}
					else
					{
						// 验证时间是否过期
						long between = System.currentTimeMillis()
								- wrap.getActiveTime();
						if (between > timeout)
						{
							ConnectionUtil.closeSocket(socket);
							socket = null;
							log.debug("当前Socket上次使用时间到现在已经超过" + between
									+ "ms,需要重新建立连接");
						}
					}
					// 把伪删除的wrap删除
					synchronized (track)
					{
						track.remove(wrap);
						delCount--;
					}
				}
				if (socket != null)
				{
					// log.debug("从队列中取得的"+socket+"加入轨迹队列中");
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
	 * 回收socket连接
	 * 
	 * @param socket
	 */
	public void freeSocket(Socket socket)
	{
		synchronized (que)
		{
			connLog.debug("que.size():" + que.size() + ",track.size():"
					+ track.size() + "===" + track);

			// 在使用轨迹队列中查找
			ConnectionWrap wrap = this.findWrap(socket);
			if (wrap != null)
			{
				if (!ConnectionUtil.isSocketAvail(socket))
				{
					log.debug("回收的连接" + socket + "不可用，直接删除");
					synchronized (track)
					{
						track.remove(wrap);
					}
				}
				else
				{
					// log.debug("回收的连接"+socket+"可用，伪删除");
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
