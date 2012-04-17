package com.chinamobile.cmpp3_0.protocol.message;

/**
 * @author Administrator
 */
public class TerminateRespMessage extends APackage implements Send, Recv
{
	private Header head;
	private byte[] buf;

	/* 主动回应的 */
	public TerminateRespMessage(TerminateMessage exitMsg)
	{
		this.head = new Header(Header.LENGTH, CommandID.CMPP_TERMINATE_RESP,
				exitMsg.getHead().getSequenceID());
		/* buf赋值 */
		buf = head.getBytes();

	}

	/* 接收到的 */
	public TerminateRespMessage(BasePackage pack)
	{
		this.head = pack.getHead();
		/* buf赋值 */
		buf = pack.getBytes();
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

	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("\r\n---------------TerminateRespMessage---------------\r\n");
		sb.append(head.toString() + "\r\n");
		sb.append("\r\n--------------------------------------------------\r\n");
		return sb.toString();
	}

}
