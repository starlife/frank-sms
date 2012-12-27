package com.cmcc.mm7.vasp.protocol;

import java.io.ByteArrayInputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import com.cmcc.mm7.vasp.common.MMConstants;
import com.cmcc.mm7.vasp.protocol.message.MM7CancelRes;
import com.cmcc.mm7.vasp.protocol.message.MM7DeliverReq;
import com.cmcc.mm7.vasp.protocol.message.MM7DeliveryReportReq;
import com.cmcc.mm7.vasp.protocol.message.MM7RSErrorRes;
import com.cmcc.mm7.vasp.protocol.message.MM7RSReq;
import com.cmcc.mm7.vasp.protocol.message.MM7RSRes;
import com.cmcc.mm7.vasp.protocol.message.MM7ReadReplyReq;
import com.cmcc.mm7.vasp.protocol.message.MM7ReplaceRes;
import com.cmcc.mm7.vasp.protocol.message.MM7SubmitRes;

public class SOAPDecoder
{
	private static final Log log = LogFactory.getLog(SOAPDecoder.class);

	/** 对SOAP的Envelope部分进行解码 */
	public static MM7RSReq parseReqXML(byte[] xmlBytes)
	{
		MM7RSReq mm7rsReq = null;

		SAXBuilder sax = new SAXBuilder();
		try
		{
			ByteArrayInputStream in = new ByteArrayInputStream(xmlBytes);
			Document doc = sax.build(in);
			Element root = doc.getRootElement();
			Element envBody = (Element) root.getChildren().get(1);
			Element Message = (Element) envBody.getChildren().get(0);
			mm7rsReq = DecodeBody(Message);
			Element envHeader = (Element) root.getChildren().get(0);
			Element transID = (Element) envHeader.getChildren().get(0);
			String transactionID = transID.getTextTrim();
			if (transactionID != null || !transactionID.equals(""))
			{
				mm7rsReq.setTransactionID(transactionID);
			}
			else
			{
				log.error("TransactionID必须有。");
			}
			return mm7rsReq;
		}
		catch (JDOMException jdome)
		{
			log.error(null, jdome);
			return null;
		}
		catch (Exception e)
		{
			// System.out.println("e=" + e);
			log.error(null, e);
			return null;
		}

	}

