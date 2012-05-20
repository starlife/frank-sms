package com.frank.ylear.modules.mmsreport.entity;

import com.frank.ylear.common.constant.Constants;

/**
 * SLogmmssubmit entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class SubmitBean implements java.io.Serializable
{

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String messageid;
	private String transactionid;
	private String mm7version;
	//private String senderAddress;// 不为空
	private String toAddress;
	private String subject;
	private String vaspid;
	private String vasid;
	private String serviceCode;
	private String linkid;
	private String sendtime;
	private Integer status;
	private String statusText;
	private String reportTime;
	private Integer mmStatus;
	private String mmStatusText;
	//private String readyTime;// MM7_read_reply_report.REQ中Date and time字段
	// 可以为空
	//private Integer readStatus;// MM7_read_reply_report.REQ中Read Status 字段 可以为空
	//private String readStatusText;// MM7_read_reply_report.REQ中Read Status
	// text字段 可以为空
	private Long sessionid;

	// Constructors

	/** default constructor */
	public SubmitBean()
	{
	}

	/** minimal constructor */
	public SubmitBean(String messageid, String transactionid,
			String toAddress, String subject, String vaspid, String vasid,
			String serviceCode, String sendtime, Integer status)
	{
		this.messageid = messageid;
		this.transactionid = transactionid;
		this.toAddress = toAddress;
		this.subject = subject;
		this.vaspid = vaspid;
		this.vasid = vasid;
		this.serviceCode = serviceCode;
		this.sendtime = sendtime;
		this.status = status;
	}

	/** full constructor */
	public SubmitBean(String messageid, String transactionid,
			String mm7version, String toAddress, String subject, String vaspid,
			String vasid, String serviceCode, String linkid, String sendtime,
			Integer status, String statusText, String reportTime,
			Integer mmStatus, String mmStatusText, Long sessionid)
	{
		this.messageid = messageid;
		this.transactionid = transactionid;
		this.mm7version = mm7version;
		this.toAddress = toAddress;
		this.subject = subject;
		this.vaspid = vaspid;
		this.vasid = vasid;
		this.serviceCode = serviceCode;
		this.linkid = linkid;
		this.sendtime = sendtime;
		this.status = status;
		this.statusText = statusText;
		this.reportTime = reportTime;
		this.mmStatus = mmStatus;
		this.mmStatusText = mmStatusText;
	
		this.sessionid = sessionid;
	}

	// Property accessors

	public Long getId()
	{
		return this.id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public String getMessageid()
	{
		return this.messageid;
	}

	public void setMessageid(String messageid)
	{
		this.messageid = messageid;
	}

	public String getTransactionid()
	{
		return this.transactionid;
	}

	public void setTransactionid(String transactionid)
	{
		this.transactionid = transactionid;
	}

	public String getMm7version()
	{
		return this.mm7version;
	}

	public void setMm7version(String mm7version)
	{
		this.mm7version = mm7version;
	}



	public String getToAddress()
	{
		return this.toAddress;
	}

	public void setToAddress(String toAddress)
	{
		this.toAddress = toAddress;
	}

	public String getSubject()
	{
		return this.subject;
	}

	public void setSubject(String subject)
	{
		this.subject = subject;
	}

	public String getVaspid()
	{
		return this.vaspid;
	}

	public void setVaspid(String vaspid)
	{
		this.vaspid = vaspid;
	}

	public String getVasid()
	{
		return this.vasid;
	}

	public void setVasid(String vasid)
	{
		this.vasid = vasid;
	}

	public String getServiceCode()
	{
		return this.serviceCode;
	}

	public void setServiceCode(String serviceCode)
	{
		this.serviceCode = serviceCode;
	}

	public String getLinkid()
	{
		return this.linkid;
	}

	public void setLinkid(String linkid)
	{
		this.linkid = linkid;
	}

	public String getSendtime()
	{
		return this.sendtime;
	}

	public void setSendtime(String sendtime)
	{
		this.sendtime = sendtime;
	}

	public Integer getStatus()
	{
		return this.status;
	}

	public void setStatus(Integer status)
	{
		this.status = status;
	}

	public String getStatusText()
	{
		return this.statusText;
	}

	public void setStatusText(String statusText)
	{
		this.statusText = statusText;
	}

	public String getReportTime()
	{
		return this.reportTime;
	}

	public void setReportTime(String reportTime)
	{
		this.reportTime = reportTime;
	}

	public Integer getMmStatus()
	{
		return this.mmStatus;
	}

	public void setMmStatus(Integer mmStatus)
	{
		this.mmStatus = mmStatus;
	}

	public String getMmStatusText()
	{
		return this.mmStatusText;
	}

	public void setMmStatusText(String mmStatusText)
	{
		this.mmStatusText = mmStatusText;
	}

	
	public Long getSessionid()
	{
		return this.sessionid;
	}

	public void setSessionid(Long sessionid)
	{
		this.sessionid = sessionid;
	}

	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("Messageid:").append(messageid + Constants.NEWLINE);
		sb.append("Transactionid:").append(transactionid).append(
				Constants.NEWLINE);
		sb.append("Mm7version:").append(mm7version).append(Constants.NEWLINE);
		sb.append("ToAddress:").append(toAddress).append(Constants.NEWLINE);
		sb.append("Subject:").append(subject).append(Constants.NEWLINE);
		sb.append("Sendtime:").append(sendtime).append(Constants.NEWLINE);
		sb.append("Vaspid:").append(vaspid).append(Constants.NEWLINE);
		sb.append("Vasid:").append(vasid).append(Constants.NEWLINE);
		sb.append("ServiceCode:").append(serviceCode).append(Constants.NEWLINE);
		sb.append("Linkid:").append(linkid).append(Constants.NEWLINE);
		sb.append("Status:").append(status).append(Constants.NEWLINE);
		sb.append("StatusText:").append(statusText).append(Constants.NEWLINE);
		sb.append("ReportTime:").append(reportTime).append(Constants.NEWLINE);
		sb.append("MmStatus:").append(mmStatus).append(Constants.NEWLINE);
		sb.append("MmStatusText:" + mmStatusText).append(Constants.NEWLINE);
		sb.append("Sessionid:").append(sessionid).append(Constants.NEWLINE);
		return sb.toString();
	}
}
