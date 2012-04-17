package com.chinamobile.cmpp2_0.protocol.message;

/**
 * @author Administrator
 */
public class TerminateMessage extends APackage implements Send, Recv
{
	private Header head;
	private byte[] buf;

	/* 主动发送的 */
	public TerminateMessage()
	{
		this.head = new Header(Header.LENGTH, CommandID.CMPP_TERMINATE, Header
				.allocSequenceID());
		/* buf赋值 */
		buf = head.getBytes();

	}

	/* 接收到的 */
	public TerminateMessage(BasePackage pack)
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

	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("\r\n---------------TerminateMessage-------------------\r\n");
		sb.append(head.toString());
		sb.append("\r\n--------------------------------------------------\r\n");
		return sb.toString();
	}

}
