/**
 * File Name:MM7SubmitRes.java Company: �й��ƶ����Ź�˾ Date : 2004-2-2
 */

package com.cmcc.mm7.vasp.protocol.message;


public class MM7SubmitRes extends MM7RSRes
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String MessageID;
	private boolean MessageIDExist;

	public MM7SubmitRes()
	{
	}

	public void setMessageID(String messageID) // ����messageID
	{
		MessageID = messageID;
		MessageIDExist = true;
	}

	public String getMessageID() // ���messageID
	{
		return (MessageID);
	}

	public boolean isMessageIDExist() // �Ƿ����messageID
	{
		return (MessageIDExist);
	}

	public String toString() // ���ض�����ı���ʾ
	{
		StringBuffer sb=new StringBuffer();
		sb.append("[TransactionID="
				+ getTransactionID() + "]");
		sb.append("[Message_Type=MM7SubmitRes]");
		return sb.toString();
	}
}
