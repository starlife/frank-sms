/**
 * Request.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.tourzj.mms.req;

public class Request  implements java.io.Serializable {
    private java.lang.String sendId;

    public Request() {
    }

    public Request(
           java.lang.String sendId) {
           this.sendId = sendId;
    }


    /**
     * Gets the sendId value for this Request.
     * 
     * @return sendId
     */
    public java.lang.String getSendId() {
        return sendId;
    }


    /**
     * Sets the sendId value for this Request.
     * 
     * @param sendId
     */
    public void setSendId(java.lang.String sendId) {
        this.sendId = sendId;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Request)) return false;
        Request other = (Request) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.sendId==null && other.getSendId()==null) || 
             (this.sendId!=null &&
              this.sendId.equals(other.getSendId())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getSendId() != null) {
            _hashCode += getSendId().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Request.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://req.mms.tourzj.com", "Request"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sendId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://req.mms.tourzj.com", "sendId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }
    
    public String toString()
    {
    	return getClass().getName()+"{sendid:" + getSendId()+"}";
    }

}
