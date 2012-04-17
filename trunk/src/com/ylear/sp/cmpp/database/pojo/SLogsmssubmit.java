package com.ylear.sp.cmpp.database.pojo;

import java.util.Date;

/**
 * SLogsmssubmit entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class SLogsmssubmit implements java.io.Serializable
{

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String msgid;
	private Integer pkTotal;
	private Integer pkNumber;
	private String msgSrc;
	private String srcId;
	private String destId;
	private Integer msgFmt;
	private String msgContent;
	private String feetype;
	private String feecode;
	private String serviceId;
	private String linkid;
	private Date sendtime;
	private Date sendresptime;
	private Date tomttime;
	private String errcode;
	private String errmsg;

	// Constructors

	/** default constructor */
	public SLogsmssubmit()
	{
	}

	/** full constructor */
	public SLogsmssubmit(String msgid, Integer pkTotal, Integer pkNumber,
			String msgSrc, String srcId, String destId, Integer msgFmt,
			String msgContent, String feetype, String feecode,
			String serviceId, String linkid, Date sendtime, Date sendresptime,
			Date tomttime, String errcode, String errmsg)
	{
		this.msgid = msgid;
		this.pkTotal = pkTotal;
		this.pkNumber = pkNumber;
		this.msgSrc = msgSrc;
		this.srcId = srcId;
		this.destId = destId;
		this.msgFmt = msgFmt;
		this.msgContent = msgContent;
		this.feetype = feetype;
		this.feecode = feecode;
		this.serviceId = serviceId;
		this.linkid = linkid;
		this.sendtime = sendtime;
		this.sendresptime = sendresptime;
		this.tomttime = tomttime;
		this.errcode = errcode;
		this.errmsg = errmsg;
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

	public Integer getPkTotal()
	{
		return this.pkTotal;
	}

	public void setPkTotal(Integer pkTotal)
	{
		this.pkTotal = pkTotal;
	}

	public Integer getPkNumber()
	{
		return this.pkNumber;
	}

	public void setPkNumber(Integer pkNumber)
	{
		this.pkNumber = pkNumber;
	}

	public String getMsgSrc()
	{
		return this.msgSrc;
	}

	public void setMsgSrc(String msgSrc)
	{
		this.msgSrc = msgSrc;
	}

	public String getSrcId()
	{
		return this.srcId;
	}

	public void setSrcId(String srcId)
	{
		this.srcId = srcId;
	}

	public String getDestId()
	{
		return this.destId;
	}

	public void setDestId(String destId)
	{
		this.destId = destId;
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

	public String getFeetype()
	{
		return this.feetype;
	}

	public void setFeetype(String feetype)
	{
		this.feetype = feetype;
	}

	public String getFeecode()
	{
		return this.feecode;
	}

	public void setFeecode(String feecode)
	{
		this.feecode = feecode;
	}

	public String getServiceId()
	{
		return this.serviceId;
	}

	public void setServiceId(String serviceId)
	{
		this.serviceId = serviceId;
	}

	public String getLinkid()
	{
		return this.linkid;
	}

	public void setLinkid(String linkid)
	{
		this.linkid = linkid;
	}

	public Date getSendtime()
	{
		return this.sendtime;
	}

	public void setSendtime(Date sendtime)
	{
		this.sendtime = sendtime;
	}

	public Date getSendresptime()
	{
		return this.sendresptime;
	}

	public void setSendresptime(Date sendresptime)
	{
		this.sendresptime = sendresptime;
	}

	public Date getTomttime()
	{
		return this.tomttime;
	}

	public void setTomttime(Date tomttime)
	{
		this.tomttime = tomttime;
	}

	public String getErrcode()
	{
		return this.errcode;
	}

	public void setErrcode(String errcode)
	{
		this.errcode = errcode;
	}

	public String getErrmsg()
	{
		return this.errmsg;
	}

	public void setErrmsg(String errmsg)
	{
		this.errmsg = errmsg;
	}

}
