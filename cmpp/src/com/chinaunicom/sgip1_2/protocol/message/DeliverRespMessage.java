/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */

package com.chinaunicom.sgip1_2.protocol.message;

/**
 * @author Administrator
 */
public class DeliverRespMessage extends CommonRespMessage implements Send
{

	public DeliverRespMessage(DeliverMessage dm)
	{
		super(CommandID.SGIP_DELIVER_RESP, dm.getHead().getSequenceId(), 0, "");

	}

}
