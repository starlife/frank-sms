/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package com.frank.sp.sgip.frame;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.chinaunicom.sgip1_2.protocol.util.Constants;
import com.frank.sp.sgip.conf.Config;
import com.frank.sp.sgip.rmi.SmsService;
import com.frank.sp.sgip.rmi.SmsServiceImpl;

/**
 * 这是程序执行的入口，入口函数main()在这个类里定义
 * 
 * @author Administrator
 */
public class Main
{
	private static final Log log = LogFactory.getLog(Main.class);
	private ManagerThread tm;
	private boolean stop = false;

	private Config config = null;

	public Main(Config config)
	{
		this.config = config;
	}

	public void start()
	{
		log.info("==============启动短信程序...=====================");
		// 设置Max_speed
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
			SmsService smsService = new SmsServiceImpl();
			// 本地主机上的远程对象注册表Registry的实例，并指定端口为7777，这一步必不可少（Java默认端口是1099），必不可缺的一步，缺少注册表创建，则无法绑定对象到远程注册表上
			String rmi = config.getRMI();
			int index1 = rmi.lastIndexOf("/");
			int index2 = rmi.lastIndexOf(":");
			int port = Integer.parseInt(rmi.substring(index2 + 1, index1));

			LocateRegistry.createRegistry(port);

			// 绑定的URL标准格式为：rmi://host:port/name(其中协议名可以省略，下面两种写法都是正确的）
			Naming.bind(config.getRMI(), smsService);
			log.info("远程smsService对象绑定成功！端口" + port);
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
		Config config = new Config("./config/SmsConfig.xml");
		log.info("读取程序配置信息\r\n " + config);
		// mm7Config.setConnConfigName("./config/ConnConfig.xml");
		Main server = new Main(config);
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
