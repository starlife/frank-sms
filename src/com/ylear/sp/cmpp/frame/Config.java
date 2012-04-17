/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package com.ylear.sp.cmpp.frame;

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
	
	private int version=48;//cmpp3.0

	/**
	 * 计费配置
	 */
	private String feecode; // 计费号
	private String feetype;// 计费类型

	/**
	 * 程序配置
	 */
	private int sendThread = 1;// 发送线程数
	private int sendQueSize = 100000;// 发送队列最大数
	private int revQueSize = 100000;// 接收队列最大数
	private int retryQueSize = 100000;// 重试队列最大数
	private int retryTime = 3;// 最大重试次数，超过这个数之后丢弃

	private int multisendMaxTel = 10;// 最发最大接收号码数

	/**
	 * logPath 日志目录
	 */
	// public String logPath;
	// public int display;
	/**
	 * feeFlag 计费标志 feeCode 计费多少
	 */
	// public String feeflag;
	// public String topid;
	// public int maxsrcid=50;
	/**
	 * 额外的 blackMobile 号码黑名单 trustIp 信任IP
	 */
	// public String blackMobile = "";
	// /public String trustIp;
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

			this.setSendThread(getNumber(pps, "sp.sms.send.thread"));
			this.setSendQueSize(getNumber(pps, "sp.sms.send.quesize"));
			this.setRevQueSize(getNumber(pps, "sp.sms.receive.quesize"));
			this.setRetryQueSize(getNumber(pps, "sp.sms.retry.quesize"));
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
		sb.append("remoteServer=" + remoteServer + "\r\n");
		sb.append("remotePort=" + remotePort + "\r\n");
		sb.append("loginName=" + loginName + "\r\n");
		sb.append("loginPswd=" + loginPswd + "\r\n");
		sb.append("spnumber=" + spnumber + "\r\n");
		sb.append("spid=" + spid + "\r\n");
		sb.append("servicecode=" + servicecode + "\r\n");
		sb.append("feecode=" + feecode + "\r\n");
		sb.append("feetype=" + feetype + "\r\n");
		sb.append("String spid=" + spid + "\r\n");
		sb.append("sendThread=" + sendThread + "\r\n");
		sb.append("sendQueSize=" + sendQueSize + "\r\n");
		sb.append("revQueSize=" + revQueSize + "\r\n");
		sb.append("retryQueSize=" + retryQueSize + "\r\n");
		sb.append("retryTime=" + retryTime + "\r\n");
		sb.append("multisendMaxTel=" + multisendMaxTel + "\r\n");

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

	public int getSendThread()
	{
		return sendThread;
	}

	void setSendThread(Integer sendThread)
	{
		if (sendThread != null)
		{
			this.sendThread = sendThread;
		}
	}

	public int getSendQueSize()
	{
		return sendQueSize;
	}

	void setSendQueSize(Integer sendQueSize)
	{
		if (sendQueSize != null)
		{
			this.sendQueSize = sendQueSize;
		}
	}

	public int getRevQueSize()
	{
		return revQueSize;
	}

	void setRevQueSize(Integer revQueSize)
	{
		if (revQueSize != null)
		{
			this.revQueSize = revQueSize;
		}
	}

	public int getRetryQueSize()
	{
		return retryQueSize;
	}

	void setRetryQueSize(Integer retryQueSize)
	{
		if (retryQueSize != null)
		{
			this.retryQueSize = retryQueSize;
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

	public static void main(String[] args)
	{
		System.out.println(Config.getInstance());
	}

	public int getVersion()
	{
		return version;
	}

	public void setVersion(int version)
	{
		this.version = version;
	}
}
