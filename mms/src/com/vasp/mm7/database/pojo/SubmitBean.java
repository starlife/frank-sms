package com.vasp.mm7.database.pojo;


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
	private String messageid;//MM7_submit.RES��messageid �ֶ� ��Ϊ��
	private String transactionid;//��Ϊ��
	private String mm7version;//����Ϊ��
	private String senderAddress;//��Ϊ��
	private String toAddress;//��Ϊ��
	private String subject;//��Ϊ��
	private String sendtime;//��Ϊ��
	private String vaspid;//��Ϊ��
	private String vasid;//��Ϊ��
	private String serviceCode;//��Ϊ��
	private String linkid;//����Ϊ��
	private Integer status;//MM7_submit.RES��Request Status �ֶ� ��Ϊ��
	private String statusText;//MM7_submit.RES��Request Status Text�ֶ� ����Ϊ��
	private String reportTime;//MM7_delivery_report.REQ��Date and time �ֶ� ����Ϊ��
	private Integer mmStatus;//MM7_delivery_report.REQ��MM Status �ֶ� ����Ϊ��
	private String mmStatusText;//MM7_delivery_report.REQ��MM Status text�ֶ� ����Ϊ��
	private String readyTime;//MM7_read_reply_report.REQ��Date and time�ֶ� ����Ϊ��
	private Integer readStatus;//MM7_read_reply_report.REQ��Read Status �ֶ� ����Ϊ��
	private String readStatusText;//MM7_read_reply_report.REQ��Read Status text�ֶ�  ����Ϊ��
	private Long sessionid;

	// Constructors

	/** default constructor */
	public SubmitBean()
	{
	}

	/** minimal constructor */
	public SubmitBean(String messageid, String transactionid,
			String mm7version, String toAddress, String subject, String vaspid,
			String vasid, String servicecode, Integer statuscode,
			String statustext, Integer isdeliverreport, Integer isreadreply)
	{
		this.messageid = messageid;
		this.transactionid = transactionid;
		this.mm7version = mm7version;
		this.toAddress = toAddress;
		this.subject = subject;
		this.vaspid = vaspid;
		this.vasid = vasid;
		this.serviceCode = servicecode;
		this.status = statuscode;
		this.statusText = statustext;
	}

	/** full constructor */
	public SubmitBean(String messageid, String transactionid,
			String mm7version, String toAddress, String ccAddress,
			String bccAddress, String subject, String sendtime, String vaspid,
			String vasid, String servicecode, String linkedid,
			Integer statuscode, String statustext, Integer isdeliverreport,
			Integer isreadreply, String deliverreportTime, Integer mmstatus,
			String mmstatustext, String readyreplyTime, Integer readstatus,
			String readstatustext, Long ummsid)
	{
		this.messageid = messageid;
		this.transactionid = transactionid;
		this.mm7version = mm7version;
		this.toAddress = toAddress;
		this.subject = subject;
		this.sendtime = sendtime;
		this.vaspid = vaspid;
		this.vasid = vasid;
		this.serviceCode = servicecode;
		this.linkid = linkedid;
		this.status = statuscode;
		this.statusText = statustext;
		this.reportTime = deliverreportTime;
		this.mmStatus = mmstatus;
		this.mmStatusText = mmstatustext;
		this.readyTime = readyreplyTime;
		this.readStatus = readstatus;
		this.readStatusText = readstatustext;
		this.sessionid = ummsid;
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

	public String getSenderAddress()
	{
		return senderAddress;
	}

	public void setSenderAddress(String senderAddress)
	{
		this.senderAddress = senderAddress;
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

	public String getSendtime()
	{
		return sendtime;
	}

	public void setSendtime(String sendtime)
	{
		this.sendtime = sendtime;
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

	public String getReadyTime()
	{
		return readyTime;
	}

	public void setReadyTime(String readyTime)
	{
		this.readyTime = readyTime;
	}

	public Integer getReadStatus()
	{
		return readStatus;
	}

	public void setReadStatus(Integer readStatus)
	{
		this.readStatus = readStatus;
	}

	public String getReadStatusText()
	{
		return readStatusText;
	}

	public void setReadStatusText(String readStatusText)
	{
		this.readStatusText = readStatusText;
	}

	public Long getSessionid()
	{
		return sessionid;
	}

	public void setSessionid(Long sessionid)
	{
		this.sessionid = sessionid;
	}



}
