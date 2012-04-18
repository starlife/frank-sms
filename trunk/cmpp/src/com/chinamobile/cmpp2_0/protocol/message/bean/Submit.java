package com.chinamobile.cmpp2_0.protocol.message.bean;

import java.io.UnsupportedEncodingException;
import java.util.regex.*;

/**
 * @author Administrator
 */
public class Submit
{

	/**
	 * 信息标识 SubmitMessage为空，SubmitRespMessage中包含该值 String(8)
	 */
	private String msgID = "";
	/**
	 * 相同Msg_Id的信息总条数，从1开始 int
	 */
	private int pkTotal = 1;
	/**
	 * 相同Msg_Id的信息序号，从1开始。 int
	 */
	private int pkNumber = 1;
	/**
	 * 是否要求返回状态报告，枚举： 1 返回 0 不放回
	 */
	private int registeredDelivery = 1;
	/**
	 * 信息级别 默认为3
	 */
	private int msgLevel = 3;
	/**
	 * 业务标识，是数字、字母和符号的组合 String(10)
	 */
	private String serviceID = "";
	/**
	 * 计费类型 0表示对目的终端计费；1表示对源终端计费 2对sp计费 3表示本字段无效(对谁计费参见FeeTerminalId)
	 * 默认为2（对sp端计费）
	 */
	private int feeUserType = 2;

	/**
	 * 计费号码
	 */
	private String feeTermID = "";

	/**
	 * TP-Protocol-identifier 默认为0
	 */
	private int tpPid = 0;
	/**
	 * TP-User-Data—Header-Indicator 枚举： 0
	 * 表示消息内容（TP-UD）不包含头信息(消息内容可包含7bit,8bit,16bit(ucs2)) 1 表示消息内容包含消息头
	 * 通常用于发送二进制短信（长短信，wappush）
	 */
	private int tpUdhi = 0;
	/**
	 * 短信内容编码 0=ACSII编码 3=短消息写卡操作 4=二进制 8=UCS2 15=GB18030
	 */
	private int msgFmt = 15;
	/**
	 * 消息来源，填spid String(6)
	 */
	private String msgSrc = "";
	/**
	 * 计费类型：01 对计费用户号码免费 02 对计费用户号码按条记信息费 03 对计费用户号码按月收取费用 String(2)
	 */
	private String feeType = "01";
	/**
	 * 资费代码 （单位分）如 5元包月这里写"500" String(6)
	 */
	private String feeCode = "0";
	/**
	 * 短信有效时间，转发过程中保持不变 String(17)
	 */
	private String validTime = "";
	/**
	 * 短信定时发送时间，转发过程中保持不变 String(17)
	 */
	private String atTime = "";
	/**
	 * 源号码（这里填接入号spnumber） String(21)
	 */
	private String srcTermID = "";
	/**
	 * 目的号码个数
	 */
	private int destTermCount = 1;
	/**
	 * 目的号码 String(DestTermCount*21)
	 */
	private String[] destTermID;

	/**
	 * 消息长度（短信内容的字节数）
	 */
	private int msgLength;
	/**
	 * 消息内容
	 */
	private byte[] msgContent;
	/**
	 * 点播业务使用的LinkID，非点播类业务的MT流程不使用该字段。
	 */
	// public String linkID="";
	private String reserve = "";

	public Submit(String spid, String spnumber, String serviceCode,
			String[] desttermid, byte[] msgcontent, String param)
	{
		this.msgSrc = spid;
		this.srcTermID = spnumber;
		this.serviceID = serviceCode;
		this.destTermID = desttermid;
		this.destTermCount = desttermid.length;
		this.msgContent = msgcontent;
		this.msgLength = msgcontent.length;
		this.setParams(param);

	}

	/**
	 * 根据键值取相应数据，例如，getValue("srvcode")如果，有返回该值，否则返回空
	 * 
	 * @param param
	 * @return
	 */
	private String getValue(String param, String key)
	{
		String pattern = "(" + key + "[ ]*=[ ]*)(.*)(\r\n)";
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(param);
		if (m.find())
		{
			return m.group(2);
		}
		else
		{
			return null;
		}
	}

	public String getMsg()
	{
		String msg;
		try
		{
			if (this.msgFmt == 15)
			{
				msg = new String(this.msgContent, "GB18030");
			}
			else if (this.msgFmt == 4)
			{
				msg = new String(this.msgContent, "UTF-16BE");
			}
			else if (this.msgFmt == 8)
			{
				msg = new String(this.msgContent, "UTF-16BE");
			}
			else if (this.msgFmt == 0)
			{
				msg = new String(this.msgContent, "iso-8859-1");
			}
			else
			{
				msg = new String(this.msgContent, "UTF-16BE");
			}
		}
		catch (UnsupportedEncodingException ex)
		{
			msg = new String(this.msgContent);
		}
		return msg;
	}

