/**
 * SmsEngineSoapBindingImpl.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.tourzj.sms;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tempuri.LTSoapStub;

import com.tourzj.common.Env;
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
		String signName=req.getSignName();
		//这里重置短信,增加签名 add by 2013-2-27
		if(signName==null||signName.trim().length()==0)
		{
			msgContent += "【浙江省旅游局】";
		}else
		{
			msgContent += signName;
		}
		//end by 2013-2-27
		
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
		try {
			log.debug("转送sms-deliver消息给对方");
			LTSoapStub stub = new LTSoapStub(new URL(getWsAddress()),
					null);
			stub.setTimeout(10000);
			stub.notifyLTSms(req.getRecipient(), req.getMsgContent());
		} catch (MalformedURLException ex) {
			log.error(null, ex);
			return false;
		} catch (RemoteException ex) {
			log.error(null, ex);
			return false;
		}
		// return rsp != null && rsp.getResultCode() == Constants.SUCCESS;
		return true;
	}

	private boolean notifyReport(ReportReq req) {
		return false;
	}

	private String getWsAddress() {
		String address = Env.getEnv().getString("ws_smsaddress");
		if (address == null) {
			log.error("取tourzj地址ws_smsaddress值为空");
			address = "";
		}
		return address;
	}
	
	private String getDefaultSignName() {
		String sginName = Env.getEnv().getString("sign_name");
		if (sginName == null) {
			log.error("取签名sign_name值为空");
			sginName = "";
		}
		return sginName;
	}

	public static void main(String[] args) {
		SmsEngineSoapBindingImpl impl = new SmsEngineSoapBindingImpl();
		DeliverReq req = new DeliverReq();
		req.setSendid("111");
		req.setRecipient("13003664740");
		req.setMsgContent("msgcontent");
		System.out.println(impl.notifyDeliver(req));
	}

}
