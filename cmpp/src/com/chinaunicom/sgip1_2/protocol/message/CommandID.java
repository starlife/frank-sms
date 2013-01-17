/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */

package com.chinaunicom.sgip1_2.protocol.message;

/**
 * @author Administrator
 */
public class CommandID
{

	public static final int SGIP_BIND = 0x00000001;
	public static final int SGIP_BIND_RESP = 0x80000001;
	public static final int SGIP_UNBIND = 0x00000002;
	public static final int SGIP_UNBIND_RESP = 0x80000002;
	public static final int SGIP_SUBMIT = 0x00000003;
	public static final int SGIP_SUBMIT_RESP = 0x80000003;
	public static final int SGIP_DELIVER = 0x00000004;
	public static final int SGIP_DELIVER_RESP = 0x80000004;
	public static final int SGIP_REPORT = 0x00000005;
	public static final int SGIP_REPORT_RESP = 0x80000005;

	public static String getCommandString(int commandid)
	{
		switch (commandid)
		{
			case SGIP_BIND:
				return "SGIP_BIND";
			case SGIP_BIND_RESP:
				return "SGIP_BIND_RESP";
			case SGIP_UNBIND:
				return "SGIP_UNBIND";
			case SGIP_UNBIND_RESP:
				return "SGIP_UNBIND_RESP";
			case SGIP_SUBMIT:
				return "SGIP_SUBMIT";
			case SGIP_SUBMIT_RESP:
				return "SGIP_SUBMIT_RESP";
			case SGIP_DELIVER:
				return "SGIP_DELIVER";
			case SGIP_DELIVER_RESP:
				return "SGIP_DELIVER_RESP";
			case SGIP_REPORT:
				return "SGIP_REPORT";
			case SGIP_REPORT_RESP:
				return "SGIP_REPORT_RESP";
			default:
				return "" + commandid;

		}
	}

}
