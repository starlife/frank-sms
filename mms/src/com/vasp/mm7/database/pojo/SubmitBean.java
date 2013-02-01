package com.vasp.mm7.database.pojo;

import com.vasp.mm7.util.Constants;

/**
 * SLogMmssubmit entity.
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
	private String messageid;// MM7_submit.RES中messageid 字段 不为空
	private String transactionid;// 不为空
	private String mm7version;// 可以为空
	//private String senderAddress;// 不为空
	private String toAddress;// 不为空
	private String subject;// 不为空
	private String sendtime;// 不为空
	private String vaspid;// 不为空
	private String vasid;// 不为空
	private String serviceCode;// 不为空
	private String linkid;// 可以为空
	private Integer status;// MM7_submit.RES中Request Status 字段 不为空
	private String statusText;// MM7_submit.RES中Request Status Text字段 可以为空
	private String reportTime;// MM7_delivery_report.REQ中Date and time 字段 可以为空
	private Integer mmStatus;// MM7_delivery_report.REQ中MM Status 字段 可以为空
	private String mmStatusText;// MM7_delivery_report.REQ中MM Status text字段 可以为空
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

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public String getMessageid()
	{
		return messageid;
	}

	public void setMessageid(String messageid)
	{
		this.messageid = messageid;
	}

	public String getTransactionid()
	{
		return transactionid;
	}

	public void setTransactionid(String transactionid)
	{
		this.transactionid = transactionid;
	}

	public String getMm7version()
	{
		return mm7version;
	}

	public void setMm7version(String mm7version)
	{
		this.mm7version = mm7version;
	}



	public String getToAddress()
	{
		return toAddress;
	}

	public void setToAddress(String toAddress)
	{
		this.toAddress = toAddress;
	}

	public String getSubject()
	{
		return subject;
	}

	public void setSubject(String subject)
	{
		this.subject = subject;
	}


	public String getVaspid()
	{
		return vaspid;
	}

	public void setVaspid(String vaspid)
	{
		this.vaspid = vaspid;
	}

	public String getVasid()
	{
		return vasid;
	}

	public void setVasid(String vasid)
	{
		this.vasid = vasid;
	}

	public String getServiceCode()
	{
		return serviceCode;
	}

	public void setServiceCode(String serviceCode)
	{
		this.serviceCode = serviceCode;
	}

	public String getLinkid()
	{
		return linkid;
	}

	public void setLinkid(String linkid)
	{
		this.linkid = linkid;
	}

	public String getSendtime()
	{
		return sendtime;
	}

	public void setSendtime(String sendtime)
	{
		this.sendtime = sendtime;
	}
	public Integer getStatus()
	{
		return status;
	}

	public void setStatus(Integer status)
	{
		this.status = status;
	}

	public String getStatusText()
	{
		return statusText;
	}

	public void setStatusText(String statusText)
	{
		this.statusText = statusText;
	}

	public String getReportTime()
	{
		return reportTime;
	}

	public void setReportTime(String reportTime)
	{
		this.reportTime = reportTime;
	}

	public Integer getMmStatus()
	{
		return mmStatus;
	}

	public void setMmStatus(Integer mmStatus)
	{
		this.mmStatus = mmStatus;
	}

	public String getMmStatusText()
	{
		return mmStatusText;
	}

	public void setMmStatusText(String mmStatusText)
	{
		this.mmStatusText = mmStatusText;
	}

	
	public Long getSessionid()
	{
		return sessionid;
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
