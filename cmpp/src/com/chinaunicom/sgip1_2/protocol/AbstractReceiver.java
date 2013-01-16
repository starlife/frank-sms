package com.chinaunicom.sgip1_2.protocol;

import com.chinaunicom.sgip1_2.protocol.message.DeliverMessage;
import com.chinaunicom.sgip1_2.protocol.message.DeliverRespMessage;
import com.chinaunicom.sgip1_2.protocol.message.ReportMessage;
import com.chinaunicom.sgip1_2.protocol.message.ReportRespMessage;

public interface AbstractReceiver
{
	
	public DeliverRespMessage doDeliver(DeliverMessage req);
	
	public ReportRespMessage doReport(ReportMessage req);
	
	
}
