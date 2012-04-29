package com.ylear.sp.cmpp.database.pojo;


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
	private Long id;// 唯一主键
	private String msgid;// 消息id 值唯一
	private Integer pkTotal;// 总消息数，不为空
	private Integer pkNumber;// 当前第几条 不为空
	private String msgSrc;// spid 不为空
	private String srcId;// spnumber 不为空
	private String destId;// 接收手机号码 不为空
	private Integer msgFmt;// 消息格式 不为空
	private Integer msgLength;// 消息长度 不为空
	private String msgContent;// 消息内容 不为空
	private String feetype;// 计费类型 不为空
	private String feecode;// 计费号码 不为空
	private String serviceId;// 业务id 不为空
	private String linkid;// 在CMPP2.0协议中该字段无效，可以为空
	private String sendtime;// CMPP_SUBMIT消息的提交时间 yyyyMMddHHmmss 不为空
	private Integer resultCode;// CMPP_SUBMIT_RESP中的提交结果状态 不为空
	private String resultStr;// CMPP_SUBMIT_RESP中的提交结果状态 不为空
	private String submitTime;// CMPP_REPORT中的Submit_time yyyyMMddHHmm 可以为空
	private String doneTime;// CMPP_REPORT中的done_time yyyyMMddHHmm 可以为空
	private String stat;// CMPP_REPORT中的Stat，可以为空
	private Long sessionid;// 消息的sessionid，可以为空

	// Constructors

	/** default constructor */
	public SubmitBean()
	{
	}

	/** full constructor */
	public SubmitBean(String msgid, Integer pkTotal, Integer pkNumber,
			String msgSrc, String srcId, String destId, Integer msgFmt,
			String msgContent, String feetype, String feecode,
			String serviceId, String linkid, String sendtime,
			String sendresptime, String tomttime, String errcode, String errmsg)
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

	public String getSendtime()
	{
		return this.sendtime;
	}

	public void setSendtime(String sendtime)
	{
		this.sendtime = sendtime;
	}

	public Integer getMsgLength()
	{
		return msgLength;
	}

	public void setMsgLength(Integer msgLength)
	{
		this.msgLength = msgLength;
	}

	public Integer getResultCode()
	{
		return resultCode;
	}

	public void setResultCode(Integer resultCode)
	{
		this.resultCode = resultCode;
	}

	public String getResultStr()
	{
		return resultStr;
	}

	public void setResultStr(String resultStr)
	{
		this.resultStr = resultStr;
	}

	public String getSubmitTime()
	{
		return submitTime;
	}

	public void setSubmitTime(String submitTime)
	{
		this.submitTime = submitTime;
	}

	public String getDoneTime()
	{
		return doneTime;
	}

	public void setDoneTime(String doneTime)
	{
		this.doneTime = doneTime;
	}

	public String getStat()
	{
		return stat;
	}

	public void setStat(String stat)
	{
		this.stat = stat;
	}

	public Long getSessionid()
	{
		return sessionid;
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
		sb.append("ResultCode:" + resultCode + "\r\n");
		sb.append("ResultStr:" + resultStr + "\r\n");
		sb.append("Submit_time:" + submitTime + "\r\n");
		sb.append("Done_time:" + doneTime + "\r\n");
		sb.append("Stat:" + stat + "\r\n");
		sb.append("Sessionid:" + sessionid + "\r\n");
		return sb.toString();
	}


}
