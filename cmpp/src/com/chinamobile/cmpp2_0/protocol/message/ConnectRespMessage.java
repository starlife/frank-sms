package com.chinamobile.cmpp2_0.protocol.message;

/**
 * @author Administrator
 */

public class ConnectRespMessage extends APackage implements Recv
{
	public static final int LENGTH = 30;// 包长度
	private Header head;
	private byte[] buf;

	private int status; // int(1)

	private int serverVersion;// byte(1)�
	/**
	 * ISMG认证码，用于鉴别ISMG。 其值通过单向MD5 hash计算得出，表示如下： AuthenticatorISMG
	 * =MD5（Status+AuthenticatorSource+shared secret），Shared secret
	 * 由中国移动与源地址实体事先商定，AuthenticatorSource为源地址实体发送给ISMG的对应消息CMPP_Connect中的值。
	 * 认证出错时，此项为空。
	 */
	private byte[] AuthenticatorServer = new byte[16];// string(16)

	public ConnectRespMessage(BasePackage pack)
	{
		this.head = pack.getHead();
		this.buf = pack.getBytes();
		// init data
		int loc = 12;
		this.status = buf[loc];
		loc += 1;
		System.arraycopy(buf, loc, AuthenticatorServer, 0, 16);
		loc += 16;
		serverVersion = buf[loc];
	}

	public int getStatus()
	{
		return this.status;
	}

	public String getStatusString(int status)
	{
		switch (status)
		{
			case 0:
				return "正确(0)";
			case 1:
				return "消息结构错(1)";
			case 2:
				return "非法源地址(2)";
			case 3:
				return "认证错(3)";
			case 4:
				return "版本太高(4)";
			default:
				return "其他错误(" + status + ")";
		}
	}

	public String getSrvVersion(int serverVersion)
	{
		String str = Integer.toHexString(serverVersion);
		if (str.length() > 1)
		{
			str = "v" + str.substring(0, 1) + "." + str.substring(1);
		}
		return str;
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

	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb
				.append("\r\n---------------ConnectRespMessage-------------------\r\n");
		sb.append(head + "\r\n");
		sb.append("Status: " + this.getStatusString(status)
				+ "  ServerVersion :" + this.getSrvVersion(serverVersion));
		sb
				.append("\r\n----------------------------------------------------\r\n");
		return sb.toString();
	}

}
