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

import junit.framework.TestCase;

public class LdapServiceTest extends TestCase {

    static final String base = "ou=anv,O=VGR";
    static final String uid = "(uid=plite1)";
    static final String search = "(uid=andr*)";

    LdapService serviceClient;
    HashMap addAttributes;
    HashMap modifyAttributes;

    public void testDummy() {
        // TODO: Mock away external dependencies
        assertTrue(true);
    }
    /*
     * @Override
     * 
     * @Before public void setUp() throws Exception {
     * 
     * Properties p = new Properties(); p.load(ClassLoader.getSystemResourceAsStream("ldap.properties"));
     * serviceClient = new LdapServiceImpl(p);
     * 
     * addAttributes = new HashMap(); addAttributes.put("cn", "Test"); addAttributes.put("sn", "Test");
     * addAttributes.put("vgr-id", "testtest"); addAttributes.put("mail", "testtest@vgr.se");
     * addAttributes.put("displayname", "Test Test"); addAttributes.put("givenname", "Test Test");
     * addAttributes.put("userPassword", "password");
     * 
     * modifyAttributes = new HashMap(); modifyAttributes.put("mail", "test@test.com");
     * 
     * }
     * 
     * @Override
     * 
     * @After public void tearDown() throws Exception { }
     * 
     * @Test public void testGetLdapUser() throws Exception { // make sure that we get an entry for this uid
     * LdapUser entry = serviceClient.getLdapUser(base, uid); assert (entry != null); }
     * 
     * @Test public void testGetLdapUserWithAttributes() throws Exception { // make sure that we get an entry for
     * this uid LdapUser entry = serviceClient.getLdapUser(base, uid, new String[] { "hsaPersonIdentityNumber" });
     * assert (entry != null); }
     * 
     * @Test public void testGetLdapUserAttribute() throws Exception { // make sure that we can access the values
     * of the attribute uid LdapUser entry = serviceClient.getLdapUser(base, uid); String value =
     * entry.getAttributeValue("uid"); assertEquals(value, "plite1"); }
     * 
     * @Test public void testGetAllAttributes() throws Exception { // make sure that we can access the values of
     * the attribute uid LdapUser entry = serviceClient.getLdapUser(base, uid); Map<String, ArrayList<String>>
     * allAttrs = entry.getAttributes(); assert (allAttrs != null); allAttrs.get("objectClass").size(); }
     * 
     * @Test public void testGetAttributeValueFromAllAttributes() throws Exception { // make sure that we can
     * access the values of the attribute uid LdapUser entry = serviceClient.getLdapUser(base, uid); Map<String,
     * ArrayList<String>> allAttrs = entry.getAttributes(); assert (allAttrs.get("objectClass").size() > 1); }
     * 
     * @Test public void testSearch() throws Exception { // make sure that we can find more than one result
     * LdapUser[] entries = serviceClient.search("", search); assert (entries.length > 1); }
     * 
     * @Test public void testSearchWithAttributes() throws Exception { // make sure that we can find more than one
     * result LdapUser[] entries = serviceClient.search("", search, new String[] { "hsaPersonIdentityNumber" });
     * assert (entries.length > 1); }
     * 
     * @Test public void testAdd() throws Exception { // add an entry to the catalog Boolean result =
     * serviceClient.addLdapUser("uid=testtest,ou=personal,ou=anv,O=VGR", addAttributes); assert (result); }
     * 
     * @Test public void testModify() throws Exception { // modify the entry we just added LdapUser entry =
     * serviceClient.getLdapUser(base, "(uid=testtest)"); Boolean result = serviceClient.modifyLdapUser(entry,
     * modifyAttributes); // check that the value actually stuck entry = serviceClient.getLdapUser(base,
     * "(uid=testtest)"); String value = entry.getAttributeValue("mail"); assertEquals(value, "test@test.com"); }
     * 
     * @Test public void testDelete() throws Exception { // modify the entry we just added LdapUser entry =
     * serviceClient.getLdapUser(base, "(uid=testtest)"); Boolean result = serviceClient.deleteLdapUser(entry);
     * assert (result); }
     */
}
