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
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.SearchResult;

public class MockNamingEnumeration implements NamingEnumeration {
    Enumeration e;

    public MockNamingEnumeration(String uid) {
        List l = new ArrayList();
        Attributes a = new BasicAttributes();
        a.put("uid", "andcu1");
        a.put("mail", "test@test.com");
        l.add(new SearchResult(uid, null, a));
        e = Collections.enumeration(l);

    }

    public void close() throws NamingException {
        throw new UnsupportedOperationException("TODO: Implement this method");
    }

    public boolean hasMore() throws NamingException {
        return e.hasMoreElements();
    }

    public Object next() throws NamingException {
        return e.nextElement();
    }

    public boolean hasMoreElements() {
        throw new UnsupportedOperationException("TODO: Implement this method");
    }

    public Object nextElement() {
        throw new UnsupportedOperationException("TODO: Implement this method");
    }
}
