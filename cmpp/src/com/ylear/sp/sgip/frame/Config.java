/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package com.ylear.sp.sgip.frame;

import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Administrator
 */
public class Config
{

	private static final Log log = LogFactory.getLog(Config.class);// 记录日志

	private static Config cfg = new Config();

	/**
	 * 与SMG通讯 remoteServer 电信登陆服务器地址 remotePort 电信服务器端口号 loginName 电信网关登陆名称
	 * loginPass 电信网关登陆密码 spid 企业编号 spNumber 接入号
	 */
	private String remoteServer;
	private int remotePort = 7890;
	private String loginName;
	private String loginPswd;
	private String spnumber;
	private String spid;
	private String servicecode;

	private int version = 32;// cmpp2.0

	/**
	 * 计费配置
	 */
	private String feecode; // 计费号
	private String feetype;// 计费类型

	/**
	 * 程序配置
	 */
	private int sendThreadCount = 1;// 发送线程数
	private int recvThreadCount = 1;// 接收线程数

	private int maxSpeed = 20;// 每秒最大提交数

	private int timeout = 60000;// 通道超时时间
	private int heartbeat = 5000;// 心跳频率(ms)
	private int respTime = 60000;// 包等待确认时间

	private int retryTime = 3;// 最大重试次数，超过这个数之后丢弃

	private int multisendMaxTel = 10;// 最发最大接收号码数

	private Config()
	{
		load();

	}

	public static Config getInstance()
	{
		return cfg;
	}

	private Integer getNumber(Properties pps, String key)
	{
		Integer integer = null;
		String value = pps.getProperty(key);
		if (value == null)
		{
			log.error(String.format("key %s get value null", key));
		}
		else
		{
			if (StringUtils.isNumeric(value))
			{
				integer = Integer.parseInt(value);
			}
			else
			{
				log.error(String.format(
						"key %s get value %s but can't parseInt", key, value));
			}
		}

		return integer;

	}

	private String getString(Properties pps, String key)
	{
		String value = pps.getProperty(key);
		if (value == null)
		{
			log.error(String.format("key %s get value null", key));

		}
		return value;
	}

	private void load()
	{
		try
		{
			InputStream inputStream = getClass().getResourceAsStream(
					"/sms.properties");
			Properties pps = new Properties();
			pps.load(inputStream);

			this.setRemoteServer(getString(pps, "smg.server.addr"));
			this.setRemotePort(getNumber(pps, "smg.server.port"));
			this.setLoginName(getString(pps, "smg.server.loginname"));
			this.setLoginPswd(getString(pps, "smg.server.password"));
			this.setSpid(getString(pps, "smg.server.spid"));
			this.setSpnumber(getString(pps, "smg.server.spnumber"));
			this.setServicecode(getString(pps, "smg.server.servicecode"));

			this.setFeecode(getString(pps, "sp.sms.feecode"));
			this.setFeetype(getString(pps, "sp.sms.feetype"));

			this.setSendThreadCount(getNumber(pps, "sp.sms.send.thread"));
			this.setRecvThreadCount(getNumber(pps, "sp.sms.recv.thread"));
			this.setMaxSpeed(getNumber(pps, "sp.sms.send.maxspeed"));
			this.setTimeout(getNumber(pps, "sp.sms.timeout"));
			this.setHeartbeat(getNumber(pps, "sp.sms.heartbeat"));
			this.setRespTime(getNumber(pps, "sp.sms.resp.time"));
			this.setRetryTime(getNumber(pps, "sp.sms.retry.maxretry"));
			this.setMultisendMaxTel(getNumber(pps, "sp.sms.multisend.number"));

		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			log.error("读取配置文件失败", e);
		}

	}

	public String toString()
	{

		StringBuffer sb = new StringBuffer();
		sb.append("RemoteServer=").append(remoteServer).append("\r\n");
		sb.append("RemotePort=").append(remotePort).append("\r\n");
		sb.append("LoginName=").append(loginName).append("\r\n");
		sb.append("LoginPswd=").append(loginPswd).append("\r\n");
		sb.append("Spnumber=").append(spnumber).append("\r\n");
		sb.append("Spid=").append(spid).append("\r\n");
		sb.append("Servicecode=").append(servicecode).append("\r\n");
		sb.append("Feecode=").append(feecode).append("\r\n");
		sb.append("Feetype=").append(feetype).append("\r\n");
		sb.append("SendThreadCount=").append(sendThreadCount).append("\r\n");
		sb.append("RecvThreadCount=").append(recvThreadCount).append("\r\n");
		sb.append("MaxSpeed=").append(maxSpeed).append("\r\n");
		sb.append("Timeout=").append(timeout).append("\r\n");
		sb.append("Heartbeat=").append(heartbeat).append("\r\n");
		sb.append("RespTime=").append(respTime).append("\r\n");
		sb.append("RetryTime=").append(retryTime).append("\r\n");
		sb.append("MultisendMaxTel=").append(multisendMaxTel).append("\r\n");

		return sb.toString();

	}

