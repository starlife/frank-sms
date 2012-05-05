package com.vasp.mm7.database.pojo;

import java.util.Date;

/**
 * SLogMmsdeliver entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class DeliverBean implements java.io.Serializable
{

	// Fields

	private Long id;
	private String transactionid;
	private String messageid;
	private String messagetype;
	private String mm7version;
	private String vaspid;
	private String vasid;
	private String sendAddress;
	private String recipientAddress;
	private String servicecode;
	private String linkedId;
	private Date dateTime;
	private String subject;
	private String content;

	// Constructors

	/** default constructor */
	public DeliverBean()
	{
	}

	/** full constructor */
	public DeliverBean(String transactionid, String messageid,
			String messagetype, String mm7version, String vaspid, String vasid,
			String sendAddress, String recipientAddress, String servicecode,
			String linkedId, Date dateTime, String subject, String content)
	{
		this.transactionid = transactionid;
		this.messageid = messageid;
		this.messagetype = messagetype;
		this.mm7version = mm7version;
		this.vaspid = vaspid;
		this.vasid = vasid;
		this.sendAddress = sendAddress;
		this.recipientAddress = recipientAddress;
		this.servicecode = servicecode;
		this.linkedId = linkedId;
		this.dateTime = dateTime;
		this.subject = subject;
		this.content = content;
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

	public String getTransactionid()
	{
		return this.transactionid;
	}

	public void setTransactionid(String transactionid)
	{
		this.transactionid = transactionid;
	}

	public String getMessageid()
	{
		return this.messageid;
	}

	public void setMessageid(String messageid)
	{
		this.messageid = messageid;
	}

	public String getMessagetype()
	{
		return this.messagetype;
	}

	public void setMessagetype(String messagetype)
	{
		this.messagetype = messagetype;
	}

	public String getMm7version()
	{
		return this.mm7version;
	}

	public void setMm7version(String mm7version)
	{
		this.mm7version = mm7version;
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

	public String getSendAddress()
	{
		return this.sendAddress;
	}

	public void setSendAddress(String sendAddress)
	{
		this.sendAddress = sendAddress;
	}

	public String getRecipientAddress()
	{
		return this.recipientAddress;
	}

	public void setRecipientAddress(String recipientAddress)
	{
		this.recipientAddress = recipientAddress;
	}

	public String getServicecode()
	{
		return this.servicecode;
	}

	public void setServicecode(String servicecode)
	{
		this.servicecode = servicecode;
	}

	public String getLinkedId()
	{
		return this.linkedId;
	}

	public void setLinkedId(String linkedId)
	{
		this.linkedId = linkedId;
	}

	public Date getDateTime()
	{
		return this.dateTime;
	}

	public void setDateTime(Date dateTime)
	{
		this.dateTime = dateTime;
	}

	public String getSubject()
	{
		return this.subject;
	}

	public void setSubject(String subject)
	{
		this.subject = subject;
	}

	public String getContent()
	{
		return this.content;
	}

	public void setContent(String content)
	{
		this.content = content;
	}

}
