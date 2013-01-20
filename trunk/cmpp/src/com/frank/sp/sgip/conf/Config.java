/**
 * File Name:MM7Config.java
 */

package com.frank.sp.sgip.conf;

import java.util.Map;

public class Config
{

	private String SMGServer;
	private int SMGPort = 8801;
	private String LoginName;// 登陆用户名
	private String LoginPass;// 登录密码

	private String SPID;// spid
	private String SPNumber;// 接入号
	private String ServiceCode;// 业务代码
	private String NodeID;//

	private boolean FeeSwitch = false;// 计费开关
	private int FeeType = 1;// 计费类型
	private String FeeValue = "0";// 计费值 单位 分

	private String ListenAddr;// 本地服务端ip地址
	private int ListenPort;// 本地服务端端口
	private int BackLog;// 本地服务最大监听数

	private int TimeOut;// socket连接超时时间
	private int SendThread = 1;// 发送线程
	private int RetryCount = 3;// 重发次数
	private int MaxSpeed = 2;// 每秒发送条数
	private int MassCount = 10;// 群发短信每条短信号码数
	private String RMI;
	private String NotifyURL;

	// private boolean bload=false;

	/** 默认构造方法 */
	public Config()
	{
	}

	/** 构造方法。参数必须传递系统配置文件名 */
	public Config(String configFile)
	{
		// bload=true;
		load(configFile);

	}

	/** 加载配置文件 */
	public void load(String configFileName)
	{
		// bload=true;
		ConfigManager cm = new ConfigManager();
		cm.load(configFileName);
		Map<String, String> map = cm.map;
		SMGServer = (String) map.get("SMGServer");
		SMGPort = Integer.parseInt((String) map.get("SMGPort"));
		LoginName = (String) map.get("LoginName");
		LoginPass = (String) map.get("LoginPass");

		SPID = (String) map.get("SPID");
		SPNumber = (String) map.get("SPNumber");
		ServiceCode = (String) map.get("ServiceCode");
		NodeID = (String) map.get("NodeID");

		FeeSwitch = Boolean.parseBoolean((String) map.get("FeeSwitch"));
		FeeType = Integer.parseInt((String) map.get("FeeType"));
		FeeValue = (String) map.get("FeeValue");

		ListenAddr = (String) map.get("ListenAddr");
		ListenPort = Integer.parseInt((String) map.get("ListenPort"));
		BackLog = Integer.parseInt((String) map.get("BackLog"));

		TimeOut = Integer.parseInt((String) map.get("TimeOut"));
		SendThread = Integer.parseInt((String) map.get("SendThread"));
		RetryCount = Integer.parseInt((String) map.get("RetryCount"));
		MaxSpeed = Integer.parseInt((String) map.get("MaxSpeed"));
		MassCount = Integer.parseInt((String) map.get("MassCount"));
		RMI = (String) map.get("RMI");
		NotifyURL = (String) map.get("NotifyURL");

	}