	public String getRemoteServer()
	{
		return remoteServer;
	}

	void setRemoteServer(String remoteServer)
	{
		if (remoteServer != null)
		{
			this.remoteServer = remoteServer;
		}
	}

	public int getRemotePort()
	{
		return remotePort;
	}

	void setRemotePort(Integer remotePort)
	{
		if (remotePort != null)
		{
			this.remotePort = remotePort;
		}
	}

	public String getLoginName()
	{
		return loginName;
	}

	void setLoginName(String loginName)
	{
		if (loginName != null)
		{
			this.loginName = loginName;
		}
	}

	public String getLoginPswd()
	{
		return loginPswd;
	}

	void setLoginPswd(String loginPswd)
	{
		if (loginPswd != null)
		{
			this.loginPswd = loginPswd;
		}
	}

	public String getSpnumber()
	{
		return spnumber;
	}

	void setSpnumber(String spnumber)
	{
		if (spnumber != null)
		{
			this.spnumber = spnumber;
		}
	}

	public String getSpid()
	{
		return spid;
	}

	void setSpid(String spid)
	{
		if (spid != null)
		{
			this.spid = spid;
		}
	}

	public String getServicecode()
	{
		return servicecode;
	}

	void setServicecode(String servicecode)
	{
		if (servicecode != null)
		{
			this.servicecode = servicecode;
		}
	}

	public String getFeecode()
	{
		return feecode;
	}

	void setFeecode(String feecode)
	{
		if (feecode != null)
		{
			this.feecode = feecode;
		}
	}

	public String getFeetype()
	{
		return feetype;
	}

	void setFeetype(String feetype)
	{
		if (feetype != null)
		{
			this.feetype = feetype;
		}
	}

	public int getSendThreadCount()
	{
		return sendThreadCount;
	}

	public void setSendThreadCount(Integer sendThreadCount)
	{
		if (sendThreadCount != null)
		{
			this.sendThreadCount = sendThreadCount;
		}
	}

	public int getRecvThreadCount()
	{
		return recvThreadCount;
	}

	public void setRecvThreadCount(Integer recvThreadCount)
	{
		if (recvThreadCount != null)
		{
			this.recvThreadCount = recvThreadCount;
		}
	}

	public int getMaxSpeed()
	{
		return maxSpeed;
	}

	public void setMaxSpeed(Integer maxSpeed)
	{
		if (maxSpeed != null)
		{
			this.maxSpeed = maxSpeed;
		}
	}

	public int getTimeout()
	{
		return timeout;
	}

	public void setTimeout(Integer timeout)
	{
		if (timeout != null)
		{
			this.timeout = timeout;
		}
	}

	public int getHeartbeat()
	{
		return heartbeat;
	}

	public void setHeartbeat(Integer heartbeat)
	{
		if (heartbeat != null)
		{
			this.heartbeat = heartbeat;
		}
	}

	public int getRespTime()
	{
		return respTime;
	}

	public void setRespTime(Integer respTime)
	{
		if (respTime != null)
		{
			this.respTime = respTime;
		}
	}

	public int getRetryTime()
	{
		return retryTime;
	}

	void setRetryTime(Integer retryTime)
	{
		if (retryTime != null)
		{
			this.retryTime = retryTime;
		}
	}

	public int getMultisendMaxTel()
	{
		return multisendMaxTel;
	}

	void setMultisendMaxTel(Integer multisendMaxTel)
	{
		if (multisendMaxTel != null)
		{
			this.multisendMaxTel = multisendMaxTel;
		}
	}

	public int getVersion()
	{
		return version;
	}

	public void setVersion(Integer version)
	{
		if (version != null)
		{
			this.version = version;
		}
	}

	public static void main(String[] args)
	{
		System.out.println(Config.getInstance());
	}

}
