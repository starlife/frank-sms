/**
 * SmsEngine.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.tourzj.sms;

public interface SmsEngine extends java.rmi.Remote {
    public com.tourzj.sms.rsp.ReportRsp report(com.tourzj.sms.req.ReportReq req) throws java.rmi.RemoteException;
    public com.tourzj.sms.rsp.DeliverRsp deliver(com.tourzj.sms.req.DeliverReq req) throws java.rmi.RemoteException;
    public com.tourzj.sms.rsp.SubmitRsp submit(com.tourzj.sms.req.SubmitReq req) throws java.rmi.RemoteException;
}
