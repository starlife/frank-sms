package com.chinamobile.cmpp3_0.protocol.message;

import com.chinamobile.cmpp2_0.protocol.util.*;

/**
 * @author Administrator
 */
public class DeliverRespMessage extends APackage implements Send
{
	public static final int LENGTH = 26;// 包长度
	private String MsgID;
	private int Status;
	/* Message Bytes */
	private Header head;
	private byte[] buf = new byte[DeliverRespMessage.LENGTH];

	public DeliverRespMessage(DeliverMessage delivermsg)
	{
		head = new Header(buf.length,
				CommandID.CMPP_DELIVER_RESP, delivermsg.getHead()
						.getSequenceID());
		MsgID = delivermsg.getDeliver().MsgID;
		Status = 0;
		// 对buf赋值
		System.arraycopy(head.getBytes(), 0, buf, 0, 12);
		System.arraycopy(Hex.rstr(MsgID), 0, buf, 12, 8);
		System.arraycopy(ByteConvert.int2byte(Status), 0, buf, 20, 4);

	}

	@Override
	public Header getHead()
	{
		return head;
	}

	public byte[] getBytes()
	{
		return buf;
	}

	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("\r\n---------------DeliverRespMessage-----------------\r\n");
		sb.append(head.toString() + "\r\n");
		sb.append("MsgID: " + MsgID + "\r\n");
		sb.append("Status: " + Status);
		sb.append("\r\n--------------------------------------------------\r\n");
		return sb.toString();
	}

}
