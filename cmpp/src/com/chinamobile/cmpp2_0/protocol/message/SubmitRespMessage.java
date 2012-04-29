package com.chinamobile.cmpp2_0.protocol.message;

import com.chinamobile.cmpp2_0.protocol.util.*;

/**
 * @author Administrator
 */
public class SubmitRespMessage extends APackage implements Recv
{
	private Header head;
	private byte[] buf;
	private String MsgID;
	private int Status;

	public SubmitRespMessage(BasePackage pack)
	{

		this.head = pack.getHead();
		this.buf = pack.getBytes();
		// init
		int offset = Header.LENGTH;
		byte[] msgidByte = new byte[8];
		System.arraycopy(buf, offset, msgidByte, 0, msgidByte.length);
		offset += 8;
		MsgID = Hex.rhex(msgidByte);
		Status = buf[offset];
		this.setTimeStamp(pack.getTimeStamp());//设置包时间
	}

	public int getStatus()
	{
		return Status;
	}

	public int getPacketLength()
	{
		return head.getPackageLength();
	}

	public int getSequenceID()
	{
		return head.getSequenceId();
	}

	public String getMsgId()
	{
		return MsgID;
	}

	@Override
	public Header getHead()
	{
		return head;
	}

	@Override
	public byte[] getBytes()
	{
		return buf;
	}

	public String getStatus(int status)
	{

		switch (status)
		{
			case 0:
				return "正确";
			case 1:
				return "消息结构错";
			case 2:
				return "命令字错";
			case 3:
				return "消息序号重复";
			case 4:
				return "消息长度错";
			case 5:
				return "资费代码错";
			case 6:
				return "超过最大信息长";
			case 7:
				return "业务代码错";
			case 8:
				return "流量控制错";
			case 9:
				return "本网关不负责服务此计费号码";
			case 10:
				return "Src_Id错误";
			case 11:
				return "Msg_src错误";
			case 12:
				return "Fee_terminal_Id错误";
			case 13:
				return "Dest_terminal_Id错误";
			default:
				return "未知错误(" + status + ")";
		}
	}

	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb
				.append("\r\n---------------SubmitRespMessage----------------------\r\n");
		sb.append(head.toString() + "\r\n");
		sb.append("MsgID: " + MsgID + " Status :" + getStatus(Status));
		sb
				.append("\r\n------------------------------------------------------\r\n");
		return sb.toString();
	}

}
