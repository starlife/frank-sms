package com.frank.sp.sgip.frame;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

import com.frank.sp.sgip.conf.Config;

/**
 * 线程管理类
 * 
 * @author Administrator
 */
public class ManagerThread extends Thread
{
	private volatile boolean stop = false;

	private final LinkedList<Receiver> recvThreadList = new LinkedList<Receiver>();
	private final LinkedList<Sender> sendThreadList = new LinkedList<Sender>();

	private static final Object recvLock = new Object();
	private static final Object sendLock = new Object();

	private int sendThreadCount = 1;
	private int recvThreadCount = 1;

	private Config config = null;

	public ManagerThread(Config config, int sendThreadCount)
	{
		super("ManagerThread");
		this.config = config;
		this.sendThreadCount = sendThreadCount;
		this.setDaemon(true);
	}

	@Override
	public void run()
	{
		while (!stop)
		{
			try
			{
				synchronized (sendLock)
				{
					for (int i = 0; i < sendThreadList.size(); i++)
					{
						Sender t = sendThreadList.get(i);
						if (t.getState() == Thread.State.TERMINATED)
						{
							sendThreadList.remove(i);
							break;
						}
					}
					if (sendThreadList.size() < this.sendThreadCount)
					{
						Sender t = new Sender(config);
						t.start();
						sendThreadList.add(t);
					}
				}

				synchronized (recvLock)
				{
					for (int i = 0; i < recvThreadList.size(); i++)
					{
						Receiver t = recvThreadList.get(i);
						if (t.getState() == Thread.State.TERMINATED)
						{
							recvThreadList.remove(i);
							break;
						}
					}
					if (recvThreadList.size() < this.recvThreadCount)
					{
						Receiver t = new Receiver(config);
						t.start();
						recvThreadList.add(t);
					}
				}
				// ˯��30s
				TimeUnit.SECONDS.sleep(30);

			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

		}
	}

	public void myStop()
	{
		synchronized (sendLock)
		{
			for (int i = 0; i < this.sendThreadList.size(); i++)
			{
				Sender sender = this.sendThreadList.get(i);
				if (sender != null)
				{
					sender.myStop();
				}
			}
			this.sendThreadList.clear();
		}

		synchronized (recvLock)
		{
			for (int i = 0; i < this.recvThreadList.size(); i++)
			{
				Receiver receiver = this.recvThreadList.get(i);
				receiver.myStop();
			}
			this.recvThreadList.clear();

		}
		this.stop = true;

	}

}
