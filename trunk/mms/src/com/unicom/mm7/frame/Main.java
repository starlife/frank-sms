package com.unicom.mm7.frame;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
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
		Sender.maxSrcID = config.getMassCount();
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
			String rmi=config.getRmi();
			int index1=rmi.lastIndexOf("/");
			int index2=rmi.lastIndexOf(":");
			int port =Integer.parseInt(rmi.substring(index2+1,index1));
			
			LocateRegistry.createRegistry(port);

			// ��Զ�̶���ע�ᵽRMIע��������ϣ�������ΪRHello
			// �󶨵�URL��׼��ʽΪ��rmi://host:port/name(����Э��������ʡ�ԣ���������д��������ȷ�ģ�
			Naming.bind(config.getRmi(), mmsService);
			// Naming.bind("//localhost:8888/RHello",rhello);

			System.out.println(">>>>>INFO:Զ��mmsService����󶨳ɹ����˿�"+port);
		}
		catch (RemoteException e)
		{
			System.out.println("����Զ�̶������쳣��");
			e.printStackTrace();
		}
		catch (AlreadyBoundException e)
		{
			System.out.println("�����ظ��󶨶����쳣��");
			e.printStackTrace();
		}
		catch (MalformedURLException e)
		{
			System.out.println("����URL�����쳣��");
			e.printStackTrace();
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
		MM7Config mm7Config = new MM7Config("./config/uni-mm7Config.xml");
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
