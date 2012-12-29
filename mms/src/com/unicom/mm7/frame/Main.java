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
		// ����Max_speed
		log.info("�������ų���...");
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
			// ����һ��Զ�̶���
			MmsService mmsService = new MmsServiceImpl();
			// ���������ϵ�Զ�̶���ע���Registry��ʵ������ָ���˿�Ϊ8888����һ���ز����٣�JavaĬ�϶˿���1099�����ز���ȱ��һ����ȱ��ע����������޷��󶨶���Զ��ע�����
			String rmi = config.getRmi();
			int index1 = rmi.lastIndexOf("/");
			int index2 = rmi.lastIndexOf(":");
			int port = Integer.parseInt(rmi.substring(index2 + 1, index1));

			LocateRegistry.createRegistry(port);

			// �󶨵�URL��׼��ʽΪ��rmi://host:port/name(����Э��������ʡ�ԣ���������д��������ȷ�ģ�
			Naming.bind(config.getRmi(), mmsService);

			log.info("Զ��mmsService����󶨳ɹ����˿�" + port);
		}
		catch (Exception e)
		{
			log.error("����Զ�̶������쳣", e);
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
		log.info("��ȡ����������Ϣ\r\n " + mm7Config);
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
