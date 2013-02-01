package com.chinaunicom.sgip1_2.protocol;

import com.chinaunicom.sgip1_2.protocol.message.APackage;
import com.chinaunicom.sgip1_2.protocol.message.SubmitMessage;
import com.chinaunicom.sgip1_2.protocol.message.SubmitRespMessage;

public interface AbstractSender
{
	public void doSubmitResp(SubmitMessage sm, SubmitRespMessage srm);

	/**
	 * 该方法提供给运用程序继承
	 * 
	 * @return
	 */
	public APackage doSubmit();

}
