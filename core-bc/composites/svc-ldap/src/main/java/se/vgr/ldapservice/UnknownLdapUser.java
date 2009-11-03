/**
 * Copyright 2009 VŠstra Gštalandsregionen
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
package se.vgr.ldapservice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UnknownLdapUser implements LdapUser {

    public void addAttributeValue(String attr, Object value) {
        throw new UnsupportedOperationException("TODO: Implement this method");
    }

    public void clearAttribute(String attr) {
        throw new UnsupportedOperationException("TODO: Implement this method");
    }

    public String getAttributeValue(String attrName) {
        return "unknown";
    }

    public String[] getAttributeValues(String attrName) {
        return new String[] { "unknown" };
    }

    public String getDN() {
        return "unknownDN";
    }

    public Map get_attrs() {
        return new HashMap();
    }

    public void setAttributeValue(String attr, Object value) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public void setAttributeValue(String attr, Object[] values) {
        throw new UnsupportedOperationException("TODO: Implement this method");
    }

    public Map<String, ArrayList<String>> getAttributes() {
        throw new UnsupportedOperationException("TODO: Implement this method");
    }
}
