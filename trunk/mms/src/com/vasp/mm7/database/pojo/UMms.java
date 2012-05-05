package com.vasp.mm7.database.pojo;

/**
 * UMms entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class UMms implements java.io.Serializable
{

	// Fields

	private Long id;
	private Long mmsid;
	private String subject;
	private String recipient;
	private Float mmsSize;
	private Integer frames;
	private String sendtime;
	private Integer status;

	// Constructors

	/** default constructor */
	public UMms()
	{
	}

	/** full constructor */
	public UMms(Long mmsid, String subject, String recipient, Float mmsSize,
			Integer frames, String sendtime, Integer status)
	{
		this.mmsid = mmsid;
		this.subject = subject;
		this.recipient = recipient;
		this.mmsSize = mmsSize;
		this.frames = frames;
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

	public Long getMmsid()
	{
		return this.mmsid;
	}

	public void setMmsid(Long mmsid)
	{
		this.mmsid = mmsid;
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

	public Float getMmsSize()
	{
		return this.mmsSize;
	}

	public void setMmsSize(Float mmsSize)
	{
		this.mmsSize = mmsSize;
	}

	public Integer getFrames()
	{
		return this.frames;
	}

	public void setFrames(Integer frames)
	{
		this.frames = frames;
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

}
