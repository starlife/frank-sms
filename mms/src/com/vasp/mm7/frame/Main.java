package com.vasp.mm7.frame;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cmcc.mm7.vasp.conf.MM7Config;

public class Main
{
	private static final Log log = LogFactory.getLog(Main.class);
	// private MM7Config config=null;
	private Receiver receiver = null;
	private Sender sender = null;
	private boolean stop = false;

	public Main(MM7Config config) throws Exception
	{
		// this.config=config;
		receiver = new Receiver(config);
		sender = new Sender(config);
	}

	public void start()
	{
		log.info("Æô¶¯²ÊÐÅ³ÌÐò...");
		sender.start();
		receiver.start();
		while (!stop)
		{
			;
		}
	}

	public void stop()
	{
		sender.myStop();
		receiver.mystop();
		stop = true;
	}

	public static void main() throws Exception
	{
		
		MM7Config mm7Config = new MM7Config("./config/mm7Config.xml");
		mm7Config.setConnConfigName("./config/ConnConfig.xml");
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

	public static void main(String[] args) throws Exception
	{
		main();
		// MM7Config mm7Config = new MM7Config("./config/mm7Config.xml");
		// System.out.println(mm7Config.getListenIP());
	}
}
