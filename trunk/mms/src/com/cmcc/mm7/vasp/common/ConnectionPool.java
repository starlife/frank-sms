/**
 * File Name:ConnectionPool.java Company: �й��ƶ����Ź�˾ Date : 2004-1-9
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
	private int minCount;// ��С������
	private int maxCount;// ���������
	private int step;// ���ӵ�������
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

	// �������ļ��л��һЩ������Ϣ
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
			// �����ӿ���
			String keepAlive = (String) confManager.hashmap.get("KeepAlive");
			if (keepAlive != null)
			{
				this.setKeepAlive(keepAlive);
			}
			// ��������������
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

	
	
	
	
	// ��ÿ��е�����
	public synchronized ConnectionWrap getConnWrap()
	{
		if (mmscIpList == null || mmscIpList.length == 0)
		{
			return null;
		}

		ConnectionWrap conn = null;
		// ����ǳ����ӣ���ô�Ӷ������ҿ��õ�����
		if (getKeepAlive().equals("on"))
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
					if (!conn.isDel()
							|| !conn.getSocket().isConnected())
					{
						// ɾ��ʧЧ������
						log.debug("ɾ��ʧ�����ӣ�" + conn);
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
					// �ж�Ŀǰ�������Ӽ���Ҫ�½��������Ƿ񳬹����������

					conn = new ConnectionWrap(this.mmscIpList[getIndex()]);
					if (conn.buildLink())
					{
						que.add(conn);
						return conn;
					}

				}
			}

		}
		// ��һ��������
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

	
	// �������ӡ�һЩ��ʱ������ˢ��
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
