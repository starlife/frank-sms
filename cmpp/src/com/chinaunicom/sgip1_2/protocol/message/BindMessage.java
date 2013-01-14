/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */

package com.chinaunicom.sgip1_2.protocol.message;

import com.chinaunicom.sgip1_2.protocol.util.ByteConvert;

/**
 * @author Administrator
 */
public class BindMessage extends APackage implements Send, Recv
{

	public static final int LENGTH = 61;// 包长度

	private int LoginType;// integer(1) 登录类型
	private String LoginName;// text(16) 登录用户
	private String LoginPass;// //text(16) 登录密码
	private String Reserve = "";// //text(8) 保留字

	private Header head;
	private byte[] buf = new byte[LENGTH];

	public BindMessage(String nodeid, int loginType, String loginName,
			String loginPass)
	{
		this.LoginType = loginType;
		this.LoginName = loginName;
		this.LoginPass = loginPass;
		head = new Header(Header.LENGTH, CommandID.SGIP_BIND, new Sequence(
				nodeid));
		/* buf 赋值 */
		int loc = 0;
		System.arraycopy(head.getBytes(), 0, buf, loc, Header.LENGTH);// copy
																		// header
		loc += Header.LENGTH;
		buf[loc++] = (byte) loginType; // copy loginType

		System.arraycopy(ByteConvert.getBytes(loginName, 16), 0, buf, loc, 16);
		loc += 16;
		System.arraycopy(ByteConvert.getBytes(loginPass, 16), 0, buf, loc, 16);
		loc += 16;
		System.arraycopy(ByteConvert.getBytes(Reserve, 8), 0, buf, loc, 8);
	}

	public BindMessage(BasePackage pack)
	{
		this.head = pack.getHead();
		/* buf赋值 */
		buf = pack.getBytes();
		this.setTimeStamp(pack.getTimeStamp());// 设置包时间
		//
		int loc = Header.LENGTH;
		this.LoginType = buf[loc];
		loc++;
		byte[] temp = new byte[16];
		System.arraycopy(buf, loc, temp, 0, temp.length);
		this.LoginName = new String(temp).trim();
		System.arraycopy(buf, loc, temp, 0, temp.length);
		this.LoginPass = new String(temp).trim();
		temp = new byte[8];
		System.arraycopy(buf, loc, temp, 0, temp.length);
		this.Reserve = new String(temp).trim();

	}

	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb
				.append("\r\n---------------BindMessage----------------------------\r\n");
		sb.append(getHead().toString() + "\r\n");
		sb.append("LoginType:").append(LoginType).append("\t");
		sb.append("LoginName:").append(LoginName).append("\t");
		sb.append("LoginName:").append(LoginName).append("\r\n");
		sb.append("Reserve:").append(Reserve);
		sb
				.append("\r\n------------------------------------------------------\r\n");
		return sb.toString();

	}

	@Override
	public byte[] getBytes()
	{
		// TODO Auto-generated method stub
		return buf;
	}

	@Override
	public Header getHead()
	{
		// TODO Auto-generated method stub
		return head;
	}

	public int getLoginType()
	{
		return LoginType;
	}

	public String getLoginName()
	{
		return LoginName;
	}

	public String getLoginPass()
	{
		return LoginPass;
	}

}
