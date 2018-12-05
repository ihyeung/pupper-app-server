package com.utahmsd.pupper.util;

import com.amazonaws.util.StringUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.TimeZone;

import static com.utahmsd.pupper.util.Constants.ISO_DATE_FORMAT;

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
        //TODO: Implement this
        return null;
    }

    public static String getIsoFormatTimestampFromDate(final Date date, String timezone) {
//        final String ISO_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS zzz";
        final SimpleDateFormat formatter = new SimpleDateFormat(ISO_DATE_FORMAT);
        formatter.setTimeZone(
                TimeZone.getTimeZone(StringUtils.isNullOrEmpty(timezone) ? "UTC" : timezone));
        return formatter.format(date);
    }

    /**
     * Generates a string representing the current ISO 8601 formatted timestamp in UTC.
     * @return
     */
    public static String getIsoFormatTimestamp (Instant instant) {
        final SimpleDateFormat f = new SimpleDateFormat(ISO_DATE_FORMAT);
        f.setTimeZone(TimeZone.getTimeZone("UTC"));
        return f.format(Date.from(instant));
    }

    /**
     * Convenience method that returns a scaled instance of the
     * provided {@code File}.
     *
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
