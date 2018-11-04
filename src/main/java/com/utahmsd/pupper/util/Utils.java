package com.utahmsd.pupper.util;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
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

    /**
     * Convenience method that returns a scaled instance of the
     * provided {@code File}.
     *
     * @param fileToScale the original image file
     * @param targetWidth the desired width of the scaled instance,
     *    in pixels
     * @param targetHeight the desired height of the scaled instance,
     *    in pixels
     * @param hint one of the rendering hints that corresponds to
     *    {@code RenderingHints.KEY_INTERPOLATION} (e.g.
     *    {@code RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR},
     *    {@code RenderingHints.VALUE_INTERPOLATION_BILINEAR},
     *    {@code RenderingHints.VALUE_INTERPOLATION_BICUBIC})
     * @param higherQuality if true, this method will use a multi-step
     *    scaling technique that provides higher quality than the usual
     *    one-step technique (only useful in downscaling cases, where
     *    {@code targetWidth} or {@code targetHeight} is
     *    smaller than the original dimensions, and generally only when
     *    the {@code BILINEAR} hint is specified)
     * @return a scaled version of the original {@code File}
     */
    public static File scaleImageFile(File fileToScale, int targetWidth, int targetHeight, Object hint, boolean higherQuality) {
        hint = RenderingHints.VALUE_RENDER_DEFAULT;
        BufferedImage img = null;
        try {
            img = ImageIO.read(fileToScale);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int type = (img.getTransparency() == Transparency.OPAQUE) ?
                BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
        BufferedImage ret = img;
        int h = higherQuality ? img.getHeight() : targetHeight;
        int w = higherQuality ? img.getWidth() : targetWidth;
        do {
            if (higherQuality && w > targetWidth) {
                w /= 2;
                if (w < targetWidth) {
                    w = targetWidth;
                }
            }

            if (higherQuality && h > targetHeight) {
                h /= 2;
                if (h < targetHeight) {
                    h = targetHeight;
                }
            }

            BufferedImage tmp = new BufferedImage(w, h, type);
            Graphics2D g2 = tmp.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, hint);
            g2.drawImage(ret, 0, 0, w, h, null);
            g2.dispose();

            ret = tmp;
        } while (w != targetWidth || h != targetHeight);

        File outputfile = new File(fileToScale.getName() + "-scaled.jpg");
        try {
            ImageIO.write(ret, "jpg", outputfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outputfile;
    }




}
