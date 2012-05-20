package com.frank.ylear.modules.smsreport.entity;

/**
 * SLogsmssubmit entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class SmsSubmitBean implements java.io.Serializable
{

	// Fields

	private Long id;
	private String msgid;
	private Integer pkTotal;
	private Integer pkNumber;
	private String msgSrc;
	private String srcId;
	private String destId;
	private Integer msgFmt;
	private Integer msgLength;
	private String msgContent;
	private String feetype;
	private String feecode;
	private String serviceId;
	private String linkid;
	private String sendtime;
	private Integer resultcode;
	private String resultstr;
	private String submitTime;
	private String doneTime;
	private String stat;
	private Long sessionid;

	// Constructors

	/** default constructor */
	public SmsSubmitBean()
	{
	}

	/** minimal constructor */
	public SmsSubmitBean(String msgid, Integer pkTotal, Integer pkNumber,
			String msgSrc, String srcId, String destId, Integer msgFmt,
			Integer msgLength, String msgContent, String feetype,
			String feecode, String serviceId, String sendtime,
			Integer resultcode, String resultstr)
	{
		this.msgid = msgid;
		this.pkTotal = pkTotal;
		this.pkNumber = pkNumber;
		this.msgSrc = msgSrc;
		this.srcId = srcId;
		this.destId = destId;
		this.msgFmt = msgFmt;
		this.msgLength = msgLength;
		this.msgContent = msgContent;
		this.feetype = feetype;
		this.feecode = feecode;
		this.serviceId = serviceId;
		this.sendtime = sendtime;
		this.resultcode = resultcode;
		this.resultstr = resultstr;
	}

	/** full constructor */
	public SmsSubmitBean(String msgid, Integer pkTotal, Integer pkNumber,
			String msgSrc, String srcId, String destId, Integer msgFmt,
			Integer msgLength, String msgContent, String feetype,
			String feecode, String serviceId, String linkid, String sendtime,
			Integer resultcode, String resultstr, String submitTime,
			String doneTime, String stat, Long sessionid)
	{
		this.msgid = msgid;
		this.pkTotal = pkTotal;
		this.pkNumber = pkNumber;
		this.msgSrc = msgSrc;
		this.srcId = srcId;
		this.destId = destId;
		this.msgFmt = msgFmt;
		this.msgLength = msgLength;
		this.msgContent = msgContent;
		this.feetype = feetype;
		this.feecode = feecode;
		this.serviceId = serviceId;
		this.linkid = linkid;
		this.sendtime = sendtime;
		this.resultcode = resultcode;
		this.resultstr = resultstr;
		this.submitTime = submitTime;
		this.doneTime = doneTime;
		this.stat = stat;
		this.sessionid = sessionid;
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

	public Integer getMsgLength()
	{
		return this.msgLength;
	}

	public void setMsgLength(Integer msgLength)
	{
		this.msgLength = msgLength;
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

	public String getSendtime()
	{
		return this.sendtime;
	}

	public void setSendtime(String sendtime)
	{
		this.sendtime = sendtime;
	}

	public Integer getResultcode()
	{
		return this.resultcode;
	}

	public void setResultcode(Integer resultcode)
	{
		this.resultcode = resultcode;
	}

	public String getResultstr()
	{
		return this.resultstr;
	}

	public void setResultstr(String resultstr)
	{
		this.resultstr = resultstr;
	}

	public String getSubmitTime()
	{
		return this.submitTime;
	}

	public void setSubmitTime(String submitTime)
	{
		this.submitTime = submitTime;
	}

	public String getDoneTime()
	{
		return this.doneTime;
	}

	public void setDoneTime(String doneTime)
	{
		this.doneTime = doneTime;
	}

	public String getStat()
	{
		return this.stat;
	}

	public void setStat(String stat)
	{
		this.stat = stat;
	}

	public Long getSessionid()
	{
		return this.sessionid;
	}

	public void setSessionid(Long sessionid)
	{
		this.sessionid = sessionid;
	}



	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("Msg_id:" + msgid + "\r\n");
		sb.append("Pk_total:" + pkTotal + "\r\n");
		sb.append("Pk_number:" + pkNumber + "\r\n");
		sb.append("Msg_src:" + msgSrc + "\r\n");
		sb.append("Src_id:" + srcId + "\r\n");
		sb.append("Dest_id:" + destId + "\r\n");
		sb.append("Service_id:" + serviceId + "\r\n");
		sb.append("Msg_fmt:" + msgFmt + "\r\n");
		sb.append("Msg_Length:" + msgLength + "\r\n");
		sb.append("Msg_Content:" + msgContent + "\r\n");
		sb.append("Feetype:" + feetype + "\r\n");
		sb.append("Feecode:" + feecode + "\r\n");
		sb.append("Msg_SendTime:" + sendtime + "\r\n");
		sb.append("Linkid:" + linkid + "\r\n");
		sb.append("Resultcode:" + resultcode + "\r\n");
		sb.append("Resultstr:" + resultstr + "\r\n");
		sb.append("Submit_time:" + submitTime + "\r\n");
		sb.append("Done_time:" + doneTime + "\r\n");
		sb.append("Stat:" + stat + "\r\n");
		sb.append("Sessionid:" + sessionid + "\r\n");
		return sb.toString();
	}
}
