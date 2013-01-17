/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */

package com.chinaunicom.sgip1_2.protocol.message;

/**
 * @author Administrator 只包含消息头的消息
 */
public class SGIPMessage extends APackage
{
	private Header head = null;

	SGIPMessage(int packageLength, int commandId, Sequence sequenceId)
	{
		head = new Header(packageLength, commandId, sequenceId);
	}

	SGIPMessage(BasePackage pack)
	{
		this.head = pack.getHead();
		this.setTimeStamp(pack.getTimeStamp());// 设置包时间

	}

	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("\r\n--------------------")
				.append(getClass().getSimpleName()).append(
						"--------------------\r\n");
		sb.append(getHead().toString());
		sb
				.append("\r\n------------------------------------------------------------\r\n");
		return sb.toString();
	}

	@Override
	public byte[] getBytes()
	{
		// TODO Auto-generated method stub
		return head.getBytes();
	}

	@Override
	public Header getHead()
	{
		// TODO Auto-generated method stub
		return head;
	}

}