	private static MM7DeliverReq parseMM7DeliverReq(Element deliver)
	{

		MM7DeliverReq deliverReq = new MM7DeliverReq();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

		int size = deliver.getChildren().size();
		for (int i = 0; i < size; i++)
		{
			Element child = (Element) deliver.getChildren().get(i);
			String name = child.getName();
			if (name.equals("Sender"))
			{
				// Element sub = (Element)child.getChildren().get(0);
				deliverReq.setSender(child.getTextTrim());
				// deliverReq.setSender(sub.getTextTrim());

			}
			else if (name.equals("Recipients"))
			{
				parseRecipients(deliverReq, child);
			}
			else if (name.equals("LinkedID"))
			{
				deliverReq.setLinkedID(child.getTextTrim());

			}
			else if (name.equals("MMSRelayServerID"))
			{
				deliverReq.setMMSRelayServerID(child.getTextTrim());

			}
			else if (name.equals("ReplyChargingID"))
			{
				deliverReq.setReplyChargingID(child.getTextTrim());

			}
			else if (name.equals("Subject"))
			{
				deliverReq.setSubject(child.getTextTrim());

			}
			else if (name.equals("Priority"))
			{
				if (child.getTextTrim().equalsIgnoreCase("Low"))
					deliverReq.setPriority((byte) 0);
				else if (child.getTextTrim().equalsIgnoreCase("Normal"))
					deliverReq.setPriority((byte) 1);
				else if (child.getTextTrim().equalsIgnoreCase("High"))
					deliverReq.setPriority((byte) 2);

			}
			else if (name.equals("TimeStamp"))
			{
				String time = child.getTextTrim();
				if (time.length() > 19)
				{
					time = time.substring(0, 19);
				}
				try
				{
					deliverReq.setTimeStamp(sdf.parse(time));
				}
				catch (ParseException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
		return deliverReq;

	}

	private static MM7DeliveryReportReq parseMM7DeliveryReportReq(
			Element deliveryReport)
	{

		MM7DeliveryReportReq deliveryReportReq = new MM7DeliveryReportReq();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

		int size = deliveryReport.getChildren().size();

		for (int i = 0; i < size; i++)
		{
			Element child = (Element) deliveryReport.getChildren().get(i);
			String name = child.getName();
			if (name.equals("MMSRelayServerID"))
			{
				deliveryReportReq.setMMSRelayServerID(child.getTextTrim());

			}
			else if (name.equals("MessageID"))
			{
				deliveryReportReq.setMessageID(child.getTextTrim());

			}
			else if (name.equals("Recipient"))
			{
				int recsize = child.getChildren().size();
				for (int j = 0; j < recsize; j++)
				{
					Element rec = (Element) child.getChildren().get(j);
					deliveryReportReq.setRecipient(rec.getTextTrim());

				}
			}
			else if (name.equals("Sender"))
			{
				deliveryReportReq.setSender(child.getTextTrim());

			}
			else if (name.equals("TimeStamp"))
			{
				String time = child.getTextTrim();
				if (time.length() > 19)
				{
					time = time.substring(0, 19);
				}
				try
				{
					deliveryReportReq.setTimeStamp(sdf.parse(time));
				}
				catch (ParseException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			else if (name.equals("MMStatus"))
			{
				if (child.getTextTrim().equalsIgnoreCase("Expired"))
					deliveryReportReq.setMMStatus((byte) 0);
				else if (child.getTextTrim().equalsIgnoreCase("Retrieved"))
					deliveryReportReq.setMMStatus((byte) 1);
				else if (child.getTextTrim().equalsIgnoreCase("Rejected"))
					deliveryReportReq.setMMStatus((byte) 2);
				else if (child.getTextTrim()
						.equalsIgnoreCase("System Rejected"))
					deliveryReportReq.setMMStatus((byte) 2);
				else if (child.getTextTrim().equalsIgnoreCase(
						"Recipient Rejected"))
					deliveryReportReq.setMMStatus((byte) 3);
				else if (child.getTextTrim().equalsIgnoreCase("Indeterminate"))
					deliveryReportReq.setMMStatus((byte) 4);
				else if (child.getTextTrim().equalsIgnoreCase("Forwarded"))
					deliveryReportReq.setMMStatus((byte) 5);
				else
					deliveryReportReq.setMMStatus((byte) 4);

			}
			else if (name.equals("StatusText"))
			{
				deliveryReportReq.setStatusText(child.getTextTrim());
			}
		}
		return deliveryReportReq;

	}

	private static MM7ReadReplyReq parseMM7ReadReplyReq(Element readReply)
	{

		MM7ReadReplyReq readReplyReq = new MM7ReadReplyReq();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

		int size = readReply.getChildren().size();

		for (int i = 0; i < size; i++)
		{
			Element child = (Element) readReply.getChildren().get(i);
			String name = child.getName();
			if (name.equals("MMSReplayServerID"))
			{
				readReplyReq.setMMSRelayServerID(child.getTextTrim());

			}
			else if (name.equals("MessageID"))
			{
				readReplyReq.setMessageID(child.getTextTrim());

			}
			else if (name.equals("Recipient"))
			{
				int recsize = child.getChildren().size();
				for (int j = 0; j < recsize; j++)
				{
					Element rec = (Element) child.getChildren().get(j);
					readReplyReq.setRecipient(rec.getTextTrim());

				}
			}
			else if (name.equals("Sender"))
			{
				readReplyReq.setSender(child.getTextTrim());

			}
			else if (name.equals("TimeStamp"))
			{
				String time = child.getTextTrim();
				if (time.length() > 19)
				{
					time = time.substring(0, 19);
				}
				try
				{
					readReplyReq.setTimeStamp(sdf.parse(time));
				}
				catch (ParseException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			else if (name.equals("MMStatus"))
			{
				String strReadStatus = child.getTextTrim().trim();
				byte bReadStatus = 2;
				if (strReadStatus.equalsIgnoreCase("read"))
					bReadStatus = 0;
				else if (strReadStatus.equalsIgnoreCase("deleted"))
					bReadStatus = 1;
				else
					bReadStatus = 2;
				readReplyReq.setMMStatus(bReadStatus);
				// readReplyReq.setMMStatus(Byte.parseByte(child.getTextTrim()));

			}
			else if (name.equals("StatusText"))
			{
				readReplyReq.setStatusText(child.getTextTrim());

			}
		}
		return readReplyReq;

	}

	/** 对soap Envelope中的env:Body部分进行解码 */
	private static MM7RSReq DecodeBody(Element message)
	{

		String messageType = message.getName();

		if (messageType.equals("DeliverReq"))
		{
			return parseMM7DeliverReq(message);
		}
		else if (messageType.equals("DeliveryReportReq"))
		{
			return parseMM7DeliveryReportReq(message);
		}
		else if (messageType.equals("ReadReplyReq"))
		{
			return parseMM7ReadReplyReq(message);
		}

		return null;

	}

	private static void parseRecipients(MM7DeliverReq deliverReq,
			Element recipients)
	{
		List<String> To = new ArrayList<String>();
		List<String> Cc = new ArrayList<String>();
		List<String> Bcc = new ArrayList<String>();
		int recsize = recipients.getChildren().size();
		for (int j = 0; j < recsize; j++)
		{
			Element rec = (Element) recipients.getChildren().get(j);
			if (rec.getName().equals("To"))
			{
				int tosize = rec.getChildren().size();
				for (int m = 0; m < tosize; m++)
				{
					Element ele = (Element) rec.getChildren().get(m);
					if (ele.getName().equals("Number")
							|| ele.getName().equals("RFC2822Address")
							|| ele.getName().equals("ShortCode"))
						To.add(ele.getTextTrim());
				}

			}
			else if (rec.getName().equals("Cc"))
			{
				int ccsize = rec.getChildren().size();
				for (int m = 0; m < ccsize; m++)
				{
					Element ele = (Element) rec.getChildren().get(m);
					if (ele.getName().equals("Number")
							|| ele.getName().equals("RFC2822Address")
							|| ele.getName().equals("ShortCode"))
						Cc.add(ele.getTextTrim());
				}

			}
			else if (rec.getName().equals("Bcc"))
			{
				int bccsize = rec.getChildren().size();
				for (int m = 0; m < bccsize; m++)
				{
					Element ele = (Element) rec.getChildren().get(m);
					if (ele.getName().equals("Number")
							|| ele.getName().equals("RFC2822Address")
							|| ele.getName().equals("ShortCode"))
						Bcc.add(ele.getTextTrim());
				}

			}
		}
		if (!To.isEmpty())
			deliverReq.setTo(To);
		if (!Cc.isEmpty())
			deliverReq.setCc(Cc);
		if (!Bcc.isEmpty())
			deliverReq.setBcc(Bcc);

	}

	public static MM7RSRes parseResXML(byte[] xmlBytes)
	{
		SAXBuilder sax = new SAXBuilder();
		try
		{

			ByteArrayInputStream in = new ByteArrayInputStream(xmlBytes);
			// log.debug("sax.build(in) 之前:"+System.currentTimeMillis());
			Document doc = sax.build(in);
			// log.debug("sax.build(in) 之后:"+System.currentTimeMillis());
			Element root = doc.getRootElement();
			Element envHeader = (Element) root.getChildren().get(0);
			Element envBody = (Element) root.getChildren().get(1);
			if (!envHeader.getName().equalsIgnoreCase("Header"))
			{
				MM7RSErrorRes errRes = new MM7RSErrorRes();
				errRes.setStatusCode(-110);
				errRes.setStatusText("缺少Header元素");
				return errRes;
			}
			// 消息头
			Element transID = (Element) envHeader.getChildren().get(0);
			String transactionID = transID.getTextTrim();
			// 消息体
			Element message = (Element) envBody.getChildren().get(0);

			log.info("Message.getName()=" + message.getName() + "\r\n");

			int size = message.getChildren().size();

			if (message.getName().equals(MMConstants.SUBMITRSP))
			{
				MM7SubmitRes submitRes = new MM7SubmitRes();
				submitRes.setTransactionID(transactionID);
				for (int i = 0; i < size; i++)
				{
					Element element = (Element) message.getChildren().get(i);
					if (element.getName().equals(MMConstants.STATUS))
					{
						parseStatus(submitRes, element);

					}
					else if (element.getName().equals(MMConstants.MESSAGEID))
					{
						submitRes.setMessageID(element.getTextTrim());
					}
				}
				return submitRes;
			}
			else if (message.getName().equals(MMConstants.CANCELRSP))
			{
				MM7CancelRes cancelRes = new MM7CancelRes();
				cancelRes.setTransactionID(transactionID);
				for (int i = 0; i < size; i++)
				{
					Element element = (Element) message.getChildren().get(i);
					if (element.getName().equals(MMConstants.STATUS))
					{
						parseStatus(cancelRes, element);
					}
				}
				return cancelRes;
			}
			else if (message.getName().equals(MMConstants.REPLACERSP))
			{
				MM7ReplaceRes replaceRes = new MM7ReplaceRes();
				replaceRes.setTransactionID(transactionID);
				for (int i = 0; i < size; i++)
				{
					Element ele = (Element) message.getChildren().get(i);
					if (ele.getName().equals(MMConstants.STATUS))
					{
						parseStatus(replaceRes, ele);
					}
				}
				return replaceRes;
			}
			else if (message.getName().equals(MMConstants.RSERRORRSP))
			{
				log.debug("xmlBytes:" + new String(xmlBytes, "UTF-8"));
				MM7RSErrorRes res = new MM7RSErrorRes();
				res.setTransactionID(transactionID);
				for (int i = 0; i < size; i++)
				{
					Element ele = (Element) message.getChildren().get(i);
					if (ele.getName().equals(MMConstants.STATUS))
					{
						parseStatus(res, ele);
					}
				}
				return res;
			}
			else
			{
				MM7RSRes res = new MM7RSRes();
				res.setTransactionID(transactionID);
				for (int i = 0; i < size; i++)
				{
					Element ele = (Element) message.getChildren().get(i);
					if (ele.getName().equals(MMConstants.STATUS))
					{
						parseStatus(res, ele);
					}
				}
				return res;
			}
		}
		catch (JDOMException jdome)
		{
			MM7RSErrorRes error = new MM7RSErrorRes();
			log.error(null, jdome);
			error.setStatusCode(-109);
			error.setStatusText("XML解析错误！原因：" + jdome);
			return error;
		}
		catch (Exception e)
		{
			log.error(null, e);
			MM7RSErrorRes error = new MM7RSErrorRes();
			error.setStatusCode(-100);
			return error;
		}
	}

	protected static void parseStatus(MM7RSRes res, Element element)
	{
		for (int j = 0; j < element.getChildren().size(); j++)
		{
			Element sub = (Element) element.getChildren().get(j);
			if (sub.getName().equals(MMConstants.STATUSCODE))
			{
				res.setStatusCode(Integer.parseInt(sub.getTextTrim()));
			}
			else if (sub.getName().equals(MMConstants.STATUSTEXT))
			{
				res.setStatusText(sub.getTextTrim());
			}
			else if (sub.getName().equals(MMConstants.STATUSDETAIL))
			{
				res.setStatusDetail(sub.getTextTrim());
			}
		}
	}

}
