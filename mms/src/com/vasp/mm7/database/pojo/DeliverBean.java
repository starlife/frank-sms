package com.vasp.mm7.database.pojo;

import com.vasp.mm7.util.Constants;

/**
 * SLogMmsdeliver entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class DeliverBean implements java.io.Serializable
{

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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
	private String linkid;
	private String recvTime;
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
			String linkedId, String dateTime, String subject, String content)
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
		this.linkid = linkedId;
		this.recvTime = dateTime;
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

	public String getLinkId()
	{
		return this.linkid;
	}

	public void setLinkId(String linkedId)
	{
		this.linkid = linkedId;
	}

	public String getRecvTime()
	{
		return this.recvTime;
	}

	public void setRecvTime(String dateTime)
	{
		this.recvTime = dateTime;
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

	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("Messageid:").append(messageid).append(Constants.NEWLINE);
		sb.append("Transactionid:").append(transactionid).append(
				Constants.NEWLINE);
		sb.append("Messagetype:").append(messagetype).append(Constants.NEWLINE);
		sb.append("Mm7version:").append(mm7version).append(Constants.NEWLINE);
		sb.append("Vaspid:").append(vaspid).append(Constants.NEWLINE);
		sb.append("Vasid:").append(vasid).append(Constants.NEWLINE);
		sb.append("Servicecode:").append(servicecode).append(Constants.NEWLINE);
		sb.append("SendAddress:").append(sendAddress).append(Constants.NEWLINE);
		sb.append("RecipientAddress:").append(recipientAddress).append(
				Constants.NEWLINE);
		sb.append("LinkedId:").append(linkid).append(Constants.NEWLINE);
		sb.append("DateTime:").append(recvTime).append(Constants.NEWLINE);
		sb.append("Subject:").append(subject).append(Constants.NEWLINE);
		sb.append("Content:").append(content).append(Constants.NEWLINE);
		return sb.toString();

	}

}
