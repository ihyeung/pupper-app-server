package com.utahmsd.pupper.util;

import com.amazonaws.util.StringUtils;
import org.imgscalr.Scalr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.TimeZone;

import static com.utahmsd.pupper.client.AmazonAwsClient.MAX_IMAGE_BYTES;
import static com.utahmsd.pupper.util.Constants.ISO_DATE_FORMAT;
import static com.utahmsd.pupper.util.Constants.MATCH_RESULT_TIMESTAMP_FORMAT;

public class Utils {

    private static final Logger LOGGER = LoggerFactory.getLogger(Utils.class);

    public static File getScaledFile(File file) throws IOException {
        LOGGER.error("Error: file being uploaded exceeds maximum allowable bytes size: {}", file.length());
        long scaleFactor = file.length()/MAX_IMAGE_BYTES;
        BufferedImage image = ImageIO.read(file);
        File outputfile = new File(file.getName() + "-scaled.jpg");
        int height = image.getHeight();
        int width = image.getWidth();
        if (scaleFactor > 2) {
            BufferedImage scaledImage =
                    Scalr.resize(image, Scalr.Method.BALANCED, width/2, height/2);
            LOGGER.info(String.format("Scaled dimensions: %d w x %d h", width/2, height/2));

            try {
                ImageIO.write(scaledImage, "jpg", outputfile);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return outputfile;
        }
        ImageIO.write(image, "jpg", outputfile);

        return outputfile;
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
