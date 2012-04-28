package com.ylear.sp.cmpp.database.pojo;

import java.util.Date;

/**
 * DeliverBean
 * 
 * @author frank
 */

public class DeliverBean implements java.io.Serializable
{

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String msgid;
	private String destId;
	private String srcId;
	private Integer tpPid;
	private Integer tpUdhi;
	private String serviceId;
	private Integer msgFmt;
	private String msgContent;
	private Integer msgLength;
	private Date recvtime;
	private String linkid;

	// Constructors

	/** default constructor */
	public DeliverBean()
	{
	}

	/** full constructor */
	public DeliverBean(String msgid, String destId, String srcId,
			Integer tpPid, Integer tpUdhi, String serviceId,Integer msgFmt, String msgContent,
			Integer msgLength, Date recvtime, String linkid)
	{
		this.msgid = msgid;
		this.destId = destId;
		this.srcId = srcId;
		this.tpPid = tpPid;
		this.tpUdhi = tpUdhi;
		this.serviceId = serviceId;
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
	public String toString()
	{
		StringBuffer sb=new StringBuffer();
		sb.append("Msg_id:"+msgid+"\r\n");
		sb.append("Dest_id:"+destId+"\r\n");
		sb.append("Src_id:"+srcId+"\r\n");
		sb.append("Tp_pid:"+tpPid+"\r\n");
		sb.append("Tp_udhi:"+tpUdhi+"\r\n");
		sb.append("Service_id:"+serviceId+"\r\n");
		sb.append("Msg_fmt:"+msgFmt+"\r\n");
		sb.append("Msg_Length:"+msgLength+"\r\n");
		sb.append("Msg_Content:"+msgContent+"\r\n");
		sb.append("Msg_RecvTime:"+recvtime+"\r\n");
		sb.append("Linkid:"+linkid+"\r\n");
		return sb.toString();
	}

}
