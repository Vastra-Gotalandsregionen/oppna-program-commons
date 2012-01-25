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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import java.io.File;
import java.security.GeneralSecurityException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * This action do that and that, if it has something special it is.
 * 
 * @author <a href="mailto:david.rosell@redpill-linpro.com">David Rosell</a>
 */
public class CryptoUtilImplTest {
    CryptoUtilImpl cryptoUtil = new CryptoUtilImpl();
    File testKeyFile;

    @Before
    public void setUp() {
        testKeyFile = new File("./testKeyFile.key");
        cryptoUtil.setKeyFile(testKeyFile);
    }

    @After
    public void tearDown() {
        testKeyFile.delete();
    }

    @Test
    public void testEncryptDecrypt() throws GeneralSecurityException {
        String value = "test-encrypt-string";

        // encrypt
        String encrypted = cryptoUtil.encrypt(value);
        assertFalse("value not encrypyed", value.equals(encrypted));

        // decrypt
        String decrypted = cryptoUtil.decrypt(encrypted);
        assertEquals(value, decrypted);

        // encrypt again
        String encrypted2 = cryptoUtil.encrypt(value);
        assertEquals("not reencrypyed", encrypted, encrypted2);
    }

    @Test
    public void testEncrypt2() throws GeneralSecurityException {
        String value = "test-encrypt-string";

        // encrypt
        String encrypted = cryptoUtil.encrypt(value);
        assertFalse("value not encrypted", value.equals(encrypted));

        // encrypt again
        String encrypted2 = cryptoUtil.encrypt(value);
        assertEquals("not reencrypted", encrypted, encrypted2);
    }

    @Test
    /**
     * This is a test to verify that error is thrown when no key file is present.
     */
    public void testDecryptNoKeyfile() throws GeneralSecurityException {
        String value = "test-encrypt-string";

        // decrypt
        try {
            String encrypted = cryptoUtil.decrypt(value);
            fail("exception should be thrown");
        } catch (Exception ex) {
            // OK
        }
    }
}
