/**
 * SmsEngineSoapBindingImpl.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.tourzj.sms;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.tourzj.common.constant.Constants;
import com.tourzj.sms.manager.SmsManager;
import com.tourzj.sms.req.DeliverReq;
import com.tourzj.sms.req.ReportReq;
import com.tourzj.sms.rsp.ReportRsp;

public class SmsEngineSoapBindingImpl implements com.tourzj.sms.SmsEngine {
	private static final Log log = LogFactory
			.getLog(SmsEngineSoapBindingImpl.class);

	public com.tourzj.sms.rsp.ReportRsp report(com.tourzj.sms.req.ReportReq req)
			throws java.rmi.RemoteException {
		log.info("收到 sms-report");
		log.debug(req);
		// 转送消息给对方
		ReportRsp rsp = new ReportRsp();
		rsp.setResultCode(Constants.SUCCESS);
		if (!notifyReport(req)) {
			log.error("转送sms-report消息给对方失败");
			rsp.setResultCode(Constants.ERROR);
		}
		log.debug(rsp);
		return rsp;
	}

	public com.tourzj.sms.rsp.DeliverRsp deliver(
			com.tourzj.sms.req.DeliverReq req) throws java.rmi.RemoteException {
		log.info("收到sms-deliver");
		log.debug(req);
		com.tourzj.sms.rsp.DeliverRsp rsp = new com.tourzj.sms.rsp.DeliverRsp();
		rsp.setResultCode(Constants.SUCCESS);
		// 转送消息给对方
		if (!notifyDeliver(req)) {
			log.error("转送sms-deliver消息给对方失败");
			rsp.setResultCode(Constants.ERROR);
		}
		log.debug(rsp);
		return rsp;
	}

	public com.tourzj.sms.rsp.SubmitRsp submit(com.tourzj.sms.req.SubmitReq req)
			throws java.rmi.RemoteException {
		log.info("收到sms-submit");
		log.debug(req);
		String sendid = req.getSendid();
		String msgContent = req.getMsgContent();
		String recipient = req.getRecipient();

		boolean ret = SmsManager.submit(sendid, msgContent, recipient);
		com.tourzj.sms.rsp.SubmitRsp rsp = new com.tourzj.sms.rsp.SubmitRsp();
		rsp.setResultCode(Constants.SUCCESS);
		if (!SmsManager.submit(sendid, msgContent, recipient)) {
			log.error("发送sms-submit消息给短信网关失败");
			rsp.setResultCode(Constants.ERROR);
		}
		log.debug(rsp);
		return rsp;
	}

	private boolean notifyDeliver(DeliverReq req) {
		return false;
	}

	private boolean notifyReport(ReportReq req) {
		return false;
	}

}
