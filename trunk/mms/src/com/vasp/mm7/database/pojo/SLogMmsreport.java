package com.vasp.mm7.database.pojo;

import java.util.Date;

/**
 * SLogMmsreport entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class SLogMmsreport implements java.io.Serializable
{

	// Fields

	private Long id;
	private String messageid;
	private String trasactionid;
	private String messagetype;
	private String mm7version;
	private String senderAddress;
	private String recipientAddress;
	private Date dateTime;
	private Integer mmstatus;
	private String mmstatustext;

	// Constructors

	/** default constructor */
	public SLogMmsreport()
	{
	}

	/** minimal constructor */
	public SLogMmsreport(String messageid, String trasactionid,
			Integer mmstatus, String mmstatustext)
	{
		this.messageid = messageid;
		this.trasactionid = trasactionid;
		this.mmstatus = mmstatus;
		this.mmstatustext = mmstatustext;
	}

	/** full constructor */
	public SLogMmsreport(String messageid, String trasactionid,
			String messagetype, String mm7version, String senderAddress,
			String recipientAddress, Date dateTime, Integer mmstatus,
			String mmstatustext)
	{
		this.messageid = messageid;
		this.trasactionid = trasactionid;
		this.messagetype = messagetype;
		this.mm7version = mm7version;
		this.senderAddress = senderAddress;
		this.recipientAddress = recipientAddress;
		this.dateTime = dateTime;
		this.mmstatus = mmstatus;
		this.mmstatustext = mmstatustext;
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
		sb.append("MMStatus=" + mmstatus + "\r\n");
		sb.append("StatusText=" + mmstatustext + "\r\n");
		return sb.toString();

	}

}
