/**
 * MmsEngineService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.tourzj.mms;

public interface MmsEngineService extends javax.xml.rpc.Service {
    public java.lang.String getMmsEngineAddress();

    public com.tourzj.mms.MmsEngine getMmsEngine() throws javax.xml.rpc.ServiceException;

    public com.tourzj.mms.MmsEngine getMmsEngine(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
