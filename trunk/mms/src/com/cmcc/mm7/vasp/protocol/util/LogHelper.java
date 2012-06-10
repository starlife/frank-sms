package com.cmcc.mm7.vasp.protocol.util;

import java.util.ArrayList;
import java.util.List;

import com.cmcc.mm7.vasp.protocol.message.MM7CancelReq;
import com.cmcc.mm7.vasp.protocol.message.MM7CancelRes;
import com.cmcc.mm7.vasp.protocol.message.MM7DeliverReq;
import com.cmcc.mm7.vasp.protocol.message.MM7DeliverRes;
import com.cmcc.mm7.vasp.protocol.message.MM7DeliveryReportReq;
import com.cmcc.mm7.vasp.protocol.message.MM7DeliveryReportRes;
import com.cmcc.mm7.vasp.protocol.message.MM7RSErrorRes;
import com.cmcc.mm7.vasp.protocol.message.MM7RSReq;
import com.cmcc.mm7.vasp.protocol.message.MM7RSRes;
import com.cmcc.mm7.vasp.protocol.message.MM7ReadReplyReq;
import com.cmcc.mm7.vasp.protocol.message.MM7ReadReplyRes;
import com.cmcc.mm7.vasp.protocol.message.MM7ReplaceReq;
import com.cmcc.mm7.vasp.protocol.message.MM7ReplaceRes;
import com.cmcc.mm7.vasp.protocol.message.MM7SubmitReq;
import com.cmcc.mm7.vasp.protocol.message.MM7SubmitRes;
import com.cmcc.mm7.vasp.protocol.message.MM7VASPErrorRes;
import com.cmcc.mm7.vasp.protocol.message.MM7VASPReq;
import com.cmcc.mm7.vasp.protocol.message.MM7VASPRes;

public class LogHelper
{

	static String logSubmitReq(MM7SubmitReq submitReq)
	{
		StringBuffer sb = new StringBuffer();
		sb.append("[Message_Type=MM7SubmitReq]");
		sb.append("[TransactionID=" + submitReq.getTransactionID() + "]");
		sb.append("[Recipient_Address={");
		if (submitReq.isToExist())
		{
			sb.append("To={");
			List<String> to = new ArrayList<String>();
			to = submitReq.getTo();
			for (int i = 0; i < to.size(); i++)
			{
				sb.append((String) to.get(i) + ",");
			}
			sb.append("}");
		}
		if (submitReq.isCcExist())
		{
			sb.append("Cc={");
			List<String> cc = new ArrayList<String>();
			cc = submitReq.getCc();
			for (int i = 0; i < cc.size(); i++)
			{
				sb.append((String) cc.get(i) + ",");
			}
			sb.append("}");
		}
		if (submitReq.isBccExist())
		{
			sb.append("Bcc={");
			List<String> bcc = new ArrayList<String>();
			bcc = submitReq.getBcc();
			for (int i = 0; i < bcc.size(); i++)
			{
				sb.append((String) bcc.get(i) + ",");
			}
			sb.append("}");
		}
		sb.append("}]\r\n");
		return sb.toString();
	}

	static String logDeliverReq(MM7DeliverReq deliverReq)
	{
		StringBuffer sb = new StringBuffer();
		sb.append("[Message_Type=MM7DeliverReq]");
		sb.append("[TransactionID=" + deliverReq.getTransactionID() + "]");
		sb.append("[Sender_Address=" + deliverReq.getSender() + "]");
		sb.append("[Recipient_Address={");
		if (deliverReq.isToExist())
		{
			sb.append("To={");
			List<String> to = new ArrayList<String>();
			to = deliverReq.getTo();
			for (int i = 0; i < to.size(); i++)
				sb.append((String) to.get(i) + ",");
			sb.append("}");
		}
		if (deliverReq.isCcExist())
		{
			sb.append("Cc={");
			List<String> cc = new ArrayList<String>();
			cc = deliverReq.getCc();
			for (int i = 0; i < cc.size(); i++)
				sb.append((String) cc.get(i) + ",");
			sb.append("}");
		}
		if (deliverReq.isBccExist())
		{
			sb.append("Bcc={");
			List<String> bcc = new ArrayList<String>();
			bcc = deliverReq.getBcc();
			for (int i = 0; i < bcc.size(); i++)
				sb.append((String) bcc.get(i) + ",");
			sb.append("}");
		}
		sb.append("}]");
		return sb.toString();
	}

