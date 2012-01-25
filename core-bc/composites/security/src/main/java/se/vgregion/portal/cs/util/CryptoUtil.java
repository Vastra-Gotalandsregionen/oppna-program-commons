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

package se.vgregion.portal.cs.util;

import java.security.GeneralSecurityException;

/**
 * Interface for encryption and decryption.
 * 
 * @author <a href="mailto:david.rosell@redpill-linpro.com">David Rosell</a>
 */
public interface CryptoUtil {

    /**
     * Encrypt a value.
     * 
     * @param value
     *            to be encrypted
     * @return encrypted value
     * @throws GeneralSecurityException
     *             Java security execption
     */
    String encrypt(String value) throws GeneralSecurityException;

    /**
     * Decrypt a value.
     * 
     * @param value
     *            to be decrypted
     * @return decrypted value
     * @throws GeneralSecurityException
     *             Java security execption
     */
    String decrypt(String value) throws GeneralSecurityException;
}
