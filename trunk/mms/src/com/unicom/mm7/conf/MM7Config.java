/**
 * File Name:MM7Config.java
 */

package com.unicom.mm7.conf;

import java.util.Map;



public class MM7Config
{
	private int AuthenticationMode;// ��Ȩģʽ 0��ʾ����Ȩ 1��ʾ������Ȩ 2��ʾժҪ��Ȩ
	private String UserName;// ��Ȩ��½�û���
	private String Password;// ��Ȩ��½����
	
	private String MMSCURL;// �����ύurl
	private String MMSCIP;// �����ύ���ص�ַ
	private String VASPID;// SPID
	private String VASID;// �����
	private String ServiceCode;// ҵ�����
	
	private int MaxMsgSize;// �����Ϣ����
	
	private String CharSet;// ����Ĭ�ϱ�������
	private String ListenIP;// ���ط����ip��ַ
	private int ListenPort;// ���ط���˶˿�
	private int TimeOut;// ��ʱʱ�䣬���ڳ�����
	private int ReSendCount;// �ط�����
	public int BackLog;// ���ط�����������
	
	private String MMSCID;// �������ر���
	private String ConnConfigName;
	
	private boolean keepAlive=false;
	
	private int poolSize=1;//���ƶ����ص�socket���ӳ�
	
	private int sendThread=1;//�����߳�
	private int maxSpeed=2;//ÿ�뷢������
	
	private int massCount=10;//Ⱥ������ÿ�����ź�����
	
	//private boolean bload=false;

	/** Ĭ�Ϲ��췽�� */
	public MM7Config()
	{
	}

	/** ���췽�����������봫��ϵͳ�����ļ��� */
	public MM7Config(String configFileName)
	{
		//bload=true;
		load(configFileName);
		
	}

	/** ���������ļ� */
	public void load(String configFileName)
	{
		//bload=true;
		MM7ConfigManager mm7c = new MM7ConfigManager();
		mm7c.load(configFileName);
		Map<String, String> hashmap = mm7c.map;
		AuthenticationMode = Integer.parseInt((String) hashmap
				.get("AuthenticationMode"));
		UserName = (String) hashmap.get("UserName");
		Password = (String) hashmap.get("Password");
		MaxMsgSize = Integer.parseInt((String) hashmap.get("MaxMessageSize"));
		CharSet = (String) hashmap.get("Charset");
		MMSCURL = (String) hashmap.get("mmscURL");
		MMSCIP = hashmap.get("mmscIP");
		VASPID = hashmap.get("VASPID");
		VASID = hashmap.get("VASID");
		ServiceCode = hashmap.get("ServiceCode");
		ListenIP = (String) hashmap.get("ListenIP");
		ListenPort = Integer.parseInt((String) hashmap.get("ListenPort"));
		BackLog = Integer.parseInt((String) hashmap.get("BackLog"));
		TimeOut = Integer.parseInt((String) hashmap.get("TimeOut"));
		ReSendCount = Integer.parseInt((String) hashmap.get("ReSendCount"));
		MMSCID = (String) hashmap.get("MmscID");
		keepAlive="on".equals((String)hashmap.get("KeepAlive"));
		poolSize=Integer.parseInt((String)hashmap.get("PoolSize"));
		sendThread=Integer.parseInt((String)hashmap.get("SendThread"));
		maxSpeed=Integer.parseInt((String)hashmap.get("MaxSpeed"));
		massCount=Integer.parseInt((String)hashmap.get("MassCount"));
		
	}

	public void setAuthenticationMode(int authMode) // ���ü�Ȩ��ʽ
	{
		AuthenticationMode = authMode;
	}

	public int getAuthenticationMode() // ��ü�Ȩ��ʽ
	{
		return (AuthenticationMode);
	}

	public void setUserName(String s_userName) // ���ü�Ȩ�û���
	{
		UserName = s_userName;
	}

	public String getUserName() // ��ü�Ȩ�û���
	{
		return (UserName);
	}

	public void setPassword(String s_password) // ���ü�Ȩ����
	{
		Password = s_password;
	}

	public String getPassword() // ��ü�Ȩ����
	{
		return (Password);
	}

	public void setMMSCURL(String urL) // ����MMSC��URL
	{
		MMSCURL = urL;
	}

	public String getMMSCURL() // ���MMSC��URL
	{
		return (MMSCURL);
	}

	public void setMMSCIP(String ip) // ����MMSC��IP�б�
	{
		MMSCIP = ip;
	}

	public String getMMSCIP() // ���MMSC��IP�б�
	{
		return (MMSCIP);
	}

	public void setMaxMsgSize(int maxSize) // ��������������Ϣ�Ĵ�С
	{
		MaxMsgSize = maxSize;
	}

	public int getMaxMsgSize() // �������������Ϣ�Ĵ�С
	{
		return (MaxMsgSize);
	}

	public void setCharSet(String charSet) // ���ö���Ϣ������ַ���
	{
		CharSet = charSet;
	}

	public String getCharSet() // ��ö���Ϣ������ַ���
	{
		return (CharSet);
	}

