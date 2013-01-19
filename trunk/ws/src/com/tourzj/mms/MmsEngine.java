/**
 * MmsEngine.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.tourzj.mms;

public interface MmsEngine extends java.rmi.Remote {
    public com.tourzj.mms.rsp.DeliverRsp deliver(com.tourzj.mms.req.DeliverReq req) throws java.rmi.RemoteException;
    public com.tourzj.mms.rsp.SubmitRsp submit(com.tourzj.mms.req.SubmitReq req) throws java.rmi.RemoteException;
    public com.tourzj.mms.rsp.DeliverReportRsp deliverReport(com.tourzj.mms.req.DeliverReportReq req) throws java.rmi.RemoteException;
}
