/**
 * File Name:SOAPEncoder.java Company: 中国移动集团公司 Date : 2004-1-8
 */

package com.cmcc.mm7.vasp.protocol;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cmcc.mm7.vasp.common.MMContent;
import com.cmcc.mm7.vasp.protocol.message.MM7CancelReq;
import com.cmcc.mm7.vasp.protocol.message.MM7DeliverRes;
import com.cmcc.mm7.vasp.protocol.message.MM7DeliveryReportRes;
import com.cmcc.mm7.vasp.protocol.message.MM7ReadReplyRes;
import com.cmcc.mm7.vasp.protocol.message.MM7ReplaceReq;
import com.cmcc.mm7.vasp.protocol.message.MM7SubmitReq;
import com.cmcc.mm7.vasp.protocol.message.MM7VASPErrorRes;
import com.cmcc.mm7.vasp.protocol.message.MM7VASPReq;
import com.cmcc.mm7.vasp.protocol.message.MM7VASPRes;

/**
 * 把MM7VASPReq消息和MM7VASPRes消息解析为Byte[]形式
 * 
 * @author Administrator
 */
public class EncodeMM7
{
	private static final Log log = LogFactory.getLog(EncodeMM7.class);

	/** 默认构造方法 */
	public EncodeMM7()
	{
	}

	

	public static byte[] encodeMM7VASPRes(MM7VASPRes mm7VASPRes,
			Charset charset)
	{
		
		if (mm7VASPRes instanceof MM7DeliverRes)
		{
			return SOAPEncoder.deliverRes2Xml((MM7DeliverRes) mm7VASPRes, charset).getBytes(charset);
		}
		else if (mm7VASPRes instanceof MM7DeliveryReportRes)
		{
			return SOAPEncoder.deliveryReportRes2Xml((MM7DeliveryReportRes) mm7VASPRes, charset).getBytes(charset);
		}
		else if (mm7VASPRes instanceof MM7ReadReplyRes)
		{
			return SOAPEncoder.readReplyRes2Xml((MM7ReadReplyRes) mm7VASPRes,
					charset).getBytes(charset);
		}
		else if (mm7VASPRes instanceof MM7VASPErrorRes)
		{
			return SOAPEncoder.vaspErrorRes2Xml((MM7VASPErrorRes) mm7VASPRes,
					charset).getBytes(charset);
		}
		else
		{
			return null;
		}

	}

	public static byte[] encodeMM7VASPReq(MM7VASPReq mm7VaspReq,
			Charset charset)
	{
		if (mm7VaspReq instanceof MM7SubmitReq)
		{
			return encodeMM7SubmitReq((MM7SubmitReq) mm7VaspReq, charset);
		}
		else if (mm7VaspReq instanceof MM7CancelReq)
		{
			log.error("MM7CancelReq消息暂时还未实现");
			return null;
		}
		else if (mm7VaspReq instanceof MM7ReplaceReq)
		{
			log.error("MM7ReplaceReq消息暂时还未实现");
			return null;
		}
		else
		{
			return null;
		}
	}
	
	
	
