/**
 * SmsEngineSoapBindingSkeleton.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.tourzj.sms;

public class SmsEngineSoapBindingSkeleton implements com.tourzj.sms.SmsEngine, org.apache.axis.wsdl.Skeleton {
    private com.tourzj.sms.SmsEngine impl;
    private static java.util.Map _myOperations = new java.util.Hashtable();
    private static java.util.Collection _myOperationsList = new java.util.ArrayList();

    /**
    * Returns List of OperationDesc objects with this name
    */
    public static java.util.List getOperationDescByName(java.lang.String methodName) {
        return (java.util.List)_myOperations.get(methodName);
    }

    /**
    * Returns Collection of OperationDescs
    */
    public static java.util.Collection getOperationDescs() {
        return _myOperationsList;
    }

    static {
        org.apache.axis.description.OperationDesc _oper;
        org.apache.axis.description.FaultDesc _fault;
        org.apache.axis.description.ParameterDesc [] _params;
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://sms.tourzj.com", "req"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://req.sms.tourzj.com", "ReportReq"), com.tourzj.sms.req.ReportReq.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("report", _params, new javax.xml.namespace.QName("http://sms.tourzj.com", "reportReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://rsp.sms.tourzj.com", "ReportRsp"));
        _oper.setElementQName(new javax.xml.namespace.QName("http://sms.tourzj.com", "report"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("report") == null) {
            _myOperations.put("report", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("report")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://sms.tourzj.com", "req"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://req.sms.tourzj.com", "DeliverReq"), com.tourzj.sms.req.DeliverReq.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("deliver", _params, new javax.xml.namespace.QName("http://sms.tourzj.com", "deliverReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://rsp.sms.tourzj.com", "DeliverRsp"));
        _oper.setElementQName(new javax.xml.namespace.QName("http://sms.tourzj.com", "deliver"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("deliver") == null) {
            _myOperations.put("deliver", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("deliver")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://sms.tourzj.com", "req"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://req.sms.tourzj.com", "SubmitReq"), com.tourzj.sms.req.SubmitReq.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("submit", _params, new javax.xml.namespace.QName("http://sms.tourzj.com", "submitReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://rsp.sms.tourzj.com", "SubmitRsp"));
        _oper.setElementQName(new javax.xml.namespace.QName("http://sms.tourzj.com", "submit"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("submit") == null) {
            _myOperations.put("submit", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("submit")).add(_oper);
    }

    public SmsEngineSoapBindingSkeleton() {
        this.impl = new com.tourzj.sms.SmsEngineSoapBindingImpl();
    }

    public SmsEngineSoapBindingSkeleton(com.tourzj.sms.SmsEngine impl) {
        this.impl = impl;
    }
    public com.tourzj.sms.rsp.ReportRsp report(com.tourzj.sms.req.ReportReq req) throws java.rmi.RemoteException
    {
        com.tourzj.sms.rsp.ReportRsp ret = impl.report(req);
        return ret;
    }

    public com.tourzj.sms.rsp.DeliverRsp deliver(com.tourzj.sms.req.DeliverReq req) throws java.rmi.RemoteException
    {
        com.tourzj.sms.rsp.DeliverRsp ret = impl.deliver(req);
        return ret;
    }

    public com.tourzj.sms.rsp.SubmitRsp submit(com.tourzj.sms.req.SubmitReq req) throws java.rmi.RemoteException
    {
        com.tourzj.sms.rsp.SubmitRsp ret = impl.submit(req);
        return ret;
    }

}
