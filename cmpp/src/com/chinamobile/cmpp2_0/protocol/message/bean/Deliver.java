package com.chinamobile.cmpp2_0.protocol.message.bean;

/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */

import java.io.UnsupportedEncodingException;

import com.chinamobile.cmpp2_0.protocol.util.Hex;

/**
 * 处理Deliver消息的body逻辑
 * 
 * @author Administrator
 */
public class Deliver
{

	private String MsgID = "";// 8 Unsigned Integer
	private String DestTermID = "";// 21 Octet String
	private String ServiceID = "";// 10 Octet String
	private int TP_pid;// 1 Unsigned Integer
	private int TP_udhi;// 1 Unsigned Integer
	private int MsgFmt;// 1 Unsigned Integer
	private String SrcTermID = "";// 21 Octet String
	private int RegisteredDelivery;// 1 Unsigned Integer 是否为状态报告 0：非状态报告 1：状态报告

	private int MsgLength; // 1 Unsigned Integer
	private String MsgContent = "";// Msg_length Octet String
	private String Reserved = "";// 8 Octet String
	private Report report;

	public Deliver(byte[] buf)
	{
		this(buf, 0);
	}

	public Deliver(byte[] buf, int offset)
	{
		// init data
		byte[] temp = null;
		// msgid
		temp = new byte[8];
		System.arraycopy(buf, offset, temp, 0, temp.length);
		MsgID = Hex.rhex(temp);
		offset += 8;
		// desttermid
		temp = new byte[21];
		System.arraycopy(buf, offset, temp, 0, temp.length);
		this.DestTermID = new String(temp).trim();
		offset += 21;
		// ServiceID
		temp = new byte[10];
		System.arraycopy(buf, offset, temp, 0, temp.length);
		this.ServiceID = new String(temp);
		offset += 10;
		// tp_pid
		this.TP_pid = buf[offset++];
		this.TP_udhi = buf[offset++];
		this.MsgFmt = buf[offset++];
		// srctermid
		temp = new byte[21];
		System.arraycopy(buf, offset, temp, 0, temp.length);
		this.SrcTermID = new String(temp).trim();
		offset += 21;
		this.RegisteredDelivery = buf[offset++];
		this.MsgLength = buf[offset++]&0xff;//如果大于127的话需要&0xff
		// msgcontent
		temp = new byte[this.MsgLength];
		System.arraycopy(buf, offset, temp, 0, temp.length);
		this.MsgContent = getMsg(temp, this.MsgFmt);
		if (this.RegisteredDelivery == 1)
		{
			report = new Report(temp);
		}
		offset += this.MsgLength;
		// reserve
		temp = new byte[8];
		System.arraycopy(buf, offset, temp, 0, temp.length);
		this.Reserved = new String(temp).trim();
	}

	private String getMsg(byte[] src, int msgFormat)
	{
		String encode = "ISO8859-1";
		if (msgFormat == 8)
		{
			encode = "UTF-16BE";
		}
		if (msgFormat == 15)
		{
			encode = "GB18030";
		}
		try
		{
			return new String(src, encode);
		}
		catch (UnsupportedEncodingException ex)
		{
			return new String(src);
		}
	}

	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("MsgID       : " + MsgID + "\r\n");
		sb.append("IsReport:   : " + RegisteredDelivery + "\r\n");
		sb.append("MsgFormat   : " + MsgFmt + "\r\n");
		sb.append("SrcTermID   : " + SrcTermID + "\r\n");
		sb.append("DestTermID  : " + DestTermID + "\r\n");
		sb.append("MsgLength   : " + MsgLength + "\r\n");
		sb.append("MsgContent  : " + MsgContent + "\r\n");
		sb.append("ServiceID   : " + ServiceID + "\r\n");
		sb.append("Reserve     : " + Reserved + "\r\n");
		sb.append("TP_pid      : " + TP_pid + "\r\n");
		sb.append("TP_udhi     : " + TP_udhi + "\r\n");
		if (RegisteredDelivery == 1)
		{
			sb.append(this.report);
		}

		return sb.toString();
	}

	public String getMsgID()
	{
		return MsgID;
	}

	public String getDestTermID()
	{
		return DestTermID;
	}

	public String getServiceID()
	{
		return ServiceID;
	}

	public int getTP_pid()
	{
		return TP_pid;
	}

	public int getTP_udhi()
	{
		return TP_udhi;
	}

	public int getMsgFmt()
	{
		return MsgFmt;
	}

	public String getSrcTermID()
	{
		return SrcTermID;
	}

	public int getRegisteredDelivery()
	{
		return RegisteredDelivery;
	}

	public int getMsgLength()
	{
		return MsgLength;
	}

	public String getMsgContent()
	{
		return MsgContent;
	}

	public String getReserved()
	{
		return Reserved;
	}

	public Report getReport()
	{
		return report;
	}

}
