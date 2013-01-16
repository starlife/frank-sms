/**
 * File Name:SOAPDecoder.java Company: 中国移动集团公司 Date : 2004-2-12
 */

package com.cmcc.mm7.vasp.protocol;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import sun.misc.BASE64Decoder;

import com.cmcc.mm7.vasp.common.MMConstants;
import com.cmcc.mm7.vasp.common.MMContent;
import com.cmcc.mm7.vasp.common.MMContentType;
import com.cmcc.mm7.vasp.http.MIMEMessage;
import com.cmcc.mm7.vasp.protocol.message.MM7DeliverReq;
import com.cmcc.mm7.vasp.protocol.message.MM7RSReq;
import com.cmcc.mm7.vasp.protocol.message.MM7RSRes;
import com.cmcc.mm7.vasp.protocol.util.ByteUtil;

public class DecodeMM7
{

	private static final Log log = LogFactory.getLog(DecodeMM7.class);

	public static MM7RSRes decodeResMessage(byte[] xmlByte, Charset charset)
	{
		return SOAPDecoder.parseResXML(xmlByte);
	}

	/**
	 * 把收到的消息进行解码
	 * 
	 * @param bodyByte
	 *            消息体字节串
	 * @param charset
	 *            默认编码
	 * @return MM7RSReq消息
	 */
	public static MM7RSReq decodeReqMessage(byte[] bodyByte, String charset,
			String boundary)
	{
		MM7RSReq req = new MM7RSReq();
		try
		{
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			baos.write(bodyByte);

			String bodyMessage = null;
			if (charset == null)
			{
				bodyMessage = baos.toString();
			}
			else
			{
				bodyMessage = baos.toString(charset);

			}
			int xmlbeg = bodyMessage.indexOf(MMConstants.BEGINXMLFLAG);
			int xmlend = bodyMessage.indexOf(MMConstants.ENDXMLFLAG)
					+ MMConstants.ENDXMLFLAG.length();
			String xmlMessage = bodyMessage.substring(xmlbeg, xmlend);
			req = SOAPDecoder.parseReqXML(xmlMessage.getBytes(charset));

			if (boundary != null && (req instanceof MM7DeliverReq)
					&& (baos.size() > xmlend))
			{
				// 解析附件 附件是一个multipart/mixed;
				parseAttachment(baos.toByteArray(), charset, boundary,
						(MM7DeliverReq) req);
			}

		}
		catch (Exception ex)
		{
			log.error(null, ex);
		}

		return req;

	}

	/**
	 * 解析附件，把解析得到的MMContent对象添加到MM7DeliverReq消息中
	 * 
	 * @param bodyBytes
	 * @param charset
	 * @param boundary
	 * @param req
	 * @throws IOException
	 */
	private static void parseAttachment(byte[] bodyBytes, String charset,
			String boundary, MM7DeliverReq req) throws IOException
	{
		MMContent deliverContent = new MMContent();
		deliverContent.setContentType(MMConstants.ContentType.MULTIPART_MIXED);
		List<byte[]> list = splitMime(bodyBytes, boundary);
		for (int i = 0; i < list.size(); i++)
		{
			byte[] part = list.get(i);
			if (new String(part).indexOf("\r\n\r\n") == -1)
			{
				// 错误的部分
				continue;
			}

			MIMEMessage mime = new MIMEMessage(part, charset);
			if (mime.getContentType() != null
					&& mime.getContentType().equals(
							MMConstants.ContentType.MULTIPART_MIXED.toString()))
			{
				// 解析MIME消息为MMContent对象
				List<MMContent> contents = new ArrayList<MMContent>();
				parseMIME(mime, contents);
				for (int j = 0; j < contents.size(); j++)
				{
					deliverContent.addSubContent(contents.get(j));
				}
				break;
			}

		}
		req.setContent(deliverContent);

	}

