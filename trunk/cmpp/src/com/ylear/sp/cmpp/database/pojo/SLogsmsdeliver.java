package com.ylear.sp.cmpp.database.pojo;

import java.util.Date;

/**
 * SLogsmsdeliver entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class SLogsmsdeliver implements java.io.Serializable
{

	// Fields

	private Long id;
	private String msgid;
	private String destId;
	private String srcId;
	private Integer tpPid;
	private Integer tpUdhi;
	private String serviceId;
	private Integer registeredDelivery;
	private Integer msgFmt;
	private String msgContent;
	private Integer msgLength;
	private Date recvtime;
	private String linkid;

	// Constructors

	/** default constructor */
	public SLogsmsdeliver()
	{
	}

	/** full constructor */
	public SLogsmsdeliver(String msgid, String destId, String srcId,
			Integer tpPid, Integer tpUdhi, String serviceId,
			Integer registeredDelivery, Integer msgFmt, String msgContent,
			Integer msgLength, Date recvtime, String linkid)
	{
		this.msgid = msgid;
		this.destId = destId;
		this.srcId = srcId;
		this.tpPid = tpPid;
		this.tpUdhi = tpUdhi;
		this.serviceId = serviceId;
		this.registeredDelivery = registeredDelivery;
		this.msgFmt = msgFmt;
		this.msgContent = msgContent;
		this.msgLength = msgLength;
		this.recvtime = recvtime;
		this.linkid = linkid;
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

	public String getMsgid()
	{
		return this.msgid;
	}

	public void setMsgid(String msgid)
	{
		this.msgid = msgid;
	}

	public String getDestId()
	{
		return this.destId;
	}

	public void setDestId(String destId)
	{
		this.destId = destId;
	}

	public String getSrcId()
	{
		return this.srcId;
	}

	public void setSrcId(String srcId)
	{
		this.srcId = srcId;
	}

	public Integer getTpPid()
	{
		return this.tpPid;
	}

	public void setTpPid(Integer tpPid)
	{
		this.tpPid = tpPid;
	}

	public Integer getTpUdhi()
	{
		return this.tpUdhi;
	}

	public void setTpUdhi(Integer tpUdhi)
	{
		this.tpUdhi = tpUdhi;
	}

	public String getServiceId()
	{
		return this.serviceId;
	}

	public void setServiceId(String serviceId)
	{
		this.serviceId = serviceId;
	}

	public Integer getRegisteredDelivery()
	{
		return this.registeredDelivery;
	}

	public void setRegisteredDelivery(Integer registeredDelivery)
	{
		this.registeredDelivery = registeredDelivery;
	}

	public Integer getMsgFmt()
	{
		return this.msgFmt;
	}

	public void setMsgFmt(Integer msgFmt)
	{
		this.msgFmt = msgFmt;
	}

	public String getMsgContent()
	{
		return this.msgContent;
	}

	public void setMsgContent(String msgContent)
	{
		this.msgContent = msgContent;
	}

	public Integer getMsgLength()
	{
		return this.msgLength;
	}

	public void setMsgLength(Integer msgLength)
	{
		this.msgLength = msgLength;
	}

	public Date getRecvtime()
	{
		return this.recvtime;
	}

	public void setRecvtime(Date recvtime)
	{
		this.recvtime = recvtime;
	}

	public String getLinkid()
	{
		return this.linkid;
	}

	public void setLinkid(String linkid)
	{
		this.linkid = linkid;
	}

}
