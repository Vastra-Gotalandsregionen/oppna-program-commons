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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;
import java.util.Properties;
import java.util.Scanner;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Component;

/**
 * This implementation uses AES encryption with ECB block mode.
 *
 * @author <a href="mailto:david.rosell@redpill-linpro.com">David Rosell</a>
 */
@Deprecated //ECB block mode is too weak
@Component
public class CryptoUtilImpl implements CryptoUtil {

    private static final String AES = "AES";

    private static final int KEY_SIZE = 128;

    private File keyFile;

    public void setKeyFile(File keyFile) {
        this.keyFile = keyFile;
    }

    /**
     * Encrypt a value and generate a keyfile. if the keyfile is not found then a new one is created
     * 
     * @param value
     *            - value to be encrypted
     * @throws GeneralSecurityException
     *             - security exception
     * @return Encrypted value
     */
    @Override
    public String encrypt(String value) throws GeneralSecurityException {
        if (!keyFile.exists()) {
            KeyGenerator keyGen = KeyGenerator.getInstance(AES);
            keyGen.init(KEY_SIZE);
            SecretKey sk = keyGen.generateKey();
            FileWriter fw = null;
            try {
                fw = new FileWriter(keyFile);
                fw.write(byteArrayToHexString(sk.getEncoded()));
                fw.flush();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    fw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        SecretKeySpec sks = getSecretKeySpec();

        Cipher cipher = Cipher.getInstance(AES);
        cipher.init(Cipher.ENCRYPT_MODE, sks, cipher.getParameters());
        byte[] encrypted = cipher.doFinal(value.getBytes());
        return byteArrayToHexString(encrypted);
    }

    /**
     * decrypt a value.
     * 
     * @param value
     *            - value to be decrypted
     * @throws GeneralSecurityException
     *             - decrypt failed
     * @return decrypted value
     */
    @Override
    public String decrypt(String value) throws GeneralSecurityException {
        SecretKeySpec sks = getSecretKeySpec();
        Cipher cipher = Cipher.getInstance(AES);
        cipher.init(Cipher.DECRYPT_MODE, sks);
        byte[] cipherBytes = hexStringToByteArray(value);
        byte[] decrypted = cipher.doFinal(cipherBytes);

        return new String(decrypted);
    }

    private SecretKeySpec getSecretKeySpec() throws NoSuchAlgorithmException {
        byte[] key = readKeyFile();
        SecretKeySpec sks = new SecretKeySpec(key, AES);
        return sks;
    }

    private byte[] readKeyFile() {
        Scanner scanner = null;
        try {
            scanner = new Scanner(keyFile).useDelimiter("\\Z");
            String keyValue = scanner.next();
            scanner.close();
            return hexStringToByteArray(keyValue);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return new byte[0];
        }
    }

    private String byteArrayToHexString(byte[] b) {
        final int ffHex = 0xff;
        final int n16 = 16;
        StringBuffer sb = new StringBuffer(b.length * 2);
        for (int i = 0; i < b.length; i++) {
            int v = b[i] & ffHex;
            if (v < n16) {
                sb.append('0');
            }
            sb.append(Integer.toHexString(v));
        }
        return sb.toString().toUpperCase(Locale.US);
    }

    private byte[] hexStringToByteArray(String s) {
        final int radix = 16;
        byte[] b = new byte[s.length() / 2];
        for (int i = 0; i < b.length; i++) {
            int index = i * 2;
            int v = Integer.parseInt(s.substring(index, index + 2), radix);
            b[i] = (byte) v;
        }
        return b;
    }

    /**
     * Main method used for creating initial key file.
     * 
     * @param args
     *            - not used
     */
    public static void main(String[] args) {
        final String keyFile = "./howto.key";
        final String pwdFile = "./howto.properties";

        CryptoUtilImpl cryptoUtils = new CryptoUtilImpl();
        cryptoUtils.setKeyFile(new File(keyFile));

        String clearPwd = "my_cleartext_pwd";

        Properties p1 = new Properties();
        Writer w = null;
        try {
            p1.put("user", "liferay");
            String encryptedPwd = cryptoUtils.encrypt(clearPwd);
            p1.put("pwd", encryptedPwd);
            w = new FileWriter(pwdFile);
            p1.store(w, "");
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (w != null) {
                try {
                    w.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        // ==================
        Properties p2 = new Properties();
        Reader r = null;
        try {
            r = new FileReader(pwdFile);
            p2.load(r);
            String encryptedPwd = p2.getProperty("pwd");
            System.out.println(encryptedPwd);
            System.out.println(cryptoUtils.decrypt(encryptedPwd));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } finally {
            if (r != null) {
                try {
                    r.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
