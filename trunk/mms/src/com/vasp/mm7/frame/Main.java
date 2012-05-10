package com.vasp.mm7.frame;

import com.cmcc.mm7.vasp.conf.MM7Config;

public class Main
{
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
		receiver.start();
		sender.start();
		while (!stop)
		{
			;
		}
	}

	public void stop()
	{
		sender.mystop();
		receiver.stop();
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
