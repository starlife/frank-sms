/**
 * File Name:SOAPDecoder.java Company: �й��ƶ����Ź�˾ Date : 2004-2-12
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

public class DecodeMM7
{

	private static final Log log = LogFactory.getLog(DecodeMM7.class);

	public static MM7RSRes decodeResMessage(byte[] xmlByte, Charset charset)
	{
		return SOAPDecoder.parseResXML(xmlByte);
	}

	/**
	 * ���յ�����Ϣ���н���
	 * 
	 * @param bodyByte
	 *            ��Ϣ���ֽڴ�
	 * @param charset
	 *            Ĭ�ϱ���
	 * @return MM7RSReq��Ϣ
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
				// �������� ������һ��multipart/mixed;
				String attachMessage = bodyMessage.substring(xmlend);
				parseAttachment(attachMessage, charset, boundary,
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
	 * �����������ѽ����õ���MMContent������ӵ�MM7DeliverReq��Ϣ��
	 * 
	 * @param attachMessage
	 * @param charset
	 * @param boundary
	 * @param req
	 * @throws IOException
	 */
	private static void parseAttachment(String attachMessage, String charset,
			String boundary, MM7DeliverReq req) throws IOException
	{
		// ȥ��attachMessage�е�boundary ��Ȼ�����parseAttachment(mime, req);
		MMContent deliverContent = new MMContent();

		String regex = "--" + boundary + "(--)?\r\n";
		String[] parts = attachMessage.split(regex);
		for (int i = 0; i < parts.length; i++)
		{
			String part = parts[i];
			if (part.indexOf("\r\n\r\n") == -1)
			{
				// ����Ĳ���
				continue;
			}
			byte[] bytes = null;
			if (charset == null)
			{
				bytes = part.getBytes();
			}
			else
			{
				bytes = part.getBytes(Charset.forName(charset));
			}
			// ����MIME��ϢΪMMContent����
			List<MMContent> contents = new ArrayList<MMContent>();
			parseMIME(new MIMEMessage(bytes, charset), contents);
			for (int j = 0; j < contents.size(); j++)
			{
				deliverContent.addSubContent(contents.get(j));
			}

		}
		req.setContent(deliverContent);

	}

	/**
	 * MIME��Ϣ����ΪMMContent
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
	 * ������Ƕ��MIME��Ϣ���õ���Ϣ�ĸ�����MIME��Ϣ
	 * 
	 * @param mime
	 * @return
	 */
	public static List<MIMEMessage> getSubMIMEMessage(MIMEMessage mime)
	{
		List<MIMEMessage> mimes = new ArrayList<MIMEMessage>();

		String charset = mime.getCharset();
		String boundary = mime.getBoundary();
		byte[] bodyByes = mime.getBody();
		String bodyMessage = null;
		if (charset == null)
		{
			bodyMessage = new String(bodyByes);
		}
		else
		{
			bodyMessage = new String(bodyByes, Charset.forName(charset));
		}

		String regex = "--" + boundary + "(--)?\r\n";
		String[] parts = bodyMessage.split(regex);
		for (int i = 0; i < parts.length; i++)
		{
			String part = parts[i];
			if (part.indexOf("\r\n\r\n") == -1)
			{
				// ����Ĳ���
				continue;
			}
			byte[] bytes = null;
			if (charset == null)
			{
				bytes = part.getBytes();
			}
			else
			{
				bytes = part.getBytes(Charset.forName(charset));
			}
			try
			{
				mimes.add(new MIMEMessage(bytes, charset));
			}
			catch (IOException ex)
			{
				log.error(null, ex);
			}

		}

		return mimes;
	}

	/**
	 * һ��MIME����Ϣ boundary֮����һ��boundary֮ǰ���ַ�ֵ��������Ϣͷ����Ϣ�� ����һ��mime��ϢΪMMContent
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
