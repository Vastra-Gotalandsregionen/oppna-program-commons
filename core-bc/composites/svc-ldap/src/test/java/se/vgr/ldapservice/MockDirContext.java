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

package se.vgr.ldapservice;

import java.util.HashMap;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

public class MockDirContext extends InitialDirContext {
    private HashMap hm = new HashMap();

    public MockDirContext() throws NamingException {
        // super();
        NamingEnumeration e = new MockNamingEnumeration(LdapServiceTest.uid);
        hm.put(LdapServiceTest.uid, e);
        hm.put(LdapServiceTest.search, e);
        hm.put("(uid=testtest)", e);
    }

    @Override
    public NamingEnumeration<SearchResult> search(String name, String filter, SearchControls cons)
            throws NamingException {
        NamingEnumeration e = new MockNamingEnumeration(filter);
        // NamingEnumeration<SearchResult> result = (NamingEnumeration<SearchResult>) hm.get(filter);

        return e;
    }

    @Override
    public void modifyAttributes(String name, int mod_op, Attributes attrs) throws NamingException {

    }

    @Override
    public DirContext createSubcontext(String arg0, Attributes arg1) throws NamingException {
        return null;
    }

    @Override
    public void destroySubcontext(String name) throws NamingException {

    }

}
