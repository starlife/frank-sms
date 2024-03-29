/**
 * File Name:MM7SubmitRes.java Company: 中国移动集团公司 Date : 2004-2-2
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

	public void setMessageID(String messageID) // 设置messageID
	{
		MessageID = messageID;
		MessageIDExist = true;
	}

	public String getMessageID() // 获得messageID
	{
		return (MessageID);
	}

	public boolean isMessageIDExist() // 是否存在messageID
	{
		return (MessageIDExist);
	}

	public String toString() // 返回对象的文本表示
	{
		StringBuffer sb=new StringBuffer();
		sb.append("[TransactionID="
				+ getTransactionID() + "]");
		sb.append("[Message_Type=MM7SubmitRes]");
		return sb.toString();
	}
}
