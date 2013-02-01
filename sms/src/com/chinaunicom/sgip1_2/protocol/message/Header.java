package com.chinaunicom.sgip1_2.protocol.message;

import com.chinamobile.cmpp2_0.protocol.util.ByteConvert;

/**
 * 该类消息头,一旦设定，不能改变
 * 
 * @version 1.0
 * @author frank
 */
public class Header
{
	public static final int LENGTH = 20;
	private int packageLength;// 数据包长度
	private int commandId;// 请求标识
	private Sequence sequenceId;// 流水号
	private byte[] buf = new byte[LENGTH];

	public Header(int packageLength, int commandId, Sequence sequenceId)
	{
		this.packageLength = packageLength;
		this.commandId = commandId;
		this.sequenceId = sequenceId;

		System.arraycopy(ByteConvert.int2byte(packageLength), 0, buf, 0, 4);
		System.arraycopy(ByteConvert.int2byte(commandId), 0, buf, 4, 4);
		//
		System.arraycopy(sequenceId.getBytes(), 0, buf, 8, 12);
	}

	/**
	 * @return the PacketLength
	 */
	public int getPackageLength()
	{
		return packageLength;
	}

	/**
	 * @return the RequestID
	 */
	public int getCommmandId()
	{
		return commandId;
	}

	/**
	 * @return the sequenceId
	 */
	public Sequence getSequenceId()
	{
		return sequenceId;
	}

	public byte[] getBytes()
	{
		return buf;

	}

	@Override
	public String toString()
	{
		return "TotalLength: " + this.packageLength + " SequenceID : "
				+ this.sequenceId + " CommandID :" + getCommandIdString();
	}

	public String getCommandIdString()
	{
		return CommandID.getCommandString(commandId);
	}

}
