/**
 * File Name:MM7VASPReq.java Company: 中国移动集团公司 Date : 2004-2-2
 */

package com.cmcc.mm7.vasp.protocol.message;

public class MM7VASPReq extends MM7Message
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 发送次数
	 */
	private int times = 0;
	
	private byte[] bytes=null;

	public int getTimes()
	{
		return times;
	}

	public void addTimes()
	{
		times += 1;
	}

	public byte[] getBytes()
	{
		return bytes;
	}

	public void setBytes(byte[] bytes)
	{
		this.bytes = bytes;
	}

}
