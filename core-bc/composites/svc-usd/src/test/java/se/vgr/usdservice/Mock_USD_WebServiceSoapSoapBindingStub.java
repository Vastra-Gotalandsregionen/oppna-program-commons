/**
 * ﻿Copyright 2009 Västra Götalandsregionen
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

package se.vgr.usdservice;

import java.rmi.RemoteException;

import javax.xml.rpc.holders.StringHolder;

import org.apache.axis.AxisFault;

import com.ca.www.UnicenterServicePlus.ServiceDesk.USD_WebServiceSoapSoapBindingStub;

public class Mock_USD_WebServiceSoapSoapBindingStub extends USD_WebServiceSoapSoapBindingStub {

    public Mock_USD_WebServiceSoapSoapBindingStub() throws AxisFault {
        super();
        // TODO Auto-generated constructor stub
    }

    @Override
    public int login(java.lang.String username, java.lang.String password) throws java.rmi.RemoteException {
        return 0;
    }

    @Override
    public String getHandleForUserid(int sid, String userID) throws RemoteException {
        return userID;

    }

    @Override
    public String createRequest(int sid, String creatorHandle, String[] attrVals, String[] propertyValues,
            String template, String[] attributes, StringHolder newRequestHandle, StringHolder newRequestNumber)
            throws RemoteException {
        return "ok";

    }

}
