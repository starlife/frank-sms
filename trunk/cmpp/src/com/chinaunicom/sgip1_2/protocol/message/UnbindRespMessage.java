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
		super(Header.LENGTH,CommandID.SGIP_UNBIND_RESP,new Sequence(nodeid));
	}

	public UnbindRespMessage(BasePackage pack)
	{
		super(pack);
	}

}
