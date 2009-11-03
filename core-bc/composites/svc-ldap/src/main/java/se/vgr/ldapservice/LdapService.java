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

import java.util.HashMap;
import java.util.Properties;

public interface LdapService {

    public abstract LdapUser[] search(String base, String filter);

    public abstract LdapUser[] search(String base, String filter, String[] attributes);

    public abstract LdapUser getLdapUser(String base, String filter);

    public abstract LdapUser getLdapUser(String base, String filter, String[] attributes);

    public abstract Properties getProperties();

    public abstract boolean addLdapUser(String context, HashMap<String, String> attributes);

    public abstract boolean modifyLdapUser(LdapUser e, HashMap<String, String> modifyAttributes);

    public abstract boolean deleteLdapUser(LdapUser e);

}