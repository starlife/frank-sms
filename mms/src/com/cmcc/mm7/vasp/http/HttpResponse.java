package com.cmcc.mm7.vasp.http;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.Charset;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cmcc.mm7.vasp.protocol.util.Hex;

public class HttpResponse
{
	private static final Log log = LogFactory.getLog(HttpResponse.class);
	private final HttpHeadResponse httpHead = new HttpHeadResponse();
	private final ByteArrayOutputStream bodyBaos = new ByteArrayOutputStream();

	public HttpResponse()
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
		// 如果有包体，读取包体

		String contentLen = this.getHeaderValue("Content-Length");
		if (contentLen != null)
		{
			int length = Integer.parseInt(contentLen);
			//length=length-(httpHead.getData().length-(httpHead.toString().indexOf("\r\n\r\n")+4));	
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

	public int getStatusCode()
	{
		return httpHead.getStatusCode();
	}

	public String getReasonPhrase()
	{
		return httpHead.getReasonPhrase();
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

	public static void main(String[] args) throws IOException
	{
		log.debug("本地接收前");
		byte[] bytes = Hex
				.rstr("485454502f312e3120323030204f4b0d0a5365727665723a20526573696e2f332e302e31340d0a534f4150416374696f6e3a2022220d0a782d6875617765692d6d6d73632d7469643a20310d0a782d6875617765692d6d6d73632d746f3a2031333737373830323338360d0a782d6875617765692d6d6d73632d636f64653a20313030300d0a436f6e74656e742d547970653a20746578742f786d6c3b636861727365743d225554462d38220d0a436f6e74656e742d4c656e6774683a203538320d0a446174653a205361742c203132204d617920323031322030383a30323a343020474d540d0a0d0a3c3f786d6c2076657273696f6e3d22312e302220656e636f64696e673d225554462d38223f3e3c656e763a456e76656c6f706520786d6c6e733a656e763d22687474703a2f2f736368656d61732e786d6c736f61702e6f72672f736f61702f656e76656c6f70652f223e3c656e763a4865616465723e3c6d6d373a5472616e73616374696f6e494420786d6c6e733a6d6d373d22687474703a2f2f7777772e336770702e6f72672f6674702f53706563732f617263686976652f32335f7365726965732f32332e3134302f736368656d612f52454c2d362d4d4d372d312d302220656e763a6d757374556e6465727374616e643d2231223e313c2f6d6d373a5472616e73616374696f6e49443e3c2f656e763a4865616465723e3c656e763a426f64793e3c5375626d697452737020786d6c6e733d22687474703a2f2f7777772e336770702e6f72672f6674702f53706563732f617263686976652f32335f7365726965732f32332e3134302f736368656d612f52454c2d362d4d4d372d312d30223e3c4d4d3756657273696f6e3e362e332e303c2f4d4d3756657273696f6e3e3c5374617475733e3c537461747573436f64653e313030303c2f537461747573436f64653e3c537461747573546578743ee58f91e98081e68890e58a9f3c2f537461747573546578743e3c2f5374617475733e3c4d65737361676549443e3035313231363032343139313030363430313135373c2f4d65737361676549443e3c2f5375626d69745273703e3c2f656e763a426f64793e3c2f656e763a456e76656c6f70653e");

		HttpResponse http = new HttpResponse();
		ByteArrayInputStream input = new ByteArrayInputStream(bytes);
		if (http.recvData(input))
		{
			log.debug("接收hou");
			System.out.println(http.toString() + "#");
			System.out.println(http.getHeaderValue("content-length"));
			// System.out.println(http.getHttpVersion()+
			// ","+http.getStatusCode()+","+http.getReasonPhrase());
			System.out.println(http.toString(Charset.forName("UTF-8")));
		}
		else
		{
			System.out.println("false");
		}
	}

}
