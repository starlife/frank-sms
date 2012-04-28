package com.ylear.sp.cmpp.database.pojo;

import java.util.Date;

/**
 * SLogsmssubmit entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class SubmitBean implements java.io.Serializable
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
	private Integer msgLength;
	private String msgContent;
	private String feetype;
	private String feecode;
	private String serviceId;
	private String linkid;//在CMPP2.0协议中该字段无效
	private Date sendtime;//CMPP_SUBMIT消息的提交时间
	private Integer resultCode;//CMPP_SUBMIT_RESP中的提交结果状态
	private String resultStr;//CMPP_SUBMIT_RESP中的提交结果状态
	private Date submitTime;//CMPP_REPORT中的Submit_time
	private Date doneTime;//CMPP_REPORT中的done_time
	private String stat;//CMPP_REPORT中的Stat

	// Constructors

	/** default constructor */
	public SubmitBean()
	{
	}

	/** full constructor */
	public SubmitBean(String msgid, Integer pkTotal, Integer pkNumber,
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

	
	public  String  toString()
	{
		StringBuffer sb=new StringBuffer();
		sb.append("Msg_id:"+msgid+"\r\n");
		sb.append("Pk_total:"+pkTotal+"\r\n");
		sb.append("Pk_number:"+pkNumber+"\r\n");
		sb.append("Msg_src:"+msgSrc+"\r\n");
		sb.append("Src_id:"+srcId+"\r\n");
		sb.append("Dest_id:"+destId+"\r\n");
		sb.append("Service_id:"+serviceId+"\r\n");
		sb.append("Msg_fmt:"+msgFmt+"\r\n");
		sb.append("Msg_Length:"+msgLength+"\r\n");
		sb.append("Msg_Content:"+msgContent+"\r\n");
		sb.append("Feetype:"+feetype+"\r\n");
		sb.append("Feecode:"+feecode+"\r\n");
		sb.append("Msg_SendTime:"+sendtime+"\r\n");
		sb.append("Linkid:"+linkid+"\r\n");
		sb.append("ResultCode:"+resultCode+"\r\n");
		sb.append("ResultStr:"+resultStr+"\r\n");
		sb.append("Submit_time:"+submitTime+"\r\n");
		sb.append("Done_time:"+doneTime+"\r\n");
		sb.append("Stat:"+stat+"\r\n");
		sb.append("Submit_time");
		return sb.toString();
	}

}
