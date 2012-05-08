package com.frank.ylear.modules.sms.entity;

import java.util.Date;


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
	private Long id;
	private String msgContent;
	private String recipient;
	private Date sendtime;
	private Integer status;

	// Constructors

	//add fields
	private String beginTime;//查询条件 开始时间
	private String endTime;//查询条件  结束时间
	private String sendtimeStr;
	// Constructors

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

	/** default constructor */
	public USms()
	{
	}

	/** full constructor */
	public USms(String msgContent, String recipient, Date sendtime,
			Integer status)
	{
		this.msgContent = msgContent;
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

	public Date getSendtime()
	{
		return this.sendtime;
	}

	public void setSendtime(Date sendtime)
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

	public String getSendtimeStr()
	{
		return sendtimeStr;
	}

	public void setSendtimeStr(String sendtimeStr)
	{
		this.sendtimeStr = sendtimeStr;
	}

}
