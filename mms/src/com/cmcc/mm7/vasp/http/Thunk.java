package com.cmcc.mm7.vasp.http;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cmcc.mm7.vasp.protocol.util.Hex;

public class Thunk
{
	private static final Log log = LogFactory.getLog(HttpResponse.class);
	private final ByteArrayOutputStream baos = new ByteArrayOutputStream();

	public boolean recvData(InputStream input)
	{
		// 先读http头信息
		if (input == null)
		{
			log.warn("input is null");
			return false;
		}
		// 表示一块thunked数据
		ByteArrayOutputStream thunked = new ByteArrayOutputStream();
		try
		{
			while (true)
			{
				thunked.reset();// 重新初始化
				while (true)
				{
					int i = input.read();
					if (i == -1)
					{
						log.warn("读取包时未读完遇到-1，失败");
						return false;
					}
					thunked.write(i);
					// 判断是否遇到了thunk头的结束，如果是，那么读thunk包内容
					if (isThunkedHeaderComplete(thunked.toString()))
					{
						break;
					}
				}
				int thunkedLen = getThunkedLength(thunked.toString());
				if (thunkedLen == 0)
				{
					// thunked包长度为0，有可能是结尾包
					byte[] temp = new byte[2];
					input.read(temp);// 读取”\r\n“
					thunked.write(temp);
					if (isLastChunked(thunked.toString()))
					{
						return true;
					}
					else
					{
						return false;
					}

				}
				// 读取thunked包内容
				this.readThunkedData(input, thunkedLen);
				// 下面读取”\r\n“,丢弃
				byte[] temp = new byte[2];
				input.read(temp);
			}

		}
		catch (Exception ex)
		{
			log.error("读取包头时错误", ex);
			return false;
		}

	}

	private boolean readThunkedData(InputStream input, int length)
	{
		try
		{
			byte tbytebuf[] = new byte[length];
			for (int temp = input.read(tbytebuf); temp != length; temp = input
					.read(tbytebuf, temp, length - temp)
					+ temp)
				;
			baos.write(tbytebuf);
		}
		catch (Exception ex)
		{
			log.error(null, ex);
			return false;
		}
		return true;
	}

	public byte[] getData()
	{
		return baos.toByteArray();
	}

	/**
	 * 是否读完thunked头
	 * 
	 * @param thunkedHeader
	 * @return
	 */
	public static boolean isThunkedHeaderComplete(String thunkedHeader)
	{
		return thunkedHeader.indexOf("\r\n") != -1;
	}

	/**
	 * 是否是最后一个thunked
	 * 
	 * @param thunked
	 * @return
	 */
	public static boolean isLastChunked(String thunked)
	{
		return thunked.indexOf("\r\n\r\n") != -1;
	}

	/**
	 * 得到thunked包的内容长度
	 * 
	 * @param thunkedHeader
	 * @return
	 */
	public static int getThunkedLength(String thunkedHeader)
	{
		int length = 0;
		int index = thunkedHeader.indexOf("\r\n");
		if (index <= 0)
		{
			return 0;
		}
		String hex = thunkedHeader.substring(0, index).trim();
		if (hex.length() > 8)
		{
			//
			return 0;
		}
		byte[] bytes = Hex.rstr(hex);
		for (int i = 0; i<bytes.length; i++)
		{
			System.out.println(bytes[i]);
			length += bytes[i]<<((bytes.length-1-i)*8);
		}
		return length;
	}
	
	public static void main(String[] args)
	{
		System.out.println(getThunkedLength("0125\r\n"));
		System.out.println(Integer.toHexString(293));
		System.out.println((1<<8)+(2<<4)+5);
	}

}
