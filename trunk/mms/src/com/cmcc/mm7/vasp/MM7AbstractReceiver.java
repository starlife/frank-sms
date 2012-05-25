/**
 * File Name:MM7AbstractReceiver.java Company: 中国移动集团公司 Date : 2004-2-17
 */

package com.cmcc.mm7.vasp;

import com.cmcc.mm7.vasp.protocol.message.MM7DeliverReq;
import com.cmcc.mm7.vasp.protocol.message.MM7DeliveryReportReq;
import com.cmcc.mm7.vasp.protocol.message.MM7ReadReplyReq;
import com.cmcc.mm7.vasp.protocol.message.MM7VASPRes;

public interface MM7AbstractReceiver
{
	// 构造方法
	// public MM7AbstractReceiver() throws Exception;

	// 抽象方法。处理到VASP的传送（deliver）多媒体消息。
	public MM7VASPRes doDeliver(MM7DeliverReq mm7DeliverReq);

	// 抽象方法。处理到VASP的发送报告
	public MM7VASPRes doDeliveryReport(MM7DeliveryReportReq mm7DeliveryReportReq);

	// 抽象方法。处理到VASP的读后回复报告
	public MM7VASPRes doReadReply(MM7ReadReplyReq mm7ReadReplyReq);

}
