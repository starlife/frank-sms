package com.unicom.mm7.bean;

/**
 * UMms entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class UMms implements java.io.Serializable
{

	// Fields

	private static final long serialVersionUID = 1L;
	// private Long id;
	private MmsFile mmsFile;
	private String sendID;
	private String subject;
	private String recipient;
	private String sendtime;

	// Constructors

	/** default constructor */
	public UMms()
	{
	}

	/** minimal constructor */
	public UMms(String subject, String recipient, String sendtime)
	{
		this.subject = subject;
		this.recipient = recipient;
		this.sendtime = sendtime;
		// this.status = status;
	}

	/** full constructor */
	public UMms(MmsFile mmsFile, String subject, String recipient,
			String sendtime)
	{
		this.mmsFile = mmsFile;
		this.subject = subject;
		this.recipient = recipient;
		this.sendtime = sendtime;

	}

	public String getSubject()
	{
		return this.subject;
	}

	public void setSubject(String subject)
	{
		this.subject = subject;
	}

	public String getRecipient()
	{
		return this.recipient;
	}

	public void setRecipient(String recipient)
	{
		this.recipient = recipient;
	}

	public String getSendtime()
	{
		return this.sendtime;
	}

	public void setSendtime(String sendtime)
	{
		this.sendtime = sendtime;
	}

	public MmsFile getMmsFile()
	{
		return mmsFile;
	}

	public void setMmsFile(MmsFile mmsFile)
	{
		this.mmsFile = mmsFile;
	}

	public String getSendID()
	{
		return sendID;
	}

	public void setSendID(String sendID)
	{
		this.sendID = sendID;
	}

}
