package com.frank.ylear.modules.mms.entity;

/**
 * UMms entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class UMms implements java.io.Serializable
{

	// Fields

	private static final long serialVersionUID = 1L;
	private Long id;
	private MmsFile mmsFile;
	private String subject;
	private String recipient;
	private String sendtime;
	private Integer status;

	// 自定义类型

	private String beginTime;
	private String endTime;

	// Constructors

	/** default constructor */
	public UMms()
	{
	}

	/** minimal constructor */
	public UMms(String subject, String recipient, String sendtime,
			Integer status)
	{
		this.subject = subject;
		this.recipient = recipient;
		this.sendtime = sendtime;
		this.status = status;
	}

	/** full constructor */
	public UMms(MmsFile mmsFile, String subject, String recipient,
			String sendtime, Integer status)
	{
		this.mmsFile = mmsFile;
		this.subject = subject;
		this.recipient = recipient;
		this.sendtime = sendtime;
		this.status = status;
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

	public MmsFile getMmsFile()
	{
		return this.mmsFile;
	}

	public void setMmsFile(MmsFile mmsFile)
	{
		this.mmsFile = mmsFile;
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

	public Integer getStatus()
	{
		return this.status;
	}

	public void setStatus(Integer status)
	{
		this.status = status;
	}

	public String getBeginTime()
	{
		return beginTime;
	}

	public void setBeginTime(String beginTime)
	{
		this.beginTime = beginTime;
	}

	public String getEndTime()
	{
		return endTime;
	}

	public void setEndTime(String endTime)
	{
		this.endTime = endTime;
	}

}
