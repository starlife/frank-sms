package com.chinamobile.cmpp2_0.protocol.message;

/**
 * 该类实现CMPP_ACTIVE_TEST消息
 * @version 1.0
 * @author frank
 *
 */
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
		this.setTimeStamp(pack.getTimeStamp());//设置包时间
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
