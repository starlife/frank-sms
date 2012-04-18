package com.chinamobile.cmpp2_0.protocol.message;

import com.chinamobile.cmpp2_0.protocol.message.bean.Submit;
import com.chinamobile.cmpp2_0.protocol.util.*;

/**
 * @author Administrator
 */
public class SubmitMessage extends APackage implements Send
{
	public static final int Msg_Id_LEN = 8;
	public static final int Pk_total_LEN = 1;
	public static final int Pk_number_LEN = 1;
	public static final int Registered_Delivery_LEN = 1;
	public static final int Msg_level_LEN = 1;
	public static final int ServiceID_LEN = 10;
	public static final int Fee_UserType_LEN = 1;
	public static final int FeeTermID_LEN = 21;
	public static final int TP_pid_LEN = 1;
	public static final int TP_udhi_LEN = 1;
	public static final int Msg_Fmt_LEN = 1;
	public static final int Msg_src_LEN = 6;
	public static final int FeeType_LEN = 2;
	public static final int FeeCode_LEN = 6;
	public static final int Valid_Time_LEN = 17;
	public static final int At_Time_LEN = 17;
	public static final int SrcTermID_LEN = 21;
	public static final int DestTermIDCount_LEN = 1;
	public static final int DestTermID_LEN = 21;
	public static final int Msg_Length_LEN = 1;
	public static final int Reserve_LEN = 8;

	/* buf */
	private Header head;
	private byte[] buf;
	private Submit submit;

	public SubmitMessage(Submit submit)
	{
		this.submit = submit;
		int offset = 0;
		byte[] destTermIdByte = ByteConvert.getBytes(submit.getDestTermID(),
				SubmitMessage.DestTermID_LEN);
		buf = new byte[getPackLength(destTermIdByte.length, submit
				.getMsgLength())];
		// set header
		head = new Header(buf.length, CommandID.CMPP_SUBMIT, Header
				.allocSequenceID());

		System.arraycopy(head.getBytes(), 0, buf, offset, Header.LENGTH);// copy
																			// header
		offset += Header.LENGTH;
		// System.arraycopy(submit.msgID.getBytes(), 0, buf, loc, 8);//msgid为空
		offset += Msg_Id_LEN;
		buf[offset++] = (byte) submit.getPkTotal();
		buf[offset++] = (byte) submit.getPkNumber();
		buf[offset++] = (byte) submit.getRegisteredDelivery();
		buf[offset++] = (byte) submit.getMsgLevel();
		System.arraycopy(ByteConvert.getBytes(submit.getServiceID(),
				ServiceID_LEN), 0, buf, offset, ServiceID_LEN);
		offset += ServiceID_LEN;
		buf[offset++] = (byte) submit.getFeeUserType();
		System.arraycopy(ByteConvert.getBytes(submit.getFeeTermID(),
				FeeTermID_LEN), 0, buf, offset, FeeTermID_LEN);
		offset += FeeTermID_LEN;
		buf[offset++] = (byte) submit.getTpPid();
		buf[offset++] = (byte) submit.getTpUdhi();
		buf[offset++] = (byte) submit.getMsgFmt();
		System.arraycopy(ByteConvert.getBytes(submit.getMsgSrc(), Msg_src_LEN),
				0, buf, offset, Msg_src_LEN);
		offset += Msg_src_LEN;
		System.arraycopy(
				ByteConvert.getBytes(submit.getFeeType(), FeeType_LEN), 0, buf,
				offset, FeeType_LEN);
		offset += FeeType_LEN;
		System.arraycopy(
				ByteConvert.getBytes(submit.getFeeCode(), FeeCode_LEN), 0, buf,
				offset, FeeCode_LEN);
		offset += FeeCode_LEN;
		System.arraycopy(ByteConvert.getBytes(submit.getValidTime(),
				Valid_Time_LEN), 0, buf, offset, Valid_Time_LEN);
		offset += Valid_Time_LEN;
		System.arraycopy(ByteConvert.getBytes(submit.getAtTime(), At_Time_LEN),
				0, buf, offset, At_Time_LEN);
		offset += At_Time_LEN;
		System.arraycopy(ByteConvert.getBytes(submit.getSrcTermID(),
				SrcTermID_LEN), 0, buf, offset, SrcTermID_LEN);
		offset += SrcTermID_LEN;
		buf[offset++] = (byte) submit.getDestTermCount();
		System.arraycopy(destTermIdByte, 0, buf, offset, destTermIdByte.length);
		offset += destTermIdByte.length;
		buf[offset++] = (byte) submit.getMsgLength();
		System.arraycopy(submit.getMsgContent(), 0, buf, offset, submit
				.getMsgLength());
		offset += submit.getMsgLength();
		System.arraycopy(
				ByteConvert.getBytes(submit.getReserve(), Reserve_LEN), 0, buf,
				offset, Reserve_LEN);

	}

	private int getPackLength(int destTermByteLen, int msgByteLen)
	{
		int len = Header.LENGTH + Msg_Id_LEN + Pk_total_LEN + Pk_number_LEN
				+ Registered_Delivery_LEN + Msg_level_LEN + ServiceID_LEN
				+ Fee_UserType_LEN + FeeTermID_LEN + TP_pid_LEN + TP_udhi_LEN
				+ Msg_Fmt_LEN + Msg_src_LEN + FeeType_LEN + FeeCode_LEN
				+ Valid_Time_LEN + At_Time_LEN + SrcTermID_LEN
				+ DestTermIDCount_LEN + destTermByteLen + Msg_Length_LEN
				+ msgByteLen + Reserve_LEN;

		return len;
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
		String[] desttermid = "13777802386".split(",");
		String msg = "您好，您发送的消息为：" + "55" + "!";
		byte[] msgByte = msg.getBytes();// gbk解码
		String param = "";
		SubmitMessage sm = MessageUtil.createSubmitMessage("911337",
				"106573061704", "MZJ3310101", desttermid, msgByte, param);
		System.out.println(Hex.rhex(sm.getBytes()));
		System.out.println(sm.getBytes().length);
	}
}
