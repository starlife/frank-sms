package com.cmcc.mm7.vasp.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MIMEMessage
{
	// private static final Log log = LogFactory.getLog(MIMEMessage.class);
	public static final String CONTENT_TYPE = "Content-Type";
	public static final String CONTENT_TRANSFER_ENCODING = "Content-Transfer-Encoding";
	public static final String CONTENT_ID = "Content-ID";
	public static final String CONTENT_LOCATION = "Content-Location";

	private byte[] bytes = null;
	private final ByteArrayOutputStream headBaos = new ByteArrayOutputStream();
	private final ByteArrayOutputStream bodyBaos = new ByteArrayOutputStream();

	private String contentType = null;
	private String charset = null;
	private String name = null;
	private String boundary = null;
	private String contentTransferEncoding = null;
	private String contentID = null;
	private String contentLocation = null;

	/**
	 * 不是一个MIME消息的时候抛出错误
	 * 
	 * @param bytes
	 * @throws IOException
	 */
	public MIMEMessage(byte[] bytes) throws IOException
	{
		this(bytes, null);
	}

	/**
	 * 修改支持MIME头包含中文
	 * 
	 * @param bytes
	 * @param charset
	 * @throws IOException
	 */
	public MIMEMessage(byte[] bytes, String charset) throws IOException
	{
		this.bytes = bytes;
		this.charset = charset;
		String mime = null;
		if (getCharset() != null)
		{
			mime = new String(bytes, Charset.forName(getCharset()));
		}
		else
		{
			mime = new String(bytes);
		}

		int index = mime.indexOf("\r\n\r\n");
		if (index == -1)
		{
			throw new IOException();
		}
		String header = mime.substring(0, index + 4);
		byte[] headBytes = null;
		if (getCharset() != null)
		{
			headBytes = header.getBytes(Charset.forName(getCharset()));
		}
		else
		{
			headBytes = header.getBytes();
		}
		headBaos.write(headBytes);
		bodyBaos
				.write(bytes, headBytes.length, bytes.length - headBytes.length);
		parseHeader();
	}

	private void parseHeader()
	{
		String contentTypeStr = getHeaderValue(CONTENT_TYPE);
		if (contentTypeStr != null)
		{
			String[] tuple = contentTypeStr.split(";");
			if (tuple.length >= 1)
			{
				setContentType(tuple[0]);
			}
			if (tuple.length >= 2)
			{
				for (int i = 1; i < tuple.length; i++)
				{
					String field = tuple[i];
					if (field.indexOf("charset") != -1)
					{
						setCharset(getValue("charset", field));
					}
					else if (field.indexOf("name") != -1)
					{
						this.setName(getValue("name", field));
					}
					else if (field.indexOf("boundary") != -1)
					{
						setBoundary(getValue("boundary", field));
					}

				}
			}
		}
		setContentTransferEncoding(getHeaderValue(CONTENT_TRANSFER_ENCODING));
		setContentID(getHeaderValue(CONTENT_ID));
		setContentLocation(getHeaderValue(CONTENT_LOCATION));
		if(getContentLocation()!=null&&getContentLocation().equals("img_18.jpg"))
		{
			//log.info(Hex.rhex(bytes));
		}
	}

	public String getHeaderValue(String key)
	{
		String header = null;
		if (getCharset() != null)
		{
			try
			{
				header = headBaos.toString(getCharset());
			}
			catch (UnsupportedEncodingException e)
			{
				// TODO Auto-generated catch block
				header = headBaos.toString();
			}
		}
		else
		{
			header = headBaos.toString();
		}

		String pattern = (new StringBuilder()).append("(").append(key).append(
				"[ ]*:[ ]*)(.*)(\r\n)").toString();
		Pattern p = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(header);
		if (m.find())
			return m.group(2);
		else
			return null;
	}

	public byte[] getData()
	{
		return bytes;
	}

	public byte[] getBody()
	{
		return bodyBaos.toByteArray();
	}
	
	public byte[] getHeader()
	{
		return headBaos.toByteArray();
	}
	
	public String toString()
	{
		if (getCharset() != null)
		{
			return new String(bytes, Charset.forName(getCharset()));
		}
		else
		{
			return new String(bytes);
		}
	}

	public String getContentType()
	{
		return contentType;
	}

	public void setContentType(String contentType)
	{
		this.contentType = contentType;
	}

	public String getCharset()
	{
		return charset;
	}

	public void setCharset(String charset)
	{
		this.charset = charset;
	}

	public String getContentTransferEncoding()
	{
		return contentTransferEncoding;
	}

	public void setContentTransferEncoding(String contentTransferEncoding)
	{
		this.contentTransferEncoding = contentTransferEncoding;
	}

	public String getContentID()
	{
		return contentID;
	}

	public void setContentID(String contentID)
	{
		this.contentID = contentID;
	}

	public String getContentLocation()
	{
		return contentLocation;
	}

	public void setContentLocation(String contentLocation)
	{
		this.contentLocation = contentLocation;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getBoundary()
	{
		return boundary;
	}

	public void setBoundary(String boundary)
	{
		this.boundary = boundary;
	}

	public static String getValue(String key, String data)
	{

		java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("("
				+ key + "=\"?)([^\"?]+)");
		java.util.regex.Matcher matcher = pattern.matcher(data);
		if (matcher.find())
			return matcher.group(2);
		else
			return null;

	}

	

}