	/**
	 * MIME消息解析为MMContent,递归
	 * 
	 * @param mime
	 * @param contents
	 */
	public static void parseMIME(MIMEMessage mime, List<MMContent> contents)
	{
		if (mime.getBoundary() == null)
		{
			MMContent subContent = parseSubContent(mime);
			if (subContent != null)
			{
				contents.add(subContent);
			}
			return;
		}
		List<MIMEMessage> mimes = getSubMIMEMessage(mime);
		for (int i = 0; i < mimes.size(); i++)
		{
			MIMEMessage subMime = mimes.get(i);
			parseMIME(subMime, contents);
		}
	}

	/**
	 * 对于内嵌的MIME消息，得到消息的各个子MIME消息
	 * 
	 * @param mime
	 * @return
	 */
	public static List<MIMEMessage> getSubMIMEMessage(MIMEMessage mime)
	{
		List<MIMEMessage> mimes = new ArrayList<MIMEMessage>();

		String charset = mime.getCharset();
		String boundary = mime.getBoundary();
		byte[] bodyBytes = mime.getBody();
		List<byte[]> list = splitMime(bodyBytes, boundary);
		for (int i = 0; i < list.size(); i++)
		{
			byte[] part = list.get(i);
			if (new String(part).indexOf("\r\n\r\n") == -1)
			{
				// 错误的部分
				continue;
			}
			try
			{
				mimes.add(new MIMEMessage(part, charset));
			}
			catch (IOException ex)
			{
				log.error(null, ex);
			}

		}

		return mimes;
	}

	/**
	 * 一个MIME段消息 boundary之后到下一个boundary之前的字符值，包括消息头和消息体 解析一个mime消息为MMContent
	 * 
	 * @param attachment
	 * @return
	 */
	public static MMContent parseSubContent(MIMEMessage mime)
	{
		if (mime.getContentType() == null)
		{
			return null;
		}
		MMContent subContent = new MMContent();
		String encodingType = mime.getContentTransferEncoding();
		if (encodingType != null)
		{
			byte[] bodyBytes = null;
			if (encodingType.equalsIgnoreCase("base64"))
			{
				bodyBytes = decodeBASE64(mime.getBody());
			}
			else
			{
				bodyBytes = mime.getBody();
			}
			subContent = MMContent.createFromBytes(bodyBytes);
		}
		subContent.setContentType(new MMContentType(mime.getContentType()));

		if (mime.getContentID() != null)
		{
			subContent.setContentID(mime.getContentID());
		}

		if (mime.getCharset() != null)
		{
			subContent.setCharset(mime.getCharset());
		}

		if (mime.getContentLocation() != null)
		{
			subContent.setContentLocation(mime.getContentLocation());
		}

		return subContent;

	}

	/**
	 * 分割MME消息的byte版本
	 * 
	 * @param bytes
	 * @param boundary
	 * @return
	 */
	public static List<byte[]> splitMime(byte[] bytes, String boundary)
	{
		String bBoundary = "--" + boundary + "\r\n";
		String eBoundary = "--" + boundary + "--" + "\r\n";
		List<byte[]> list = ByteUtil.split(bytes, bBoundary.getBytes());

		byte[] b = list.remove(list.size() - 1);
		List<byte[]> extra = ByteUtil.split(b, eBoundary.getBytes());
		list.addAll(extra);
		extra.clear();
		return list;

	}

	public static byte[] decodeBASE64(byte[] bytes)
	{
		if (bytes == null)
		{
			return null;
		}
		BASE64Decoder decoder = new BASE64Decoder();

		ByteArrayInputStream input = new ByteArrayInputStream(bytes);
		byte[] b = null;
		try
		{
			b = decoder.decodeBuffer(input);
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			log.error(null, e);
		}

		return b;

	}

	public static String getBoundary(String data)
	{
		java.util.regex.Pattern pattern = java.util.regex.Pattern
				.compile("(boundary=\")([^\"]+)");
		java.util.regex.Matcher matcher = pattern.matcher(data);
		if (matcher.find())
			return matcher.group(2);
		else
			return null;
	}
}