	static String logDeliverReportReq(MM7DeliveryReportReq deliverReportReq)
	{
		StringBuffer sb = new StringBuffer();
		sb.append("[Message_Type=MM7DeliveryReportReq]");
		sb.append("[MessageID=").append(deliverReportReq.getMessageID()+"]");
		sb.append("[Recipient_Address=").append(deliverReportReq.getRecipient()+"]");
		sb.append("[Comments={" + deliverReportReq.getMMStatus() + ";"
				+ deliverReportReq.getStatusText() + "}]\r\n");
		return sb.toString();
	}

	static String logReadReplyReq(MM7ReadReplyReq readReplyReq)
	{
		StringBuffer sb = new StringBuffer();
		sb.append("[Message_Type=MM7ReadReplyReq]");
		sb.append("[TransactionID=" + readReplyReq.getTransactionID() + "]");
		sb.append("[Sender_Address=" + readReplyReq.getSender() + "]");
		return sb.toString();
	}

	static String logReplaceReq(MM7ReplaceReq replaceReq)
	{
		StringBuffer sb = new StringBuffer();
		sb.append("[Message_Type=MM7ReplaceReq]\r\n");
		sb.append("[TransactionID=" + replaceReq.getTransactionID() + "]");
		return sb.toString();
	}

	static String logCancelReq(MM7CancelReq cancelReq)
	{
		StringBuffer sb = new StringBuffer();
		sb.append("[TransactionID=" + cancelReq.getTransactionID() + "]");
		sb.append("[Message_Type=MM7CancelReq]\r\n");
		return sb.toString();
	}

	static String logSubmitRes(MM7SubmitRes submitRes)
	{
		StringBuffer sb = new StringBuffer();
		sb.append("[Message_Type=MM7SubmitRes]");
		sb.append("[TransactionID=" + submitRes.getTransactionID() + "]");
		sb.append("[MessageID=" + submitRes.getMessageID() + "]");
		sb.append("[Comments={" + submitRes.getStatusCode() + ";"
				+ submitRes.getStatusText() + "}]\r\n");
		return sb.toString();
	}

	static String logDeliverRes(MM7DeliverRes deliverRes)
	{
		StringBuffer sb = new StringBuffer();
		sb.append("[Message_Type=MM7DeliverRes]");
		sb.append("[Comments={" + deliverRes.getStatusCode() + ";"
				+ deliverRes.getStatusText() + "}]\r\n");
		// sb.append(logMM7RSRes(deliverRes));
		return sb.toString();
	}

	static String logDeliverReportRes(MM7DeliveryReportRes deliverReportRes)
	{
		StringBuffer sb = new StringBuffer();
		sb.append("[Message_Type=MM7DeliveryReportRes]");
		sb.append("[Comments={" + deliverReportRes.getStatusCode() + ";"
				+ deliverReportRes.getStatusText() + "}]\r\n");
		return sb.toString();

	}

	static String logReadReplyRes(MM7ReadReplyRes readReplyRes)
	{
		StringBuffer sb = new StringBuffer();
		sb.append("[Message_Type=MM7ReadReplyRes]");
		sb.append("[Comments={" + readReplyRes.getStatusCode() + ";"
				+ readReplyRes.getStatusText() + "}]\r\n");
		return sb.toString();
	}

	static String logVASPErrorRes(MM7VASPErrorRes errorRes)
	{
		StringBuffer sb = new StringBuffer();
		sb.append("[Message_Type=MM7VASPErrorRes]");
		sb.append("[TransactionID=" + errorRes.getTransactionID() + "]");
		sb.append("[Comments={" + errorRes.getStatusCode() + ";"
				+ errorRes.getStatusText() + "}]\r\n");
		return sb.toString();
	}

	static String logReplaceRes(MM7ReplaceRes replaceRes)
	{
		StringBuffer sb = new StringBuffer();
		sb.append("[Message_Type=MM7ReplaceRes]");
		sb.append("[TransactionID=" + replaceRes.getTransactionID() + "]");
		sb.append("[Comments={" + replaceRes.getStatusCode() + ";"
				+ replaceRes.getStatusText() + "}]\r\n");
		return sb.toString();
	}

