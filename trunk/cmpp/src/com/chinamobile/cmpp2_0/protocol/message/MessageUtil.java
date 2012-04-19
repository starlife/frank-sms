package com.chinamobile.cmpp2_0.protocol.message;

import java.nio.charset.Charset;

import com.chinamobile.cmpp2_0.protocol.message.bean.Submit;
import com.chinamobile.cmpp2_0.protocol.util.SmsUtil;

public class MessageUtil
{

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
		if (message.getBytes().length > SmsUtil.SMS_LENGTH)
		{
			// 需要分多条发送,发长短信需要设置如下：1.tp_udhi=1 2.msg_fmt =8 3.内容用 ucs2编码
			byte[] msgByte = message.getBytes(Charset.forName("UTF-16BE"));// ucs2编码
			byte[][] msgArray = SmsUtil.getLongSmsByte(msgByte);
			smList = new SubmitMessage[msgArray.length];
			for (int i = 0; i < msgArray.length; i++)
			{
				String theParam = param + "TP_udhi=1\r\n" + "Msg_Fmt =8\r\n";
				SubmitMessage sm = createSubmitMessage(msgArray.length, i + 1,
						spid, spnumber, serviceCode, desttermid, msgArray[i],
						theParam);

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
		param += "Pk_total=" + pkTotal + "\r\n";
		param += "Pk_number=" + pkNumber + "\r\n";
		Submit submit = new Submit(spid, spnumber, serviceCode, desttermid,
				msgcontent, param);
		SubmitMessage sm = new SubmitMessage(submit);
		return sm;
	}

}
