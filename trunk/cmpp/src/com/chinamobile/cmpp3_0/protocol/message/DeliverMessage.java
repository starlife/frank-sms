package com.chinamobile.cmpp3_0.protocol.message;

import com.chinamobile.cmpp3_0.protocol.message.bean.Deliver;

/**
 * @author Administrator
 */
public class DeliverMessage extends APackage implements Recv
{

	private Header head;
	private byte[] buf;
	private Deliver deliver;

	public DeliverMessage(BasePackage pack)
	{
		this.head = pack.getHead();
		this.buf = pack.getBytes();
		//
		byte[] body = new byte[buf.length - 12];
		System.arraycopy(buf, 12, body, 0, body.length);
		this.deliver = new Deliver(body);

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

	/**
	 * @return the deliver
	 */
	public Deliver getDeliver()
	{
		return deliver;
	}

	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("\r\n---------------DeliverMessage---------------------\r\n");
		sb.append(head.toString() + "\r\n");
		sb.append(getDeliver());
		sb.append("\r\n--------------------------------------------------\r\n");
		return sb.toString();

	}

	
}
