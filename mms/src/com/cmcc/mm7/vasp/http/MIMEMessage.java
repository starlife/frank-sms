package com.cmcc.mm7.vasp.http;

import java.io.ByteArrayOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MIMEMessage
{
	private static final Log log = LogFactory.getLog(HttpHeadRequest.class);
	private byte[] bytes = null;
	private final ByteArrayOutputStream headBaos = new ByteArrayOutputStream();
	private final ByteArrayOutputStream bodyBaos = new ByteArrayOutputStream();

	private String contentType = null;
	private String charset = null;
	private String contentTransferEncoding = null;
	private String contentID = null;
	private String contentLocation = null;

	public MIMEMessage(byte[] bytes)
	{
		this.bytes = bytes;
		String mime = new String(bytes);
		int index = mime.indexOf("\r\n\r\n");
		headBaos.write(bytes, 0, index + 4);
		bodyBaos.write(bytes, index + 4, bytes.length);
		parseHeader();
	}

	private void parseHeader()
	{
		String contentTypeStr = getHeaderValue("Content-Type");
		if (contentTypeStr != null)
		{
			String[] tuple = contentTypeStr.split(";");
			if (tuple.length == 1)
			{
				contentType = tuple[0];
			}
			else if (tuple.length == 2)
			{
				contentType = tuple[0];
				charset = tuple[1];
			}
		}
		contentTransferEncoding = getHeaderValue("Content-Transfer-Encoding");
		this.contentID = getHeaderValue("Content-ID");
		this.contentLocation = getHeaderValue("Content-Location");
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

	public byte[] getData()
	{
		return bytes;
	}

	public byte[] getBody()
	{
		return bodyBaos.toByteArray();
	}

	public String toString()
	{
		return new String(bytes);
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

}
