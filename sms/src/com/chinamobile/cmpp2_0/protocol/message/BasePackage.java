package com.chinamobile.cmpp2_0.protocol.message;

import com.chinamobile.cmpp2_0.protocol.util.*;

/**
 * 基础包类,把buf的数据组成一个包
 * 
 * @author linxinzheng
 */
public class BasePackage extends APackage
{
	private Header head;
	private byte[] buf;

	public BasePackage(byte[] buf)
	{	
		this.buf = buf;
		head = new Header(ByteConvert.byte2int(buf, 0), ByteConvert.byte2int(
				buf, 4), ByteConvert.byte2int(buf, 8));
		this.setTimeStamp();

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


	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("CommmandID:" + getHead().getCommandIdString() + "\r\n");
		sb.append("TotalLength:" + getHead().getPackageLength() + "\r\n");
		sb.append("SequenceID:" + getHead().getSequenceId() + "\r\n");
		sb.append("RecvTime:" + DateUtil.getTimeString(getTimeStamp()) + "\r\n");
		return sb.toString();
	}

}
