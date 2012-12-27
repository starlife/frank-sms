package com.cmcc.mm7.vasp.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.Charset;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class HttpRequest
{
	private static final Log log = LogFactory.getLog(HttpRequest.class);
	private final HttpHeadRequest httpHead = new HttpHeadRequest();
	private final ByteArrayOutputStream bodyBaos = new ByteArrayOutputStream();

	public HttpRequest()
	{

	}

	public boolean recvData(InputStream input) throws IOException
	{
		// 先读http头信息
		if (!httpHead.recvData(input))
		{
			log.info("读取包头数据失败");
			return false;
		}
		log.debug("接收包头完成");
		// 如果有包体，读取包体

		String contentLen = this.getHeaderValue("Content-Length");
		if (contentLen != null)
		{
			int length = Integer.parseInt(contentLen);
			// length=length-(httpHead.getData().length-(httpHead.toString().indexOf("\r\n\r\n")+4));
			byte tbytebuf[] = new byte[length];
			for (int temp = input.read(tbytebuf); temp != length; temp = input
					.read(tbytebuf, temp, length - temp)
					+ temp)
				;
			bodyBaos.write(tbytebuf);
		}
		else
		{
			// 这里添加对truck传输类型的处理
			String transferEncoding = this.getHeaderValue("transfer-encoding");
			if (transferEncoding.equalsIgnoreCase("chunked"))
			{
				Thunk thunk = new Thunk();
				if (!thunk.recvData(input))
				{
					log.warn("尝试接收chunked类型数据失败");
					return false;
				}
				bodyBaos.write(thunk.getData());
				return true;
			}
			// /////////-end

			for (int j = input.read(); j != -1; j = input.read())
				bodyBaos.write(j);

		}
		return true;

	}

	public boolean recvData(Socket socket, int timeout)
	{
		try
		{
			socket.setSoTimeout(timeout);
			InputStream input = socket.getInputStream();
			return recvData(input);
		}
		catch (SocketException ex)
		{
			log.error(null, ex);
			return false;
		}
		catch (IOException ex)
		{
			log.error(null, ex);
		}
		return false;
	}

	public byte[] getData()
	{
		ByteArrayOutputStream buf = new ByteArrayOutputStream();
		try
		{
			buf.write(httpHead.getData());
			buf.write(bodyBaos.toByteArray());
		}
		catch (IOException ex)
		{
			log.error(null, ex);
		}
		return buf.toByteArray();
	}

	public byte[] getBody()
	{
		return bodyBaos.toByteArray();
	}

	public String getHeaderValue(String key)
	{
		return httpHead.getHeaderValue(key);
	}

	public String getHttpVersion()
	{
		return httpHead.getHttpVersion();
	}

	public String getHeader()
	{
		return httpHead.toString();
	}

	public String getUrl()
	{
		return httpHead.getUrl();
	}

	public String getMethod()
	{
		return httpHead.getMethod();
	}

	public String toString()
	{
		byte[] bytes = getData();
		return new String(bytes);
	}

	public String toString(Charset charset)
	{
		byte[] bytes = getData();
		return new String(bytes, charset);
	}

}
