package com.cmcc.mm7.vasp.protocol.util;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

/**
 * 字节值打印工具
 * 
 * @author Administrator
 */
public class Hex
{
	public static byte[] rstr(String hex)
	{
		int length = hex.length();
		byte[] bHex = new byte[length / 2];
		String temp = null;
		int t = 0;
		for (int i = 0; i < length; i++)
		{
			temp = "" + hex.charAt(i) + hex.charAt(++i);
			bHex[t++] = (byte) Integer.parseInt(temp, 16);
		}
		return bHex;
	}

	public static String rhex(byte[] in)
	{
		DataInputStream data = new DataInputStream(new ByteArrayInputStream(in));
		StringBuffer sb = new StringBuffer();
		try
		{
			for (int j = 0; j < in.length; j++)
			{
				String tmp = Integer.toHexString(data.readUnsignedByte());
				if (tmp.length() == 1)
				{
					sb.append("0");
				}
				sb.append(tmp);
			}
		}
		catch (Exception ex)
		{
		}
		return sb.toString();
	}

}
