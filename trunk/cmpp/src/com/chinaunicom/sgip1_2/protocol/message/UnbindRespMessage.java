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

	/**
	 * 回应包
	 * 
	 * @param um
	 */
	public UnbindRespMessage(UnbindMessage um)
	{
		super(Header.LENGTH, CommandID.SGIP_UNBIND_RESP, um.getHead()
			.getSequenceId());
	}

	/**
	 * 接收
	 * 
	 * @param pack
	 */
	public UnbindRespMessage(BasePackage pack)
	{
		super(pack);
	}

}
