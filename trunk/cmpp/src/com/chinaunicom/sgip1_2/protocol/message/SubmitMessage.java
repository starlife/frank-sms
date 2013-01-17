/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */

package com.chinaunicom.sgip1_2.protocol.message;

import com.chinaunicom.sgip1_2.protocol.message.bean.Submit;
import com.chinaunicom.sgip1_2.protocol.util.ByteConvert;

/**
 * @author Administrator
 */
public class SubmitMessage extends SGIPMessage implements Send
{

	private Submit submit;
	private byte[] buf = null;

	public SubmitMessage(String nodeid, Submit submit)
	{
		super(Header.LENGTH,CommandID.SGIP_SUBMIT,new Sequence(nodeid));
		this.submit = submit;
		int packLen = getPackageLen(submit.getUserCount(), submit
				.getMsgLength());
		buf = new byte[packLen];
		/* buf 赋值 */
		int loc = 0;
		// byte[] temp=null;
		System.arraycopy(getHead().getBytes(), 0, buf, loc, Header.LENGTH);// copy
		// header
		loc += Header.LENGTH;

		System.arraycopy(ByteConvert.getBytes(submit.getSPNumber(), 21), 0,
				buf, loc, 21);
		loc += 21;
		System.arraycopy(ByteConvert.getBytes(submit.getChargeNumber(), 21), 0,
				buf, loc, 21);
		loc += 21;
		buf[loc++] = (byte) submit.getUserCount();
		System.arraycopy(ByteConvert.getBytes(submit.getUserNumber(), 21), 0,
				buf, loc, submit.getUserCount() * 21);
		loc += submit.getUserCount() * 21;

		System.arraycopy(ByteConvert.getBytes(submit.getCorpId(), 5), 0, buf,
				loc, 5);
		loc += 5;
		System.arraycopy(ByteConvert.getBytes(submit.getServiceType(), 10), 0,
				buf, loc, 10);
		loc += 10;
		buf[loc++] = (byte) submit.getFeeType();
		System.arraycopy(ByteConvert.getBytes(submit.getFeeValue(), 6), 0, buf,
				loc, 6);
		loc += 6;
		System.arraycopy(ByteConvert.getBytes(submit.getGivenValue(), 6), 0,
				buf, loc, 6);
		loc += 6;
		buf[loc++] = (byte) submit.getAgentFlag();
		buf[loc++] = (byte) submit.getMorelatetoMTFlag();
		buf[loc++] = (byte) submit.getPriority();
		System.arraycopy(ByteConvert.getBytes(submit.getExpireTime(), 16), 0,
				buf, loc, 16);
		loc += 16;
		System.arraycopy(ByteConvert.getBytes(submit.getScheduleTime(), 16), 0,
				buf, loc, 16);
		loc += 16;
		buf[loc++] = (byte) submit.getReportFlag();
		buf[loc++] = (byte) submit.getTP_pid();
		buf[loc++] = (byte) submit.getTP_udhi();
		buf[loc++] = (byte) submit.getMsgCoding();
		buf[loc++] = (byte) submit.getMsgType();
		System.arraycopy(ByteConvert.int2byte(submit.getMsgLength()), 0, buf,
				loc, 4);
		loc += 4;
		System.arraycopy(submit.getMsgContent(), 0, buf, loc, submit
				.getMsgLength());
		loc += submit.getMsgLength();
		System.arraycopy(ByteConvert.getBytes(submit.getReserve(), 8), 0, buf,
				loc, 8);

	}

	private int getPackageLen(int userCount, int msglength)
	{
		int len = Header.LENGTH;
		len += 21;// SPNumber
		len += 21;// ChargeNumber;
		len += 1;// UserCount
		len += userCount * 21;// UserNumber
		len += 5;// CorpId
		len += 10;// ServiceType
		len += 1;// FeeType
		len += 6;// FeeValue
		len += 6;// GivenValue
		len += 1;// AgentFlag
		len += 1;// MorelatetoMTFlag
		len += 1;// Priority
		len += 16;// ExpireTime
		len += 16;// ScheduleTime
		len += 1;// ReportFlag
		len += 1;// TP_pid
		len += 1;// TP_udhi
		len += 1;// MessageCoding
		len += 1;// MessageType
		len += 4;// MessageLength
		len += msglength;// MessageContent
		len += 8;// Reserve
		return len;
	}

	public Submit getSubmit()
	{
		return submit;
	}

	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("\r\n--------------------")
				.append(getClass().getSimpleName()).append(
						"--------------------\r\n");
		sb.append(getHead() + "\r\n");
		sb.append(getSubmit().toString());
		sb
				.append("\r\n------------------------------------------------------------\r\n");
		return sb.toString();

	}

	@Override
	public byte[] getBytes()
	{
		// TODO Auto-generated method stub
		return buf;
	}

}
