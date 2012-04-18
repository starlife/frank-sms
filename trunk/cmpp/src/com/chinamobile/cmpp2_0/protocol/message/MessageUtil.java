package com.chinamobile.cmpp2_0.protocol.message;

import com.chinamobile.cmpp2_0.protocol.message.bean.Submit;

public class MessageUtil
{
	public static final int SMS_LENGTH = 140;

	/**
	 * 发送短信，如果大于140，会自动拆分为多条
	 * 
	 * @param spid
	 * @param spnumber
	 * @param serviceCode
	 * @param desttermid
	 * @param message
	 * @param param
	 * @return
	 */
	public static SubmitMessage[] createSubmitMessage(String spid,
			String spnumber, String serviceCode, String[] desttermid,
			String message, String param)
	{
		SubmitMessage[] smList = null;
		if (message.getBytes().length > SMS_LENGTH)
		{
			// 需要分多条发送
			byte[] msgByte = message.getBytes();// 拆分是GBK吗
			int count = (msgByte.length - 1) / SMS_LENGTH + 1;// 得到消息总数
			smList = new SubmitMessage[count];
			for (int i = 0; i < count; i++)
			{
				int offset = i * SMS_LENGTH;
				int length = Math.min(msgByte.length - offset, SMS_LENGTH);// 本条短信长度
				byte[] dest = new byte[length];
				System.arraycopy(msgByte, offset, dest, 0, dest.length);
				SubmitMessage sm = createSubmitMessage(count, i + 1, spid,
						spnumber, serviceCode, desttermid, dest, param);

				smList[i] = sm;
			}

		}
		else
		{
			byte[] msgByte = message.getBytes();
			smList = new SubmitMessage[1];
			SubmitMessage sm = createSubmitMessage(1, 1, spid, spnumber,
					serviceCode, desttermid, msgByte, param);
			smList[0] = sm;

		}
		return smList;
	}

	public static SubmitMessage createSubmitMessage(int pkTotal, int pkNumber,
			String spid, String spnumber, String serviceCode,
			String[] desttermid, byte[] msgcontent, String param)
	{
		param += "Pk_total" + pkTotal + "\r\n";
		param += "Pk_number" + pkNumber + "\r\n";
		Submit submit = new Submit(spid, spnumber, serviceCode, desttermid,
				msgcontent, param);
		SubmitMessage sm = new SubmitMessage(submit);
		return sm;
	}

}
