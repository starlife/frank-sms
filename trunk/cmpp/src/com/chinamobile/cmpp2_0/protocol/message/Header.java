package com.chinamobile.cmpp2_0.protocol.message;

import com.chinamobile.cmpp3_0.protocol.util.TypeConvert;

/**
 * 消息首，一旦设定，不能改变
 * 
 * @author Administrator
 */
public class Header {
	public static final int LENGTH = 12;
	private static int allocSequenceId = 0;
	private int totalLength;// 数据包长度
	private int commandID;// 请求标识
	private int sequenceID;// 流水号
	private byte[] buf = new byte[LENGTH];

	public Header(int packetLength, int requestId, int sequenceId) {
		this.totalLength = packetLength;
		this.commandID = requestId;
		this.sequenceID = sequenceId;

		System.arraycopy(TypeConvert.int2byte(packetLength), 0, buf, 0, 4);
		System.arraycopy(TypeConvert.int2byte(requestId), 0, buf, 4, 4);
		System.arraycopy(TypeConvert.int2byte(sequenceId), 0, buf, 8, 4);
	}

	/**
	 * @return the PacketLength
	 */
	public int getTotalLength() {
		return totalLength;
	}

	/**
	 * @return the RequestID
	 */
	public int getCommmandID() {
		return commandID;
	}

	/**
	 * @return the SequenceID
	 */
	public int getSequenceID() {
		return sequenceID;
	}

	/**
	 * 分配流水号
	 * 
	 * @return
	 */
	public static synchronized int allocSequenceID() {
		allocSequenceId = (allocSequenceId + 1) & 0xffffffff;
		return allocSequenceId;
	}

	public byte[] getBytes() {
		return buf;

	}

	@Override
	public String toString() {
		return "TotalLength: " + this.totalLength + " SequenceID : "
				+ this.sequenceID + " CommandID :" + getCommandIdString();
	}

	public String getCommandIdString() {
		return CommandID.getCommandString(commandID);
	}

}
