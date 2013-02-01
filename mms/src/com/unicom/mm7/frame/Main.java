package com.unicom.mm7.frame;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cmcc.mm7.vasp.common.Constants;
import com.unicom.mm7.conf.MM7Config;
import com.unicom.mm7.rmi.MmsService;
import com.unicom.mm7.rmi.MmsServiceImpl;

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
		NotifyThread.URL = config.getNotifyURL();
		startRMIServer();
		tm = new ManagerThread(config, config.getSendThread());
		tm.start();
		while (!stop)
		{
			;
		}

	}

	public void startRMIServer()
	{
		try
		{
			// 创建一个远程对象
			MmsService mmsService = new MmsServiceImpl();
			// 本地主机上的远程对象注册表Registry的实例，并指定端口为8888，这一步必不可少（Java默认端口是1099），必不可缺的一步，缺少注册表创建，则无法绑定对象到远程注册表上
			String rmi = config.getRmi();
			int index1 = rmi.lastIndexOf("/");
			int index2 = rmi.lastIndexOf(":");
			int port = Integer.parseInt(rmi.substring(index2 + 1, index1));

			LocateRegistry.createRegistry(port);

			// 绑定的URL标准格式为：rmi://host:port/name(其中协议名可以省略，下面两种写法都是正确的）
			Naming.bind(config.getRmi(), mmsService);

			log.info("远程mmsService对象绑定成功！端口" + port);
		}
		catch (Exception e)
		{
			log.error("创建远程对象发生异常", e);
		}

	}

	public void stop()
	{
		if (tm != null)
		{
			tm.myStop();
		}
		NotifyThread.getInstance().myStop();
		stop = true;
	}

	public static void main(String[] args)
	{
		MM7Config mm7Config = new MM7Config("./config/mm7Config.xml");
		log.info("读取程序配置信息\r\n " + mm7Config);
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
