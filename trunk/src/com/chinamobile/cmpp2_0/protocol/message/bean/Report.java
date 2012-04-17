package com.chinamobile.cmpp2_0.protocol.message.bean;

import com.chinamobile.cmpp2_0.protocol.util.Hex;
import com.chinamobile.cmpp2_0.protocol.util.TypeConvert;

/**
 * @author meisq
 */
public class Report
{

	public Report(byte[] content)
	{
		// Msg_Id 8 Unsigned Integer 信息标识
		// SP提交短信（CMPP_SUBMIT）操作时，与SP相连的ISMG产生的Msg_Id。
		byte[] msgid = new byte[8];
		System.arraycopy(content, 0, msgid, 0, 8);

		this.Msg_Id = Hex.rhex(msgid);
		// Stat 7 Octet String
		// 发送短信的应答结果，含义与SMPP协议要求中stat字段定义相同，详见表一。SP根据该字段确定CMPP_SUBMIT消息的处理状态。
		this.Stat = new String(content, 8, 7);
		// Submit_time 10 Octet String
		// YYMMDDHHMM（YY为年的后两位00-99，MM：01-12，DD：01-31，HH：00-23，MM：00-59）
		this.Submit_time = new String(content, 15, 10);
		// Done_time 10 Octet String YYMMDDHHMM
		this.Done_time = new String(content, 25, 10);
		// Dest_terminal_Id 32 Octet String 目的终端MSISDN号码(SP发送CMPP_SUBMIT消息的目标终端)
		this.Dest_terminal_Id = new String(content, 35, 21);
		// SMSC_sequence 4 Unsigned Integer 取自SMSC发送状态报告的消息体中的消息标识。
		this.SMSC_sequence = TypeConvert.byte2int(content, 56);
	}

	private String getSataString(String stat)
	{
		String ret;
		if ((stat == null) || (stat.equals("")))
		{
			ret = "Stat is null";
		}
		if (stat.equals("DELIVRD"))
		{
			ret = "Message is delivered to destination";
		}
		else if (stat.equals("EXPIRED"))
		{
			ret = "Message validity period has expired";
		}
		else if (stat.equals("DELETED"))
		{
			ret = "Message has been deleted";
		}
		else if (stat.equals("UNDELIV"))
		{
			ret = "Message is undeliverable";
		}
		else if (stat.equals("ACCEPTD"))
		{
			ret = "Message is in accepted state";
		}
		else if (stat.equals("UNKNOWN"))
		{
			ret = "Message is in invalid state";
		}
		else if (stat.equals("REJECTD"))
		{
			ret = "Message is in a rejected state";
		}
		else
		{
			ret = stat;
		}

		return ret;

	}

	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("\t\r\n***********************************************\r\n");
		sb.append("DELIVER Report Msg_Id: " + Msg_Id + "\r\n Stat: "
				+ getSataString(Stat));
		sb.append("\r\n");
		sb.append(" Submit_time: " + Submit_time + " Done_time: " + Done_time
				+ "\r\n Dest_terminal_Id: " + Dest_terminal_Id
				+ " SMSC_sequence: " + SMSC_sequence);
		sb.append("\t\r\n***********************************************\r\n");
		return sb.toString();
	}

	private String Msg_Id;
	private String Stat;
	private String Submit_time;
	private String Done_time;
	private String Dest_terminal_Id;
	private int SMSC_sequence;

}
