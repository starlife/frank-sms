/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */

package com.chinaunicom.sgip1_2.protocol.message;

import com.chinaunicom.sgip1_2.protocol.util.ByteConvert;

/**
 * @author Administrator 该类是一个通用的回应包类 body中包含result和reserve字段
 */
public class CommonRespMessage extends SGIPMessage
{
	public static final int LENGTH = 29;// 包长度
	private int Result = 0; // integer(1) 结果
	private String Reserve = ""; // text(8) 保留字

	private byte[] buf = new byte[LENGTH];

	CommonRespMessage(int commandId,Sequence seq, int result, String reserve)
	{
		super(LENGTH, commandId,seq);
		/* buf 赋值 */
		this.Result = result;
		this.Reserve = reserve;
		int loc = 0;
		System.arraycopy(getHead().getBytes(), 0, buf, loc, Header.LENGTH);// copy
		// header
		loc += Header.LENGTH;
		buf[loc++] = (byte) Result; // copy Result
		System.arraycopy(ByteConvert.getBytes(Reserve, 8), 0, buf, loc, 8);
	}

	CommonRespMessage(BasePackage pack)
	{

		super(pack);
		/* buf赋值 */
		buf = pack.getBytes();

		int loc = Header.LENGTH;
		this.Result = buf[loc];
		loc++;
		byte[] temp = new byte[8];
		System.arraycopy(buf, loc, temp, 0, temp.length);
		this.Reserve = new String(temp).trim();
	}

	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("\r\n--------------------")
				.append(getClass().getSimpleName()).append(
						"--------------------\r\n");
		sb.append(getHead().toString() + "\r\n");
		sb.append("Result  : ").append(Result).append("\t");
		sb.append("Reserve : ").append(Reserve);
		sb
				.append("\r\n------------------------------------------------------------\r\n");
		return sb.toString();
	}

	@Override
	public byte[] getBytes()
	{
		// TODO Auto-generated method stub
		return buf;
	}

	public int getResult()
	{
		return Result;
	}

}
