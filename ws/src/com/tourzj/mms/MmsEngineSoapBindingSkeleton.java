/**
 * MmsEngineSoapBindingSkeleton.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.tourzj.mms;

public class MmsEngineSoapBindingSkeleton implements com.tourzj.mms.MmsEngine, org.apache.axis.wsdl.Skeleton {
    private com.tourzj.mms.MmsEngine impl;
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
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://mms.tourzj.com", "req"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://req.mms.tourzj.com", "DeliverReq"), com.tourzj.mms.req.DeliverReq.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("deliver", _params, new javax.xml.namespace.QName("http://mms.tourzj.com", "deliverReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://rsp.mms.tourzj.com", "DeliverRsp"));
        _oper.setElementQName(new javax.xml.namespace.QName("http://mms.tourzj.com", "deliver"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("deliver") == null) {
            _myOperations.put("deliver", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("deliver")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://mms.tourzj.com", "req"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://req.mms.tourzj.com", "SubmitReq"), com.tourzj.mms.req.SubmitReq.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("submit", _params, new javax.xml.namespace.QName("http://mms.tourzj.com", "submitReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://rsp.mms.tourzj.com", "SubmitRsp"));
        _oper.setElementQName(new javax.xml.namespace.QName("http://mms.tourzj.com", "submit"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("submit") == null) {
            _myOperations.put("submit", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("submit")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://mms.tourzj.com", "req"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://req.mms.tourzj.com", "DeliverReportReq"), com.tourzj.mms.req.DeliverReportReq.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("deliverReport", _params, new javax.xml.namespace.QName("http://mms.tourzj.com", "deliverReportReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://rsp.mms.tourzj.com", "DeliverReportRsp"));
        _oper.setElementQName(new javax.xml.namespace.QName("http://mms.tourzj.com", "deliverReport"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("deliverReport") == null) {
            _myOperations.put("deliverReport", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("deliverReport")).add(_oper);
    }

    public MmsEngineSoapBindingSkeleton() {
        this.impl = new com.tourzj.mms.MmsEngineSoapBindingImpl();
    }

    public MmsEngineSoapBindingSkeleton(com.tourzj.mms.MmsEngine impl) {
        this.impl = impl;
    }
    public com.tourzj.mms.rsp.DeliverRsp deliver(com.tourzj.mms.req.DeliverReq req) throws java.rmi.RemoteException
    {
        com.tourzj.mms.rsp.DeliverRsp ret = impl.deliver(req);
        return ret;
    }

    public com.tourzj.mms.rsp.SubmitRsp submit(com.tourzj.mms.req.SubmitReq req) throws java.rmi.RemoteException
    {
        com.tourzj.mms.rsp.SubmitRsp ret = impl.submit(req);
        return ret;
    }

    public com.tourzj.mms.rsp.DeliverReportRsp deliverReport(com.tourzj.mms.req.DeliverReportReq req) throws java.rmi.RemoteException
    {
        com.tourzj.mms.rsp.DeliverReportRsp ret = impl.deliverReport(req);
        return ret;
    }

}
