/**
 * File Name:MM7AbstractReceiver.java Company: �й��ƶ����Ź�˾ Date : 2004-2-17
 */

package com.cmcc.mm7.vasp;

import com.cmcc.mm7.vasp.protocol.message.MM7DeliverReq;
import com.cmcc.mm7.vasp.protocol.message.MM7DeliveryReportReq;
import com.cmcc.mm7.vasp.protocol.message.MM7ReadReplyReq;
import com.cmcc.mm7.vasp.protocol.message.MM7VASPRes;

public interface MM7AbstractReceiver
{
	// ���췽��
	// public MM7AbstractReceiver() throws Exception;

	// ���󷽷�������VASP�Ĵ��ͣ�deliver����ý����Ϣ��
	public MM7VASPRes doDeliver(MM7DeliverReq mm7DeliverReq);

	// ���󷽷�������VASP�ķ��ͱ���
	public MM7VASPRes doDeliveryReport(MM7DeliveryReportReq mm7DeliveryReportReq);

	// ���󷽷�������VASP�Ķ���ظ�����
	public MM7VASPRes doReadReply(MM7ReadReplyReq mm7ReadReplyReq);

}
