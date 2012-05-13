/**
 * File Name:ConnectionPool.java Company: 中国移动集团公司 Date : 2004-1-9
 */

package com.cmcc.mm7.vasp.common;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cmcc.mm7.vasp.conf.MM7Config;
import com.cmcc.mm7.vasp.conf.MM7ConfigManager;

public class ConnectionPool implements Runnable
{
	private static final Log log = LogFactory.getLog(ConnectionPool.class);
	// public static List ClientList;
	private static final List<ConnectionWrap> que = new LinkedList<ConnectionWrap>();;
	private static boolean isCreate;
	// public HashMap hashmap;
	// private long time;
	// private int IPCount;
	// private MM7Config Mm7Config;
	//private String NonceCount;
	private int ServerMaxSize;
	private String KeepAlive;
	private long KeepAliveTimeout;
	private int minCount;// 最小连接数
	private int maxCount;// 最大连接数
	private int step;// 连接递增步长
	private String[] mmscIpList = null;

	private static int mmscIpIndex = 0;

	private static final ConnectionPool m_instance = new ConnectionPool();

	private ConnectionPool()
	{
		// hashmap = new HashMap();
		isCreate = false;
		// Mm7Config = null;
		//NonceCount = "00000001";
		ServerMaxSize = 0;
		KeepAlive = "off";
	}

	public static ConnectionPool getInstance()
	{
		return m_instance;
	}

	public void setConfig(MM7Config mm7config)
	{
		// Mm7Config = mm7config;
		init(mm7config);
		if (!isCreate)
		{
			Thread thread = new Thread(this);
			thread.run();
			isCreate = true;
		}
	}

	/*public void setNonceCount(String nc)
	{
		NonceCount = nc;
	}

	public String getNonceCount()
	{
		return NonceCount;
	}*/

	/*public void setInitNonceCount()
	{
		setNonceCount("00000001");
	}*/

	private void setServerMaxSize(int size)
	{
		ServerMaxSize = size;
	}

	public int getServerMaxSize()
	{
		return ServerMaxSize;
	}

	private void setKeepAlive(String conn)
	{
		KeepAlive = conn;
	}

	public String getKeepAlive()
	{
		return KeepAlive;
	}

	// 从配置文件中获得一些基本信息
	private void init(MM7Config config)
	{
		// hashmap.clear();
		MM7ConfigManager confManager = new MM7ConfigManager();
		this.mmscIpList = new String[config.getMMSCIP().size()];
		for (int i = 0; i < config.getMMSCIP().size(); i++)
		{
			mmscIpList[i] = (String) config.getMMSCIP().get(i);
		}
		String name = config.getConnConfigName();
		if (!name.equals(""))
		{
			confManager.load(name);
			// 长连接开关
			String keepAlive = (String) confManager.hashmap.get("KeepAlive");
			if (keepAlive != null)
			{
				this.setKeepAlive(keepAlive);
			}
			// 服务端最大连接数
			String serverMaxSize = (String) confManager.hashmap
					.get("ServerMaxKeepAlive");
			if (serverMaxSize != null)
			{
				this.setServerMaxSize(Integer.parseInt(serverMaxSize));
			}
			String KeepAliveTimeout = (String) confManager.hashmap
					.get("KeepAliveTimeout");

			if (KeepAliveTimeout != null)
			{
				this.KeepAliveTimeout = Long.parseLong(KeepAliveTimeout);
			}
			String minKeepAliveRequests = (String) confManager.hashmap
					.get("MinKeepAliveRequests");
			if (minKeepAliveRequests != null)
			{
				this.minCount = Integer.parseInt(minKeepAliveRequests);
			}
			String MaxKeepAliveRequests = (String) confManager.hashmap
					.get("MaxKeepAliveRequests");
			if (MaxKeepAliveRequests != null)
			{
				this.maxCount = Integer.parseInt(MaxKeepAliveRequests);
			}
			String step = (String) confManager.hashmap.get("step");
			if (step != null)
			{
				this.step = Integer.parseInt(step);
			}

		}

	}
	
	private int getIndex()
	{
		if (mmscIpList == null || mmscIpList.length == 0)
		{
			return 0;
		}
		mmscIpIndex++;
		mmscIpIndex = mmscIpIndex % mmscIpList.length;
		return mmscIpIndex;
	}

	
	
	
	
	// 获得空闲的连接
	public synchronized ConnectionWrap getConnWrap()
	{
		if (mmscIpList == null || mmscIpList.length == 0)
		{
			return null;
		}

		ConnectionWrap conn = null;
		// 如果是长连接，那么从队列中找可用的连接
		if (getKeepAlive().equals("on"))
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
					if (!conn.isDel()
							|| !conn.getSocket().isConnected())
					{
						// 删除失效的链接
						log.debug("删除失败连接：" + conn);
						it.remove();
						conn.close();
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
				if (que.size() < this.maxCount)
				{
					// 判断目前已有连接加入要新建的连接是否超过最大连接数

					conn = new ConnectionWrap(this.mmscIpList[getIndex()]);
					if (conn.buildLink())
					{
						que.add(conn);
						return conn;
					}

				}
			}

		}
		// 建一条短连接
		else
		{

			conn = new ConnectionWrap(this.mmscIpList[getIndex()]);
			if (conn.buildLink())
				return conn;

		}
		return null;

	}

	public void run()
	{

		// while (true) {
		try
		{
			Thread.sleep(this.KeepAliveTimeout);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		scan();
	}

	
	// 管理连接。一些超时的连接刷新
	private void scan()
	{

		if (que != null)
		{
			if (!que.isEmpty())
			{
				for (int i = 0; i < que.size(); i++)
				{
					ConnectionWrap conn = (ConnectionWrap) que.get(i);
					if (conn.activeTime > 0)
					{
						long time = System.currentTimeMillis()
								- conn.activeTime;
						if (time >= this.KeepAliveTimeout)
							conn.setFree(true);
					}
				}
			}
		}
	}

}
