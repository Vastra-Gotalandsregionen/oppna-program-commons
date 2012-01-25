package se.vgregion.portal.cs.util;

import java.security.GeneralSecurityException;

public class CryptoUtilMock implements CryptoUtil {
    @Override
    public String encrypt(String value) throws GeneralSecurityException {
        return new StringBuffer(value).reverse().toString();
    }

    @Override
    public String decrypt(String value) throws GeneralSecurityException {
        return new StringBuffer(value).reverse().toString();
    }
}