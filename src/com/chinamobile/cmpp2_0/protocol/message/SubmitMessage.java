package com.chinamobile.cmpp2_0.protocol.message;

import com.chinamobile.cmpp2_0.protocol.message.bean.Submit;
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
		byte[] destTermIdByte = TypeConvert.getBytes(submit.destTermID, 21);
		buf = new byte[getPackLength(destTermIdByte.length, submit.msgLength)];
		// set header
		head = new Header(buf.length, CommandID.CMPP_SUBMIT, Header
				.allocSequenceID());

		System.arraycopy(head.getBytes(), 0, buf, loc, 12);// copy header
		loc += 12;
		//System.arraycopy(submit.msgID.getBytes(), 0, buf, loc, 8);//msgid为空
		loc += 8;
		buf[loc++] = (byte) submit.pkTotal;
		buf[loc++] = (byte) submit.pkNumber;
		buf[loc++] = (byte) submit.registeredDelivery;
		buf[loc++] = (byte) submit.msgLevel;
		System.arraycopy(TypeConvert.getBytes(submit.serviceID,10), 0, buf, loc,10);
		loc += 10;
		buf[loc++] = (byte) submit.feeUserType;
		System.arraycopy(TypeConvert.getBytes(submit.feeTermID,21), 0, buf,loc, 21);
		loc += 21;
		buf[loc++] = (byte) submit.tpPid;
		buf[loc++] = (byte) submit.tpUdhi;
		buf[loc++] = (byte) submit.msgFmt;
		System.arraycopy(TypeConvert.getBytes(submit.msgSrc,6), 0, buf, loc,6);
		loc += 6;
		System.arraycopy(TypeConvert.getBytes(submit.feeType,2), 0, buf,loc, 2);
		loc += 2;
		System.arraycopy(TypeConvert.getBytes(submit.feeCode,6), 0, buf, loc,6);
		loc += 6;
		System.arraycopy(TypeConvert.getBytes(submit.validTime,17), 0, buf, loc,17);
		loc += 17;
		System.arraycopy(TypeConvert.getBytes(submit.atTime,17), 0, buf, loc,17);
		loc += 17;
		System.arraycopy(TypeConvert.getBytes(submit.srcTermID,21), 0, buf, loc,21);
		loc += 21;
		buf[loc++] = (byte) submit.destTermCount;
		System.arraycopy(destTermIdByte,0, buf, loc, destTermIdByte.length);
		loc += destTermIdByte.length;
		buf[loc++] = (byte) submit.msgLength;
		System.arraycopy(submit.msgContent, 0, buf, loc, submit.msgLength);
		loc += submit.msgLength;
		System.arraycopy(TypeConvert.getBytes(submit.reserve,8), 0, buf, loc,8);
		

	}

	private int getPackLength(int destTermByteLen, int msgByteLen)
	{
		int i = 12 + 8 + 1 + 1 + 1 + 1 + 10 + 1 + 21 + 1 + 1 + 1 + 6 + 2
				+ 6 + 17 + 17 + 21 + 1 + destTermByteLen + 1 + msgByteLen + 8;

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
	public static void main(String[] args)
	{
		String[] desttermid="13777802386".split(",");
		String msg="您好，您发送的消息为："+"55"+"!";
		byte[] msgByte = msg.getBytes();//gbk解码
		String param = ""; 						
		SubmitMessage sm= MessageUtil.createSubmitMessage("911337","106573061704","MZJ3310101", desttermid, msgByte,param);
		System.out.println(Hex.rhex(sm.getBytes()));
		System.out.println(sm.getBytes().length);
	}
}
