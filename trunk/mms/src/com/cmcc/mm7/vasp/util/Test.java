package com.cmcc.mm7.vasp.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cmcc.mm7.vasp.common.MMContent;
import com.cmcc.mm7.vasp.message.MM7SubmitReq;
import com.cmcc.mm7.vasp.service.MM7Sender;

public class Test
{
	private static final Log log = LogFactory.getLog(Test.class);
	private String encodeSoapHeader(String transactionID)
	{
		StringBuffer sb = new StringBuffer();
		sb.append("<env:Header><mm7:TransactionID xmlns:mm7=\"http://www.3gpp.org/ftp/Specs/archive/23_series/23.140/schema/REL-6-MM7-1-0\" env:mustUnderstand=\"1\">"
						+ transactionID + "</mm7:TransactionID></env:Header>");
		return sb.toString();
	}

	private byte[] encode(MM7SubmitReq submitReq, String charset) throws IOException
	{
		ByteArrayOutputStream byteOutput=new  ByteArrayOutputStream();
		StringBuffer sb = new StringBuffer();
		StringBuffer ContentBuffer = new StringBuffer();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		if (submitReq.isContentExist())
		{
			sb.append("this is a multi-part message in MIME format" + "\r\n");
			sb.append("\r\n");
			sb.append("\r\n");
			sb.append("----NextPart_0_2817_24856" + "\r\n");
			sb.append("Content-Type:text/xml;charset=\"" + charset + "\"\r\n");
			sb.append("Content-Transfer-Encoding:8bit" + "\r\n");
			sb.append("Content-ID:</tnn-200102/mm7-vasp>" + "\r\n");
			sb.append("\r\n");
		}
		
		sb.append("<?xml version=\"1.0\" encoding=\""
						+ charset
						+ "\"?><env:Envelope xmlns:env=\"http://schemas.xmlsoap.org/soap/envelope/\">");//
		
		if (submitReq.isTransactionIDExist()) //
			sb.append(encodeSoapHeader(submitReq.getTransactionID()));
		else
			log.error("TransactionID 不许为空！");
		sb.append("<env:Body>");
		sb
				.append("<SubmitReq xmlns=\"http://www.3gpp.org/ftp/Specs/archive/23_series/23.140/schema/REL-6-MM7-1-0\">");
		if (submitReq.isMM7VersionExist())
			sb.append("<MM7Version>" + submitReq.getMM7Version()
					+ "</MM7Version>");
		else
			System.out.println("MM7Version 不许为空！");
		if (submitReq.isVASPIDExist() || submitReq.isVASIDExist()
				|| submitReq.isSenderAddressExist())
		{
			sb.append("<SenderIdentification>");
			if (submitReq.isVASPIDExist())
				sb.append("<VASPID>" + submitReq.getVASPID() + "</VASPID>");
			else
				System.out.println("SP代码VASPID不许为空！");
			if (submitReq.isVASIDExist())
				sb.append("<VASID>" + submitReq.getVASID() + "</VASID>");
			else
				System.out.println("服务代码VASID不许为空！");
			if (submitReq.isSenderAddressExist())
				sb.append("<SenderAddress>" + submitReq.getSenderAddress()
						+ "</SenderAddress>");
			sb.append("</SenderIdentification>");
		}
		else
			System.out.println("SP代码VASPID和服务代码VASID均不允许为空！");

		if (submitReq.isToExist() || submitReq.isCcExist()
				|| submitReq.isBccExist())
		{
			sb.append("<Recipients>");
			if (submitReq.isToExist())
			{
				sb.append("<To>");
				List ToList = new ArrayList();
				ToList = submitReq.getTo();
				for (int i = 0; i < ToList.size(); i++)
				{
					String strto = (String) ToList.get(i);
					if (strto.indexOf('@') > 0)
						sb.append("<RFC2822Address>" + strto
								+ "</RFC2822Address>");
					else
						sb.append("<Number>" + strto + "</Number>");
				}
				sb.append("</To>");
			}
			if (submitReq.isCcExist())
			{
				sb.append("<Cc>");
				List CcList = new ArrayList();
				CcList = submitReq.getCc();
				for (int i = 0; i < CcList.size(); i++)
				{
					String strcc = (String) CcList.get(i);
					if (strcc.indexOf('@') > 0)
						sb.append("<RFC2822Address>" + strcc
								+ "</RFC2822Address>");
					else
						sb.append("<Number>" + strcc + "</Number>");
				}
				sb.append("</Cc>");
			}
			if (submitReq.isBccExist())
			{
				sb.append("<Bcc>");
				List BccList = new ArrayList();
				BccList = submitReq.getBcc();
				for (int i = 0; i < BccList.size(); i++)
				{
					String strbcc = (String) BccList.get(i);
					if (strbcc.indexOf('@') > 0)
						sb.append("<RFC2822Address>" + strbcc
								+ "</RFC2822Address>");
					else
						sb.append("<Number>" + strbcc + "</Number>");
				}
				sb.append("</Bcc>");
			}
			sb.append("</Recipients>");
		}
		else
			System.out.println("接收方地址To、抄送方地址Cc和密送方地址Bcc中至少需要有一个不为空！");

		if (submitReq.isServiceCodeExist())
			sb.append("<ServiceCode>" + submitReq.getServiceCode()
					+ "</ServiceCode>");
		else
			System.out.println("业务代码ServiceCode不许为空！");

		if (submitReq.isLinkedIDExist())
			sb.append("<LinkedID>" + submitReq.getLinkedID() + "</LinkedID>");
		if (submitReq.isMessageClassExist())
			sb.append("<MessageClass>" + submitReq.getMessageClass()
					+ "</MessageClass>");
		if (submitReq.isTimeStampExist())
			sb.append("<TimeStamp>" + sdf.format(submitReq.getTimeStamp())
					+ "</TimeStamp>");
		if (submitReq.isReplyChargingExist())
		{
			sb.append("<ReplyCharging");
			if (submitReq.isReplyChargingSizeExist())
				sb.append(" replyChargingSize=\""
						+ submitReq.getReplyChargingSize() + "\"");
			if (submitReq.isReplyDeadlineExist())
				sb.append(" replyDeadline=\""
						+ sdf.format(new Date(submitReq
								.getReplyDeadlineRelative())) + "+08:00\"");
			if (submitReq.isReplyDeadlineAbsoluteExist())
				sb.append(" replyDeadline=\""
						+ sdf.format(submitReq.getReplyDeadlineAbsolute())
						+ "+08:00\"");
			sb.append("></ReplyCharging>");
		}
		if (submitReq.isEarliestDeliveryTimeExist())
		{
			Date dd = new Date(submitReq.getEarliestDeliveryTimeRelative());
			sb.append("<EarliestDeliveryTime>" + sdf.format(dd) + "+08:00"
					+ "</EarliestDeliveryTime>");
		}
		if (submitReq.isEarliestDeliveryTimeAbsolute())
			sb.append("<EarliestDeliveryTime>"
					+ sdf.format(submitReq.getEarliestDeliveryTimeAbsolute())
					+ "+08:00" + "</EarliestDeliveryTime>");
		if (submitReq.isExpiryDateExist())
			sb.append("<ExpiryDate>"
					+ sdf.format(new Date(submitReq.getExpiryDateRelative()))
					+ "+08:00" + "</ExpiryDate>");
		if (submitReq.isExpiryDateAbsolute())
			sb.append("<ExpiryDate>"
					+ sdf.format(submitReq.getExpiryDateAbsolute()) + "+08:00"
					+ "</ExpiryDate>");
		if (submitReq.isDeliveryReportExist())
			sb.append("<DeliveryReport>" + submitReq.getDeliveryReport()
					+ "</DeliveryReport>");
		if (submitReq.isReadReplyExist())
			sb
					.append("<ReadReply>" + submitReq.getReadReply()
							+ "</ReadReply>");
		/*
		 * if(submitReq.isReplyChargingExist()) sb.append("<ReplyCharging>"+submitReq.getReplyCharging()+"</ReplyCharging>");
		 * if(submitReq.isReplyDeadlineExist()) sb.append("<Reply-Deadline>"+sdf.format(new
		 * Date( submitReq.getReplyDeadlineRelative()))+"+08:00"+"</Reply-Deadline>");
		 * if(submitReq.isReplyDeadlineAbsoluteExist()) sb.append("<Reply-Deadline>"+sdf.format(
		 * submitReq.getReplyDeadlineAbsolute())+"+08:00"+"</Reply-Deadline>");
		 * if(submitReq.isReplyChargingSizeExist()) sb.append("<ReplyChargingSize>"+submitReq.getReplyChargingSize()+"</ReplyChargingSize>");
		 */

		if (submitReq.isPriorityExist())
		{
			// sb.append("<Priority>" + submitReq.getPriority() +
			// "</Priority>");
			if (submitReq.getPriority() == (byte) 0)
				sb.append("<Priority>" + "Low" + "</Priority>");
			else if (submitReq.getPriority() == (byte) 1)
				sb.append("<Priority>" + "Normal" + "</Priority>");
			else if (submitReq.getPriority() == (byte) 2)
				sb.append("<Priority>" + "High" + "</Priority>");
		}
		if (submitReq.isSubjectExist())
			sb.append("<Subject>" + submitReq.getSubject() + "</Subject>");
		if (submitReq.isChargedPartyExist())
		{
			// sb.append("<ChargedParty>"+submitReq.getChargedParty()+"</ChargedParty>");
			if (submitReq.getChargedParty() == (byte) 0)
				sb.append("<ChargedParty>Sender</ChargedParty>");
			else if (submitReq.getChargedParty() == (byte) 1)
				sb.append("<ChargedParty>Recipient</ChargedParty>");
			else if (submitReq.getChargedParty() == (byte) 2)
				sb.append("<ChargedParty>Both</ChargedParty>");
			else if (submitReq.getChargedParty() == (byte) 3)
				sb.append("<ChargedParty>Neither</ChargedParty>");
			else if (submitReq.getChargedParty() == (byte) 4)
				sb.append("<ChargedParty>ThirdParty</ChargedParty>");
		}
		if (submitReq.isChargedPartyIDExist())
			sb.append("<ChargedPartyID>" + submitReq.getChargedPartyID()
					+ "</ChargedPartyID>");
		if (submitReq.isDistributionIndicatorExist())
			sb.append("<DistributionIndicator>"
					+ submitReq.getDistributionIndicator()
					+ "</DistributionIndicator>");
		if (submitReq.isContentExist())
		{
			// begin add by hudm 2004-03-09
			/*
			 * if(submitReq.getContent().isContentIDExist()) sb.append("<Content
			 * href=\"cid:"+submitReq.getContent().getContentID()+"\"");
			 * if(submitReq.isAllowAdaptationsExist())
			 * sb.append(";allowAdaptions=\"True\"/>"); else sb.append("/>");
			 */
			// end add by hudm 2004-03-09*/
			// sb.append("</mm7:SubmitReq>");
			sb.append("</SubmitReq>");
			sb.append("</env:Body></env:Envelope>");
			sb.append("\r\n");
			sb.append("----NextPart_0_2817_24856\r\n");
			MMContent parentContent = submitReq.getContent();
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
						List tempSub = new ArrayList();
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
							+ parentContent.getContentType().getPrimaryType()
							+ "/" + parentContent.getContentType().getSubType()
							+ ";boundary=\"SubPart_7452684322002_77645\""
							+ "\r\n");
				}
			}
			else
				sb.append("Content-Type:" + "multipart/mixed"
						+ ";boundary=\"SubPart_7452684322002_77645\"" + "\r\n");
			if (parentContent.isContentIDExist())
				sb
						.append("Content-ID:" + parentContent.getContentID()
								+ "\r\n");
			else
				sb.append("Content-ID:<SaturnPics-01020930>" + "\r\n");
			sb.append("Content-Transfer-Encoding:8bit" + "\r\n");
			if (parentContent.isContentLocationExist())
				sb.append("Content-Location:"
						+ parentContent.getContentLocation() + "\r\n");
			sb.append("\r\n");
			ByteArrayOutputStream Subbaos = new ByteArrayOutputStream();
			if (parentContent.isMultipart())
			{
				List subContent = new ArrayList();
				subContent = parentContent.getSubContents();
				for (int i = 0; i < subContent.size(); i++)
				{
					ContentBuffer = new StringBuffer();
					ContentBuffer.append("--SubPart_7452684322002_77645\r\n");
					MMContent content = (MMContent) subContent.get(i);
					if (content.getContentType() != null)
					{
						ContentBuffer.append("Content-Type:"
								+ content.getContentType().getPrimaryType()
								+ "/" + content.getContentType().getSubType());
						if (content.getContentType().getPrimaryType()
								.equalsIgnoreCase("text"))
						{
							if (content.getCharset() == null
									|| content.getCharset().length() == 0)
								ContentBuffer.append(";charset="
										+ charset);
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
												+ charset
												+ "\r\n");
							}
							else if (type.equals("jpg"))
							{
								ContentBuffer.append("Content-Type:image/jpeg"
										+ "\r\n");
							}
							else if (type.equals("gif"))
							{
								ContentBuffer.append("Content-Type:image/gif"
										+ "\r\n");
							}
						}
					}
					ContentBuffer.append("Content-Transfer-Encoding:8bit"
							+ "\r\n");
					if (content.getContentType().getSubType().equalsIgnoreCase(
							"related"))
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
				Subbaos.write("--SubPart_7452684322002_77645--\r\n".getBytes());
				Subbaos.write("----NextPart_0_2817_24856--\r\n".getBytes());
				byteOutput.write(sb.toString().getBytes(
						"" + charset));
				byteOutput.write(Subbaos.toByteArray());
			}
		}
		else
		{
			// sb.append("</mm7:SubmitReq>");
			sb.append("</SubmitReq>");
			sb.append("</env:Body></env:Envelope>");
			// Cancel by hudm 2004-06-21
			// sb.append("----NextPart_0_2817_24856--\r\n");
			// end Cancel by hudm 2004-06-21
			byteOutput.write(sb.toString().getBytes());
		}
		return byteOutput.toByteArray();
	}
}