	static String logCancelRes(MM7CancelRes cancelRes)
	{
		StringBuffer sb = new StringBuffer();
		sb.append("[Message_Type=MM7CancelRes]");
		sb.append("[TransactionID=" + cancelRes.getTransactionID() + "]");
		sb.append("[Comments={" + cancelRes.getStatusCode() + ";"
				+ cancelRes.getStatusText() + "}]\r\n");
		return sb.toString();
	}

	static String logRSErrorRes(MM7RSErrorRes errorRes)
	{
		StringBuffer sb = new StringBuffer();
		sb.append("[Message_Type=MM7RSErrorRes]");
		sb.append("[TransactionID=" + errorRes.getTransactionID() + "]");
		sb.append("[Comments={" + errorRes.getStatusCode() + ";"
				+ errorRes.getStatusText() + "}]\r\n");
		return sb.toString();
	}

	public static String logMM7RSRes(MM7RSRes res)
	{
		if (res instanceof MM7SubmitRes)
		{
			return LogHelper.logSubmitRes((MM7SubmitRes) res);
		}
		else if (res instanceof MM7CancelRes)
		{
			return LogHelper.logCancelRes((MM7CancelRes) res);
		}
		else if (res instanceof MM7ReplaceRes)
		{
			return LogHelper.logReplaceRes((MM7ReplaceRes) res);
		}
		else if (res instanceof MM7RSErrorRes)
		{
			return LogHelper.logRSErrorRes((MM7RSErrorRes) res);
		}
		else
		{
			StringBuffer sb = new StringBuffer();
			sb.append("[Comments={" + res.getStatusCode() + ";"
					+ res.getStatusText() + "}]\r\n");
			return sb.toString();
		}

	}

	public static String logMM7VaspRes(MM7VASPRes res)
	{
		if (res instanceof MM7DeliverRes)
		{
			return LogHelper.logDeliverRes((MM7DeliverRes) res);
		}
		else if (res instanceof MM7DeliveryReportRes)
		{
			return LogHelper.logDeliverReportRes((MM7DeliveryReportRes) res);
		}
		else if (res instanceof MM7ReadReplyRes)
		{
			return LogHelper.logReadReplyRes((MM7ReadReplyRes) res);
		}
		else if (res instanceof MM7VASPErrorRes)
		{
			return LogHelper.logVASPErrorRes((MM7VASPErrorRes) res);
		}
		else
		{
			StringBuffer sb = new StringBuffer();
			sb.append("[Comments={" + res.getStatusCode() + ";"
					+ res.getStatusText() + "}]\r\n");
			return sb.toString();
		}

	}

	public static String logMM7VASPReq(MM7VASPReq req)
	{
		if (req instanceof MM7SubmitReq)
		{
			return LogHelper.logSubmitReq((MM7SubmitReq) req);
		}
		else if (req instanceof MM7CancelReq)
		{
			return LogHelper.logCancelReq((MM7CancelReq) req);
		}
		else if (req instanceof MM7ReplaceReq)
		{
			return LogHelper.logReplaceReq((MM7ReplaceReq) req);
		}
		else
		{
			StringBuffer sb = new StringBuffer();
			sb.append("[TransactionID=" + req.getTransactionID() + "]");
			sb.append("[Message_Type=MM7VASPReq]");
			return sb.toString();
		}

	}

	public static String logMM7RSReq(MM7RSReq req)
	{
		if (req instanceof MM7DeliverReq)
		{
			return LogHelper.logDeliverReq((MM7DeliverReq) req);
		}
		else if (req instanceof MM7DeliveryReportReq)
		{
			return LogHelper.logDeliverReportReq((MM7DeliveryReportReq) req);
		}
		else if (req instanceof MM7ReadReplyReq)
		{
			return LogHelper.logReadReplyReq((MM7ReadReplyReq) req);
		}
		else
		{
			StringBuffer sb = new StringBuffer();
			sb.append("[TransactionID=" + req.getTransactionID() + "]");
			sb.append("[Message_Type=MM7RSReq]");
			return sb.toString();
		}

	}

}
