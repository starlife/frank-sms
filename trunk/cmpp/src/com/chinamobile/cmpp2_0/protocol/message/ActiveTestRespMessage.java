package com.chinamobile.cmpp2_0.protocol.message;

public class ActiveTestRespMessage extends APackage implements Send, Recv
{

	private Header head;
	private byte[] buf;
	private int state = 0;

	/* 收到的消息 */

	public ActiveTestRespMessage(BasePackage pack)
	{
		this.head = pack.getHead();
		/* buf赋值 */
		buf = pack.getBytes();
		state = buf[12];

	}

	/* 主动回应的消息 */

	public ActiveTestRespMessage(ActiveTestMessage activeMsg)
	{
		this.head = new Header(12, CommandID.CMPP_ACTIVE_TEST_RESP, activeMsg
				.getHead().getSequenceID());
		/* buf赋值 */
		buf = new byte[13];
		System.arraycopy(head.getBytes(), 0, buf, 0, 12);
		buf[12] = (byte) state;

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
		sb.append("State:" + state);
		sb.append("\r\n--------------------------------------------------\r\n");
		return sb.toString();
	}
}
