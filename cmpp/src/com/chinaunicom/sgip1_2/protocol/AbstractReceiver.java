package com.chinaunicom.sgip1_2.protocol;

import com.chinaunicom.sgip1_2.protocol.message.DeliverMessage;
import com.chinaunicom.sgip1_2.protocol.message.ReportMessage;

public interface AbstractReceiver
{

	public void doDeliver(DeliverMessage req);

	public void doReport(ReportMessage req);

}
