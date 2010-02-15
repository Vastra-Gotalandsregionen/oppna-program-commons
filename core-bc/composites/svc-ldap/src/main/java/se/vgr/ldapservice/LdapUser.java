/**
 * Copyright 2010 Västra Götalandsregionen
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
 *
 */

package se.vgr.ldapservice;

import java.util.ArrayList;
import java.util.Map;

public interface LdapUser {

    String getDN();

    String getAttributeValue(String attrName);

    String[] getAttributeValues(String attrName);

    Map<String, ArrayList<String>> getAttributes();

    void clearAttribute(String attr);

    void setAttributeValue(String attr, Object value);

    void addAttributeValue(String attr, Object value);

    void setAttributeValue(String attr, Object[] values);

}
