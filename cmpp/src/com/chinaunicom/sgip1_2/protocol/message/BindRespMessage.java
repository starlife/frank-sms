/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */

package com.chinaunicom.sgip1_2.protocol.message;

/**
 * @author Administrator
 */
public class BindRespMessage extends CommonRespMessage implements Send, Recv
{

	public BindRespMessage(String nodeid, int result)
	{
		super(nodeid, CommandID.SGIP_BIND_RESP, result, "");
	}

	public BindRespMessage(BasePackage pack)
	{
		super(pack);
	}

}
