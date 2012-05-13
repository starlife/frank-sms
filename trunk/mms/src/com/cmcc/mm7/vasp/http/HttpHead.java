package com.cmcc.mm7.vasp.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class HttpHead
{
	private static final Log log = LogFactory.getLog(HttpHead.class);
	private final ByteArrayOutputStream headBaos = new ByteArrayOutputStream();
	private String httpVersion;
	private int statusCode;
	private String reasonPhrase;

	public HttpHead()
	{

	}

	public boolean recvData(InputStream input)
	{
		// 先读http头信息
		if (input == null)
		{
			log.warn("input is null");
			return false;
		}
		try
		{
			while (true)
			{
				int i = input.read();
				headBaos.write(i);
				if (isComplete(headBaos.toString()))
					break;
				if (i != -1)
					continue;
				log.warn("读取包时未读完遇到-1，失败");
				break;
			}
		}
		catch (IOException ex)
		{
			log.error("读取包头时错误", ex);
			return false;
		}
		if (!parseStatusLine())
		{
			log.warn("parseStatusLine fails");
			return false;
		}
		return true;
	}



	private boolean parseStatusLine()
	{
		String temp = headBaos.toString();
		int index = temp.indexOf("\r\n");
		if (index <= 0)
		{
			return false;
		}
		String[] tuple = temp.substring(0, index).split(" ");
		if (tuple.length != 3)
		{
			return false;
		}
		httpVersion = tuple[0].trim();

		try
		{
			statusCode = Integer.parseInt(tuple[1].trim());
		}
		catch (Exception ex)
		{
			log.error("parse statusCode失败", ex);
			return false;
		}
		reasonPhrase = tuple[2].trim();
		return true;

	}
	
	public String getHeaderValue(String key)
	{
		String tkey = "";
		for (int i = 0; i < key.length(); i++)
			tkey = (new StringBuilder()).append(tkey).append("[").append(
					key.substring(i, i + 1).toLowerCase()).append(
					key.substring(i, i + 1).toUpperCase()).append("]")
					.toString();

		String pattern = (new StringBuilder()).append("(").append(tkey).append(
				"[ ]*:[ ]*)(.*)(\r\n)").toString();
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(headBaos.toString());
		if (m.find())
			return m.group(2);
		else
			return null;
	}
	
	public static boolean isComplete(String data)
	{
		return data.indexOf("\r\n\r\n") != -1;
	}

	public static boolean isComplete(StringBuffer data)
	{
		return data.indexOf("\r\n\r\n") != -1;
	}

	public static boolean isComplete(ByteBuffer bytebuf)
	{
		java.nio.ByteBuffer temp = bytebuf.asReadOnlyBuffer();
		temp.flip();
		String data = bytebuf.toString();
		return data.indexOf("\r\n\r\n") != -1;
	}

	public byte[] getData()
	{
		return headBaos.toByteArray();
	}

	public String toString()
	{
		return headBaos.toString();
	}

	public String getHttpVersion()
	{
		return httpVersion;
	}

	public int getStatusCode()
	{
		return statusCode;
	}

	public String getReasonPhrase()
	{
		return reasonPhrase;
	}
	

}
