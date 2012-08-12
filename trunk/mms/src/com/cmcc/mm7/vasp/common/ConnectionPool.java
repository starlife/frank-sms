/**
 * File Name:ConnectionPool.java Company: 中国移动集团公司 Date : 2004-1-9
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

	private static final LinkedBlockingQueue<Socket> que = new LinkedBlockingQueue<Socket>();// 连接队列
	
	private static final Map<Socket,Long> usedMap = new HashMap<Socket,Long>();//保存在使用中socket以及最后一次使用时间
	
	

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
			while(true)
			{	
				if(socket==null)
				{
					if(usedMap.size()<this.maxSize)
					{
						socket = createSocket();
						if(socket!=null)
						{
							//加入使用队列
							usedMap.put(socket,System.currentTimeMillis());
						}
					}
					return socket;
				}
				//socket不为空，下面是验证socket是否可用	
				if (!ConnectionUtil.isSocketAvail(socket))
				{
					socket=null;
				}else
				{
					//验证时间是否过期
					Long lastActive=usedMap.get(socket);
					if(lastActive!=null)
					{
						long between=(System.currentTimeMillis()-lastActive);
						if(between>timeout)
						{
							ConnectionUtil.closeSocket(socket);
							log.debug("当前Socket上次使用时间到现在已经超过" + between + "ms,需要重新建立连接");
						}
					}
				}
				
				if(socket!=null)
				{
					//加入使用队列
					usedMap.put(socket,System.currentTimeMillis());
					return socket;
				}				
				socket = que.poll();
			}
		}
	}
	
	/**
	 * 回收socket连接
	 * @param socket
	 */
	public void freeSocket(Socket socket)
	{
		synchronized (que)
		{
			if(usedMap.size()>100)
			{
				log.error("usedMap需要清空");
				connLog.info(usedMap);
				usedMap.clear();
				
			}
			//如果连接不可用，就清理痕迹；如果连接可用，那么重新加入队列中使用
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
