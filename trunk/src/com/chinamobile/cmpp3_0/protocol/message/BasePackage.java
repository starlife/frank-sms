package com.chinamobile.cmpp3_0.protocol.message;

import com.chinamobile.cmpp3_0.protocol.util.*;

/**
 * 该类用于接收电信的数据包
 * 
 * @author Administrator
 */
public class BasePackage extends APackage
{
	private Header head;
	private byte[] buf;
	private long timestamp;// 接收包的时间

	public BasePackage(byte[] buf)
	{
		this.buf = buf;
		head = new Header(TypeConvert.byte2int(buf, 0), TypeConvert.byte2int(
				buf, 4), TypeConvert.byte2int(buf, 8));
		this.timestamp = System.currentTimeMillis();

	}

	@Override
	public Header getHead()
	{
		return this.head;
	}

	public byte[] getBytes()
	{
		return buf;
	}

	public long getTimeStamp()
	{
		return timestamp;
	}

	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("TotalLength:" + getHead().getTotalLength() + "\r\n");
		sb.append("CommmandID:" + getHead().getCommmandID() + "\r\n");
		sb.append("SequenceID:" + getHead().getSequenceID() + "\r\n");
		sb.append("RecvTime:" + new java.util.Date(getTimeStamp()) + "\r\n");
		return sb.toString();
	}

}
