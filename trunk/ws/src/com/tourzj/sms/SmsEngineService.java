/**
 * SmsEngineService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.tourzj.sms;

public interface SmsEngineService extends javax.xml.rpc.Service {
    public java.lang.String getSmsEngineAddress();

    public com.tourzj.sms.SmsEngine getSmsEngine() throws javax.xml.rpc.ServiceException;

    public com.tourzj.sms.SmsEngine getSmsEngine(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
