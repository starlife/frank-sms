package com.chinamobile.cmpp3_0.protocol.message;

import com.chinamobile.cmpp3_0.protocol.message.bean.Submit;

public class MessageUtil
{
	public static final int SMS_LENGTH = 140;

	/**
	 * 创建SubmitMessage消息数组，如果消息长度小于140个字节，那么数组长度为1
	 * 
	 * @param spid
	 * @param spnumber
	 * @param serviceCode
	 * @param desttermid
	 * @param msgcontent
	 * @param param
	 * @return
	 */
	public static SubmitMessage[] createMultiSubmitMessage(String spid,
			String spnumber, String serviceCode, String[] desttermid,
			byte[] msgcontent, String param)
	{
		SubmitMessage[] smList = null;
		if (msgcontent.length > SMS_LENGTH)// 拆分为多条
		{
			int total = (msgcontent.length - 1) / SMS_LENGTH + 1;//得到消息总数
			smList = new SubmitMessage[total];
			for (int i = 1; i <= total; i++)
			{
				int pos = (i - 1) * SMS_LENGTH;
				int length = Math.min(msgcontent.length - pos, SMS_LENGTH);
				byte[] dest = new byte[length];
				System.arraycopy(msgcontent, pos, dest, 0, dest.length);
				
				SubmitMessage sm = createSubmitMessage(total, i, spid,
						spnumber, serviceCode, desttermid, dest, param);
				
				smList[i - 1] = sm;
			}
		}
		else
		{
			smList = new SubmitMessage[1];
			SubmitMessage sm = createSubmitMessage(1, 1, spid, spnumber,
					serviceCode, desttermid, msgcontent, param);
			smList[0] = sm;

		}
		return smList;
	}

	
	public static SubmitMessage createSubmitMessage(String spid, String spnumber, String serviceCode,
			String[] desttermid, byte[] msgcontent, String param)
	{
		
		Submit submit = new Submit(spid, spnumber, serviceCode, desttermid,
				msgcontent, param);
		SubmitMessage sm = new SubmitMessage(submit);
		return sm;
	}
	
	
	public static SubmitMessage createSubmitMessage(int pkTotal, int pkNumber,
			String spid, String spnumber, String serviceCode,
			String[] desttermid, byte[] msgcontent, String param)
	{
		param += "pkTotal" + pkTotal + "\r\n";
		param += "pkNumber" + pkNumber + "\r\n";
		Submit submit = new Submit(spid, spnumber, serviceCode, desttermid,
				msgcontent, param);
		SubmitMessage sm = new SubmitMessage(submit);
		return sm;
	}

}
