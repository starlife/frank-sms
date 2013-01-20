package com.ylear.sp.sgip.bean;

/**
 * USms entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class USms implements java.io.Serializable
{

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String sendid;;
	private String msgContent;
	private String recipient;

	// Constructors

	/** default constructor */
	public USms()
	{
	}

	/** full constructor */
	public USms(String sendid, String msgContent, String recipient)
	{
		this.sendid = sendid;
		this.msgContent = msgContent;
		this.recipient = recipient;
	}

	public String getMsgContent()
	{
		return this.msgContent;
	}

	public void setMsgContent(String msgContent)
	{
		this.msgContent = msgContent;
	}

	public String getRecipient()
	{
		return this.recipient;
	}

	public void setRecipient(String recipient)
	{
		this.recipient = recipient;
	}

	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("SendId:" + sendid + "\r\n");
		sb.append("Msg_Content:" + msgContent + "\r\n");
		sb.append("Recipient:" + recipient + "\r\n");
		return sb.toString();
	}

	public String getSendid()
	{
		return sendid;
	}

	public void setSendid(String sendId)
	{
		this.sendid = sendId;
	}

}
