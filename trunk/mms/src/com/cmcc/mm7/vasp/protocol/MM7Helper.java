package com.cmcc.mm7.vasp.protocol;

import java.io.IOException;
import java.nio.charset.Charset;

import sun.misc.BASE64Encoder;

import com.cmcc.mm7.vasp.Global;
import com.cmcc.mm7.vasp.protocol.message.MM7CancelReq;
import com.cmcc.mm7.vasp.protocol.message.MM7ReplaceReq;
import com.cmcc.mm7.vasp.protocol.message.MM7SubmitReq;
import com.cmcc.mm7.vasp.protocol.message.MM7VASPReq;
import com.cmcc.mm7.vasp.protocol.message.MM7VASPRes;
import com.cmcc.mm7.vasp.protocol.util.ByteUtil;

public class MM7Helper
{
	//private static final Log log = LogFactory.getLog(MM7Helper.class);

	public static byte[] getMM7Message(MM7VASPReq mm7VASPReq, String mmscIP,
			String mmscURL,int authmode, String userName, String password,
			boolean keepAlive, Charset charset) throws IOException
	{
		
		StringBuffer beforAuth = new StringBuffer();
		// StringBuffer afterAuth = new StringBuffer();

		//byte[] bcontent = SOAPEncoder.encodeVASPReqMessage(mm7VASPReq, charset
		//		.toString());
		byte[] bcontent = EncodeMM7.encodeMM7VASPReq(mm7VASPReq, charset);

		if (mmscURL == null)
		{
			mmscURL = "/";
		}
		beforAuth.append("POST " + mmscURL + " HTTP/1.1\r\n");
		beforAuth.append("Host:" + mmscIP + "\r\n");

		// 设置Host结束
		/** 设置ContentType，不带附件为text/xml;带附件为multipart/related */
		if (mm7VASPReq instanceof MM7SubmitReq)
		{
			MM7SubmitReq submitReq = (MM7SubmitReq) mm7VASPReq;

			if (submitReq.isContentExist())
				beforAuth
						.append("Content-Type:multipart/related; boundary=\"--NextPart_0_2817_24856\";"
								+ "type=\"text/xml\";start=\"</tnn-200102/mm7-vasp>\""
								+ "\r\n");
			else
				beforAuth.append("Content-Type:text/xml;charset=\"" + charset
						+ "\"" + "\r\n");
		}
		else if (mm7VASPReq instanceof MM7ReplaceReq)
		{
			MM7ReplaceReq replaceReq = (MM7ReplaceReq) mm7VASPReq;

			if (replaceReq.isContentExist())
				beforAuth
						.append("Content-Type:multipart/related; boundary=\"--NextPart_0_2817_24856\";"
								+ "\r\n");
			else
				beforAuth.append("Content-Type:text/xml;charset=\"" + charset
						+ "\"" + "\r\n");
		}
		else if (mm7VASPReq instanceof MM7CancelReq)
		{
			//MM7CancelReq cancelReq = (MM7CancelReq) mm7VASPReq;

			beforAuth.append("Content-Type:text/xml;charset=\"" + charset
					+ "\"" + "\r\n");
		}
		// 设置ContentType结束
		// 设置Content-Trans-Encoding
		beforAuth.append("Content-Transfer-Encoding:8bit" + "\r\n");

		// 验证头
		if(authmode==1)
		{
			beforAuth.append("Authorization:Basic "
				+ getBASE64(userName + ":" + password) + "\r\n");
		}

		// 验证后
		beforAuth.append("SOAPAction:\"\"" + "\r\n");
		beforAuth.append("MM7APIVersion:" + Global.APIVersion + "\r\n");
		/**
		 * 判断是否是长连接，若是长连接，则将Connection设为keep-alive，
		 * 否则设为close，以告诉服务器端客户端是长连接还是短连接
		 */
		if (keepAlive)
		{
			beforAuth.append("Connection: Keep-Alive" + "\r\n");
		}
		else
		{
			beforAuth.append("Connection:Close" + "\r\n");
		}
		beforAuth.append("Content-Length:" + bcontent.length + "\r\n");
		beforAuth.append("Mime-Version:1.0" + "\r\n");
		beforAuth.append("\r\n");
		
		byte[] headByte = beforAuth.toString().getBytes(charset);

		return ByteUtil.merge(headByte, bcontent);

	}

	/** 进行BASE64编码 */
	public static String getBASE64(String value)
	{
		if (value == null)
			return null;
		BASE64Encoder baseEncode = new BASE64Encoder();
		return baseEncode.encode(value.getBytes());
	}

	public static byte[] getMM7Message(MM7VASPRes mm7VASPRes, Charset charset,
			boolean keepAlive)
	{
		//byte[] bodyByte = SOAPEncoder.encodeVASPResMessage(mm7VASPRes, charset
		//		.toString());
		byte[] bodyByte = EncodeMM7.encodeMM7VASPRes(mm7VASPRes, charset);

		StringBuffer header = new StringBuffer();

		header.append("HTTP/1.1 200 OK\r\n");
		if (keepAlive)
		{
			header.append("Connection: Keep-Alive\r\n");
		}
		else
		{
			header.append("Connection: close\r\n");
		}
		header.append("Content-Type: text/xml;charset=" + charset + "\r\n");

		header.append("Content-Length: " + bodyByte.length + "\r\n");

		header.append("\r\n");
		byte[] headByte = header.toString().getBytes(charset);

		return ByteUtil.merge(headByte, bodyByte);

	}

}
