/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package com.ylear.sp.sgip.frame;

import com.chinamobile.cmpp2_0.protocol.util.Constants;

/**
 * 这是程序执行的入口，入口函数main()在这个类里定义
 * 
 * @author Administrator
 */
public class Main
{
	private ManagerThread tm;
	private boolean stop = false;

	public void start()
	{
		// 设置Max_speed
		Constants.MAX_SPEED = Config.getInstance().getMaxSpeed();
		tm = new ManagerThread(Config.getInstance().getSendThreadCount(),
				Config.getInstance().getRecvThreadCount());
		tm.start();
		while (!stop)
		{
			;
		}

	}

	public void stop()
	{
		if (tm != null)
		{
			tm.myStop();
		}
		stop = true;
	}

	public static void main(String[] arg)
	{
		Main server = new Main();
		try
		{
			server.start();
		}
		finally
		{
			server.stop();
		}

	}
}
