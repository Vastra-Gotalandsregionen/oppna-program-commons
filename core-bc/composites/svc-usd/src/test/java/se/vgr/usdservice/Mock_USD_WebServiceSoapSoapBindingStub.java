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
