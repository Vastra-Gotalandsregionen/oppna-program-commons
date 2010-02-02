/**
 * Copyright 2009 V�stra G�talandsregionen
 *
 *   This library is free software; you can redistribute it and/or modify
 *   it under the terms of version 2.1 of the GNU Lesser General Public
 *   License as published by the Free Software Foundation.
 *
 *   This library is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public
 *   License along with this library; if not, write to the
 *   Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 *   Boston, MA 02111-1307  USA
 */
/**
 * USD_WebServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ca.www.UnicenterServicePlus.ServiceDesk;

public class USD_WebServiceLocator extends org.apache.axis.client.Service implements com.ca.www.UnicenterServicePlus.ServiceDesk.USD_WebService {

    public USD_WebServiceLocator() {
    }


    public USD_WebServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public USD_WebServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for USD_WebServiceSoap
    private java.lang.String USD_WebServiceSoap_address = "http://vgrusd.vgregion.se:8080/axis/services/USD_R11_WebService";

    public java.lang.String getUSD_WebServiceSoapAddress() {
        return USD_WebServiceSoap_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String USD_WebServiceSoapWSDDServiceName = "USD_WebServiceSoap";

    public java.lang.String getUSD_WebServiceSoapWSDDServiceName() {
        return USD_WebServiceSoapWSDDServiceName;
    }

    public void setUSD_WebServiceSoapWSDDServiceName(java.lang.String name) {
        USD_WebServiceSoapWSDDServiceName = name;
    }

    public com.ca.www.UnicenterServicePlus.ServiceDesk.USD_WebServiceSoap getUSD_WebServiceSoap() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(USD_WebServiceSoap_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getUSD_WebServiceSoap(endpoint);
    }

    public com.ca.www.UnicenterServicePlus.ServiceDesk.USD_WebServiceSoap getUSD_WebServiceSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.ca.www.UnicenterServicePlus.ServiceDesk.USD_WebServiceSoapSoapBindingStub _stub = new com.ca.www.UnicenterServicePlus.ServiceDesk.USD_WebServiceSoapSoapBindingStub(portAddress, this);
            _stub.setPortName(getUSD_WebServiceSoapWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setUSD_WebServiceSoapEndpointAddress(java.lang.String address) {
        USD_WebServiceSoap_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.ca.www.UnicenterServicePlus.ServiceDesk.USD_WebServiceSoap.class.isAssignableFrom(serviceEndpointInterface)) {
                com.ca.www.UnicenterServicePlus.ServiceDesk.USD_WebServiceSoapSoapBindingStub _stub = new com.ca.www.UnicenterServicePlus.ServiceDesk.USD_WebServiceSoapSoapBindingStub(new java.net.URL(USD_WebServiceSoap_address), this);
                _stub.setPortName(getUSD_WebServiceSoapWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("USD_WebServiceSoap".equals(inputPortName)) {
            return getUSD_WebServiceSoap();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://www.ca.com/UnicenterServicePlus/ServiceDesk", "USD_WebService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://www.ca.com/UnicenterServicePlus/ServiceDesk", "USD_WebServiceSoap"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("USD_WebServiceSoap".equals(portName)) {
            setUSD_WebServiceSoapEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
