package com.utahmsd.pupper.util;

import io.vavr.control.Try;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Utils {

    public static Try<byte[]> computeHash(String plainText, byte[] salt) {
        return Try.of(()->plainText.getBytes("UTF-8")).map(plain -> concatBytes(plain, salt));
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

    public static byte[] generateSalt(int length) {
        SecureRandom secureRandom = new SecureRandom();
        byte [] bytes = new byte[length];
        secureRandom.nextBytes(bytes);
        return bytes;
    }

    public static String getIsoFormatDate (final Date date) {
        final String ISO_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS zzz";
        final SimpleDateFormat formatter = new SimpleDateFormat(ISO_DATE_FORMAT);
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        return formatter.format(date);
    }


}
