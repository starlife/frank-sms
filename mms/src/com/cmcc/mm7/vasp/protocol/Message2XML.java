/**
 * File Name:SOAPEncoder.java Company: 中国移动集团公司 Date : 2004-1-8
 */

package com.cmcc.mm7.vasp.protocol;

import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cmcc.mm7.vasp.message.MM7CancelReq;
import com.cmcc.mm7.vasp.message.MM7DeliverRes;
import com.cmcc.mm7.vasp.message.MM7DeliveryReportRes;
import com.cmcc.mm7.vasp.message.MM7ReadReplyRes;
import com.cmcc.mm7.vasp.message.MM7ReplaceReq;
import com.cmcc.mm7.vasp.message.MM7SubmitReq;
import com.cmcc.mm7.vasp.message.MM7VASPErrorRes;

/**
 * 把MM7VASPReq消息和MM7VASPRes消息解析为Byte[]形式
 * 
 * @author Administrator
 */
public class Message2XML
{
	private static final Log log = LogFactory.getLog(Message2XML.class);

	/** 默认构造方法 */
	public Message2XML()
	{
	}

	public static String deliveryReportRes2Xml(
			MM7DeliveryReportRes deliveryReportRes, Charset charset)
	{

		StringBuffer sb = new StringBuffer();

		addSoapXmlHeader(sb, charset, deliveryReportRes.getTransactionID());
		sb
				.append("<DeliveryReportRsp xmlns=\"http://www.3gpp.org/ftp/Specs/archive/23_series/23.140/schema/REL-6-MM7-1-0\">");
		if (deliveryReportRes.isMM7VersionExist())
		{
			sb.append("<MM7Version>" + deliveryReportRes.getMM7Version()
					+ "</MM7Version>");
		}
		else
		{
			log.error("MM7Version 不许为空！");
		}

		sb.append("<Status>");
		if (deliveryReportRes.isStatusCodeExist())
		{
			sb.append("<StatusCode>" + deliveryReportRes.getStatusCode()
					+ "</StatusCode>");
		}
		else
		{
			log.error("StatusCode 不许为空");
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
		addSoapXmlFooter(sb);
		return sb.toString();
	}

	public static String readReplyRes2Xml(MM7ReadReplyRes readReplyRes,
			Charset charset)
	{

		StringBuffer sb = new StringBuffer();

		addSoapXmlHeader(sb, charset, readReplyRes.getTransactionID());

		sb
				.append("<ReadReplyRsp xmlns=\"http://www.3gpp.org/ftp/Specs/archive/23_series/23.140/schema/REL-6-MM7-1-0\">");
		if (readReplyRes.isMM7VersionExist())
		{
			sb.append("<MM7Version>" + readReplyRes.getMM7Version()
					+ "</MM7Version>");
		}
		else
		{
			System.err.println("MM7Version 不许为空！");
		}

		sb.append("<Status>");
		if (readReplyRes.isStatusCodeExist())
		{
			sb.append("<StatusCode>" + readReplyRes.getStatusCode()
					+ "</StatusCode>");
		}
		else
		{
			System.err.println("StatusCode 不许为空");
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
		addSoapXmlFooter(sb);
		return sb.toString();
	}

	public static String deliverRes2Xml(MM7DeliverRes deliverRes,
			Charset charset)
	{
		// frame.MGlobal.getInstance().charset.
		StringBuffer sb = new StringBuffer();

		addSoapXmlHeader(sb, charset, deliverRes.getTransactionID());

		sb
				.append("<DeliverRsp xmlns=\"http://www.3gpp.org/ftp/Specs/archive/23_series/23.140/schema/REL-6-MM7-1-0\">");

		sb
				.append("<MM7Version>" + deliverRes.getMM7Version()
						+ "</MM7Version>");

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

		if (deliverRes.isStatusTextExist())
		{
			sb.append("<StatusText>" + deliverRes.getStatusText()
					+ "</StatusText>");
		}
		if (deliverRes.isStatusDetailExist())
		{
			sb
					.append("<Details>" + deliverRes.getStatusDetail()
							+ "</Details>");
		}
		sb.append("</Status>");

		sb.append("</DeliverRsp>");
		addSoapXmlFooter(sb);
		return sb.toString();

	}
	
	public static String vaspErrorRes2Xml(MM7VASPErrorRes deliverRes,
			Charset charset)
	{
		// frame.MGlobal.getInstance().charset.
		StringBuffer sb = new StringBuffer();

		addSoapXmlHeader(sb, charset, deliverRes.getTransactionID());

		sb
				.append("<MM7VASPErrorRsp xmlns=\"http://www.3gpp.org/ftp/Specs/archive/23_series/23.140/schema/REL-6-MM7-1-0\">");

		sb
				.append("<MM7Version>" + deliverRes.getMM7Version()
						+ "</MM7Version>");

		
		sb.append("<Status>");
		if (deliverRes.isStatusCodeExist())
		{
			sb.append("<StatusCode>" + deliverRes.getStatusCode()
					+ "</StatusCode>");
		}

		if (deliverRes.isStatusTextExist())
		{
			sb.append("<StatusText>" + deliverRes.getStatusText()
					+ "</StatusText>");
		}
		if (deliverRes.isStatusDetailExist())
		{
			sb
					.append("<Details>" + deliverRes.getStatusDetail()
							+ "</Details>");
		}
		sb.append("</Status>");

		sb.append("</MM7VASPErrorRsp>");
		addSoapXmlFooter(sb);
		return sb.toString();

	}

	public static String submitReq2Xml(MM7SubmitReq mm7VaspReq, Charset charset)
	{
		StringBuffer sb = new StringBuffer();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

		addSoapXmlHeader(sb, charset, mm7VaspReq.getTransactionID());
		/**
		 * 若发送的消息为MM7SubmitReq
		 */

		sb
				.append("<SubmitReq xmlns=\"http://www.3gpp.org/ftp/Specs/archive/23_series/23.140/schema/REL-6-MM7-1-0\">");

		sb
				.append("<MM7Version>" + mm7VaspReq.getMM7Version()
						+ "</MM7Version>");

		sb.append("<SenderIdentification>");

		sb.append("<VASPID>" + mm7VaspReq.getVASPID() + "</VASPID>");

		sb.append("<VASID>" + mm7VaspReq.getVASID() + "</VASID>");

		if (mm7VaspReq.isSenderAddressExist())
		{
			sb.append("<SenderAddress>" + mm7VaspReq.getSenderAddress()
					+ "</SenderAddress>");
		}
		sb.append("</SenderIdentification>");

		if (mm7VaspReq.isToExist() || mm7VaspReq.isCcExist()
				|| mm7VaspReq.isBccExist())
		{
			sb.append("<Recipients>");
			if (mm7VaspReq.isToExist())
			{
				sb.append("<To>");
				
				List<String> ToList = mm7VaspReq.getTo();
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
			if (mm7VaspReq.isCcExist())
			{
				sb.append("<Cc>");
				
				List<String> CcList = mm7VaspReq.getCc();
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
			if (mm7VaspReq.isBccExist())
			{
				sb.append("<Bcc>");
				List<String> BccList = mm7VaspReq.getBcc();
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

		if (mm7VaspReq.isServiceCodeExist())
			sb.append("<ServiceCode>" + mm7VaspReq.getServiceCode()
					+ "</ServiceCode>");
		else
			System.out.println("业务代码ServiceCode不许为空！");

		if (mm7VaspReq.isLinkedIDExist())
			sb.append("<LinkedID>" + mm7VaspReq.getLinkedID() + "</LinkedID>");
		if (mm7VaspReq.isMessageClassExist())
			sb.append("<MessageClass>" + mm7VaspReq.getMessageClass()
					+ "</MessageClass>");
		if (mm7VaspReq.isTimeStampExist())
			sb.append("<TimeStamp>" + sdf.format(mm7VaspReq.getTimeStamp())
					+ "</TimeStamp>");
		if (mm7VaspReq.isReplyChargingExist())
		{
			sb.append("<ReplyCharging");
			if (mm7VaspReq.isReplyChargingSizeExist())
				sb.append(" replyChargingSize=\""
						+ mm7VaspReq.getReplyChargingSize() + "\"");
			if (mm7VaspReq.isReplyDeadlineExist())
				sb.append(" replyDeadline=\""
						+ sdf.format(new Date(mm7VaspReq
								.getReplyDeadlineRelative())) + "+08:00\"");
			if (mm7VaspReq.isReplyDeadlineAbsoluteExist())
				sb.append(" replyDeadline=\""
						+ sdf.format(mm7VaspReq.getReplyDeadlineAbsolute())
						+ "+08:00\"");
			sb.append("></ReplyCharging>");
		}
		if (mm7VaspReq.isEarliestDeliveryTimeExist())
		{
			Date dd = new Date(mm7VaspReq.getEarliestDeliveryTimeRelative());
			sb.append("<EarliestDeliveryTime>" + sdf.format(dd) + "+08:00"
					+ "</EarliestDeliveryTime>");
		}
		if (mm7VaspReq.isEarliestDeliveryTimeAbsolute())
			sb.append("<EarliestDeliveryTime>"
					+ sdf.format(mm7VaspReq.getEarliestDeliveryTimeAbsolute())
					+ "+08:00" + "</EarliestDeliveryTime>");
		if (mm7VaspReq.isExpiryDateExist())
			sb.append("<ExpiryDate>"
					+ sdf.format(new Date(mm7VaspReq.getExpiryDateRelative()))
					+ "+08:00" + "</ExpiryDate>");
		if (mm7VaspReq.isExpiryDateAbsolute())
			sb.append("<ExpiryDate>"
					+ sdf.format(mm7VaspReq.getExpiryDateAbsolute()) + "+08:00"
					+ "</ExpiryDate>");
		if (mm7VaspReq.isDeliveryReportExist())
			sb.append("<DeliveryReport>" + mm7VaspReq.getDeliveryReport()
					+ "</DeliveryReport>");
		if (mm7VaspReq.isReadReplyExist())
			sb.append("<ReadReply>" + mm7VaspReq.getReadReply()
					+ "</ReadReply>");
		/*
		 * if(submitReq.isReplyChargingExist()) sb.append("<ReplyCharging>"+submitReq.getReplyCharging()+"</ReplyCharging>");
		 * if(submitReq.isReplyDeadlineExist()) sb.append("<Reply-Deadline>"+sdf.format(new
		 * Date( submitReq.getReplyDeadlineRelative()))+"+08:00"+"</Reply-Deadline>");
		 * if(submitReq.isReplyDeadlineAbsoluteExist()) sb.append("<Reply-Deadline>"+sdf.format(
		 * submitReq.getReplyDeadlineAbsolute())+"+08:00"+"</Reply-Deadline>");
		 * if(submitReq.isReplyChargingSizeExist()) sb.append("<ReplyChargingSize>"+submitReq.getReplyChargingSize()+"</ReplyChargingSize>");
		 */

		if (mm7VaspReq.isPriorityExist())
		{
			// sb.append("<Priority>" + submitReq.getPriority() +
			// "</Priority>");
			if (mm7VaspReq.getPriority() == (byte) 0)
				sb.append("<Priority>" + "Low" + "</Priority>");
			else if (mm7VaspReq.getPriority() == (byte) 1)
				sb.append("<Priority>" + "Normal" + "</Priority>");
			else if (mm7VaspReq.getPriority() == (byte) 2)
				sb.append("<Priority>" + "High" + "</Priority>");
		}
		if (mm7VaspReq.isSubjectExist())
			sb.append("<Subject>" + mm7VaspReq.getSubject() + "</Subject>");
		if (mm7VaspReq.isChargedPartyExist())
		{
			// sb.append("<ChargedParty>"+submitReq.getChargedParty()+"</ChargedParty>");
			if (mm7VaspReq.getChargedParty() == (byte) 0)
				sb.append("<ChargedParty>Sender</ChargedParty>");
			else if (mm7VaspReq.getChargedParty() == (byte) 1)
				sb.append("<ChargedParty>Recipient</ChargedParty>");
			else if (mm7VaspReq.getChargedParty() == (byte) 2)
				sb.append("<ChargedParty>Both</ChargedParty>");
			else if (mm7VaspReq.getChargedParty() == (byte) 3)
				sb.append("<ChargedParty>Neither</ChargedParty>");
			else if (mm7VaspReq.getChargedParty() == (byte) 4)
				sb.append("<ChargedParty>ThirdParty</ChargedParty>");
		}
		if (mm7VaspReq.isChargedPartyIDExist())
			sb.append("<ChargedPartyID>" + mm7VaspReq.getChargedPartyID()
					+ "</ChargedPartyID>");
		if (mm7VaspReq.isDistributionIndicatorExist())
			sb.append("<DistributionIndicator>"
					+ mm7VaspReq.getDistributionIndicator()
					+ "</DistributionIndicator>");

		sb.append("</SubmitReq>");
		sb.append("</env:Body></env:Envelope>");
		return sb.toString();

	}

	public static String cancelReq2Xml(MM7CancelReq cancelReq, Charset charset)
	{

		// bEncoder = false;
		StringBuffer sb = new StringBuffer();
		//StringBuffer ContentBuffer = new StringBuffer();
		//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		// add by hudm 2004-06-21

		// end add by hudm 2004-06-21
		addSoapXmlHeader(sb, charset, cancelReq.getTransactionID());

		sb
				.append("<CancelReq xmlns:mm7=\"http://www.3gpp.org/ftp/Specs/archive/23_series/23.140/schema/REL-6-MM7-1-0\" env:mustUnderstand=\"1\">");
		if (cancelReq.isMM7VersionExist())
			sb.append("<MM7Version>" + cancelReq.getMM7Version()
					+ "</MM7Version>");
		else
			System.out.println("MM7Version 不许为空！");
		if (cancelReq.isVASPIDExist() || cancelReq.isVASIDExist())
		{
			sb.append("<SenderIdentification>");
			if (cancelReq.isVASPIDExist())
				sb.append("<VASPID>" + cancelReq.getVASPID() + "</VASPID>");
			if (cancelReq.isVASIDExist())
				sb.append("<VASID>" + cancelReq.getVASID() + "</VASID>");
			sb.append("</SenderIdentification>");
		}
		if (cancelReq.isSenderAddressExist())
			sb.append("<SenderAddress>" + cancelReq.getSenderAddress()
					+ "</SenderAddress>");
		if (cancelReq.isMessageIDExist())
			sb
					.append("<MessageID>" + cancelReq.getMessageID()
							+ "</MessageID>");
		else
			System.out.println("待取消的消息的标识符MessageID不许为空！");
		sb.append("</CancelReq>");
		addSoapXmlFooter(sb);

		return sb.toString();

	}

	public static String replaceReq2Xml(MM7ReplaceReq replaceReq,
			Charset charset)
	{

		// bEncoder = false;
		StringBuffer sb = new StringBuffer();
		//StringBuffer ContentBuffer = new StringBuffer();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		// add by hudm 2004-06-21

		// end add by hudm 2004-06-21
		addSoapXmlHeader(sb, charset, replaceReq.getTransactionID());

		// bEncoder = false;

		sb
				.append("<ReplaceReq xmlns:mm7=\"http://www.3gpp.org/ftp/Specs/archive/23_series/23.140/schema/REL-6-MM7-1-0\" env:mustUnderstand=\"1\">");
		if (replaceReq.isMM7VersionExist())
			sb.append("<MM7Version>" + replaceReq.getMM7Version()
					+ "</MM7Version>");
		if (replaceReq.isVASPIDExist() || replaceReq.isVASIDExist())
		{
			sb.append("<SenderIdentification>");
			if (replaceReq.isVASPIDExist())
				sb.append("<VASPID>" + replaceReq.getVASPID() + "</VASPID>");
			if (replaceReq.isVASIDExist())
				sb.append("<VASID>" + replaceReq.getVASID() + "</VASID>");
			sb.append("</SenderIdentification>");
		}
		if (replaceReq.isMessageIDExist())
			sb.append("<MessageID>" + replaceReq.getMessageID()
					+ "</MessageID>");
		else
			System.out.println("被当前消息所替换的消息的标识符MessageID 不许为空！");
		if (replaceReq.isServiceCodeExist())
			sb.append("<ServiceCode>" + replaceReq.getServiceCode()
					+ "</ServiceCode>");
		if (replaceReq.isTimeStampExist())
			sb.append("<TimeStamp>" + sdf.format(replaceReq.getTimeStamp())
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
					+ sdf.format(replaceReq.getEarliestDeliveryTimeAbsolute())
					+ "+08:00" + "</EarliestDeliveryTime>");
		if (replaceReq.isReadReplyExist())
			sb.append("<ReadReply>" + replaceReq.getReadReply()
					+ "</ReadReply>");
		if (replaceReq.isDistributionIndicatorExist())
			sb.append("<DistributionIndicator>"
					+ replaceReq.getDistributionIndicator()
					+ "</DistributionIndicator>");

		// bEncoder = true;

		addSoapXmlFooter(sb);

		return sb.toString();

	}
	
	
	
	private static void addSoapXmlHeader(StringBuffer sb, Charset charset,
			String transcationid)
	{
		sb.append("<?xml version=\"1.0\" encoding=\"" + charset + "\"?>");
		sb
				.append("<env:Envelope xmlns:env=\"http://schemas.xmlsoap.org/soap/envelope/\">");

		sb
				.append("<env:Header><mm7:TransactionID xmlns:mm7=\"http:www.3gpp.org/ftp/Specs/archive/23_series/23.140/schema/REL-6-MM7-1-0\" nv:mustUnderstand=\"1\">"
						+ transcationid
						+ "</mm7:TransactionID></env:Header><env:Body>");
	}

	private static void addSoapXmlFooter(StringBuffer sb)
	{
		sb.append("</env:Body></env:Envelope>");

	}

}
