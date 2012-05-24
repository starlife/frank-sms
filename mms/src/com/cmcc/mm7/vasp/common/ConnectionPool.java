/**
 * File Name:ConnectionPool.java Company: �й��ƶ����Ź�˾ Date : 2004-1-9
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
	// private int minCount;// ��С������
	//private int maxCount;// ���������
	// private int step;// ���ӵ�������
	
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

	public void offer(ConnectionWrap conn)
	{
		// ����ǳ����ӣ���ô��������Ϊ���ã�����Ƕ����ӣ���ô�ر�
		if (keepAlive)
		{
			conn.setFree(true);
		}
		else
		{
			conn.close();
		}
	}

	// ��ÿ��е�����
	public synchronized ConnectionWrap poll()
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

	}

}
