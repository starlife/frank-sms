package com.cmcc.mm7.vasp.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class HttpHeadRequest
{
	private static final Log log = LogFactory.getLog(HttpHeadRequest.class);
	private final ByteArrayOutputStream headBaos = new ByteArrayOutputStream();
	private String method;
	private String url;
	private String httpVersion;

	public HttpHeadRequest()
	{

	}

	public boolean recvData(InputStream input) throws IOException
	{
		// �ȶ�httpͷ��Ϣ
		if (input == null)
		{
			log.warn("input is null");
			return false;
		}	
		while (true)
		{
			// byte[] b=new byte[1];
			int i = input.read();
			if (i == -1)
			{
				// socket����һ�˹ر�
				log.error("��ȡ��ʱδ��������-1");
				throw new  IOException("peer socket is closed");
			}
			headBaos.write(i);
			if (isComplete(headBaos.toString()))
			{
				break;
			}

		}
		if (!parseRequestLine())
		{
			log.warn("parseRequestLine fails");
			return false;
		}
		return true;
	}

	private boolean parseRequestLine()
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
		this.method = tuple[0].trim();
		this.url = tuple[1].trim();
		this.httpVersion = tuple[2].trim();
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

	public String getMethod()
	{
		return method;
	}

	public String getUrl()
	{
		return url;
	}

}