	public void setParams(String param)
	{
		String value = "";

		if ((value = getValue(param, "Pk_total")) != null)
		{
			try
			{
				pkTotal = Integer.parseInt(value);
			}
			catch (Exception e)
			{
			}
		}
		if ((value = getValue(param, "Pk_number")) != null)
		{
			try
			{
				pkNumber = Integer.parseInt(value);
			}
			catch (Exception e)
			{
			}
		}
		if ((value = getValue(param, "Registered_Delivery")) != null)
		{
			try
			{
				this.registeredDelivery = Integer.parseInt(value);
			}
			catch (Exception e)
			{
			}
		}
		if ((value = getValue(param, "Msg_level")) != null)
		{
			try
			{
				this.msgLevel = Integer.parseInt(value);
			}
			catch (Exception e)
			{
			}
		}

		if ((value = getValue(param, "Fee_UserType")) != null)
		{
			try
			{
				this.feeUserType = Integer.parseInt(value);
			}
			catch (Exception e)
			{
			}
		}

		if ((value = getValue(param, "Fee_terminal_Id")) != null)
		{
			this.feeTermID = value;
		}

		if ((value = getValue(param, "TP_pid")) != null)
		{
			try
			{
				this.tpPid = Integer.parseInt(value);
			}
			catch (Exception e)
			{
			}
		}

		if ((value = getValue(param, "TP_udhi")) != null)
		{
			try
			{
				tpUdhi = Integer.parseInt(value);
			}
			catch (Exception e)
			{
			}
		}
		if ((value = getValue(param, "Msg_Fmt")) != null)
		{
			try
			{
				msgFmt = Integer.parseInt(value);
			}
			catch (Exception e)
			{
			}
		}

		if ((value = getValue(param, "FeeType")) != null)
		{
			feeType = value;
		}
		if ((value = getValue(param, "FeeCode")) != null)
		{
			feeCode = value;
		}

		if ((value = getValue(param, "Valid_Time")) != null)
		{
			this.validTime = value;
		}

		if ((value = getValue(param, "At_Time")) != null)
		{
			this.atTime = value;
		}

	}

	@Override
	public String toString()
	{

		StringBuffer sb = new StringBuffer();
		sb.append(" MsgID-char          : " + this.msgID + "\r\n");
		sb.append(" PkTotal-byte        : " + this.pkTotal + "\r\n");
		sb.append(" PkNumber-byte       : " + this.pkNumber + "\r\n");
		sb.append(" Registered_Delivery : " + this.registeredDelivery + "\r\n");
		sb.append(" MsgLevel-byte       : " + this.msgLevel + "\r\n");
		sb.append(" Spid-char        	: " + this.msgSrc + "\r\n");
		sb.append(" ServiceCode-char    : " + this.serviceID + "\r\n");
		sb.append(" SrcTermID-char      : " + this.srcTermID + "\r\n");
		sb.append(" DestTermCount-byte  : " + this.destTermCount + "\r\n");
		if (this.destTermCount == 1)
		{
			sb.append(" DestTermID-char     : " + destTermID[0] + "\r\n");
		}
		else
		{
			sb.append(" DestTermID-char     : [");
			for (String dest : destTermID)
			{
				sb.append(dest + ",");
			}
			sb.append("]\r\n");
		}
		sb.append(" MsgFormat-byte      : " + this.msgFmt + "\r\n");
		sb.append(" MsgLength-byte      : " + this.msgLength + "\r\n");
		sb.append(" MsgContent-char     : " + getMsg() + "\r\n");

		sb.append(" FeeUserType-byte    : " + this.feeUserType + "\r\n");
		sb.append(" FeeTermID-char      : " + this.feeTermID + "\r\n");
		sb.append(" TP_pId-byte         : " + this.tpPid + "\r\n");
		sb.append(" TP_udhi-byte        : " + this.tpUdhi + "\r\n");
		sb.append(" FeeType-char        : " + this.feeType + "\r\n");
		sb.append(" FeeCode-char        : " + this.feeCode + "\r\n");
		sb.append(" ValidTime-char      : " + this.validTime + "\r\n");
		sb.append(" AtTime-char         : " + this.atTime + "\r\n");
		sb.append(" Reserve-char         : " + this.reserve + "\r\n");

		// sb.append("\r\n------------------------------------------------------\r\n");
		return sb.toString();
	}

	public String getMsgID()
	{
		return msgID;
	}

	public int getPkTotal()
	{
		return pkTotal;
	}

	public int getPkNumber()
	{
		return pkNumber;
	}

	public int getRegisteredDelivery()
	{
		return registeredDelivery;
	}

	public int getMsgLevel()
	{
		return msgLevel;
	}

	public String getServiceID()
	{
		return serviceID;
	}

	public int getFeeUserType()
	{
		return feeUserType;
	}

	public String getFeeTermID()
	{
		return feeTermID;
	}

	public int getTpPid()
	{
		return tpPid;
	}

	public int getTpUdhi()
	{
		return tpUdhi;
	}

	public int getMsgFmt()
	{
		return msgFmt;
	}

	public String getMsgSrc()
	{
		return msgSrc;
	}

	public String getFeeType()
	{
		return feeType;
	}

	public String getFeeCode()
	{
		return feeCode;
	}

	public String getValidTime()
	{
		return validTime;
	}

	public String getAtTime()
	{
		return atTime;
	}

	public String getSrcTermID()
	{
		return srcTermID;
	}

	public int getDestTermCount()
	{
		return destTermCount;
	}

	public String[] getDestTermID()
	{
		return destTermID;
	}

	public int getMsgLength()
	{
		return msgLength;
	}

	public String getReserve()
	{
		return reserve;
	}

	public byte[] getMsgContent()
	{
		return msgContent;
	}

}
