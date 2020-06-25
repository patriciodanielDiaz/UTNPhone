package com.utn.UTN.Phone.config;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Crypt {

    public static String cryptWithMD5 (String pass){

        MessageDigest m = null;
        try {
            m = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] data = pass.getBytes();
        m.update(data,0,data.length);
        BigInteger i = new BigInteger(1, m.digest());
        String hashtext = i.toString(16);
        while (hashtext.length() < 32) {
            hashtext = "0" + hashtext;
        }
        return hashtext;
    }
}
