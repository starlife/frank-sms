package com.chinamobile.cmpp3_0.protocol.message;

import com.chinamobile.cmpp3_0.protocol.util.*;

public class ConnectMessage extends APackage implements Send
{
	public static final int LENGTH = 39;// 包长度
	private String spid;
	private String secret;
	private int version;

	private Header head;
	private byte[] buf = new byte[ConnectMessage.LENGTH];

	// 初始化登陆信息
	public ConnectMessage(String spid, String secret, int version)
	{
		this.spid = spid;
		this.secret = secret;
		this.version = version;
		head = new Header(buf.length, CommandID.CMPP_CONNECT, Header
				.allocSequenceID());
		/* buf 赋值 */
		String timestamp = DateUtil.getTimeStamp();
		int loc = 0;
		System.arraycopy(head.getBytes(), 0, buf, 0, 12);// copy header
		loc += 12;
		System.arraycopy(spid.getBytes(), 0, buf, loc, spid.length());// copy
		// clientid
		// (6)
		loc += 6;
		byte[] authClientByte = getAuthenticatorClient(spid, secret, timestamp);
		System.arraycopy(authClientByte, 0, buf, loc, authClientByte.length);// copy
		// authClient
		// (16)
		loc += 16;// authbyte 长度
		buf[loc++] = (byte) version; // version (1)
		TypeConvert.int2byte(Integer.parseInt(timestamp), buf, loc);// timeStamp
		// (4)

	}

	private byte[] getAuthenticatorClient(String clientId, String sharedKey,
			String timestamp)
	{
		byte[] bytes;
		int bufLen;
		int loc = 0;
		bufLen = clientId.length() + sharedKey.length() + 19;
		bytes = new byte[bufLen];
		System.arraycopy(clientId.getBytes(), 0, bytes, 0, clientId.length());// copy
		// clientId
		loc = clientId.length() + 9;// copy 9 byte \x00

		System.arraycopy(sharedKey.getBytes(), 0, bytes, loc, sharedKey
				.length());// copy
		// sharedKey
		loc += sharedKey.length();

		System.arraycopy(timestamp.getBytes(), 0, bytes, loc, 10);// copy
		// timestamp
		return MD5.compile(bytes);
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
		sb.append("\r\n---------------ConnectMessage---------------------\r\n");
		sb.append(head.toString()).append("\r\n");
		sb.append("SPID:").append(spid).append("\r\n");
		sb.append("Secret:").append(secret).append("\r\n");
		sb.append("Version:").append(version).append("\r\n");
		sb.append("\r\n--------------------------------------------------\r\n");
		return sb.toString();

	}

}