	public void setListenIP(String listenIP) // ���ü���IP��ַ
	{
		ListenIP = listenIP;
	}

	public String getListenIP() // ��ü���IP��ַ
	{
		return (ListenIP);
	}

	public void setListenPort(int port) // ���ü����˿�
	{
		ListenPort = port;
	}

	public int getListenPort() // ��ü����˿�
	{
		return (ListenPort);
	}

	public void setBackLog(int backlog)
	{
		BackLog = backlog;
	}

	public int getBackLog()
	{
		return BackLog;
	}

	public void setTimeOut(int timeout)
	{
		TimeOut = timeout;
	}

	public int getTimeOut()
	{
		return TimeOut;
	}

	public void setReSendCount(int resend)
	{
		ReSendCount = resend;
	}

	public int getReSendCount()
	{
		return ReSendCount;
	}

	public void setConnConfigName(String name)
	{
		ConnConfigName = name;
	}

	public String getConnConfigName()
	{
		return ConnConfigName;
	}

	public String getVASPID()
	{
		return VASPID;
	}

	public void setVASPID(String vaspid)
	{
		VASPID = vaspid;
	}

	public String getVASID()
	{
		return VASID;
	}

	public void setVASID(String vasid)
	{
		VASID = vasid;
	}

	public String getServiceCode()
	{
		return ServiceCode;
	}

	public void setServiceCode(String serviceCode)
	{
		ServiceCode = serviceCode;
	}

	public String getMMSCID()
	{
		return MMSCID;
	}

	public void setMMSCID(String mmscid)
	{
		MMSCID = mmscid;
	}

	/** ���������ļ� */
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("<vasp:MM7Config>");
		sb.append("\r\n");
		sb.append("<AuthenticationMode>" + AuthenticationMode
				+ "</AuthenticationMode>");
		sb.append("\r\n");
		sb.append("<UserName>" + UserName + "</UserName>");
		sb.append("\r\n");
		sb.append("<Password>" + Password + "</Password>");
		sb.append("\r\n");
		sb.append("<VASPID>" + VASPID + "</VASPID>");
		sb.append("\r\n");
		sb.append("<VASID>" + VASID + "</VASID>");
		sb.append("\r\n");
		sb.append("<ServiceCode>" + ServiceCode + "</ServiceCode>");
		sb.append("\r\n");
		sb.append("<MaxMessageSize>" + MaxMsgSize + "</MaxMessageSize>");
		sb.append("\r\n");
		sb.append("<Charset>" + CharSet + "</Charset>");
		sb.append("\r\n");
		sb.append("<mmscURL>" + MMSCURL + "</mmscURL>");
		sb.append("\r\n");
		sb.append("<mmscIP>" + MMSCIP + "</mmscIP>");
		sb.append("\r\n");
		sb.append("<MmscId>" + MMSCID + "</MmscId>");
		sb.append("\r\n");
		sb.append("<ListenIP>" + ListenIP + "</ListenIP>");
		sb.append("\r\n");
		sb.append("<ListenPort>" + ListenPort + "</ListenPort>");
		sb.append("\r\n");
		sb.append("<BackLog>" + BackLog + "</BackLog>");
		sb.append("\r\n");
		sb.append("<KeepAlive>" + keepAlive + "</KeepAlive>");
		sb.append("\r\n");
		sb.append("<PoolSize>" + poolSize + "</PoolSize>");
		sb.append("\r\n");
		sb.append("<TimeOut>" + TimeOut + "</TimeOut>");
		sb.append("\r\n");
		sb.append("<ReSendCount>" + ReSendCount + "</ReSendCount>");
		sb.append("\r\n");
		sb.append("<SendThread>" + sendThread + "</SendThread>");
		sb.append("\r\n");
		sb.append("<MaxSpeed>" + maxSpeed + "</MaxSpeed>");
		sb.append("\r\n");
		sb.append("<MassCount>" + massCount + "</MassCount>");
		sb.append("\r\n");
		sb.append("</vasp:MM7Config>");
		return sb.toString();
	}

	public static void main(String[] args)
	{
		MM7Config config = new MM7Config("./config/mm7Config.xml");
		System.out.println(config);
	}

	public boolean isKeepAlive()
	{
		return keepAlive;
	}

	public void setKeepAlive(boolean keepAlive)
	{
		this.keepAlive = keepAlive;
	}

	public int getPoolSize()
	{
		return poolSize;
	}

	public void setPoolSize(int poolSize)
	{
		this.poolSize = poolSize;
	}

	public int getSendThread()
	{
		return sendThread;
	}

	public void setSendThread(int sendThread)
	{
		this.sendThread = sendThread;
	}

	public int getMaxSpeed()
	{
		return maxSpeed;
	}

	public void setMaxSpeed(int maxSpeed)
	{
		this.maxSpeed = maxSpeed;
	}

	public int getMassCount()
	{
		return massCount;
	}

	public void setMassCount(int massCount)
	{
		this.massCount = massCount;
	}
}
