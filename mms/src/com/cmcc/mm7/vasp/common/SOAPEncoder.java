/**
 * File Name:SOAPEncoder.java Company: �й��ƶ����Ź�˾ Date : 2004-1-8
 */

package com.cmcc.mm7.vasp.common;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import sun.misc.BASE64Encoder;

import com.cmcc.mm7.vasp.message.MM7CancelReq;
import com.cmcc.mm7.vasp.message.MM7DeliverRes;
import com.cmcc.mm7.vasp.message.MM7DeliveryReportRes;
import com.cmcc.mm7.vasp.message.MM7ReadReplyRes;
import com.cmcc.mm7.vasp.message.MM7ReplaceReq;
import com.cmcc.mm7.vasp.message.MM7SubmitReq;
import com.cmcc.mm7.vasp.message.MM7VASPReq;
import com.cmcc.mm7.vasp.message.MM7VASPRes;
import com.cmcc.mm7.vasp.service.MM7Receiver;

public class SOAPEncoder
{
	private static final Log log = LogFactory.getLog(SOAPEncoder.class);
	/** Ĭ�Ϲ��췽�� */
	public SOAPEncoder()
	{
		//this.charset = charset;
	}

	/**
	 * ��MM7VASPReq��Ϣ���б��룬�õ�byte[]��ʽ����Ϣ
	 */
	public static byte[] encodeVASPReqMessage(MM7VASPReq mm7VaspReq,String charset)
			throws SOAPEncodeException
	{
		ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();

		try
		{
			// bEncoder = false;
			StringBuffer sb = new StringBuffer();
			StringBuffer ContentBuffer = new StringBuffer();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
			// add by hudm 2004-06-21
			if (mm7VaspReq instanceof MM7SubmitReq)
			{
				MM7SubmitReq req = (MM7SubmitReq) mm7VaspReq;
				if (req.isContentExist())
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
			}
			else if (mm7VaspReq instanceof MM7ReplaceReq)
			{
				MM7ReplaceReq req = (MM7ReplaceReq) mm7VaspReq;
				if (req.isContentExist())
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
			}
			// end add by hudm 2004-06-21
			sb
					.append("<?xml version=\"1.0\" encoding=\""
							+ charset
							+ "\"?><env:Envelope xmlns:env=\"http://schemas.xmlsoap.org/soap/envelope/\"><env:Header>");//
			if (mm7VaspReq.isTransactionIDExist()) //
				sb
						.append("<mm7:TransactionID xmlns:mm7=\"http://www.3gpp.org/ftp/Specs/archive/23_series/23.140/schema/REL-6-MM7-1-0\" env:mustUnderstand=\"1\">"
								+ mm7VaspReq.getTransactionID()
								+ "</mm7:TransactionID>");
			else
				System.out.println("TransactionID ����Ϊ�գ�");
			sb.append("</env:Header><env:Body>");
			/**
			 * �����͵���ϢΪMM7SubmitReq
			 */
			if (mm7VaspReq instanceof MM7SubmitReq)
			{
				MM7SubmitReq submitReq = (MM7SubmitReq) mm7VaspReq;
				sb
						.append("<SubmitReq xmlns=\"http://www.3gpp.org/ftp/Specs/archive/23_series/23.140/schema/REL-6-MM7-1-0\">");
				if (submitReq.isMM7VersionExist())
					sb.append("<MM7Version>" + submitReq.getMM7Version()
							+ "</MM7Version>");
				else
					System.out.println("MM7Version ����Ϊ�գ�");
				if (submitReq.isVASPIDExist() || submitReq.isVASIDExist()
						|| submitReq.isSenderAddressExist())
				{
					sb.append("<SenderIdentification>");
					if (submitReq.isVASPIDExist())
						sb.append("<VASPID>" + submitReq.getVASPID()
								+ "</VASPID>");
					else
						System.out.println("SP����VASPID����Ϊ�գ�");
					if (submitReq.isVASIDExist())
						sb
								.append("<VASID>" + submitReq.getVASID()
										+ "</VASID>");
					else
						System.out.println("�������VASID����Ϊ�գ�");
					if (submitReq.isSenderAddressExist())
						sb.append("<SenderAddress>"
								+ submitReq.getSenderAddress()
								+ "</SenderAddress>");
					sb.append("</SenderIdentification>");
				}
				else
					System.out.println("SP����VASPID�ͷ������VASID��������Ϊ�գ�");

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
					System.out.println("���շ���ַTo�����ͷ���ַCc�����ͷ���ַBcc��������Ҫ��һ����Ϊ�գ�");

				if (submitReq.isServiceCodeExist())
					sb.append("<ServiceCode>" + submitReq.getServiceCode()
							+ "</ServiceCode>");
				else
					System.out.println("ҵ�����ServiceCode����Ϊ�գ�");

				if (submitReq.isLinkedIDExist())
					sb.append("<LinkedID>" + submitReq.getLinkedID()
							+ "</LinkedID>");
				if (submitReq.isMessageClassExist())
					sb.append("<MessageClass>" + submitReq.getMessageClass()
							+ "</MessageClass>");
				if (submitReq.isTimeStampExist())
					sb.append("<TimeStamp>"
							+ sdf.format(submitReq.getTimeStamp())
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
										.getReplyDeadlineRelative()))
								+ "+08:00\"");
					if (submitReq.isReplyDeadlineAbsoluteExist())
						sb.append(" replyDeadline=\""
								+ sdf.format(submitReq
										.getReplyDeadlineAbsolute())
								+ "+08:00\"");
					sb.append("></ReplyCharging>");
				}
				if (submitReq.isEarliestDeliveryTimeExist())
				{
					Date dd = new Date(submitReq
							.getEarliestDeliveryTimeRelative());
					sb.append("<EarliestDeliveryTime>" + sdf.format(dd)
							+ "+08:00" + "</EarliestDeliveryTime>");
				}
				if (submitReq.isEarliestDeliveryTimeAbsolute())
					sb.append("<EarliestDeliveryTime>"
							+ sdf.format(submitReq
									.getEarliestDeliveryTimeAbsolute())
							+ "+08:00" + "</EarliestDeliveryTime>");
				if (submitReq.isExpiryDateExist())
					sb.append("<ExpiryDate>"
							+ sdf.format(new Date(submitReq
									.getExpiryDateRelative())) + "+08:00"
							+ "</ExpiryDate>");
				if (submitReq.isExpiryDateAbsolute())
					sb.append("<ExpiryDate>"
							+ sdf.format(submitReq.getExpiryDateAbsolute())
							+ "+08:00" + "</ExpiryDate>");
				if (submitReq.isDeliveryReportExist())
					sb.append("<DeliveryReport>"
							+ submitReq.getDeliveryReport()
							+ "</DeliveryReport>");
				if (submitReq.isReadReplyExist())
					sb.append("<ReadReply>" + submitReq.getReadReply()
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
					sb.append("<Subject>" + submitReq.getSubject()
							+ "</Subject>");
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
					sb.append("<ChargedPartyID>"
							+ submitReq.getChargedPartyID()
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
					 * sb.append(";allowAdaptions=\"True\"/>"); else
					 * sb.append("/>");
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
						strSubType = parentContent.getContentType()
								.getSubType();
						if (strSubType.equalsIgnoreCase("related"))
						{
							if (parentContent.isMultipart())
							{
								List tempSub = new ArrayList();
								tempSub = parentContent.getSubContents();
								for (int x = 0; x < tempSub.size(); x++)
								{
									MMContent tempCon = (MMContent) tempSub
											.get(x);
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
							sb
									.append("Content-Type:"
											+ "multipart/related;"
											+ "start=\""
											+ strtempID
											+ "\";type=\"application/smil\""
											+ ";boundary=\"SubPart_7452684322002_77645\""
											+ "\r\n");
						}
						else
						{
							sb
									.append("Content-Type:"
											+ parentContent.getContentType()
													.getPrimaryType()
											+ "/"
											+ parentContent.getContentType()
													.getSubType()
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
					ByteArrayOutputStream Subbaos = new ByteArrayOutputStream();
					if (parentContent.isMultipart())
					{
						List subContent = new ArrayList();
						subContent = parentContent.getSubContents();
						for (int i = 0; i < subContent.size(); i++)
						{
							ContentBuffer = new StringBuffer();
							ContentBuffer
									.append("--SubPart_7452684322002_77645\r\n");
							MMContent content = (MMContent) subContent.get(i);
							if (content.getContentType() != null)
							{
								ContentBuffer
										.append("Content-Type:"
												+ content.getContentType()
														.getPrimaryType()
												+ "/"
												+ content.getContentType()
														.getSubType());
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
									String strContentID = content
											.getContentID();
									int index = strContentID.indexOf(".");
									String type = strContentID
											.substring(index + 1);
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
							ContentBuffer
									.append("Content-Transfer-Encoding:8bit"
											+ "\r\n");
							if (content.getContentType().getSubType()
									.equalsIgnoreCase("related"))
							{
								if (content.isContentIDExist())
									ContentBuffer.append("Content-ID:"
											+ content.getContentID() + "\r\n");
								else
									ContentBuffer
											.append("Content-ID:<START>\r\n");
							}
							else
							{
								if (content.isContentIDExist())
									ContentBuffer.append("Content-ID:"
											+ content.getContentID() + "\r\n");
							}
							if (content.isContentLocationExist())
								ContentBuffer
										.append("Content-Location:"
												+ content.getContentLocation()
												+ "\r\n");

							ContentBuffer.append("\r\n");
							try
							{
								Subbaos.write(ContentBuffer.toString()
										.getBytes());
								Subbaos.write(content.getContent());
								Subbaos.write("\r\n\r\n".getBytes());
							}
							catch (IOException e)
							{
								e.printStackTrace();
							}
						}
						Subbaos.write("--SubPart_7452684322002_77645--\r\n"
								.getBytes());
						Subbaos.write("----NextPart_0_2817_24856--\r\n"
								.getBytes());
						byteOutput.write(sb.toString().getBytes("" + charset));
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

			}
			/**
			 * �����͵���ϢΪMM7CancelReq
			 */
			else if (mm7VaspReq instanceof MM7CancelReq)
			{
				MM7CancelReq cancelReq = (MM7CancelReq) mm7VaspReq;
				sb
						.append("<CancelReq xmlns:mm7=\"http://www.3gpp.org/ftp/Specs/archive/23_series/23.140/schema/REL-6-MM7-1-0\" env:mustUnderstand=\"1\">");
				if (cancelReq.isMM7VersionExist())
					sb.append("<MM7Version>" + cancelReq.getMM7Version()
							+ "</MM7Version>");
				else
					System.out.println("MM7Version ����Ϊ�գ�");
				if (cancelReq.isVASPIDExist() || cancelReq.isVASIDExist())
				{
					sb.append("<SenderIdentification>");
					if (cancelReq.isVASPIDExist())
						sb.append("<VASPID>" + cancelReq.getVASPID()
								+ "</VASPID>");
					if (cancelReq.isVASIDExist())
						sb
								.append("<VASID>" + cancelReq.getVASID()
										+ "</VASID>");
					sb.append("</SenderIdentification>");
				}
				if (cancelReq.isSenderAddressExist())
					sb.append("<SenderAddress>" + cancelReq.getSenderAddress()
							+ "</SenderAddress>");
				if (cancelReq.isMessageIDExist())
					sb.append("<MessageID>" + cancelReq.getMessageID()
							+ "</MessageID>");
				else
					System.out.println("��ȡ������Ϣ�ı�ʶ��MessageID����Ϊ�գ�");
				sb.append("</CancelReq>");
				sb.append("</env:Body></env:Envelope>");
				// Cancel by hudm 2004-06-21
				// sb.append("----NextPart_0_2817_24856--");
				// end Cancel by hudm 2004-06-21
				byteOutput.write(sb.toString().getBytes());
			}
			/**
			 * �����͵���ϢΪMM7ReplaceReq
			 */
			else if (mm7VaspReq instanceof MM7ReplaceReq)
			{
				MM7ReplaceReq replaceReq = (MM7ReplaceReq) mm7VaspReq;
				sb
						.append("<ReplaceReq xmlns:mm7=\"http://www.3gpp.org/ftp/Specs/archive/23_series/23.140/schema/REL-6-MM7-1-0\" env:mustUnderstand=\"1\">");
				if (replaceReq.isMM7VersionExist())
					sb.append("<MM7Version>" + replaceReq.getMM7Version()
							+ "</MM7Version>");
				if (replaceReq.isVASPIDExist() || replaceReq.isVASIDExist())
				{
					sb.append("<SenderIdentification>");
					if (replaceReq.isVASPIDExist())
						sb.append("<VASPID>" + replaceReq.getVASPID()
								+ "</VASPID>");
					if (replaceReq.isVASIDExist())
						sb.append("<VASID>" + replaceReq.getVASID()
								+ "</VASID>");
					sb.append("</SenderIdentification>");
				}
				if (replaceReq.isMessageIDExist())
					sb.append("<MessageID>" + replaceReq.getMessageID()
							+ "</MessageID>");
				else
					System.out.println("����ǰ��Ϣ���滻����Ϣ�ı�ʶ��MessageID ����Ϊ�գ�");
				if (replaceReq.isServiceCodeExist())
					sb.append("<ServiceCode>" + replaceReq.getServiceCode()
							+ "</ServiceCode>");
				if (replaceReq.isTimeStampExist())
					sb.append("<TimeStamp>"
							+ sdf.format(replaceReq.getTimeStamp())
							+ "</TimeStamp>");
				if (replaceReq.isEarliestDeliveryTimeExist())
				{
					String earliestTime = sdf.format(new Date(replaceReq
							.getEarliestDeliveryTimeRelative()))
							+ "+08:00";
					sb.append("<EarliestDeliveryTime>" + earliestTime
							+ "</EarliestDeliveryTime>");
				}
				if (replaceReq.isEarliestDeliveryTimeAbsoluteExist())
					sb.append("<EarliestDeliveryTime>"
							+ sdf.format(replaceReq
									.getEarliestDeliveryTimeAbsolute())
							+ "+08:00" + "</EarliestDeliveryTime>");
				if (replaceReq.isReadReplyExist())
					sb.append("<ReadReply>" + replaceReq.getReadReply()
							+ "</ReadReply>");
				if (replaceReq.isDistributionIndicatorExist())
					sb.append("<DistributionIndicator>"
							+ replaceReq.getDistributionIndicator()
							+ "</DistributionIndicator>");
				if (replaceReq.isContentExist())
				{
					/*
					 * if(replaceReq.getContent().isContentIDExist())
					 * sb.append("<Content
					 * href=\"cid:"+replaceReq.getContent().getContentID()+"\"");
					 * if(replaceReq.isAllowAdaptationsExist())
					 * sb.append(";allowAdaptions=\"True\"/>"); else
					 * sb.append("/>");
					 */// ;allowAdaptions=\"False\"*/
					sb.append("</ReplaceReq>");
					sb.append("</env:Body></env:Envelope>" + "\r\n");
					sb.append("----NextPart_0_2817_24856\r\n");
					MMContent parentContent = replaceReq.getContent();
					if (parentContent.getContentType() != null)
					{
						/*
						 * sb.append("Content-Type:" +
						 * parentContent.getContentType().getPrimaryType() +"/"
						 * +parentContent.getContentType().getSubType()+
						 * ";boundary=\"SubPart_7452684322002_77645\"" +
						 * "\r\n");
						 */
						String strSubType = "";
						String strtempID = "<START>";
						strSubType = parentContent.getContentType()
								.getSubType();
						if (strSubType.equalsIgnoreCase("related"))
						{
							if (parentContent.isMultipart())
							{
								List tempSub = new ArrayList();
								tempSub = parentContent.getSubContents();
								for (int x = 0; x < tempSub.size(); x++)
								{
									MMContent tempCon = (MMContent) tempSub
											.get(x);
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
							sb
									.append("Content-Type:"
											+ "multipart/related;"
											+ "start=\""
											+ strtempID
											+ "\";type=\"application/smil\""
											+ ";boundary=\"SubPart_7452684322002_77645\""
											+ "\r\n");
						}
						else
						{
							sb
									.append("Content-Type:"
											+ parentContent.getContentType()
													.getPrimaryType()
											+ "/"
											+ parentContent.getContentType()
													.getSubType()
											+ ";boundary=\"SubPart_7452684322002_77645\""
											+ "\r\n");
						}

					}
					else
					{
						sb.append("Content-Type:" + "multipart/mixed"
								+ ";boundary=\"SubPart_7452684322002_77645\""
								+ "\r\n");
					}
					sb.append("Content-Transfer-Encoding:8bit" + "\r\n");
					if (parentContent.isContentIDExist())
						sb.append("Content-ID:" + parentContent.getContentID()
								+ "\r\n");
					else
						sb.append("Content-ID:<SaturnPics-01020930>" + "\r\n");
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
							ContentBuffer
									.append("----SubPart_7452684322002_77645"
											+ "\r\n");
							MMContent content = (MMContent) subContent.get(i);
							if (content.getContentType() != null)
								ContentBuffer.append("Content-Type:"
										+ content.getContentType()
												.getPrimaryType() + "/"
										+ content.getContentType().getSubType()
										+ "\r\n");
							else
							{
								if (content.isContentIDExist())
								{
									String strContentID = content
											.getContentID();
									int index = strContentID.indexOf(".");
									String type = strContentID
											.substring(index + 1);
									type = type.toLowerCase();
									if (type.equals("txt"))
									{
										ContentBuffer
												.append("Content-Type:text/plain"
														+ "\r\n");
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
							ContentBuffer
									.append("Content-Transfer-Encoding:8bit"
											+ "\r\n");
							if (content.isContentIDExist())
								ContentBuffer.append("Content-ID:"
										+ content.getContentID() + "\r\n");
							if (content.isContentLocationExist())
								ContentBuffer
										.append("Content-Location:"
												+ content.getContentLocation()
												+ "\r\n");
							ContentBuffer.append("\r\n");
							try
							{
								Subbaos.write(ContentBuffer.toString()
										.getBytes());
								Subbaos.write(content.getContent());
								Subbaos.write("\r\n\r\n".getBytes());
							}
							catch (IOException e)
							{
								e.printStackTrace();
							}
						}
						Subbaos.write("--SubPart_7452684322002_77645--\r\n"
								.getBytes());
						Subbaos.write("----NextPart_0_2817_24856--".getBytes());
						byteOutput.write(sb.toString().getBytes());
						byteOutput.write(Subbaos.toByteArray());
					}
				}
				else
				{
					sb.append("</ReplaceReq>");
					sb.append("</env:Body></env:Envelope>");
					// Cancel by hudm 2004-06-21
					// sb.append("----NextPart_0_2817_24856--");
					// end Cancel by hudm 2004-06-21
					byteOutput.write(sb.toString().getBytes());
				}
			}
			// bEncoder = true;
		}
		catch (Exception e)
		{
			System.err.println(e);
		}
		return byteOutput.toByteArray();
	}
	
	
	public static byte[] encodeVASPResMessage(MM7VASPRes mm7VASPRes,String charset)
	{
		ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
		StringBuffer sb = new StringBuffer();

		sb
				.append("<?xml version=\"1.0\"?><env:Envelope xmlns:env=\"http://schemas.xmlsoap.org/soap/envelope/\"><env:Header>");//
		if (mm7VASPRes.isTransactionIDExist()) //
			sb
					.append("<mm7:TransactionID xmlns:mm7=\"http://www.3gpp.org/ftp/Specs/archive/23_series/23.140/schema/REL-6-MM7-1-0\">"
							+ mm7VASPRes.getTransactionID()
							+ "</mm7:TransactionID>");
		else
			System.err.println("TransactionID ����Ϊ�գ�");
		sb.append("</env:Header><env:Body>");

		/**
		 * �����͵���ϢΪMM7SubmitReq
		 */
		if (mm7VASPRes instanceof MM7DeliverRes)
		{
			MM7DeliverRes deliverRes = (MM7DeliverRes) mm7VASPRes;
			sb
					.append("<DeliverRsp xmlns=\"http://www.3gpp.org/ftp/Specs/archive/23_series/23.140/schema/REL-6-MM7-1-0\">");
			if (deliverRes.isMM7VersionExist())
			{
				sb.append("<MM7Version>" + deliverRes.getMM7Version()
						+ "</MM7Version>");
			}
			else
			{
				System.err.println("MM7Version ����Ϊ�գ�");
			}

			if (deliverRes.isServiceCodeExist())
			{
				sb.append("<ServiceCode>" + deliverRes.getServiceCode()
						+ "</ServiceCode>");
			}

			sb.append("<Status>");
			if (deliverRes.isStatusCodeExist())
			{
				sb.append("<StatusCode>" + deliverRes.getStatusCode()
						+ "</StatusCode>");
			}
			else
			{
				System.err.println("StatusCode ����Ϊ��");
			}

			if (deliverRes.isStatusTextExist())
			{
				sb.append("<StatusText>" + deliverRes.getStatusText()
						+ "</StatusText>");
			}
			if (deliverRes.isStatusDetailExist())
			{
				sb.append("<Details>" + deliverRes.getStatusDetail()
						+ "</Details>");
			}
			sb.append("</Status>");

			sb.append("</DeliverRsp>");
			sb.append("</env:Body></env:Envelope>");
			sb.append("\r\n");
		}
		else if (mm7VASPRes instanceof MM7DeliveryReportRes)
		{
			MM7DeliveryReportRes deliveryReportRes = (MM7DeliveryReportRes) mm7VASPRes;
			sb
					.append("<DeliveryReportRsp xmlns=\"http://www.3gpp.org/ftp/Specs/archive/23_series/23.140/schema/REL-6-MM7-1-0\">");
			if (deliveryReportRes.isMM7VersionExist())
			{
				sb.append("<MM7Version>" + deliveryReportRes.getMM7Version()
						+ "</MM7Version>");
			}
			else
			{
				System.err.println("MM7Version ����Ϊ�գ�");
			}

			sb.append("<Status>");
			if (deliveryReportRes.isStatusCodeExist())
			{
				sb.append("<StatusCode>" + deliveryReportRes.getStatusCode()
						+ "</StatusCode>");
			}
			else
			{
				System.err.println("StatusCode ����Ϊ��");
			}

			if (deliveryReportRes.isStatusTextExist())
			{
				sb.append("<StatusText>" + deliveryReportRes.getStatusText()
						+ "</StatusText>");
			}
			if (deliveryReportRes.isStatusDetailExist())
			{
				sb.append("<Details>" + deliveryReportRes.getStatusDetail()
						+ "</Details>");
			}
			sb.append("</Status>");

			sb.append("</DeliveryReportRsp>");
			sb.append("</env:Body></env:Envelope>");
			sb.append("\r\n");
		}
		else if (mm7VASPRes instanceof MM7ReadReplyRes)
		{
			MM7ReadReplyRes readReplyRes = (MM7ReadReplyRes) mm7VASPRes;
			sb
					.append("<ReadReplyRsp xmlns=\"http://www.3gpp.org/ftp/Specs/archive/23_series/23.140/schema/REL-6-MM7-1-0\">");
			if (readReplyRes.isMM7VersionExist())
			{
				sb.append("<MM7Version>" + readReplyRes.getMM7Version()
						+ "</MM7Version>");
			}
			else
			{
				System.err.println("MM7Version ����Ϊ�գ�");
			}

			sb.append("<Status>");
			if (readReplyRes.isStatusCodeExist())
			{
				sb.append("<StatusCode>" + readReplyRes.getStatusCode()
						+ "</StatusCode>");
			}
			else
			{
				System.err.println("StatusCode ����Ϊ��");
			}

			if (readReplyRes.isStatusTextExist())
			{
				sb.append("<StatusText>" + readReplyRes.getStatusText()
						+ "</StatusText>");
			}
			if (readReplyRes.isStatusDetailExist())
			{
				sb.append("<Details>" + readReplyRes.getStatusDetail()
						+ "</Details>");
			}
			sb.append("</Status>");

			sb.append("</ReadReplyRsp>");
			sb.append("</env:Body></env:Envelope>");
			sb.append("\r\n");
		}
		try
		{
			byteOutput.write(sb.toString().getBytes());
		}
		catch (Exception e)
		{
			log.error(null, e);
		}

		return byteOutput.toByteArray();
	}

	

	/** ����BASE64���� */
	public static String getBASE64(String value)
	{
		if (value == null)
			return null;
		BASE64Encoder BaseEncode = new BASE64Encoder();
		return (BaseEncode.encode(value.getBytes()));
	}
}
