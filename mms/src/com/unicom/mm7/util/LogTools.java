package com.unicom.mm7.util;

import com.cmcc.mm7.vasp.protocol.message.MM7DeliveryReportReq;

public class LogTools
{
	public static String logMM7DeliveryReportReq(MM7DeliveryReportReq req)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("[").append(req.getMessageID());
		sb.append(",").append(req.getRecipient()).append("]");
		sb.append("[״̬=").append(req.getStatusText()).append("]");
		return sb.toString();
	}
}
