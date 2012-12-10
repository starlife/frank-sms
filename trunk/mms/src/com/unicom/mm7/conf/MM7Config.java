/**
 * File Name:MM7Config.java
 */

package com.unicom.mm7.conf;

import java.util.Map;



public class MM7Config
{
	private int AuthenticationMode;// 鉴权模式 0表示不鉴权 1表示基本鉴权 2表示摘要鉴权
	private String UserName;// 鉴权登陆用户名
	private String Password;// 鉴权登陆密码
	
	private String MMSCURL;// 彩信提交url
	private String MMSCIP;// 彩信提交网关地址
	private String VASPID;// SPID
	private String VASID;// 接入号
	private String ServiceCode;// 业务代码
	
	private int MaxMsgSize;// 最大消息长度
	
	private String CharSet;// 彩信默认编码类型
	private String ListenIP;// 本地服务端ip地址
	private int ListenPort;// 本地服务端端口
	private int TimeOut;// 超时时间，用于长连接
	private int ReSendCount;// 重发次数
	public int BackLog;// 本地服务最大监听数
	
	private String MMSCID;// 彩信网关编码
	private String ConnConfigName;
	
	private boolean keepAlive=false;
	
	private int poolSize=1;//和移动网关的socket连接池
	
	private int sendThread=1;//发送线程
	private int maxSpeed=2;//每秒发送条数
	
	private int massCount=10;//群发短信每条短信号码数
	
	//private boolean bload=false;

	/** 默认构造方法 */
	public MM7Config()
	{
	}

	/** 构造方法。参数必须传递系统配置文件名 */
	public MM7Config(String configFileName)
	{
		//bload=true;
		load(configFileName);
		
	}

	/** 加载配置文件 */
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

	public void setAuthenticationMode(int authMode) // 设置鉴权方式
	{
		AuthenticationMode = authMode;
	}

	public int getAuthenticationMode() // 获得鉴权方式
	{
		return (AuthenticationMode);
	}

	public void setUserName(String s_userName) // 设置鉴权用户名
	{
		UserName = s_userName;
	}

	public String getUserName() // 获得鉴权用户名
	{
		return (UserName);
	}

	public void setPassword(String s_password) // 设置鉴权口令
	{
		Password = s_password;
	}

	public String getPassword() // 获得鉴权口令
	{
		return (Password);
	}

	public void setMMSCURL(String urL) // 设置MMSC的URL
	{
		MMSCURL = urL;
	}

	public String getMMSCURL() // 获得MMSC的URL
	{
		return (MMSCURL);
	}

	public void setMMSCIP(String ip) // 设置MMSC的IP列表
	{
		MMSCIP = ip;
	}

	public String getMMSCIP() // 获得MMSC的IP列表
	{
		return (MMSCIP);
	}

	public void setMaxMsgSize(int maxSize) // 设置允许的最大消息的大小
	{
		MaxMsgSize = maxSize;
	}

	public int getMaxMsgSize() // 获得允许的最大消息的大小
	{
		return (MaxMsgSize);
	}

	public void setCharSet(String charSet) // 设置对消息编码的字符集
	{
		CharSet = charSet;
	}

	public String getCharSet() // 获得对消息编码的字符集
	{
		return (CharSet);
	}

	public void setListenIP(String listenIP) // 设置监听IP地址
	{
		ListenIP = listenIP;
	}

	public String getListenIP() // 获得监听IP地址
	{
		return (ListenIP);
	}

	public void setListenPort(int port) // 设置监听端口
	{
		ListenPort = port;
	}

	public int getListenPort() // 获得监听端口
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

	/** 保存配置文件 */
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
