package com.cmcc.mm7.vasp.service;

import java.util.ArrayList;
import java.util.List;

import com.cmcc.mm7.vasp.message.MM7CancelReq;
import com.cmcc.mm7.vasp.message.MM7CancelRes;
import com.cmcc.mm7.vasp.message.MM7RSErrorRes;
import com.cmcc.mm7.vasp.message.MM7RSRes;
import com.cmcc.mm7.vasp.message.MM7ReplaceReq;
import com.cmcc.mm7.vasp.message.MM7ReplaceRes;
import com.cmcc.mm7.vasp.message.MM7SubmitReq;
import com.cmcc.mm7.vasp.message.MM7SubmitRes;

public class LogHelper
{
	public static String logSubmitReq(MM7SubmitReq submitReq)
	{
		StringBuffer sb=new StringBuffer();
		sb.append("[TransactionID="
				+ submitReq.getTransactionID() + "]");
		sb.append("[Message_Type=MM7SubmitReq]");
		sb.append("[Sender_Address="
				+ submitReq.getSenderAddress() + "]");
		sb.append("[Recipient_Address={");
		if (submitReq.isToExist())
		{
			sb.append("To={");
			List to = new ArrayList();
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
			List cc = new ArrayList();
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
			List bcc = new ArrayList();
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
		
	
	public static String logReplaceReq(MM7ReplaceReq replaceReq)
	{
		StringBuffer sb=new StringBuffer();
		sb.append("[TransactionID="
				+ replaceReq.getTransactionID() + "]");
		sb.append("[Message_Type=MM7ReplaceReq]\r\n");
		return sb.toString();
	}
	
	public static String logCancelReq(MM7CancelReq cancelReq)
	{
		StringBuffer sb=new StringBuffer();
		sb.append("[TransactionID="
				+ cancelReq.getTransactionID() + "]");
		sb.append("[Message_Type=MM7CancelReq]\r\n");
		return sb.toString();
	}
	
	public static String logSubmitRes(MM7SubmitRes submitRes)
	{
		StringBuffer sb=new StringBuffer();
		sb.append("[TransactionID="
				+ submitRes.getTransactionID() + "]");
		sb.append("[Message_Type=MM7SubmitRes]");
		sb.append(logMM7RSRes(submitRes));
		return sb.toString();
	}
	
	public static String logReplaceRes(MM7ReplaceRes replaceRes)
	{
		StringBuffer sb=new StringBuffer();
		sb.append("[TransactionID="
				+ replaceRes.getTransactionID() + "]");
		sb.append("[Message_Type=MM7ReplaceRes]");
		sb.append(logMM7RSRes(replaceRes));
		return sb.toString();
	}
	
	public static String logCancelRes(MM7CancelRes cancelRes)
	{
		StringBuffer sb=new StringBuffer();
		sb.append("[TransactionID="
				+ cancelRes.getTransactionID() + "]");
		sb.append("[Message_Type=MM7CancelRes]");
		sb.append(logMM7RSRes(cancelRes));
		return sb.toString();
	}
	
	public static String logRSErrorRes(MM7RSErrorRes errorRes)
	{
		StringBuffer sb=new StringBuffer();
		sb.append("[TransactionID="
				+ errorRes.getTransactionID() + "]");
		sb.append("[Message_Type=MM7RSErrorRes]");
		sb.append(logMM7RSRes(errorRes));
		return sb.toString();
	}
	
	public static String logMM7RSRes(MM7RSRes res)
	{
		StringBuffer sb=new StringBuffer();
		sb.append("[Comments={" + res.getStatusCode() + ";"
				+ res.getStatusText() + "}]\r\n");
		return sb.toString();
	}
	
	
	
	

	
}
