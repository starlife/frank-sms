/**
 * File Name:MM7AbstractReceiver.java Company: �й��ƶ����Ź�˾ Date : 2004-2-17
 */

package com.cmcc.mm7.vasp;

import com.cmcc.mm7.vasp.protocol.message.MM7CancelReq;
import com.cmcc.mm7.vasp.protocol.message.MM7RSRes;
import com.cmcc.mm7.vasp.protocol.message.MM7ReplaceReq;
import com.cmcc.mm7.vasp.protocol.message.MM7SubmitReq;

public interface MM7AbstractSender
{

	/**
	 * ��������ύ����
	 * 
	 * @return
	 */
	public MM7SubmitReq submit();

	/**
	 * ���ύ��Ϣ�Ĵ���
	 * 
	 * @param req
	 * @param res
	 */
	public void doSubmit(MM7SubmitReq req, MM7RSRes res);

	/**
	 * ���滻��Ϣ�Ĵ���
	 * 
	 * @param req
	 * @param res
	 */
	public void doReplace(MM7ReplaceReq req, MM7RSRes res);

	/**
	 * ��ȡ����Ϣ�Ĵ���
	 * 
	 * @param req
	 * @param res
	 */
	public void doCancel(MM7CancelReq req, MM7RSRes res);

}
