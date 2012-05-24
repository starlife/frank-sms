/**
 * File Name:ConnectionPool.java Company: 中国移动集团公司 Date : 2004-1-9
 */

package com.cmcc.mm7.vasp.common;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ConnectionPool
{
	private static final Log log = LogFactory.getLog(ConnectionPool.class);
	// public static List ClientList;
	private static final List<ConnectionWrap> que = new LinkedList<ConnectionWrap>();;
	// private static boolean isCreate;
	// public HashMap hashmap;
	// private long time;
	// private int IPCount;
	// private MM7Config Mm7Config;
	// private String NonceCount;
	//private int ServerMaxSize;
	//private String KeepAlive;
	// private long KeepAliveTimeout;
	// private int minCount;// 最小连接数
	//private int maxCount;// 最大连接数
	// private int step;// 连接递增步长
	
	private boolean keepAlive=false;
	private String mmscIp = null;
	private int timeout = 10000;

	//private static final ConnectionPool m_instance = new ConnectionPool();

	public ConnectionPool(String mmscIp,int timeout,boolean keepAlive)
	{
		this.mmscIp=mmscIp;
		this.timeout=timeout;
		this.keepAlive=keepAlive;
	}

	
	/*public void setConfig(MM7Config mm7config)
	{
		// Mm7Config = mm7config;
		init(mm7config);

	}*/

	/*
	 * public void setNonceCount(String nc) { NonceCount = nc; } public String
	 * getNonceCount() { return NonceCount; }
	 */

	/*
	 * public void setInitNonceCount() { setNonceCount("00000001"); }
	 */

	
	// 从配置文件中获得一些基本信息
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
			// 长连接开关
			String keepAlive = (String) confManager.map.get("KeepAlive");
			if (keepAlive != null)
			{
				this.setKeepAlive(keepAlive);
			}
			// 服务端最大连接数
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

	public void offer(ConnectionWrap conn)
	{
		// 如果是长连接，那么重新设置为可用，如果是短连接，那么关闭
		if (keepAlive)
		{
			conn.setFree(true);
		}
		else
		{
			conn.close();
		}
	}

	// 获得空闲的连接
	public synchronized ConnectionWrap poll()
	{
		if (this.mmscIp == null)
		{
			return null;
		}

		ConnectionWrap conn = null;
		// 如果是长连接，那么从队列中找可用的连接
		if (keepAlive)
		{
			// 寻找空闲的连接
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
						// 删除失效的链接
						log.debug("删除失败连接：" + conn);
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
				
				
					// 判断目前已有连接加入要新建的连接是否超过最大连接数

					conn = new ConnectionWrap(mmscIp, timeout);
					if (conn.buildLink())
					{
						que.add(conn);
						return conn;
					}

				
			}

		}
		// 建一条短连接
		else
		{

			conn = new ConnectionWrap(mmscIp, timeout);
			if (conn.buildLink())
			{
				return conn;
			}

		}
		return null;

	}

}
