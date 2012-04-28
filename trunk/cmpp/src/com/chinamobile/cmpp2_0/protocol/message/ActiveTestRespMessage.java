package com.chinamobile.cmpp2_0.protocol.message;

/**
 * 该类实现CMPP_ACTIVE_TEST_RESP消息
 * @version 1.0
 * @author frank
 *
 */
public class ActiveTestRespMessage extends APackage implements Send, Recv
{
	public static final int LENGTH=13;//包长度固定为13
	private Header head;
	private byte[] buf;
	private int reserve = 0;
	

	/* 收到的消息 */

	public ActiveTestRespMessage(BasePackage pack)
	{
		this.head = pack.getHead();
		/* buf赋值 */
		buf = pack.getBytes();
		reserve = buf[Header.LENGTH];
		this.setTimeStamp(pack.getTimeStamp());//设置包时间

	}

	/* 主动回应的消息 */

	public ActiveTestRespMessage(ActiveTestMessage activeMsg)
	{
		this.head = new Header(ActiveTestRespMessage.LENGTH, CommandID.CMPP_ACTIVE_TEST_RESP, activeMsg
				.getHead().getSequenceId());
		/* buf赋值 */
		buf = new byte[ActiveTestRespMessage.LENGTH];
		System.arraycopy(head.getBytes(), 0, buf, 0,Header.LENGTH);
		buf[Header.LENGTH] = (byte) reserve;

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
		sb.append("\r\n---------------ActiveTestRespMessage--------------\r\n");
		sb.append(head.toString() + "\r\n");
		sb.append("Reserved:" + reserve);
		sb.append("\r\n--------------------------------------------------\r\n");
		return sb.toString();
	}
}
