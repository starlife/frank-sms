/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */

package com.chinaunicom.sgip1_2.protocol.message;

/**
 * @author Administrator
 */
public class ReportRespMessage extends CommonRespMessage implements Send
{

	public ReportRespMessage(String nodeid)
	{
		super(nodeid, CommandID.SGIP_REPORT_RESP, 0, "");

	}

}