	private static byte[] encodeMM7SubmitReq(MM7SubmitReq mm7VaspReq,
			Charset charset)
	{
		ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();

		try
		{
			// bEncoder = false;
			StringBuffer sb = new StringBuffer();
			//StringBuffer ContentBuffer = new StringBuffer();
			//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

			if (mm7VaspReq.isContentExist())
			{
				sb.append("this is a multi-part message in MIME format"
						+ "\r\n");
				sb.append("\r\n");
				sb.append("\r\n");
				sb.append("----NextPart_0_2817_24856" + "\r\n");
				sb.append("Content-Type:text/xml;charset=\"" + charset
						+ "\"\r\n");
				sb.append("Content-Transfer-Encoding:8bit" + "\r\n");
				sb.append("Content-ID:</tnn-200102/mm7-vasp>" + "\r\n");
				sb.append("\r\n");
			}
			
			sb.append(SOAPEncoder.submitReq2Xml(mm7VaspReq,charset));

			
			if (mm7VaspReq.isContentExist())
			{
				
				
				sb.append("\r\n");
				sb.append("----NextPart_0_2817_24856\r\n");
				MMContent parentContent = mm7VaspReq.getContent();
				// sb.append("Content-Type:"+parentContent.getContentType().getPrimaryType()+"/"+
				// parentContent.getContentType().getSubType()+
				// ";boundary=\"SubPart_7452684322002_77645\""+"\r\n");
				if (parentContent.getContentType() != null)
				{
					String strSubType = "";
					String strtempID = "<START>";
					strSubType = parentContent.getContentType().getSubType();
					if (strSubType.equalsIgnoreCase("related"))
					{
						if (parentContent.isMultipart())
						{
							List<MMContent> tempSub = new ArrayList<MMContent>();
							tempSub = parentContent.getSubContents();
							for (int x = 0; x < tempSub.size(); x++)
							{
								MMContent tempCon = (MMContent) tempSub.get(x);
								if (tempCon.getContentType().getSubType()
										.equalsIgnoreCase("smil"))
								{
									if (tempCon.isContentIDExist())
										strtempID = tempCon.getContentID();
									else
										strtempID = "<START>";
									break;
								}
							}
						}
						sb.append("Content-Type:" + "multipart/related;"
								+ "start=\"" + strtempID
								+ "\";type=\"application/smil\""
								+ ";boundary=\"SubPart_7452684322002_77645\""
								+ "\r\n");
					}
					else
					{
						sb.append("Content-Type:"
								+ parentContent.getContentType()
										.getPrimaryType() + "/"
								+ parentContent.getContentType().getSubType()
								+ ";boundary=\"SubPart_7452684322002_77645\""
								+ "\r\n");
					}
				}
				else
					sb.append("Content-Type:" + "multipart/mixed"
							+ ";boundary=\"SubPart_7452684322002_77645\""
							+ "\r\n");
				if (parentContent.isContentIDExist())
					sb.append("Content-ID:" + parentContent.getContentID()
							+ "\r\n");
				else
					sb.append("Content-ID:<SaturnPics-01020930>" + "\r\n");
				sb.append("Content-Transfer-Encoding:8bit" + "\r\n");
				if (parentContent.isContentLocationExist())
					sb.append("Content-Location:"
							+ parentContent.getContentLocation() + "\r\n");
				sb.append("\r\n");
				
				if (parentContent.isMultipart())
				{
					ByteArrayOutputStream Subbaos = new ByteArrayOutputStream();
					addSubContent(Subbaos,parentContent,charset);
					
					Subbaos.write("--SubPart_7452684322002_77645--\r\n"
							.getBytes());
					Subbaos.write("----NextPart_0_2817_24856--\r\n".getBytes());
					byteOutput.write(sb.toString().getBytes(charset));
					byteOutput.write(Subbaos.toByteArray());
					
				}
			}
			else
			{
				
				byteOutput.write(sb.toString().getBytes());
			}

			// bEncoder = true;
		}
		catch (Exception e)
		{
			System.err.println(e);
		}
		return byteOutput.toByteArray();

	}
	
	private static void addSubContent(ByteArrayOutputStream Subbaos,MMContent parentContent,Charset charset) throws IOException
	{
		List<MMContent> subContent = new ArrayList<MMContent>();
		subContent = parentContent.getSubContents();
		for (int i = 0; i < subContent.size(); i++)
		{
			StringBuffer ContentBuffer = new StringBuffer();
			ContentBuffer
					.append("--SubPart_7452684322002_77645\r\n");
			MMContent content = (MMContent) subContent.get(i);
			if (content.getContentType() != null)
			{
				ContentBuffer.append("Content-Type:"
						+ content.getContentType().getPrimaryType()
						+ "/"
						+ content.getContentType().getSubType());
				if (content.getContentType().getPrimaryType()
						.equalsIgnoreCase("text"))
				{
					if (content.getCharset() == null
							|| content.getCharset().length() == 0)
						ContentBuffer.append(";charset=" + charset);
					else
						ContentBuffer.append(";charset="
								+ content.getCharset());
				}
				ContentBuffer.append("\r\n");
			}
			else
			{
				if (content.isContentIDExist())
				{
					String strContentID = content.getContentID();
					int index = strContentID.indexOf(".");
					String type = strContentID.substring(index + 1);
					type = type.toLowerCase();
					if (type.equals("txt"))
					{
						ContentBuffer
								.append("Content-Type:text/plain;charset="
										+ charset + "\r\n");
					}
					else if (type.equals("jpg"))
					{
						ContentBuffer
								.append("Content-Type:image/jpeg"
										+ "\r\n");
					}
					else if (type.equals("gif"))
					{
						ContentBuffer
								.append("Content-Type:image/gif"
										+ "\r\n");
					}
				}
			}
			ContentBuffer.append("Content-Transfer-Encoding:8bit"
					+ "\r\n");
			if (content.getContentType().getSubType()
					.equalsIgnoreCase("related"))
			{
				if (content.isContentIDExist())
					ContentBuffer.append("Content-ID:"
							+ content.getContentID() + "\r\n");
				else
					ContentBuffer.append("Content-ID:<START>\r\n");
			}
			else
			{
				if (content.isContentIDExist())
					ContentBuffer.append("Content-ID:"
							+ content.getContentID() + "\r\n");
			}
			if (content.isContentLocationExist())
				ContentBuffer.append("Content-Location:"
						+ content.getContentLocation() + "\r\n");

			ContentBuffer.append("\r\n");
			try
			{
				Subbaos.write(ContentBuffer.toString().getBytes());
				Subbaos.write(content.getContent());
				Subbaos.write("\r\n\r\n".getBytes());
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		
	}
	
	

}
