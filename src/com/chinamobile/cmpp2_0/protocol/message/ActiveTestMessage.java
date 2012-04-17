package com.chinamobile.cmpp2_0.protocol.message;

public class ActiveTestMessage extends APackage implements Send, Recv
{
	private Header head;
	private byte[] buf;

	public ActiveTestMessage()
	{
		this.head = new Header(Header.LENGTH, CommandID.CMPP_ACTIVE_TEST,
				Header.allocSequenceID());
		/* buf赋值 */
		buf = head.getBytes();
	}

	/* 收到的消息 */
	public ActiveTestMessage(BasePackage pack)
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
		sb.append("\r\n---------------ActiveTestMessage------------------\r\n");
		sb.append(head.toString());
		sb.append("\r\n--------------------------------------------------\r\n");
		return sb.toString();
	}

}
