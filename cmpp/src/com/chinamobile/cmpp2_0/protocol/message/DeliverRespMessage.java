package com.chinamobile.cmpp2_0.protocol.message;

import com.chinamobile.cmpp2_0.protocol.util.*;

/**
 * @author Administrator
 */
public class DeliverRespMessage extends APackage implements Send
{
	public static final int LENGTH = 21;// 包长度
	private String MsgID;
	private int Status;
	/* Message Bytes */
	private Header head;
	private byte[] buf = new byte[DeliverRespMessage.LENGTH];

	public DeliverRespMessage(DeliverMessage delivermsg)
	{
		head = new Header(DeliverRespMessage.LENGTH,
				CommandID.CMPP_DELIVER_RESP, delivermsg.getHead()
						.getSequenceId());
		MsgID = delivermsg.getDeliver().getMsgID();
		Status = 0;
		// 对buf赋值
		int offset = 0;
		System.arraycopy(head.getBytes(), 0, buf, 0, Header.LENGTH);
		offset += Header.LENGTH;
		System.arraycopy(Hex.rstr(MsgID), 0, buf, offset, 8);
		offset += 8;
		buf[offset++] = (byte) Status;

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
