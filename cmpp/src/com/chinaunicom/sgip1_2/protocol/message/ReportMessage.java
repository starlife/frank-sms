/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */

package com.chinaunicom.sgip1_2.protocol.message;

/**
 * @author Administrator
 */
public class ReportMessage extends SGIPMessage implements Recv
{

	private Sequence SubmitSeq; // integer(12) submit消息的序列号
	/**
	 * Report命令类型 0：对先前一条Submit命令的状态报告 1：对先前一条前转Deliver命令的状态报告
	 */
	private int ReportType;// integer(1)
	private String UserNumber;// submit消息中 接收手机号码
	/**
	 * 该命令所涉及的短消息的当前执行状态 0：发送成功 1：等待发送 2：发送失败
	 */
	private int State;
	private int ErrorCode;// 当State=2时为错误码值，否则为0
	private String Reserve;// 保留，扩展用

	private byte[] buf;

	public ReportMessage(BasePackage pack)
	{
		super(pack);
		/* buf赋值 */
		buf = pack.getBytes();
		//
		int loc = Header.LENGTH;
		// SubmitSeqNum
		byte[] temp = new byte[12];
		System.arraycopy(buf, loc, temp, 0, temp.length);
		SubmitSeq = new Sequence(temp);
		loc += 12;
		// ReportType
		ReportType = buf[loc++];
		// UserNumber
		temp = new byte[21];
		System.arraycopy(buf, loc, temp, 0, temp.length);
		UserNumber = new String(temp).trim();
		loc += 21;
		// State
		State = buf[loc++];
		// ErrorCode
		ErrorCode = buf[loc++];
		// //Reserve
		temp = new byte[8];
		System.arraycopy(buf, loc, temp, 0, temp.length);
		Reserve = new String(temp).trim();

	}

	@Override
	public byte[] getBytes()
	{
		// TODO Auto-generated method stub
		return buf;
	}

	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("\r\n--------------------")
			.append(getClass().getSimpleName()).append(
				"--------------------\r\n");
		sb.append(getHead().toString()).append("\r\n");
		sb.append("SubmitSeqNum : ").append(SubmitSeq).append("\r\n");
		sb.append("ReportType   : ").append(ReportType).append("\r\n");
		sb.append("UserNumber   : ").append(UserNumber).append("\r\n");
		sb.append("State        : ").append(State).append("\r\n");
		sb.append("ErrorCode    : ").append(ErrorCode).append("\r\n");
		sb.append("Reserve      : ").append(Reserve);
		sb
			.append("\r\n------------------------------------------------------------\r\n");
		return sb.toString();
	}

	public Sequence getSubmitSeq()
	{
		return SubmitSeq;
	}

	public int getReportType()
	{
		return ReportType;
	}

	public String getUserNumber()
	{
		return UserNumber;
	}

	public int getState()
	{
		return State;
	}

	public int getErrorCode()
	{
		return ErrorCode;
	}

	public String getReserve()
	{
		return Reserve;
	}

}
