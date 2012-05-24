/**
 * File Name:MM7AbstractReceiver.java Company: �й��ƶ����Ź�˾ Date : 2004-2-17
 */

package com.cmcc.mm7.vasp;

import com.cmcc.mm7.vasp.protocol.message.*;

import java.lang.Exception;

public interface MM7AbstractReceiver
{
	// ���췽��
	// public MM7AbstractReceiver() throws Exception;

	// ���󷽷�������VASP�Ĵ��ͣ�deliver����ý����Ϣ��
	public MM7VASPRes doDeliver(MM7DeliverReq mm7DeliverReq) throws Exception;

	// ���󷽷�������VASP�ķ��ͱ���
	public MM7VASPRes doDeliveryReport(MM7DeliveryReportReq mm7DeliveryReportReq)
			throws Exception;

	// ���󷽷�������VASP�Ķ���ظ�����
	public MM7VASPRes doReadReply(MM7ReadReplyReq mm7ReadReplyReq)
			throws Exception;

}
