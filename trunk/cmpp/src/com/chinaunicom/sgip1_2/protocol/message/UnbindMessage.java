/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */

package com.chinaunicom.sgip1_2.protocol.message;

/**
 * @author Administrator
 */
public class UnbindMessage extends Message implements Send, Recv
{

	public UnbindMessage(String nodeid)
	{
		super(nodeid, CommandID.SGIP_UNBIND);
	}

	public UnbindMessage(BasePackage pack)
	{
		super(pack);
	}

}
