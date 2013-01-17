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

	/**
	 * 发起
	 * 
	 * @param nodeid
	 */
	public UnbindMessage(String nodeid)
	{
		super(Header.LENGTH, CommandID.SGIP_UNBIND, new Sequence(nodeid));
	}

	/**
	 * 接收
	 * 
	 * @param pack
	 */
	public UnbindMessage(BasePackage pack)
	{
		super(pack);
	}

}
