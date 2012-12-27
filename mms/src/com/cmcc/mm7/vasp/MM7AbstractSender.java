/**
 * File Name:MM7AbstractReceiver.java Company: 中国移动集团公司 Date : 2004-2-17
 */

package com.cmcc.mm7.vasp;

import com.cmcc.mm7.vasp.protocol.message.MM7CancelReq;
import com.cmcc.mm7.vasp.protocol.message.MM7RSRes;
import com.cmcc.mm7.vasp.protocol.message.MM7ReplaceReq;
import com.cmcc.mm7.vasp.protocol.message.MM7SubmitReq;

public interface MM7AbstractSender
{

	/**
	 * 定义如何提交彩信
	 * 
	 * @return
	 */
	public MM7SubmitReq submit();

	/**
	 * 对提交消息的处理
	 * 
	 * @param req
	 * @param res
	 */
	public void doSubmit(MM7SubmitReq req, MM7RSRes res);

	/**
	 * 对替换消息的处理
	 * 
	 * @param req
	 * @param res
	 */
	public void doReplace(MM7ReplaceReq req, MM7RSRes res);

	/**
	 * 对取消消息的处理
	 * 
	 * @param req
	 * @param res
	 */
	public void doCancel(MM7CancelReq req, MM7RSRes res);

}
