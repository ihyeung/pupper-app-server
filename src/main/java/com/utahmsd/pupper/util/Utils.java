package com.utahmsd.pupper.util;

import com.amazonaws.util.StringUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Utils {

    /**
     * Generates a location string from a user zipcode for displaying on the profile card.
     * @param zipcode
     * @return
     */
    public static String getProfileLocationByZipcode(String zipcode) {
        if (StringUtils.isNullOrEmpty(zipcode)) {
            return null;
        }
        return null;
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
