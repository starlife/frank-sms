package com.chinamobile.cmpp2_0.protocol.message.bean;

import java.io.UnsupportedEncodingException;
import java.util.regex.*;

/**
 * @author Administrator
 */
public class Submit
{
	
	/**
	 * 信息标识 SubmitMessage为空，SubmitRespMessage中包含该值
	 *  String(8)
	 */
	public String msgID="";
	/**
	 * 相同Msg_Id的信息总条数，从1开始
	 * int
	 */
	public int pkTotal = 1;
	/**
	 * 相同Msg_Id的信息序号，从1开始。
	 * int
	 */
	public int pkNumber = 1;
	/**
	 * 是否要求返回状态报告，枚举： 1 返回  0 不放回
	 */
	public int registeredDelivery = 1;
	/**
	 * 信息级别 默认为3
	 */
	public int msgLevel = 3;
	/**
	 * 业务标识，是数字、字母和符号的组合
	 * String(10)
	 */
	public String serviceID="";
	/**
	 *  计费类型 0表示对目的终端计费；1表示对源终端计费 2对sp计费 3表示本字段无效(对谁计费参见FeeTerminalId)
	 * 默认为2（对sp端计费）
	 */
	public int feeUserType = 2;
	
	/**
	 * 计费号码 当feeUserType为3时有效，当feeUserType为0、1、2时无效
	 */
	public String feeTermID="";
	
	
	
	/**
	 * TP-Protocol-identifier 默认为0
	 */
	public int tpPid=0;
	/**
	 * TP-User-Data—Header-Indicator 枚举： 
	 * 0 表示消息内容（TP-UD）不包含头信息(消息内容可包含7bit,8bit,16bit(ucs2)) 
	 * 1 表示消息内容包含消息头  通常用于发送二进制短信（长短信，wappush）
	 */
	public int tpUdhi=0;
	/**
	 * 短信内容编码 0=ACSII编码 3=短消息写卡操作 4=二进制 8=UCS2 15=GB18030
	 * 
	 */
	public int msgFmt=15;
	/**
	 * 消息来源，填spid
	 *  String(6)
	 */
	public String msgSrc="";
	/**
	 * 计费类型：01 对计费用户号码免费 02 对计费用户号码按条记信息费 03 对计费用户号码按月收取费用
	 * String(2)
	 */
	public String feeType = "01";
	/**
	 * 资费代码 （单位分）如 5元包月这里写"500"
	 * String(6)
	 */
	public String feeCode="0";
	/**
	 *  短信有效时间，转发过程中保持不变
	 *  String(17)
	 */
	public String validTime = "";
	/**
	 * 短信定时发送时间，转发过程中保持不变
	 * String(17)
	 */
	public String atTime = "";
	/**
	 * 源号码（这里填接入号spnumber）
	 * String(21)
	 */
	public String srcTermID="";
	/**
	 * 目的号码个数
	 */
	public int destTermCount=1;
	/**
	 * 目的号码
	 * String(DestTermCount*32)
	 */
	public String[] destTermID;

	/**
	 * 消息长度（短信内容的字节数）
	 */
	public int msgLength;
	/**
	 * 消息内容
	 */
	public byte[] msgContent;
	/**
	 * 点播业务使用的LinkID，非点播类业务的MT流程不使用该字段。
	 */
	//public String linkID="";
	public String reserve="";

	public Submit(String spid,String spnumber,String serviceCode,String[] desttermid, byte[] msgcontent,
			String param)
	{
		this.msgSrc=spid;
		this.srcTermID = spnumber;
		this.serviceID=serviceCode;
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

	public String getMsgContent()
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
		if ((value = getValue(param, "srcTermID")) != null)
		{
			srcTermID = value;
		}
		if ((value = getValue(param, "feeType")) != null)
		{
			feeType = value;
		}
		if ((value = getValue(param, "feeCode")) != null)
		{
			feeCode = value;
		}

		if ((value = getValue(param, "tpUdhi")) != null)
		{
			try
			{
				tpUdhi = Integer.parseInt(value);
			}
			catch (Exception e)
			{
			}
		}
		if ((value = getValue(param, "msgFmt")) != null)
		{
			try
			{
				msgFmt = Integer.parseInt(value);
			}
			catch (Exception e)
			{
			}
		}
		if ((value = getValue(param, "serviceID")) != null)
		{
			serviceID = value;
		}
		if ((value = getValue(param, "pkTotal")) != null)
		{
			try
			{
				pkTotal = Integer.parseInt(value);
			}
			catch (Exception e)
			{
			}
		}
		if ((value = getValue(param, "pkNumber")) != null)
		{
			try
			{
				pkNumber = Integer.parseInt(value);
			}
			catch (Exception e)
			{
			}
		}

	}

	@Override
	public String toString()
	{

		StringBuffer sb = new StringBuffer();
		sb.append(" MsgID-char          : " + this.msgID + "\r\n");
		sb.append(" PkTotal-byte        : " + this.pkTotal + "\r\n");
		sb.append(" PkNumber-byte       : " + this.pkNumber + "\r\n");
		sb.append(" NeedReport-byte     : " + this.registeredDelivery + "\r\n");
		sb.append(" MsgLevel-byte       : " + this.msgLevel + "\r\n");
		sb.append(" ServiceID-char      : " + this.serviceID + "\r\n");
		sb.append(" FeeUserType-byte    : " + this.feeUserType + "\r\n");
		sb.append(" FeeTermID-char      : " + this.feeTermID + "\r\n");
		sb.append(" FeeType-char        : " + this.feeType + "\r\n");
		sb.append(" FeeCode-char        : " + this.feeCode + "\r\n");
		sb.append(" MsgFormat-byte      : " + this.msgFmt + "\r\n");
		sb.append(" ValidTime-char      : " + this.validTime + "\r\n");
		sb.append(" AtTime-char         : " + this.atTime + "\r\n");
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
		sb.append(" MsgLength-byte      : " + this.msgLength + "\r\n");
		sb.append(" MsgContent-char     : " + getMsgContent() + "\r\n");
		sb.append(" Reserve-char         : " + this.reserve + "\r\n");
		sb.append(" MsgSrc-char         : " + this.msgSrc + "\r\n");

		// sb.append("\r\n------------------------------------------------------\r\n");
		return sb.toString();
	}

}
