package com.vasp.mm7.database.pojo;

import java.util.Date;

/**
 * SLogMmsreadreply entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class SLogMmsreadreply implements java.io.Serializable
{

	// Fields

	private Long id;
	private String messageid;
	private String trasactionid;
	private String mm7version;
	private String senderAddress;
	private String recipientAddress;
	private Date dateTime;
	private Integer readstatus;
	private String readstatustext;
	// Constructors

	/** default constructor */
	public SLogMmsreadreply()
	{
	}

	/** minimal constructor */
	public SLogMmsreadreply(String messageid, String trasactionid,
			Integer readstatus, String readstatustext)
	{
		this.messageid = messageid;
		this.trasactionid = trasactionid;
		this.readstatus = readstatus;
		this.readstatustext = readstatustext;
	}

	/** full constructor */
	public SLogMmsreadreply(String messageid, String trasactionid,
			String messagetype, String mm7version, String senderAddress,
			String recipientAddress, Date dateTime, Integer readstatus,
			String readstatustext)
	{
		this.messageid = messageid;
		this.trasactionid = trasactionid;
		this.mm7version = mm7version;
		this.senderAddress = senderAddress;
		this.recipientAddress = recipientAddress;
		this.dateTime = dateTime;
		this.readstatus = readstatus;
		this.readstatustext = readstatustext;
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

	public String getTrasactionid()
	{
		return this.trasactionid;
	}

	public void setTrasactionid(String trasactionid)
	{
		this.trasactionid = trasactionid;
	}

	public String getMm7version()
	{
		return this.mm7version;
	}

	public void setMm7version(String mm7version)
	{
		this.mm7version = mm7version;
	}

	public String getSenderAddress()
	{
		return this.senderAddress;
	}

	public void setSenderAddress(String senderAddress)
	{
		this.senderAddress = senderAddress;
	}

	public String getRecipientAddress()
	{
		return this.recipientAddress;
	}

	public void setRecipientAddress(String recipientAddress)
	{
		this.recipientAddress = recipientAddress;
	}

	public Date getDateTime()
	{
		return this.dateTime;
	}

	public void setDateTime(Date dateTime)
	{
		this.dateTime = dateTime;
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

	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("------------------ReadReply--------------------\r\n");
		sb.append("MessageID=" + messageid + "\r\n");
		sb.append("trasactionid=" + trasactionid + "\r\n");
		sb.append("mm7version=" + mm7version + "\r\n");
		sb.append("Recipient=" + recipientAddress + "\r\n");
		sb.append("Sender=" + senderAddress + "\r\n");
		sb.append("TimeStamp=" + dateTime + "\r\n");
		sb.append("MMStatus=" + readstatus + "\r\n");
		sb.append("StatusText=" + readstatustext + "\r\n");
		return sb.toString();
	}

	

}
