/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */

package com.chinaunicom.sgip1_2.protocol.message;

/**
 * @author Administrator
 */
public class UnbindRespMessage extends SGIPMessage implements Send, Recv
{

	public UnbindRespMessage(String nodeid)
	{
		super(nodeid, CommandID.SGIP_UNBIND_RESP);
	}

	public UnbindRespMessage(BasePackage pack)
	{
		super(pack);
	}

}
