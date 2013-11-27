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

package se.vgregion.ldapservice.search;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created with IntelliJ IDEA.
 * User: Claes Lundahl
 * Date: 2013-08-14
 * Time: 09:52
 * Used to give information about what field (name) in a ldap database the one in a class represents. Used in beans
 * that are passed to the LdapService.find method.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ExplicitLdapName {

    /**
     * Used to give information about what field (name) in a ldap database the one in a class represents. Used in beans
     * that are passed to the LdapService.find method.
     *
     */
    String value();

}
