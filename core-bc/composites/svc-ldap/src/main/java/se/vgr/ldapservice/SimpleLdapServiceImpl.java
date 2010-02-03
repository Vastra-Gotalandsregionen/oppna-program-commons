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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DistinguishedName;
import org.springframework.ldap.core.simple.ParameterizedContextMapper;
import org.springframework.ldap.core.simple.SimpleLdapTemplate;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;

public class SimpleLdapServiceImpl extends LdapServiceImpl {

    private SimpleLdapTemplate ldapTemplate;
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleLdapServiceImpl.class);

    public SimpleLdapServiceImpl(SimpleLdapTemplate ldapTemplate) {
        this.ldapTemplate = ldapTemplate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LdapUser[] search(String base, String filter) {
        ParameterizedContextMapper<SimpleLdapUser> ldapUserMapper = new SimpleLdapServiceImpl.LdapUserMapper();
        List<SimpleLdapUser> ldapUsers = ldapTemplate.search(base, filter, ldapUserMapper);
        return ldapUsers.toArray(new SimpleLdapUser[] {});
    }

    @Override
    public boolean addLdapUser(String context, HashMap<String, String> attributes) {
        throw new UnsupportedOperationException("Not implemented in simple ldap service, use LdapServiceImpl.");
    }

    @Override
    public boolean deleteLdapUser(LdapUser e) {
        throw new UnsupportedOperationException("Not implemented in simple ldap service, use LdapServiceImpl.");
    }

    @Override
    public LdapUser getLdapUser(String base, String filter, String[] attributes) {
        throw new UnsupportedOperationException("Not implemented in simple ldap service, use LdapServiceImpl.");
    }

    @Override
    public LdapUser getLdapUser(String base, String filter) {
        throw new UnsupportedOperationException("Not implemented in simple ldap service, use LdapServiceImpl.");
    }

    /**
     * Returns an ldap user by its id.
     * 
     * @param uid
     * @return
     */
    public LdapUser getLdapUserByUid(String uid) {
        ParameterizedContextMapper<SimpleLdapUser> ldapUserMapper = new SimpleLdapServiceImpl.LdapUserMapper();
        SimpleLdapUser ldapUser = null;
        AndFilter filter = new AndFilter();
        filter.and(new EqualsFilter("objectclass", "person")).and(new EqualsFilter("uid", uid));
        try {
            ldapUser = ldapTemplate.searchForObject(DistinguishedName.EMPTY_PATH, filter.encode(), ldapUserMapper);
        } catch (EmptyResultDataAccessException e) {
            // User was not found.
        }
        return ldapUser;
    }

    @Override
    public Properties getProperties() {
        throw new UnsupportedOperationException("Not implemented in simple ldap service, use LdapServiceImpl.");
    }

    @Override
    public boolean modifyLdapUser(LdapUser e, HashMap<String, String> modifyAttributes) {
        throw new UnsupportedOperationException("Not implemented in simple ldap service, use LdapServiceImpl.");
    }

    @Override
    public LdapUser[] search(String base, String filter, String[] attributes) {
        throw new UnsupportedOperationException("Not implemented in simple ldap service, use LdapServiceImpl.");
    }

    /**
     * Used to map an LDAP entry into a SimpleLdapUser.
     */
    public static final class LdapUserMapper implements ParameterizedContextMapper<SimpleLdapUser> {
        public SimpleLdapUser mapFromContext(Object ctx) {
            DirContextAdapter adapter = (DirContextAdapter) ctx;
            SimpleLdapUser simpleLdapUser = new SimpleLdapUser(adapter.getDn().toString());
            try {
                Attributes cnAttributes = adapter.getAttributes("cn");
                Attributes mailAttributes = adapter.getAttributes("mail");
                Attributes telephoneNumberAttributes = adapter.getAttributes("telephoneNumber");
                if (cnAttributes.size() > 0) {
                    simpleLdapUser.setCn(cnAttributes.getAll().next().get().toString());
                }
                if (mailAttributes.size() > 0) {
                    simpleLdapUser.setMail(mailAttributes.getAll().next().get().toString());
                }
                if (telephoneNumberAttributes.size() > 0) {
                    simpleLdapUser.setTelephoneNumber(telephoneNumberAttributes.getAll().next().get().toString());
                }

                // Set all attributes in order to conform with old low-level style of LdapUserEntryImpl.
                NamingEnumeration<? extends Attribute> allAttributes = adapter.getAttributes().getAll();
                ArrayList<? extends Attribute> allAttributesList = Collections.list(allAttributes);
                for (Attribute attribute : allAttributesList) {
                    if (attribute.size() > 0) {
                        simpleLdapUser.setAttributeValue(attribute.getID(), attribute.getAll());
                    }
                }

            } catch (NamingException e) {
                LOGGER.error("Error when retrieving attributes for ldap user: ", e);
            }
            return simpleLdapUser;
        }
    }
}
