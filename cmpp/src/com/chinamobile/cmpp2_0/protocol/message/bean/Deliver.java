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

	public String MsgID = "";
	public String ServiceID = "";
	public int TP_pid;
	public int TP_udhi;
	public int MsgFormat;
	public String SrcTermID = "";
	public int SrcTermType;
	public int RegisteredDelivery;
	public String DestTermID = "";
	public int MsgLength;
	public String MsgContent = "";
	//public String LinkID = "";
	public String reserve="";
	public Report report;

	public Deliver(byte[] buf)
	{
		// init data
		int loc = 0;
		byte[] temp = null;
		// msgid
		temp = new byte[8];
		System.arraycopy(buf, loc, temp, 0, temp.length);
		MsgID = Hex.rhex(temp);
		loc += 8;
		// desttermid
		temp = new byte[21];
		System.arraycopy(buf, loc, temp, 0, temp.length);
		this.DestTermID = new String(temp).trim();
		loc += 21;
		// ServiceID
		temp = new byte[10];
		System.arraycopy(buf, loc, temp, 0, temp.length);
		this.ServiceID = new String(temp);
		loc += 10;
		// tp_pid
		this.TP_pid = buf[loc++];
		this.TP_udhi = buf[loc++];
		this.MsgFormat = buf[loc++];
		// srctermid
		temp = new byte[21];
		System.arraycopy(buf, loc, temp, 0, temp.length);
		this.SrcTermID = new String(temp).trim();
		loc += 21;
		this.RegisteredDelivery = buf[loc++];
		this.MsgLength = buf[loc++];
		// msgcontent
		temp = new byte[this.MsgLength];
		System.arraycopy(buf, loc, temp, 0, temp.length);
		this.MsgContent = getMsg(temp, this.MsgFormat);
		if (this.RegisteredDelivery == 1)
		{
			report = new Report(temp);
		}
		loc += this.MsgLength;
		// reserve
		temp = new byte[8];
		System.arraycopy(buf, loc, temp, 0, temp.length);
		this.reserve = new String(temp).trim();
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
		sb.append("MsgFormat   : " + MsgFormat + "\r\n");
		sb.append("SrcTermID   : " + SrcTermID + "\r\n");
		sb.append("SrcTermType : " + SrcTermType + "\r\n");
		sb.append("DestTermID  : " + DestTermID + "\r\n");
		sb.append("MsgLength   : " + MsgLength + "\r\n");
		sb.append("MsgContent  : " + MsgContent + "\r\n");
		sb.append("ServiceID   : " + ServiceID + "\r\n");
		sb.append("Reserve      : " + reserve + "\r\n");
		sb.append("TP_pid      : " + TP_pid + "\r\n");
		sb.append("TP_udhi     : " + TP_udhi + "\r\n");
		if (RegisteredDelivery == 1)
		{
			sb.append(this.report);
		}

		return sb.toString();
	}

}
