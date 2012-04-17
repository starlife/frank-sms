package com.chinamobile.cmpp3_0.protocol.message;

/**
 * @author Administrator
 */

import com.chinamobile.cmpp3_0.protocol.util.*;

public class ConnectRespMessage extends APackage implements Recv
{
	private Header head;
	private byte[] buf;

	private int Status; // int(4)

	private int ServerVersion;// byte(1)�
	/**
	 * ISMG认证码，用于鉴别ISMG。 其值通过单向MD5 hash计算得出，表示如下： AuthenticatorISMG
	 * =MD5（Status+AuthenticatorSource+shared secret），Shared secret
	 * 由中国移动与源地址实体事先商定，AuthenticatorSource为源地址实体发送给ISMG的对应消息CMPP_Connect中的值。
	 * 认证出错时，此项为空。
	 */
	private byte[] AuthenticatorServer = new byte[16];// string(16)

	public ConnectRespMessage(BasePackage pack)
	{
		System.out.println("changudddddddddddddddddddddd:"+pack.getBytes().length);
		System.out.println(Hex.rhex(pack.getBytes()));
		this.head = pack.getHead();
		this.buf = pack.getBytes();
		// init data
		int loc = 12;
		this.Status = TypeConvert.byte2int(buf, loc);
		loc += 4;
		System.arraycopy(buf, loc, AuthenticatorServer, 0, 16);
		loc += 16;
		ServerVersion = buf[loc];
	}

	public int getStatus()
	{
		return this.Status;
	}

	@Override
	public Header getHead()
	{
		return head;
	}

	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("\r\n---------------LoginRespMessage-------------------\r\n");
		sb.append(head + "\r\n");
		sb.append("Status: " + Status + "  ServerVersion :" + ServerVersion);
		sb.append("\r\n--------------------------------------------------\r\n");
		return sb.toString();
	}

	@Override
	public byte[] getBytes()
	{
		return buf;
	}

}
