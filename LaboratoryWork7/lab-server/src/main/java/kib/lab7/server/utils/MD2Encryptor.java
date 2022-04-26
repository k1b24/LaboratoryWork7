package kib.lab7.server.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD2Encryptor {

    public String encrypt(String s) throws NoSuchAlgorithmException {
        final int maxLen = 32;
        final int radix = 16;
        MessageDigest md = MessageDigest.getInstance("MD2");
        byte[] messageDigest = md.digest(s.getBytes());
        BigInteger no = new BigInteger(1, messageDigest);
        StringBuilder hashtext = new StringBuilder(no.toString(radix));
        while (hashtext.length() < maxLen) {
            hashtext.insert(0, "0");
        }
        return hashtext.toString();
    }

}
