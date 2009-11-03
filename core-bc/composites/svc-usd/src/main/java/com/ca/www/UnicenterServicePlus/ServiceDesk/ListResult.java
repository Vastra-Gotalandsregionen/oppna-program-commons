/**
 * ListResult.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ca.www.UnicenterServicePlus.ServiceDesk;

public class ListResult  implements java.io.Serializable {
    private int listHandle;

    private int listLength;

    public ListResult() {
    }

    public ListResult(
           int listHandle,
           int listLength) {
           this.listHandle = listHandle;
           this.listLength = listLength;
    }


    /**
     * Gets the listHandle value for this ListResult.
     * 
     * @return listHandle
     */
    public int getListHandle() {
        return listHandle;
    }


    /**
     * Sets the listHandle value for this ListResult.
     * 
     * @param listHandle
     */
    public void setListHandle(int listHandle) {
        this.listHandle = listHandle;
    }


    /**
     * Gets the listLength value for this ListResult.
     * 
     * @return listLength
     */
    public int getListLength() {
        return listLength;
    }


    /**
     * Sets the listLength value for this ListResult.
     * 
     * @param listLength
     */
    public void setListLength(int listLength) {
        this.listLength = listLength;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ListResult)) return false;
        ListResult other = (ListResult) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.listHandle == other.getListHandle() &&
            this.listLength == other.getListLength();
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
        _hashCode += getListHandle();
        _hashCode += getListLength();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ListResult.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.ca.com/UnicenterServicePlus/ServiceDesk", "ListResult"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("listHandle");
        elemField.setXmlName(new javax.xml.namespace.QName("", "listHandle"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("listLength");
        elemField.setXmlName(new javax.xml.namespace.QName("", "listLength"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
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

}
