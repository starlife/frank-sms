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
import com.cmcc.mm7.vasp.message.MM7DeliverReq;
import com.cmcc.mm7.vasp.message.MM7RSReq;
import com.cmcc.mm7.vasp.message.MM7RSRes;

public class DecodeMM7
{
	
	private static final Log log = LogFactory.getLog(DecodeMM7.class);
	
	
	public static MM7RSRes decodeResMessage(byte[] xmlByte,Charset charset)
	{
		return XML2Message.parseResXML(xmlByte);
	}
	
	/**
	 * 把收到的消息进行解码
	 * @param bodyByte  消息体字节串
	 * @param charset  默认编码
	 * @return MM7RSReq消息
	 */
	public static MM7RSReq decodeReqMessage(byte[] bodyByte,String charset,String boundary)
	{
		MM7RSReq req = new MM7RSReq();
		try{
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			baos.write(bodyByte);
			
			String bodyMessage=null;
			if(charset==null)
			{
				bodyMessage=baos.toString();
			}else
			{
				bodyMessage=baos.toString(charset);
			}
			int xmlbeg = bodyMessage.indexOf(MMConstants.BEGINXMLFLAG);
			int xmlend=	bodyMessage.indexOf(MMConstants.ENDXMLFLAG)+MMConstants.ENDXMLFLAG.length();
			String xmlMessage=bodyMessage.substring(xmlbeg,xmlend);
			req = XML2Message.parseReqXML(xmlMessage.getBytes(charset));
			
			if(boundary!=null&&(req instanceof MM7DeliverReq)&&(baos.size()>xmlend))
			{
				//解析附件
				ByteArrayOutputStream attachbaos = new ByteArrayOutputStream();
				attachbaos.write(baos.toByteArray(), xmlend,
						(baos.size() - xmlend));
				req = parseAttachment(attachbaos, (MM7DeliverReq)req,boundary);
			}
			
			
		}catch(Exception ex)
		{
			
		}
		
		return req;

	}

	private static MM7DeliverReq parseAttachment(ByteArrayOutputStream content,
			MM7DeliverReq deliverReq,String boundary)
	{

		MMContent deliverContent = new MMContent();
		
		int length = boundary.length() + 2;
		List<Integer> bound = new ArrayList<Integer>();
		// 得到所有边界部分的index
		byte[] bContent = content.toByteArray();
		int index = 0;
		byte[] bBoundary = ("--" + boundary).getBytes();
		int m = 0;
		for (int i = 0; i < bContent.length; i++)
		{
			if (bContent[i] == bBoundary[m] && m < bBoundary.length)
			{
				if (m == bBoundary.length - 1)
				{
					index = i - bBoundary.length + 1;
					bound.add(index);
					m = 0;
					continue;
				}
				index = i;
				m++;
				continue;
			}
			else
			{
				m = 0;
				continue;
			}
		}
		for (int j = 1; j < bound.size(); j++)
		{
			
			int bound1 = bound.get(j - 1);
			int bound2 = bound.get(j);
			ByteArrayOutputStream attachment = new ByteArrayOutputStream();

			attachment.write(content.toByteArray(), bound1 + length,
					(bound2 - (bound1 + length)));
			
			MMContent subContent= parseSubContent(attachment);
			if(subContent!=null)
			{
				deliverContent.addSubContent(subContent);
			}
			
		}
		deliverReq.setContent(deliverContent);
		return deliverReq;
	}
	
	/**
	 * 一个MIME段消息 boundary之后到下一个boundary之前的字符值，包括消息头和消息体
	 * @param attachment
	 * @return
	 */
	private static MMContent parseSubContent(ByteArrayOutputStream attachment)
	{
		
		MMContent subContent = new MMContent();
		
		MIMEMessage mime=new MIMEMessage(attachment.toByteArray());
		
		if(mime.getContentType()!=null)
		{
			return null;
		}
		subContent.setContentType(new MMContentType(mime.getContentType()));
		
		if(mime.getContentID()!=null)
		{
			subContent.setContentID(mime.getContentID());
		}
		
		if(mime.getCharset()!=null)
		{
			subContent.setCharset(mime.getCharset());
		}
		
		if(mime.getContentLocation()!=null)
		{
			subContent.setContentLocation(mime.getContentLocation());
		}
		
		String encodingType=mime.getContentTransferEncoding();
		if(encodingType!=null)
		{
			byte[] bodyBytes=null;
			if(encodingType.equalsIgnoreCase("base64"))
			{
				bodyBytes=decodeBASE64(mime.getBody());
			}else
			{
				bodyBytes=mime.getBody();
			}
			subContent = MMContent.createFromBytes(bodyBytes);
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
		
			ByteArrayInputStream input=new ByteArrayInputStream(bytes);
			byte[] b=null;
			try
			{
				b = decoder.decodeBuffer(input);
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				log.error(null,e);
			}
			
			return b;
		
		
	}
	
	public static String getBoundary(String data)
    {
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("(boundary=\")([^\"]+)");
        java.util.regex.Matcher matcher = pattern.matcher(data);
        if(matcher.find())
            return matcher.group(2);
        else
            return null;
    }
}