	/** 保存配置文件 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer();

		sb.append("<?xml version=\"1\"?>\r\n<SmsConfig>\r\n");

		sb.append("\t<SMGServer>" + SMGServer + "</SMGServer>\r\n");
		sb.append("\t<SMGPort>" + SMGPort + "</SMGPort>\r\n");
		sb.append("\t<LoginName>" + LoginName + "</LoginName>\r\n");
		sb.append("\t<LoginPass>" + LoginPass + "</LoginPass>\r\n");

		sb.append("\t<SPID>" + SPID + "</SPID>\r\n");
		sb.append("\t<SPNumber>" + SPNumber + "</SPNumber>\r\n");
		sb.append("\t<ServiceCode>" + ServiceCode + "</ServiceCode>\r\n");
		sb.append("\t<NodeID>" + NodeID + "</NodeID>\r\n");

		sb.append("\t<FeeSwitch>" + FeeSwitch + "</FeeSwitch>\r\n");
		sb.append("\t<FeeType>" + FeeType + "</FeeType>\r\n");
		sb.append("\t<FeeValue>" + FeeValue + "</FeeValue>\r\n");

		sb.append("\t<ListenAddr>" + ListenAddr + "</ListenAddr>\r\n");
		sb.append("\t<ListenPort>" + ListenPort + "</ListenPort>\r\n");
		sb.append("\t<BackLog>" + BackLog + "</BackLog>\r\n");

		sb.append("\t<TimeOut>" + TimeOut + "</TimeOut>\r\n");
		sb.append("\t<SendThread>" + SendThread + "</SendThread>\r\n");
		sb.append("\t<RetryCount>" + RetryCount + "</RetryCount>\r\n");
		sb.append("\t<MaxSpeed>" + MaxSpeed + "</MaxSpeed>\r\n");
		sb.append("\t<MassCount>" + MassCount + "</MassCount>\r\n");

		sb.append("\t<RMI>" + RMI + "</RMI>\r\n");
		sb.append("\t<NotifyURL>" + NotifyURL + "</NotifyURL>\r\n");
		sb.append("</SmsConfig>\r\n");
		return sb.toString();
	}

	public String getSMGServer()
	{
		return SMGServer;
	}

	public void setSMGServer(String server)
	{
		SMGServer = server;
	}

	public int getSMGPort()
	{
		return SMGPort;
	}

	public void setSMGPort(int port)
	{
		SMGPort = port;
	}

	public String getLoginName()
	{
		return LoginName;
	}

	public void setLoginName(String loginName)
	{
		LoginName = loginName;
	}

	public String getLoginPass()
	{
		return LoginPass;
	}

	public void setLoginPass(String loginPass)
	{
		LoginPass = loginPass;
	}

	public String getSPID()
	{
		return SPID;
	}

	public void setSPID(String spid)
	{
		SPID = spid;
	}

	public String getSPNumber()
	{
		return SPNumber;
	}

	public void setSPNumber(String number)
	{
		SPNumber = number;
	}

	public String getServiceCode()
	{
		return ServiceCode;
	}

	public void setServiceCode(String serviceCode)
	{
		ServiceCode = serviceCode;
	}

	public boolean isFeeSwitch()
	{
		return FeeSwitch;
	}

	public void setFeeSwitch(boolean feeSwitch)
	{
		FeeSwitch = feeSwitch;
	}

	public int getFeeType()
	{
		return FeeType;
	}

	public void setFeeType(int feeType)
	{
		FeeType = feeType;
	}

	public String getFeeValue()
	{
		return FeeValue;
	}

	public void setFeeValue(String feeValue)
	{
		FeeValue = feeValue;
	}

	public String getListenAddr()
	{
		return ListenAddr;
	}

	public void setListenAddr(String listenAddr)
	{
		ListenAddr = listenAddr;
	}

	public int getListenPort()
	{
		return ListenPort;
	}

	public void setListenPort(int listenPort)
	{
		ListenPort = listenPort;
	}

	public int getBackLog()
	{
		return BackLog;
	}

	public void setBackLog(int backLog)
	{
		BackLog = backLog;
	}

	public int getTimeOut()
	{
		return TimeOut;
	}

	public void setTimeOut(int timeOut)
	{
		TimeOut = timeOut;
	}

	public int getSendThread()
	{
		return SendThread;
	}

	public void setSendThread(int sendThread)
	{
		SendThread = sendThread;
	}

	public int getRetryCount()
	{
		return RetryCount;
	}

	public void setRetryCount(int retryCount)
	{
		RetryCount = retryCount;
	}

	public int getMaxSpeed()
	{
		return MaxSpeed;
	}

	public void setMaxSpeed(int maxSpeed)
	{
		MaxSpeed = maxSpeed;
	}

	public int getMassCount()
	{
		return MassCount;
	}

	public void setMassCount(int massCount)
	{
		MassCount = massCount;
	}

	public String getRMI()
	{
		return RMI;
	}

	public void setRMI(String rmi)
	{
		RMI = rmi;
	}

	public String getNotifyURL()
	{
		return NotifyURL;
	}

	public void setNotifyURL(String notifyURL)
	{
		NotifyURL = notifyURL;
	}

	
	public String getNodeID()
	{
		return NodeID;
	}

	public void setNodeID(String nodeID)
	{
		NodeID = nodeID;
	}
	public static void main(String[] args)
	{
		Config config = new Config("./config/SmsConfig.xml");
		System.out.println(config);
	}


}
