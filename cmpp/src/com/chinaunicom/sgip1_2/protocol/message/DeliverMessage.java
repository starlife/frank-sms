/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */

package com.chinaunicom.sgip1_2.protocol.message;

import java.io.UnsupportedEncodingException;

import com.chinaunicom.sgip1_2.protocol.util.ByteConvert;

/**
 * @author Administrator
 */
public class DeliverMessage extends SGIPMessage implements Recv
{
	private String UserNumber;
	private String SPNumber;
	private int TP_pid;
	private int TP_udhi;
	private int MsgCoding;
	private int MsgLength;
	private String MsgContent;
	private byte[] msgBytes;
	private String Reserve;

	private byte[] buf;

	public DeliverMessage(BasePackage pack)
	{
		super(pack);
		this.buf = pack.getBytes();
		//
		// init data
		int loc = Header.LENGTH;
		byte[] temp = null;
		// UserNumber
		temp = new byte[21];
		System.arraycopy(buf, loc, temp, 0, temp.length);
		UserNumber = new String(temp).trim();
		loc += 21;
		// SPNumber
		temp = new byte[21];
		System.arraycopy(buf, loc, temp, 0, temp.length);
		SPNumber = new String(temp).trim();
		loc += 21;
		// TP_pid
		TP_pid = buf[loc++];
		TP_udhi = buf[loc++];
		MsgCoding = buf[loc++];
		// MsgLength;
		MsgLength = ByteConvert.byte2int(buf, loc);
		loc += 4;
		// MsgContent
		msgBytes = new byte[MsgLength];
		System.arraycopy(buf, loc, msgBytes, 0, msgBytes.length);
		this.MsgContent = getMsg(msgBytes, MsgCoding);
		loc += MsgLength;
		// Reserve
		temp = new byte[8];
		System.arraycopy(buf, loc, temp, 0, temp.length);
		Reserve = new String(temp).trim();

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
		sb.append("\r\n--------------------")
				.append(getClass().getSimpleName()).append(
						"--------------------\r\n");
		sb.append(getHead().toString() + "\r\n");
		sb.append("UserNumber  : ").append(UserNumber).append("\t");
		sb.append("SPNumber    : ").append(SPNumber).append("\r\n");
		sb.append("TP_pid      : ").append(TP_pid).append("\r\n");
		sb.append("TP_udhi     : ").append(TP_udhi).append("\r\n");
		sb.append("MsgCoding   : ").append(MsgCoding).append("\r\n");
		sb.append("MsgLength   : ").append(MsgLength).append("\r\n");
		sb.append("MsgContent  : ").append(MsgContent).append("\r\n");
		sb.append("Reserve     : ").append(Reserve);
		sb
				.append("\r\n------------------------------------------------------------\r\n");
		return sb.toString();

	}

	public String getUserNumber()
	{
		return UserNumber;
	}

	public String getSPNumber()
	{
		return SPNumber;
	}

	public int getTP_pid()
	{
		return TP_pid;
	}

	public int getTP_udhi()
	{
		return TP_udhi;
	}

	public int getMsgCoding()
	{
		return MsgCoding;
	}

	public int getMsgLength()
	{
		return MsgLength;
	}

	public String getMsgContent()
	{
		return MsgContent;
	}

	public String getReserve()
	{
		return Reserve;
	}

	@Override
	public byte[] getBytes()
	{
		// TODO Auto-generated method stub
		return buf;
	}

	public byte[] getMsgBytes()
	{
		return msgBytes;
	}

}
