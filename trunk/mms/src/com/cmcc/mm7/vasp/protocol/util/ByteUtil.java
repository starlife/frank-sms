package com.cmcc.mm7.vasp.protocol.util;

import java.util.ArrayList;
import java.util.List;

/**
 * 浜岃繘鍒跺拰鍏朵粬绫诲瀷锛坰hort,char,int,long,float,double,string锛夎浆鎹㈠伐鍏风被
 * 
 * @author Administrator
 */
public class ByteUtil
{

	public static byte[] short2byte(short i)
	{
		byte[] b = new byte[2];
		short2byte(i, b, 0);
		return b;
	}

	public static void short2byte(short n, byte[] buf, int offset)
	{
		buf[offset] = (byte) (n >> 8);
		buf[offset + 1] = (byte) n;
	}

	public static short byte2short(byte[] b)
	{
		return byte2short(b, 0);
	}

	public static short byte2short(byte[] b, int offset)
	{
		return (short) (b[offset + 1] & 0xff | (b[offset + 0] & 0xff) << 8);
	}

	public static byte[] int2byte(int n)
	{
		byte[] b = new byte[4];
		int2byte(n, b, 0);
		return b;
	}

	public static void int2byte(int n, byte[] buf, int offset)
	{
		buf[offset] = (byte) (n >> 24);
		buf[offset + 1] = (byte) (n >> 16);
		buf[offset + 2] = (byte) (n >> 8);
		buf[offset + 3] = (byte) n;
	}

	public static int byte2int(byte b[])
	{
		return byte2int(b, 0);
	}

	public static int byte2int(byte b[], int offset)
	{
		return b[offset + 3] & 0xff | (b[offset + 2] & 0xff) << 8
				| (b[offset + 1] & 0xff) << 16 | (b[offset] & 0xff) << 24;
	}

	public static byte[] long2byte(long n)
	{
		byte[] b = new byte[8];
		long2byte(n, b, 0);
		return b;
	}

	public static void long2byte(long n, byte[] buf, int offset)
	{

		buf[offset] = (byte) (n >> 56);
		buf[offset + 1] = (byte) (n >> 48);
		buf[offset + 2] = (byte) (n >> 40);
		buf[offset + 3] = (byte) (n >> 32);
		buf[offset + 4] = (byte) (n >> 24);
		buf[offset + 5] = (byte) (n >> 16);
		buf[offset + 6] = (byte) (n >> 8);
		buf[offset + 7] = (byte) n;

	}

	/**
	 * 鎷疯礉瀛楃涓瞫rc鐨刲en涓瓧鑺傜殑闀垮害锛屽鏋渓en<src.lenght()鍒欐嫹璐濋儴鍒?
	 * 锛屽鏋渓en>=src.length()鍏ㄦ嫹
	 */
	public static byte[] getBytes(String src, int len)
	{

		byte[] buf = new byte[len];
		byte[] temp = src.getBytes();
		if (len > temp.length)
		{
			System.arraycopy(temp, 0, buf, 0, temp.length);
		}
		else
		{
			System.arraycopy(temp, 0, buf, 0, len);
		}
		return buf;
	}

	public static byte[] getBytes(String[] src, int len)
	{

		byte[] buf = new byte[len * src.length];
		for (int i = 0; i < src.length; i++)
		{
			byte[] temp = src[i].getBytes();
			System.arraycopy(temp, 0, buf, i * len, temp.length);
		}
		return buf;
	}

	public static byte[] merge(byte[] byte1, byte[] byte2)
	{
		byte[] buf = new byte[byte1.length + byte2.length];
		System.arraycopy(byte1, 0, buf, 0, byte1.length);
		System.arraycopy(byte2, 0, buf, byte1.length, byte2.length);
		return buf;
	}

	/**
	 * 字符串split的byte实现形式
	 * 
	 * @param bytes
	 *            字符串的byte
	 * @param boundary
	 *            分隔符的byte
	 * @return 各个段的byte
	 */
	public static List<byte[]> split(byte[] bytes, byte[] boundary)
	{
		List<byte[]> tupleList = new ArrayList<byte[]>();
		List<Integer> posList = new ArrayList<Integer>();
		for (int i = 0, j = 0; i <= bytes.length; i++)
		{
			if (i == bytes.length)
			{
				if (j == boundary.length)
				{
					posList.add(i - boundary.length);
				}
				break;
			}
			if (j == boundary.length)
			{
				// 找到，b[0..j-1] 和a[i-j..i-1]都相等
				posList.add(i - boundary.length);
				j = 0;
			}
			if (bytes[i] == boundary[j])
			{
				j++;
			}
			else
			{
				j = 0;
			}
		}
		int bpos = 0;
		int epos = 0;
		for (Integer i : posList)
		{
			epos = i;
			if (epos > bpos)
			{
				byte[] temp = new byte[epos - bpos];
				System.arraycopy(bytes, bpos, temp, 0, temp.length);
				tupleList.add(temp);
				bpos = i + boundary.length;
			}
		}
		if (bpos < bytes.length)
		{
			epos = bytes.length;
			byte[] temp = new byte[epos - bpos];
			System.arraycopy(bytes, bpos, temp, 0, temp.length);
			// System.out.println(bpos+":"+epos);
			tupleList.add(temp);
		}
		return tupleList;
	}

}
