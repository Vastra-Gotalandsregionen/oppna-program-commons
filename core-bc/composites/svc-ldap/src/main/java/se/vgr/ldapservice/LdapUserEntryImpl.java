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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.naming.NamingEnumeration;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.SearchResult;

class LdapUserEntryImpl implements LdapUser {
  private String _dn;

  Map _attrs;

  public LdapUserEntryImpl(String base, SearchResult res) {
    _dn = res.getName();
    if (base != null && base.length() != 0) {
      _dn = _dn + "," + base;
    }
    _attrs = attrsToHashtable(res.getAttributes());
  }

  public Attributes getAttributes(String[] modifyAttrs) {
    return mapToAttrs(modifyAttrs, _attrs);
  }

  /**
   * @param rdn
   */
  public LdapUserEntryImpl(String rdn) {
    _dn = rdn;
    _attrs = new HashMap();
  }

  private Map attrsToHashtable(Attributes attrs) {
    try {
      Map res = new HashMap();
      NamingEnumeration attrIter = attrs.getAll();
      while (attrIter.hasMore()) {
        Attribute oneAttr = (Attribute) attrIter.next();
        String attrId = oneAttr.getID();
        NamingEnumeration attrValuesIter = oneAttr.getAll();
        List attrValues = new ArrayList();
        while (attrValuesIter.hasMore()) {
          Object oneValue = attrValuesIter.next();
          attrValues.add(oneValue);
        }
        if (attrValues.isEmpty()) {
          attrValues.add(null);
        }
        res.put(attrId, attrValues);
      }
      return res;
    } catch (Exception e) {
      throw new RuntimeException("Parsing attrs failed", e);
    }
  }

  public String getDN() {
    return _dn;
  }

  public String getAttributeValue(String attrName) {
    String[] vals = getAttributeValues(attrName);
    if (vals.length > 0) {
      return vals[0];
    }
    return null;
  }

  public String[] getAttributeValues(String attrName) {
    List vals = (List) _attrs.get(attrName);
    if (vals == null) {
      vals = new ArrayList();
    }
    String[] res = new String[vals.size()];
    for (int i = 0; i < res.length; i++) {
      res[i] = (String) vals.get(i);
    }

    return res;
  }

  public static String dumpAttrMap(Map m) {
    StringBuffer buf = new StringBuffer(256);
    Iterator it = m.keySet().iterator();
    while (it.hasNext()) {
      String key = (String) it.next();
      List values = (List) m.get(key);
      buf.append("   " + key + ": |");
      Iterator it2 = values.iterator();
      while (it2.hasNext()) {
        String oneVal = (String) it2.next();
        buf.append(oneVal + "|");
      }
      buf.append("\n");
    }
    return buf.toString();
  }

  @Override
  public String toString() {
    StringBuffer buf = new StringBuffer(200);
    buf.append("" + _dn + "\n");
    buf.append(dumpAttrMap(_attrs));
    buf.append("\n");
    return buf.toString();
  }

  /*
   * (non-Javadoc)
   * 
   * @see simple_jndiclient.Entry#clearAttribute(java.lang.String)
   */
  public void clearAttribute(String attr) {
    _attrs.remove(attr);

  }

  /*
   * (non-Javadoc)
   * 
   * @see simple_jndiclient.Entry#setAttributeValue(java.lang.String, java.lang.Object)
   */
  public void setAttributeValue(String attr, Object value) {
    List val = new ArrayList();
    val.add(value);
    _attrs.put(attr, val);

  }

  /*
   * (non-Javadoc)
   * 
   * @see simple_jndiclient.Entry#addAttributeValue(java.lang.String, java.lang.Object)
   */
  public void addAttributeValue(String attr, Object value) {
    List currValues = (List) _attrs.get(attr);
    if (currValues == null) {
      currValues = new ArrayList();
      _attrs.put(attr, currValues);
    }
    currValues.add(value);
  }

  /*
   * (non-Javadoc)
   * 
   * @see simple_jndiclient.Entry#setAttributeValue(java.lang.String, java.lang.Object[])
   */
  public void setAttributeValue(String attr, Object[] values) {
    List vals = new ArrayList();

    for (int i = 0; i < values.length; i++) {
      vals.add(values[i]);
    }
    _attrs.put(attr, vals);

  }

  public Map getAttributes() {
    return _attrs;
  }

  private static Attributes mapToAttrs(String[] attrNames, Map m) {
    BasicAttributes attrs = new BasicAttributes();
    Iterator it = m.keySet().iterator();
    while (it.hasNext()) {
      String key = (String) it.next();
      if (arrayContains(attrNames, key)) {
        List values = (List) m.get(key);
        BasicAttribute oneAttr = new BasicAttribute(key);
        Iterator it2 = values.iterator();
        while (it2.hasNext()) {

          Object oneVal = it2.next();
          oneAttr.add(oneVal);
        }
        attrs.put(oneAttr);
      }
    }

    return attrs;
  }

  public static String dumpSearchRes(LdapUser[] res) {
    StringBuffer buf = new StringBuffer(256);
    for (int i = 0; i < res.length; i++) {
      buf.append(res[i]);
    }
    return buf.toString();
  }

  private static boolean arrayContains(String[] a, String val) {
    for (int i = 0; i < a.length; i++) {
      if (a[i] == null) {
        if (val == null) {
          return true;
        }
      } else {
        if (a[i].equals(val)) {
          return true;
        }
      }

    }
    return false;
  }

}