package com.chinamobile.cmpp3_0.protocol.message;

import com.chinamobile.cmpp3_0.protocol.message.bean.Submit;
import com.chinamobile.cmpp2_0.protocol.util.*;

/**
 * @author Administrator
 */
public class SubmitMessage extends APackage implements Send
{

	/* buf */
	private Header head;
	private byte[] buf;
	private Submit submit;

	public SubmitMessage(Submit submit)
	{
		this.submit = submit;
		int loc = 0;
		byte[] destTermIdByte = ByteConvert.getBytes(submit.destTermID, 32);
		buf = new byte[getPackLength(destTermIdByte.length, submit.msgLength)];
		// set header
		head = new Header(buf.length, CommandID.CMPP_SUBMIT, Header
				.allocSequenceID());

		System.arraycopy(head.getBytes(), 0, buf, loc, 12);// copy header
		loc += 12;
		System.arraycopy(submit.msgID.getBytes(), 0, buf, loc, 8);
		loc += 8;
		buf[loc++] = (byte) submit.pkTotal;
		buf[loc++] = (byte) submit.pkNumber;
		buf[loc++] = (byte) submit.registeredDelivery;
		buf[loc++] = (byte) submit.msgLevel;
		System.arraycopy(submit.serviceID, 0, buf, loc, 10);
		loc += 10;
		buf[loc++] = (byte) submit.feeUserType;
		System.arraycopy(submit.feeTermID.getBytes(), 0, buf, 0, 32);
		loc += 32;
		buf[loc++] = (byte) submit.feeTermType;
		buf[loc++] = (byte) submit.tpPid;
		buf[loc++] = (byte) submit.tpUdhi;
		buf[loc++] = (byte) submit.msgFmt;
		System.arraycopy(submit.msgSrc.getBytes(), 0, buf, loc, 6);
		loc += 6;
		System.arraycopy(submit.feeType.getBytes(), 0, buf, loc, 2);
		loc += 2;
		System.arraycopy(submit.feeCode.getBytes(), 0, buf, loc, 6);
		loc += 6;
		System.arraycopy(submit.validTime.getBytes(), 0, buf, loc, 17);
		loc += 17;
		System.arraycopy(submit.atTime.getBytes(), 0, buf, loc, 17);
		loc += 17;
		System.arraycopy(submit.srcTermID.getBytes(), 0, buf, loc, 21);
		loc += 21;
		buf[loc++] = (byte) submit.destTermCount;
		System.arraycopy(destTermIdByte, loc, head, loc, destTermIdByte.length);
		loc += destTermIdByte.length;
		buf[loc++] = (byte) submit.destTermType;
		buf[loc++] = (byte) submit.msgLength;
		System.arraycopy(submit.msgContent, 0, buf, loc, submit.msgLength);
		loc += submit.msgLength;
		System.arraycopy(submit.linkID, 0, buf, loc, 20);
		System.out.println("PackLength: " + buf.length + " loc: " + loc);

	}

	private int getPackLength(int destlength, int msglength)
	{
		int i = 12 + 8 + 1 + 1 + 1 + 1 + 10 + 1 + 32 + 1 + 1 + 1 + 1 + 6 + 2
				+ 6 + 17 + 17 + 21 + 1 + destlength + 1 + 1 + msglength + 20;

		return i;
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
		sb.append("\r\n---------------SubmitMessage----------------------\r\n");
		sb.append(head.toString() + "\r\n");
		sb.append(getSubmit().toString());
		sb.append("\r\n--------------------------------------------------\r\n");
		return sb.toString();

	}

	/**
	 * @return the submit
	 */
	public Submit getSubmit()
	{
		return submit;
	}
}
