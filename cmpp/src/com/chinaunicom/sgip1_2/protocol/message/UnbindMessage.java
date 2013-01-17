/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */

package com.chinaunicom.sgip1_2.protocol.message;

/**
 * @author Administrator
 */
public class UnbindMessage extends SGIPMessage implements Send, Recv
{

	public UnbindMessage(String nodeid)
	{
		super(Header.LENGTH,CommandID.SGIP_UNBIND,new Sequence(nodeid));
	}

	public UnbindMessage(BasePackage pack)
	{
		super(pack);
	}

}
