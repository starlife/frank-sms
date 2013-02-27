/**
 * MmsEngineSoapBindingImpl.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.tourzj.mms;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

import javax.servlet.ServletContext;

import org.apache.axis.MessageContext;
import org.apache.axis.transport.http.AxisServlet;
import org.apache.axis.transport.http.HTTPConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tempuri.LTSoapStub;

import com.tourzj.common.Env;
import com.tourzj.common.constant.Constants;
import com.tourzj.mms.manager.MmsEncode;
import com.tourzj.mms.manager.MmsManager;
import com.tourzj.mms.manager.MmsXmlMake;
import com.tourzj.mms.req.DeliverReportReq;
import com.tourzj.mms.req.DeliverReq;
import com.tourzj.mms.rsp.DeliverReportRsp;
import com.unicom.mm7.bean.UMms;

public class MmsEngineSoapBindingImpl implements com.tourzj.mms.MmsEngine {
	private static final Log log = LogFactory
			.getLog(MmsEngineSoapBindingImpl.class);

	public com.tourzj.mms.rsp.SubmitRsp submit(com.tourzj.mms.req.SubmitReq req)
			throws java.rmi.RemoteException {
		log.info("收到 submit");
		log.debug(req);
		String sendId = req.getSendId();
		// http://interface.tourzj.gov.cn/MobilePaper/default.aspx
		/*
		 * String address = Env.getEnv().getString("tourzj_address"); if
		 * (address == null) { log.error("取tourzj地址tourzj_address值为空"); address
		 * = "http://interface.tourzj.gov.cn/MobilePaper/default.aspx"; } //
		 * 根据url地址去取彩信内容，组装成Umms对象，然后发送给彩信网关 String url = address + "?sendid=" +
		 * sendId;
		 */
		String url = sendId;
		UMms mms = MmsEncode.makeMms(url);
		boolean ret = MmsManager.submit(mms);
		com.tourzj.mms.rsp.SubmitRsp rsp = new com.tourzj.mms.rsp.SubmitRsp();
		if (ret) {
			rsp.setResultCode(Constants.SUCCESS);
		} else {
			rsp.setResultCode(Constants.ERROR);
		}
		log.debug(rsp);
		return rsp;
	}

	public com.tourzj.mms.rsp.DeliverReportRsp deliverReport(
			com.tourzj.mms.req.DeliverReportReq req)
			throws java.rmi.RemoteException {
		log.info("收到 deliverReport");
		log.debug(req);
		// 转送消息给对方
		boolean b = notifyDeliverReport(req);
		DeliverReportRsp rsp = new DeliverReportRsp();
		if (b) {
			rsp.setResultCode(Constants.SUCCESS);
		} else {
			rsp.setResultCode(Constants.ERROR);
		}
		log.debug(rsp);
		return rsp;
	}

	public com.tourzj.mms.rsp.DeliverRsp deliver(
			com.tourzj.mms.req.DeliverReq req) throws java.rmi.RemoteException {
		log.info("收到 deliver");
		log.debug(req);
		// 1.取彩信
		String sendId = req.getSendId();
		UMms mms = MmsManager.getMms(sendId);
		com.tourzj.mms.rsp.DeliverRsp rsp = new com.tourzj.mms.rsp.DeliverRsp();
		rsp.setResultCode(Constants.ERROR);
		if (mms == null) {
			log.debug("取彩信失败");
			return rsp;
		}
		// 2. 把彩信文件转换为xml形式
		if (!MmsXmlMake
				.make(mms, getUploadFilePath(), getXMLFilePath(), sendId)) {
			log.debug("把彩信文件转换为xml失败");
			return rsp;
		}
		// 3.转送消息给对方
		if (!notifyDeliver(req)) {
			log.debug("转送消息给对方失败");
			return rsp;
		}
		rsp.setResultCode(Constants.SUCCESS);
		log.debug(rsp);
		return rsp;
	}

	private String getUploadFilePath() {
		MessageContext ctx = MessageContext.getCurrentContext();
		// 获得AxisServlet对象
		AxisServlet as = (AxisServlet) ctx
				.getProperty(HTTPConstants.MC_HTTP_SERVLET);
		// 获得servletcontext
		ServletContext sc = as.getServletContext();
		String uploadDir = sc.getRealPath(Constants.UPLOAD_FILE_DIR);
		return uploadDir;
	}

	private String getXMLFilePath() {
		MessageContext ctx = MessageContext.getCurrentContext();
		// 获得AxisServlet对象
		AxisServlet as = (AxisServlet) ctx
				.getProperty(HTTPConstants.MC_HTTP_SERVLET);
		// 获得servletcontext
		ServletContext sc = as.getServletContext();
		String uploadDir = sc.getRealPath(Constants.XML_FILE_DIR);
		return uploadDir;
	}

	public boolean notifyDeliverReport(DeliverReportReq req) {
		try {
			log.debug("DeliverReport notify");
			LTSoapStub stub = new LTSoapStub(new URL(getWsAddress()), null);
			stub.setTimeout(1000);
			stub.notifyMmsDeliveryReport(req);
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

	private boolean notifyDeliver(DeliverReq req) {
		/*
		 * DeliverRsp rsp = null; try { log.debug("Deliver notify");
		 * resetSendid(req); MmsEngineSoapBindingStub stub = new
		 * MmsEngineSoapBindingStub( new URL(getWsAddress()), null);
		 * stub.setTimeout(1000); rsp = stub.deliver(req); } catch
		 * (MalformedURLException ex) { log.error(null, ex); } catch
		 * (RemoteException ex) { log.error(null, ex); } return rsp != null &&
		 * rsp.getResultCode() == Constants.SUCCESS;
		 */
		return false;
	}

	/*
	 * private void resetSendid(DeliverReq req) { String address =
	 * Env.getEnv().getString("default_address"); if (address == null) {
	 * log.error("取default地址default_address值为空"); address =
	 * "http://60.191.70.231/ws/index.do"; } //
	 * 根据url地址去取彩信内容，组装成Umms对象，然后发送给彩信网关 // http://60.191.70.231/ws/ String url
	 * = address + "?sendid=" + req.getSendId(); req.setSendId(url); }
	 */

	private String getWsAddress() {
		String address = Env.getEnv().getString("ws_address");
		if (address == null) {
			log.error("取tourzj地址ws_address值为空");
			address = "";
		}
		return address;
	}
}
