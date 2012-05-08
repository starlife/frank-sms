package com.frank.ylear.modules.mms.entity;

import java.util.Date;

/**
 * SLogMmssubmit entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class SLogMmssubmit implements java.io.Serializable
{

	// Fields

	private Long id;
	private String messageid;
	private String transactionid;
	private String mm7version;
	private String toAddress;
	private String ccAddress;
	private String bccAddress;
	private String subject;
	private Date sendtime;
	private String vaspid;
	private String vasid;
	private String servicecode;
	private String linkedid;
	private Integer statuscode;
	private String statustext;
	private Integer isdeliverreport;
	private Integer isreadreply;
	private Date deliverreportTime;
	private Integer mmstatus;
	private String mmstatustext;
	private Date readyreplyTime;
	private Integer readstatus;
	private String readstatustext;
	private Long ummsid;

	// Constructors

	/** default constructor */
	public SLogMmssubmit()
	{
	}

	/** minimal constructor */
	public SLogMmssubmit(String messageid, String transactionid,
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
		this.servicecode = servicecode;
		this.statuscode = statuscode;
		this.statustext = statustext;
		this.isdeliverreport = isdeliverreport;
		this.isreadreply = isreadreply;
	}

	/** full constructor */
	public SLogMmssubmit(String messageid, String transactionid,
			String mm7version, String toAddress, String ccAddress,
			String bccAddress, String subject, Date sendtime, String vaspid,
			String vasid, String servicecode, String linkedid,
			Integer statuscode, String statustext, Integer isdeliverreport,
			Integer isreadreply, Date deliverreportTime, Integer mmstatus,
			String mmstatustext, Date readyreplyTime, Integer readstatus,
			String readstatustext, Long ummsid)
	{
		this.messageid = messageid;
		this.transactionid = transactionid;
		this.mm7version = mm7version;
		this.toAddress = toAddress;
		this.ccAddress = ccAddress;
		this.bccAddress = bccAddress;
		this.subject = subject;
		this.sendtime = sendtime;
		this.vaspid = vaspid;
		this.vasid = vasid;
		this.servicecode = servicecode;
		this.linkedid = linkedid;
		this.statuscode = statuscode;
		this.statustext = statustext;
		this.isdeliverreport = isdeliverreport;
		this.isreadreply = isreadreply;
		this.deliverreportTime = deliverreportTime;
		this.mmstatus = mmstatus;
		this.mmstatustext = mmstatustext;
		this.readyreplyTime = readyreplyTime;
		this.readstatus = readstatus;
		this.readstatustext = readstatustext;
		this.ummsid = ummsid;
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

	public String getCcAddress()
	{
		return this.ccAddress;
	}

	public void setCcAddress(String ccAddress)
	{
		this.ccAddress = ccAddress;
	}

	public String getBccAddress()
	{
		return this.bccAddress;
	}

	public void setBccAddress(String bccAddress)
	{
		this.bccAddress = bccAddress;
	}

	public String getSubject()
	{
		return this.subject;
	}

	public void setSubject(String subject)
	{
		this.subject = subject;
	}

	public Date getSendtime()
	{
		return this.sendtime;
	}

	public void setSendtime(Date sendtime)
	{
		this.sendtime = sendtime;
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

	public String getServicecode()
	{
		return this.servicecode;
	}

	public void setServicecode(String servicecode)
	{
		this.servicecode = servicecode;
	}

	public String getLinkedid()
	{
		return this.linkedid;
	}

	public void setLinkedid(String linkedid)
	{
		this.linkedid = linkedid;
	}

	public Integer getStatuscode()
	{
		return this.statuscode;
	}

	public void setStatuscode(Integer statuscode)
	{
		this.statuscode = statuscode;
	}

	public String getStatustext()
	{
		return this.statustext;
	}

	public void setStatustext(String statustext)
	{
		this.statustext = statustext;
	}

	public Integer getIsdeliverreport()
	{
		return this.isdeliverreport;
	}

	public void setIsdeliverreport(Integer isdeliverreport)
	{
		this.isdeliverreport = isdeliverreport;
	}

	public Integer getIsreadreply()
	{
		return this.isreadreply;
	}

	public void setIsreadreply(Integer isreadreply)
	{
		this.isreadreply = isreadreply;
	}

	public Date getDeliverreportTime()
	{
		return this.deliverreportTime;
	}

	public void setDeliverreportTime(Date deliverreportTime)
	{
		this.deliverreportTime = deliverreportTime;
	}

	public Integer getMmstatus()
	{
		return this.mmstatus;
	}

	public void setMmstatus(Integer mmstatus)
	{
		this.mmstatus = mmstatus;
	}

	public String getMmstatustext()
	{
		return this.mmstatustext;
	}

	public void setMmstatustext(String mmstatustext)
	{
		this.mmstatustext = mmstatustext;
	}

	public Date getReadyreplyTime()
	{
		return this.readyreplyTime;
	}

	public void setReadyreplyTime(Date readyreplyTime)
	{
		this.readyreplyTime = readyreplyTime;
	}

	public Integer getReadstatus()
	{
		return this.readstatus;
	}

	public void setReadstatus(Integer readstatus)
	{
		this.readstatus = readstatus;
	}

	public String getReadstatustext()
	{
		return this.readstatustext;
	}

	public void setReadstatustext(String readstatustext)
	{
		this.readstatustext = readstatustext;
	}

	public Long getUmmsid()
	{
		return this.ummsid;
	}

	public void setUmmsid(Long ummsid)
	{
		this.ummsid = ummsid;
	}

}
