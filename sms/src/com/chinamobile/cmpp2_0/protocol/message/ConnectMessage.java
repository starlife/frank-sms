package com.chinamobile.cmpp2_0.protocol.message;

import com.chinamobile.cmpp2_0.protocol.util.*;

public class ConnectMessage extends APackage implements Send
{
	public static final int LENGTH = 39;// 包长度
	private String account;
	private String secret;
	private int version;

	private Header head;
	private byte[] buf = new byte[ConnectMessage.LENGTH];

	// 初始化登陆信息 登陆用户名、密码、版本
	public ConnectMessage(String account, String secret, int version)
	{
		this.account = account;
		this.secret = secret;
		this.version = version;
		head = new Header(ConnectMessage.LENGTH, CommandID.CMPP_CONNECT, Header
				.allocSequenceID());
		/* buf 赋值 */
		int loc = 0;
		byte[] temp=null;
		System.arraycopy(head.getBytes(), 0, buf,loc,Header.LENGTH);// copy header
		loc += Header.LENGTH;
		temp=account.getBytes();
		System.arraycopy(temp, 0, buf, loc,temp.length);// copy
		// clientid
		// (6)
		loc += 6;
		String timestamp = DateUtil.getTimeStamp("MMddHHmmss");
		byte[] authClientByte = getAuthenticatorClient(account, secret, timestamp);
		System.arraycopy(authClientByte, 0, buf, loc, authClientByte.length);// copy
		// authClient
		// (16)
		loc += 16;// authbyte 长度
		buf[loc++] = (byte) version; // version (1)
		ByteConvert.int2byte(Integer.parseInt(timestamp), buf, loc);// timeStamp
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
	
	public String getSrvVersion(int serverVersion)
	{
		String str= Integer.toHexString(serverVersion);
		if(str.length()>1)
		{
			str="v"+str.substring(0,1)+"."+str.substring(1);
		}
		return str;
	}
	
	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("\r\n---------------ConnectMessage---------------------\r\n");
		sb.append(head.toString()).append("\r\n");
		sb.append("Source_Addr:").append(account).append("\t");
		sb.append("Secret:").append(secret).append("\t");
		sb.append("Version:").append(getSrvVersion(version));
		sb.append("\r\n--------------------------------------------------\r\n");
		return sb.toString();

	}

}
