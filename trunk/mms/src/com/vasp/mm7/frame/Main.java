package com.vasp.mm7.frame;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cmcc.mm7.vasp.common.Constants;
import com.vasp.mm7.conf.MM7Config;

public class Main
{
	private static final Log log = LogFactory.getLog(Main.class);

	private ManagerThread tm;
	private boolean stop = false;

	private MM7Config config = null;

	public Main(MM7Config config)
	{
		this.config = config;
	}

	public void start()
	{
		// 设置Max_speed
		log.info("启动彩信程序...");
		Constants.MAX_SPEED = config.getMaxSpeed();
		tm = new ManagerThread(config,config.getSendThread());
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
	
	public static void main(String[] args)
	{
		MM7Config mm7Config = new MM7Config("./config/mm7Config.xml");
		log.info("读取程序配置信息\r\n "+mm7Config);
		// mm7Config.setConnConfigName("./config/ConnConfig.xml");
		Main server = new Main(mm7Config);
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
