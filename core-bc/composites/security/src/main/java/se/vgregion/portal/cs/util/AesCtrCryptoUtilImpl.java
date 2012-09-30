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

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;
import java.util.Scanner;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * This implementation uses the AES algorithm with CTR block cipher mode to provide secure encryption.
 *
 * @author <a href="mailto:david.rosell@redpill-linpro.com">David Rosell</a>
 * @author <a href="mailto:patrik.bergstrom@knowit.se">Patrik Bergström</a>
 */
public class AesCtrCryptoUtilImpl implements CryptoUtil {

    private static final String AES = "AES";

    private static final String ALGORITHM = "AES/CTR/PKCS5Padding";

    private static final String CHARSET_NAME = "UTF-8";

    private static final int KEY_SIZE = 128;

    private File keyFile;
    private final SecretKeySpec sks;
    private final Cipher encryptCipher;
    private final Cipher decryptCipher;
    private Lock encryptLock = new ReentrantLock();
    private Lock decryptLock = new ReentrantLock();

    /**
     * Constructor.
     *
     * @param keyFile file where the key is stored
     */
    public AesCtrCryptoUtilImpl(File keyFile) {
        if (!keyFile.exists()) {
            throw new IllegalStateException("The encryption file [" + keyFile.getAbsolutePath() + "] must exist.");
        }

        this.keyFile = keyFile;
        try {
            sks = getSecretKeySpec();
            encryptCipher = Cipher.getInstance(ALGORITHM);
            decryptCipher = Cipher.getInstance(ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (NoSuchPaddingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Encrypt a value using the key from keyfile. If the keyfile is not found then a new one is created with a new
     * key.
     *
     * @param value value to be encrypted
     * @return Encrypted value
     * @throws java.security.GeneralSecurityException
     *          security exception
     */
    @Override
    public String encrypt(String value) throws GeneralSecurityException {

        // Initialization vector - it is randomly generated and stored together with the encrypted password since
        // it does not need to be kept secret. E.g. it makes so that equal passwords will not be encrypted
        // equally and thus makes it harder for a potential attacker.
        byte[] encrypted;
        byte[] iv;
        try {
            encryptLock.lock();
            encryptCipher.init(Cipher.ENCRYPT_MODE, sks);
            iv = encryptCipher.getParameters().getParameterSpec(IvParameterSpec.class).getIV();
            encrypted = encryptCipher.doFinal(value.getBytes(CHARSET_NAME));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } finally {
            encryptLock.unlock();
        }

        String encryptedHex = byteArrayToHexString(encrypted);
        String ivHex = byteArrayToHexString(iv);

        return encryptedHex + "-" + ivHex;
    }

    /**
     * Decrypts a value.
     *
     * @param value value to be decrypted
     * @return decrypted value
     * @throws GeneralSecurityException decrypt failed
     * @throws IllegalArgumentException if the <code>value</code> does not contain two parts divided by a dash (-)
     *                                  sign.
     */
    @Override
    public String decrypt(String value) throws GeneralSecurityException {
        String[] encPasswordPlusIv = value.split("-");

        if (encPasswordPlusIv.length != 2) {
            throw new IllegalArgumentException("The value must consist of a String with two parts separated by "
                    + " a dash (-) sign.");
        }

        String encPasswordHex = encPasswordPlusIv[0];
        String ivHex = encPasswordPlusIv[1];

        byte[] decrypted;
        try {
            decryptLock.lock();
            decryptCipher.init(Cipher.DECRYPT_MODE, sks, new IvParameterSpec(hexStringToByteArray(ivHex)));
            byte[] cipherBytes = hexStringToByteArray(encPasswordHex);
            decrypted = decryptCipher.doFinal(cipherBytes);
        } finally {
            decryptLock.unlock();
        }

        try {
            return new String(decrypted, CHARSET_NAME);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Won't happen");
        }
    }

    private SecretKeySpec getSecretKeySpec() throws NoSuchAlgorithmException {
        byte[] key = readKeyFile();
        SecretKeySpec sks = new SecretKeySpec(key, AES);
        return sks;
    }

    private byte[] readKeyFile() {
        Scanner scanner;
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

    private static String byteArrayToHexString(byte[] b) {
        StringBuffer sb = new StringBuffer(b.length * 2);
        final int ffHex = 0xff;
        final int n16 = 16;
        for (int i = 0; i < b.length; i++) {
            int v = b[i] & ffHex;
            if (v < n16) {
                sb.append('0');
            }
            sb.append(Integer.toHexString(v));
        }
        return sb.toString().toUpperCase(Locale.US);
    }

    private static byte[] hexStringToByteArray(String s) {
        final int radix = 16;
        byte[] b = new byte[s.length() / 2];
        for (int i = 0; i < b.length; i++) {
            int index = i * 2;
            int v = Integer.parseInt(s.substring(index, index + 2), radix);
            b[i] = (byte) v;
        }
        return b;
    }

    public static void createKeyFile(File keyFile) {
        FileWriter fw = null;
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance(AES);
            keyGen.init(KEY_SIZE);
            SecretKey sk = keyGen.generateKey();
            fw = null;
            fw = new FileWriter(keyFile);
            fw.write(byteArrayToHexString(sk.getEncoded()));
            fw.flush();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        } finally {
            try {
                if (fw != null) {
                    fw.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
