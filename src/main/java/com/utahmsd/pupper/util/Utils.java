package com.utahmsd.pupper.util;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.TimeZone;

public class Utils {

    private static final int ITERATIONS = 65536;
    private static final int KEY_LENGTH = 256;
    private static final String ALGORITHM_NAME = "PBKDF2WithHmacSHA1";

    public static String generateHash(String plainText, byte[] saltBytes) {
        KeySpec keySpec = new PBEKeySpec(plainText.toCharArray(), saltBytes, ITERATIONS, KEY_LENGTH);
        SecretKeyFactory keyFactory = null;
        byte[] hash = null;
        try {
            keyFactory = SecretKeyFactory.getInstance(ALGORITHM_NAME);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            hash = keyFactory.generateSecret(keySpec).getEncoded();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(hash);
    }

    public static byte[] concatBytes(byte[] plainBytes, byte[] saltBytes) {
        byte[] appendedBytes = new byte[plainBytes.length + saltBytes.length];
        System.arraycopy(plainBytes,0,appendedBytes,0         ,plainBytes.length);
        System.arraycopy(saltBytes,0,appendedBytes, plainBytes.length, saltBytes.length);
        for (byte b: appendedBytes) {
            System.out.println(b);
        }
        return appendedBytes;
    }

    public static byte[] generateSaltBytes(int length) {
        SecureRandom secureRandom = new SecureRandom();
        byte [] bytes = new byte[length];
        secureRandom.nextBytes(bytes);
        return bytes;
    }

    public static String generateSaltString(int len) {
        Base64.Encoder encoder = Base64.getEncoder();
        byte[] saltBytes = generateSaltBytes(len);
        return encoder.encodeToString(saltBytes);
    }

    public static String getIsoFormatDate (final Date date) {
        final String ISO_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS zzz";
        final SimpleDateFormat formatter = new SimpleDateFormat(ISO_DATE_FORMAT);
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        return formatter.format(date);
    }


}
