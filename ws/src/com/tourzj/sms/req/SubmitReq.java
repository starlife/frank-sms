/**
 * SubmitReq.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.tourzj.sms.req;

public class SubmitReq  extends com.tourzj.sms.req.Request  implements java.io.Serializable {
    private java.lang.String msgContent;

    private java.lang.String recipient;

    public SubmitReq() {
    }

    public SubmitReq(
           java.lang.String sendid,
           java.lang.String msgContent,
           java.lang.String recipient) {
        super(
            sendid);
        this.msgContent = msgContent;
        this.recipient = recipient;
    }


    /**
     * Gets the msgContent value for this SubmitReq.
     * 
     * @return msgContent
     */
    public java.lang.String getMsgContent() {
        return msgContent;
    }


    /**
     * Sets the msgContent value for this SubmitReq.
     * 
     * @param msgContent
     */
    public void setMsgContent(java.lang.String msgContent) {
        this.msgContent = msgContent;
    }


    /**
     * Gets the recipient value for this SubmitReq.
     * 
     * @return recipient
     */
    public java.lang.String getRecipient() {
        return recipient;
    }


    /**
     * Sets the recipient value for this SubmitReq.
     * 
     * @param recipient
     */
    public void setRecipient(java.lang.String recipient) {
        this.recipient = recipient;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof SubmitReq)) return false;
        SubmitReq other = (SubmitReq) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.msgContent==null && other.getMsgContent()==null) || 
             (this.msgContent!=null &&
              this.msgContent.equals(other.getMsgContent()))) &&
            ((this.recipient==null && other.getRecipient()==null) || 
             (this.recipient!=null &&
              this.recipient.equals(other.getRecipient())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = super.hashCode();
        if (getMsgContent() != null) {
            _hashCode += getMsgContent().hashCode();
        }
        if (getRecipient() != null) {
            _hashCode += getRecipient().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(SubmitReq.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://req.sms.tourzj.com", "SubmitReq"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("msgContent");
        elemField.setXmlName(new javax.xml.namespace.QName("http://req.sms.tourzj.com", "msgContent"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("recipient");
        elemField.setXmlName(new javax.xml.namespace.QName("http://req.sms.tourzj.com", "recipient"));
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
    	return getClass().getName()+"{sendid:" + getSendid() + "|msgContent:" + this.getMsgContent() + "|recipient:" + this.getRecipient() + "}";
    }

}
